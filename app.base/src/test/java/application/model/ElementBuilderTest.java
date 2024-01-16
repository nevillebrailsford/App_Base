package application.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ElementBuilderTest {

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
	void testNullTag() {
		assertThrows(IllegalArgumentException.class, () -> {
			ElementBuilder.build(null, "test", document);
		});
	}

	@Test
	void testNullText() {
		assertThrows(IllegalArgumentException.class, () -> {
			ElementBuilder.build("test", null, document);
		});
	}

	@Test
	void testNullDocument() {
		assertThrows(IllegalArgumentException.class, () -> {
			ElementBuilder.build("test", "test", null);
		});
	}

	@Test
	void testBlankTag() {
		assertThrows(IllegalArgumentException.class, () -> {
			ElementBuilder.build("", "test", document);
		});
	}

	@Test
	void testEmptyTag() {
		assertThrows(IllegalArgumentException.class, () -> {
			ElementBuilder.build("     ", "test", document);
		});
	}

	@Test
	void testBlankText() {
		assertThrows(IllegalArgumentException.class, () -> {
			ElementBuilder.build("test", "", document);
		});
	}

	@Test
	void testEmptyText() {
		assertThrows(IllegalArgumentException.class, () -> {
			ElementBuilder.build("test", "    ", document);
		});
	}

	@Test
	void testValidCall() {
		Element element = ElementBuilder.build("test", "text", document);
		assertEquals("text", element.getTextContent());
		assertEquals("test", element.getTagName());
	}

}
