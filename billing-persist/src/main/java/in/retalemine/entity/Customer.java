package in.retalemine.entity;

import in.retalemine.constants.MongoDBKeys;

import org.springframework.data.mongodb.core.mapping.Field;

public class Customer {

	@Field(MongoDBKeys.CUSTOMER_NAME)
	private String customerName;
	@Field(MongoDBKeys.CUSTOMER_CONTACT_NO)
	private Integer contactNo;
	@Field(MongoDBKeys.CUSTOMER_ADDRESS)
	private String address;

	public Customer(String customerName, Integer contactNo, String address) {
		this.customerName = customerName;
		this.contactNo = contactNo;
		this.address = address;
	}

}
