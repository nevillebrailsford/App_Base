package application.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import application.base.app.IApplication;

/**
 * Action to show a preferences dialog.
 */
public class PreferencesAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	/**
	 * The application.
	 */
	private IApplication application;

	/**
	 * Create the preferences action for this application.
	 * 
	 * @param application - the application.
	 */
	public PreferencesAction(IApplication application) {
		super("Preferences");
		this.application = application;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		application.preferencesAction();
	}

}
