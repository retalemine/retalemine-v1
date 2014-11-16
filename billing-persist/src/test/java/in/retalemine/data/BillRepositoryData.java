package in.retalemine.data;

import static javax.measure.unit.NonSI.LITER;
import static javax.measure.unit.SI.KILOGRAM;
import static javax.measure.unit.SI.MILLI;
import in.retalemine.entity.Bill;
import in.retalemine.entity.BillItem;
import in.retalemine.entity.Customer;
import in.retalemine.entity.Payment;
import in.retalemine.entity.PaymentMode;
import in.retalemine.entity.Tax;
import in.retalemine.entity.TaxType;
import in.retalemine.measure.unit.BillingUnits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.measure.Measure;

import org.jscience.economics.money.Money;
import org.jscience.physics.amount.Amount;
import org.testng.annotations.DataProvider;

public class BillRepositoryData {

	@DataProvider(name = "billInsertSaveData")
	public static Object[][] billInsertSaveData() {
		List<BillItem> bItems = new ArrayList<BillItem>();

		BillItem sugar = BillItem.valueOf("Sugar",
				Measure.valueOf(1.0, KILOGRAM), "Sugar - 1kg",
				Amount.valueOf(45.0, BillingUnits.INR),
				Measure.valueOf(5.0, KILOGRAM),
				Amount.valueOf(225.0, BillingUnits.INR));

		BillItem oil = BillItem.valueOf("Oil", Measure.valueOf(1.0, LITER),
				"Oil - 1L", Amount.valueOf(140.0, BillingUnits.INR),
				Measure.valueOf(500.0, MILLI(LITER)),
				Amount.valueOf(70.0, BillingUnits.INR));

		bItems.add(sugar);
		bItems.add(oil);

		Amount<Money> subTotal = Amount.valueOf(295.0, BillingUnits.INR);
		Tax tax = new Tax(TaxType.VAT, 4.0);
		Amount<Money> totalAmount = Amount.valueOf(306.8, BillingUnits.INR);
		Payment payment = new Payment(PaymentMode.CASH, false, true, new Date());
		Customer customer = new Customer("User", "9934454578", "first street");

		Bill bill = new Bill(new Date(), bItems, subTotal, Arrays.asList(tax),
				totalAmount, payment, customer, false);

		return new Object[][] { { bill } };
	}

}
