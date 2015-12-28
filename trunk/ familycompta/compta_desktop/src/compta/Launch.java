package compta;

import org.apache.log4j.Logger;

import compta.controller.AccountController;

public class Launch {

	private static final Logger logger = Logger.getLogger(Launch.class);

	public static void main(String[] args) {
		AccountController controller = new AccountController();
		controller.displayGUI();
		if (args.length > 0) {
			try {
				controller.openAccount(args[0]);
			} catch (AccountException e) {
				logger.error(e.getMessage());
			}
		}
	}

}
