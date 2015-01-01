package in.retalemine.util;

import in.retalemine.measure.unit.BillingUnits;
import in.retalemine.view.VO.BillItemVO;
import in.retalemine.view.VO.TaxVO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SimpleTimeZone;

import javax.measure.quantity.Quantity;

import org.jscience.economics.money.Money;
import org.jscience.physics.amount.Amount;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.server.Page;
import com.vaadin.server.WebBrowser;

public class BillingComputationUtil {

	public static Set<TaxVO> getBillingTaxSet() {
		Set<TaxVO> taxSet = new HashSet<TaxVO>();
		for (Map.Entry<String, Double> tax : ComputationUtil.getTaxPercentMap()
				.entrySet()) {
			taxSet.add(new TaxVO(tax.getKey(), tax.getValue()));
		}
		return taxSet;
	}

	@SuppressWarnings("unchecked")
	public static <U> Container getContainerSource(Collection<U> options,
			Container container) {
		if (options != null) {
			if (container instanceof BeanContainer) {
				for (final Iterator<U> i = options.iterator(); i.hasNext();) {
					((BeanContainer<?, U>) container).addBean(i.next());
				}
			} else {
				for (final Iterator<U> i = options.iterator(); i.hasNext();) {
					container.addItem(i.next());
				}
			}

		}
		return container;
	}

	public static Amount<Money> computeSubAmount(
			List<BillItemVO<? extends Quantity, ? extends Quantity>> billItemIds) {
		Amount<Money> subAmount = Amount.valueOf(0.0, BillingUnits.INR);
		for (BillItemVO<? extends Quantity, ? extends Quantity> billItemVO : billItemIds) {
			subAmount = subAmount.plus(billItemVO.getAmount());
		}
		return subAmount;
	}

	public static Amount<Money> computeTotalAmount(Amount<Money> subTotal,
			List<TaxVO> taxVOList) {
		double taxPercent = 0.0;
		for (TaxVO taxVO : taxVOList) {
			taxPercent += taxVO.getTaxPercent();
		}
		return subTotal.times((100 + taxPercent) / 100);
	}

	public static String getClientDateString(DateFormat dateTimeFormat) {
		// TODO Does synchronization needed?
		// SHORT,MEDIUM,LONG,FULL
		// 7/9/14 12:37 PM
		// 7 Sep, 2014 12:37:23 PM
		// 7 September, 2014 12:37:23 PM GMT+05:30
		// Sunday, 7 September, 2014 12:37:23 PM GMT+05:30
		WebBrowser webBrowser = Page.getCurrent().getWebBrowser();
		SimpleTimeZone clientTimeZone = new SimpleTimeZone(
				webBrowser.getTimezoneOffset(), "client timezone");
		dateTimeFormat.setTimeZone(clientTimeZone);
		return dateTimeFormat.format(webBrowser.getCurrentDate());
	}

	public static String getDateTimeFormat(Date date, String format) {
		return new SimpleDateFormat(format).format(date);
	}

	public static String getClientDateTimeFormat(Date date,
			String dateTimeFormat) {
		WebBrowser webBrowser = Page.getCurrent().getWebBrowser();
		SimpleTimeZone clientTimeZone = new SimpleTimeZone(
				webBrowser.getTimezoneOffset(), "client timezone");
		DateFormat format = new SimpleDateFormat(dateTimeFormat);
		format.setTimeZone(clientTimeZone);
		return format.format(date);
	}
}
