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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "classpath:spring-mongo-config.xml" })
public class ProductRepositoryTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private ProductRepository prodrepository;
	Currency INR;

	@BeforeClass
	public void setup() {
		INR = new Currency("INR");
	}

	@DataProvider
	public Object[][] productDataProvider() {

		Set<Amount<Money>> prices = new HashSet<Amount<Money>>();
		prices.add(Amount.valueOf(45.0, INR));

		Product<?> product1 = new Product<Mass>("Sugar", Measure.valueOf(1.0,
				SI.KILOGRAM), prices, new Date());
		Product<?> product2 = new Product<Volume>("Sun oil", Measure.valueOf(
				1.0, NonSI.LITER), prices, new Date());

		return new Object[][] { { product1 }, { product2 } };
	}

	@Test(enabled = true)
	public void test_upsert() {
	}

	@Test(enabled = true)
	public void test_updateFirst() {
	}

	@Test(enabled = true)
	public void test_findProductsByName() {
	}

	@Test(enabled = true)
	public void test_findOne() {
	}

	@Test(enabled = true)
	public void test_findAll() {
	}

	@Test(enabled = true)
	public void test_findByProductNameIgnoreCase() {
	}

	@Test(enabled = true, dataProvider = "productDataProvider")
	public void test_save(Product<?> product) {
		prodrepository.deleteAll();
		prodrepository.save(product);
	}
}
