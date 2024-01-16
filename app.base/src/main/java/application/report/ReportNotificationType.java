package application.report;

import application.notification.NotificationType;

public enum ReportNotificationType implements NotificationType {
	Created("Created"), Failed("Failed");

	private String type;

	ReportNotificationType(String type) {
		this.type = type;
	}

	public String type() {
		return type;
	}

	@Override
	public String category() {
		return ReportConstants.REPORT_CATEGORY;
	}
}
