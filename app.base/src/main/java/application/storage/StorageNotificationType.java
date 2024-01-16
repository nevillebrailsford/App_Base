package application.storage;

import application.notification.NotificationType;

public enum StorageNotificationType implements NotificationType {
	Load("load"), Store("store");

	private String type;

	StorageNotificationType(String type) {
		this.type = type;
	}

	public String type() {
		return type;
	}

	@Override
	public String category() {
		return StorageConstants.STORAGE_CATEGORY;
	}

}
