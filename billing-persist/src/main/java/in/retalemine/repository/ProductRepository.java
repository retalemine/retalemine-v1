package in.retalemine.repository;

import in.retalemine.entity.Product;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product<?>, String> {

	public Product<?> findByProductName(String productName);

}
