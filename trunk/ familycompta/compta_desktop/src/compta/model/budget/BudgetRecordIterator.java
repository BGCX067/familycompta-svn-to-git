package compta.model.budget;

import java.util.Date;
import java.util.GregorianCalendar;

public class BudgetRecordIterator {

	private BudgetRecord budgetRecord = null;

	private Date currentOccDate = null;

	private Date nextOccDate = null;

	public BudgetRecordIterator(BudgetRecord budgetRecord_) {
		if (budgetRecord_ == null) {
			throw new NullPointerException("BudgetRecord cannot be null");
		}
		budgetRecord = budgetRecord_;
		reset();
	}

	/**
	 * 
	 * 
	 */
	public void reset() {
		currentOccDate = null;
		nextOccDate = (Date) budgetRecord.getStartDate().clone();
	}

	/**
	 * 
	 * @return
	 */
	public boolean hasNext() {

		if (budgetRecord.getReccurrence().isUnique() && currentOccDate != null) {
			// special case for nil-reccurrnce
			return false;
		}

		// double negation in the following statement in order to include thr
		// case where dates are equals
		return !nextOccDate.after(budgetRecord.getEndDate());
	}

	/**
	 * 
	 * @return
	 */
	public BudgetRecordOccurrence next() {

		if (!hasNext()) {
			throw new ArrayIndexOutOfBoundsException(
					"No next item for BudgetRecord");
		}

		GregorianCalendar nextOccGregorianDate = new GregorianCalendar();
		nextOccGregorianDate.setTime(nextOccDate);

		nextOccGregorianDate.add(budgetRecord.getReccurrence().getRecField(),
				budgetRecord.getReccurrence().getRecValue());

		currentOccDate = nextOccDate;
		nextOccDate = nextOccGregorianDate.getTime();

		return new BudgetRecordOccurrence(budgetRecord, currentOccDate);
	}

}
