package compta.model.history;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import compta.model.Account;
import compta.model.AccountRecord;

public class HistoryRecord implements AccountRecord {

	private Date recordDate = null;

	private float recordAmount = 0f;

	private Account account = null;

	/**
	 * 
	 * 
	 */
	public HistoryRecord() {
		this(new Date(), 0f);
	}

	/**
	 * 
	 * @param recordDate_
	 *            MUST NOT BE NULL
	 * @param recordAmount_
	 */
	public HistoryRecord(Date recordDate_, float recordAmount_) {
		recordDate = recordDate_;
		recordAmount = recordAmount_;
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
	 * @return
	 */
	public float getAmount() {
		return recordAmount;
	}

	/**
	 * 
	 * @param recordAmount_
	 */
	public void setAmount(float recordAmount_) {
		this.recordAmount = recordAmount_;
		if (account != null) {
			account.fireHistoryRecordChanged(this);
		}

	}

	/**
	 * 
	 * @return
	 */
	public Date getDate() {
		return recordDate;
	}

	/**
	 * 
	 * @param recordDate_
	 *            MUST NOT BE NULL
	 */
	public void setDate(Date recordDate_) {
		this.recordDate = (Date) recordDate_.clone();
		if (account != null) {
			account.fireHistoryRecordChanged(this);
		}
	}

	/**
	 * 
	 * @param otherRecord
	 * @return
	 */
	public boolean isSameDate(HistoryRecord otherRecord) {

		if (otherRecord == null) {
			return false;
		}

		if (otherRecord.recordDate == null) {
			return false;
		}

		GregorianCalendar thisDate = new GregorianCalendar();
		thisDate.setTime(this.recordDate);
		GregorianCalendar otherDate = new GregorianCalendar();
		otherDate.setTime(otherRecord.recordDate);

		if (otherDate.get(Calendar.YEAR) != thisDate.get(Calendar.YEAR)) {
			return false;
		}

		if (otherDate.get(Calendar.MONTH) != thisDate.get(Calendar.MONTH)) {
			return false;
		}

		if (otherDate.get(Calendar.DAY_OF_MONTH) != thisDate
				.get(Calendar.DAY_OF_MONTH)) {
			return false;
		}

		return true;
	}

	public Object clone() {
		return new HistoryRecord(this.recordDate, this.recordAmount);
	}

}
