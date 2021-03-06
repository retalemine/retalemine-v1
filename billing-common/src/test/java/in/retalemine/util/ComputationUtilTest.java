package in.retalemine.util;

import in.retalemine.data.ComputationUtilTestData;
import in.retalemine.measure.unit.BillingUnits;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.measure.Measure;
import javax.measure.converter.ConversionException;
import javax.measure.quantity.Quantity;

import javolution.text.Text;

import org.jscience.economics.money.Money;
import org.jscience.physics.amount.Amount;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ComputationUtilTest {

	@BeforeClass
	public void setup() {
	}

	@Test(enabled = true)
	public void test_getValidUnit() {
		Assert.assertEquals(ComputationUtil.getValidUnit("G"), "g");
		Assert.assertEquals(ComputationUtil.getValidUnit("Gram"), "g");
		Assert.assertEquals(ComputationUtil.getValidUnit("Gram "), "g");
		Assert.assertNull(ComputationUtil.getValidUnit("Grammy"));
		Assert.assertNull(ComputationUtil.getValidUnit(null));
	}

	/**
	 * Ensure that number of valid char sequence is in sync with Collection size
	 */
	@Test(enabled = true)
	public void test_getValidUnitsSize() {
		Assert.assertEquals(10, ComputationUtil.getValidUnits().size(),
				"Initial capacity for the validunits list is mismatching");
	}

	/**
	 * Ensure that, for each char sequence assumed to be valid, a Unit object is
	 * getting created without any error
	 */
	@Test(enabled = true)
	public void test_getValidUnitsIntegrity() {
		for (String unit : ComputationUtil.getValidUnits()) {
			Assert.assertNotNull(BillingUnits.valueOf(unit));
			Assert.assertEquals(BillingUnits.valueOf(unit).toString(), unit);
		}
	}

	@Test(enabled = true)
	public void test_getValidUnits() {
		Assert.assertEquals(ComputationUtil.getValidUnitsGroup("g"),
				Arrays.asList("kg", "g"));
		Assert.assertEquals(ComputationUtil.getValidUnitsGroup("inch"),
				Arrays.asList("in"));
		Assert.assertEquals(ComputationUtil.getValidUnitsGroup("dozen"),
				Arrays.asList("dz", "pcs"));
		Assert.assertNull(ComputationUtil.getValidUnitsGroup(null));
		Assert.assertNull(ComputationUtil.getValidUnitsGroup(""));
		Assert.assertNull(ComputationUtil.getValidUnitsGroup(" "));
		Assert.assertNull(ComputationUtil.getValidUnitsGroup("killgram"));
	}

	@Test(enabled = true)
	public void test_checkValidUnitsCompatibility() {
		HashSet<List<String>> validUnitsSet = new HashSet<List<String>>();
		for (String unit : ComputationUtil.getValidUnits()) {
			validUnitsSet.add(ComputationUtil.getValidUnitsGroup(unit));
		}
		Assert.assertEquals(validUnitsSet.size(), 7);
		for (List<String> list : validUnitsSet) {
			for (int i = 1; i < list.size(); i++) {
				Assert.assertEquals(BillingUnits.valueOf(list.get(0))
						.isCompatible(BillingUnits.valueOf(list.get(i))), true);
			}
		}
	}

	@Test(enabled = true)
	public void test_getBaseUnit() {
		Assert.assertEquals(ComputationUtil.getBaseUnit("g"),
				BillingUnits.valueOf("kg"));
		Assert.assertEquals(ComputationUtil.getBaseUnit("liter"),
				BillingUnits.valueOf("L"));
		Assert.assertEquals(ComputationUtil.getBaseUnit("inch"),
				BillingUnits.valueOf("in"));
		Assert.assertEquals(ComputationUtil.getBaseUnit("dozen "),
				BillingUnits.valueOf("dz"));
		Assert.assertEquals(ComputationUtil.getBaseUnit("pcs"),
				BillingUnits.valueOf("dz"));
		Assert.assertEquals(ComputationUtil.getBaseUnit("packet"),
				BillingUnits.valueOf("dz"));
		Assert.assertNull(ComputationUtil.getBaseUnit("dozz "));
	}

	@Test(enabled = true, dataProvider = "netQuantityData", dataProviderClass = ComputationUtilTestData.class)
	public void test_computeNetQuantity(
			Measure<Double, ? extends Quantity> quantityX,
			Measure<Double, ? extends Quantity> quantityY,
			Measure<Double, ? extends Quantity> result) {
		Assert.assertEquals(
				ComputationUtil.computeNetQuantity(quantityX, quantityY),
				result);
	}

	@Test(enabled = true, dataProvider = "netQuantityExceptionData", dataProviderClass = ComputationUtilTestData.class, expectedExceptions = AssertionError.class)
	public void test_computeNetQuantityExceptions(
			Measure<Double, ? extends Quantity> quantityX,
			Measure<Double, ? extends Quantity> quantityY) {
		ComputationUtil.computeNetQuantity(quantityX, quantityY);
	}

	@Test(enabled = true, dataProvider = "amountData", dataProviderClass = ComputationUtilTestData.class)
	public void test_computeAmount(
			Measure<Double, ? extends Quantity> unitQuantity,
			Amount<Money> unitRate,
			Measure<Double, ? extends Quantity> netQuantity,
			Amount<Money> result) {
		Assert.assertEquals(
				ComputationUtil.computeAmount(unitQuantity, unitRate,
						netQuantity).toText(), result.toText());
	}

	@Test(enabled = true, dataProvider = "amountExceptionData", dataProviderClass = ComputationUtilTestData.class, expectedExceptions = ConversionException.class)
	public void test_computeAmountExceptions(
			Measure<Double, ? extends Quantity> unitQuantity,
			Amount<Money> unitRate,
			Measure<Double, ? extends Quantity> netQuantity) {
		ComputationUtil.computeAmount(unitQuantity, unitRate, netQuantity);
	}

	@Test(enabled = true, dataProvider = "getUnitData", dataProviderClass = ComputationUtilTestData.class)
	public void test_getUnitQuantity(String desc,
			Measure<Double, ? extends Quantity> unitQuantity) {
		Assert.assertEquals(ComputationUtil.getUnitQuantity(desc).toString(),
				unitQuantity.toString());
	}

	@Test(enabled = true, dataProvider = "amountTextData", dataProviderClass = ComputationUtilTestData.class)
	public void test_computeClearAmount(String input, Double result) {
		Assert.assertEquals(
				ComputationUtil.computeClearAmount(new Text(input)), result);
	}

	@Test(enabled = true, dataProvider = "roundedAmountData", dataProviderClass = ComputationUtilTestData.class)
	public void test_computeRoundedAmount(Amount<Money> input,
			Amount<Money> result) {
		Assert.assertEquals(ComputationUtil.computeRoundedAmount(input)
				.toText(), result.toText());
	}

}
