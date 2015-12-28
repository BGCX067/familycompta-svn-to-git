package compta.ihm.table;

import java.util.Date;

import javax.swing.table.DefaultTableModel;

import compta.controller.AccountController;
import compta.model.history.HistoryRecord;

public class HistoryRecordsTableModel extends DefaultTableModel {

	private AccountController controller = null;

	public HistoryRecordsTableModel(AccountController controller_) {
		super();
		controller = controller_;
	}

	public int getColumnCount() {
		return 2;
	}

	public String getColumnName(int _columnIndex) {
		switch (_columnIndex) {
		case 0:
			return "Date";
		case 1:
			return "Provision";
		default:
			return null;
		}
	}

	/**
	 * 
	 */
	public int getRowCount() {
		if (controller == null || controller.getAccount() == null) {
			return 0;
		}
		return controller.getAccount().getHistoryRecordsCount();
	}

	/**
	 * 
	 */
	public Class<?> getColumnClass(int _columnIndex) {
		switch (_columnIndex) {
		case 0:
			return Date.class;
		case 1:
			return Float.class;
		default:
			return null;
		}
	}

	/**
	 * 
	 */
	public Object getValueAt(int _rowIndex, int _columnIndex) {

		if (controller == null || controller.getAccount() == null) {
			System.out.println("ERROR NULL");
			return null;
		}

		HistoryRecord historyRecord = controller.getAccount().getHistoryRecord(
				_rowIndex);

		switch (_columnIndex) {
		case 0:
			return historyRecord.getDate();
		case 1:
			return new Float(historyRecord.getAmount());
		default:
			return null;
		}
	}

	/**
	 * 
	 */
	public void setValueAt(Object _obj, int _rowIndex, int _columnIndex) {
		if (controller == null || controller.getAccount() == null) {
			return;
		}

		HistoryRecord historyRecord = controller.getAccount().getHistoryRecord(
				_rowIndex);

		switch (_columnIndex) {
		case 0:
			Date recordDate = (Date) _obj;
			historyRecord.setDate(recordDate);
			break;
		case 1:
			float amount = ((Float) _obj).floatValue();
			historyRecord.setAmount(amount);
			break;
		}

		this.fireTableCellUpdated(_rowIndex, _columnIndex);
	}

	/**
	 * 
	 */
	public boolean isCellEditable(int arg0, int arg1) {
		return true;
	}

	public void removeHistoryRecord(HistoryRecord record) {
		controller.getAccount().removeProvisionHistoryRecord(record);
	}

	public int getIndexOf(HistoryRecord record) {
		return controller.getAccount().getIndexOfHistoryRecord(record);
	}

	public HistoryRecord getHistoryRecord(int modelRowIndex) {
		return controller.getAccount().getHistoryRecord(modelRowIndex);
	}

	public void fireHistorytRecordUpdated(HistoryRecord record) {
		int modelRowIndex = controller.getAccount().getIndexOfHistoryRecord(record);
		this.fireTableRowsUpdated(modelRowIndex, modelRowIndex);
	}

}
