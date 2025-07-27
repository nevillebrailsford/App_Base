package application.security;

import java.util.logging.Logger;

import application.definition.ApplicationConfiguration;

/**
 * DGAdmin represents the SQL database administrator and is the user that
 * performs all the configuring of the user and application.
 */
public class DBAdmin {

	private static final String CLASS_NAME = DBAdmin.class.getName();
	private static final Logger LOGGER = ApplicationConfiguration.logger();

	private static DBAdmin instance = null;

	private String admin;
	private String password;
	private String url;
	private String database;

	private DBAdmin() {
	}

	public static DBAdmin instance() {
		LOGGER.entering(CLASS_NAME, "instance");
		if (instance == null) {
			instance = new DBAdmin();
		}
		LOGGER.exiting(CLASS_NAME, "instance");
		return instance;
	}

	public String admin() {
		LOGGER.entering(CLASS_NAME, "admin");
		LOGGER.exiting(CLASS_NAME, "admin", admin);
		return admin;
	}

	public void setAdmin(String admin) {
		LOGGER.entering(CLASS_NAME, "setAdmin", admin);
		this.admin = admin;
		LOGGER.exiting(CLASS_NAME, admin);
	}

	public String password() {
		LOGGER.entering(CLASS_NAME, "password");
		LOGGER.exiting(CLASS_NAME, "password", "************");
		return password;
	}

	public void setPassword(String password) {
		LOGGER.entering(CLASS_NAME, "setPassword", "************");
		this.password = password;
		LOGGER.exiting(CLASS_NAME, "setPassword");
	}

	public String url() {
		LOGGER.entering(CLASS_NAME, "url");
		LOGGER.exiting(CLASS_NAME, "url", url);
		return url;
	}

	public void setUrl(String url) {
		LOGGER.entering(CLASS_NAME, "setUrl", url);
		this.url = url;
		LOGGER.exiting(CLASS_NAME, "setUrl");
	}

	public String database() {
		LOGGER.entering(CLASS_NAME, "database");
		LOGGER.exiting(CLASS_NAME, "database", database);
		return database;
	}

	public void setDatabase(String database) {
		LOGGER.entering(CLASS_NAME, "setDatabase", database);
		this.database = database;
		LOGGER.exiting(CLASS_NAME, "setDatabase");
	}

}
