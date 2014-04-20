package in.retalemine.view.ui;

import in.retalemine.util.UnitUtil;
import in.retalemine.view.constants.UIconstants;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.measure.Measure;
import javax.measure.quantity.Quantity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

public class ProductQuantityCB extends ComboBox {

	private static final long serialVersionUID = -466419943610903057L;
	private static final Logger logger = LoggerFactory
			.getLogger(ProductQuantityCB.class);

	private javax.measure.unit.Unit<? extends Quantity> unit;

	public ProductQuantityCB() {
		setWidth("100%");
		setPageLength(6);
		setInputPrompt(UIconstants.PROMPT_QUANTITY);
		setFilteringMode(FilteringMode.STARTSWITH);
		setRequired(true);
		setNullSelectionAllowed(true);
		setContainerDataSource(new BeanItemContainer<Measure<Double, ? extends Quantity>>(
				Measure.class));
		setItemCaptionMode(AbstractSelect.ItemCaptionMode.ID);
		setImmediate(true);
		addItemSetChangeListener(new ItemSetChangeListener() {
			private static final long serialVersionUID = 2122075138884519543L;

			@Override
			public void containerItemSetChange(ItemSetChangeEvent event) {
				logger.info("Prod Quantity item set change {}",
						((ComboBox) event.getContainer())
								.getContainerDataSource());
			}
		});
		setNewItemsAllowed(true);
		setNewItemHandler(new NewItemHandler() {
			private static final long serialVersionUID = -332009848701480764L;

			@Override
			public void addNewItem(String newQuantity) {
				Measure<Double, ? extends Quantity> quantity;
				try {
					quantity = Measure.valueOf(
							Double.parseDouble(newQuantity.trim()), unit);
					addItem(quantity);
					setValue(quantity);
				} catch (NumberFormatException e) {
					quantity = parseQuantity(newQuantity);
					if (null == quantity) {
						Notification.show("Invalid Quantity", newQuantity,
								Type.TRAY_NOTIFICATION);
						setValue(null);
						focus();
					} else {
						addItem(quantity);
						setValue(quantity);
					}
				}
			}

			private Measure<Double, ? extends Quantity> parseQuantity(
					String newQuantity) {
				Matcher quantityMatcher = Pattern.compile("(([0-9]+)(.*))")
						.matcher(newQuantity.trim().replaceAll("\\s+", " "));
				if (quantityMatcher.matches()
						&& 3 == quantityMatcher.groupCount()) {
					String quantity = quantityMatcher.group(2);
					String unit = UnitUtil.getValidUnit(quantityMatcher.group(3).trim().toLowerCase());

					logger.debug("Parsed Quantity-${}$",
							quantityMatcher.group(1));

					return Measure.valueOf(Double.parseDouble(quantity),
							javax.measure.unit.Unit.valueOf(unit));
				} else {
					logger.error("Parsed Quantity-${}$",
							quantityMatcher.group(1));
					return null;
				}
			}
		});
	}

	public void setUnit(javax.measure.unit.Unit<? extends Quantity> unit) {
		this.unit = unit;
	}

	@Override
	public void changeVariables(Object source, Map<String, Object> variables) {
		logger.info("changeVariables {}", source);
		for (Map.Entry<String, Object> entry : variables.entrySet()) {
			logger.info("variables key= {}, value={}", entry.getKey(),
					entry.getValue());
		}
		String newFilter = ((String) variables.get("filter"));
		if (null != this.unit && null != newFilter
				&& !"".equals(newFilter.trim()) && !newFilter.endsWith(" ")) {
			try {
				getContainerDataSource().removeAllItems();
				getContainerDataSource().addItem(
						Measure.valueOf(Double.parseDouble(newFilter),
								this.unit));
				// if (this.unit instanceof BaseUnit) {
				// getContainerDataSource().addItem(
				// Measure.valueOf(Double.parseDouble(newFilter),
				// this.unit.getStandardUnit()));
				// }
				logger.info("new value suggested {}", newFilter);
			} catch (NumberFormatException e) {
				getContainerDataSource().removeAllItems();
				logger.info("new value not suggested for {}", newFilter);
			}
		}
		super.changeVariables(source, variables);
	}

	@Override
	protected List<?> getOptionsWithFilter(boolean needNullSelectOption) {
		logger.info("getOptionsWithFilter - needNullSelectOption - {}",
				needNullSelectOption);
		return super.getOptionsWithFilter(needNullSelectOption);
	}

	@Override
	protected List<?> getFilteredOptions() {
		logger.info("getFilteredOptions");
		return super.getFilteredOptions();
	}

	@Override
	protected Filter buildFilter(String filterString,
			FilteringMode filteringMode) {
		logger.info("buildFilter - filterString = {} - filteringMode = {} ",
				filterString, filteringMode);
		return super.buildFilter(filterString, filteringMode);
	}

	@Override
	public void setItemCaptionPropertyId(Object propertyId) {
		logger.info("setItemCaptionPropertyId {}", propertyId);
		super.setItemCaptionPropertyId(propertyId);
	}

}
