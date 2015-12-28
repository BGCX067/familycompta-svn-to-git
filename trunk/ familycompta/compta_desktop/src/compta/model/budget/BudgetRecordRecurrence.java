package compta.model.budget;

import java.util.GregorianCalendar;

import compta.AccountException;

public class BudgetRecordRecurrence {

	public static final BudgetRecordRecurrence REC_UNIQUE = new BudgetRecordRecurrence(
			GregorianCalendar.DAY_OF_MONTH, 0);

	public static final BudgetRecordRecurrence REC_1_JOUR = new BudgetRecordRecurrence(
			GregorianCalendar.DAY_OF_MONTH, 1);

	public static final BudgetRecordRecurrence REC_1_SEMAINE = new BudgetRecordRecurrence(
			GregorianCalendar.WEEK_OF_MONTH, 1);

	public static final BudgetRecordRecurrence REC_2_SEMAINES = new BudgetRecordRecurrence(
			GregorianCalendar.WEEK_OF_MONTH, 2);

	public static final BudgetRecordRecurrence REC_1_MOIS = new BudgetRecordRecurrence(
			GregorianCalendar.MONTH, 1);

	public static final BudgetRecordRecurrence REC_2_MOIS = new BudgetRecordRecurrence(
			GregorianCalendar.MONTH, 2);

	public static final BudgetRecordRecurrence REC_6_MOIS = new BudgetRecordRecurrence(
			GregorianCalendar.MONTH, 6);

	public static final BudgetRecordRecurrence REC_1_AN = new BudgetRecordRecurrence(
			GregorianCalendar.YEAR, 1);

	public static final BudgetRecordRecurrence REC_2_ANS = new BudgetRecordRecurrence(
			GregorianCalendar.YEAR, 2);

	private int field;

	private int value;

	/**
	 * Converts the input String into a BudgetRecordRecurrence. Format must be
	 * of the form : "recField-recValue"
	 * 
	 * @param str
	 *            the String to parse
	 * @return the recurrence
	 * @throws AccountException
	 *             if a parse error occurs
	 */
	public static BudgetRecordRecurrence parse(String str)
			throws AccountException {
		int recField;
		int recValue;

		String[] splitrec = str.split("-");
		if (splitrec.length != 2) {
			throw new AccountException("Wrong format for reccurrence = " + str);
		}
		try {
			recField = new Integer(splitrec[0].trim()).intValue();
			recValue = new Integer(splitrec[1].trim()).intValue();
		} catch (NumberFormatException e) {
			throw new AccountException(

			"Wrong reccurrence = " + str);
		} catch (NullPointerException e) {
			throw new AccountException("Reccurrence is null");
		}
		return new BudgetRecordRecurrence(recField, recValue);

	}

	public BudgetRecordRecurrence(int _field, int _value) {
		field = _field;
		value = _value;
	}

	public int getRecValue() {
		return value;
	}

	public int getRecField() {
		return field;
	}

	public boolean equals(BudgetRecordRecurrence _rec) {
		return (value == _rec.value && field == _rec.field);
	}

	/**
	 * A string representation of the recurrence. Only for display purpose.
	 */
	public String toString() {
		if (this.isUnique()) {
			return "once";
		} else {
			return "Every " + ((value == 1) ? "" : value) + " "
					+ dateUnitToString(field) + ((value == 1) ? "" : "s");
		}
	}

	private String dateUnitToString(int _dateUnit) {
		switch (_dateUnit) {
		case GregorianCalendar.DAY_OF_MONTH:
			return "day";
		case GregorianCalendar.WEEK_OF_MONTH:
			return "week";
		case GregorianCalendar.MONTH:
			return "month";
		case GregorianCalendar.YEAR:
			return "year";
		default:
			return "unknown";
		}
	}

	public boolean isUnique() {
		return value == 0;
	}

}
