package application.timer;

import application.definition.BaseConstants;
import application.notification.NotificationType;

public enum TimerNotificationType implements NotificationType {
	Ticked("ticked");

	private String type;

	TimerNotificationType(String type) {
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
