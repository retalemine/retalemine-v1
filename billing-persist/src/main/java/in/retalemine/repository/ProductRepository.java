package in.retalemine.repository;

import in.retalemine.entity.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product, String>,
		ProductBaseRepository<Product, String> {

	Page<Product> findByProductIdRegex(String productIdRegex, Pageable pageable);

}
