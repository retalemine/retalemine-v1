package in.retalemine.entity;

import in.retalemine.constants.MongoDBKeys;

import javax.measure.Measure;
import javax.measure.quantity.Quantity;

import org.jscience.economics.money.Money;
import org.jscience.physics.amount.Amount;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Field;

public class BillItem<U extends Quantity, V extends Quantity> {

	@Transient
	private String productName;
	@Transient
	private Measure<Double, U> productUnit;
	@Field(MongoDBKeys.PRODUCT_DESCRIPTION)
	private String productDescription;
	@Field(MongoDBKeys.PRODUCT_PRICE)
	protected Amount<Money> unitPrice;
	@Field(MongoDBKeys.BILL_ITEM_QTY)
	private Measure<Double, V> quantity;
	@Transient
	private Amount<Money> amount;

	public BillItem() {

	}

	public BillItem(String productName, Measure<Double, U> productUnit,
			String productDescription, Amount<Money> unitPrice,
			Measure<Double, V> quantity, Amount<Money> amount) {
		this.productName = productName;
		this.productUnit = productUnit;
		this.productDescription = productDescription;
		this.unitPrice = unitPrice;
		this.quantity = quantity;
		this.amount = amount;
	}

	public static <U extends Quantity, V extends Quantity> BillItem<U, V> valueOf(
			String productName, Measure<Double, U> productUnit,
			String productDescription, Amount<Money> unitPrice,
			Measure<Double, V> quantity, Amount<Money> amount) {
		return new BillItem<U, V>(productName, productUnit, productDescription,
				unitPrice, quantity, amount);
	}

}
