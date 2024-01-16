package application.base.app;

public class DuplicateParameterException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public DuplicateParameterException() {
	}

	public DuplicateParameterException(String message) {
		super(message);
	}

}
