package application.audit;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import application.definition.BaseConstants;

/**
 * A record that is passed to the audit function to record an activity of some
 * kind.
 * 
 * @author neville
 * @version 3.0.0
 *
 */
public class AuditRecord<T extends AuditType, O extends AuditObject> implements Comparable<AuditRecord<T, O>> {
	private T auditType;
	private O auditObject;
	private String auditInformation;

	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(BaseConstants.dateFormatForAuditRecord);
	private String user;
	private ZonedDateTime timeStamp;

	/**
	 * Create an audit record
	 * 
	 * @param type
	 * @param object
	 * @param information
	 * @throws IllegalArgumentException if type is null, object is null, or
	 *                                  information is null or empty.
	 */
	AuditRecord(T type, O object, String information) {
		if (type == null) {
			throw new IllegalArgumentException("AuditRecord - type is null");
		}
		if (object == null) {
			throw new IllegalArgumentException("AuditRecord - object is null");
		}
		if (information == null) {
			throw new IllegalArgumentException("AuditRecord - information is null");
		}
		if (information.trim().isEmpty()) {
			throw new IllegalArgumentException("AuditRecord - information is empty");
		}
		auditType = type;
		auditObject = object;
		auditInformation = information;
		user = System.getProperty("user.name");
		timeStamp = ZonedDateTime.now();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(timeStamp.format(formatter)).append(" ");
		builder.append(user).append(" ");
		builder.append(auditType.type()).append(" ");
		builder.append(auditObject.object()).append(" ");
		builder.append(auditInformation);
		return builder.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(timeStamp);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AuditRecord<?, ?> that = (AuditRecord<?, ?>) obj;
		return Objects.equals(this.timeStamp, that.timeStamp);
	}

	@Override
	public int compareTo(AuditRecord<T, O> that) {
		return this.timeStamp.compareTo(that.timeStamp);
	}
}
