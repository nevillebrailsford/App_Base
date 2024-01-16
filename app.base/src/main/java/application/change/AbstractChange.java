package application.change;

import java.util.logging.Logger;

import application.definition.ApplicationConfiguration;

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

	protected abstract void doHook() throws Failure;

	protected abstract void undoHook() throws Failure;

	protected abstract void redoHook() throws Failure;

}
