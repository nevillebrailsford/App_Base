package application.security;

import java.util.logging.Logger;

import application.definition.ApplicationConfiguration;

public class DBUser {

	private static final String CLASS_NAME = DBUser.class.getName();
	private static final Logger LOGGER = ApplicationConfiguration.logger();

	private String username;
	private String password;
	private static DBUser instance;

	private DBUser() {
	}

	public static DBUser instance() {
		LOGGER.entering(CLASS_NAME, "instance");
		if (instance == null) {
			instance = new DBUser();
		}
		LOGGER.exiting(CLASS_NAME, "instance");
		return instance;
	}

	public String username() {
		LOGGER.entering(CLASS_NAME, "username");
		LOGGER.exiting(CLASS_NAME, "username", username);
		return username;
	}

	public void setUsername(String username) {
		LOGGER.entering(CLASS_NAME, "setUsername", username);
		this.username = username;
		LOGGER.exiting(CLASS_NAME, "setUsername");
	}

	public String password() {
		LOGGER.entering(CLASS_NAME, "password");
		LOGGER.exiting(CLASS_NAME, "password", "********");
		return password;
	}

	public void setPassword(String password) {
		LOGGER.entering(CLASS_NAME, "setPassword", "********");
		this.password = password;
		LOGGER.exiting(CLASS_NAME, "setPassword");
	}
}
