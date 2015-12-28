package compta.model;

import java.util.Date;
import java.util.Vector;

import javax.swing.event.EventListenerList;

import org.apache.log4j.Logger;

import compta.model.budget.BudgetRecord;
import compta.model.history.HistoryRecord;

public class Account {

	private static final Logger logger = Logger.getLogger(Account.class);

	private Vector<BudgetRecord> budgetRecords = new Vector<BudgetRecord>();

	private Vector<HistoryRecord> provisionHistoryRecords = new Vector<HistoryRecord>();

	private HistoryRecord currentProvisionRecord = null;

	private EventListenerList listeners = null;

	/**
	 * 
	 * 
	 */
	public Account() {

		currentProvisionRecord = new HistoryRecord();
		listeners = new EventListenerList();
	}

	// //////////////////////////////////////////////////////////////////////////////////////
	// PROVISION
	// //////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 
	 * @param historyRecord
	 */
	public void addHistoryRecord(HistoryRecord historyRecord) {
		if (historyRecord == null) {
			logger.error("null history record passed for adding");
			return;
		}

		// check if a record has already the same date
		// int i = 0;
		// int recordsCount = provisionHistoryRecords.size();
		// while (i < recordsCount
		// && !provisionHistoryRecords.get(i).isSameDate(historyRecord)) {
		// i++;
		// }
		//
		// if (i != recordsCount) {
		// // delete the old record
		// provisionHistoryRecords.remove(i);
		//		}

		// add new record
		provisionHistoryRecords.add(historyRecord);
		historyRecord.setAccount(this);

		this.fireAccountChanged();

	}

	/**
	 * 
	 * @param historyRecordToDelete
	 */
	public void removeProvisionHistoryRecord(
			HistoryRecord historyRecordToDelete) {
		if (historyRecordToDelete == null) {
			logger.error("null history record passed for deletion");
			return;
		}

		provisionHistoryRecords.remove(historyRecordToDelete);

		this.fireAccountChanged();
	}

	/**
	 * 
	 * @return
	 */
	public int getHistoryRecordsCount() {
		return provisionHistoryRecords.size();
	}

	public HistoryRecord getHistoryRecord(int index) {
		return provisionHistoryRecords.get(index);
	}

	/**
	 * 
	 * @param aRecord
	 * @return
	 */
	public int getIndexOfHistoryRecord(HistoryRecord aRecord) {
		return provisionHistoryRecords.indexOf(aRecord);
	}

	/**
	 * 
	 * @return
	 */
	public HistoryRecord[] getAllHistoryProvisionrecords() {
		HistoryRecord[] provisionRecordsArray = new HistoryRecord[provisionHistoryRecords
				.size()];

		return provisionHistoryRecords.toArray(provisionRecordsArray);
	}

	/**
	 * 
	 * @return
	 */
	public Date getCurrentProvisionDate() {
		return currentProvisionRecord.getDate();
	}

	/**
	 * 
	 * @param currentProvisionDate_
	 */
	public void setCurrentProvisionDate(Date currentProvisionDate_) {
		if (currentProvisionDate_ == null) {
			logger.error("Attempting to set null for date solde");
			return;
		}
		this.currentProvisionRecord.setDate(currentProvisionDate_);
		this.fireAccountChanged();
	}

	/**
	 * 
	 * @return
	 */
	public float getCurrentProvisionAmount() {
		return currentProvisionRecord.getAmount();
	}

	/**
	 * 
	 * @param currentProvision_
	 */
	public void setCurrentProvision(float currentProvision_) {
		logger.debug("set solde courant = " + currentProvision_);
		if (currentProvision_ != currentProvisionRecord.getAmount()) {
			this.currentProvisionRecord.setAmount(currentProvision_);
			this.fireAccountChanged();
		}
	}

