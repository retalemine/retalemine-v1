package in.retalemine.entity;

public enum PaymentMode {
	CASH("Cash"), CHEQUE("Cheque");

	private String value;

	private PaymentMode(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static PaymentMode getPaymentMode(String value) {
		if (null != value && !value.trim().isEmpty()) {
			value = value.trim();
			for (PaymentMode mode : PaymentMode.values()) {
				if (mode.value.equalsIgnoreCase(value)) {
					return mode;
				}
			}
		}
		return null;
	}
}
