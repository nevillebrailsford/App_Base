package application.audit;

/**
 * Anything that needs to be audited.
 * 
 * @author neville
 * @version 3.0.0
 */
public interface AuditObject {

	/**
	 * Return a readable description of the object.
	 * 
	 * @return - a String
	 */
	public abstract String object();
}
