package in.retalemine.data;

import org.testng.annotations.DataProvider;

public class InputParserTestData {

	@DataProvider(name = "productname")
	public static Object[][] getCamelCaseStringData() {
		String[][] validDatas = new String[][] {

		{ "sugar", "Sugar" }, { "item nameD SoOn 101", "Item Named Soon 101" },
				{ " coOl drink bTl  ", "Cool Drink btl" },
				{ "KurKure snacks pkt", "Kurkure Snacks pkt" },
				{ "GHEE  ", "Ghee" },
				{ "## MyValueSeen 0 0 0 ## %%", "## Myvalueseen 0 0 0 ## %%" }

		};
		return validDatas;
	}

	@DataProvider(name = "product")
	public static Object[][] resolveProductUnitData() {
		Object[][] validDatas = new Object[][] {

				{ "sugar12kg", new String[] { "Sugar", "12", "kg" } },
				{ "  sugar  12.50  kg  ",
						new String[] { "Sugar", "12.50", "kg" } },

				{ "Sugar-12kg", new String[] { "Sugar", "12", "kg" } },
				{ "  Sugar  -  12.50  kg  ",
						new String[] { "Sugar", "12.50", "kg" } },

				{ "Sugar--12kg", new String[] { "Sugar", "12", "kg" } },
				{ "  Sugar  --  12.50  kg  ",
						new String[] { "Sugar", "12.50", "kg" } },
				{ "  Sugar  - -  .5  kg  ",
						new String[] { "Sugar", ".5", "kg" } },

				{ "Oil 10l", new String[] { "Oil", "10", "L" } },
				{ "Oil 16.50Litre", new String[] { "Oil", "16.50", "L" } },

				{ " coOl drink   500ml bTl  ",
						new String[] { "Cool Drink btl", "500", "mL" } },

				{ "101 soap powder  11.5 kg packet",
						new String[] { "101 Soap Powder packet", "11.5", "kg" } }

		};
		return validDatas;
	}

	@DataProvider(name = "quantity")
	public static Object[][] resolveQuantityData() {
		Object[][] validDatas = new Object[][] {
				{ "5kg", new String[] { "5", "kg" } },
				{ "12kg", new String[] { "12", "kg" } },
				{ ".5kg", new String[] { ".5", "kg" } },
				{ "12.5kg", new String[] { "12.5", "kg" } },
				{ "12.50kg", new String[] { "12.50", "kg" } },

				{ "5Kg", new String[] { "5", "kg" } },
				{ "12KG", new String[] { "12", "kg" } },
				{ ".5kilogram", new String[] { ".5", "kg" } },
				{ "12.5Liter", new String[] { "12.5", "L" } },
				{ "12.50m", new String[] { "12.50", "m" } },

				{ "Lorem5kg", new String[] { "5", "kg" } },
				{ "Lorem 12kg", new String[] { "12", "kg" } },
				{ "  Lorem  .5kg", new String[] { ".5", "kg" } },
				{ "Lorem  12.5kg  ", new String[] { "12.5", "kg" } },
				{ "Lorem  12.50  kg  ", new String[] { "12.50", "kg" } },

				{ "Lorem5kg packet", new String[] { "5", "kg" } },
				{ "Lorem  12kg pkt", new String[] { "12", "kg" } },
				{ "  Lorem   .5kg pkt", new String[] { ".5", "kg" } },
				{ "Lorem  12.5kg  pkt", new String[] { "12.5", "kg" } },
				{ "Lorem  12.50  kg  pkt  ", new String[] { "12.50", "kg" } },
				{ "Lorem.  12.50  kg  pkt  ", new String[] { "12.50", "kg" } },

				{ "101Lorem  5kg packet", new String[] { "5", "kg" } },
				{ "  101  Lorem 12kg pkt", new String[] { "12", "kg" } },
				{ "  Lorem  101    .5kg pkt", new String[] { ".5", "kg" } },
				{ "Lorem 12.5 Ipsum   12.5kg  pkt",
						new String[] { "12.5", "kg" } },
				{ "Lorem 12.5Ipsum  12.50  kg  pkt  ",
						new String[] { "12.50", "kg" } },

				{ "Lorem 12.5Ipsum  12.50 kilo", null },

				{ "12.50.kg", null }, { "  12.50.  kg  ", null },

				{ "..50kg", null }, { "  ..50kg  ", null },

				{ "12..50kg", null }, { "  12..50kg  ", null },

				{ ".12.50kg", null }, { "  .12.50  kg  ", null }

		};
		return validDatas;
	}

}
