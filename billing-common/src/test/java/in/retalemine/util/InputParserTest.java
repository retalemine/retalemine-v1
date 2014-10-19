package in.retalemine.util;

import in.retalemine.data.InputParserTestData;

import org.testng.Assert;
import org.testng.annotations.Test;

public class InputParserTest {

	@Test(enabled = true, dataProvider = "productname", dataProviderClass = InputParserTestData.class)
	public void test_getCamelCaseString(String actual, String expected) {
		Assert.assertEquals(InputParser.getCamelCaseString(actual), expected);
	}

	@Test(enabled = true, dataProvider = "product", dataProviderClass = InputParserTestData.class)
	public void test_resolveProductUnit(String actual, String[] expected) {
		Assert.assertEquals(InputParser.resolveProductUnit(actual), expected);
	}

	@Test(enabled = true, dataProvider = "quantity", dataProviderClass = InputParserTestData.class)
	public void test_resolveQuantity(String actual, String[] expected) {
		Assert.assertEquals(InputParser.resolveQuantity(actual), expected);
	}

}
