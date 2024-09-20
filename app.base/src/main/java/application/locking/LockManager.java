package application.locking;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.StandardOpenOption;

import application.definition.ApplicationConfiguration;

/**
 * Provide a mechanism to indicate that the application is in use.
 * 
 * @author neville
 * @version 3.0.0
 */

public class LockManager {
	private static File file;
	private static FileChannel channel;

	/**
	 * Use a file to provide a locking mechanism for the application. If the file
	 * exists, it means that the application is already running. The file is placed
	 * within the working directory of the application, so should work across
	 * multiple machines.
	 * 
	 * @return true if the file was created successfully, false otherwise.
	 */
	public static boolean lock() {
		File applicationRoot = ApplicationConfiguration.rootDirectory();
		String lockFileName = ApplicationConfiguration.applicationDefinition().applicationName() + ".lock";
		file = new File(applicationRoot, lockFileName);
		try {
			channel = FileChannel.open(file.toPath(), StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
			channel.close();
		} catch (FileAlreadyExistsException ex) {
			// this means the application is already running
			return false;
		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
		}
		file.deleteOnExit();
		return true;
	}

	/**
	 * Release the lock, by deleting the file.
	 */
	public static void unlock() {
		file.delete();
	}

}
