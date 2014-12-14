package in.retalemine.view.UI;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
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
		setContent(pdfBillFrame);
		JavaScript.getCurrent().execute(
				"setTimeout(function() {  print(); self.close();}, 1000);");
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

		public PdfBill(String billNo) {
			Document document = null;
			try {
				document = new Document(new Rectangle(330, 380));
				PdfWriter.getInstance(document, oStream);
				document.open();
				document.add(new Paragraph(billNo));
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (document != null) {
					document.close();
				}
			}
		}

		@Override
		public InputStream getStream() {
			return new ByteArrayInputStream(oStream.toByteArray());
		}
	}

}
