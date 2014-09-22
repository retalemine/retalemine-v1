package in.retalemine.entity;

import in.retalemine.constants.MongoDBKeys;

import java.util.Date;
import java.util.Set;

import javax.measure.Measure;
import javax.measure.quantity.Quantity;

import org.jscience.economics.money.Money;
import org.jscience.physics.amount.Amount;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "products")
public class Product<Q extends Quantity> {

	@Field(MongoDBKeys.PRODUCT_NAME)
	private String productName;
	@Field(MongoDBKeys.PRODUCT_UNIT)
	private Measure<Double, Q> productUnit;
	@Transient
	private String productDescription;
	@Field(MongoDBKeys.PRODUCT_PRICES)
	private Set<Amount<Money>> unitPrices;
	@Field(MongoDBKeys.PRODUCT_CREATION_OR_MODIFIED_DATE)
	private Date createdOrModifiedDate;

	public Product() {

	}

	public Product(String productName, Measure<Double, Q> productUnit,
			String productDescription, Set<Amount<Money>> unitPrices,
			Date createdOrModifiedDate) {
		this.productName = productName;
		this.productUnit = productUnit;
		this.productDescription = productDescription;
		this.unitPrices = unitPrices;
		this.createdOrModifiedDate = createdOrModifiedDate;
	}
}
