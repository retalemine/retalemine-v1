package in.retalemine.data;

import in.retalemine.measure.unit.BillingUnits;

import javax.measure.Measure;
import javax.measure.unit.Unit;

import org.testng.annotations.DataProvider;

public class TestDataProvider {

	@DataProvider(name = "measureSumUpData")
	public static Object[][] measureSumUpData() {
		int i = 0, j = 0;
		String[][] validDatas = new String[][] {

				// [kg, g]
				{ "1kg", "2kg", "3kg" }, { "1.5kg", "2kg", "3.5kg" },

				{ "500g", "500g", "1kg" }, { "250g", "500g", "750g" },

				{ "1kg", "500g", "1.5kg" },

				// [L, mL]
				{ "1L", "2L", "3L" }, { "1.5L", "2L", "3.5L" },

				{ "500mL", "500mL", "1L" }, { "250mL", "500mL", "750mL" },

				{ "1L", "500mL", "1.5L" },

				// [m]
				{ "1m", "2m", "3m" }, { "1.5m", "2m", "3.5m" },

				// [ft]
				{ "1ft", "2ft", "3ft" }, { "1.5ft", "2ft", "3.5ft" },

				// [in]
				{ "1in", "2in", "3in" }, { "1.5in", "2in", "3.5in" },

				// [pcs, dz]
				{ "1dz", "2dz", "3dz" }, { "1pcs", "2pcs", "3pcs" },

				{ "1.5dz", "2dz", "3.5dz" }, { "6pcs", "2dz", "2.5dz" },

				// [pkt, dz]
				{ "1pkt", "2pkt", "3pkt" },

				{ "1.5dz", "2dz", "3.5dz" }, { "6pkt", "2dz", "2.5dz" }

		};

		String[][] inValidDatas = new String[][] {

		{}

		};

		Object[][] rVal = new Object[validDatas.length][3];
		BillingUnits.getInstance();
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
