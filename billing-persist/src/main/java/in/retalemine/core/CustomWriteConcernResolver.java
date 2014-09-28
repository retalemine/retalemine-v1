package in.retalemine.core;

import org.springframework.data.mongodb.core.MongoAction;
import org.springframework.data.mongodb.core.WriteConcernResolver;

import com.mongodb.WriteConcern;

public class CustomWriteConcernResolver implements WriteConcernResolver {

	@Override
	public WriteConcern resolve(MongoAction action) {
		if (null != action.getEntityType()) {
			if (action.getEntityType().getSimpleName().contains("Bill")) {
				return WriteConcern.ACKNOWLEDGED;
			}
		}
		return action.getDefaultWriteConcern();
	}

}
