package application.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import application.base.app.IApplication;

/**
 * Action to undo the last action..
 * 
 * @author neville
 * @version 3.0.0
 */
public class UndoAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	/**
	 * The application.
	 */
	private IApplication application;

	/**
	 * Create the undo action for this application.
	 * 
	 * @param application - the application.
	 */
	public UndoAction(IApplication application) {
		super("Undo");
		this.application = application;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		application.undoAction();
	}

}
