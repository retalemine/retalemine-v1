package in.retalemine.entity;

import in.retalemine.constants.MongoDBKeys;

import javax.measure.Measure;
import javax.measure.quantity.Quantity;

import org.jscience.economics.money.Money;
import org.jscience.physics.amount.Amount;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Field;

public class BillItem {

	@Transient
	private String productName;
	@Transient
	private Measure<Double, ? extends Quantity> productUnit;
	@Field(MongoDBKeys.PRODUCT_DESCRIPTION)
	private String productDescription;
	@Field(MongoDBKeys.PRODUCT_PRICE)
	protected Amount<Money> unitPrice;
	@Field(MongoDBKeys.BILL_ITEM_QTY)
	private Measure<Double, ? extends Quantity> quantity;
	@Transient
	private Amount<Money> amount;

	public BillItem() {

	}

	public BillItem(String productName,
			Measure<Double, ? extends Quantity> productUnit,
			String productDescription, Amount<Money> unitPrice,
			Measure<Double, ? extends Quantity> quantity, Amount<Money> amount) {
		this.productName = productName;
		this.productUnit = productUnit;
		this.productDescription = productDescription;
		this.unitPrice = unitPrice;
		this.quantity = quantity;
		this.amount = amount;
	}

	public static BillItem valueOf(String productName,
			Measure<Double, ? extends Quantity> productUnit,
			String productDescription, Amount<Money> unitPrice,
			Measure<Double, ? extends Quantity> quantity, Amount<Money> amount) {
		return new BillItem(productName, productUnit, productDescription,
				unitPrice, quantity, amount);
	}

}
