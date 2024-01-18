package application.utils;

public class Util {
	public enum OS {
		WINDOWS, MAC
	};

	private static OS os = null;

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
}
