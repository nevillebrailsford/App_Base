package application.inifile;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Properties;

import application.definition.ApplicationConfiguration;
import application.definition.ApplicationDefinition;

/**
 * The file to use to store the applications preferences.
 * 
 * @author neville
 * @version 3.0.0
 */
public class IniFile {
	private static Properties values = null;

	/**
	 * Retrieve the value of a property stored in an ini file.
	 * 
	 * @param key
	 * @return corresponding value
	 * @throws IllegalArgumentException if key is null or empty
	 */
	public static synchronized String value(String key) {
		if (key == null) {
			throw new IllegalArgumentException("IniFile: key was null");
		}
		if (key.trim().isEmpty()) {
			throw new IllegalArgumentException("IniFile: key was empty");
		}
		loadProperties();
		String result = values.getProperty(key);
		return result == null ? "" : result;
	}

	/**
	 * Store a property in an ini file.
	 * 
	 * @param key
	 * @param value
	 * @throws IllegalArgumentException if either key or value is null, or if either
	 *                                  is empty
	 */
	public static synchronized void store(String key, String value) {
		if (key == null) {
			throw new IllegalArgumentException("IniFile: key was null");
		}
		if (key.trim().isEmpty()) {
			throw new IllegalArgumentException("IniFile: key was empty");
		}
		if (value == null) {
			throw new IllegalArgumentException("IniFile: value was null");
		}
		if (value.trim().isEmpty()) {
			throw new IllegalArgumentException("IniFile: value was empty");
		}
		loadProperties();
		values.put(key, value);
		saveProperties();
	}

	/**
	 * Clear the properties. Generally a method to be used in testing.
	 */
	public static synchronized void clear() {
		values = null;
	}

	private static void createProperties() {
		values = new Properties();
	}

	private static void loadProperties() {
		if (values != null) {
			return;
		}
		createProperties();
		ApplicationDefinition applicationDefinition = ApplicationConfiguration.applicationDefinition();
		File iniFile = new File(applicationDefinition.iniFile());
		if (!iniFile.exists()) {
			File iniFileDirectory = new File(applicationDefinition.iniFileDirectory());
			if (!iniFileDirectory.exists()) {
				iniFileDirectory.mkdirs();
			}
			return;
		}
		try (FileReader file = new FileReader(iniFile)) {
			values.load(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void saveProperties() {
		if (values == null) {
			return;
		}
		ApplicationDefinition application = ApplicationConfiguration.applicationDefinition();
		File iniFileDirectory = new File(application.iniFileDirectory());
		if (!iniFileDirectory.exists()) {
			iniFileDirectory.mkdirs();
		}
		File iniFile = new File(application.iniFile());
		try (FileWriter file = new FileWriter(iniFile)) {
			values.store(file, "Properties written by " + application.applicationName());
			file.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
