package compta.ihm.chart;

import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeriesDataItem;

import compta.model.budget.BudgetRecordOccurrence;

public class BudgetRecordTimeSeriesDataItem extends TimeSeriesDataItem {

	private BudgetRecordOccurrence[] occs;

	public BudgetRecordTimeSeriesDataItem(RegularTimePeriod period, Number value) {
		super(period, value);
	}

	/**
	 * 
	 * @param period
	 * @param value
	 * @param occs_
	 */
	public BudgetRecordTimeSeriesDataItem(RegularTimePeriod period, Number value,
			BudgetRecordOccurrence[] occs_) {
		super(period, value);
		if (occs_ == null) {
			occs = new BudgetRecordOccurrence[0];
		} else {
			occs = occs_;
		}
	}

	public BudgetRecordOccurrence[] getOccurrences() {
		return occs;
	}

}
