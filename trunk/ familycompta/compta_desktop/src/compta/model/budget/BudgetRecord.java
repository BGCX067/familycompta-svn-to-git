package compta.model.budget;

import java.util.Date;

import org.apache.log4j.Logger;

import compta.model.Account;
import compta.model.AccountRecord;

public class BudgetRecord implements AccountRecord {

	private static final Logger logger = Logger.getLogger(BudgetRecord.class);

	private static int idCpt = 0;

	private boolean active = true;

	private String label = "";

	private String category = "";

	private float amount = 0f;

	private Date startDate = null;

	private Date endDate = null;

	private BudgetRecordRecurrence reccurrence = null;

	private Account account = null;

	private int id;

	/**
	 * 
	 * @param category_
	 * @param label_
	 * @param amount_
	 * @param startDate_
	 *            MUST NOT BE NULL
	 * @param endDate_
	 *            MUST NOT BE NULL
	 * @param rec_
	 * @param active_
	 */
	public BudgetRecord(String category_, String label_, float amount_,
			Date startDate_, Date endDate_, BudgetRecordRecurrence rec_,
			boolean active_) {

		id = idCpt;
		idCpt++;
		category = category_;
		label = label_;
		amount = amount_;
		reccurrence = rec_;
		active = active_;
		startDate = (Date) startDate_.clone();
		if (endDate_.after(startDate)) {
			endDate = (Date) endDate_.clone();
		} else {
			endDate = (Date) startDate.clone();
		}
	}

	/**
	 * Unique identifier for an BudgetRecord
	 * 
	 * @return
	 */
	public int getID() {
		return id;
	}

	/**
	 * 
	 * 
	 */
	public BudgetRecord() {
		this("", "<nouvelle entree>", 0f, new Date(), new Date(),
				BudgetRecordRecurrence.REC_UNIQUE, true);
	}

	/**
	 * 
	 * @return
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * 
	 * @param _category
	 */
	public void setCategory(String _category) {
		category = _category;
	}

	/**
	 * 
	 * @param account_
	 */
	public void setAccount(Account account_) {
		account = account_;
	}

	/**
	 * 
	 * @return
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * 
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * 
	 * @param _active
	 */
	public void setActive(boolean _active) {
		this.active = _active;
		if (account != null) {
			account.fireBudgetRecordChanged(this);
		}
	}

	/**
	 * 
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * 
	 * @param _label
	 */
	public void setLabel(String _label) {
		this.label = _label;
		if (account != null) {
			account.fireBudgetRecordChanged(this);
		}
	}

	/**
	 * 
	 */
	public float getAmount() {
		return amount;
	}

	/**
	 * 
	 * @param _montant
	 */
	public void setAmount(float _montant) {
		if (_montant != amount) {
			this.amount = _montant;
			if (account != null) {
				account.fireBudgetRecordChanged(this);
			}
		}
	}

	/**
	 * 
	 */
	public BudgetRecordRecurrence getReccurrence() {
		return reccurrence;
	}

	/**
	 * 
	 * @param rec_
	 */
	public void setReccurrence(BudgetRecordRecurrence rec_) {
		if (!rec_.equals(reccurrence)) {
			this.reccurrence = rec_;
			if (account != null) {
				account.fireBudgetRecordChanged(this);
			}
		}
	}

	/**
	 * 
	 */
	public Date getStartDate() {
		return (Date) startDate.clone();
	}

	/**
	 * 
	 * @param startDate_
	 */
	public void setStartDate(Date startDate_) {
		if (startDate_ == null) {
			logger.error("date is null");
		} else {
			this.startDate = startDate_;
			if (startDate.after(endDate) || reccurrence.isUnique()) {
				endDate = (Date) startDate.clone();
			}
			if (account != null) {
				account.fireBudgetRecordChanged(this);
			}
		}
	}

	/**
	 * 
	 * @param endDate_
	 */
	public void setEndDate(Date endDate_) {
		if (endDate_ == null) {
			logger.error("date is null");
		} else {
			this.endDate = endDate_;
			if (startDate.after(endDate) || reccurrence.isUnique()) {
				startDate = (Date) endDate.clone();
			}
			if (account != null) {
				account.fireBudgetRecordChanged(this);
			}
		}
	}

	/**
	 * 
	 */
	public Date getEndDate() {
		return (Date) endDate.clone();
	}

	/**
	 * 
	 */
	public String toString() {
		return "[" + isActive() + "] [" + getLabel() + "] [" + getAmount()
				+ "] [" + getStartDate().getTime() + "] ["
				+ getEndDate().getTime() + "] [" + getReccurrence() + "]";
	}
}
