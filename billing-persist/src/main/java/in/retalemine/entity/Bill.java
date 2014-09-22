package in.retalemine.entity;

import in.retalemine.constants.MongoDBKeys;

import java.util.Date;
import java.util.List;

import javax.measure.quantity.Quantity;

import org.jscience.economics.money.Money;
import org.jscience.physics.amount.Amount;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "sales")
public class Bill {

	@Id
	@Field(MongoDBKeys.BILL_NO)
	private Integer billNo;
	@Field(MongoDBKeys.BILL_DATE)
	private Date billDate;
	@Field(MongoDBKeys.BILL_ITEMS)
	private List<BillItem<? extends Quantity, ? extends Quantity>> billItems;
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

	public Bill(Integer billNo, Date billDate,
			List<BillItem<? extends Quantity, ? extends Quantity>> billItems,
			Amount<Money> subTotal, List<Tax> taxes, Amount<Money> total,
			Payment paymentDetails, Customer customerDetails,
			Boolean isDoorDelivery) {
		this.billNo = billNo;
		this.billDate = billDate;
		this.billItems = billItems;
		this.subTotal = subTotal;
		this.taxes = taxes;
		this.total = total;
		this.paymentDetails = paymentDetails;
		this.customerDetails = customerDetails;
		this.isDoorDelivery = isDoorDelivery;
	}
}
