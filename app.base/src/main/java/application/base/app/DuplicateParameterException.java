package application.base.app;

/**
 * The exception thrown if a parameter is specified twice.
 * 
 * @author neville
 * @version 3.0.0
 */
public class DuplicateParameterException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * Create the exception.
	 */
	public DuplicateParameterException() {
	}

	/**
	 * Create the exception with an accompanying message.
	 * 
	 * @param message an explanation for the exception being thrown.
	 */
	public DuplicateParameterException(String message) {
		super(message);
	}

}
