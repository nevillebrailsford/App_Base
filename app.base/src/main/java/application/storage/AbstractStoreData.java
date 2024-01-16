package application.storage;

import java.io.IOException;
import java.util.logging.Logger;

import application.definition.ApplicationConfiguration;
import application.notification.Notification;
import application.notification.NotificationCentre;

public abstract class AbstractStoreData implements StoreData {
	private final static String CLASS_NAME = AbstractStoreData.class.getName();
	public final static Logger LOGGER = ApplicationConfiguration.logger();

	private String fileName;

	/**
	 * Store the data on any file storage device. This method will run in its own
	 * thread.
	 * 
	 * @throws IllegalStateException if application has not been registered.
	 */
	@Override
	public void run() {
		if (LOGGER == null) {
			throw new IllegalStateException("AbstractStoreData - logger is null");
		}
		LOGGER.entering(CLASS_NAME, "run");
		LOGGER.fine("Acquiring readLock");
		StorageLock.readLock().lock();
		LOGGER.fine("Acquired readLock");
		boolean loading = LoadingState.isLoading();
		if (loading) {
			StorageLock.readLock().unlock();
			LOGGER.fine("Released readLock");
			LOGGER.exiting(CLASS_NAME, "run", loading);
			return;
		}
		LOGGER.fine("Releasing readLock");
		StorageLock.readLock().unlock();
		LOGGER.fine("Released readLock");
		LOGGER.fine("Acquiring writeLock");
		StorageLock.writeLock().lock();
		LOGGER.fine("Acquired writeLock");
		try {
			storeStarted();
			storeData();
			storeComplete();
		} catch (IOException e) {
			LOGGER.warning("caught exception: " + e.getMessage());
			storeFailed(e);
		} finally {
			LOGGER.fine("Releasing writeLock");
			StorageLock.writeLock().unlock();
			LOGGER.fine("Released writeLock");
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

	public abstract void storeData() throws IOException;

	private void storeStarted() {
		LOGGER.entering(CLASS_NAME, "storeStarted");
		NotificationCentre.broadcast(new Notification(StorageNotificationType.Store, this, StoreState.Started));
		LOGGER.exiting(CLASS_NAME, "storeStarted");
	}

	private void storeComplete() {
		LOGGER.entering(CLASS_NAME, "storeComplete");
		NotificationCentre.broadcast(new Notification(StorageNotificationType.Store, this, StoreState.Complete));
		LOGGER.exiting(CLASS_NAME, "storeComplete");
	}

	private void storeFailed(IOException e) {
		LOGGER.entering(CLASS_NAME, "storeFailed", e);
		NotificationCentre.broadcast(new Notification(StorageNotificationType.Store, this, StoreState.Failed));
		LOGGER.exiting(CLASS_NAME, "storeFailed");
	}

}
