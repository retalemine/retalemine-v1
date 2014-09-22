package in.retalemine.entity;

import in.retalemine.constants.MongoDBKeys;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Field;

public class Payment {

	@Field(MongoDBKeys.BILL_PAYMENT_MODE)
	private PaymentMode payMode;
	@Field(MongoDBKeys.BILL_PAYMENT_DELAYED)
	private Boolean isDelayedPay;
	@Field(MongoDBKeys.BILL_PAYMENT_PAID)
	private Boolean isPaid;
	@Field(MongoDBKeys.BILL_PAYMENT_DATE)
	private Date paidDate;

	public Payment(PaymentMode payMode, Boolean isDelayedPay, Boolean isPaid,
			Date paidDate) {
		this.payMode = payMode;
		this.isDelayedPay = isDelayedPay;
		this.isPaid = isPaid;
		this.paidDate = paidDate;
	}

	public PaymentMode getPayMode() {
		return payMode;
	}

	public Boolean getIsDelayedPay() {
		return isDelayedPay;
	}

	public Boolean getIsPaid() {
		return isPaid;
	}

	public Date getPaidDate() {
		return paidDate;
	}

}
