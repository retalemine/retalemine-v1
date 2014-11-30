package in.retalemine.util;

import in.retalemine.entity.Bill;
import in.retalemine.entity.BillItem;
import in.retalemine.entity.Customer;
import in.retalemine.entity.Payment;
import in.retalemine.entity.Product;
import in.retalemine.entity.Tax;
import in.retalemine.entity.TaxType;
import in.retalemine.view.VO.BillItemVO;
import in.retalemine.view.VO.BillVO;
import in.retalemine.view.VO.CustomerVO;
import in.retalemine.view.VO.PaymentMode;
import in.retalemine.view.VO.ProductVO;
import in.retalemine.view.VO.TaxVO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.measure.quantity.Quantity;

import org.jscience.economics.money.Money;
import org.jscience.physics.amount.Amount;
import org.springframework.data.domain.Page;

public class VOConverterUtil {

	public VOConverterUtil() {
		// TODO Auto-generated constructor stub
	}

	public static Bill constructBillObject(BillVO billVO) {
		return new Bill(billVO.getBillDate(),
				constructBillItems(billVO.getBillItems()),
				billVO.getSubTotal(), constructTax(billVO.getTaxes()),
				billVO.getTotal(), constructPayment(billVO.getPayMode(),
						billVO.getIsDelayedPay(), billVO.getIsPaid(),
						billVO.getPaidDate()),
				constructCustomerObj(billVO.getCustomerInfo()),
				billVO.getIsDoorDelivery());
	}

	private static Payment constructPayment(PaymentMode paymentMode,
			Boolean isDelayedPay, Boolean isPaid, Date paidDate) {
		return new Payment(
				in.retalemine.entity.PaymentMode.getPaymentMode(paymentMode
						.getValue()), isDelayedPay, isPaid, paidDate);
	}

	private static Customer constructCustomerObj(CustomerVO customerInfo) {
		if (null == customerInfo) {
			return null;
		}
		return new Customer(customerInfo.getCustomerName(),
				customerInfo.getContactNo(), customerInfo.getAddress());
	}

	private static List<Tax> constructTax(List<TaxVO> taxes) {
		if (null == taxes || taxes.size() == 0) {
			return null;
		}
		List<Tax> taxesObj = new ArrayList<Tax>(taxes.size());
		for (TaxVO taxVO : taxes) {
			taxesObj.add(new Tax(TaxType.getTaxType(taxVO.getTaxType()), taxVO
					.getTaxPercent()));
		}
		return taxesObj;
	}

	private static List<BillItem> constructBillItems(
			List<BillItemVO<? extends Quantity, ? extends Quantity>> billItems) {
		if (null == billItems || billItems.size() == 0) {
			return null;
		}
		List<BillItem> billItemsObj = new ArrayList<BillItem>(billItems.size());
		for (BillItemVO<? extends Quantity, ? extends Quantity> billItemVO : billItems) {
			billItemsObj.add(new BillItem(billItemVO.getProductName(),
					billItemVO.getProductUnit(), billItemVO
							.getProductDescription(), billItemVO.getUnitRate(),
					billItemVO.getNetQuantity(), billItemVO.getAmount()));
		}
		return billItemsObj;
	}

	public static Product constructProductObject(
			ProductVO<? extends Quantity> productVO, Amount<Money> newUnitRate) {
		if (null != newUnitRate) {
			return new Product(productVO.getProductName(),
					productVO.getProductUnit(), newUnitRate, new Date());
		} else {
			return new Product(productVO.getProductName(),
					productVO.getProductUnit(), productVO.getUnitRates(),
					new Date());
		}
	}

	public static List<ProductVO<? extends Quantity>> constructProductVOObjects(
			Page<Product> productPage) {
		List<ProductVO<? extends Quantity>> products = new ArrayList<ProductVO<? extends Quantity>>(
				productPage.getSize());
		for (Product product : productPage.getContent()) {
			products.add(ProductVO.valueOf(product.getProductName(),
					product.getProductUnit(), product.getUnitPrices()));
		}
		return products;
	}
}
