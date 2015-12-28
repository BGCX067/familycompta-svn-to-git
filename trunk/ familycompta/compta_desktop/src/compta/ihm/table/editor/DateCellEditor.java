package compta.ihm.table.editor;

import java.awt.Component;
import java.util.Date;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;

import compta.ihm.MyDateChooser;

public class DateCellEditor extends DefaultCellEditor {

	private MyDateChooser dateChooser = getDateChooser();

	public DateCellEditor() {
		super(new JCheckBox());
		setClickCountToStart(2);
	}

	private MyDateChooser getDateChooser() {
		if (dateChooser == null) {
			dateChooser = new MyDateChooser();
		}
		return dateChooser;
	}

	public Object getCellEditorValue() {
		return dateChooser.getDate();
	}

	public Component getTableCellEditorComponent(JTable _table, Object _value,
			boolean _isSelected, int _row, int _column) {

		dateChooser.setDate((Date) _value);
		return dateChooser;
	}

}
