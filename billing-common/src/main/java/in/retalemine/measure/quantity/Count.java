package in.retalemine.measure.quantity;

import in.retalemine.measure.unit.BillingUnits;

import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;

public interface Count extends Quantity {

	public final static Unit<Count> UNIT = BillingUnits.PIECE;

}