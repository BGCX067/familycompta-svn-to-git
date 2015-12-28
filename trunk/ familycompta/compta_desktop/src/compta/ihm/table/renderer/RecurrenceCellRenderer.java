package compta.ihm.table.renderer;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import compta.model.budget.BudgetRecordRecurrence;

public class RecurrenceCellRenderer extends DefaultTableCellRenderer {

	@Override
	public Component getTableCellRendererComponent(JTable _table,
			Object _value, boolean _isSelected, boolean _hasFocus, int _row,
			int _column) {

		String text = ((BudgetRecordRecurrence) _value).toString();

		return super.getTableCellRendererComponent(_table, text, _isSelected,
				_hasFocus, _row, _column);

	}

}
