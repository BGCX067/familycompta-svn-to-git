package compta.ihm;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

import com.toedter.calendar.JDateChooser;

public class MyDateChooser extends JDateChooser {

	private Vector<PropertyChangeListener> dateChangeListeners = null;

	public MyDateChooser() {
		super();
		setDateFormatString("dd MMM yy");
		setLocale(Locale.FRANCE);
		this.setDate(new Date());
	}

	@Override
	public void propertyChange(PropertyChangeEvent _evt) {

		super.propertyChange(_evt);
		for (int i = 0; i < getDateChangeListeners().size(); i++) {
			getDateChangeListeners().get(i).propertyChange(_evt);
		}
	}

	public void addDateChangeListener(PropertyChangeListener _pcl) {
		getDateChangeListeners().add(_pcl);
	}

	public void removeDateChangeListener(PropertyChangeListener _pcl) {
		getDateChangeListeners().remove(_pcl);
	}

	private Vector<PropertyChangeListener> getDateChangeListeners() {
		if (dateChangeListeners == null) {
			dateChangeListeners = new Vector<PropertyChangeListener>();
		}
		return dateChangeListeners;
	}

}
