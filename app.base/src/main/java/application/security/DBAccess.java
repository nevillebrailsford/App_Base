package application.security;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import application.definition.ApplicationConfiguration;

/**
 * DBAccess is a utility class that performs user registration, user
 * verification, user authentication functions against the SQL database.
 */
public class DBAccess {

	private static final String CLASS_NAME = DBAccess.class.getName();
	private static final Logger LOGGER = ApplicationConfiguration.logger();

	/**
	 * Record a user name and password in the database. This inserts the user name
	 * and encrypted password in the table users.
	 * 
	 * @param username the chosen user name
	 * @param password the encrypted password chosen by the user
	 * @return true if the operation completed successfully
	 * @throws SQLException
	 */
	public static boolean register(String username, String password) throws SQLException {
		LOGGER.entering(CLASS_NAME, "register", username);
		if (!checkUser(username)) {
			String admin = DBAdmin.instance().admin();
			String adminPassword = DBAdmin.instance().password();
			String url = DBAdmin.instance().url() + "/" + DBAdmin.instance().database();
			try (Connection connection = DriverManager.getConnection(url, admin, adminPassword)) {
				PreparedStatement insertUser = connection.prepareStatement(
						"INSERT INTO " + SecutiryConstants.DB_USERS_TABLE_NAME + " (username, password) VALUE(?,?)");
				insertUser.setString(1, username);
				insertUser.setString(2, password);

				insertUser.executeUpdate();
			} catch (SQLException e) {
				LOGGER.warning("Caught exception: " + e.getMessage());
				LOGGER.throwing(CLASS_NAME, "register", e);
				LOGGER.exiting(CLASS_NAME, "register");
				throw e;
			}
		}
		LOGGER.exiting(CLASS_NAME, "register", "true");
		return true;
	}

	/**
	 * Check if the user is already registered in the users database.
	 * 
	 * @param username the user name to check
	 * @return true if user name exists in the users table, false otherwise.
	 * @throws SQLException
	 */
	public static boolean checkUser(String username) throws SQLException {
		LOGGER.entering(CLASS_NAME, "checkUser", username);
		String admin = DBAdmin.instance().admin();
		String adminPassword = DBAdmin.instance().password();
		String url = DBAdmin.instance().url() + "/" + DBAdmin.instance().database();
		try (Connection connection = DriverManager.getConnection(url, admin, adminPassword)) {
			PreparedStatement checkUserExists = connection
					.prepareStatement("SELECT * FROM " + SecutiryConstants.DB_USERS_TABLE_NAME + " WHERE username = ?");
			checkUserExists.setString(1, username);
			ResultSet resultSet = checkUserExists.executeQuery();
			if (!resultSet.isBeforeFirst()) {
				LOGGER.exiting(CLASS_NAME, "checkUser", false);
				return false;
			}
		} catch (SQLException e) {
			LOGGER.warning("Caught exception: " + e.getMessage());
			LOGGER.throwing(CLASS_NAME, "checkUser", e);
			LOGGER.exiting(CLASS_NAME, "checkUser");
			throw e;
		}
		LOGGER.exiting(CLASS_NAME, "checkUser", true);
		return true;
	}

	/**
	 * Validate the user name and password match the data stored for the user name.
	 * 
	 * @param username the chosen user name
	 * @param password the encrypted password chosen by the user
	 * @return if the user name and password match, false otherwise.
	 * @throws SQLException
	 */
	public static boolean validateLogin(String username, String password) throws SQLException {
		LOGGER.entering(CLASS_NAME, "validateLogin", username);
		String admin = DBAdmin.instance().admin();
		String adminPassword = DBAdmin.instance().password();
		String url = DBAdmin.instance().url() + "/" + DBAdmin.instance().database();
		try (Connection connection = DriverManager.getConnection(url, admin, adminPassword)) {
			PreparedStatement validateUser = connection.prepareStatement(
					"SELECT * FROM " + SecutiryConstants.DB_USERS_TABLE_NAME + " WHERE username = ? AND password = ?");
			validateUser.setString(1, username);
			validateUser.setString(2, password);

			ResultSet resultSet = validateUser.executeQuery();
			if (!resultSet.isBeforeFirst()) {
				LOGGER.exiting(CLASS_NAME, "validateLogin", false);
				return false;
			}
		} catch (SQLException e) {
			LOGGER.warning("Caught exception: " + e.getMessage());
			LOGGER.throwing(CLASS_NAME, "validateLogin", e);
			LOGGER.exiting(CLASS_NAME, "validateLogin");
			throw e;
		}
		LOGGER.exiting(CLASS_NAME, "validateLogin", true);
		return true;
	}

