package application.change;

public interface Change {
	enum State {
		READY, DONE, UNDONE, STUCK
	};

	State state();

	void execute();

	void undo();

	void redo();

}
