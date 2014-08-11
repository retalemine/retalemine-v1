package in.retalemine.view.component;

import in.retalemine.view.VO.ProductVO;
import in.retalemine.view.converter.AmountConverter;

import java.util.Date;

import javax.measure.quantity.Quantity;

import org.jscience.economics.money.Currency;
import org.jscience.economics.money.Money;
import org.jscience.physics.amount.Amount;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Runo;


public class BillingComponent extends CustomComponent {

	private static final long serialVersionUID = -5001424944545200006L;

	private VerticalLayout mainLayout;

	private Table billableItemsTB = new Table();

	private Label totalValueLB = new Label();
	
	private Button billMeBT = new Button();
	private Button resetBT = new Button();
	private OptionGroup payModeOG = new OptionGroup();
	private CheckBox deliveryChB = new CheckBox();

	private TextField cusNameTF = new TextField();
	private TextField cusContactNoTF = new TextField();
	private TextArea cusAddressTA = new TextArea();

	BeanContainer<String, ProductVO<? extends Quantity>> productNameCBCTR = new BeanContainer<String, ProductVO<? extends Quantity>>(
			ProductVO.class);

	private final String HOME_DELIVERY = "Home delivery";
	private final String BILL_ME = "Bill Me";
	private final String RESET = "Reset";
	private final String PAY_CASH = "Cash";
	private final String PAY_CHEQUE = "Cheque";
	private final String PAY_DELAYED = "Delayed";
	private final String CUSTOMER_NAME = "Customer Name";
	private final String CUSTOMER_CONTACT_NO = "Contact No.";
	private final String CUSTOMER_ADDRESS = "Address";
	private final Integer CUSTOMER_ADDRESS_ROWS = 2;

	public BillingComponent() {
		setCompositionRoot(buildMainLayout());
	}

	public static Currency getGlobalCurrency() {
		return null;//Rupee.INR;
	}

	private VerticalLayout buildMainLayout() {
		mainLayout = new VerticalLayout();

		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);

		setWidth("100.0%");
		setHeight("100.0%");

		mainLayout.addComponent(buildBillingFooter());

