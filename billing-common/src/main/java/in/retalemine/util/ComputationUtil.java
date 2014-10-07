package in.retalemine.util;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import in.retalemine.measure.unit.BillingUnits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.measure.Measure;
import javax.measure.converter.ConversionException;
import javax.measure.converter.UnitConverter;
import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;

import org.jscience.economics.money.Money;
import org.jscience.physics.amount.Amount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComputationUtil {

	private static final Logger logger = LoggerFactory
			.getLogger(ComputationUtil.class);

	private static final List<String[]> validUnitsGroup = new ArrayList<String[]>(
			6) {
		private static final long serialVersionUID = 2017339255473656836L;
		{
			add(new String[] { "kg", "g" });
			add(new String[] { "L", "mL" });
			add(new String[] { "m", "ft", "in" });
			add(new String[] { "Small" });
			add(new String[] { "Medium" });
			add(new String[] { "Big" });
		}
	};

	private static final String[] validCommonUnitsGroup = new String[] { "pcs",
			"dz" }; // "pkt"

	private static final Map<String, String> validUnitsMapper = new HashMap<String, String>(
			40) {
		private static final long serialVersionUID = 2572938028394300264L;
		{
			put("kg", "kg");
			put("g", "g");
			put("gram", "g");
			put("l", "L");
			put("lt", "L");
			put("litre", "L");
			put("liter", "L");
			put("ml", "mL");
			put("m", "m");
			put("meter", "m");
			put("ft", "ft");
			put("foot", "ft");
			put("in", "in");
			put("inch", "in");
			put("inches", "in");
			put("pcs", "pcs");
			put("pc", "pcs");
			put("piece", "pcs");
			put("pieces", "pcs");
			// put("pkt", "pkt");
			// put("packet", "pkt");
			put("dz", "dz");
			put("dozen", "dz");
			put("dozens", "dz");
			put("small", "Small");
			put("s", "Small");
			put("medium", "Medium");
			put("med", "Medium");
			put("big", "Big");
			put("b", "Big");
		}
	};

	private static final Map<String, Double> taxPercentMap = new HashMap<String, Double>(
			4) {

		private static final long serialVersionUID = 570223805652706264L;

		{
			put("VAT", 4.0);
			put("Sales Tax", 5.0);
			put("Service Tax", 12.0);
		}
	};

	private ComputationUtil() {
	}

	public static String getValidUnit(String unit) {
		unit = null == unit ? "" : unit.trim().toLowerCase();
		return validUnitsMapper.get(unit);
	}

	public static List<String> getValidUnits() {
		List<String> validUnits = new ArrayList<String>(12);
		for (String[] units : validUnitsGroup) {
			validUnits.addAll(Arrays.asList(units));
		}
		validUnits.addAll(Arrays.asList(validCommonUnitsGroup));
		return validUnits;
	}

	public static List<String> getValidUnits(String unit) {
		List<String> rUnits = null;
		if (null == unit || (unit = unit.trim()).isEmpty()
				|| null == (unit = getValidUnit(unit))) {
			return null;
		}
		for (String[] units : validUnitsGroup) {
			if (Arrays.asList(units).contains(unit)) {
				rUnits = new ArrayList<String>(units.length
						+ validCommonUnitsGroup.length);
				rUnits.addAll(Arrays.asList(units));
				rUnits.addAll(Arrays.asList(validCommonUnitsGroup));
				return rUnits;
			}
		}
		rUnits = Arrays.asList(validCommonUnitsGroup);
		if (rUnits.contains(unit)) {
			return rUnits;
		}
		return null;
	}

	public static Unit<?> getBaseUnit(String unit) {
		List<String> validUnits = getValidUnits(unit);
		if (null == validUnits) {
			return null;
		}
		String units[] = validUnits.toArray(new String[0]);
		return Unit.valueOf(units[0]);
	}

	public static Map<String, Double> getTaxPercentMap() {
		return taxPercentMap;
	}

	@SuppressWarnings("unchecked")
	public static <U extends Quantity, V extends Quantity, W extends Quantity> Measure<Double, ? extends Quantity> computeNetQuantity(
			Measure<Double, U> productUnit, Measure<Double, V> quantity1,
			Measure<Double, W> quantity2) {

		assertThat(quantity1, notNullValue());
		assertThat(quantity2, notNullValue());

		if (quantity1.getUnit().equals(quantity2.getUnit())) {
			logger.debug("equal units");
			return Measure.valueOf(quantity1.getValue() + quantity2.getValue(),
					quantity1.getUnit());
		} else {
			logger.debug("not equal units");
			assertThat(productUnit, notNullValue());
			String units[] = getValidUnits(productUnit.getUnit().toString())
					.toArray(new String[0]);
			return computeNetQuantity(productUnit,
					quantity1.to((Unit<V>) Unit.valueOf(units[0])),
					quantity2.to((Unit<W>) Unit.valueOf(units[0])));
		}
	}

	public static <U extends Quantity, V extends Quantity> Amount<Money> computeAmount(
			Measure<Double, U> unitQuantity, Amount<Money> unitRate,
			Measure<Double, V> netQuantity) {
		if (unitQuantity.getUnit().isCompatible(netQuantity.getUnit())) {
			UnitConverter toNetQuantityUnit = unitQuantity.getUnit()
					.getConverterTo(netQuantity.getUnit());
			return unitRate.times(netQuantity.getValue()
					/ toNetQuantityUnit.convert(unitQuantity.getValue()));
		} else {
			if (BillingUnits.PIECE.equals(netQuantity.getUnit())) {
				return unitRate.times(netQuantity.getValue());
			}
			// else if
			// (RetaSI.PACKET.equals(netQuantity.getUnit())) {
			// return unitPrice.times(
			// netQuantity.getValue());
			// }
			else if (BillingUnits.DOZEN.equals(netQuantity.getUnit())) {
				return unitRate.times(netQuantity.getValue() * 12);
			} else {
				throw new ConversionException(String.format(
						"Failed conversion : %s -> %s", unitQuantity.getUnit(),
						netQuantity.getUnit()));
			}
		}
	}
}
