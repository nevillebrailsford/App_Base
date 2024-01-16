package application.report;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;

import application.definition.ApplicationConfiguration;
import application.definition.ApplicationDefinition;
import application.notification.Notification;
import application.notification.NotificationCentre;

public abstract class ReportCreator implements Runnable {

	public final static String CLASS_NAME = ReportCreator.class.getName();
	public final static Logger LOGGER = ApplicationConfiguration.logger();

	public ApplicationDefinition applicationDescriptor;

	public PdfFont font = null;
	public PdfFont bold = null;
	public PdfWriter writer = null;
	public PdfDocument pdf = null;
	public Document document = null;
	public DateTimeFormatter reportDateFormatter = DateTimeFormatter.ofPattern(ReportConstants.dateFormatForUI);

	private File pdfFile = null;
	private String reportName = null;

	public ReportCreator(String reportName) {
		LOGGER.entering(CLASS_NAME, "init", reportName);
		this.reportName = reportName;
		LOGGER.exiting(CLASS_NAME, "init");
	}

	@Override
	public void run() {
		LOGGER.entering(CLASS_NAME, "run");
		try {
			generateReport();
		} catch (Exception e) {
			LOGGER.warning("Caught exception: " + e.getMessage());
			LOGGER.exiting(CLASS_NAME, "run");
		}
		LOGGER.exiting(CLASS_NAME, "run");
	}

	public DateTimeFormatter dateTimeFormatter() {
		return reportDateFormatter;
	}

	public abstract void writePdfReport();

	private void generateReport() throws Exception {
		LOGGER.entering(CLASS_NAME, "generateReport");
		applicationDescriptor = ApplicationConfiguration.applicationDefinition();
		if (applicationDescriptor == null) {
			IllegalStateException exc = new IllegalStateException("Report - applicationDecsriptor is null");
			LOGGER.throwing(CLASS_NAME, "generateReport", exc);
			LOGGER.exiting(CLASS_NAME, "generateReport");
			throw exc;
		}
		try {
			createFonts();
			createPdfFile();
			createPdfDocument();
			writePdfReport();
			closePdfDocument();
			NotificationCentre.broadcast(new Notification(ReportNotificationType.Created, this, pdfFile.getName()));
		} catch (Exception e) {
			LOGGER.warning("Caught exception: " + e.getMessage());
			LOGGER.throwing(CLASS_NAME, "generateReport", e);
			LOGGER.exiting(CLASS_NAME, "generateReport");
			NotificationCentre.broadcast(new Notification(ReportNotificationType.Failed, this, e.getMessage()));
			throw e;
		}
		LOGGER.exiting(CLASS_NAME, "generateReport");
	}

	private void createFonts() {
		LOGGER.entering(CLASS_NAME, "createFonts");
		try {
			font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
			bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
		} catch (IOException e) {
			LOGGER.warning("Caught exception: " + e.getMessage());
			cleanup();
			LOGGER.exiting(CLASS_NAME, "createFonts");
		}
		LOGGER.exiting(CLASS_NAME, "createFonts");
	}

	private void createPdfFile() throws IOException {
		LOGGER.entering(CLASS_NAME, "createPdfFile");
		File reportDirectory = new File(applicationDescriptor.reportDirectory());
		if (!Files.exists(reportDirectory.toPath())) {
			LOGGER.fine("Creating report directory " + reportDirectory.getAbsolutePath());
			try {
				Files.createDirectories(reportDirectory.toPath());
			} catch (IOException e) {
				LOGGER.warning("Caught exception: " + e.getMessage());
				LOGGER.throwing(CLASS_NAME, "createPdfFile", e);
				LOGGER.exiting(CLASS_NAME, "createPdfFile");
				throw e;
			}
		}
		pdfFile = new File(reportDirectory, reportName);
		LOGGER.fine("Created PDF file called " + pdfFile.getAbsolutePath());
		LOGGER.exiting(CLASS_NAME, "createPdfFile");
	}

	private void createPdfDocument() {
		LOGGER.entering(CLASS_NAME, "createPdfDocument");
		try {
			writer = new PdfWriter(pdfFile);
			pdf = new PdfDocument(writer);
			document = new Document(pdf);
		} catch (FileNotFoundException e) {
			LOGGER.warning("Caught exception: " + e.getMessage());
			cleanup();
			LOGGER.exiting(CLASS_NAME, "createPdfDocument");
		}
		LOGGER.exiting(CLASS_NAME, "createPdfDocument");
	}

	private void closePdfDocument() {
		LOGGER.entering(CLASS_NAME, "closePdfDocument");
		cleanup();
		LOGGER.exiting(CLASS_NAME, "closePdfDocument");
	}

	private void cleanup() {
		LOGGER.entering(CLASS_NAME, "cleanup");
		if (font != null) {
			font = null;
		}
		if (bold != null) {
			bold = null;
		}
		if (document != null) {
			document.close();
			document = null;
		}
		if (writer != null) {
			try {
				writer.close();
			} catch (IOException e) {
			}
			writer = null;
		}
		LOGGER.exiting(CLASS_NAME, "cleanup");
	}

}
