package in.retalemine.repository;

import java.io.Serializable;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;

public class FeaturedRepositoryImpl<T, ID extends Serializable> extends
		SimpleMongoRepository<T, ID> implements FeaturedRepository<T, ID> {

	public FeaturedRepositoryImpl(MongoEntityInformation<T, ID> metadata,
			MongoOperations mongoOperations) {
		super(metadata, mongoOperations);
	}

	@Override
	public void insert(T entity) {
		this.getMongoOperations().insert(entity);
	}

	@Override
	public T findOne(Query query, Class<T> entityClass) {
		return this.getMongoOperations().findOne(query, entityClass);
	}

}
