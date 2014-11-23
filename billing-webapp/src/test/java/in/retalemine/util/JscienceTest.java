package in.retalemine.util;

import in.retalemine.measure.unit.BillingUnits;

import org.jscience.economics.money.Money;
import org.jscience.physics.amount.Amount;
import org.testng.Assert;
import org.testng.annotations.Test;

public class JscienceTest {

	@Test(enabled = true)
	public void test_Amount() {
		Amount<Money> value = Amount.valueOf(306.6, BillingUnits.INR);
		Assert.assertEquals(value.longValue(BillingUnits.INR), 307);
		value = Amount.valueOf(306.49, BillingUnits.INR);
		Assert.assertEquals(value.longValue(BillingUnits.INR), 306);
	}
}
