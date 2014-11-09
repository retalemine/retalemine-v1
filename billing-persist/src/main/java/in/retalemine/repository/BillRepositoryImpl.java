package in.retalemine.repository;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

public class BillRepositoryImpl<T, ID extends Serializable> implements
		BillBaseRepository<T, ID> {

	@Autowired
	MongoTemplate mongoTemplate;

	@Override
	public void insert(T entity) {
		mongoTemplate.insert(entity);
	}

}
