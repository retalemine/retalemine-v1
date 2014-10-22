package in.retalemine.data;

import in.retalemine.entity.Product;
import in.retalemine.measure.unit.BillingUnits;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.measure.Measure;
import javax.measure.quantity.Mass;
import javax.measure.quantity.Volume;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;

import org.jscience.economics.money.Money;
import org.jscience.physics.amount.Amount;
import org.testng.annotations.DataProvider;

public class ProductRepositoryData {

	@DataProvider(name = "productSaveData")
	public static Object[][] productSaveData() {

		Set<Amount<Money>> prices = new HashSet<Amount<Money>>();
		prices.add(Amount.valueOf(45.0, BillingUnits.INR));

		Product<?> product = new Product<Mass>("Sugar", Measure.valueOf(1.0,
				SI.KILOGRAM), prices, new Date());

		return new Object[][] { { product } };
	}

	@DataProvider(name = "productUpsertData")
	public static Object[][] productUpsertData() {

		Product<Volume> firstProduct, secondProduct, firstProductUpdated, firstProductReset;
		final Product<Volume> firstProductObj, secondProductObj, firstProductUpdatedObj, firstProductResetObj;
		List<Product<Volume>> oneProductList, twoProductList, twoProductUpdatedList, twoProductResetList;
		/** -- **/
		String product = "Sunflower Oil";
		Measure<Double, Volume> liter = Measure.valueOf(1.0, NonSI.LITER);
		final Amount<Money> firstRate = Amount.valueOf(155.0, BillingUnits.INR);
		firstProduct = new Product<Volume>(product, liter, firstRate,
				new Date());
		Set<Amount<Money>> firstRateSet = new HashSet<Amount<Money>>() {
			private static final long serialVersionUID = -1747204176656953536L;
			{
				add(firstRate);
			}
		};
		firstProductObj = new Product<Volume>(product, liter, firstRateSet,
				new Date());
		oneProductList = new ArrayList<Product<Volume>>() {
			private static final long serialVersionUID = -3247727786018672288L;
			{
				add(firstProductObj);
			}
		};
		/** -- **/
		Measure<Double, Volume> milliLiter = Measure.valueOf(500.0,
				SI.MILLI(NonSI.LITER));
		final Amount<Money> secondRate = Amount.valueOf(80.0, BillingUnits.INR);
		secondProduct = new Product<Volume>(product, milliLiter, secondRate,
				new Date());
		Set<Amount<Money>> secondRateSet = new HashSet<Amount<Money>>() {
			private static final long serialVersionUID = -2670194938302981528L;
			{
				add(secondRate);
			}
		};
		secondProductObj = new Product<Volume>(product, liter, secondRateSet,
				new Date());
		twoProductList = new ArrayList<Product<Volume>>() {
			private static final long serialVersionUID = -2470829611245233328L;
			{
				add(secondProductObj);
			}
		};
		/** -- **/
		final Amount<Money> oneAnotherRate = Amount.valueOf(145.0,
				BillingUnits.INR);
		firstProductUpdated = new Product<Volume>(product, liter,
				oneAnotherRate, new Date());
		Set<Amount<Money>> oneAnotherRateSet = new HashSet<Amount<Money>>() {
			private static final long serialVersionUID = 7591023670383873663L;
			{
				add(firstRate);
				add(oneAnotherRate);
			}
		};
		firstProductUpdatedObj = new Product<Volume>(product, liter,
				oneAnotherRateSet, new Date());
		twoProductUpdatedList = new ArrayList<Product<Volume>>() {
			private static final long serialVersionUID = 1510246908307676937L;
			{
				add(firstProductUpdatedObj);
				add(secondProductObj);
			}
		};
		/** -- **/
		final Amount<Money> resetRate = Amount.valueOf(180.0, BillingUnits.INR);
		Set<Amount<Money>> resetRateSet = new HashSet<Amount<Money>>() {
			private static final long serialVersionUID = -8136777748874197385L;
			{
				add(resetRate);
			}
		};
		firstProductReset = new Product<Volume>(product, liter, resetRateSet,
				new Date());
		firstProductResetObj = new Product<Volume>(product, liter,
				resetRateSet, new Date());
		twoProductResetList = new ArrayList<Product<Volume>>() {
			private static final long serialVersionUID = 7607809816720378150L;
			{
				add(firstProductResetObj);
				add(secondProductObj);
			}
		};

		return new Object[][] { { firstProduct, oneProductList, false },
				{ secondProduct, twoProductList, false },
				{ firstProductUpdated, twoProductUpdatedList, false },
				{ firstProductReset, twoProductResetList, true } };
	}
}
