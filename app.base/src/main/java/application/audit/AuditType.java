package application.audit;

/**
 * The type of audit record.
 * 
 * @author neville
 * @version 3.0.0
 */
public interface AuditType {
	/**
	 * Return a readable version of the type.
	 * 
	 * @return - a String
	 */
	public abstract String type();

}
