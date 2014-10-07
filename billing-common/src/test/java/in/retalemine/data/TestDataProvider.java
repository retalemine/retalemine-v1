package in.retalemine.data;

import javax.measure.Measure;
import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;

import org.testng.annotations.DataProvider;

public class TestDataProvider {

	@DataProvider(name = "create")
	public static Object[][] createData() {
		// 1kg,1kg,2kg,3kg
		// 1kg,1.5kg,2kg,3.5kg
		// 1kg,1kg,500g,1.5kg
		// 1kg,500g,500g,1000g->1kg
		// 1kg,250g,500g,750g
		// 200g,500g,100g,600g
		// 2kg, 1pkt, 2pkt, 3pkt
		// 1kg, 1kg, 2pkt, 3kg

		// 1L,1L,2L,3L
		// 1L,1.5L,2L,3.5L
		// 1L,1L,500mL,1.5L
		// 1L,500mL,500mL,1000mL->1L
		// 1L,250mL,500mL,750mL

		// 1m,1m,2m,3m
		// 1m,1.5m,2m,3.5m

		// 1ft,1ft,2ft,3ft
		// 1ft,1.5ft,2ft,3.5ft

		// 1in,1in,2in,3in
		// 1in,1.5in,2in,3.5in

		Measure<Double, ? extends Quantity> oneKg = Measure.valueOf(1.0,
				Unit.valueOf("kg"));
		return new Object[][] { { "" } };
	}
}
