package in.retalemine.util;

import in.retalemine.data.TestDataProvider;
import in.retalemine.measure.unit.BillingUnits;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.measure.Measure;
import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test()
public class ComputationUtilTest {

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
		// TODO
		// How to ensure that BillingUnits instance is loaded even before
		// calling Unit.valueOf()
		BillingUnits.getInstance();
		for (String unit : ComputationUtil.getValidUnits()) {
			Assert.assertNotNull(Unit.valueOf(unit));
		}
	}

	@Test(enabled = true)
	public void test_getValidUnits() {
		Assert.assertEquals(ComputationUtil.getValidUnits("g"),
				Arrays.asList("kg", "g"));
		Assert.assertEquals(ComputationUtil.getValidUnits("inch"),
				Arrays.asList("in"));
		Assert.assertEquals(ComputationUtil.getValidUnits("dozen"),
				Arrays.asList("dz", "pcs"));
		Assert.assertNull(ComputationUtil.getValidUnits(null));
		Assert.assertNull(ComputationUtil.getValidUnits(""));
		Assert.assertNull(ComputationUtil.getValidUnits(" "));
		Assert.assertNull(ComputationUtil.getValidUnits("killgram"));
	}

	@Test(enabled = true)
	public void test_checkValidUnitsCompatibility() {
		HashSet<List<String>> validUnitsSet = new HashSet<List<String>>();
		BillingUnits.getInstance();
		for (String unit : ComputationUtil.getValidUnits()) {
			validUnitsSet.add(ComputationUtil.getValidUnits(unit));
		}
		Assert.assertEquals(validUnitsSet.size(), 7);
		for (List<String> list : validUnitsSet) {
			for (int i = 1; i < list.size(); i++) {
				Assert.assertEquals(
						Unit.valueOf(list.get(0)).isCompatible(
								Unit.valueOf(list.get(i))), true);
			}
		}
	}

	@Test(enabled = true)
	public void test_getBaseUnit() {
		BillingUnits.getInstance();
		Assert.assertEquals(ComputationUtil.getBaseUnit("g"),
				Unit.valueOf("kg"));
		Assert.assertEquals(ComputationUtil.getBaseUnit("liter"),
				Unit.valueOf("L"));
		Assert.assertEquals(ComputationUtil.getBaseUnit("inch"),
				Unit.valueOf("in"));
		Assert.assertEquals(ComputationUtil.getBaseUnit("dozen "),
				Unit.valueOf("dz"));
		Assert.assertEquals(ComputationUtil.getBaseUnit("pcs"),
				Unit.valueOf("dz"));
		Assert.assertEquals(ComputationUtil.getBaseUnit("packet"),
				Unit.valueOf("dz"));
		Assert.assertNull(ComputationUtil.getBaseUnit("dozz "));
	}

	@Test(enabled = true, dataProvider = "measureSumUpData", dataProviderClass = TestDataProvider.class)
	public void test_computeNetQuantity(
			Measure<Double, ? extends Quantity> quantityX,
			Measure<Double, ? extends Quantity> quantityY,
			Measure<Double, ? extends Quantity> result) {
		Assert.assertEquals(
				ComputationUtil.computeNetQuantity(quantityX, quantityY),
				result);
	}

	@Test(enabled = true)
	public void test_computeAmount() {

	}

}
