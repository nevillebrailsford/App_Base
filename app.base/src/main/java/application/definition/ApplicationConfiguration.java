package application.definition;

import java.io.File;
import java.util.logging.Logger;

public class ApplicationConfiguration {
	private static ApplicationDefinition registeredApplication = null;
	private static Logger logger = null;
	private static File rootDir = null;
	private static int executorServiceThreads = 5;

	/**
	 * Register the application class for this app.
	 * 
	 * @param applicationDefinition
	 * @param rootDirectory
	 * @throws IllegalArgumentException if application is null, or rootDirectory is
	 *                                  null or empty
	 * @throws IllegalSTateException    if an application has already been
	 *                                  registered
	 */
	public synchronized static void registerApplication(ApplicationDefinition applicationDefinition,
			String rootDirectory) {
		if (applicationDefinition == null) {
			throw new IllegalArgumentException("ApplicationConfiguration - application is null");
		}
		if (rootDirectory == null) {
			throw new IllegalArgumentException("ApplicationConfiguration - rootDirectory is null");
		}
		if (rootDirectory.trim().isEmpty()) {
			throw new IllegalArgumentException("ApplicationConfiguration - rootDirectory is empty");
		}
		if (registeredApplication != null) {
			throw new IllegalStateException("ApplicationConfiguration - registeredApplication is not null");
		}
		registeredApplication = applicationDefinition;
		logger = Logger.getLogger(registeredApplication.loggerName());
		rootDir = new File(rootDirectory);
	}

	/**
	 * Retrieve the application registered for this app.
	 * 
	 * @return application
	 * @throws IllegalStateException if no application has been registered.
	 */
	public synchronized static ApplicationDefinition applicationDefinition() {
		if (registeredApplication == null) {
			throw new IllegalStateException("ApplicationConfiguration - registeredApplication is null");
		}
		return registeredApplication;
	}

	/**
	 * Clear the registration
	 */
	public synchronized static void clear() {
		registeredApplication = null;
		logger = null;
		rootDir = null;
	}

	/**
	 * Check if application definition has been registered with the configuration.
	 * 
	 * @return registered or not
	 */
	public synchronized static boolean isDefinitionRegistered() {
		return registeredApplication != null;
	}

	/**
	 * Retrieve the logger to be used by all classes in the application.
	 * 
	 * @return logger
	 * @throws IllegalStateException if application has not been registered.
	 */
	public synchronized static Logger logger() {
		if (logger == null) {
			throw new IllegalStateException("ApplicationConfiguration - logger is null");
		}
		return logger;
	}

	/**
	 * Retrieve the rootDirectory for this application.
	 * 
	 * @return rootDirectory
	 * @throws IllegalStateException if application has not been registered.
	 */
	public synchronized static File rootDirectory() {
		if (rootDir == null) {
			throw new IllegalStateException("ApplicationConfiguration - rootDirectory is null");
		}
		return rootDir;
	}

	/**
	 * This is the number of ExecutorService threads required in the thread pool.
	 * This defaults to 5.
	 * 
	 * @return number of threads.
	 */
	public static int executorServiceThreads() {
		return executorServiceThreads;
	}

	/**
	 * Change the number of threads that the executor service will use. Once the
	 * executor service has been created, this call will have no effect, however.
	 * 
	 * @param number
	 */
	public static void setExecutorServiceThreads(int number) {
		ApplicationConfiguration.executorServiceThreads = number;
	}
}
