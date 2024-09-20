package application.change;

/**
 * Listener that will be told when <code>ChangeManager</code> carries out some
 * work.
 * 
 * @author neville
 * @version 3.0.0
 */
public interface ChangeStateListener {
	/**
	 * The state of the change manager has changed. Either a <code>Change</code>
	 * object has been told to execute, undo or redo some work.
	 */
	public void stateChanged();
}
