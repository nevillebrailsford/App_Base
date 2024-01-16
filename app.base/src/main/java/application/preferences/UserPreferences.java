package application.preferences;

import java.util.Optional;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class UserPreferences {
	private static UserPreferences instance = null;

	private Preferences applicationPreferences = null;

	public synchronized static UserPreferences instance(String... nodeName) {
		if (instance == null) {
			if (nodeName.length == 0) {
				throw new IllegalArgumentException("UserPreferences: nodeName was null");
			} else if (nodeName.length > 1) {
				throw new IllegalArgumentException("UserPreferences: more than 1 nodeName was specified");
			}
			if (nodeName[0] == null || nodeName[0].isBlank() || nodeName[0].isEmpty()) {
				throw new IllegalArgumentException("UserPreferences: nodeName was null");
			}
			instance = new UserPreferences(nodeName[0]);
		}
		return instance;
	}

	private UserPreferences(String nodeName) {
		applicationPreferences = Preferences.userRoot().node(nodeName);
	}

	public void clear() throws BackingStoreException {
		applicationPreferences.clear();
		applicationPreferences.flush();
		instance = null;
	}

	public void setPreference(String preferenceName, String preferenceValue) {
		applicationPreferences.put(preferenceName, preferenceValue);
	}

	public Optional<String> preference(String preferenceName) {

		String preferenceValue = applicationPreferences.get(preferenceName, null);
		return Optional.ofNullable(preferenceValue);
	}

}
