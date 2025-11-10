package application.security;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

import javax.swing.JOptionPane;

import application.definition.ApplicationConfiguration;

/**
 * Validates database security information provided in the securities property
 * file.
 */
public class Security {

	private static final String PASSWORD_PROPERTIES = "password.properties";

	/**
	 * Process password security information<br>
	 * 1. Check file exists<br>
	 * 2. Set up PasswordEncryptor
	 * 
	 * @return true if all security information is present and correct, false
	 *         otherwise.
	 */
	public static boolean processPasswordProperties() {
		Properties prop = new Properties();
		File secFile = new File(ApplicationConfiguration.rootDirectory(), PASSWORD_PROPERTIES);
		if (secFile.exists()) {
			try (FileReader reader = new FileReader(secFile)) {
				prop.load(reader);
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null,
						"Exception: loading " + PASSWORD_PROPERTIES + " file: " + e.getMessage());
				return false;
			}
		} else {
			JOptionPane.showMessageDialog(null, "Error: " + PASSWORD_PROPERTIES + " file not found");
			return false;
		}
		String key = prop.getProperty("Key", "");
		if (validatePasswordProperties(key)) {
			PasswordEncryptor passwordEncryptor = PasswordEncryptor.instance();
			passwordEncryptor.setKey(key);
			return true;
		} else {
			return false;
		}

	}

	private static boolean validatePasswordProperties(String key) {
		if (key.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Error: " + PASSWORD_PROPERTIES + " file is missing a property.");
			return false;
		}
		if (key.length() != 16) {
			JOptionPane.showMessageDialog(null,
					"Error: " + PASSWORD_PROPERTIES + " file property Key not 16 characters long.");
			return false;
		}
		return true;
	}
}
