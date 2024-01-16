package application.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

class SortCodeTest {

	private static final String SAMPLE_SORT_CODE = "98-76-54";
	SortCode sortcodeToBeTested = new SortCode(SAMPLE_SORT_CODE);

	String toString = "12-34-56";

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
	void testValidSortCode() {
		SortCode sc = new SortCode("12-34-56");
		assertNotNull(sc);
		assertEquals("12-34-56", sc.value());
	}

	@Test
	void testEqualsSameObject() {
		SortCode sc = new SortCode("12-34-56");
		assertTrue(sc.equals(sc));
	}

	@Test
	void testEqualsSameValues() {
		SortCode sc1 = new SortCode("12-34-56");
		SortCode sc2 = new SortCode("12-34-56");
		assertTrue(sc1.equals(sc2));
	}

	@Test
	void testEqualsNull() {
		SortCode sc = new SortCode("12-34-56");
		assertFalse(sc.equals(null));
	}

	@SuppressWarnings("unlikely-arg-type")
	@Test
	void testEqualsDifferentClass() {
		SortCode sc = new SortCode("12-34-56");
		assertFalse(sc.equals("12-34-56"));
	}

	@Test
	void testToString() {
		SortCode sc = new SortCode("12-34-56");
		assertEquals(sc.toString(), toString);
	}

	@Test
	void testBuildElement() {
		assertNotNull(document);
		Element testElement = sortcodeToBeTested.buildElement(document);
		assertNotNull(testElement);
		assertNotNull(testElement.getTextContent());
		assertEquals(SAMPLE_SORT_CODE, testElement.getTextContent());
	}

	@Test
	void testSortCodeWithElement() {
		assertNotNull(document);
		Element testElement = sortcodeToBeTested.buildElement(document);
		assertNotNull(testElement);
		assertNotNull(testElement.getTextContent());
		assertEquals(SAMPLE_SORT_CODE, testElement.getTextContent());
		SortCode newSortCode = new SortCode(testElement);
		assertEquals(SAMPLE_SORT_CODE, newSortCode.value());
	}

	@Test
	void testMissingSortCodeValue() {
		assertThrows(IllegalArgumentException.class, () -> {
			new SortCode((String) null);
		});
	}

	@Test
	void testMissingSortCode() {
		assertThrows(IllegalArgumentException.class, () -> {
			new SortCode((SortCode) null);
		});
	}

	@Test
	void testEmptySortCode() {
		assertThrows(IllegalArgumentException.class, () -> {
			new SortCode("");
		});
	}

	@Test
	void testBlankSortCode() {
		assertThrows(IllegalArgumentException.class, () -> {
			new SortCode(" ");
		});
	}

	@Test
	void testSortCodeMissingHyphens() {
		assertThrows(IllegalArgumentException.class, () -> {
			new SortCode("123456");
		});
	}

	@Test
	void testSortCodeMissingFirstHyphen() {
		assertThrows(IllegalArgumentException.class, () -> {
			new SortCode("1234-56");
		});
	}

	@Test
	void testSortCodeMissingSecondHyphen() {
		assertThrows(IllegalArgumentException.class, () -> {
			new SortCode("12-3456");
		});
	}

	@Test
	void testSortCodeTooLong() {
		assertThrows(IllegalArgumentException.class, () -> {
			new SortCode("12-34-567");
		});
	}

	@Test
	void testSortCodeTooManyHyphens() {
		assertThrows(IllegalArgumentException.class, () -> {
			new SortCode("12-34-56-89");
		});
	}

	@Test
	void testAlphaCharacter() {
		assertThrows(IllegalArgumentException.class, () -> {
			new SortCode("ab-cd-ef");
		});
	}

	@Test
	void testInvalidSeparatorCharacter() {
		assertThrows(IllegalArgumentException.class, () -> {
			new SortCode("12>34-56");
		});
	}

	@Test
	void testInvalidTagName() {
		Element element = ElementBuilder.build("test", "text", document);
		assertThrows(IllegalArgumentException.class, () -> {
			new SortCode(element);
		});

	}
}
