package in.retalemine.unit;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.measure.quantity.Dimensionless;
import javax.measure.unit.SystemOfUnits;
import javax.measure.unit.Unit;
import javax.measure.unit.UnitFormat;

import org.jscience.economics.money.Currency;

public final class BillingUnits extends SystemOfUnits {

	private static final BillingUnits INSTANCE = new BillingUnits();

	private BillingUnits() {
	}

	public static BillingUnits getInstance() {
		return INSTANCE;
	}

	public static final Currency INR;

	private static HashSet<Unit<?>> UNITS = new HashSet<Unit<?>>(3);

	public static final Unit<Dimensionless> PIECE = billingUnits(Unit.ONE);

	public static final Unit<Dimensionless> DOZEN = billingUnits(PIECE
			.times(12));

	// TODO
	// Has regex limitation 1 kg ~ [Small] size
	// public static final Unit<Dimensionless> SMALL = billingUnits(Unit.ONE);
	// public static final Unit<Dimensionless> MEDIUM = billingUnits(SMALL
	// .times(5));
	// public static final Unit<Dimensionless> BIG =
	// billingUnits(SMALL.times(10));

	// TODO
	// need to check the need for pkt
	// if needed what is the right implementation
	// current implementation is ambiguous as 1-dz to 1-pkt considered as 1pcs
	// public static final Unit<Dimensionless> PACKET = PIECE.alternate("pkt");

	static {
		UnitFormat.getInstance().label(BillingUnits.PIECE, "pcs");
		UnitFormat.getInstance().label(BillingUnits.DOZEN, "dz");
		// UnitFormat.getInstance().label(BillingUnits.SMALL, "small");
		// UnitFormat.getInstance().label(BillingUnits.MEDIUM, "medium");
		// UnitFormat.getInstance().label(BillingUnits.BIG, "big");
		INR = new Currency("INR");
	}

	@Override
	public Set<Unit<?>> getUnits() {
		return Collections.unmodifiableSet(UNITS);
	}

	private static <U extends Unit<?>> U billingUnits(U unit) {
		UNITS.add(unit);
		return unit;
	}

}
