package application.base.app;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

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
import application.storage.LoadData;
import application.storage.LoadState;
import application.storage.Storage;
import application.storage.StorageNotificationType;
import application.storage.StoreDetails;
import application.thread.ThreadServices;
import application.timer.TimerService;
import application.utils.Util;

public abstract class ApplicationBaseForGUI extends JFrame {
	private static final long serialVersionUID = 1L;

	private static final String CLASS_NAME = ApplicationBaseForGUI.class.getName();
	public static Logger LOGGER;

	public static final String DIR = "dir";
	public static final String NAME = "name";

	protected Object loadComplete = new Object();
	protected Exception loadFailed = null;
	protected LoadData dataLoader;
	protected StoreDetails storeDetails;

	private Parameters parameters;
	private static ApplicationBaseForGUI application;

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
	 * 
	 * @param args
	 */
	public static void launch(String[] args) {
		Exception ex = new Exception();
		StackTraceElement stackInfo = ex.getStackTrace()[ex.getStackTrace().length - 1];
		String className = stackInfo.getClassName();
		try {
			Class<?> c = Class.forName(className);
			Constructor<?> con = c.getConstructor();
			application = (ApplicationBaseForGUI) con.newInstance();
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException | ClassNotFoundException e) {
			e.printStackTrace();
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
	}

	private void createEnvironment(String[] args) {
		parameters = new Parameters(args);
		try {
			application.init();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error during initialization",
					JOptionPane.ERROR_MESSAGE);
		}
		application.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		application.addWindowListener(new WindowAdapter() {
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
		application.start(application);
		application.setLocationRelativeTo(null);
		application.setVisible(true);
	}

	public Parameters getParameters() {
		return parameters;
	}

	private void init() throws Exception {
		Parameters parameters = getParameters();
		if (invalidParameters(parameters)) {
			System.out.println("Usage: java -jar jar_name <--name=application name> <--dir=base directory>");
			System.exit(0);
		}
		configureApplication(parameters);
		if (!LockManager.lock()) {
			String message = "Another instance of " + ApplicationConfiguration.applicationDefinition().applicationName()
					+ " is running. This instance is stopping.";
			System.out.println(message);
			JOptionPane.showMessageDialog(null, message);
			System.exit(0);
		}
		addShutDownHook();
		configureLogging();
		configureStoreDetails();
		LOGGER = ApplicationConfiguration.logger();
		if (!IniFile.value(BaseConstants.MONITORING).isBlank()) {
			boolean monitor = Boolean.parseBoolean(IniFile.value(BaseConstants.MONITORING));
			if (monitor)
				new NotificationMonitor(System.out);
		} else {
			IniFile.store(BaseConstants.MONITORING, "true");
			new NotificationMonitor(System.out);
		}
		NotificationCentre.addListener(listener);
		setLookAndFeel();
		if (ApplicationConfiguration.applicationDefinition().hasModelFile()) {
			loadModelAndWait(storeDetails);
		}
		NotificationCentre.removeListener(listener);
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

	public void setLookAndFeel() {
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

	private boolean invalidParameters(Parameters parameters) {
		return (tooMany(parameters) || mixedTypeOf(parameters) || wrongNameIn(parameters));
	}

	private boolean tooMany(Parameters parameters) {
		return parameters.getRaw().size() > 2;
	}

	private boolean mixedTypeOf(Parameters parameters) {
		return parameters.getRaw().size() != parameters.getNamed().size();
	}

	private boolean wrongNameIn(Parameters parameters) {
		boolean wrongName = false;
		Iterator<String> it = parameters.getNamed().keySet().iterator();
		while (it.hasNext()) {
			String name = it.next();
			if (!name.equals(NAME) && !name.equals(DIR)) {
				wrongName = true;
			}
		}
		return wrongName;
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
	 * This call is made during the ApplicationBaseForGUI.init() process to allow
	 * the application to establish its unique values. The ApplicationDefinition
	 * will be dependent on each application's requirements.
	 * 
	 * @param parameters
	 * @return an ApplicationDefinition
	 */
	public abstract ApplicationDefinition createApplicationDefinition(Parameters parameters);

	/**
	 * This call is made during the ApplicationBaseForGUI.init() process to allow
	 * the application to define the requirements to be able to load the model into
	 * storage.
	 */
	public abstract void configureStoreDetails();

	/**
	 * This call is made during the ApplicationBaseForGUI.createEnvironment()
	 * process to allow the application to define and create the initial screen to
	 * display in the subsequent setVisible() call.
	 * 
	 * @param frame
	 */
	public abstract void start(JFrame frame);

	/**
	 * This call is made during the windowClosing process to allow the application
	 * to tidy up before the framework itself is shut down.
	 */
	public abstract void terminate();

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

	public void shutdown() {
		LOGGER.entering(CLASS_NAME, "shutdown");
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		LOGGER.exiting(CLASS_NAME, "shutdown");
	}

	public void stop() throws Exception {
		LOGGER.entering(CLASS_NAME, "stop");
		application.terminate();
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

}
