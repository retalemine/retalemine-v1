package in.retalemine.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import in.retalemine.constants.MongoDBKeys;
import in.retalemine.data.ProductRepositoryData;
import in.retalemine.entity.Product;

import java.util.List;

import javax.measure.quantity.Quantity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "classpath:spring-mongo-config.xml",
		"classpath:test-spring-mongo-config.xml" })
public class ProductRepositoryTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private ProductRepository prodrepository;
	@Autowired
	private MongoTemplate mongoTemplate;

	@BeforeClass
	public void setup() {
		prodrepository.deleteAll();
		assertThat(prodrepository.count(), equalTo(0l));
	}

	@Test(enabled = true, dataProvider = "productSaveData", dataProviderClass = ProductRepositoryData.class)
	public <T extends Quantity> void test_save_findOne(Product expectedProduct) {
		prodrepository.save(expectedProduct);
		Product actualProduct = (Product) prodrepository
				.findOne(expectedProduct.getProductId());
		assertThat(actualProduct, samePropertyValuesAs(expectedProduct));
	}

	@Test(enabled = true, dataProvider = "productUpsertData", dataProviderClass = ProductRepositoryData.class)
	public void test_upsert(List<Product> productList, int[] flags,
			List<List<Product>> resultSetList) {
		int i = 0;
		prodrepository.deleteAll();
		for (Product product : productList) {
			prodrepository.upsert(product, 0 == flags[i] ? false : true,
					mongoTemplate);
			List<Product> actualList = prodrepository.findAll();
			assertThat(actualList, hasSize(resultSetList.get(i).size()));
			for (final Product expected : resultSetList.get(i)) {
				assertThat(actualList, hasItem(samePropertyValuesAs(expected)));
			}
			i++;
		}
	}

	@Test(enabled = true, dependsOnMethods = { "test_save_findOne",
			"test_upsert" })
	public void test_findAll() {
		Pageable pageable = new PageRequest(0, 1, new Sort(Direction.ASC,
				MongoDBKeys.ID));
		Page<Product> productsPage = prodrepository.findAll(pageable);
		if (productsPage.hasContent()) {
			assertThat(productsPage.isFirst(), equalTo(true));
			assertThat(productsPage.getContent().size(), equalTo(1));
		}
	}

	@Test(enabled = true, dependsOnMethods = { "test_save_findOne",
			"test_upsert" })
	public void test_findByProductIdRegex() {
		int count = 5;
		for (String productRegEx : new String[] { "S", "su", "sun", "sunflower" }) {
			Pageable pageable = new PageRequest(0, 5, new Sort(Direction.ASC,
					MongoDBKeys.ID));
			Page<Product> productsPage = prodrepository.findByProductIdRegex(
					productRegEx, pageable);
			if (productsPage.hasContent()) {
				assertThat(productsPage.isFirst(), equalTo(true));
				assertThat(productsPage.getSize(), lessThanOrEqualTo(count));
				count = productsPage.getSize();
			}
		}
	}

	@Test(enabled = true, dependsOnMethods = { "test_save_findOne",
			"test_upsert" })
	public void test_searchProducts() {
		int count = 5;
		for (String productRegEx : new String[] { "S", "su", "sun", "sunflower" }) {
			Pageable pageable = new PageRequest(0, 5, new Sort(Direction.ASC,
					MongoDBKeys.ID));
			Page<Product> productsPage = prodrepository.searchProducts(
					productRegEx, pageable);
			if (productsPage.hasContent()) {
				assertThat(productsPage.isFirst(), equalTo(true));
				assertThat(productsPage.getSize(), lessThanOrEqualTo(count));
				count = productsPage.getSize();
			}
		}
	}
}
