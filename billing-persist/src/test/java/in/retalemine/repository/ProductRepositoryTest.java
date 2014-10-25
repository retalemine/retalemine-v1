package in.retalemine.repository;

import in.retalemine.data.ProductRepositoryData;
import in.retalemine.entity.Product;

import java.util.List;

import javax.measure.quantity.Quantity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "classpath:spring-mongo-config.xml",
		"classpath:test-spring-mongo-config.xml" })
public class ProductRepositoryTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private ProductRepository prodrepository;

	@BeforeClass
	public void setup() {
		prodrepository.deleteAll();
	}

	@Test(enabled = true)
	public void test_deleteAll() {
		prodrepository.deleteAll();
		Assert.assertEquals(prodrepository.count(), 0);
	}

	@Test(enabled = true, dataProvider = "productSaveData", dataProviderClass = ProductRepositoryData.class)
	public void test_save(Product<?> product) {
		prodrepository.save(product);
	}

	@Test(enabled = true, dataProvider = "productSaveData", dataProviderClass = ProductRepositoryData.class)
	public void test_findOne(Product<?> expectedProduct) {
		prodrepository.save(expectedProduct);
		Product<?> actualProduct = prodrepository.findOne(expectedProduct
				.getObjectId());
		Assert.assertNotNull(actualProduct);
		assertProduct(actualProduct, expectedProduct);
	}

	@Test(enabled = false, dependsOnMethods = { "test_deleteAll" }, dataProvider = "productUpsertData", dataProviderClass = ProductRepositoryData.class)
	public <T extends Quantity> void test_upsert(Product<T> product,
			List<Product<T>> expectedList, Boolean reset) {
		prodrepository.upsert(product, reset);
		List<Product<?>> actualList = prodrepository.findAll();
	}

	@Test(enabled = false)
	public void test_updateFirst() {
	}

	@Test(enabled = false)
	public void test_findProductsByName() {
	}

	@Test(enabled = false, dependsOnMethods = { "test_save", "test_upsert" })
	public void test_findAll() {
		prodrepository.findAll();
	}

	@Test(enabled = false)
	public void test_findByProductNameIgnoreCase() {
	}

	public void assertProduct(Product<?> actualObj, Product<?> expectedObj) {
		Assert.assertEquals(actualObj.getProductName(),
				expectedObj.getProductName());
		Assert.assertEquals(actualObj.getProductUnit(),
				expectedObj.getProductUnit());
		Assert.assertEquals(actualObj.getUnitPrices(),
				expectedObj.getUnitPrices());
	}

}
