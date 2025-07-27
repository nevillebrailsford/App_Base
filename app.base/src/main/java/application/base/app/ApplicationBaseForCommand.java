package application.base.app;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

import application.archive.Archive;
import application.definition.ApplicationConfiguration;
import application.definition.ApplicationDefinition;
import application.definition.BaseConstants;
import application.inifile.IniFile;
import application.locking.LockManager;
import application.logging.LogConfigurer;
import application.notification.NotificationCentre;
import application.notification.NotificationListener;
import application.notification.NotificationMonitor;
import application.notification.NotificationType;
import application.security.DBSecurity;
import application.storage.LoadData;
import application.storage.LoadState;
import application.storage.Storage;
import application.storage.StorageNotificationType;
import application.storage.StoreDetails;
import application.thread.ThreadServices;
import application.timer.TimerService;

/**
 * <p>
 * This is the base class for all command line based applications in this
 * framework.
 * <p>
 * You can extend this class and get all the benefits of a graphical program
 * with facilities (such as logging, auditing, notification, storage) already
 * provided.
 * <p>
 * You define the name and working directory of the application by passing in
 * the following parameters as arguments to the <code>main</code>, which are
 * then forwarded to the <code>launch</code> method: <br>
 * <code>--name=</code> the name of the application. <br>
 * <code>--dir=</code> the path to the working directory. If not specified the
 * application will use the users <code>home</code> directory.
 * <p>
 * Your application will be called at <code>createApplicationDefinition</code>
 * to define this application's specific requirements. Once this call is
 * complete, the logging facility will be initialised.
 * <p>
 * Your application will next be called at <code>configureStoreDetails</code> to
 * define the storage requirements for this application.
 * <p>
 * If your application requires its model to be loaded into storage, this will
 * occur now, and this class will wait for the load to complete before
 * proceeding.
 * <p>
 * Finally, your application is called at <code>run</code> for your code to
 * perform its processing.
 * <p>
 * When your application finishes and returns from run, <code>terminate</code>
 * will be called so that your application can do any tidying up necessary.
 * <p>
 * After returning from this method, this application will close.
 * <p>
 * Information for using MySQL database. You must provide a properties file in
 * the root directory (as defined in --dir see above), that contains the
 * following properties and values. This file must be called
 * <code>security.properties</code><br>
 * <code>Administrator</code> the MySQL administrator user.<br>
 * <code>Password</code> the password for the administrator. <br>
 * <code>Key</code> the secret key used to encrypt all other passwords stored in
 * the database. This must be 16 characters in length.<br>
 * <code>Url</code> the JDBC URL to the MySQL server to be used by the
 * application.<br>
 * <code>Database</code> the name of the database that holds all the tables for
 * the application.
 * 
 * @see IApplication
 * 
 * @author neville
 * @version 4.1.0
 * 
 */
public abstract class ApplicationBaseForCommand implements IApplication {

	private static final String CLASS_NAME = ApplicationBaseForCommand.class.getName();
	public static Logger LOGGER;

	public Parameters parameters;

	protected Object loadComplete = new Object();
	protected Exception loadFailed = null;
	protected LoadData dataLoader;
	protected StoreDetails storeDetails;

	private static String mainClass = "";
	private static ApplicationBaseForCommand application;

	protected NotificationListener listener = ((notification) -> {
		NotificationType notificationType = notification.notificationType();
		if (notificationType == StorageNotificationType.Load) {
			Object state = notification.subject().get();
			if (state == LoadState.Complete) {
				synchronized (loadComplete) {
					loadComplete.notify();
				}
			}
			if (state == LoadState.Failed) {
				loadFailed = new Exception("Load has failed.");
				synchronized (loadComplete) {
					loadComplete.notify();
				}
			}
		}
	});

	/**
	 * The start of the application.
	 * <p>
	 * You define the name and working directory of the application by passing in
	 * the following parameters as arguments to the <code>main</code>, which you
	 * then forward to the <code>launch</code> method: <br>
	 * <code>--name=</code> the name of the application. <br>
	 * <code>--dir=</code> the path to the working directory. If not specified the
	 * application will use the users <code>home</code> directory.
	 * 
	 * @param args as described above
	 */
	public static void main(String[] args) {
		mainClass = getApplicationsMainClass();
		launch(args);
	}

	/**
	 * This call is made during the windowClosing process to allow the application
	 * to tidy up before the framework itself is shut down.
	 */
	public abstract void terminate();

	@Override
	public ApplicationDefinition createApplicationDefinition(Parameters parameters) {
		return null;
	}

