package in.retalemine.repository;

import in.retalemine.entity.Product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.WriteResult;

public class ProductRepositoryImpl<T, ID extends Serializable> implements
		ProductBaseRepository<T, ID> {

	@Autowired
	MongoTemplate mongoTemplate;

	@Override
	public WriteResult upsert(Query query, Update update, Class<T> entityClass) {
		return mongoTemplate.upsert(query, update, entityClass);
	}

	@Override
	public WriteResult updateFirst(Query query, Update update,
			Class<?> entityClass) {
		return mongoTemplate.updateFirst(query, update, entityClass);
	}

	@Override
	public Page<?> findProductsByName(Query query, Pageable pageable,
			Class<T> entityClass) {
		List<String> result;
		Long count = mongoTemplate.count(new Query(), entityClass);
		List<T> list = mongoTemplate.find(new Query().with(pageable),
				entityClass);
		if (entityClass.isInstance(Product.class)) {
			result = new ArrayList<String>(pageable.getPageSize());
			for (T value : list) {
				Product<?> product = (Product<?>) value;
				result.add(product.getProductDescription());
			}
			return new PageImpl<String>(result, pageable, count);
		}
		return new PageImpl<T>(list, pageable, count);
	}
}
