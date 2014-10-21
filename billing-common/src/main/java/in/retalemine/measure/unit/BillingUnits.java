package in.retalemine.measure.unit;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.measure.quantity.Dimensionless;
import javax.measure.quantity.Quantity;
import javax.measure.unit.SystemOfUnits;
import javax.measure.unit.Unit;
import javax.measure.unit.UnitFormat;

import org.jscience.economics.money.Currency;

public final class BillingUnits extends SystemOfUnits {

	public static final Currency INR = new Currency("INR");

	private static final BillingUnits INSTANCE = new BillingUnits();

	private BillingUnits() {
	}

	public static BillingUnits getInstance() {
		return INSTANCE;
	}

	private static HashSet<Unit<?>> UNITS = new HashSet<Unit<?>>(7);

	public static final Unit<Dimensionless> DOZEN = billingUnits(Unit.ONE
			.times(12));

	public static final Unit<Dimensionless> PIECE = billingUnits(DOZEN
			.divide(12));

	public static final Unit<Dimensionless> PACKET = PIECE.alternate("pkt");

	static {
		UnitFormat.getInstance().label(BillingUnits.DOZEN, "dz");
		UnitFormat.getInstance().label(BillingUnits.PIECE, "pcs");
	}

	@Override
	public Set<Unit<?>> getUnits() {
		return Collections.unmodifiableSet(UNITS);
	}

	private static <U extends Unit<?>> U billingUnits(U unit) {
		UNITS.add(unit);
		return unit;
	}

	public static Unit<? extends Quantity> valueOf(CharSequence csq) {
		return Unit.valueOf(csq);
	}
}
