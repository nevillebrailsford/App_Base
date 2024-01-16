package application.notification;

public enum TestNotificationType implements NotificationType {
	Test("Test");

	private String type;

	TestNotificationType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return type;
	}

	@Override
	public String category() {
		return "testcategory";
	}

}
