package application.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import application.definition.BaseConstants;

/**
 * <p>
 * The Util class provides some boiler plate functions used by other classes
 */
public class Util {
	/**
	 * Operating system - can be Windows or Mac
	 */
	public enum OS {
		WINDOWS, MAC
	};

	private static OS os = null;

	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(BaseConstants.displayDate);

	/**
	 * Determine the operating system. This works with Windows and MacOs systems.
	 * 
	 * @return the operating system
	 */
	public static OS getOS() {
		if (os == null) {
			String operSys = System.getProperty("os.name").toLowerCase();
			if (operSys.contains("win")) {
				os = OS.WINDOWS;
			} else if (operSys.contains("mac")) {
				os = OS.MAC;
			}
		}
		return os;
	}

	/**
	 * Format the date to a consistent pattern - dd/mm/yyyy
	 * 
	 * @param date - a LocalDate
	 * @return the formatted date
	 */
	public static String displayDate(LocalDate date) {
		return formatter.format(date);
	}

	/**
	 * Format the date to a consistent pattern - dd/mm/yyyy
	 * 
	 * @param date - a Date
	 * @return the formatted date
	 */
	public static String displayDate(Date date) {
		return formatter.format(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
	}
}
