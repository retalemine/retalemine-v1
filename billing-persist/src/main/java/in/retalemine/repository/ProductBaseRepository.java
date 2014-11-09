package in.retalemine.repository;

import in.retalemine.entity.Product;

import java.io.Serializable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

import com.mongodb.WriteResult;

@NoRepositoryBean
public interface ProductBaseRepository<T, ID extends Serializable> {

	WriteResult upsert(T entity, Boolean resetRate);

	/**
	 * Search text should be words like 'Sugar' and won't look up words
	 * formation like 'suga'
	 * 
	 * @param searchText
	 * @param pageable
	 * @return Page
	 */
	Page<Product> searchProducts(String searchText, Pageable pageable);
}
