package application.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import application.base.app.IApplication;

/**
 * Action to exit the application.
 */
public class HelpAboutAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	/**
	 * The application.
	 */
	private IApplication application;

	/**
	 * Create the exit application action for this application.
	 * 
	 * @param application - the application.
	 */
	public HelpAboutAction(IApplication application) {
		super("About");
		this.application = application;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		application.helpAboutAction();
	}

}
