package compta.ihm.chart;

import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;
import org.jfree.data.xy.XYDataset;

import compta.model.budget.BudgetRecordOccurrence;

public class BudgetRecordChartToolTipGenerator extends StandardXYToolTipGenerator {

	public String generateToolTip(XYDataset dataset, int seriesIndex,
			int itemIndex) {

		String defaultToolTip = super.generateLabelString(dataset, seriesIndex,
				itemIndex);

		if (dataset instanceof TimeSeriesCollection) {
			TimeSeriesCollection timeSeriesCollection = (TimeSeriesCollection) dataset;
			TimeSeries timeSeries = timeSeriesCollection.getSeries(seriesIndex);

			TimeSeriesDataItem dataItem = timeSeries.getDataItem(itemIndex);
			RegularTimePeriod date = dataItem.getPeriod();
			Number value = dataItem.getValue();

			String toolTip = "<html><b>Date :</b> " + date.toString()
					+ "<br><b>Provision :</b> " + value.floatValue();

			if (dataItem instanceof BudgetRecordTimeSeriesDataItem) {
				toolTip += "<br><b>Records :</b>";
				BudgetRecordTimeSeriesDataItem comptesDataItem = (BudgetRecordTimeSeriesDataItem) dataItem;
				BudgetRecordOccurrence[] occs = comptesDataItem.getOccurrences();
				for (int i = 0; i < occs.length; i++) {
					toolTip += "<br>  " + occs[i].getBudgetRecord().getLabel()
							+ " = " + occs[i].getBudgetRecord().getAmount();
				}
			}

			toolTip += "</html>";

			return toolTip;

		}

		return defaultToolTip;

	}

}
