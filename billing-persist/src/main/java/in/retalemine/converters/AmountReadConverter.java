package in.retalemine.converters;

import in.retalemine.constants.MongoDBKeys;
import javax.measure.unit.Unit;
import org.jscience.physics.amount.Amount;
import org.springframework.core.convert.converter.Converter;
import com.mongodb.DBObject;

public class AmountReadConverter implements Converter<DBObject, Amount<?>> {

	@Override
	public Amount<?> convert(DBObject source) {
		Double value = (Double) source.get(MongoDBKeys.VALUE);
		Unit<?> unit = Unit
				.valueOf((CharSequence) source.get(MongoDBKeys.UNIT));
		return Amount.valueOf(value, unit);
	}
}
