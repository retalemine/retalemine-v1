package in.retalemine.measure.unit;

import in.retalemine.measure.quantity.Count;
import in.retalemine.measure.quantity.Size;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.measure.unit.BaseUnit;
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

	public static final BaseUnit<Count> PIECE = billingUnits(new BaseUnit<Count>(
			"pcs"));

	public static final Unit<Count> DOZEN = billingUnits(PIECE.times(12));

	public static final Unit<Size.Small> SMALL = billingUnits(new BaseUnit<Size.Small>(
			"Small"));

	public static final Unit<Size.Medium> MEDIUM = billingUnits(new BaseUnit<Size.Medium>(
			"Medium"));

	public static final Unit<Size.Big> BIG = billingUnits(new BaseUnit<Size.Big>(
			"Big"));

	// TODO
	// need to check the need for pkt
	// if needed what is the right implementation
	// current implementation is ambiguous as 1-dz to 1-pkt considered as 1pcs
	// public static final Unit<Dimensionless> PACKET = PIECE.alternate("pkt");

	static {
		UnitFormat.getInstance().label(BillingUnits.DOZEN, "dz");
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
