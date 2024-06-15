package application.audit;

/**
 * The type of audit record.
 */
public interface AuditType {
	/**
	 * Return a readable version of the type.
	 * 
	 * @return - a String
	 */
	public abstract String type();

}
