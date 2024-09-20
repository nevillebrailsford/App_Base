package application.base.app;

import javax.swing.JFrame;

import application.definition.ApplicationDefinition;
import application.storage.StoreDetails;

/**
 * The interface that all applications have to implement.
 * 
 * @author neville
 * @version 3.0.0
 * @since 3.0.0
 */
public interface IApplication {
	/**
	 * This call is made during the ApplicationBaseForGUI.init() process to allow
	 * the application to establish its unique values. The ApplicationDefinition
	 * will be dependent on each application's requirements.
	 * 
	 * @param parameters
	 * @return an ApplicationDefinition
	 */
	public ApplicationDefinition createApplicationDefinition(Parameters parameters);

	/**
	 * This call is made during the ApplicationBaseForGUI.init() process to allow
	 * the application to define the requirements to be able to load the model into
	 * storage.
	 */
	public StoreDetails configureStoreDetails();

	/**
	 * This call is made during the ApplicationBaseForGUI.createEnvironment()
	 * process to allow the application to define and create the initial screen to
	 * display in the subsequent setVisible() call.
	 * 
	 * @param frame
	 */
	public void start(JFrame frame);

	/**
	 * This call is made during the windowClosing process to allow the application
	 * to tidy up before the framework itself is shut down.
	 */
	public void terminate();

	/**
	 * This call is to handle a request for the preferences dialog to be displayed.
	 * The default method displays the default <code>PreferencesDialog</code>.
	 */
	public void preferencesAction();

	/**
	 * This call is to handle a request for undo to be performed. The default action
	 * is to call the <code>ChangeManager</code> to undo the last change action.
	 */
	public void undoAction();

	/**
	 * This call is to handle a request for redo to be performed. The default action
	 * is to call the <code>ChangeManager</code> to redo the last change action
	 */
	public void redoAction();

	/**
	 * This call is to handle a request for the application to close. The default
	 * method calls <code>shutdown</code> to shutdown the application.
	 */
	public void exitApplicationAction();

	/**
	 * This call is to handle a request for help information about this application.
	 * The default method displays help about this application in a standard dialog.
	 */
	public void helpAboutAction();
}
