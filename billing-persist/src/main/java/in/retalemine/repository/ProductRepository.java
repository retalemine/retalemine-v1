package in.retalemine.repository;

import in.retalemine.entity.Product;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product<?>, String>,
		ProductBaseRepository<Product<?>, String> {

	List<Product<?>> findByProductNameIgnoreCase(String productName, Pageable pageable);
	
	List<Product<?>> findByProductNameRegex(String productName, Pageable pageable);

}
