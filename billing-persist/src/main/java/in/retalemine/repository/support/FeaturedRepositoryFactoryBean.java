package in.retalemine.repository.support;

import in.retalemine.repository.FeaturedRepository;
import in.retalemine.repository.FeaturedRepositoryImpl;

import java.io.Serializable;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.BasicMongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.MappingMongoEntityInformation;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;

public class FeaturedRepositoryFactoryBean<R extends MongoRepository<T, ID>, T, ID extends Serializable>
		extends MongoRepositoryFactoryBean<R, T, ID> {

	@Override
	protected RepositoryFactorySupport getFactoryInstance(
			MongoOperations operations) {
		return new FeaturedRepositoryFactory<T, ID>(operations);
	}

	private static class FeaturedRepositoryFactory<T, ID extends Serializable>
			extends MongoRepositoryFactory {
		private final MongoOperations mongoOperations;

		public FeaturedRepositoryFactory(MongoOperations mongoOperations) {
			super(mongoOperations);
			this.mongoOperations = mongoOperations;
		}

		@SuppressWarnings("unchecked")
		protected Object getTargetRepository(RepositoryMetadata metadata) {
			TypeInformation<T> information = ClassTypeInformation
					.from((Class<T>) metadata.getDomainType());
			MongoPersistentEntity<T> pe = new BasicMongoPersistentEntity<T>(
					information);
			MongoEntityInformation<T, ID> mongometa = new MappingMongoEntityInformation<T, ID>(
					pe);

			return new FeaturedRepositoryImpl<T, ID>(mongometa, mongoOperations);
		}

		protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
			return FeaturedRepository.class;
		}
	}
}
