package application.mail;

import java.time.LocalDate;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import application.definition.ApplicationConfiguration;
import application.inifile.IniFile;
import application.notification.Notification;
import application.notification.NotificationCentre;

public class MailSender implements Runnable {
	private static final String CLASS_NAME = MailSender.class.getName();
	public final static Logger LOGGER = ApplicationConfiguration.logger();

	private String text;

	public MailSender(String text) {
		this.text = text;
	}

	@Override
	public void run() {
		LOGGER.entering(CLASS_NAME, "run");
		MailConfigurer configurer = MailConfigurer.instance();
		if (configurer.isValidConfiguration()) {
			Session session = configurer.session();
			Message message = new MimeMessage(session);
			try {
				String recipients = IniFile.value(MailConstants.EMAIL_LIST);
				message.setFrom(new InternetAddress(configurer.userName()));
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
				String subject = ApplicationConfiguration.applicationDefinition().applicationName() + " Report";
				message.setSubject(subject);
				message.setText(text);
				Transport.send(message);
				Notification notification = new Notification(MailNotificationType.Sent, this,
						"Email sent successfully");
				NotificationCentre.broadcast(notification);
				LocalDate now = LocalDate.now();
				IniFile.store(MailConstants.DATE_OF_LAST_EMAIL, now.toString());
			} catch (Exception e) {
				LOGGER.warning("Caught exception: " + e.getMessage());
				Notification notification = new Notification(MailNotificationType.Failed, this,
						"Email send attempt failed");
				NotificationCentre.broadcast(notification);
			}
		} else {
			LOGGER.warning("EmailConfiguration not valid");
		}
		LOGGER.exiting(CLASS_NAME, "run");
	}

}
