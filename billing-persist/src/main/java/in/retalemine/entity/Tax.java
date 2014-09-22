package in.retalemine.entity;

import in.retalemine.constants.MongoDBKeys;

import org.springframework.data.mongodb.core.mapping.Field;

public class Tax {

	@Field(MongoDBKeys.TAX_TYPE)
	private TaxType taxType;
	@Field(MongoDBKeys.TAX_PERCENTAGE)
	private Double taxPercent;

	public Tax(TaxType taxType, Double taxPercent) {
		this.taxType = taxType;
		this.taxPercent = taxPercent;
	}

}
