package application.archive;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Logger;

import application.definition.ApplicationConfiguration;

/**
 * A class that provide the function to copy a model to an archive, that is to
 * save the model.
 * 
 * @author neville
 * @version 3.0.0
 */
public class Archive {
	private final static String CLASS_NAME = Archive.class.getName();
	public final static Logger LOGGER = ApplicationConfiguration.logger();

	/**
	 * Copies modelFile to archiveFile.
	 * 
	 * @param modelFile - a File that is the model to archive
	 * @return archiveFile - a File that is the archive file to copy
	 * @throws IOException              - when an error occurs while reading the
	 *                                  model file or writing the archive file.
	 * @throws IllegalStateException    if application not registered.
	 * @throws IllegalArgumentException if modelFile is null, or modelFile does not
	 *                                  exist.
	 */
	public synchronized static File archive(File modelFile) throws IOException {
		if (modelFile == null) {
			throw new IllegalArgumentException("Archive - modelFile is null");
		}
		if (!modelFile.exists()) {
			throw new IllegalArgumentException("Archive - modelFile does not exist");
		}
		if (LOGGER == null) {
			throw new IllegalStateException("Archive - application has not been registered");
		}
		LOGGER.entering(CLASS_NAME, "archive", modelFile);
		File archiveDirectory = new File(ApplicationConfiguration.applicationDefinition().archiveDirectory());
		if (!archiveDirectory.exists()) {
			LOGGER.fine("Creating directory " + archiveDirectory.getAbsolutePath());
			archiveDirectory.mkdirs();
		}
		File archiveFile = new File(ApplicationConfiguration.applicationDefinition().archiveFile());
		try {
			Files.copy(modelFile.toPath(), archiveFile.toPath());
		} catch (IOException e) {
			LOGGER.warning("Archive: exception caught " + e);
			LOGGER.throwing(CLASS_NAME, "archive", e);
			throw e;
		} finally {
			LOGGER.exiting(CLASS_NAME, "archive", archiveFile);
		}
		return archiveFile;
	}

}
