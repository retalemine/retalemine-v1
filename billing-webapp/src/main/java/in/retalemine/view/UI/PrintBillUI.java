package in.retalemine.view.UI;

import in.retalemine.constants.MongoDBKeys;
import in.retalemine.entity.Bill;
import in.retalemine.entity.BillItem;
import in.retalemine.entity.Tax;
import in.retalemine.measure.unit.BillingUnits;
import in.retalemine.repository.BillRepository;
import in.retalemine.util.ApplicationContextProvider;
import in.retalemine.util.BillingComputationUtil;
import in.retalemine.util.ComputationUtil;
import in.retalemine.util.InputParser;
import in.retalemine.view.constants.BillingConstants;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;

import org.jscience.economics.money.Money;
import org.jscience.physics.amount.Amount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.UI;

@Theme("retaletheme")
@Title("Printing Bill....")
public class PrintBillUI extends UI {

	private static final long serialVersionUID = -2151928483171679984L;
	private static final Logger logger = LoggerFactory
			.getLogger(PrintBillUI.class);

	@Override
	protected void init(VaadinRequest request) {
		logger.info("Initializing {}", getClass().getSimpleName());
		BrowserFrame pdfBillFrame = new BrowserFrame();
		pdfBillFrame.setSource(getPdfBillResource(request
				.getParameter("billNo")));
		pdfBillFrame.setSizeFull();
		setContent(pdfBillFrame);
		JavaScript.getCurrent().execute(
				"setTimeout(function() {  print();}, 1000);");
	}

	private Resource getPdfBillResource(String billNo) {
		StreamResource resource = new StreamResource(new PdfBill(billNo),
				billNo + ".pdf");
		resource.setMIMEType("application/pdf");
		resource.getStream().setParameter("Content-Disposition",
				"attachment; filename=" + billNo + ".pdf");
		return resource;
	}

	class PdfBill implements StreamSource {
		private static final long serialVersionUID = 5436511090062056105L;

		private final ByteArrayOutputStream oStream = new ByteArrayOutputStream();
		private final BillRepository billRepository;
		private final Font fontTimesRoman;

