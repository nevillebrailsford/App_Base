/**
 * 
 */
package application.menu;

import java.util.logging.Logger;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import application.action.BaseActionFactory;
import application.definition.ApplicationConfiguration;

/**
 * MenuBar that applications can extend. It provides default File, Edit and Help
 * menus.
 */
public abstract class AbstractMenuBar extends JMenuBar {
	private static final long serialVersionUID = 1L;
	private static final String CLASS_NAME = AbstractMenuBar.class.getName();
	private static final Logger LOGGER = ApplicationConfiguration.logger();

	private JMenu fileMenu;
	private JMenu editMenu;
	private JMenu helpMenu;

	private BaseActionFactory factory;

	/**
	 * Create the menu bar
	 */
	public AbstractMenuBar(BaseActionFactory factory) {
		super();
		LOGGER.entering(CLASS_NAME, "cinit");
		this.factory = factory;
		createFileMenu();
		add(fileMenu);
		createEditMenu();
		add(editMenu);
		addAdditionalMenus(this);
		createHelpMenu();
		add(helpMenu);
		LOGGER.exiting(CLASS_NAME, "cinit");
	}

	private void createFileMenu() {
		LOGGER.entering(CLASS_NAME, "createFileMenu");
		fileMenu = new JMenu("File");
		addBeforePreferences(fileMenu);
		fileMenu.add(preferencesItem());
		addBeforeExit(fileMenu);
		fileMenu.addSeparator();
		fileMenu.add(exitItem());
		LOGGER.exiting(CLASS_NAME, "createFileMenu");
	}

	private void createEditMenu() {
		LOGGER.entering(CLASS_NAME, "createEditMenu");
		editMenu = new JMenu("Edit");
		editMenu.add(undoItem());
		editMenu.add(redoItem());
		addToEditMenu(editMenu);
		LOGGER.exiting(CLASS_NAME, "createEditMenu");
	}

	private void createHelpMenu() {
		LOGGER.entering(CLASS_NAME, "createHelpMenu");
		helpMenu = new JMenu("Help");
		helpMenu.add(helpAboutItem());
		addToHelpMenu(helpMenu);
		LOGGER.exiting(CLASS_NAME, "createHelpMenu");
	}

	/**
	 * Callback to allow an application to add additional menus to the menu bar.
	 * These will be placed after the Edit menu.
	 */
	public void addAdditionalMenus(JMenuBar menuBar) {
	}

	/**
	 * Callback to allow an application to add items to the File menu. These items
	 * will be placed before the Preferences menu item.
	 */
	public void addBeforePreferences(JMenu fileMenu) {
	}

	/**
	 * Callback to allow an application to add items to the File menu. These items
	 * will be placed before the Exit menu item.
	 */
	public void addBeforeExit(JMenu fileMenu) {
	}

	/**
	 * Callback to allow an application to add items to the Edit menu. These items
	 * will be placed after the redo item.
	 */
	public void addToEditMenu(JMenu editMenu) {
	}

	/**
	 * Callback to allow an application to add items to the Help menu. These items
	 * will be placed after the about item.
	 */
	public void addToHelpMenu(JMenu helpMenu) {
	}

	/**
	 * Provide an alternative to the preferences item in the file menu.
	 */
	public JMenuItem preferencesItem() {
		return new JMenuItem(factory.preferencesAction());
	}

	/**
	 * Provide an alternative to the exit item in the file menu.
	 */
	public JMenuItem exitItem() {
		return new JMenuItem(factory.exitApplicationAction());
	}

	/**
	 * Provide an alternative to the undo item in the edit menu.
	 */
	public JMenuItem undoItem() {
		return new JMenuItem(factory.undoAction());
	}

	/**
	 * Provide an alternative to the redo item in the edit menu.
	 */
	public JMenuItem redoItem() {
		return new JMenuItem(factory.redoAction());
	}

	/**
	 * Provide an alternative to the about item in the help menu.
	 */
	public JMenuItem helpAboutItem() {
		return new JMenuItem(factory.helpAboutAction());
	}
}
