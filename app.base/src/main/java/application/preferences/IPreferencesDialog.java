package application.preferences;

import javax.swing.JPanel;

/**
 * All dialogs implementing preferences dialog must implement this interface.
 * 
 * @author neville
 * @version 3.0.0
 * @since 3.0.0
 */
public interface IPreferencesDialog {
	/**
	 * Add items to the GUI. This method is called during initialisation of the GUI,
	 * and is used to add any additional GUI items that the application may require,
	 * over and above those already saved.
	 * 
	 * @param contentPanel that already contains default preferences. contentPanel
	 *                     will have the a <code>GridLayout</code> of three columns
	 *                     set.
	 */
	public void additionalGUIItems(JPanel contentPanel);

	/**
	 * Add action listeners to additional items. This method is called during
	 * initialisation of the GUI, and is used to add action listeners to any items
	 * added by additionalGUIItems.
	 */
	public void additionalActionListeners();

	/**
	 * Save additional preferences. This method is called when the user requests
	 * that the preferences chosen be saved.
	 */
	public void saveAdditionalPreferences();

}
