package compta.ihm.table.renderer;

import java.awt.Color;
import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class DateCellRenderer extends DefaultTableCellRenderer {

	private static final SimpleDateFormat dateFormatter = new SimpleDateFormat(
			"dd MMM yy", Locale.FRANCE);

	@Override
	public Component getTableCellRendererComponent(JTable _table,
			Object _value, boolean _isSelected, boolean _hasFocus, int _row,
			int _column) {

		Date today = new Date();
		Date value = (Date) _value;

		String formattedDate = dateFormatter.format(value);

		JLabel renderer = (JLabel) super.getTableCellRendererComponent(_table,
				formattedDate, _isSelected, _hasFocus, _row, _column);
		renderer.setHorizontalAlignment(JLabel.RIGHT);

		if (_column == 5) {
			if (today.after(value)) {
				// expired
				renderer.setForeground(Color.RED);
			} else {
				// not expired
				renderer.setForeground(Color.BLACK);
			}
		}

		return renderer;
	}

}
