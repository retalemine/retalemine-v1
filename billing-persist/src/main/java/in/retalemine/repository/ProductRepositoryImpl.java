package in.retalemine.repository;

import in.retalemine.constants.MongoDBKeys;
import in.retalemine.entity.Product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.UncategorizedMongoDbException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.WriteResult;

public class ProductRepositoryImpl implements
		ProductBaseRepository<Product, String> {

	@Autowired
	MongoTemplate mongoTemplate;

	@Override
	public WriteResult upsert(Product entity, Boolean resetRate) {
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

	@Override
	public Page<Product> searchProducts(String searchText, Pageable pageable) {
		Query query = null;
		Long count = 0l;
		List<Product> list = null;
		try {
			query = TextQuery
					.queryText(new TextCriteria().matching(searchText));
			count = mongoTemplate.count(query, Product.class);
			list = mongoTemplate.find(query.with(pageable), Product.class);
		} catch (UncategorizedMongoDbException e) {
			query = new Query().addCriteria(Criteria.where(MongoDBKeys.ID)
					.regex(searchText, "i"));
			count = mongoTemplate.count(query, Product.class);
			list = mongoTemplate.find(query.with(pageable), Product.class);
		}
		return new PageImpl<Product>(list, pageable, count);
	}
}
