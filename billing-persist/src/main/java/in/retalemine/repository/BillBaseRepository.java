package in.retalemine.repository;

import java.io.Serializable;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BillBaseRepository<T, ID extends Serializable> {

	void insert(T entity);

	T findOne(Query query, Class<T> entityClass);
}
