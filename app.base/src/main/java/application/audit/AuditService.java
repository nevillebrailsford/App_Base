package application.audit;

/**
 * AuditService supports the writing of audit information to an audit file.
 */
public class AuditService {
	private static AuditWriter writer = new AuditWriter();
	private static boolean suspended = false;

	/**
	 * Service to permit classes from other packages to write audit records.
	 * 
	 * @param type    - type of audit record
	 * @param object  - an instance of any object that needs to be audited.
	 * @param message - associated message
	 */
	public synchronized static void writeAuditInformation(AuditType type, AuditObject object, String message) {
		if (!suspended) {
			AuditRecord<?, ?> record = new AuditRecord<>(type, object, message);
			writer.write(record);
		}
	}

	/**
	 * Stop the auditing service.
	 */
	public static void suspend() {
		suspended = true;
	}

	/**
	 * Resume the auditing service.
	 */
	public static void resume() {
		suspended = false;
	}

	/**
	 * Reset the auditing service to its default behaviour which is to record
	 * auditing information.
	 */
	public static void reset() {
		suspended = false;
	}
}
