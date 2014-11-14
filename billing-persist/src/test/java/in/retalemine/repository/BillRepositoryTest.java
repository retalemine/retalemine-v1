package in.retalemine.repository;

import in.retalemine.data.BillRepositoryData;
import in.retalemine.entity.Bill;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "classpath:spring-mongo-config.xml",
		"classpath:test-spring-mongo-config.xml" })
public class BillRepositoryTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private BillRepository billrepository;
	@Autowired
	private MongoConverter mongoConverter;
	@Autowired
	private MongoTemplate mongoTemplate;

	@BeforeClass
	public void setup() {
		billrepository.deleteAll();
	}

	@Test(enabled = true, dataProvider = "billInsertSaveData", dataProviderClass = BillRepositoryData.class)
	public void test_insert_save(Bill bill) {
		billrepository.insert(bill, mongoTemplate);
		billrepository.save(bill);
		mongoConverter.convertToMongoType(bill);
	}

	@Test(enabled = true, expectedExceptions = DuplicateKeyException.class, dependsOnMethods = { "test_insert_save" })
	public void test_findOne_insertDuplicate() {
		Bill fetchBill = billrepository.findOne(new Query(), Bill.class);
		Assert.assertNotNull(fetchBill, "Sales Collection is empty");
		billrepository.insert(fetchBill, mongoTemplate);
	}

}
