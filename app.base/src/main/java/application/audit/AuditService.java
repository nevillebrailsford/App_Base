package application.audit;

public class AuditService {
	private static AuditWriter writer = new AuditWriter();

	/**
	 * Service to permit classes from other packages to write audit records.
	 * 
	 * @param type
	 * @param object
	 * @param message
	 */
	public synchronized static void writeAuditInformation(AuditType type, AuditObject object, String message) {
		AuditRecord<?, ?> record = new AuditRecord<>(type, object, message);
		writer.write(record);
	}

}