	/**
	 * Register a user with the running application, if not already performed. This
	 * operation involves the following steps:<br>
	 * 1. Insert user and application into table userapplications<br>
	 * 2. Create an SQL User for the users user name<br>
	 * 3. Apply permissions for the user to the applications tables<br>
	 * 4. Flush the permissions
	 * 
	 * @return true if all successful, false otherwise.
	 * @throws SQLException
	 */
	public static boolean registerUserApplication() throws SQLException {
		boolean result = false;
		LOGGER.entering(CLASS_NAME, "registerUserApplication");
		String admin = DBAdmin.instance().admin();
		String adminPassword = DBAdmin.instance().password();
		String url = DBAdmin.instance().url() + "/" + DBAdmin.instance().database();
		try (Connection connection = DriverManager.getConnection(url, admin, adminPassword)) {
			result = registerUserForApplication(connection);
		} catch (SQLException e) {
			LOGGER.warning("Caught exception: " + e.getMessage());
			LOGGER.throwing(CLASS_NAME, "registerUserApplication", e);
			LOGGER.exiting(CLASS_NAME, "registerUserApplication");
			throw e;
		}
		LOGGER.exiting(CLASS_NAME, "registerUserApplication", result);
		return result;
	}

	private static boolean registerUserForApplication(Connection connection) throws SQLException {
		LOGGER.entering(CLASS_NAME, "registerUserForApplication");
		try {
			if (!userIsRegisteredForThisApplication(connection)) {
				connection.setAutoCommit(false);
				int user_id = getIdForUser(connection);
				int app_id = getIdForApplication(connection);
				registerUserForApplication(connection, user_id, app_id);
				createUser(connection);
				grantPermissionsForUser(connection);
				connection.commit();
			}
		} catch (SQLException e) {
			LOGGER.warning("Caught exeception: " + e.getMessage());
			LOGGER.throwing(CLASS_NAME, "registerUserForApplication", e);
			LOGGER.exiting(CLASS_NAME, "registerUserForApplication");
			throw e;
		}
		LOGGER.exiting(CLASS_NAME, "registerUserForApplication", true);
		return true;
	}

