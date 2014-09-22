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

	public BillItem(){
		
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

//	public BillItem(String productDescription, Amount<Money> unitPrice,
//			Measure<Double, V> quantity) {
//		this.productDescription = productDescription;
//		this.unitPrice = unitPrice;
//		this.quantity = quantity;
//	}
	
	public static <U extends Quantity, V extends Quantity> BillItem<U, V> valueOf(
			String productName, Measure<Double, U> productUnit,
			String productDescription, Amount<Money> unitPrice,
			Measure<Double, V> quantity, Amount<Money> amount) {
		return new BillItem<U, V>(productName, productUnit, productDescription,
				unitPrice, quantity, amount);
	}
//
//	public String getProductName() {
//		return productName;
//	}
//
//	public void setProductName(String productName) {
//		this.productName = productName;
//	}
//
//	public Measure<Double, U> getProductUnit() {
//		return productUnit;
//	}
//
//	public void setProductUnit(Measure<Double, U> productUnit) {
//		this.productUnit = productUnit;
//	}
//
//	public String getProductDescription() {
//		return productDescription;
//	}
//
//	public void setProductDescription(String productDescription) {
//		this.productDescription = productDescription;
//	}
//
//	public Amount<Money> getUnitPrice() {
//		return unitPrice;
//	}
//
//	public void setUnitPrice(Amount<Money> unitPrice) {
//		this.unitPrice = unitPrice;
//	}
//
//	public Measure<Double, V> getQuantity() {
//		return quantity;
//	}
//
//	public void setQuantity(Measure<Double, V> quantity) {
//		this.quantity = quantity;
//	}
//
//	public Amount<Money> getAmount() {
//		return amount;
//	}
//
//	public void setAmount(Amount<Money> amount) {
//		this.amount = amount;
//	}

}
