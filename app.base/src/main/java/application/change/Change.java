package application.change;

public interface Change {
	enum State {
		READY, DONE, UNDONE, STUCK
	};

	/**
	 * Determine the state of the change.
	 * 
	 * @return the state of the change.
	 */
	State state();

	/**
	 * The method that gets called to carry out the task.
	 */
	void execute();

	/**
	 * The method that gets called to undo a change.
	 */
	void undo();

	/**
	 * The method that gets called to redo a change.
	 */
	void redo();

}
