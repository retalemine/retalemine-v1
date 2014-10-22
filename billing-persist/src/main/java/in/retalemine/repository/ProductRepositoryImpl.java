package in.retalemine.repository;

import in.retalemine.constants.BillingConstants;
import in.retalemine.constants.MongoDBKeys;
import in.retalemine.entity.Product;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
		Query query = new Query(Criteria.where(MongoDBKeys.PRODUCT_NAME)
				.is(entity.getProductName())
				.and(MongoDBKeys.PRODUCT_UNIT + "." + MongoDBKeys.VALUE)
				.is(entity.getProductUnit().getValue())
				.and(MongoDBKeys.PRODUCT_UNIT + "." + MongoDBKeys.UNIT)
				.is(entity.getProductUnit().getUnit().toString()));
		Update update = null;
		if (!resetRate) {
			update = new Update().addToSet(MongoDBKeys.PRODUCT_PRICES,
					entity.getUnitPrice()).set(
					MongoDBKeys.PRODUCT_CREATION_OR_MODIFIED_DATE,
					entity.getCreatedOrModifiedDate());
		} else {
			update = new Update().set(MongoDBKeys.PRODUCT_PRICES,
					entity.getUnitPrices()).set(
					MongoDBKeys.PRODUCT_CREATION_OR_MODIFIED_DATE,
					entity.getCreatedOrModifiedDate());
		}
		return mongoTemplate.upsert(query, update, entity.getClass());
	}

	@Override
	public WriteResult updateFirst(Query query, Update update,
			Class<?> entityClass) {
		return mongoTemplate.updateFirst(query, update, entityClass);
	}

	@Override
	public Page<?> findProductsByName(Query query, Pageable pageable,
			Class<Product<?>> entityClass) {
		List<String> result;
		Long count = mongoTemplate.count(new Query(), entityClass);
		List<Product<?>> list = mongoTemplate.find(new Query().with(pageable),
				entityClass);
		if (entityClass.isInstance(Product.class)) {
			result = new ArrayList<String>(pageable.getPageSize());
			for (Product<?> value : list) {
				Product<?> product = (Product<?>) value;
				result.add(product.getProductName()
						+ BillingConstants.PRODUCT_DESC_DIVIDER
						+ product.getProductUnit());
			}
			return new PageImpl<String>(result, pageable, count);
		}
		return new PageImpl<Product<?>>(list, pageable, count);
	}

}
