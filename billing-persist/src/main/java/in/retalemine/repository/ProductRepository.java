package in.retalemine.repository;

import in.retalemine.entity.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product, String>,
		ProductBaseRepository<Product, String> {

	@Query(value = "{'_id': {$regex : ?0, $options: 'i'}}")
	Page<Product> findByProductIdRegex(String productIdRegex, Pageable pageable);

}
