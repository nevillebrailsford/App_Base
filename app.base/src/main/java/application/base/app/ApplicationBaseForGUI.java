package application.base.app;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import application.archive.Archive;
import application.change.ChangeManager;
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
import application.preferences.PreferencesDialog;
import application.security.DBLogin;
import application.security.DBSecurity;
import application.storage.LoadData;
import application.storage.LoadState;
import application.storage.Storage;
import application.storage.StorageNotificationType;
import application.storage.StoreDetails;
import application.thread.ThreadServices;
import application.timer.TimerService;
import application.utils.Util;

/**
 * <p>
 * This is the base class for all Swing based applications in this framework.
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
 * Finally, your application is called at <code>start</code> for you to create
 * the graphical user interface within the provided <code>JFrame</code>.
 * <p>
 * Finally, the application window will be displayed on the screen.
 * <p>
 * When your application wants to stop it simply calls the <code>shutdown</code>
 * method. This method will call <code>terminate</code> so that your application
 * can do any tidying up necessary.
 * <p>
 * As a result, this application will close.
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
public abstract class ApplicationBaseForGUI extends JFrame implements IApplication {
	private static final long serialVersionUID = 1L;
	private static final String CLASS_NAME = ApplicationBaseForGUI.class.getName();
	private static String mainClass = "";
	private static ApplicationBaseForGUI application;

	public static Logger LOGGER;

	protected Object loadComplete = new Object();
	protected Exception loadFailed = null;
	protected LoadData dataLoader;

	private StoreDetails storeDetails;
	private Parameters parameters;

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

	@Override
	public void preferencesAction() {
		LOGGER.entering(CLASS_NAME, "preferencesAction");
		PreferencesDialog dialog = new PreferencesDialog(this);
		dialog.setVisible(true);
		dialog.dispose();
		LOGGER.exiting(CLASS_NAME, "preferencesAction");
	}

	@Override
	public void undoAction() {
		LOGGER.entering(CLASS_NAME, "undoAction");
		ThreadServices.instance().executor().submit(() -> {
			ChangeManager.instance().undo();
		});
		LOGGER.exiting(CLASS_NAME, "undoAction");
	}

	@Override
	public void redoAction() {
		LOGGER.entering(CLASS_NAME, "redoAction");
		ThreadServices.instance().executor().submit(() -> {
			ChangeManager.instance().redo();
		});
		LOGGER.exiting(CLASS_NAME, "redoAction");
	}

	@Override
	public void exitApplicationAction() {
		LOGGER.entering(CLASS_NAME, "exitApplicationAction");
		try {
			shutdown();
		} catch (Exception e) {
		}
		LOGGER.exiting(CLASS_NAME, "exitApplicationAction");
	}

	@Override
	public void helpAboutAction() {
		LOGGER.entering(CLASS_NAME, "helpAboutAction");
		String applicationName = ApplicationConfiguration.applicationDefinition().applicationName();
		String title = "About " + applicationName;
		String message = getBuildInformation(applicationName);
		JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
		LOGGER.exiting(CLASS_NAME, "helpAboutAction");
	}

	@Override
	public ApplicationDefinition createApplicationDefinition(Parameters parameters) {
		return null;
	}

	@Override
	public StoreDetails configureStoreDetails() {
		return null;
	}

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
		System.out.println(">main");
		mainClass = getApplicationsMainClass();
		System.setProperty("apple.eawt.quitStrategy", "CLOSE_ALL_WINDOWS");
		launch(args);
		System.out.println("<main");
	}

	/**
	 * This call is made during the windowClosing process to allow the application
	 * to tidy up before the framework itself is shut down.
	 */
	public abstract void terminate();

	/**
	 * Obtain the <code>Parameters</code> object that contains the arguments passed
	 * into <code>launch</code> in a more structured fashion.
	 * 
	 * @return the <code>Parameters</code> object.
	 */
	public Parameters getParameters() {
		return parameters;
	}

	/**
	 * Shut this application down.
	 */
	public void shutdown() {
		LOGGER.entering(CLASS_NAME, "shutdown");
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		LOGGER.exiting(CLASS_NAME, "shutdown");
	}

	private static void launch(String[] args) {
		System.out.println(">launch");
		try {
			Class<?> c = Class.forName(mainClass);
			Constructor<?> con = c.getConstructor();
			application = (ApplicationBaseForGUI) con.newInstance();
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException | ClassNotFoundException e) {
			e.printStackTrace();
			System.err.println("ERROR: Unable to load application class.");
			System.exit(0);
		}

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					application.createEnvironment(args);
				} catch (Throwable t) {
					System.out.println("Exception occurred: " + t);
					System.exit(0);
				}
			}
		});
		System.out.println("<launch");
	}

	private void createEnvironment(String[] args) {
		System.out.println(">createEnvironment");
		parameters = new Parameters(args);
		try {
			application.init();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error during initialization",
					JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					application.stop();
				} catch (Exception e1) {
					e1.printStackTrace();
					System.exit(0);
				}
			}
		});
		application.setTitle(ApplicationConfiguration.applicationDefinition().applicationName());
		application.start(this);
		application.setLocationRelativeTo(null);
		application.setVisible(true);
		System.out.println("<createEnvironment");
	}

	private void init() throws Exception {
		System.out.println(">init");
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
		if (ApplicationConfiguration.applicationDefinition().requiresSecurity()) {
			if (!DBLogin.instance().login()) {
				String message = "Cannot proceed without a successful login. This application is terminating.";
				ErrorReporter.displayError(message);
				System.exit(0);
			}
		}
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
		setLookAndFeel();
		if (ApplicationConfiguration.applicationDefinition().hasModelFile()) {
			NotificationCentre.addListener(listener);
			loadModelAndWait(storeDetails);
			NotificationCentre.removeListener(listener);
		}
		System.out.println("<init");
	}

	private void setLookAndFeel() {
		LOGGER.entering(CLASS_NAME, "setLookAndFeel");
		ApplicationConfiguration.applicationDefinition().topColor();
		if (ApplicationConfiguration.applicationDefinition().bottomColor().isPresent()) {
			Color menuBarBackground = ApplicationConfiguration.applicationDefinition().bottomColor().get();
			UIManager.put("MenuBar.background", menuBarBackground);
			UIManager.put("MenuBar.disabled", menuBarBackground.darker());
			UIManager.put("Menu.background", menuBarBackground);
			UIManager.put("Menu.disabled", menuBarBackground.darker());
			UIManager.put("RadioButtonMenuItem.background", menuBarBackground);
			UIManager.put("RadioButtonMenuItem.disabled", menuBarBackground.darker());
			UIManager.put("CheckBoxMenuItem.background", menuBarBackground);
			UIManager.put("CheckBoxMenuItem.disabled", menuBarBackground.darker());
			UIManager.put("MenuItem.background", menuBarBackground);
			UIManager.put("MenuItem.disabled", menuBarBackground.darker());
			UIManager.put("Menu.opaque", true);
			UIManager.put("Separator.background", menuBarBackground);
			UIManager.put("Separator.disabled", menuBarBackground.darker());
			UIManager.put("PopupMenu.background", menuBarBackground);
			UIManager.put("PopupMenu.disabled", menuBarBackground.darker());
			UIManager.put("PopupMenuSeparator.background", menuBarBackground);
			UIManager.put("PopupMenuSeparator.disabled", menuBarBackground.darker());
		}
		try {
			if (Util.getOS() == Util.OS.MAC) {
				System.setProperty("apple.laf.useScreenMenuBar", "true");
				System.setProperty("com.apple.mrj.application.apple.menu.about.name",
						ApplicationConfiguration.applicationDefinition().applicationName());
			}
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			LOGGER.fine("Caught exception: " + e.getMessage());
		}
		LOGGER.exiting(CLASS_NAME, "setLookAndFeel");
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

	private void stop() throws Exception {
		LOGGER.entering(CLASS_NAME, "stop");
		application.terminate();
		TimerService.instance().stop();
		NotificationCentre.stop();
		ThreadServices.instance().executor().shutdown();
		LOGGER.exiting(CLASS_NAME, "stop");
		LogConfigurer.shutdown();
	}

	private void archiveExistingModel(String modelName, String propertyFileName) throws Exception {
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

	private boolean loadExistingModel(LoadData loadData, String modelName, String propertyFileName) {
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

	private String getBuildInformation(String applicationName) {
		LOGGER.entering(applicationName, "getBuildInformation");
		String result = "";
		StringBuilder builder = new StringBuilder(applicationName);
		try {
			builder.append("\nBuild: ").append(ApplicationDefinition.getFromManifest("Build-Number", getClass())
					.orElse("Could not be determined"));
			builder.append("\nBuild Date: ").append(
					ApplicationDefinition.getFromManifest("Build-Date", getClass()).orElse("Could not be determined"));
			builder.append("\nVersion: ").append(ApplicationDefinition
					.getFromManifest("Application-Version", getClass()).orElse("Could not be determined"));
		} catch (Exception e) {
			builder.append("\nUnable to gather build version and date information\ndue to exception " + e.getMessage());
			LOGGER.fine("Caught exception: " + e.getMessage());
		}
		result = builder.toString();
		LOGGER.exiting(applicationName, "getBuildInformation", result);
		return result;
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
		return ApplicationDefinition.getFromManifest("Main-Class", ApplicationBaseForGUI.class).orElse("Not present");
	}

}