	private static boolean userIsRegisteredForThisApplication(Connection connection) throws SQLException {
		int count = 0;
		LOGGER.entering(CLASS_NAME, "userIsRegisteredForThisApplication");
		try {
			PreparedStatement registerUser = connection.prepareStatement("SELECT COUNT(*) FROM "
					+ SecutiryConstants.DB_REGISTRATIONS_TABLE_NAME + " WHERE username= ? AND appname = ?");
			registerUser.setString(1, DBUser.instance().username());
			registerUser.setString(2, ApplicationConfiguration.applicationDefinition().applicationName());
			ResultSet resultSet = registerUser.executeQuery();
			if (resultSet.next()) {
				count = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			LOGGER.warning("Caught exception: " + e.getMessage());
			LOGGER.throwing(CLASS_NAME, "userIsRegisteredForThisApplication", e);
			LOGGER.exiting(CLASS_NAME, "userIsRegisteredForThisApplication");
			throw e;
		}
		LOGGER.exiting(CLASS_NAME, "userIsRegisteredForThisApplication", count > 0);
		return count > 0;
	}

	private static int getIdForUser(Connection connection) throws SQLException {
		int user_id = 0;
		LOGGER.entering(CLASS_NAME, "getIdForUser");
		try {
			PreparedStatement getUserid = connection.prepareStatement("select (idusers) from users where username = ?");
			getUserid.setString(1, DBUser.instance().username());
			ResultSet resultSet = getUserid.executeQuery();
			if (resultSet.next()) {
				user_id = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			LOGGER.warning("Caught exception: " + e.getMessage());
			LOGGER.throwing(CLASS_NAME, "getIdForUser", e);
			LOGGER.exiting(CLASS_NAME, "getIdForUser");
			throw e;
		}
		LOGGER.exiting(CLASS_NAME, "getIdForUser", user_id);
		return user_id;
	}

	private static int getIdForApplication(Connection connection) throws SQLException {
		int app_id = 0;
		LOGGER.entering(CLASS_NAME, "getIdForApplication");
		try {
			PreparedStatement getAppId = connection
					.prepareStatement("select (idapps) from applications where appname = ?");
			getAppId.setString(1, ApplicationConfiguration.applicationDefinition().applicationName());
			ResultSet resultSet = getAppId.executeQuery();
			if (resultSet.next()) {
				app_id = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			LOGGER.warning("Caught exception: " + e.getMessage());
			LOGGER.throwing(CLASS_NAME, "getIdForApplication", e);
			LOGGER.exiting(CLASS_NAME, "getIdForApplication");
			throw e;
		}
		LOGGER.exiting(CLASS_NAME, "getIdForApplication", app_id);
		return app_id;
	}

	private static void registerUserForApplication(Connection connection, int user_id, int app_id) throws SQLException {
		LOGGER.entering(CLASS_NAME, "registerUserForApplication",
				new Object[] { Integer.toString(user_id), Integer.toString(app_id) });
		try {
			PreparedStatement addRegistration = connection
					.prepareStatement("INSERT INTO userapplications(user_id, app_id) VALUE (?,?)");
			addRegistration.setInt(1, user_id);
			addRegistration.setInt(2, app_id);
			addRegistration.executeUpdate();
		} catch (SQLException e) {
			LOGGER.warning("Caught exception: " + e.getMessage());
			LOGGER.throwing(CLASS_NAME, "registerUserForApplication", e);
			LOGGER.exiting(CLASS_NAME, "registerUserForApplication");
			throw e;
		}
		LOGGER.exiting(CLASS_NAME, "registerUserForApplication");
	}

	private static void createUser(Connection connection) throws SQLException {
		LOGGER.entering(CLASS_NAME, "createUser");
		try {
			PreparedStatement addUser = connection
					.prepareStatement("CREATE USER if NOT EXISTS ? @'localhost' IDENTIFIED BY ?");
			addUser.setString(1, DBUser.instance().username());
			addUser.setString(2, DBUser.instance().password());
			addUser.executeUpdate();
		} catch (SQLException e) {
			LOGGER.warning("Caught exception: " + e.getMessage());
			LOGGER.throwing(CLASS_NAME, "createUser", e);
			LOGGER.exiting(CLASS_NAME, "createUser");
			throw e;
		}
		LOGGER.exiting(CLASS_NAME, "createUser");
	}

	private static void grantPermissionsForUser(Connection connection) throws SQLException {
		LOGGER.entering(CLASS_NAME, "grantPermissionsForUser");
		try {
			List<String> tables = getTablesForApplication(connection);
			for (String table : tables) {
				grantPrivlegesForUserOnTable(connection, table);
			}
			flushPriveleges(connection);
		} catch (SQLException e) {
			LOGGER.warning("Caught exception: " + e.getMessage());
			LOGGER.throwing(CLASS_NAME, "grantPermissionsForUser", e);
			LOGGER.exiting(CLASS_NAME, "grantPermissionsForUser");
			throw e;
		}
		LOGGER.exiting(CLASS_NAME, "grantPermissionsForUser");
	}

	private static List<String> getTablesForApplication(Connection connection) throws SQLException {
		List<String> result = new ArrayList<>();
		LOGGER.entering(CLASS_NAME, "getTablesForApplication");
		try {
			PreparedStatement getTables = connection
					.prepareStatement("SELECT (tablename) from dependencies where appname = ?");
			getTables.setString(1, ApplicationConfiguration.applicationDefinition().applicationName());
			ResultSet resultSet = getTables.executeQuery();
			while (resultSet.next()) {
				String table = resultSet.getString(1);
				result.add(table);
			}
		} catch (SQLException e) {
			LOGGER.warning("Caught exception: " + e.getMessage());
			LOGGER.throwing(CLASS_NAME, "getTablesForApplication", e);
			LOGGER.exiting(CLASS_NAME, "getTablesForApplication");
			throw e;
		}
		LOGGER.exiting(CLASS_NAME, "getTablesForApplication", result);
		return result;
	}

	private static void grantPrivlegesForUserOnTable(Connection connection, String table) throws SQLException {
		LOGGER.entering(CLASS_NAME, "grantPrivlegesForUserOnTable", table);
		try {
			String stmt = "GRANT ALL privileges ON " + DBAdmin.instance().database() + "." + table + " TO '"
					+ DBUser.instance().username() + "'@'localhost';";
			Statement grantPriveleges = connection.createStatement();
			grantPriveleges.execute(stmt);
		} catch (SQLException e) {
			LOGGER.warning("Caught exception: " + e.getMessage());
			LOGGER.throwing(CLASS_NAME, "grantPrivlegesForUserOnTable", e);
			LOGGER.exiting(CLASS_NAME, "grantPrivlegesForUserOnTable");
			throw e;
		}
		LOGGER.exiting(CLASS_NAME, "grantPrivlegesForUserOnTable");
	}

	private static void flushPriveleges(Connection connection) throws SQLException {
		LOGGER.entering(CLASS_NAME, "flushPriveleges");
		try {
			Statement flushPermission = connection.createStatement();
			flushPermission.execute("FLUSH PRIVILEGES");
		} catch (SQLException e) {
			LOGGER.warning("Caught exception: " + e.getMessage());
			LOGGER.throwing(CLASS_NAME, "flushPriveleges", e);
			LOGGER.exiting(CLASS_NAME, "flushPriveleges");
			throw e;
		}
		LOGGER.exiting(CLASS_NAME, "flushPriveleges");
	}

}