	@Override
	public StoreDetails configureStoreDetails() {
		return null;
	}

	/**
	 * Call the applications run method. This is where the work will be done.
	 */
	public abstract void run();

	/**
	 * Get the parameters
	 * 
	 * @return the parameters
	 */
	public Parameters getParameters() {
		return parameters;
	}

	private static void launch(String[] args) {
		try {
			Class<?> c = Class.forName(mainClass);
			Constructor<?> con = c.getConstructor();
			application = (ApplicationBaseForCommand) con.newInstance();
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException | ClassNotFoundException e) {
			e.printStackTrace();
			System.err.println("ERROR: Unable to load application class.");
			System.exit(0);
		}
		application.createEnvironment(args);
		try {
			application.init();
			application.run();
			application.terminate();
			application.stop();
		} catch (Exception e) {
			String message = "Error during initialization: " + e.getMessage();
			ErrorReporter.displayError(message);
			System.exit(0);
		}
	}

	private void createEnvironment(String[] args) {
		parameters = new Parameters(args);
	}

	private void init() throws Exception {
		Parameters parameters = getParameters();
		if (ParametersUtility.invalidParameters(parameters)) {
			String message = "Usage: java -jar jar_name <--name=application name> <--dir=base directory>";
			ErrorReporter.displayError(message);
			System.exit(0);
		}
		configureApplication(parameters);
		if (!LockManager.lock()) {
			String message = "Another instance of " + ApplicationConfiguration.applicationDefinition().applicationName()
					+ " is running. This instance is stopping.";
			ErrorReporter.displayError(message);
			System.exit(0);
		}
		addShutDownHook();
		if (ApplicationConfiguration.applicationDefinition().requiresSecurity()
				&& !DBSecurity.processSecurityProperties()) {
			String message = "Unable to process security properties. This instance is stopping.";
			ErrorReporter.displayError(message);
			System.exit(0);
		}
		configureLogging();
		storeDetails = configureStoreDetails();
		LOGGER = ApplicationConfiguration.logger();
		if (!IniFile.value(BaseConstants.MONITORING).isBlank()) {
			boolean monitor = Boolean.parseBoolean(IniFile.value(BaseConstants.MONITORING));
			if (monitor)
				new NotificationMonitor(System.out);
		} else {
			IniFile.store(BaseConstants.MONITORING, "true");
			new NotificationMonitor(System.out);
		}
		if (ApplicationConfiguration.applicationDefinition().hasModelFile()) {
			NotificationCentre.addListener(listener);
			loadModelAndWait(storeDetails);
			NotificationCentre.removeListener(listener);
		}
	}

	private final void addShutDownHook() {
		Runnable runner = new Runnable() {
			@Override
			public void run() {
				LockManager.unlock();
			}
		};
		Runtime.getRuntime().addShutdownHook(new Thread(runner, "Application Hook"));
	}

	private void configureLogging() {
		LogConfigurer.setUp();
		String level = IniFile.value(BaseConstants.LOGGING_LEVEL);
		if (!level.isBlank()) {
			try {
				LogConfigurer.changeLevel(Level.parse(level));
			} catch (Exception e) {
				LogConfigurer.changeLevel(ApplicationConfiguration.applicationDefinition().level());
			}
		} else {
			LogConfigurer.changeLevel(ApplicationConfiguration.applicationDefinition().level());
		}
	}

	private void configureApplication(Parameters parameters) {
		ApplicationDefinition applicationDefinition = createApplicationDefinition(parameters);
		if (parameters.getNamed().containsKey(DIR)) {
			String baseDirectory = parameters.getNamed().get(DIR);
			ApplicationConfiguration.registerApplication(applicationDefinition, baseDirectory);
		} else {
			ApplicationConfiguration.registerApplication(applicationDefinition, System.getProperty("user.home"));
		}
	}

