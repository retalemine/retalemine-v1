package in.retalemine.repository;

import in.retalemine.constants.MongoDBKeys;
import in.retalemine.entity.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.WriteResult;

public class ProductRepositoryImpl implements
		ProductBaseRepository<Product<?>, String> {

	@Autowired
	MongoTemplate mongoTemplate;

	@Override
	public WriteResult upsert(Product<?> entity, Boolean resetRate) {
		Query query = new Query();
		Update update = new Update();

		query.addCriteria(Criteria.where(MongoDBKeys.ID).is(
				entity.getProductId()));

		update.set(MongoDBKeys.PRODUCT_NAME, entity.getProductName());
		update.set(MongoDBKeys.PRODUCT_UNIT + "." + MongoDBKeys.VALUE, entity
				.getProductUnit().getValue());
		update.set(MongoDBKeys.PRODUCT_UNIT + "." + MongoDBKeys.UNIT, entity
				.getProductUnit().getUnit().toString());
		if (!resetRate) {
			update.addToSet(MongoDBKeys.PRODUCT_PRICES, entity.getUnitPrice());
		} else {
			update.set(MongoDBKeys.PRODUCT_PRICES, entity.getUnitPrices());
		}
		update.set(MongoDBKeys.PRODUCT_CREATION_OR_MODIFIED_DATE,
				entity.getCreatedOrModifiedDate());

		return mongoTemplate.upsert(query, update, entity.getClass());
	}

}
