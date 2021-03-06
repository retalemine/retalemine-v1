package in.retalemine.converters;

import in.retalemine.constants.MongoDBKeys;
import in.retalemine.util.ComputationUtil;

import org.jscience.physics.amount.Amount;
import org.springframework.core.convert.converter.Converter;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class AmountWriteConverter implements Converter<Amount<?>, DBObject> {

	@Override
	public DBObject convert(Amount<?> source) {
		DBObject dbO = new BasicDBObject();
		dbO.put(MongoDBKeys.VALUE,
				ComputationUtil.computeClearAmount(source.toText()));
		dbO.put(MongoDBKeys.UNIT, source.getUnit().toString());
		return dbO;
	}

}