	/**
	 * Perform the initial archive and load of the model. This will be done on a
	 * separate thread.
	 * 
	 * @param storeDetails
	 * @throws Exception
	 */
	private void loadModelAndWait(StoreDetails storeDetails) throws Exception {
		LOGGER.entering(CLASS_NAME, "loadModelAndWait", storeDetails);
		if (storeDetails == null) {
			throw new IllegalArgumentException("storeDetails is null.");
		}
		new Thread(() -> {
			try {
				LOGGER.entering(CLASS_NAME, "loadModelAndWait.run");
				archiveExistingModel(storeDetails.modelDirectory(), storeDetails.modelFileName());
				if (!loadExistingModel(storeDetails.loadData(), storeDetails.modelDirectory(),
						storeDetails.modelFileName())) {
					synchronized (loadComplete) {
						LOGGER.fine("about to notify");
						loadComplete.notify();
						LOGGER.fine("notify complete");
					}
				}
			} catch (Exception e) {
				LOGGER.fine("Caught exception: " + e.getMessage());
				System.out.println("Exception " + e + " has occured.");
				LOGGER.exiting(CLASS_NAME, "loadModelAndWait.run");
				System.exit(0);
			}
			LOGGER.exiting(CLASS_NAME, "loadModelAndWait.run");
		}).start();
		synchronized (loadComplete) {
			try {
				LOGGER.fine("about to wait");
				loadComplete.wait();
				LOGGER.fine("wait ended");
			} catch (InterruptedException e) {
			}
		}
		if (loadFailed != null) {
			LOGGER.throwing(CLASS_NAME, "loadModelAndWait", loadFailed);
			throw loadFailed;
		}
		LOGGER.exiting(CLASS_NAME, "loadModelAndWait");
	}

	public void stop() throws Exception {
		LOGGER.entering(CLASS_NAME, "stop");
		TimerService.instance().stop();
		NotificationCentre.stop();
		ThreadServices.instance().executor().shutdown();
		LOGGER.exiting(CLASS_NAME, "stop");
		LogConfigurer.shutdown();
	}

	public void archiveExistingModel(String modelName, String propertyFileName) throws Exception {
		LOGGER.entering(CLASS_NAME, "archiveExistingModel", new Object[] { modelName, propertyFileName });
		File modelDirectory = obtainModelDirectory(modelName);
		File dataFile = new File(modelDirectory, propertyFileName);
		if (dataFile.exists()) {
			LOGGER.fine("Data file " + dataFile.getAbsolutePath() + " exists");
			try {
				Archive.archive(dataFile);
			} catch (Exception e) {
				LOGGER.fine("Caught exception " + e.getMessage());
				LOGGER.throwing(CLASS_NAME, "archiveExistingModel", e);
				LOGGER.exiting(CLASS_NAME, "archiveExistingModel");
				throw e;
			}
		} else {
			LOGGER.fine("Data file " + dataFile.getAbsolutePath() + " does not exist");
		}
		LOGGER.exiting(CLASS_NAME, "archiveExistingModel");
	}

	public boolean loadExistingModel(LoadData loadData, String modelName, String propertyFileName) {
		LOGGER.entering(CLASS_NAME, "loadExistingModel", new Object[] { modelName, propertyFileName });
		boolean success = false;
		File modelDirectory = obtainModelDirectory(modelName);
		File dataFile = new File(modelDirectory, propertyFileName);
		if (dataFile.exists()) {
			LOGGER.fine("Data file " + dataFile.getAbsolutePath() + " exists");
			loadData.setFileName(dataFile.getAbsolutePath());
			Storage storage = new Storage();
			try {
				storage.loadStoredData(loadData);
				success = true;
			} catch (IOException e) {
				LOGGER.fine("Caught exception " + e.getMessage());
			}
		} else {
			LOGGER.fine("Data file " + dataFile.getAbsolutePath() + " does not exist");
		}
		LOGGER.exiting(CLASS_NAME, "loadExistingModel", success);
		return success;
	}

	private File obtainModelDirectory(String modelName) {
		LOGGER.entering(CLASS_NAME, "obtainModelDirectory", modelName);
		File rootDirectory = ApplicationConfiguration.rootDirectory();
		File applicationDirectory = new File(rootDirectory,
				ApplicationConfiguration.applicationDefinition().applicationName());
		File modelDirectory = new File(applicationDirectory, modelName);
		if (modelDirectory.exists()) {
			LOGGER.fine("Model directory " + modelDirectory.getAbsolutePath() + " does exist");
		} else {
			LOGGER.fine("Model directory " + modelDirectory.getAbsolutePath() + " does not exist");
		}
		LOGGER.exiting(CLASS_NAME, "obtainModelDirectory", modelDirectory);
		return modelDirectory;
	}

	// Need to determine if we are running in a jar file or outside a jar. If in a
	// jar file, get the main class from the manifest file.
	private static String getApplicationsMainClass() {
		String command = System.getProperty("sun.java.command").split(" ")[0];
		if (command.endsWith(".jar")) {
			try {
				command = getMainClass();
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
		return command;
	}

	// Get the name of the main class from the manifest file in the jar.
	private static String getMainClass() throws Exception {
		return ApplicationDefinition.getFromManifest("Main-Class", ApplicationBaseForCommand.class)
				.orElse("Not present");
	}

}