		return mainLayout;
	}

	private Component buildBillingFooter() {
		GridLayout footerGrid = new GridLayout(5, 1);

		footerGrid.setImmediate(false);
		footerGrid.setWidth("100%");
		footerGrid.setMargin(false);
		footerGrid.setSpacing(true);

		footerGrid.addComponent(buildCustomerProfile(), 0, 0, 2, 0);
		footerGrid.addComponent(buildBillingPayments(), 3, 0, 4, 0);

		return footerGrid;
	}

	private Component buildBillingPayments() {
		VerticalLayout paymentLayout = new VerticalLayout();
		Panel paymentModeLayout = new Panel("Payment Mode");
		VerticalLayout paymentDeliveryLayout = new VerticalLayout();
		VerticalLayout BillMeLayout = new VerticalLayout();
		HorizontalLayout paymentDeliveryBillMeLayout = new HorizontalLayout();

		payModeOG.addItem(PAY_CASH);
		payModeOG.addItem(PAY_CHEQUE);
		payModeOG.addItem(PAY_DELAYED);
		payModeOG.setMultiSelect(false);
		payModeOG.select(PAY_CASH);
		payModeOG.setWidth("100%");
		payModeOG.setStyleName("v-select-optiongroup-horizontal");
		payModeOG.setImmediate(true);
		payModeOG.addValueChangeListener(new Property.ValueChangeListener() {

			private static final long serialVersionUID = 5654804808822582441L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (PAY_CHEQUE.equals(event.getProperty().getValue())) {
					mandateCustomerDetails(true);
				} else if (PAY_DELAYED.equals(event.getProperty().getValue())) {
					mandateCustomerDetails(true);
				} else if (PAY_CASH.equals(event.getProperty().getValue())) {
					if (!deliveryChB.getValue()) {
						mandateCustomerDetails(false);
					}

				}
			}
		});

		deliveryChB.setCaption(HOME_DELIVERY);
		deliveryChB.setImmediate(true);
		deliveryChB.addValueChangeListener(new Property.ValueChangeListener() {

			private static final long serialVersionUID = 5867647913560616744L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (PAY_CASH.equals(payModeOG.getValue())) {
					mandateCustomerDetails((Boolean) event.getProperty()
							.getValue());
				}
			}
		});

		billMeBT.setCaption(BILL_ME);
		billMeBT.setWidth("100%");
		billMeBT.setEnabled(false);
		billMeBT.setImmediate(true);
		billMeBT.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 8093769158458975223L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO validate mandatory fields
				// TODO based on payment mode respective modal box
				if (PAY_CASH.equals(payModeOG.getValue())) {
					displayCashModal();
				} else if (PAY_CHEQUE.equals(payModeOG.getValue())) {
					displayChequeModal();
				} else if (PAY_DELAYED.equals(payModeOG.getValue())) {
					displayDelayedModal();
				} else {
					Notification.show("Bill Me", "Invalid Payment Mode",
							Type.TRAY_NOTIFICATION);
				}
			}
		});

		resetBT.setCaption(RESET);
		resetBT.setWidth("100%");
		resetBT.setEnabled(false);
		resetBT.setImmediate(true);
		resetBT.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 9118398197062156254L;

			@Override
			public void buttonClick(ClickEvent event) {
				resetBillingComponent();
			}
		});

		paymentLayout.addComponent(paymentDeliveryBillMeLayout);

		paymentDeliveryBillMeLayout.addComponent(paymentDeliveryLayout);
		paymentDeliveryBillMeLayout.addComponent(BillMeLayout);

		paymentDeliveryLayout.addComponent(paymentModeLayout);
		paymentDeliveryLayout.addComponent(deliveryChB);

		paymentModeLayout.setContent(payModeOG);

		BillMeLayout.addComponent(billMeBT);
		BillMeLayout.addComponent(resetBT);
		BillMeLayout.setComponentAlignment(billMeBT, Alignment.BOTTOM_RIGHT);
		BillMeLayout.setComponentAlignment(resetBT, Alignment.BOTTOM_RIGHT);

		paymentDeliveryLayout.setImmediate(false);
		paymentDeliveryLayout.setWidth("100%");
		paymentDeliveryLayout.setMargin(false);
		paymentDeliveryLayout.setSpacing(true);

		paymentModeLayout.setStyleName(Runo.PANEL_LIGHT);
		paymentModeLayout.setImmediate(false);
		paymentModeLayout.setWidth("100%");

		BillMeLayout.setImmediate(false);
		BillMeLayout.setWidth("100%");
		BillMeLayout.setHeight("");
		BillMeLayout.setMargin(false);
		BillMeLayout.setSpacing(true);

		paymentDeliveryBillMeLayout.setImmediate(false);
		paymentDeliveryBillMeLayout.setWidth("100%");
		paymentDeliveryBillMeLayout.setMargin(false);
		paymentDeliveryBillMeLayout.setSpacing(true);
		paymentDeliveryBillMeLayout.setExpandRatio(paymentDeliveryLayout, 2.0f);
		paymentDeliveryBillMeLayout.setExpandRatio(BillMeLayout, 1.0f);
		paymentDeliveryBillMeLayout.setComponentAlignment(BillMeLayout,
				Alignment.BOTTOM_RIGHT);

		paymentLayout.setImmediate(false);
		paymentLayout.setWidth("100%");
		paymentLayout.setMargin(true);
		paymentLayout.setSpacing(true);

		return paymentLayout;
	}

	protected void displayCashModal() {
		final Window sub = new Window("Cash Payment");

		VerticalLayout contentVL = new VerticalLayout();
		FormLayout cashFL = new FormLayout();
		final TextField billAmt = new TextField();
		final TextField amtReceived = new TextField();
		final TextField payBackAmt = new TextField();
		HorizontalLayout footerHL = new HorizontalLayout();
		Button saveDraft = new Button();
		Button printBill = new Button();

		billAmt.setCaption("Billable Amount");
		billAmt.setWidth("100%");
		billAmt.setStyleName("v-textfield-align-right");
		billAmt.setReadOnly(true);
		billAmt.setConverter(new AmountConverter());
		billAmt.setPropertyDataSource(totalValueLB.getPropertyDataSource());

		amtReceived.setCaption("Received Amount");
		amtReceived.setWidth("100%");
		amtReceived.setStyleName("v-textfield-align-right");
		amtReceived.setConverter(new AmountConverter());
//		amtReceived.setPropertyDataSource(new ObjectProperty<Amount<Money>>(
//				Amount.valueOf(0.0, Rupee.INR)));
		amtReceived.setImmediate(true);
		amtReceived.addFocusListener(new FocusListener() {

			private static final long serialVersionUID = -5687359956231689993L;

			@Override
			public void focus(FocusEvent event) {
				((TextField) event.getComponent()).setValue("");
			}
		});
		amtReceived.addValueChangeListener(new ValueChangeListener() {

			private static final long serialVersionUID = 5355490604466827525L;

			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (null != event.getProperty().getValue()
						&& !((String) event.getProperty().getValue()).isEmpty()) {
					payBackAmt
							.getPropertyDataSource()
							.setValue(
									((Amount<Money>) amtReceived
											.getPropertyDataSource().getValue())
											.minus((Amount<Money>) billAmt
													.getPropertyDataSource()
													.getValue()));
				}

			}
		});

		payBackAmt.setCaption("Payback Amount");
		payBackAmt.setWidth("100%");
		payBackAmt.setStyleName("v-textfield-align-right");
		payBackAmt.setReadOnly(true);
		payBackAmt.setConverter(new AmountConverter());
