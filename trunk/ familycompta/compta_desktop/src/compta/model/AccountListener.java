package compta.model;

import java.util.EventListener;

public interface AccountListener extends EventListener {

	/**
	 * This method is triggered when an BudgetRecord (_evt.getRecord()) has been changed.
	 * @param evt
	 */
	public void budgetRecordChanged(AccountChangedEvent evt);
	
	/**
	 * This method is triggered when an HistoryRecord (_evt.getRecord()) has been changed.
	 * @param evt
	 */
	public void historyRecordChanged(AccountChangedEvent evt);

	/**
	 * This method is triggered when Account has been moidofied i.e.:
	 * - attributes have been changed
	 * - an BudgetRecord has been added/removed
	 * - account has been loaded/closd
	 * @param evt
	 */
	public void accountChanged(AccountChangedEvent evt);

}