		public PdfBill(String billNo) {

			billRepository = (BillRepository) ApplicationContextProvider
					.getApplicationContext().getBean("billRepository");
			Bill bill = billRepository.findOne(new Query().addCriteria(Criteria
					.where(MongoDBKeys.BILL_NO).is(billNo)), Bill.class);

			Document document = null;
			Amount<Money> subTotal = Amount.valueOf(0, BillingUnits.INR);
			Double taxPercent = 0.0;
			DecimalFormat amountFormat = new DecimalFormat("0.00");

			Rectangle billPage = new Rectangle(224f, getBillHeight(bill));
			fontTimesRoman = new Font(FontFamily.TIMES_ROMAN, 8f, Font.NORMAL);
			try {
				document = new Document(billPage, 8f, 8f, 0f, 0f);
				PdfWriter.getInstance(document, oStream);

				document.addTitle("Invoice");
				document.addSubject("Retalemine - Invoice");
				document.addAuthor("Retalemine");
				document.addKeywords("invoice");
				document.addProducer();
				document.addCreator("Retalemine");
				document.addCreationDate();

				document.open();

				Paragraph billHeader = new Paragraph(
						"Retalemine Billing Solutions\n", fontTimesRoman);
				billHeader.add(new Phrase("mail: retalemine@retalemine.com"));
				billHeader.setAlignment(Element.ALIGN_CENTER);
				billHeader.setSpacingBefore(0f);
				billHeader.setSpacingAfter(5f);
				document.add(billHeader);

				PdfPTable billInfo = new PdfPTable(2);
				billInfo.setWidthPercentage(100f);
				billInfo.setWidths(new int[] { 1, 2 });

				billInfo.addCell(dottedLinePdfPCell());
				billInfo.addCell(contentPdfPCell("Bill No:"));
				billInfo.addCell(contentPdfPCell(bill.getBillNo()));

				billInfo.addCell(contentPdfPCell("Date & Time:"));
				billInfo.addCell(contentPdfPCell(BillingComputationUtil
						.getClientDateTimeFormat(bill.getBillDate(),
								BillingConstants.FORMAT_DATE)
						+ " & "
						+ BillingComputationUtil.getClientDateTimeFormat(
								bill.getBillDate(),
								BillingConstants.FORMAT_TIME)));

				document.add(billInfo);

				PdfPTable billContent = new PdfPTable(3);
				billContent.setWidthPercentage(100f);
				billContent.setWidths(new float[] { 2.5f, 8.5f, 3 });

				billContent.addCell(dottedLinePdfPCell());
				billContent.addCell(contentPdfPCell("Qty"));
				billContent.addCell(contentPdfPCell("Product Name"));
				billContent.addCell(contentPdfPCell("Amount",
						Element.ALIGN_RIGHT));
				billContent.addCell(dottedLinePdfPCell());
				for (BillItem item : bill.getBillItems()) {
					String productDetail[] = InputParser
							.resolveProductUnit(item.getProductDescription());
					Amount<Money> amount = ComputationUtil.computeAmount(
							ComputationUtil.getUnitQuantity(item
									.getProductDescription()), item
									.getUnitPrice(), item.getQuantity());
					billContent.addCell(contentPdfPCell(item.getQuantity()
							.toString()));
					billContent.addCell(contentPdfPCell(productDetail[0]));

					billContent.addCell(contentPdfPCell(amountFormat
							.format(ComputationUtil.computeClearAmount(amount
									.toText())), Element.ALIGN_RIGHT));
					subTotal = subTotal.plus(amount);
				}
				billContent.addCell(dottedLinePdfPCell());

				document.add(billContent);

				PdfPTable billValues = new PdfPTable(2);
				billValues.setWidthPercentage(75f);
				billValues.setHorizontalAlignment(Element.ALIGN_RIGHT);
				billValues.setWidths(new float[] { 1.5f, 1 });

				billValues.addCell(contentPdfPCell("Sub Total :",
						Element.ALIGN_RIGHT));
				billValues.addCell(contentPdfPCell(amountFormat
						.format(ComputationUtil.computeClearAmount(subTotal
								.toText())), Element.ALIGN_RIGHT));

				if (null != bill.getTaxes()) {
					for (Tax tax : bill.getTaxes()) {
						billValues.addCell(contentPdfPCell(
								tax.getTaxType().getValue() + " "
										+ tax.getTaxPercent() + "% :",
								Element.ALIGN_RIGHT));
						billValues.addCell(contentPdfPCell(amountFormat
								.format(ComputationUtil
										.computeClearAmount(subTotal
												.times(tax.getTaxPercent())
												.divide(100).toText())),
								Element.ALIGN_RIGHT));
						taxPercent += tax.getTaxPercent();
					}
				}

				billValues.addCell(dottedLinePdfPCell(35f));
				billValues.addCell(contentPdfPCell("Grand Total (rounded) :",
						Element.ALIGN_RIGHT));
				billValues.addCell(contentPdfPCell(amountFormat
						.format(ComputationUtil
								.computeClearAmount(ComputationUtil
										.computeRoundedAmount(bill.getTotal())
										.toText())), Element.ALIGN_RIGHT));
				billValues.addCell(dottedLinePdfPCell(35f));

				document.add(billValues);

				Paragraph billFooter = new Paragraph("Thank You, Visit Again!",
						fontTimesRoman);
				billFooter.setAlignment(Element.ALIGN_CENTER);
				document.add(billFooter);
			} catch (DocumentException e) {
				e.printStackTrace();
			} finally {
				if (document != null) {
					document.close();
				}
			}
		}

		private float getBillHeight(Bill bill) {
			if (null == bill.getTaxes()) {
				return 130f + bill.getBillItems().size() * 10;
			} else {
				return 130f + bill.getBillItems().size() * 10
						+ bill.getTaxes().size() * 10;
			}
		}

		@Override
		public InputStream getStream() {
			return new ByteArrayInputStream(oStream.toByteArray());
		}

		private PdfPCell contentPdfPCell(String value) {
			return contentPdfPCell(new Paragraph(value, fontTimesRoman));
		}

		private PdfPCell contentPdfPCell(String value, int alignRight) {
			Paragraph content = new Paragraph(value, fontTimesRoman);
			content.setAlignment(alignRight);
			return contentPdfPCell(content);
		}

		private PdfPCell contentPdfPCell(Paragraph para) {
			PdfPCell cell = new PdfPCell();
			cell.setBorder(Rectangle.NO_BORDER);
			cell.addElement(para);
			cell.setPaddingTop(0f);
			cell.setPaddingBottom(2f);
			return cell;
		}

		private PdfPCell dottedLinePdfPCell() {
			return dottedLinePdfPCell(new DottedLineSeparator());
		}

		private PdfPCell dottedLinePdfPCell(float width) {
			DottedLineSeparator dottedLine = new DottedLineSeparator();
			dottedLine.setPercentage(width);
			dottedLine.setAlignment(Element.ALIGN_RIGHT);
			return dottedLinePdfPCell(dottedLine);
		}

		private PdfPCell dottedLinePdfPCell(DottedLineSeparator obj) {
			PdfPCell dottedLine = new PdfPCell();
			dottedLine.setBorder(Rectangle.NO_BORDER);
			dottedLine.addElement(obj);
			dottedLine.setColspan(3);
			dottedLine.setPaddingBottom(0f);
			dottedLine.setPaddingLeft(0f);
			dottedLine.setPaddingRight(0f);
			return dottedLine;
		}
	}

}