//		payBackAmt.setPropertyDataSource(new ObjectProperty<Amount<Money>>(
//				Amount.valueOf(0.0, Rupee.INR)));

		saveDraft.setCaption("Save Draft");
		saveDraft.setSizeUndefined();
		saveDraft.setImmediate(true);
		saveDraft.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 3808358760477618923L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO save the bill as draft and don't print it
				// TODO do provision to save the draft and make a print also
				// TODO what will be the payment mode defaulted to?
				sub.close();
				resetBillingComponent();
			}
		});

		printBill.setCaption("Print Bill");
		printBill.setSizeUndefined();
		printBill.setImmediate(true);
		printBill.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -779726628843991503L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO save the bill and print it
				sub.close();
				resetBillingComponent();
			}
		});

		contentVL.addComponent(cashFL);
		contentVL.addComponent(footerHL);
		contentVL.setComponentAlignment(footerHL, Alignment.MIDDLE_RIGHT);

		cashFL.addComponent(billAmt);
		cashFL.addComponent(amtReceived);
		cashFL.addComponent(payBackAmt);

		footerHL.addComponent(saveDraft);
		footerHL.addComponent(printBill);
		footerHL.setComponentAlignment(saveDraft, Alignment.MIDDLE_RIGHT);
		footerHL.setComponentAlignment(printBill, Alignment.MIDDLE_RIGHT);

		cashFL.setImmediate(false);
		cashFL.setWidth("100%");
		cashFL.setMargin(false);
		cashFL.setSpacing(true);

		footerHL.setImmediate(false);
		footerHL.setSizeUndefined();
		footerHL.setMargin(false);
		footerHL.setSpacing(true);

		contentVL.setImmediate(false);
		contentVL.setWidth("100%");
		contentVL.setMargin(true);
		contentVL.setSpacing(true);

		sub.setContent(contentVL);
		sub.setModal(true);
		sub.setResizable(false);
		sub.addActionHandler(new Handler() {

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

		UI.getCurrent().addWindow(sub);

	}

	protected void displayChequeModal() {
		final Window sub = new Window("Cheque Payment");

		FormLayout chequeFL = new FormLayout();
		TextField bankName = new TextField();
		TextField chequeNumber = new TextField();
		final TextField billAmt = new TextField();
		final TextField amtReceived = new TextField();
		final TextField payBackAmt = new TextField();
		Button printBill = new Button();

		bankName.setCaption("Bank Name");
		bankName.setWidth("100%");
		bankName.setStyleName("v-textfield-align-right");

		chequeNumber.setCaption("Cheque Number#");
		chequeNumber.setWidth("100%");
		chequeNumber.setStyleName("v-textfield-align-right");

		billAmt.setCaption("Billable Amount");
		billAmt.setWidth("100%");
		billAmt.setStyleName("v-textfield-align-right");
		billAmt.setReadOnly(true);
		billAmt.setConverter(new AmountConverter());
		billAmt.setPropertyDataSource(totalValueLB.getPropertyDataSource());

		amtReceived.setCaption("Received Amount");
		amtReceived.setWidth("100%");
		amtReceived.setStyleName("v-textfield-align-right");
		amtReceived.setConverter(new AmountConverter());
//		amtReceived.setPropertyDataSource(new ObjectProperty<Amount<Money>>(
//				Amount.valueOf(0.0, Rupee.INR)));
		amtReceived.setImmediate(true);
		amtReceived.addFocusListener(new FocusListener() {

			private static final long serialVersionUID = 3216033202874236783L;

			@Override
			public void focus(FocusEvent event) {
				((TextField) event.getComponent()).setValue("");
			}
		});
		amtReceived.addValueChangeListener(new ValueChangeListener() {

			private static final long serialVersionUID = 7908458548102263922L;

			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (null != event.getProperty().getValue()
						&& !((String) event.getProperty().getValue()).isEmpty()) {
					payBackAmt
							.getPropertyDataSource()
							.setValue(
									((Amount<Money>) amtReceived
											.getPropertyDataSource().getValue())
											.minus((Amount<Money>) billAmt
													.getPropertyDataSource()
													.getValue()));
				}

			}
		});

		payBackAmt.setCaption("Payback Amount");
		payBackAmt.setWidth("100%");
		payBackAmt.setStyleName("v-textfield-align-right");
		payBackAmt.setReadOnly(true);
		payBackAmt.setConverter(new AmountConverter());
//		payBackAmt.setPropertyDataSource(new ObjectProperty<Amount<Money>>(
//				Amount.valueOf(0.0, Rupee.INR)));

		printBill.setCaption("Print Bill");
		printBill.setSizeUndefined();
		printBill.setImmediate(true);
		printBill.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 535739329405714802L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO save the bill and print it
				sub.close();
				resetBillingComponent();
			}
		});

		chequeFL.addComponent(bankName);
		chequeFL.addComponent(chequeNumber);
		chequeFL.addComponent(billAmt);
		chequeFL.addComponent(amtReceived);
		chequeFL.addComponent(payBackAmt);
		chequeFL.addComponent(printBill);

		chequeFL.setImmediate(false);
		chequeFL.setWidth("100%");
		chequeFL.setMargin(true);
		chequeFL.setSpacing(true);

		sub.setContent(chequeFL);
		sub.setModal(true);
		sub.setResizable(false);
		sub.addActionHandler(new Handler() {

			private static final long serialVersionUID = 5855458178562827022L;
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

		UI.getCurrent().addWindow(sub);
	}

	protected void displayDelayedModal() {
		final Window sub = new Window("Delayed Payment");

		FormLayout delayedFL = new FormLayout();
		final TextField billAmt = new TextField();
		final DateField payableDate = new DateField();
		Date currentDate = null;
		Button printBill = new Button();

		billAmt.setCaption("Billable Amount");
		billAmt.setWidth("100%");
		billAmt.setReadOnly(true);
		billAmt.setConverter(new AmountConverter());
		billAmt.setPropertyDataSource(totalValueLB.getPropertyDataSource());

		payableDate.setCaption("Payable Date");
		payableDate.setWidth("100%");
		payableDate.setDateFormat("dd-MMM-yyyy (E)");
		payableDate.setImmediate(true);
		payableDate.setRangeStart((currentDate = new Date()));
		payableDate.setRangeEnd(new Date(currentDate.getTime() + 2592000000L));
		payableDate.addValueChangeListener(new ValueChangeListener() {

			private static final long serialVersionUID = -5135380705893758953L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (null != event.getProperty()) {
				}
			}
		});

		printBill.setCaption("Print Bill");
		printBill.setSizeUndefined();
		printBill.setImmediate(true);
		printBill.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 9049648993181705643L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO save the bill and print it
				sub.close();
				resetBillingComponent();
			}
		});

		delayedFL.addComponent(billAmt);
		delayedFL.addComponent(payableDate);
		delayedFL.addComponent(printBill);

		delayedFL.setImmediate(false);
		delayedFL.setWidth("100%");
		delayedFL.setMargin(true);
		delayedFL.setSpacing(true);

		sub.setContent(delayedFL);
		sub.setModal(true);
		sub.setResizable(false);
		sub.addActionHandler(new Handler() {

			private static final long serialVersionUID = 5779658659989871382L;
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

		UI.getCurrent().addWindow(sub);
	}

	protected void resetBillingComponent() {
		billableItemsTB.getContainerDataSource().removeAllItems();
		payModeOG.select(PAY_CASH);
		deliveryChB.setValue(false);
	}

	protected void mandateCustomerDetails(boolean isRequired) {
		cusNameTF.setRequired(isRequired);
		cusContactNoTF.setRequired(isRequired);
		cusAddressTA.setRequired(isRequired);
	}

	private Component buildCustomerProfile() {
		Panel customerPanel = new Panel();
		VerticalLayout customerVLayout = new VerticalLayout();
		FormLayout customerForm = new FormLayout();

		cusNameTF.setCaption(CUSTOMER_NAME);
		cusNameTF.setWidth("50%");
		cusContactNoTF.setCaption(CUSTOMER_CONTACT_NO);
		cusContactNoTF.setWidth("50%");
		cusAddressTA.setCaption(CUSTOMER_ADDRESS);
		cusAddressTA.setWidth("60%");
		cusAddressTA.setRows(CUSTOMER_ADDRESS_ROWS);

		customerForm.addComponent(cusNameTF);
		customerForm.addComponent(cusContactNoTF);
		customerForm.addComponent(cusAddressTA);

		customerVLayout.addComponent(customerForm);
		customerPanel.setContent(customerVLayout);

		customerVLayout.setImmediate(false);
		customerVLayout.setWidth("100%");
		customerVLayout.setMargin(true);
		customerVLayout.setSpacing(true);

		customerPanel.setImmediate(false);
		customerPanel.setWidth("100%");

		return customerPanel;
	}

}
