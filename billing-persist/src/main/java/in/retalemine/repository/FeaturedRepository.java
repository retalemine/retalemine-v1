package in.retalemine.repository;

import java.io.Serializable;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface FeaturedRepository<T, ID extends Serializable> extends
		MongoRepository<T, ID> {

	T findOne(Query query, Class<T> entityClass);

}
