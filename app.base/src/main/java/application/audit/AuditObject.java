package application.audit;

/**
 * Anything that needs to be audited.
 */
public interface AuditObject {

	/**
	 * Return a readable description of the object.
	 * 
	 * @return - a String
	 */
	public abstract String object();
}
