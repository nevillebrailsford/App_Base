package application.mail;

import java.util.logging.Logger;

import application.definition.ApplicationConfiguration;
import application.preferences.UserPreferences;

public class MailPreferences {
	private static final String CLASS_NAME = MailPreferences.class.getName();
	public final static Logger LOGGER = ApplicationConfiguration.logger();
	private final static String MAILING_LIST = "mailing.list";

	public static String mailingList() {
		LOGGER.entering(CLASS_NAME, "mailingList");
		String mailingList = "";
		UserPreferences preferences = UserPreferences
				.instance(ApplicationConfiguration.applicationDefinition().applicationName());
		if (preferences.preference(MAILING_LIST).isPresent()) {
			mailingList = preferences.preference(MAILING_LIST).get();
		}
		LOGGER.exiting(CLASS_NAME, "mailingList", mailingList);
		return mailingList;
	}

	public static void setMailingList(String list) {
		LOGGER.entering(CLASS_NAME, "setMailingList", list);
		UserPreferences preferences = UserPreferences
				.instance(ApplicationConfiguration.applicationDefinition().applicationName());
		preferences.setPreference(MAILING_LIST, list);
		LOGGER.exiting(CLASS_NAME, "setMailingList");
	}

	public static void clear() {
		LOGGER.entering(CLASS_NAME, "clear");
		UserPreferences preferences = UserPreferences
				.instance(ApplicationConfiguration.applicationDefinition().applicationName());
		try {
			preferences.clear();
		} catch (Exception e) {
			LOGGER.fine("MailPreferences: caught exception " + e.getMessage());
		}
		LOGGER.exiting(CLASS_NAME, "clear");
	}

}
