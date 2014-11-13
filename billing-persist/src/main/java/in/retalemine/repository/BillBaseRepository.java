package in.retalemine.repository;

import java.io.Serializable;

import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BillBaseRepository<T, ID extends Serializable> {

	T insert(T entity);

}
