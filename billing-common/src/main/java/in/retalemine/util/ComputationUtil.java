package in.retalemine.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import in.retalemine.constants.BillingConstants;
import in.retalemine.measure.unit.BillingUnits;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.measure.Measure;
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
			35) {
		private static final long serialVersionUID = 2572938028394300264L;
		{
			put("kg", "kg");
			put("kilogram", "kg");
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

	private static final List<String> validUnitDescList = new ArrayList<String>(
			4) {
		private static final long serialVersionUID = 6320100716299157709L;

		{
			add("bottle");
			add("btl");
			add("packet");
			add("pkt");
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

	public static List<String> getValidUnitsGroup(String unit) {
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
		List<String> validUnits = getValidUnitsGroup(unit);
		if (null == validUnits) {
			return null;
		}
		String units[] = validUnits.toArray(new String[0]);
		return BillingUnits.valueOf(units[0]);
	}

	public static Boolean isBaseUnit(String unit) {
		List<String> validUnits = getValidUnitsGroup(unit);
		if (null == validUnits) {
			return false;
		}
		String units[] = validUnits.toArray(new String[0]);
		return units[0].equals(unit);
	}

	public static List<String> getValidUnitDescriptionList() {
		return validUnitDescList;
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
			DecimalFormat formatter = new DecimalFormat(
					BillingConstants.MEASURE_VALUE_FORMAT);
			if (result.getValue() < 1
					|| !formatter.format(result.getValue()).equals(
							result.getValue().toString())) {
				return Measure.valueOf(
						quantityX.getValue() + quantityY.getValue(),
						quantityX.getUnit());
			}
			return result;
		} else {
			logger.debug("X,Y different and either one non Standard ");
			Measure<Double, ? extends Quantity> result;
			DecimalFormat formatter = new DecimalFormat(
					BillingConstants.MEASURE_VALUE_FORMAT);
			String unitX = quantityX.getUnit().toString();
			if (!isBaseUnit(unitX)) {
				result = quantityX.to((Unit<X>) getBaseUnit(unitX));
				if (formatter.format(result.getValue()).equals(
						result.getValue().toString())) {
					quantityX = (Measure<Double, X>) result;
					unitX = quantityX.getUnit().toString();
				}
			}
			String unitY = quantityY.getUnit().toString();
			if (!unitY.equals(unitX)) {
				result = quantityY.to((Unit<Y>) BillingUnits.valueOf(unitX));
				if (formatter.format(result.getValue()).equals(
						result.getValue().toString())) {
					quantityY = (Measure<Double, Y>) result;
				} else {
					quantityX = quantityX.to((Unit<X>) BillingUnits
							.valueOf(unitY));
				}
			}
			return Measure.valueOf(quantityX.getValue() + quantityY.getValue(),
					quantityX.getUnit());
		}
	}

	@SuppressWarnings("unchecked")
	public static <U extends Quantity, V extends Quantity> Amount<Money> computeAmount(
			Measure<Double, U> unitQuantity, Amount<Money> unitRate,
			Measure<Double, V> netQuantity) {
		return unitRate.times(netQuantity.to((Unit<V>) unitQuantity.getUnit())
				.getValue() / unitQuantity.getValue());
	}
}
