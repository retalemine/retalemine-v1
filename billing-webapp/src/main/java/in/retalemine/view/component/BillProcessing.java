package in.retalemine.view.component;

import in.retalemine.entity.Bill;
import in.retalemine.measure.unit.BillingUnits;
import in.retalemine.repository.BillRepository;
import in.retalemine.util.ApplicationContextProvider;
import in.retalemine.util.BillingComputationUtil;
import in.retalemine.util.ComputationUtil;
import in.retalemine.util.VOConverterUtil;
import in.retalemine.view.UI.PrintBillUI;
import in.retalemine.view.VO.BillVO;
import in.retalemine.view.VO.PaymentMode;
import in.retalemine.view.VO.TaxVO;
import in.retalemine.view.constants.BillingConstants;
import in.retalemine.view.converter.AmountConverter;
import in.retalemine.view.event.BillItemChangeEvent;
import in.retalemine.view.event.DeliveryModeSelectionEvent;
import in.retalemine.view.event.PaymentModeSelectionEvent;
import in.retalemine.view.event.ResetBillingEvent;
import in.retalemine.view.event.TaxSelectionEvent;

import java.util.Date;

import org.jscience.economics.money.Money;
import org.jscience.physics.amount.Amount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.vaadin.data.Property;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

public class BillProcessing extends HorizontalLayout {
	private static final long serialVersionUID = 375609907461568097L;
	private static final Logger logger = LoggerFactory
			.getLogger(BillProcessing.class);

	private BillVO billVO = new BillVO();
	private TaxVO taxVO = null;
	private Button resetBill;
	private Button draftBill;
	private Button saveBill;

	private BillRepository billRepository;
	private MongoTemplate mongoTemplate;

