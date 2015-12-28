package compta.ihm.table.editor;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;

import compta.model.budget.BudgetRecordRecurrence;

public class RecurrenceCellEditor extends DefaultCellEditor {

	private static final BudgetRecordRecurrence[] recurrences = { BudgetRecordRecurrence.REC_UNIQUE,
			BudgetRecordRecurrence.REC_1_JOUR, BudgetRecordRecurrence.REC_1_SEMAINE,
			BudgetRecordRecurrence.REC_2_SEMAINES, BudgetRecordRecurrence.REC_1_MOIS,
			BudgetRecordRecurrence.REC_2_MOIS, BudgetRecordRecurrence.REC_6_MOIS, BudgetRecordRecurrence.REC_1_AN,
			BudgetRecordRecurrence.REC_2_ANS };

	private final JComboBox comboBox = new JComboBox(recurrences);

	public RecurrenceCellEditor() {
		super(new JComboBox());
		setClickCountToStart(2);
	}

	public Object getCellEditorValue() {
		return comboBox.getSelectedItem();
	}

	public Component getTableCellEditorComponent(JTable _table, Object _value,
			boolean _isSelected, int _row, int _column) {

		BudgetRecordRecurrence rec = (BudgetRecordRecurrence) _value;
		comboBox.setSelectedItem(rec);
		return comboBox;
	}

}