	// //////////////////////////////////////////////////////////////////////////////////////
	// BUDGET
	// //////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 
	 * @param newBudgetRecord
	 */
	public void addBudgetRecord(BudgetRecord newBudgetRecord) {
		if (newBudgetRecord != null) {
			logger.debug("add entree : "
					+ budgetRecords.indexOf(newBudgetRecord));
			budgetRecords.add(newBudgetRecord);
			newBudgetRecord.setAccount(this);
			this.fireAccountChanged();
		}
	}

	/**
	 * 
	 * @param budgetRecordToDelete
	 */
	public void removeBudgetRecord(BudgetRecord budgetRecordToDelete) {
		if (budgetRecordToDelete != null) {
			logger.debug("remove entree : "
					+ budgetRecords.indexOf(budgetRecordToDelete));
			budgetRecords.remove(budgetRecordToDelete);
			this.fireAccountChanged();
		}
	}

	/**
	 * 
	 * @param _entree
	 */
	// public void removeBudgetRecord(int _index) {
	// budgetRecords.remove(_index);
	// this.fireAccountChanged();
	// }
	/**
	 * 
	 * @param index
	 */
	public BudgetRecord getBudgetRecord(int index) {
		return budgetRecords.get(index);
	}

	/**
	 * 
	 * @param aRecord
	 * @return
	 */
	public int getIndexOfBudgetRecord(BudgetRecord aRecord) {
		return budgetRecords.indexOf(aRecord);
	}

	/**
	 * 
	 * @return
	 */
	public int getBudgetRecordCount() {
		return budgetRecords.size();
	}

	/**
	 * 
	 * @return an array containig all the entree.
	 */
	public BudgetRecord[] getAllBudgetRecords() {
		BudgetRecord[] budgetRecordsArray = new BudgetRecord[budgetRecords
				.size()];

		return budgetRecords.toArray(budgetRecordsArray);
	}

	/**
	 * 
	 * @return an array containing all the "active" entrees.
	 */
	public BudgetRecord[] getAllActiveBudgetRecords() {
		Vector<BudgetRecord> activeBudgetRecords = new Vector<BudgetRecord>();
		for (int i = 0; i < budgetRecords.size(); i++) {
			BudgetRecord currentBudgetRecord = budgetRecords.get(i);
			if (currentBudgetRecord.isActive()) {
				activeBudgetRecords.add(currentBudgetRecord);
			}
		}

		BudgetRecord[] budgetRecordsArray = new BudgetRecord[activeBudgetRecords
				.size()];

		return activeBudgetRecords.toArray(budgetRecordsArray);
	}

	/**
	 * 
	 * @param listener
	 */
	public void addAccountListener(AccountListener listener) {
		listeners.add(AccountListener.class, listener);
	}

	/**
	 * 
	 * @param listener
	 */
	public void removeAccountListener(AccountListener listener) {
		listeners.remove(AccountListener.class, listener);
	}

	/**
	 * 
	 * @param _entree
	 */
	public void fireAccountChanged() {
		logger.debug("fire account changed");
		AccountListener[] listenerList = (AccountListener[]) listeners
				.getListeners(AccountListener.class);

		for (AccountListener listener : listenerList) {
			listener.accountChanged(new AccountChangedEvent(this, null));
		}
	}

	/**
	 * 
	 * @param record
	 */
	public void fireBudgetRecordChanged(BudgetRecord record) {
		AccountListener[] listenerList = (AccountListener[]) listeners
				.getListeners(AccountListener.class);

		for (AccountListener listener : listenerList) {
			listener
					.budgetRecordChanged(new AccountChangedEvent(this, record));
		}
	}
	
	/**
	 * 
	 * @param record
	 */
	public void fireHistoryRecordChanged(HistoryRecord record) {
		AccountListener[] listenerList = (AccountListener[]) listeners
				.getListeners(AccountListener.class);

		for (AccountListener listener : listenerList) {
			listener
					.historyRecordChanged(new AccountChangedEvent(this, record));
		}
	}
}
