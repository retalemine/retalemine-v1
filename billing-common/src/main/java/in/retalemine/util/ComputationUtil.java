package in.retalemine.util;

import static org.hamcrest.CoreMatchers.equalTo;
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
			7) {
		private static final long serialVersionUID = 2017339255473656836L;
		{
			add(new String[] { "kg", "g" });
			add(new String[] { "L", "mL" });
			add(new String[] { "m" });
			add(new String[] { "ft" });
			add(new String[] { "in" });
			add(new String[] { "dz", "pcs" });
			add(new String[] { "dz", "pkt" });
		}
	};

	private static final Map<String, String> validUnitsMapper = new HashMap<String, String>(
			32) {
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
			put("pkt", "pkt");
			put("packet", "pkt");
			put("dz", "dz");
			put("dozen", "dz");
			put("dozens", "dz");
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
		List<String> validUnits = new ArrayList<String>(10);
		for (String[] units : validUnitsGroup) {
			for (String unit : units) {
				if (!validUnits.contains(unit)) {
					validUnits.add(unit);
				}
			}
		}
		return validUnits;
	}

	public static List<String> getValidUnits(String unit) {
		List<String> rUnits = null;
		if (null == unit || (unit = unit.trim()).isEmpty()
				|| null == (unit = getValidUnit(unit))) {
			return null;
		}
		for (String[] units : validUnitsGroup) {
			rUnits = Arrays.asList(units);
			if (rUnits.contains(unit)) {
				return rUnits;
			}
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

	public static Boolean isBaseUnit(String unit) {
		List<String> validUnits = getValidUnits(unit);
		if (null == validUnits) {
			return false;
		}
		String units[] = validUnits.toArray(new String[0]);
		return units[0].equals(unit);
	}

	public static Map<String, Double> getTaxPercentMap() {
		return taxPercentMap;
	}

	@SuppressWarnings("unchecked")
	public static <X extends Quantity, Y extends Quantity> Measure<Double, ? extends Quantity> computeNetQuantity(
			Measure<Double, X> quantityX, Measure<Double, Y> quantityY) {

		assertThat(quantityX, notNullValue());
		assertThat(quantityY, notNullValue());
		assertThat(quantityX.getUnit().isCompatible(quantityY.getUnit()),
				equalTo(true));

		if (quantityX.getUnit().equals(quantityY.getUnit())
				&& ComputationUtil.isBaseUnit(quantityX.getUnit().toString())) {
			logger.debug("X,Y Same and Standard");
			return Measure.valueOf(quantityX.getValue() + quantityY.getValue(),
					quantityX.getUnit());
		} else if (quantityX.getUnit().equals(quantityY.getUnit())) {
			logger.debug("X,Y Same but not Standard");
			Measure<Double, X> result = Measure.valueOf(
					quantityX.getValue() + quantityY.getValue(),
					quantityX.getUnit()).to(
					(Unit<X>) getBaseUnit(quantityX.getUnit().toString()));
			if (result.getValue() < 1) {
				return Measure.valueOf(
						quantityX.getValue() + quantityY.getValue(),
						quantityX.getUnit());
			}
			return result;
		} else {
			logger.debug("X,Y different and either one non Standard "
					+ quantityX + "  " + quantityY);
			String unit = quantityX.getUnit().toString();
			if (!isBaseUnit(unit)) {
				quantityX = quantityX.to((Unit<X>) getBaseUnit(unit));
			}
			unit = quantityY.getUnit().toString();
			if (!isBaseUnit(unit)) {
				quantityY = quantityY.to((Unit<Y>) getBaseUnit(unit));
			}
			return computeNetQuantity(quantityX, quantityY);
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
