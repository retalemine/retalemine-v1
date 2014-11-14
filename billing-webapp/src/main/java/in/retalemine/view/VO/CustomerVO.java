package in.retalemine.view.VO;

public class CustomerVO {

	private String customerName;
	private String contactNo;
	private String address;

	public CustomerVO(String customerName, String customerNumber,
			String customerAddress) {
		this.customerName = customerName;
		contactNo = customerNumber;
		address = customerAddress;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("<CustomerVO><customerName>");
		builder.append(customerName);
		builder.append("</customerName><contactNo>");
		builder.append(contactNo);
		builder.append("</contactNo><address>");
		builder.append(address);
		builder.append("</address></CustomerVO>");
		return builder.toString();
	}

}
