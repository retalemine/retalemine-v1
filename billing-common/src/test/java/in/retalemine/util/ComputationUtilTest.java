package in.retalemine.util;

import in.retalemine.measure.unit.BillingUnits;

import java.util.Arrays;

import javax.measure.Measure;
import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;

import org.testng.Assert;
import org.testng.annotations.Test;

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
		Assert.assertEquals(12, ComputationUtil.getValidUnits().size(),
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
				Arrays.asList("kg", "g", "pcs", "dz"));
		Assert.assertEquals(ComputationUtil.getValidUnits("dozen"),
				Arrays.asList("pcs", "dz"));
		Assert.assertNull(ComputationUtil.getValidUnits(null));
		Assert.assertNull(ComputationUtil.getValidUnits(""));
		Assert.assertNull(ComputationUtil.getValidUnits(" "));
		Assert.assertNull(ComputationUtil.getValidUnits("killgram"));
	}

	@Test(enabled = true)
	public void test_getBaseUnit() {
		BillingUnits.getInstance();
		Assert.assertEquals(ComputationUtil.getBaseUnit("g"),
				Unit.valueOf("kg"));
		Assert.assertEquals(ComputationUtil.getBaseUnit("liter"),
				Unit.valueOf("L"));
		Assert.assertEquals(ComputationUtil.getBaseUnit("Med"),
				Unit.valueOf("Medium"));
		Assert.assertEquals(ComputationUtil.getBaseUnit("inch"),
				Unit.valueOf("m"));
		Assert.assertEquals(ComputationUtil.getBaseUnit("dozen "),
				Unit.valueOf("pcs"));
		Assert.assertNull(ComputationUtil.getBaseUnit("dozz "));
	}

	@Test(enabled = false)
	public void test_computeNetQuantity(
			Measure<Double, ? extends Quantity> productUnit,
			Measure<Double, ? extends Quantity> quantity1,
			Measure<Double, ? extends Quantity> quantity2,
			Measure<Double, ? extends Quantity> result) {
		// BillingUnits.getInstance();
		Assert.assertEquals(ComputationUtil.computeNetQuantity(productUnit,
				quantity1, quantity2), result);
	}

	@Test(enabled = true)
	public void test_computeAmount() {

	}

}
