package compta.model.budget;

import java.util.Date;

public class BudgetRecordOccurrence {

	private BudgetRecord entree = null;

	private Date date = null;

	/**
	 * 
	 * @param entree_
	 *            MUST NOT BE NULL
	 * @param date_
	 */
	protected BudgetRecordOccurrence(BudgetRecord entree_, Date date_) {
		entree = entree_;
		date = date_;
	}

	public Date getDate() {
		return date;
	}

	public BudgetRecord getBudgetRecord() {
		return entree;
	}
}
