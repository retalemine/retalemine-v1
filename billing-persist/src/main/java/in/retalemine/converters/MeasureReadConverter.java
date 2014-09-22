package in.retalemine.converters;

import in.retalemine.constants.MongoDBKeys;

import javax.measure.Measure;
import javax.measure.unit.Unit;

import org.springframework.core.convert.converter.Converter;

import com.mongodb.DBObject;

public class MeasureReadConverter implements Converter<DBObject, Measure<?, ?>> {

	public Measure<?, ?> convert(DBObject source) {
		Double value = (Double) source.get(MongoDBKeys.VALUE);
		Unit<?> unit = Unit
				.valueOf((CharSequence) source.get(MongoDBKeys.UNIT));
		return Measure.valueOf(value, unit);
	}

}
