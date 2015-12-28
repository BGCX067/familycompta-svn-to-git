package compta.ihm.table;

import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import compta.controller.AccountController;
import compta.ihm.table.editor.DateCellEditor;
import compta.ihm.table.renderer.AmountCellRenderer;
import compta.ihm.table.renderer.DateCellRenderer;
import compta.model.history.HistoryRecord;

public class HistoryRecordsTable extends JTable {

	public HistoryRecordsTable(AccountController controller) {
		super();

		// model
		this.setModel(new HistoryRecordsTableModel(controller));
		this.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		this.setRowSelectionAllowed(true);
		this.setColumnSelectionAllowed(false);
		this.setRowHeight(20);

		// renderer
		this.getColumnModel().getColumn(0).setCellRenderer(
				new DateCellRenderer());
		this.getColumnModel().getColumn(1).setCellRenderer(
				new AmountCellRenderer());

		// editor
		this.getColumnModel().getColumn(0).setCellEditor(new DateCellEditor());

		// sorter
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(this
				.getModel());
		this.setRowSorter(sorter);

		// sizes
		this.getTableHeader().setReorderingAllowed(false);
		//this.getColumnModel().getColumn(0).setMaxWidth(20);
	}

	/**
	 * 
	 * @param rowIndex
	 * @param vColIndex
	 */
	public void scrollToVisible(int rowIndex, int vColIndex) {
		if (!(this.getParent() instanceof JViewport)) {
			return;
		}
		JViewport viewport = (JViewport) this.getParent();

		// This rectangle is relative to the table where the
		// northwest corner of cell (0,0) is always (0,0).
		Rectangle rect = this.getCellRect(rowIndex, vColIndex, true);

		// The location of the viewport relative to the table
		Point pt = viewport.getViewPosition();

		// Translate the cell location so that it is relative
		// to the view, assuming the northwest corner of the
		// view is (0,0)
		rect.setLocation(rect.x - pt.x, rect.y - pt.y);

		// Scroll the area into view
		viewport.scrollRectToVisible(rect);
	}

	public void removeHistoryRecord(HistoryRecord record) {
		((HistoryRecordsTableModel) this.getModel())
				.removeHistoryRecord(record);
	}

	public int getViewIndexOf(HistoryRecord record) {
		int modelRowIndex = ((HistoryRecordsTableModel) this.getModel())
				.getIndexOf(record);
		return this.convertRowIndexToView(modelRowIndex);
	}

	public HistoryRecord getHistoryRecord(int viewRowIndex) {

		return ((HistoryRecordsTableModel) this.getModel())
				.getHistoryRecord(convertRowIndexToModel(viewRowIndex));
	}

	public void fireHistoryRecordUpdated(HistoryRecord record) {
		((HistoryRecordsTableModel) this.getModel())
				.fireHistorytRecordUpdated(record);
	}

}
