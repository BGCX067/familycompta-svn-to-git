package compta.ihm;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.NumberFormat;

import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class AmountTextField extends JFormattedTextField implements
		DocumentListener, ActionListener, FocusListener {

	public AmountTextField() {
		super(NumberFormat.getInstance());
		this.setValue(0f);
		this.getDocument().addDocumentListener(this);
		this.addActionListener(this);
		this.addFocusListener(this);
		this.setHorizontalAlignment(JTextField.RIGHT);
		this.setFocusLostBehavior(COMMIT_OR_REVERT);
	}

	public void changedUpdate(DocumentEvent arg0) {
		updateColor();
	}

	public void insertUpdate(DocumentEvent arg0) {
		updateColor();
	}

	public void removeUpdate(DocumentEvent arg0) {
		updateColor();
	}

	public void actionPerformed(ActionEvent arg0) {
		updateColor();
	}

	public void focusGained(FocusEvent arg0) {
		selectAll();
	}

	public void focusLost(FocusEvent arg0) {

	}

	private static final Color dark_green = new Color(0, 128, 0);

	private void updateColor() {
		Number value = (Number) this.getValue();
		if (value != null) {
			float floatValue = value.floatValue();
			if (floatValue == 0) {
				this.setForeground(Color.BLACK);
			} else if (floatValue < 0) {
				this.setForeground(Color.RED);
			} else {
				this.setForeground(dark_green);
			}
		}

	}

}
