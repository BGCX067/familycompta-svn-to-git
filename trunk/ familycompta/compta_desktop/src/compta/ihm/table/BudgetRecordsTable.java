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
import compta.ihm.table.editor.RecurrenceCellEditor;
import compta.ihm.table.renderer.AmountCellRenderer;
import compta.ihm.table.renderer.DateCellRenderer;
import compta.ihm.table.renderer.RecurrenceCellRenderer;
import compta.model.budget.BudgetRecord;
import compta.model.budget.BudgetRecordRecurrenceComparator;

public class BudgetRecordsTable extends JTable {

	public BudgetRecordsTable(AccountController controller) {
		super();

		// model
		this.setModel(new BudgetRecordsTableModel(controller));
		this.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		this.setRowSelectionAllowed(true);
		this.setColumnSelectionAllowed(false);
		this.setRowHeight(20);

		// renderer
		this.getColumnModel().getColumn(6).setCellRenderer(
				new RecurrenceCellRenderer());
		this.getColumnModel().getColumn(5).setCellRenderer(
				new DateCellRenderer());
		this.getColumnModel().getColumn(4).setCellRenderer(
				new DateCellRenderer());
		this.getColumnModel().getColumn(3).setCellRenderer(
				new AmountCellRenderer());

		// editor
		this.getColumnModel().getColumn(4).setCellEditor(
				new DateCellEditor());

		this.getColumnModel().getColumn(5).setCellEditor(
				new DateCellEditor());

		this.getColumnModel().getColumn(6).setCellEditor(
				new RecurrenceCellEditor());

		// sorter
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(this
				.getModel());
		sorter.setComparator(6, new BudgetRecordRecurrenceComparator());
		this.setRowSorter(sorter);

		// sizes
		this.getTableHeader().setReorderingAllowed(false);
		this.getColumnModel().getColumn(0).setMaxWidth(20);
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

	public void removeBudgetRecord(BudgetRecord entree) {
		((BudgetRecordsTableModel) this.getModel()).removeBudgetRecord(entree);
	}

	// public void removeEntree(int viewRowIndex) {
	// int modelRowIndex = this.convertRowIndexToModel(viewRowIndex);
	// ((ComptaTableModel) this.getModel()).removeEntree(modelRowIndex);
	// }

	public int getViewIndexOf(BudgetRecord entree) {
		int modelRowIndex = ((BudgetRecordsTableModel) this.getModel())
				.getIndexOf(entree);
		return this.convertRowIndexToView(modelRowIndex);
	}

	public BudgetRecord getBudgetRecord(int viewRowIndex) {

		return ((BudgetRecordsTableModel) this.getModel())
				.getBudgetRecord(convertRowIndexToModel(viewRowIndex));
	}

	public void fireBudgetRecordUpdated(BudgetRecord entree) {
		((BudgetRecordsTableModel) this.getModel()).fireBudgetRecordUpdated(entree);
	}

}
