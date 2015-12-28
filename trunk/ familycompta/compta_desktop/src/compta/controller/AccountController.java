package compta.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import compta.AccountException;
import compta.ihm.MainFrame;
import compta.model.Account;
import compta.model.AccountChangedEvent;
import compta.model.AccountListener;
import compta.model.budget.BudgetRecord;
import compta.model.budget.BudgetRecordRecurrence;
import compta.model.history.HistoryRecord;

public class AccountController implements AccountListener {

	private static final Logger logger = Logger
			.getLogger(AccountController.class);

	protected static final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd");

	private Account account = new Account();

	private MainFrame mainFrame = null;

	public AccountController() {
		mainFrame = new MainFrame(this);
		account.addAccountListener(this);
	}

	/**
	 * 
	 * @return the comptes (never null)
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * 
	 * @param fileName
	 * @throws AccountException
	 */
	public void openAccount(String fileName) throws AccountException {
		this.openAccount(new File(fileName));
	}

	/**
	 * 
	 * @param fileName
	 * @throws AccountException
	 */
	public void saveAccount(String fileName) throws AccountException {
		this.saveAccount(new File(fileName));
	}

	/**
	 * 
	 * @param file
	 * @throws AccountException
	 */
	public void openAccount(File file) throws AccountException {
		if (FileUtils.XML_FILE_FILTER.accept(file)) {
			openAccountXML(file);
		}

		// else if (FileUtils.CSV_FILE_FILTER.accept(file)) {
		// openAccountCSV(file);
		// }
		else {
			logger.error("Not a valid file " + file.getAbsolutePath());
			throw new AccountException("Not a valid file "
					+ file.getAbsolutePath());
		}
		account.addAccountListener(this);
		this.accountOpened(file);
	}

	/**
	 * 
	 * @param file
	 * @throws AccountException
	 */
	public void saveAccount(File file) throws AccountException {
		if (FileUtils.XML_FILE_FILTER.accept(file)) {
			saveAccountXML(file);
		}

		// else if (FileUtils.CSV_FILE_FILTER.accept(file)) {
		//			
		// saveAccountCSV(file);
		// }
		else {
			logger.error("Not a valid file " + file.getAbsolutePath());
			throw new AccountException("Not a valid file "
					+ file.getAbsolutePath());
		}
	}

	/**
	 * 
	 * @param file
	 * @throws AccountException
	 * @Deprecated use openAccountXml instead
	 */
	@Deprecated
	public void openAccountCSV(File file) throws AccountException {

		if (file == null) {
			logger.error("Null file to open");
			throw new AccountException("Null file to open");
		}

		try {
			CSVDocument csvFile = new CSVDocument(file);
			Account accountTmp = new Account();
			int state = 0;

			for (int i = 0; i < csvFile.getRowsCount(); i++) {
				// skip the empty rows
				if (!csvFile.isRowEmpty(i)) {
					switch (state) {
					case 0:
						// current provision
						float currentProvision = Float.valueOf(csvFile
								.get(i, 0));
						accountTmp.setCurrentProvision(currentProvision);
						state = 1;

						// date of current provision
						accountTmp.setCurrentProvisionDate(dateFormat
								.parse(csvFile.get(i, 1)));
						state = 1;
						break;
					case 1:
						// budget record
						boolean active = Boolean
								.parseBoolean(csvFile.get(i, 0));
						String category = csvFile.get(i, 1);
						String label = csvFile.get(i, 2);
						float amount = Float.valueOf(csvFile.get(i, 3));
						Date startDate = dateFormat.parse(csvFile.get(i, 4));
						Date endDate = dateFormat.parse(csvFile.get(i, 5));
						BudgetRecordRecurrence rec = BudgetRecordRecurrence
								.parse(csvFile.get(i, 6));

						accountTmp
								.addBudgetRecord(new BudgetRecord(category,
										label, amount, startDate, endDate, rec,
										active));
						break;
					default:
						logger.fatal("State cannot be > 1");
						throw new AccountException("State cannot be > 1");
					}

				}
			}

			account = accountTmp;
			accountTmp = null;

		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new AccountException(e);
		}

		logger.info("file" + file.getAbsolutePath() + " successfully loaded.");

	}

