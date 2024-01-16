package application.mail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import application.definition.ApplicationConfiguration;

public class MailConfigurer {
	private static final String CLASS_NAME = MailConfigurer.class.getName();
	public final static Logger LOGGER = ApplicationConfiguration.logger();

	private static MailConfigurer instance = null;

	private String username = null;
	private String password = null;
	private boolean validConfiguration = true;
	private Session session = null;

	public synchronized static MailConfigurer instance() {
		if (instance == null) {
			instance = new MailConfigurer();
		}
		instance.loadProperties();
		return instance;
	}

	private MailConfigurer() {
	}

	public Session session() {
		return session;
	}

	public boolean isValidConfiguration() {
		return validConfiguration;
	}

	private void loadProperties() {
		LOGGER.entering(CLASS_NAME, "loadProperties");
		Properties prop = new Properties();
		File rootDirectory = ApplicationConfiguration.rootDirectory();
		File mailFile = new File(rootDirectory, "mail.properties");
		try (FileReader reader = new FileReader(mailFile)) {
			prop.load(reader);
		} catch (FileNotFoundException e) {
			LOGGER.warning("Caught exception: " + e.getMessage());
			LOGGER.warning("Recording invalid configuration");
			validConfiguration = false;
			LOGGER.exiting(CLASS_NAME, "loadProperties");
			return;
		} catch (IOException e) {
			LOGGER.warning("Caught exception: " + e.getMessage());
			LOGGER.warning("Recording invalid configuration");
			validConfiguration = false;
			LOGGER.exiting(CLASS_NAME, "loadProperties");
			return;
		}
		username = prop.getProperty(MailConstants.USER_NAME, null);
		password = prop.getProperty(MailConstants.PASSWORD, null);
		if (username == null || password == null) {
			LOGGER.warning("Recording invalid configuration");
			validConfiguration = false;
			LOGGER.exiting(CLASS_NAME, "loadProperties");
			return;
		}
		session = createSession();
		LOGGER.exiting(CLASS_NAME, "loadProperties");
	}

	public String userName() {
		return username;
	}

	private Session createSession() {
		LOGGER.entering(CLASS_NAME, "createSession");
		Properties prop = new Properties();
		prop.put(MailConstants.SMTP_HOST, MailConstants.OUTLOOK_HOST);
		prop.put(MailConstants.SMTP_PORT, MailConstants.OUTLOOK_PORT);
		prop.put(MailConstants.SMTP_AUTH, "true");
		prop.put(MailConstants.SMTP_START_TLS, "true");

		session = Session.getInstance(prop, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		LOGGER.exiting(CLASS_NAME, "createSession");
		return session;
	}

}
