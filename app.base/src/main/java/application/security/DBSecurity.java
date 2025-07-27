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
public class DBSecurity {

	private static final String SECURITY_PROPERTIES = "security.properties";

	/**
	 * Process database security information<br>
	 * 1. Check file exists<br>
	 * 2. Check information provided is valid<br>
	 * 3. Create DBAdmin object
	 * 
	 * @return true if all security information is present and correct, false
	 *         otherwise.
	 */
	public static boolean processSecurityProperties() {
		Properties prop = new Properties();
		File secFile = new File(ApplicationConfiguration.rootDirectory(), SECURITY_PROPERTIES);
		if (secFile.exists()) {
			try (FileReader reader = new FileReader(secFile)) {
				prop.load(reader);
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null,
						"Exception: loading " + SECURITY_PROPERTIES + " file: " + e.getMessage());
				return false;
			}
		} else {
			JOptionPane.showMessageDialog(null, "Error: " + SECURITY_PROPERTIES + " file not found");
			return false;
		}
		String administrator = prop.getProperty("Administrator", "");
		String password = prop.getProperty("Password", "");
		String key = prop.getProperty("Key", "");
		String url = prop.getProperty("Url", "");
		String database = prop.getProperty("Database", "");
		if (validateSecurityProperties(administrator, password, key, url, database)) {
			PasswordEncryptor passwordEncryptor = PasswordEncryptor.instance();
			passwordEncryptor.setKey(key);
			DBAdmin admin = DBAdmin.instance();
			admin.setAdmin(administrator);
			admin.setPassword(password);
			admin.setUrl(url);
			admin.setDatabase(database);
			return true;
		} else {
			return false;
		}

	}

	private static boolean validateSecurityProperties(String administrator, String password, String key, String url,
			String database) {
		if (administrator.length() == 0 || password.length() == 0 || key.length() == 0 || url.length() == 0
				|| database.length() == 0) {
			JOptionPane.showMessageDialog(null, "Error: " + SECURITY_PROPERTIES + " file is missing a property.");
			return false;
		}
		if (key.length() != 16) {
			JOptionPane.showMessageDialog(null,
					"Error: " + SECURITY_PROPERTIES + " file property Key not 16 characters long.");
			return false;
		}
		return true;
	}
}
