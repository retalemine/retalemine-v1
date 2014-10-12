package in.retalemine.data;

import in.retalemine.measure.unit.BillingUnits;

import java.lang.reflect.Method;

import javax.measure.Measure;
import javax.measure.unit.Unit;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;

public class TestDataProvider {

	@BeforeClass
	public void setup() {
		BillingUnits.getInstance();
	}

	@DataProvider(name = "measureSumUpData")
	public static Object[][] measureSumUpData(Method methodName) {
		String[][] validDatas = new String[][] {

				// [kg, g]
				{ "1kg", "2kg", "3kg" },
				{ "1.5kg", "2kg", "3.5kg" },

				{ "500g", "500g", "1kg" },
				{ "250g", "500g", "750g" },

				{ "1kg", "500g", "1.5kg" },
				{ "1kg", "750g", "1.75kg" },

				// [L, mL]
				{ "1L", "2L", "3L" },
				{ "1.5L", "2L", "3.5L" },

				{ "500mL", "500mL", "1L" },
				{ "250mL", "500mL", "750mL" },

				{ "1L", "500mL", "1.5L" },
				{ "1L", "750mL", "1.75L" },

				// [m]
				{ "1m", "2m", "3m" },
				{ "1.5m", "2m", "3.5m" },

				// [ft]
				{ "1ft", "2ft", "3ft" },
				{ "1.5ft", "2ft", "3.5ft" },

				// [in]
				{ "1in", "2in", "3in" },
				{ "1.5in", "2in", "3.5in" },

				// [dz, pcs]
				{ "1dz", "2dz", "3dz" }, { "1.5dz", "2dz", "3.5dz" },

				{ "1pcs", "2pcs", "3pcs" }, { "6pcs", "6pcs", "1dz" },
				{ "10pcs", "10pcs", "20pcs" }, { "10pcs", "14pcs", "2dz" },

				{ "6pcs", "2dz", "2.5dz" },
				{ "2dz", "6pcs", "2.5dz" },
				{ "2dz", "10pcs", "34pcs" },

				// [dz, pkt]
				{ "1pkt", "2pkt", "3pkt" }, { "9pkt", "3pkt", "1dz" },
				{ "10pkt", "10pkt", "20pkt" }, { "10pkt", "14pkt", "2dz" },

				{ "6pkt", "2dz", "2.5dz" }, { "2dz", "6pkt", "2.5dz" },
				{ "2dz", "10pkt", "34pkt" },

		};

		String[][] inValidDatas = new String[][] {

				// null check
				{ null, null },
				{ "2kg", null },

				// compatible check
				{ "2kg", "1L" }, { "2kg", "1m" }, { "2kg", "1in" },
				{ "2kg", "1ft" }, { "2kg", "1dz" }, { "2kg", "6pcs" },
				{ "2kg", "8pkt" }

		};

		if ("test_computeNetQuantity".equals(methodName.getName())) {
			return constructSumUpData(validDatas, 3);
		} else {
			return constructSumUpData(inValidDatas, 2);
		}
	}

	private static Object[][] constructSumUpData(String[][] validDatas,
			int rowLength) {
		int i = 0, j = 0;
		Object[][] rVal = new Object[validDatas.length][rowLength];
		for (String[] validData : validDatas) {
			j = 0;
			for (String data : validData) {
				if (null != data) {
					Double value;
					try {
						value = Double.valueOf(data.replaceAll("[a-zA-Z]", ""));
					} catch (NumberFormatException e) {
						value = 1.0;
					}
					String unit = data.replaceAll("[0-9.]", "");
					rVal[i][j++] = Measure.valueOf(value, Unit.valueOf(unit));
				} else {
					rVal[i][j++] = null;
				}
			}
			i++;
		}
		return rVal;
	}
}
