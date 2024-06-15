package application.notification;

import java.util.Optional;

public class Notification {
	private NotificationType notificationType;

	private Object source;

	private Object subject = null;

	/**
	 * 
	 * @param notificationType
	 * @param source
	 * @param subject
	 * @throws IllegalArgumentException if notificationType is null, source is null,
	 *                                  or there are more than 1 subject objects.
	 */
	public Notification(NotificationType notificationType, Object source, Object... subject) {
		if (notificationType == null) {
			throw new IllegalArgumentException("Notification - notification is null");
		}
		if (source == null) {
			throw new IllegalArgumentException("Notification - source is null");
		}
		if (subject.length > 1) {
			throw new IllegalArgumentException("Notification - too many subject objects");
		}
		this.notificationType = notificationType;
		this.source = source;
		if (subject.length == 1) {
			this.subject = subject[0];
		}
	}

	/**
	 * @return the notificationType
	 */
	public NotificationType notificationType() {
		return notificationType;
	}

	/**
	 * @return the source
	 */
	public Object source() {
		return source;
	}

	/**
	 * @return the subject
	 */
	public Optional<Object> subject() {
		return Optional.ofNullable(subject);
	}

	@Override
	/**
	 * @return the notification in string format
	 */
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(notificationType.getClass().getSimpleName()).append(".").append(notificationType).append(" ");
		builder.append(notificationType.category()).append(" ");
		builder.append(source.getClass().getSimpleName()).append(" ");
		if (subject == null) {
			builder.append("null");
		} else {
			builder.append(subject);
		}
		return builder.toString();
	}

}
