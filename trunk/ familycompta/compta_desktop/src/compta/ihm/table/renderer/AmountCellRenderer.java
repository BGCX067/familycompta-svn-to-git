package compta.ihm.table.renderer;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;

import compta.ihm.AmountTextField;

public class AmountCellRenderer extends DefaultTableCellRenderer {

	private AmountTextField numericRenderer = null;

	private Color backGroundColor = null;

	public AmountCellRenderer() {
		super();
		numericRenderer = new AmountTextField();
		numericRenderer.setBorder(null);
		numericRenderer.setHorizontalAlignment(JTextField.RIGHT);
		backGroundColor = numericRenderer.getBackground();
	}

	@Override
	public Component getTableCellRendererComponent(JTable _table,
			Object _value, boolean _isSelected, boolean _hasFocus, int _row,
			int _column) {

		numericRenderer.setValue(_value);
		if (_isSelected) {
			numericRenderer.setBackground(_table.getSelectionBackground());
		} else {
			numericRenderer.setBackground(backGroundColor);
		}
		return numericRenderer;
	}

}