	/**
	 * 
	 * @param _file
	 * @throws AccountException
	 * @Deprecated use saveAccountXml instead
	 */
	@Deprecated
	public void saveAccountCSV(File file) throws AccountException {
		CSVDocument csvFile = new CSVDocument();

		int lineIndex = 0;
		// current provision
		csvFile.set(lineIndex, 0, Float.toString(account
				.getCurrentProvisionAmount()));

		// current provision date
		csvFile.set(lineIndex, 1, dateFormat.format(account
				.getCurrentProvisionDate().getTime()));
		lineIndex++;

		// budget Records
		BudgetRecord[] bRecords = account.getAllBudgetRecords();
		for (int i = 0; i < bRecords.length; i++) {
			csvFile.set(lineIndex, 0, Boolean.toString(bRecords[i].isActive()));
			csvFile.set(lineIndex, 1, bRecords[i].getCategory());
			csvFile.set(lineIndex, 2, bRecords[i].getLabel());
			csvFile.set(lineIndex, 3, Float.toString(bRecords[i].getAmount()));
			csvFile.set(lineIndex, 4, dateFormat.format(bRecords[i]
					.getStartDate().getTime()));
			csvFile.set(lineIndex, 5, dateFormat.format(bRecords[i]
					.getEndDate().getTime()));
			csvFile.set(lineIndex, 6, bRecords[i].getReccurrence()
					.getRecField()
					+ "-" + bRecords[i].getReccurrence().getRecValue());

			lineIndex++;
		}

		try {
			csvFile.printToFile(file);
		} catch (IOException e) {
			logger.error(e);
			throw new AccountException(e);
		}

		logger.info("Saved to file" + file.getAbsolutePath());
	}

	/**
	 * 
	 * @param xmlFile
	 * @throws AccountException
	 */
	@SuppressWarnings("unchecked")
	public void openAccountXML(File xmlFile) throws AccountException {
		Document document;
		try {
			document = new SAXBuilder().build(xmlFile);

			Account comptesTmp = new Account();
			Element elmtRoot = document.getRootElement();

			// 1 - solde courant
			Element elmtSoldeCourant = elmtRoot
					.getChild(XmlUtils.CURRENT_PROVISION_XMLTAG);
			comptesTmp.setCurrentProvision(Float.parseFloat(elmtSoldeCourant
					.getText()));

			// 2 - date solde courant
			Element elmtDateSoldeCourant = elmtRoot
					.getChild(XmlUtils.CURRENT_PROVISION_DATE_XMLTAG);

			comptesTmp.setCurrentProvisionDate(dateFormat
					.parse(elmtDateSoldeCourant.getText()));

			// 3 - history records
			Element elmtHistoryRecordList = elmtRoot
					.getChild(XmlUtils.HISTORY_RECORD_LIST_XMLTAG);
			if (elmtHistoryRecordList != null) {
				List historeyRecordList = elmtHistoryRecordList
						.getChildren(XmlUtils.HISTORY_RECORD_XMLTAG);
				ListIterator<Element> historyRecordIterator = historeyRecordList
						.listIterator();

				while (historyRecordIterator.hasNext()) {
					Element elmtHistoryRecord = historyRecordIterator.next();

					// 3.1 date
					Element elmtRecordDate = elmtHistoryRecord
							.getChild(XmlUtils.HISTORY_RECORD_DATE_XMLTAG);
					Date recordDate = dateFormat
							.parse(elmtRecordDate.getText());

					// 3.2 provision
					Element elmtProvision = elmtHistoryRecord
							.getChild(XmlUtils.HISTORY_RECORD_PROVISION_XMLTAG);
					float provision = Float.parseFloat(elmtProvision.getText());

					comptesTmp.addHistoryRecord(new HistoryRecord(recordDate,
							provision));

				}
			}

			// 4 - budget records
			Element elmtBudgetRecordList = elmtRoot
					.getChild(XmlUtils.BUDGET_RECORD_LIST_XMLTAG);
			List budgetRecordList = elmtBudgetRecordList
					.getChildren(XmlUtils.BUDGET_RECORD_XMLTAG);
			ListIterator<Element> budgetRecordIterator = budgetRecordList
					.listIterator();

			while (budgetRecordIterator.hasNext()) {
				Element elmtBudgetRecord = budgetRecordIterator.next();

				// 4.1 active
				Element elmtActive = elmtBudgetRecord
						.getChild(XmlUtils.ACTIVE_XMLTAG);
				boolean active = Boolean.parseBoolean(elmtActive.getText());

				// 4.2 category
				Element elmtCategory = elmtBudgetRecord
						.getChild(XmlUtils.CATEGORY_XMLTAG);
				String category = "";
				if (elmtCategory != null) {
					// optionnal statement
					category = elmtCategory.getText();
				}

				// 4.3 label
				Element elmtLabel = elmtBudgetRecord
						.getChild(XmlUtils.LABEL_XMLTAG);
				String label = elmtLabel.getText();

				// 4.4 amount
				Element elmtAmount = elmtBudgetRecord
						.getChild(XmlUtils.AMOUNT_XMLTAG);
				float amount = Float.parseFloat(elmtAmount.getText());

				// 4.5 start date
				Element elmtStartDate = elmtBudgetRecord
						.getChild(XmlUtils.STARTDATE_XMLTAG);
				Date startDate = dateFormat.parse(elmtStartDate.getText());

				// 4.6 end date
				Element elmtEndDate = elmtBudgetRecord
						.getChild(XmlUtils.ENDDATE_XMLTAG);
				Date endDate = dateFormat.parse(elmtEndDate.getText());

				// 4.7 recurrence
				Element elmtRec = elmtBudgetRecord
						.getChild(XmlUtils.REC_XMLTAG);

				// 4.7.1 rec field
				int recField = Integer.parseInt(elmtRec.getChild(
						XmlUtils.RECFIELD_XMLTAG).getText());

				// 4.7.2 rec value
				int recValue = Integer.parseInt(elmtRec.getChild(
						XmlUtils.RECVALUE_XMLTAG).getText());

				comptesTmp.addBudgetRecord(new BudgetRecord(category, label,
						amount, startDate, endDate, new BudgetRecordRecurrence(
								recField, recValue), active));
			}
			account = comptesTmp;
			comptesTmp = null;
		} catch (Exception e) {
			throw new AccountException(e);
		}
		logger
				.info("file" + xmlFile.getAbsolutePath()
						+ " successfully loaded");

	}

