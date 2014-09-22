package in.retalemine.repository;

import in.retalemine.entity.Product;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.measure.Measure;
import javax.measure.quantity.Mass;
import javax.measure.quantity.Volume;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;

import org.jscience.economics.money.Currency;
import org.jscience.economics.money.Money;
import org.jscience.physics.amount.Amount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "classpath:spring-mongo-config.xml" })
public class ProductRepositoryTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private ProductRepository prodrepository;

	@Test()
	public void test_save() {
		Currency INR = new Currency("INR");
		Set<Amount<Money>> prices = new HashSet<Amount<Money>>();
		prices.add(Amount.valueOf(45.0, INR));

		prodrepository.deleteAll();

		Product<?> prod = new Product<Mass>("Sugar", Measure.valueOf(1.0,
				SI.KILOGRAM), "", prices, new Date());
		prodrepository.save(prod);

		prod = new Product<Volume>("Sun oil",
				Measure.valueOf(1.0, NonSI.LITER), "", prices, new Date());
		prodrepository.save(prod);

		Product<?> prodfind = prodrepository.findByProductName("Sugar");
	}
}
