package in.retalemine.entity;

import in.retalemine.constants.BillingConstants;
import in.retalemine.constants.MongoDBKeys;

import java.util.Date;
import java.util.Set;

import javax.measure.Measure;
import javax.measure.quantity.Quantity;

import org.jscience.economics.money.Money;
import org.jscience.physics.amount.Amount;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "products")
public class Product<Q extends Quantity> {

	@Id
	private String productId;
	@Field(MongoDBKeys.PRODUCT_NAME)
	private String productName;
	@Field(MongoDBKeys.PRODUCT_UNIT)
	private Measure<Double, Q> productUnit;
	@Transient()
	private Amount<Money> unitPrice;
	@Field(MongoDBKeys.PRODUCT_PRICES)
	private Set<Amount<Money>> unitPrices;
	@Field(MongoDBKeys.PRODUCT_CREATION_OR_MODIFIED_DATE)
	private Date createdOrModifiedDate;

	public Product() {

	}

	public Product(String productName, Measure<Double, Q> productUnit,
			Set<Amount<Money>> unitPrices, Date createdOrModifiedDate) {
		this.productId = productName + BillingConstants.PRODUCT_DESC_DIVIDER
				+ productUnit;
		this.productName = productName;
		this.productUnit = productUnit;
		this.unitPrices = unitPrices;
		this.createdOrModifiedDate = createdOrModifiedDate;
	}

	public Product(String productName, Measure<Double, Q> productUnit,
			Amount<Money> unitPrice, Date createdOrModifiedDate) {
		this.productId = productName + BillingConstants.PRODUCT_DESC_DIVIDER
				+ productUnit;
		this.productName = productName;
		this.productUnit = productUnit;
		this.unitPrice = unitPrice;
		this.createdOrModifiedDate = createdOrModifiedDate;
	}

	public String getProductId() {
		return productId;
	}

	public String getProductName() {
		return productName;
	}

	public Measure<Double, Q> getProductUnit() {
		return productUnit;
	}

	public Amount<Money> getUnitPrice() {
		return unitPrice;
	}

	public Set<Amount<Money>> getUnitPrices() {
		return unitPrices;
	}

	public Date getCreatedOrModifiedDate() {
		return createdOrModifiedDate;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("<Product><productId>");
		builder.append(productId);
		builder.append("</productId><productName>");
		builder.append(productName);
		builder.append("</productName><productUnit>");
		builder.append(productUnit);
		builder.append("</productUnit><unitPrice>");
		builder.append(unitPrice);
		builder.append("</unitPrice><unitPrices>");
		builder.append(unitPrices);
		builder.append("</unitPrices><createdOrModifiedDate>");
		builder.append(createdOrModifiedDate);
		builder.append("</createdOrModifiedDate></Product>");
		return builder.toString();
	}

}
