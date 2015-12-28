package compta.ihm.chart;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;

import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;

import compta.model.Account;
import compta.model.budget.BudgetRecord;
import compta.model.budget.BudgetRecordIterator;
import compta.model.budget.BudgetRecordOccurrence;
import compta.model.history.HistoryRecord;

public class ChartUtils {

	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	private static Date[] createDateSample(Date _startDate, Date _endDate) {

		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.setTime(_startDate);
		// GregorianCalendar endDate = new GregorianCalendar();
		// endDate.setTime(_endDate);

		// create the sample
		Vector<Date> dateSamples = new Vector<Date>();

		while (!currentDate.getTime().after(_endDate)) {
			dateSamples.add(currentDate.getTime());
			currentDate.add(GregorianCalendar.DAY_OF_MONTH, 1);
		}

		Date[] dateArray = new Date[dateSamples.size()];

		return dateSamples.toArray(dateArray);

	}

	/**
	 * 
	 * @param account
	 * @param endDate
	 * @return
	 */
	public static TimeSeries computePrevision(Account account, Date endDate) {

		// create the Date samples
		Date startDate = account.getCurrentProvisionDate();
		Date[] dateSamples = createDateSample(startDate, endDate);

		// init the time series
		TimeSeries timeSeries = new TimeSeries("Forecast");

		// compute the amounts for every date
		// float[] amounts = new float[dateSamples.length];
		float sum = account.getCurrentProvisionAmount();

		BudgetRecord[] allActiveBudgetRecords = account
				.getAllActiveBudgetRecords();
		BudgetRecordIterator[] iterators = new BudgetRecordIterator[allActiveBudgetRecords.length];
		BudgetRecordOccurrence[] currentOccs = new BudgetRecordOccurrence[allActiveBudgetRecords.length];

		// init the iterators
		for (int i = 0; i < iterators.length; i++) {
			iterators[i] = new BudgetRecordIterator(allActiveBudgetRecords[i]);
			if (iterators[i].hasNext()) {
				currentOccs[i] = iterators[i].next();
			} else {
				currentOccs[i] = null;
			}
		}

		for (int i = 0; i < dateSamples.length; i++) {
			Vector<BudgetRecordOccurrence> occForThisItem = new Vector<BudgetRecordOccurrence>();

			for (int j = 0; j < currentOccs.length; j++) {
				while (currentOccs[j] != null
						&& !currentOccs[j].getDate().after(dateSamples[i])) {

					// no need to test if the budgetRecord is active.
					if (currentOccs[j].getDate().after(startDate)) {
						sum += currentOccs[j].getBudgetRecord().getAmount();
						occForThisItem.add(currentOccs[j]);
					}

					if (!iterators[j].hasNext()) {
						currentOccs[j] = null;
					} else {
						currentOccs[j] = iterators[j].next();
					}
				}
			}

			BudgetRecordOccurrence[] occForThisItemArray = new BudgetRecordOccurrence[occForThisItem
					.size()];

			timeSeries.add(new BudgetRecordTimeSeriesDataItem(new Day(
					dateSamples[i]), sum, occForThisItem
					.toArray(occForThisItemArray)));
		}

		return timeSeries;
	}

	public static TimeSeries computeHistorySeries(Account account) {

		HistoryRecord[] historyRecords = account
				.getAllHistoryProvisionrecords();

		// init the time series
		TimeSeries timeSeries = new TimeSeries("History");

		for (int i = 0; i < historyRecords.length; i++) {
			timeSeries.add(new Day(historyRecords[i].getDate()),
					historyRecords[i].getAmount());
		}

		return timeSeries;
	}
}
