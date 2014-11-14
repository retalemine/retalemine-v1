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

	public static TaxType getTaxType(String value) {
		if (null != value && !value.trim().isEmpty()) {
			value = value.trim();
			for (TaxType mode : TaxType.values()) {
				if (mode.value.equalsIgnoreCase(value)) {
					return mode;
				}
			}
		}
		return null;
	}

}
