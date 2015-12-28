package compta.model.budget;

import java.util.Comparator;
import java.util.GregorianCalendar;

public class BudgetRecordRecurrenceComparator implements Comparator<BudgetRecordRecurrence> {

	public int compare(BudgetRecordRecurrence rec1, BudgetRecordRecurrence rec2) {

		return getNumberOfDays(rec1) - getNumberOfDays(rec2);
	}

	private int getNumberOfDays(BudgetRecordRecurrence _rec) {
		switch (_rec.getRecField()) {
		case GregorianCalendar.DAY_OF_MONTH:
			return _rec.getRecValue();
		case GregorianCalendar.WEEK_OF_MONTH:
			return _rec.getRecValue() * 7;
		case GregorianCalendar.MONTH:
			return _rec.getRecValue() * 30;
		case GregorianCalendar.YEAR:
			return _rec.getRecValue() * 365;
		default:
			return _rec.getRecValue();
		}
	}

}