	public BillProcessing(final EventBus eventBus,
			final PropertysetItem propertysetItem) {
		logger.info("Initializing {}", getClass().getSimpleName());

		setWidth("100%");
		setSpacing(true);
		setMargin(false);
		setImmediate(false);

		resetBill = new Button();
		resetBill.setCaption(BillingConstants.RESET);
		resetBill.setWidth("100%");
		resetBill.setEnabled(false);
		resetBill.setImmediate(true);
		resetBill.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 9118398197062156254L;

			@Override
			public void buttonClick(ClickEvent event) {
				logger.info("{} posts ResetBillingEvent", getClass()
						.getSimpleName());
				eventBus.post(new ResetBillingEvent());
			}
		});

		draftBill = new Button();
		draftBill.setCaption(BillingConstants.DRAFT);
		draftBill.setWidth("100%");
		draftBill.setEnabled(false);
		draftBill.setImmediate(true);
		draftBill.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1537017795817327088L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO save the bill as draft and don't print it
				// TODO do provision to save the draft and make a print also
				// TODO what will be the payment mode defaulted to?
			}
		});

		saveBill = new Button();
		saveBill.setCaption(BillingConstants.SAVE);
		saveBill.setWidth("100%");
		saveBill.setEnabled(false);
		saveBill.setImmediate(true);
		saveBill.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 8093769158458975223L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO validate mandatory fields
				// TODO should the billVO be reinitialized
				billVO.setSubTotal(BillingComputationUtil
						.computeSubAmount(billVO.getBillItems()));
				billVO.getTaxes().clear();
				if (null != taxVO) {
					billVO.getTaxes().add(taxVO);
				}
				billVO.setTotal(BillingComputationUtil.computeTotalAmount(
						billVO.getSubTotal(), billVO.getTaxes()));
				billVO.setCustomerInfo(CustomerForm
						.buildCustomerVO(propertysetItem));
				if (billVO.getIsDoorDelivery()
						&& (null == billVO.getCustomerInfo() || ComputationUtil
								.isEmpty(billVO.getCustomerInfo().getAddress()))) {
					Notification.show(
							"Customer Details Needed For Home Delivery",
							"Customer Name and Address is mandatory",
							Type.TRAY_NOTIFICATION);
					return;
				}
				if (PaymentMode.CASH.equals(billVO.getPayMode())) {
					payCashModel(eventBus, billVO);
				} else if (PaymentMode.CHEQUE.equals(billVO.getPayMode())) {
				} else if (billVO.getIsDelayedPay()) {
				} else {
					Notification.show("Bill Me", "Invalid Payment Mode",
							Type.TRAY_NOTIFICATION);
				}
			}
		});

		addComponent(resetBill);
		addComponent(draftBill);
		addComponent(saveBill);

		billRepository = (BillRepository) ApplicationContextProvider
				.getApplicationContext().getBean("billRepository");

		mongoTemplate = (MongoTemplate) ApplicationContextProvider
				.getApplicationContext().getBean("mongoTemplate");

	}

	private void enableBillProcessing(Boolean enabled) {
		resetBill.setEnabled(enabled);
		// draftBill.setEnabled(enabled);
		saveBill.setEnabled(enabled);
	}

	@Subscribe
	public void listenBillItemChangeEvent(final BillItemChangeEvent event) {
		logger.info("Event - {} : handler - {} : value - {}", event.getClass()
				.getSimpleName(), getClass().getSimpleName(), event);

		if (null != event.getBillItems()) {
			enableBillProcessing(true);
			billVO.setBillItems(event.getBillItems());
		} else {
			enableBillProcessing(false);
			billVO.setBillItems(null);
		}
	}

	@Subscribe
	public void listenTaxSelectionEvent(final TaxSelectionEvent event) {
		logger.info("Event - {} : handler - {} : value - {}", event.getClass()
				.getSimpleName(), getClass().getSimpleName(), event);
		taxVO = event.getTaxVO();
	}

	@Subscribe
	public void listenPaymentModeSelectionEvent(
			final PaymentModeSelectionEvent event) {
		logger.info("Event - {} : handler - {} : value - {}", event.getClass()
				.getSimpleName(), getClass().getSimpleName(), event);
		if (BillingConstants.PAY_DELAYED.equals(event.getPaymentMode())) {
			billVO.setIsDelayedPay(true);
			billVO.setPayMode(null);
		} else {
			billVO.setIsDelayedPay(false);
			billVO.setPayMode(PaymentMode.valueOf(event.getPaymentMode()
					.toUpperCase()));
		}
	}

	@Subscribe
	public void listenDeliveryModeSelectionEvent(
			final DeliveryModeSelectionEvent event) {
		logger.info("Event - {} : handler - {} : value - {}", event.getClass()
				.getSimpleName(), getClass().getSimpleName(), event);
		billVO.setIsDoorDelivery(event.getIsDoorDelivery());
	}

	public void payCashModel(final EventBus eventBus, final BillVO billVO) {
		final Window cashWindow = new Window(BillingConstants.CASH_PAYMENT);
		final FormLayout cashForm = new FormLayout();
		final TextField billAmt = new TextField();
		final TextField roundedBillAmt = new TextField();
		final TextField receivedAmt = new TextField();
		final TextField payBackAmt = new TextField();
		final HorizontalLayout footerHL = new HorizontalLayout();
		final Button printBill = new Button();
		final BrowserWindowOpener printWindow = new BrowserWindowOpener(
				PrintBillUI.class, BillingConstants.URI_PRINT);

		billAmt.setCaption(BillingConstants.BILLABLE_AMT);
		billAmt.setWidth("100%");
		billAmt.setStyleName("v-textfield-align-right");
		billAmt.setConverter(new AmountConverter());
		billAmt.setPropertyDataSource(new ObjectProperty<Amount<Money>>(billVO
				.getTotal()));
		billAmt.setReadOnly(true);

		roundedBillAmt.setCaption(BillingConstants.ROUNDED_AMT);
		roundedBillAmt.setWidth("100%");
		roundedBillAmt.setStyleName("v-textfield-align-right");
		roundedBillAmt.setConverter(new AmountConverter());
		roundedBillAmt.setPropertyDataSource(new ObjectProperty<Amount<Money>>(
				ComputationUtil.computeRoundedAmount(billVO.getTotal())));
		roundedBillAmt.setReadOnly(true);

		receivedAmt.setCaption(BillingConstants.RECEIVED_AMT);
		receivedAmt.setWidth("100%");
		receivedAmt.setStyleName("v-textfield-align-right");
		receivedAmt.setConverter(new AmountConverter());
		receivedAmt.setPropertyDataSource(new ObjectProperty<Amount<Money>>(
				Amount.valueOf(0.0, BillingUnits.INR)));
		receivedAmt.setImmediate(true);
		receivedAmt.addFocusListener(new FocusListener() {

			private static final long serialVersionUID = -5687359956231689993L;

			@Override
			public void focus(FocusEvent event) {
				((TextField) event.getComponent()).setValue("");
			}
		});
		receivedAmt.addValueChangeListener(new Property.ValueChangeListener() {

			private static final long serialVersionUID = 5355490604466827525L;

			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				if (null != event.getProperty().getValue()) {
					if (!((String) event.getProperty().getValue()).isEmpty()) {
						Amount<Money> receivedMoney = (Amount<Money>) receivedAmt
								.getPropertyDataSource().getValue();
						Amount<Money> roundedMoney = (Amount<Money>) roundedBillAmt
								.getPropertyDataSource().getValue();
						if (-1 != receivedMoney.compareTo(roundedMoney)) {
							payBackAmt.getPropertyDataSource().setValue(
									receivedMoney.minus(roundedMoney));
							printBill.setEnabled(true);
						} else {
							Notification.show("Cash not enough!",
									receivedMoney.toString(),
									Type.TRAY_NOTIFICATION);
							printBill.setEnabled(false);
						}
					} else {
						payBackAmt.getPropertyDataSource().setValue(
								((Amount<Money>) payBackAmt
										.getPropertyDataSource().getValue())
										.times(0));
						printBill.setEnabled(false);
					}
				}

			}
		});

		payBackAmt.setCaption(BillingConstants.PAYBACK_AMT);
		payBackAmt.setWidth("100%");
		payBackAmt.setStyleName("v-textfield-align-right");
		payBackAmt.setReadOnly(true);
		payBackAmt.setConverter(new AmountConverter());
		payBackAmt.setPropertyDataSource(new ObjectProperty<Amount<Money>>(
				Amount.valueOf(0.0, BillingUnits.INR)));

		printBill.setCaption(BillingConstants.CONFIRM);
		printBill.setSizeUndefined();
		printBill.setImmediate(true);
		printBill.setEnabled(false);
		printBill.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -779726628843991503L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (BillingConstants.CONFIRM.equals(event.getButton()
						.getCaption())) {
					Date todaysDate = new Date();
					billVO.setBillDate(todaysDate);
					billVO.setIsPaid(true);
					billVO.setPaidDate(todaysDate);
					Bill bill = VOConverterUtil.constructBillObject(billVO);
					bill = billRepository.insert(bill, mongoTemplate);
					logger.info("BillVO {}", billVO);
					Notification.show(bill.getBillNo(),
							"Bill Saved Successfully", Type.TRAY_NOTIFICATION);
					event.getButton().setCaption(BillingConstants.PRINT);
					printWindow.setFeatures("resizable=yes");
					printWindow.setParameter("billNo", bill.getBillNo());
					printWindow.extend(printBill);
				} else if (BillingConstants.PRINT.equals(event.getButton()
						.getCaption())) {
					// TODO print the bill
					// TODO keep the modal with progress bar depicting print
					// TODO call the reset event on successful save and print
					logger.info("{} posts ResetBillingEvent", getClass()
							.getSimpleName());
					eventBus.post(new ResetBillingEvent());
					cashWindow.close();
				}
			}
		});

		footerHL.addComponent(printBill);
		footerHL.setComponentAlignment(printBill, Alignment.MIDDLE_RIGHT);
		footerHL.setImmediate(false);
		footerHL.setWidth("100%");
		footerHL.setMargin(false);
		footerHL.setSpacing(true);

		cashForm.addComponent(billAmt);
		cashForm.addComponent(roundedBillAmt);
		cashForm.addComponent(receivedAmt);
		cashForm.addComponent(payBackAmt);
		cashForm.addComponent(footerHL);

		cashForm.setImmediate(false);
		cashForm.setWidth("100%");
		cashForm.setMargin(true);
		cashForm.setSpacing(true);

		cashWindow.setContent(cashForm);
		cashWindow.setModal(true);
		cashWindow.setResizable(false);
		cashWindow.addActionHandler(new Handler() {

			private static final long serialVersionUID = -5095434104534198849L;
			Action actionEsc = new ShortcutAction("Close Modal Box",
					ShortcutAction.KeyCode.ESCAPE, null);

			@Override
			public void handleAction(Action action, Object sender, Object target) {
				if (sender instanceof Window) {
					if (action == actionEsc) {
						((Window) sender).close();
					}
				}
			}

			@Override
			public Action[] getActions(Object target, Object sender) {
				return new Action[] { actionEsc };
			}
		});

		UI.getCurrent().addWindow(cashWindow);
	}
}