	/**
	 * 
	 * @param xmlFile
	 * @throws AccountException
	 */
	private void saveAccountXML(File xmlFile) throws AccountException {
		if (account == null) {
			logger.warn("Nothing to be saved");
			return;
		}

		Element elmtRoot = new Element(XmlUtils.ROOT_XMLTAG);
		Document document = new Document(elmtRoot);

		// 1 - solde courant
		Element elmtSoldeCourant = new Element(
				XmlUtils.CURRENT_PROVISION_XMLTAG);
		elmtSoldeCourant.addContent("" + account.getCurrentProvisionAmount());
		elmtRoot.addContent(elmtSoldeCourant);

		// 2 - dateSoldeCourant
		Element elmtDateSoldeCourant = new Element(
				XmlUtils.CURRENT_PROVISION_DATE_XMLTAG);
		elmtDateSoldeCourant.addContent(dateFormat.format(account
				.getCurrentProvisionDate().getTime()));
		elmtRoot.addContent(elmtDateSoldeCourant);

		// 3 - history records
		Element elmtHistoryRecordList = new Element(
				XmlUtils.HISTORY_RECORD_LIST_XMLTAG);

		HistoryRecord[] historyRecords = account
				.getAllHistoryProvisionrecords();
		for (int i = 0; i < historyRecords.length; i++) {
			Element elmtProvisionRecord = new Element(
					XmlUtils.HISTORY_RECORD_XMLTAG);

			// 3.1 date
			Element elmtRecordDate = new Element(
					XmlUtils.HISTORY_RECORD_DATE_XMLTAG);
			elmtRecordDate.addContent(dateFormat.format(historyRecords[i]
					.getDate()));
			elmtProvisionRecord.addContent(elmtRecordDate);

			// 3.2 provision
			Element elmtProvision = new Element(
					XmlUtils.HISTORY_RECORD_PROVISION_XMLTAG);
			elmtProvision.addContent("" + historyRecords[i].getAmount());
			elmtProvisionRecord.addContent(elmtProvision);

			elmtHistoryRecordList.addContent(elmtProvisionRecord);
		}

		elmtRoot.addContent(elmtHistoryRecordList);

		// 4 - budget records
		Element elmtBudgetRecordList = new Element(
				XmlUtils.BUDGET_RECORD_LIST_XMLTAG);
		BudgetRecord[] budgetRecords = account.getAllBudgetRecords();
		for (int i = 0; i < budgetRecords.length; i++) {
			Element elmtBudgetRecord = new Element(
					XmlUtils.BUDGET_RECORD_XMLTAG);

			// 4.1 active
			Element elmtActive = new Element(XmlUtils.ACTIVE_XMLTAG);
			elmtActive.addContent("" + budgetRecords[i].isActive());
			elmtBudgetRecord.addContent(elmtActive);

			// 4.2 category
			Element elmtcategory = new Element(XmlUtils.CATEGORY_XMLTAG);
			elmtcategory.addContent(budgetRecords[i].getCategory());
			elmtBudgetRecord.addContent(elmtcategory);

			// 4.3 label
			Element elmtLabel = new Element(XmlUtils.LABEL_XMLTAG);
			elmtLabel.addContent(budgetRecords[i].getLabel());
			elmtBudgetRecord.addContent(elmtLabel);

			// 4.4 amount
			Element elmtAmount = new Element(XmlUtils.AMOUNT_XMLTAG);
			elmtAmount.addContent("" + budgetRecords[i].getAmount());
			elmtBudgetRecord.addContent(elmtAmount);

			// 4.5 start date
			Element elmtStartDate = new Element(XmlUtils.STARTDATE_XMLTAG);
			elmtStartDate.addContent(dateFormat.format(budgetRecords[i]
					.getStartDate().getTime()));
			elmtBudgetRecord.addContent(elmtStartDate);

			// 4.6 end date
			Element elmtEndDate = new Element(XmlUtils.ENDDATE_XMLTAG);
			elmtEndDate.addContent(dateFormat.format(budgetRecords[i]
					.getEndDate().getTime()));
			elmtBudgetRecord.addContent(elmtEndDate);

			// 4.7 recurrence
			Element elmtRec = new Element(XmlUtils.REC_XMLTAG);

			// 4.7.1 recfield
			Element elmtRecField = new Element(XmlUtils.RECFIELD_XMLTAG);
			elmtRecField.addContent(""
					+ budgetRecords[i].getReccurrence().getRecField());
			elmtRec.addContent(elmtRecField);

			// 4.7.2 recvalue
			Element elmtRecValue = new Element(XmlUtils.RECVALUE_XMLTAG);
			elmtRecValue.addContent(""
					+ budgetRecords[i].getReccurrence().getRecValue());
			elmtRec.addContent(elmtRecValue);

			elmtBudgetRecord.addContent(elmtRec);

			elmtBudgetRecordList.addContent(elmtBudgetRecord);
		}

		elmtRoot.addContent(elmtBudgetRecordList);

		// write to file
		FileOutputStream xmlOutput = null;
		try {
			// On utilise ici un affichage classique avec getPrettyFormat()
			XMLOutputter sortie = new XMLOutputter(org.jdom.output.Format
					.getPrettyFormat());
			xmlOutput = new FileOutputStream(xmlFile);
			sortie.output(document, xmlOutput);
		} catch (java.io.IOException e) {
			logger.error(e);
			throw new AccountException(e);
		} finally {
			if (xmlOutput != null) {
				try {
					xmlOutput.close();
				} catch (IOException e) {
				}
			}
		}

		logger.info("Saved to file" + xmlFile.getAbsolutePath());
	}

	/**
	 * 
	 * 
	 */
	public void closeAccount() {
		account = new Account();
		account.addAccountListener(this);
		this.accountChanged(new AccountChangedEvent(account, null));
	}

	/**
	 * 
	 * 
	 */
	public MainFrame displayGUI() {
		mainFrame.display();
		return mainFrame;
	}

	/**
	 * 
	 */
	public void budgetRecordChanged(AccountChangedEvent _evt) {
		// logger.debug("entree changed");
		// relay the notification
		mainFrame.budgetRecordChanged(_evt);
	}

	public void historyRecordChanged(AccountChangedEvent _evt) {
		logger.debug("history record changed");
		// relay the notification
		mainFrame.historyRecordChanged(_evt);
	}

	public void accountChanged(AccountChangedEvent _evt) {
		// logger.debug("comptes changed");
		// relay the notification
		mainFrame.accountChanged(_evt);
	}

	/**
	 * 
	 * @param _file
	 */
	private void accountOpened(File _file) {
		mainFrame.accountOpened(_file);
	}

}
