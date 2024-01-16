package application.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

class ElementCheckerTest {

	Document document;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		document = documentBuilder.newDocument();
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testValidElement() {
		Element element = ElementBuilder.build("test", "text", document);
		assertTrue(ElementChecker.verifyTag(element, "test"));
	}

	@Test
	void testInValidElement() {
		Element element = ElementBuilder.build("test", "text", document);
		assertFalse(ElementChecker.verifyTag(element, "test1"));
	}

	@Test
	void testNullTag() {
		Element element = ElementBuilder.build("test", "text", document);
		assertThrows(IllegalArgumentException.class, () -> {
			ElementChecker.verifyTag(element, null);
		});
	}

	@Test
	void testEmptyTag() {
		Element element = ElementBuilder.build("test", "text", document);
		assertThrows(IllegalArgumentException.class, () -> {
			ElementChecker.verifyTag(element, "");
		});
	}

	@Test
	void testBlankTag() {
		Element element = ElementBuilder.build("test", "text", document);
		assertThrows(IllegalArgumentException.class, () -> {
			ElementChecker.verifyTag(element, "    ");
		});
	}

	@Test
	void testNullElement() {
		assertThrows(IllegalArgumentException.class, () -> {
			ElementChecker.verifyTag(null, null);
		});
	}
}
