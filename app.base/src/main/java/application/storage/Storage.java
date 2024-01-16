package application.storage;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import application.definition.ApplicationConfiguration;
import application.definition.ApplicationDefinition;
import application.thread.ThreadServices;

public class Storage {
	private final static String CLASS_NAME = Storage.class.getName();
	private final static Logger LOGGER = ApplicationConfiguration.logger();

	private ApplicationDefinition applicationDefintiion;
	private ThreadServices executor;

	public Storage() {
		if (LOGGER == null) {
			throw new IllegalStateException("Storage - logger is null");
		}
		LOGGER.entering(CLASS_NAME, "init");
		applicationDefintiion = ApplicationConfiguration.applicationDefinition();
		if (applicationDefintiion == null) {
			throw new IllegalStateException("Storage - applicationDescriptor is null");
		}
		LOGGER.exiting(CLASS_NAME, "init");
	}

	public void loadStoredData(LoadData workerLoad) throws IOException {
		LOGGER.entering(CLASS_NAME, "loadStoredData");
		File storageFile = new File(workerLoad.fileName());
		if (!storageFile.exists()) {
			String message = storageFile.getAbsolutePath() + " cannot be found";
			LOGGER.fine(message);
			IOException exc = new IOException(message);
			LOGGER.throwing(CLASS_NAME, "loadStoredData", exc);
			LOGGER.exiting(CLASS_NAME, "loadStoredData");
			throw exc;
		}
		executor = ThreadServices.instance();
		executor.executor().execute(workerLoad);
		LOGGER.exiting(CLASS_NAME, "loadStoredData");
	}

	public void storeData(StoreData workerStore) {
		LOGGER.entering(CLASS_NAME, "storeData");
		executor = ThreadServices.instance();
		executor.executor().execute(workerStore);
		LOGGER.exiting(CLASS_NAME, "storeData");
	}

}
