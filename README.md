retalemine-v1
=============

Core Billing Solution

####Components:
 * ProductComboBox
   * Holds product description __sugar - 1kg__
   * Data construction
     * Dynamic
     * Lazy loading
     * Cache
   * Error handling
   * Listens:
     * RateSelectionEvent
     * CartSelectionEvent
     * BillItemSelectionEvent
     * ResetBillingEvent
   * Post:
     * ProductSelectionEvent
       * Product [Name,Unit]
       * Product [Name,Unit,UnitRates]

 * RateComboBox     
   * Holds unitRates __ 145.50 INR__
   * Error handling
   * Listens:
     * ProductSelectionEvent
     * CartSelectionEvent
     * BillItemSelectionEvent
     * ResetBillingEvent
   * Post:
     * RateSelectionEvent
       * Rate [new]
       * Rate [old]

 * QuantityComboBox
   * Holds quantityUnit __1 kg__
   * Data construction
     * Suggest reasonable units
   * Error handling
   * Listens:
     * ProductSelectionEvent
     * RateSelectionEvent
     * CartSelectionEvent
     * BillItemSelectionEvent
     * ResetBillingEvent
   * Post:
     * QuantitySelectionEvent
       * NetQuantity
           
 * CartButton
   * Add/Update Cart
   * Validation & Error Handling
   * Listens:
     * ProductSelectionEvent
     * RateSelectionEvent
     * QuantitySelectionEvent
     * BillItemSelectionEvent
     * ResetBillingEvent
   * Post:
     * CartSelectionEvent
       * Add -> add					[produntDesc,unitRate] - new
       * Add -> update				[produntDesc,unitRate] - exist
       * Update -> update			[produntDesc,unitRate] - exist [qty modified]
       * Update -> remove;add		[produntDesc,modified unitRate] - new   [unitRate itself modified(optional-qty modified)]
       * Update -> remove;update	[produntDesc,modified unitRate] - exist [unitRate itself modified(optional-qty modified)]

 * BillTable
   * Holds billItems
   * Error handling
   * Listens:
     * CartSelectionEvent
     * ResetBillingEvent
   * Post:
     * BillItemSelectionEvent
       * BillItem [selected]
       * BillItem [unselected]
     * BillItemChangeEvent
       * subTotal [Items > 0]

 * BillingAmountPanel
   * Holds Amount related values
   * Listens:
     * BillItemChangeEvent
     * TaxSelectionEvent

 * BillingAmountPanel.TaxComboBox
   * Allows to define tax for the bill
   * Post:
     * TaxSelectionEvent

 * DeliveryOption
   * Allows to define Home Delivery option
   * Post:
     * DeliveryModeSelectionEvent
   * Listens:
     * ResetBillingEvent

 * CustomerForm
   * Collects customer details
   * Listens:
     * PaymentModeSelectionEvent
     * DeliveryModeSelectionEvent
     * ResetBillingEvent

 * PaymentOptions
   * Allows to define the payment mode
   * Post:
     * PaymentModeSelectionEvent
   * Listens:
     * ResetBillingEvent 

 * BillProcessing
   * Finalize bill submission and printing
   * Post:
     * ResetBillingEvent
   * Listens:
     * BillItemChangeEvent
     * TaxSelectionEvent
     * PaymentModeSelectionEvent
     * DeliveryModeSelectionEvent
