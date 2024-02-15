package application.locking;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.StandardOpenOption;

import application.definition.ApplicationConfiguration;

public class LockManager {
	private static File file;
	private static FileChannel channel;

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

	public static void unlock() {
		file.delete();
	}

}
