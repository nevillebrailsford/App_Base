package application.audit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

import application.definition.ApplicationConfiguration;
import application.definition.ApplicationDefinition;

/**
 * The class that writes audit information to the audit file.
 * 
 * @author neville
 * @version 3.0.0
 */
public class AuditWriter {
	/**
	 * The application definition for this application. *
	 */
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
	 * @param record - the record to write to the audit file.
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
