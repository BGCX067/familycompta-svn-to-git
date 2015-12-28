package compta.ihm.table;

import java.util.Date;

import javax.swing.table.DefaultTableModel;

import compta.controller.AccountController;
import compta.model.budget.BudgetRecord;
import compta.model.budget.BudgetRecordRecurrence;

public class BudgetRecordsTableModel extends DefaultTableModel {

	private AccountController controller = null;

	public BudgetRecordsTableModel(AccountController controller_) {
		super();
		controller = controller_;
	}

	public int getColumnCount() {
		return 7;
	}

	public String getColumnName(int _columnIndex) {
		switch (_columnIndex) {
		case 0:
			return "";
		case 1:
			return "Category";
		case 2:
			return "Label";
		case 3:
			return "Amount";
		case 4:
			return "Start date";
		case 5:
			return "End date";
		case 6:
			return "Repeat";
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
		return controller.getAccount().getBudgetRecordCount();
	}

	public Class<?> getColumnClass(int _columnIndex) {
		switch (_columnIndex) {
		case 0:
			return Boolean.class;
		case 1:
			return String.class;
		case 2:
			return String.class;
		case 3:
			return Float.class;
		case 4:
			return Date.class;
		case 5:
			return Date.class;
		case 6:
			return BudgetRecordRecurrence.class;
		}
		return null;
	}

	/**
	 * 
	 */
	public Object getValueAt(int _rowIndex, int _columnIndex) {

		if (controller == null || controller.getAccount() == null) {
			return null;
		}

		BudgetRecord entree = controller.getAccount()
				.getBudgetRecord(_rowIndex);

		switch (_columnIndex) {
		case 0:
			return new Boolean(entree.isActive());
		case 1:
			return entree.getCategory();
		case 2:
			return entree.getLabel();
		case 3:
			return new Float(entree.getAmount());
		case 4:
			return entree.getStartDate();
		case 5:
			return entree.getEndDate();
		case 6:
			return entree.getReccurrence();
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

		BudgetRecord entree = controller.getAccount()
				.getBudgetRecord(_rowIndex);

		switch (_columnIndex) {
		case 0:
			Boolean selected = (Boolean) _obj;
			entree.setActive(selected.booleanValue());
			break;
		case 1:
			String category = (String) _obj;
			entree.setCategory(category);
			break;
		case 2:
			String label = (String) _obj;
			entree.setLabel(label);
			break;
		case 3:
			float amount = ((Float) _obj).floatValue();
			entree.setAmount(amount);
			break;
		case 4:
			Date startDate = (Date) _obj;
			entree.setStartDate(startDate);
			break;
		case 5:
			Date endDate = (Date) _obj;
			entree.setEndDate(endDate);
			break;
		case 6:
			BudgetRecordRecurrence rec = (BudgetRecordRecurrence) _obj;
			entree.setReccurrence(rec);
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

	public void removeBudgetRecord(BudgetRecord entree) {
		controller.getAccount().removeBudgetRecord(entree);
	}

	public int getIndexOf(BudgetRecord entree) {
		return controller.getAccount().getIndexOfBudgetRecord(entree);
	}

	public BudgetRecord getBudgetRecord(int modelRowIndex) {
		return controller.getAccount().getBudgetRecord(modelRowIndex);
	}

	public void fireBudgetRecordUpdated(BudgetRecord entree) {
		int modelRowIndex = controller.getAccount().getIndexOfBudgetRecord(
				entree);
		this.fireTableRowsUpdated(modelRowIndex, modelRowIndex);
	}

}
