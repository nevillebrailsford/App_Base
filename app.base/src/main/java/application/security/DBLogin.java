package application.security;

import java.sql.SQLException;

import javax.swing.JOptionPane;

import application.base.app.gui.LoginFormGUI;

/**
 * Controls the logging in of a user.<br>
 */
public class DBLogin {
	private static DBLogin instance = null;

	private DBLogin() {
	}

	public static DBLogin instance() {
		if (instance == null) {
			instance = new DBLogin();
		}
		return instance;
	}

	/**
	 * A login panel is displayed where the user enters their user name and
	 * password<br>
	 * If the user has not registered themselves before they may do so by using the
	 * register option.<br>
	 * Once the user closes the login panel, their input is checked to make sure
	 * that all user name and password have been entered.<br>
	 * If all required data is entered the user is then registered for the
	 * controlling application.
	 * 
	 * @return true if user logs in and registers successfully, false otherwise
	 */
	public boolean login() {
		new LoginFormGUI("Login").setVisible(true);
		if (DBUser.instance().username() == null || DBUser.instance().password() == null) {
			System.out.println("Failure 1");
			return false;
		}
		try {
			return DBAccess.registerUserApplication();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Exception caught trying to register user");
		}
		return false;
	}

}
