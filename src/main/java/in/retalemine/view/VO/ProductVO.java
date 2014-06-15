package in.retalemine.view.VO;

import java.util.ArrayList;
import java.util.List;

import javax.measure.Measure;
import javax.measure.quantity.Quantity;

import org.jscience.economics.money.Money;
import org.jscience.physics.amount.Amount;

public class ProductVO<Q extends Quantity> {

	private String productName;
	private Measure<Double, Q> productUnit;
	private String productDescription;
	private List<Amount<Money>> unitPrices = new ArrayList<Amount<Money>>();

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Measure<Double, Q> getProductUnit() {
		return productUnit;
	}

	public void setProductUnit(Measure<Double, Q> productUnit) {
		this.productUnit = productUnit;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public List<Amount<Money>> getUnitPrices() {
		return unitPrices;
	}

	public void setUnitPrices(List<Amount<Money>> unitPrices) {
		this.unitPrices = unitPrices;
	}

	public static <T extends Quantity> ProductVO<T> valueOf(String productName,
			Measure<Double, T> productUnit, List<Amount<Money>> unitPrices) {
		ProductVO<T> obj = new ProductVO<T>();
		obj.setProductName(productName);
		obj.setProductUnit(productUnit);
		obj.setProductDescription(productName + " - " + productUnit);
		if (null != unitPrices) {
			obj.setUnitPrices(unitPrices);
		} else {
			obj.setUnitPrices(new ArrayList<Amount<Money>>());
		}

		return obj;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((productDescription == null) ? 0 : productDescription
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductVO<?> other = (ProductVO<?>) obj;
		if (productDescription == null) {
			if (other.productDescription != null)
				return false;
		} else if (!productDescription.equals(other.productDescription))
			return false;
		return true;
	}

}
