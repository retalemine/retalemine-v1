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

		final Product<Volume> firstProduct, secondProduct, firstProductUpdated, firstProductReset;
		final Product<Volume> firstProductObj, secondProductObj, firstProductUpdatedObj, firstProductResetObj;
		final List<Product<Volume>> oneProductList, twoProductList, twoProductUpdatedList, twoProductResetList;
		List<Product<Volume>> actual;
		List<List<Product<Volume>>> expected;
		/** -- **/
		String product = "Sunflower Oil";
		Date date = new Date();
		Measure<Double, Volume> liter = Measure.valueOf(1.0, NonSI.LITER);
		final Amount<Money> firstRate = Amount.valueOf(155.0, BillingUnits.INR);
		firstProduct = new Product<Volume>(product, liter, firstRate, date);
		Set<Amount<Money>> firstRateSet = new HashSet<Amount<Money>>() {
			private static final long serialVersionUID = -1747204176656953536L;
			{
				add(firstRate);
			}
		};
		firstProductObj = new Product<Volume>(product, liter, firstRateSet,
				date);
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
				date);
		Set<Amount<Money>> secondRateSet = new HashSet<Amount<Money>>() {
			private static final long serialVersionUID = -2670194938302981528L;
			{
				add(secondRate);
			}
		};
		secondProductObj = new Product<Volume>(product, milliLiter,
				secondRateSet, date);
		twoProductList = new ArrayList<Product<Volume>>() {
			private static final long serialVersionUID = -2470829611245233328L;
			{
				add(firstProductObj);
				add(secondProductObj);
			}
		};
		/** -- **/
		final Amount<Money> oneAnotherRate = Amount.valueOf(145.0,
				BillingUnits.INR);
		firstProductUpdated = new Product<Volume>(product, liter,
				oneAnotherRate, date);
		Set<Amount<Money>> oneAnotherRateSet = new HashSet<Amount<Money>>() {
			private static final long serialVersionUID = 7591023670383873663L;
			{
				add(firstRate);
				add(oneAnotherRate);
			}
		};
		firstProductUpdatedObj = new Product<Volume>(product, liter,
				oneAnotherRateSet, date);
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
				date);
		firstProductResetObj = new Product<Volume>(product, liter,
				resetRateSet, date);
		twoProductResetList = new ArrayList<Product<Volume>>() {
			private static final long serialVersionUID = 7607809816720378150L;
			{
				add(firstProductResetObj);
				add(secondProductObj);
			}
		};

		actual = new ArrayList<Product<Volume>>(4) {
			private static final long serialVersionUID = -1360069955475948541L;

			{
				add(firstProduct);
				add(secondProduct);
				add(firstProductUpdated);
				add(firstProductReset);
			}
		};
		expected = new ArrayList<List<Product<Volume>>>(4) {
			private static final long serialVersionUID = -6383513906077841474L;

			{
				add(oneProductList);
				add(twoProductList);
				add(twoProductUpdatedList);
				add(twoProductResetList);
			}
		};

		// return new Object[][] { { firstProduct, oneProductList, false },
		// { secondProduct, twoProductList, false },
		// { firstProductUpdated, twoProductUpdatedList, false },
		// { firstProductReset, twoProductResetList, true } };
		return new Object[][] { { actual, new int[] { 0, 0, 0, 1 }, expected } };
	}

}
