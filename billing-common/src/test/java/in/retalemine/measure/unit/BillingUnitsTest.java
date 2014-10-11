package in.retalemine.measure.unit;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = { "billing-unit" })
public class BillingUnitsTest {

	@Test(enabled = true)
	public void test_billingUnits() {
		Assert.assertNotNull(BillingUnits.getInstance());
	}

}
