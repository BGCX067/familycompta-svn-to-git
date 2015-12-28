package compta.model;

import java.util.EventObject;

public class AccountChangedEvent extends EventObject {

	private AccountRecord record = null;

	private Account account = null;

	public AccountChangedEvent(Account account_, AccountRecord record_) {
		super(account_);
		record = record_;
		account = account_;
	}

	public AccountRecord getRecord() {
		return record;
	}

	public Account getAccount() {
		return account;
	}

}
