package in.retalemine.entity;

public enum TaxType {
	VAT("VAT"), SALESTAX("Sales Tax"), SERVICETAX("Service Tax");

	private String value;

	private TaxType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
