package application.change;

import java.util.Stack;
import java.util.Vector;
import java.util.logging.Logger;

import application.definition.ApplicationConfiguration;

public class ChangeManager {
	private static final String CLASS_NAME = ChangeManager.class.getName();
	private static final Logger LOGGER = ApplicationConfiguration.logger();

	private static ChangeManager instance = null;

	private Stack<Change> undoStack = new Stack<>();
	private Stack<Change> redoStack = new Stack<>();

	private boolean undoable = false;
	private boolean redoable = false;

	private Vector<ChangeStateListener> listeners = new Vector<>();

	public static synchronized ChangeManager instance() {
		if (instance == null) {
			instance = new ChangeManager();
			instance.setChangable();
		}
		return instance;
	}

	private ChangeManager() {
	}

	public boolean undoable() {
		return undoable;
	}

	public boolean redoable() {
		return redoable;
	}

	public void reset() {
		undoStack.clear();
		redoStack.clear();
		setChangable();
	}

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
		fireStateChanged();
		LOGGER.exiting(CLASS_NAME, "redo");
	}

	private void setChangable() {
		undoable = !undoStack.isEmpty();
		redoable = !redoStack.isEmpty();
	}

	public void addListener(ChangeStateListener l) {
		listeners.addElement(l);
	}

	public void removeListener(ChangeStateListener l) {
		listeners.removeElement(l);
	}

	private void fireStateChanged() {
		LOGGER.entering(CLASS_NAME, "fireStateChanged");
		for (ChangeStateListener csl : listeners) {
			csl.stateChanged();
		}
		LOGGER.exiting(CLASS_NAME, "fireStateChanged");
	}

}
