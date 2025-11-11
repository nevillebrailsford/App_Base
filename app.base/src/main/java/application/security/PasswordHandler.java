package application.security;

import java.util.logging.Logger;

import application.definition.ApplicationConfiguration;
import application.inifile.IniFile;

public class PasswordHandler {
	private static final String CLASS_NAME = PasswordHandler.class.getName();
	private static final Logger LOGGER = ApplicationConfiguration.logger();

	private static PasswordHandler instance = null;

	private boolean validPassword = false;

	private PasswordHandler() {
	}

	public static synchronized PasswordHandler instance() {
		LOGGER.entering(CLASS_NAME, "instance");
		if (instance == null) {
			instance = new PasswordHandler();
		}
		LOGGER.exiting(CLASS_NAME, "instance");
		return instance;
	}

	public boolean validatePassword(String checkPassword) {
		LOGGER.entering(CLASS_NAME, "validatePassword", "********");
		validPassword = false;
		String appPassword = IniFile.value("password");
		try {
			if (appPassword.isEmpty()) {
				IniFile.store("password", checkPassword);
				validPassword = true;
				LOGGER.exiting(CLASS_NAME, "validatePassword", true);
				return true;
			}
			if (appPassword.equals(checkPassword)) {
				validPassword = true;
				LOGGER.exiting(CLASS_NAME, "validatePassword", true);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.exiting(CLASS_NAME, "validatePassword", false);
			return false;
		}
		LOGGER.exiting(CLASS_NAME, "validatePassword", false);
		return false;
	}

	public boolean validPassword() {
		LOGGER.entering(CLASS_NAME, "validPassword");
		LOGGER.exiting(CLASS_NAME, "", validPassword);
		return validPassword;
	}
}
