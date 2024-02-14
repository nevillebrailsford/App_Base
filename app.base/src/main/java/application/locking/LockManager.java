package application.locking;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;

import application.definition.ApplicationConfiguration;

public class LockManager {
	private static File file;
	private static FileChannel channel;

	public static boolean lock() {
		File applicationRoot = ApplicationConfiguration.rootDirectory();
		String lockFileName = ApplicationConfiguration.applicationDefinition().applicationName() + ".lock";
		file = new File(applicationRoot, lockFileName);
		if (file.exists()) {
			return false;
		}
		file.deleteOnExit();
		try {
			channel = FileChannel.open(file.toPath(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
			channel.close();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
			return false;
		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	public static void unlock() {
		file.delete();
	}

}
