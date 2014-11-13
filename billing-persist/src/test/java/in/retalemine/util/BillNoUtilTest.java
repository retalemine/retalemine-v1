package in.retalemine.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.testng.annotations.Test;

public class BillNoUtilTest {

	@Test(enabled = true)
	public void test_generateBillNo() {
		assertThat(BillNoUtil.generateBillNo(BillNoUtil.suffixLen).length(),
				equalTo(12));
		assertThat(
				BillNoUtil.generateBillNo(BillNoUtil.suffixLen + 1).length(),
				equalTo(13));
	}
}
