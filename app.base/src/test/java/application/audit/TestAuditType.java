package application.audit;

public enum TestAuditType implements AuditType {
	Opened("Opened");

	private String type;

	TestAuditType(String type) {
		this.type = type;
	}

	@Override
	public String type() {
		return type;
	}

}
