package application.change;

import java.util.logging.Logger;

import application.definition.ApplicationConfiguration;

/**
 * Abstract class to use when building a change element for an application.
 * 
 * @author neville
 * @version 3.0.0
 */
public abstract class AbstractChange implements Change {
	private static final String CLASS_NAME = AbstractChange.class.getName();
	private static final Logger LOGGER = ApplicationConfiguration.logger();

	State state = State.READY;

	@Override
	public State state() {
		LOGGER.entering(CLASS_NAME, "getState");
		LOGGER.exiting(CLASS_NAME, "getState", state);
		return state;
	}

	@Override
	public void execute() {
		LOGGER.entering(CLASS_NAME, "execute");
		assert state == State.READY;
		try {
			doHook();
			state = State.DONE;
		} catch (Failure e) {
			state = State.STUCK;
		} catch (Throwable e) {
			assert false;
		} finally {
			LOGGER.exiting(CLASS_NAME, "execute");
		}

	}

	@Override
	public void undo() {
		LOGGER.exiting(CLASS_NAME, "undo");
		assert state == State.DONE;
		try {
			undoHook();
			state = State.UNDONE;
		} catch (Failure e) {
			state = State.STUCK;
		} catch (Throwable e) {
			assert false;
		} finally {
			LOGGER.exiting(CLASS_NAME, "undo");
		}
	}

	@Override
	public void redo() {
		LOGGER.entering(CLASS_NAME, "redo");
		assert state == State.UNDONE;
		try {
			redoHook();
			state = State.DONE;
		} catch (Failure e) {
			state = State.STUCK;
		} catch (Throwable e) {
			assert false;
		} finally {
			LOGGER.exiting(CLASS_NAME, "redo");
		}
	}

	/**
	 * The action to be taken to implement the change.
	 * 
	 * @throws Failure occurs when the action failed.
	 */
	protected abstract void doHook() throws Failure;

	/**
	 * The action to be taken when an undo request is made.
	 * 
	 * @throws Failure occurs when the action failed.
	 */
	protected abstract void undoHook() throws Failure;

	/**
	 * The action to be taken whan a redo request is made.
	 * 
	 * @throws Failure occurs when the action failed.
	 */
	protected abstract void redoHook() throws Failure;

}
