package in.retalemine.measure.quantity;

import in.retalemine.measure.unit.BillingUnits;

import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;

public interface Size {

	interface Small extends Quantity {

		public final static Unit<Size.Small> UNIT = BillingUnits.SMALL;

	}

	interface Medium extends Quantity {

		public final static Unit<Size.Medium> UNIT = BillingUnits.MEDIUM;

	}

	interface Big extends Quantity {

		public final static Unit<Size.Big> UNIT = BillingUnits.BIG;

	}

}