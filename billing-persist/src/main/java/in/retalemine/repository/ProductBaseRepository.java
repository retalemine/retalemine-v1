package in.retalemine.repository;

import java.io.Serializable;

import org.springframework.data.repository.NoRepositoryBean;

import com.mongodb.WriteResult;

@NoRepositoryBean
public interface ProductBaseRepository<T, ID extends Serializable> {

	WriteResult upsert(T entity, Boolean resetRate);

}
