package in.retalemine.entity;

import in.retalemine.constants.MongoDBKeys;

import java.util.Date;
import java.util.List;

import org.jscience.economics.money.Money;
import org.jscience.physics.amount.Amount;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "sales")
public class Bill {

	@Id
	private String id;
	@Indexed(unique = true)
	@Field(MongoDBKeys.BILL_NO)
	private String billNo;
	@Field(MongoDBKeys.BILL_DATE)
	private Date billDate;
	@Field(MongoDBKeys.BILL_ITEMS)
	private List<BillItem> billItems;
	@Transient
	private Amount<Money> subTotal;
	@Field(MongoDBKeys.BILL_TAX)
	private List<Tax> taxes;
	@Field(MongoDBKeys.BILL_TOTAL)
	private Amount<Money> total;
	@Field(MongoDBKeys.BILL_PAYMENT)
	private Payment paymentDetails;
	@Field(MongoDBKeys.BILL_CUS_DETAIL)
	private Customer customerDetails;
	@Field(MongoDBKeys.BILL_DELIVERED)
	private Boolean isDoorDelivery;

	public Bill() {
	}

	public Bill(Date billDate, List<BillItem> billItems,
			Amount<Money> subTotal, List<Tax> taxes, Amount<Money> total,
			Payment paymentDetails, Customer customerDetails,
			Boolean isDoorDelivery) {
		this.billDate = billDate;
		this.billItems = billItems;
		this.subTotal = subTotal;
		this.taxes = taxes;
		this.total = total;
		this.paymentDetails = paymentDetails;
		this.customerDetails = customerDetails;
		this.isDoorDelivery = isDoorDelivery;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getId() {
		return id;
	}

	public Date getBillDate() {
		return billDate;
	}

	public List<BillItem> getBillItems() {
		return billItems;
	}

	public Amount<Money> getSubTotal() {
		return subTotal;
	}

	public List<Tax> getTaxes() {
		return taxes;
	}

	public Amount<Money> getTotal() {
		return total;
	}

	public Payment getPaymentDetails() {
		return paymentDetails;
	}

	public Customer getCustomerDetails() {
		return customerDetails;
	}

	public Boolean getIsDoorDelivery() {
		return isDoorDelivery;
	}

}
