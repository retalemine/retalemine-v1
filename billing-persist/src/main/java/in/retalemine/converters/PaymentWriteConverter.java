package in.retalemine.converters;

import in.retalemine.constants.MongoDBKeys;
import in.retalemine.entity.Payment;

import org.springframework.core.convert.converter.Converter;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class PaymentWriteConverter implements Converter<Payment, DBObject> {

	@Override
	public DBObject convert(Payment source) {
		DBObject dbO = new BasicDBObject();
		dbO.put(MongoDBKeys.BILL_PAYMENT_MODE, source.getPayMode().getValue());
		dbO.put(MongoDBKeys.BILL_PAYMENT_DELAYED, source.getIsDelayedPay());
		dbO.put(MongoDBKeys.BILL_PAYMENT_PAID, source.getIsPaid());
		dbO.put(MongoDBKeys.BILL_PAYMENT_DATE, source.getPaidDate());
		return dbO;
	}
}
