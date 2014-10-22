package in.retalemine.repository;

import java.io.Serializable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.repository.NoRepositoryBean;

import com.mongodb.WriteResult;

@NoRepositoryBean
public interface ProductBaseRepository<T, ID extends Serializable> {

	WriteResult upsert(T entity, Boolean resetRate);

	WriteResult updateFirst(Query query, Update update, Class<?> entityClass);

	Page<?> findProductsByName(Query query, Pageable pageable,
			Class<T> entityClass);

}
