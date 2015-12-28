package compta.controller;

import java.util.GregorianCalendar;

import junit.framework.TestCase;

import compta.model.budget.BudgetRecord;
import compta.model.budget.BudgetRecordIterator;
import compta.model.budget.BudgetRecordOccurrence;
import compta.model.budget.BudgetRecordRecurrence;

public class TestBudgetRecordIterator extends TestCase {

	public TestBudgetRecordIterator(String name) {
		super(name);
	}

	public void testIterator1() {
		System.out.println("test 1");

		BudgetRecord entree = new BudgetRecord("cat", "label", 100f,
				new GregorianCalendar(2007, 00, 01).getTime(),
				new GregorianCalendar(2007, 11, 01).getTime(),
				BudgetRecordRecurrence.REC_1_MOIS, true);

		BudgetRecordIterator it = new BudgetRecordIterator(entree);

		while (it.hasNext()) {
			BudgetRecordOccurrence occ = it.next();
			System.out.println(occ.getDate().toString());
		}
	}

	public void testIterator2() {
		System.out.println("test 2");
		BudgetRecord entree = new BudgetRecord("cat", "label", 100f,
				new GregorianCalendar(2007, 00, 01).getTime(),
				new GregorianCalendar(2007, 11, 15).getTime(),
				BudgetRecordRecurrence.REC_UNIQUE, true);

		BudgetRecordIterator it = new BudgetRecordIterator(entree);

		while (it.hasNext()) {
			BudgetRecordOccurrence occ = it.next();
			System.out.println(occ.getDate().toString());
		}
	}

}
