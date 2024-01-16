package application.notification;

import application.definition.BaseConstants;

public enum UTNotificationType implements NotificationType {
	Ticked("ticked");

	private String type;

	UTNotificationType(String type) {
		this.type = type;
	}

	public String type() {
		return type;
	}

	@Override
	public String category() {
		return BaseConstants.TIMER_CATEGORY;
	}

}
