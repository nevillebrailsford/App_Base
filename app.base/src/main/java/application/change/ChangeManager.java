package application.change;

import java.util.Stack;
import java.util.Vector;
import java.util.logging.Logger;

import application.definition.ApplicationConfiguration;

/**
 * ChangeManager is responsible for handling change requests. It keeps track of
 * <code>Change</code> objects in two states - a change is either undoable or
 * redoable. Changes that are executed are added to the undo stack. A request to
 * undo causes the object to be moved from the undo to the redo stack. Listeners
 * can be registered to be told when a change object is executed, or undone or
 * redone.
 * 
 * @author neville
 * @version 3.0.0
 */
public class ChangeManager {
	private static final String CLASS_NAME = ChangeManager.class.getName();
	private static final Logger LOGGER = ApplicationConfiguration.logger();

	private static ChangeManager instance = null;

	private Stack<Change> undoStack = new Stack<>();
	private Stack<Change> redoStack = new Stack<>();

	private boolean undoable = false;
	private boolean redoable = false;

	private Vector<ChangeStateListener> listeners = new Vector<>();

	/**
	 * Obtain the <code>ChangeManager</code>
	 * 
	 * @return the change manager.
	 */
	public static synchronized ChangeManager instance() {
		if (instance == null) {
			instance = new ChangeManager();
			instance.setChangable();
		}
		return instance;
	}

	private ChangeManager() {
	}

	/**
	 * Determine if there are undoable changes available.
	 * 
	 * @return true if there are undoable changes available.
	 */
	public boolean undoable() {
		return undoable;
	}

	/**
	 * Determine if there are redoable changes available.
	 * 
	 * @return true if there are redoable changes available.
	 */
	public boolean redoable() {
		return redoable;
	}

	/**
	 * Reset to empty stacks. Really a method for testing purposes.
	 */
	public void reset() {
		undoStack.clear();
		redoStack.clear();
		setChangable();
	}

	/**
	 * Call the <code>Change</code> object to carry out its work, and once complete,
	 * move it to the undoable collection.
	 * 
	 * @param change - the object that has work to do.
	 */
	public void execute(Change change) {
		LOGGER.entering(CLASS_NAME, "execute", change);
		change.execute();
		if (change.state() == Change.State.DONE) {
			undoStack.push(change);
			redoStack.clear();
		} else {
			undoStack.clear();
			redoStack.clear();
		}
		setChangable();
		LOGGER.exiting(CLASS_NAME, "execute");
	}

	/**
	 * Call the <code>Change</code> object on top of the stack to undo its work, and
	 * once complete, move it to the redoable collection.
	 */
	public void undo() {
		LOGGER.entering(CLASS_NAME, "undo");
		if (undoStack.size() > 0) {
			Change change = undoStack.pop();
			change.undo();
			if (change.state() == Change.State.UNDONE) {
				redoStack.push(change);
			} else {
				undoStack.clear();
				redoStack.clear();
			}
		}
		setChangable();
		LOGGER.exiting(CLASS_NAME, "undo");
	}

	/**
	 * Call the <code>Change</code> object on top of the stack to redo its work, and
	 * once complete, move it to the undoable collection.
	 */
	public void redo() {
		LOGGER.entering(CLASS_NAME, "redo");
		if (redoStack.size() > 0) {
			Change change = redoStack.pop();
			change.redo();
			if (change.state() == Change.State.DONE) {
				undoStack.push(change);
			} else {
				undoStack.clear();
				redoStack.clear();
			}
		}
		setChangable();
		LOGGER.exiting(CLASS_NAME, "redo");
	}

	private void setChangable() {
		LOGGER.entering(CLASS_NAME, "setChangable");
		undoable = !undoStack.isEmpty();
		redoable = !redoStack.isEmpty();
		fireStateChanged();
		LOGGER.exiting(CLASS_NAME, "setChangable");
	}

	/**
	 * Add a listener to the change manager.
	 * 
	 * @param listener - the listener
	 */
	public void addListener(ChangeStateListener listener) {
		listeners.addElement(listener);
	}

	/**
	 * remove a listener to the change manager.
	 * 
	 * @param listener - the listener
	 */
	public void removeListener(ChangeStateListener listener) {
		listeners.removeElement(listener);
	}

	private void fireStateChanged() {
		LOGGER.entering(CLASS_NAME, "fireStateChanged");
		for (ChangeStateListener csl : listeners) {
			LOGGER.fine("Calling stateChanged on " + csl);
			csl.stateChanged();
		}
		LOGGER.exiting(CLASS_NAME, "fireStateChanged");
	}

}
