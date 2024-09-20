package application.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import application.base.app.IApplication;

/**
 * Action to redo the last undo action.
 * 
 * @author neville
 * @version 3.0.0
 */
public class RedoAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	/**
	 * The application.
	 */
	private IApplication application;

	/**
	 * Create the redo action for this application.
	 * 
	 * @param application - the application.
	 */
	public RedoAction(IApplication application) {
		super("redo");
		this.application = application;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		application.redoAction();
	}

}
