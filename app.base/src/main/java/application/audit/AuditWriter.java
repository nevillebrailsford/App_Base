package application.audit;

import java.io.*;

import application.definition.*;

public class AuditWriter {
	private ApplicationDefinition applicationDefinition;

	/**
	 * Create an audit writer
	 * 
	 * @throws IllegalStateException if applicationDescriptor has not been
	 *                               registered.
	 */
	public AuditWriter() {
		if (ApplicationConfiguration.applicationDefinition() == null) {
			throw new IllegalStateException("AuditWriter - applicationDefinition is null");
		}
		applicationDefinition = ApplicationConfiguration.applicationDefinition();
	}

	/**
	 * write a record to the audit file
	 * 
	 * @param record
	 * @throws IllegalArgumentException if record is null
	 * @throws IllegalStateException    if cannot create audit directory
	 */
	public void write(AuditRecord<? extends AuditType, ? extends AuditObject> record) {
		if (record == null) {
			throw new IllegalArgumentException("AuditWriter - record is null");
		}
		File auditDirectory = new File(applicationDefinition.auditDirectory());
		if (!auditDirectory.exists()) {
			if (!auditDirectory.mkdirs()) {
				throw new IllegalStateException("AuditWriter - unable to create directory");
			}
		}
		File auditFile = new File(applicationDefinition.auditFile());
		try (PrintStream writer = new PrintStream(new FileOutputStream(auditFile, true))) {
			writer.println(record);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
