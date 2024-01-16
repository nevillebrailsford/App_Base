package application.storage;

import java.io.IOException;
import java.util.logging.Logger;

import application.definition.ApplicationConfiguration;
import application.notification.Notification;
import application.notification.NotificationCentre;

public abstract class AbstractLoadData implements LoadData {
	private final static String CLASS_NAME = AbstractLoadData.class.getName();
	public final static Logger LOGGER = ApplicationConfiguration.logger();

	private String fileName;

	/**
	 * Load the data from any file storage device. This method will run in its own
	 * thread.
	 * 
	 * @throws IllegalStateException if application has not been registered.
	 */
	@Override
	public void run() {
		if (LOGGER == null) {
			throw new IllegalStateException("AbstractLoadData - logger is null");
		}
		LOGGER.entering(CLASS_NAME, "run");
		LOGGER.fine("Acquiring readLock");
		StorageLock.readLock().lock();
		LOGGER.fine("Acquired readLock");
		LoadingState.startLoading();
		try {
			loadStarted();
			readData();
			loadComplete();
		} catch (IOException e) {
			LOGGER.warning("caught exception: " + e.getMessage());
			loadFailed(e);
		} finally {
			LoadingState.stopLoading();
			LOGGER.fine("Releasing readLock");
			StorageLock.readLock().unlock();
			LOGGER.fine("Released readLock");
			LOGGER.exiting(CLASS_NAME, "run");
		}
	}

	@Override
	public void setFileName(String fileName) {
		LOGGER.entering(CLASS_NAME, "setFileName", fileName);
		this.fileName = fileName;
		LOGGER.exiting(CLASS_NAME, "setFileName");
	}

	@Override
	public String fileName() {
		LOGGER.entering(CLASS_NAME, "fileName");
		LOGGER.exiting(CLASS_NAME, "fileName", fileName);
		return fileName;
	}

	public abstract void readData() throws IOException;

	private void loadStarted() {
		LOGGER.entering(CLASS_NAME, "loadStarted");
		NotificationCentre.broadcast(new Notification(StorageNotificationType.Load, this, LoadState.Started));
		LOGGER.exiting(CLASS_NAME, "loadStarted");
	}

	private void loadComplete() {
		LOGGER.entering(CLASS_NAME, "loadComplete");
		NotificationCentre.broadcast(new Notification(StorageNotificationType.Load, this, LoadState.Complete));
		LOGGER.exiting(CLASS_NAME, "loadComplete");
	}

	private void loadFailed(IOException e) {
		LOGGER.entering(CLASS_NAME, "loadFailed", e);
		NotificationCentre.broadcast(new Notification(StorageNotificationType.Load, this, LoadState.Failed));
		LOGGER.exiting(CLASS_NAME, "loadFailed");
	}

}
