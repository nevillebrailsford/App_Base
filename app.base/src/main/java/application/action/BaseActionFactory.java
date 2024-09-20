package application.action;

import java.util.logging.Logger;

import javax.swing.JOptionPane;

import application.base.app.IApplication;
import application.change.ChangeManager;
import application.change.ChangeStateListener;
import application.definition.ApplicationConfiguration;

/**
 * Provide action commands
 * 
 * @author neville
 * @version 3.0.0
 */
public class BaseActionFactory implements ChangeStateListener {
	private static final String CLASS_NAME = BaseActionFactory.class.getName();
	private static Logger LOGGER = ApplicationConfiguration.logger();

	private static BaseActionFactory instance = null;
	public IApplication application;

	private UndoAction undoAction = null;
	private RedoAction redoAction = null;
	private ExitApplicationAction exitApplicationAction = null;
	private PreferencesAction preferencesAction = null;
	private HelpAboutAction helpAboutAction = null;

	/**
	 * Create a new instance of the action factory if not already created.
	 * 
	 * @param application - the application for this factory. This parameter is
	 *                    optional, but must be specified on the first call to
	 *                    instance.
	 * @return - the action factory.
	 */
	public synchronized static BaseActionFactory instance(IApplication... application) {
		LOGGER.entering(CLASS_NAME, "instance", application);
		if (instance == null) {
			if (application.length == 0) {
				JOptionPane.showMessageDialog(null, "Application was not specified on first call to instance.",
						"ActionFactory error.", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
			instance = new BaseActionFactory();
			instance.application = application[0];
		}
		LOGGER.exiting(CLASS_NAME, "instance");
		return instance;
	}

	public BaseActionFactory() {
		ChangeManager.instance().addListener(this);
	}

	/**
	 * Obtain the redo action.
	 * <p>
	 * The action is created in the disabled state
	 * </p>
	 * 
	 * @return - a redo action.
	 */
	public RedoAction redoAction() {
		if (redoAction == null) {
			redoAction = new RedoAction(application);
			redoAction.setEnabled(false);
		}
		return redoAction;
	}

	/**
	 * Obtain the undo action.
	 * <p>
	 * The action is created in the disabled state
	 * </p>
	 * 
	 * @return - a undo action.
	 */
	public UndoAction undoAction() {
		if (undoAction == null) {
			undoAction = new UndoAction(application);
			undoAction.setEnabled(false);
		}
		return undoAction;
	}

	/**
	 * Obtain the exit action.
	 * 
	 * @return - an exit action.
	 */
	public ExitApplicationAction exitApplicationAction() {
		LOGGER.entering(CLASS_NAME, "exitApplicationAction");
		if (exitApplicationAction == null) {
			exitApplicationAction = new ExitApplicationAction(application);
		}
		LOGGER.exiting(CLASS_NAME, "exitApplicationAction");
		return exitApplicationAction;
	}

	/**
	 * Obtain the preferences action.
	 * 
	 * @return - a preferences action.
	 */
	public PreferencesAction preferencesAction() {
		if (preferencesAction == null) {
			preferencesAction = new PreferencesAction(application);
		}
		return preferencesAction;
	}

	/**
	 * Obtain the help about action.
	 * 
	 * @return - a helpAboutAction.
	 */
	public HelpAboutAction helpAboutAction() {
		if (helpAboutAction == null) {
			helpAboutAction = new HelpAboutAction(application);
		}
		return helpAboutAction;
	}

	@Override
	public void stateChanged() {
		LOGGER.entering(CLASS_NAME, "stateChanged");
		undoAction().setEnabled(ChangeManager.instance().undoable());
		redoAction().setEnabled(ChangeManager.instance().redoable());
		LOGGER.exiting(CLASS_NAME, "stateChanged");
	}
}
