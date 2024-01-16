package application.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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

class PostCodeTest {

	private static final String LOWER_POST_CODE = "CW3 9SR";
	private static final String SAMPLE_POST_CODE = "CW3 9SS";
	private static final String HIGHER_POST_CODE = "CW3 9ST";
	private static final String FOUR_CHARACTER_POST_CODE = "WC2H 7LT";

	PostCode postcodeToBeTested = new PostCode(SAMPLE_POST_CODE);
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
	void testValidPostCode() {
		new PostCode(SAMPLE_POST_CODE);
	}

	@Test
	void testGetValue() {
		assertEquals(SAMPLE_POST_CODE, postcodeToBeTested.value());
	}

	@Test
	void testEqualsObject() {
		assertEquals(postcodeToBeTested, new PostCode(SAMPLE_POST_CODE));
		assertNotEquals(postcodeToBeTested, new PostCode(LOWER_POST_CODE));
		assertNotEquals(postcodeToBeTested, new PostCode(HIGHER_POST_CODE));
	}

	@Test
	void testToString() {
		assertEquals(SAMPLE_POST_CODE, postcodeToBeTested.toString());
	}

	@Test
	void testCompareTo() {
		assertTrue(postcodeToBeTested.compareTo(new PostCode(LOWER_POST_CODE)) > 0);
		assertTrue(postcodeToBeTested.compareTo(new PostCode(SAMPLE_POST_CODE)) == 0);
		assertTrue(postcodeToBeTested.compareTo(new PostCode(HIGHER_POST_CODE)) < 0);
	}

	@Test
	void testEqualsSameObject() {
		assertTrue(postcodeToBeTested.equals(postcodeToBeTested));
	}

	@Test
	void testEqualsSameValues() {
		PostCode postcodeToBeTested2 = new PostCode(SAMPLE_POST_CODE);
		assertTrue(postcodeToBeTested.equals(postcodeToBeTested2));
	}

	@Test
	void testEqualsNull() {
		assertFalse(postcodeToBeTested.equals(null));
	}

	@SuppressWarnings("unlikely-arg-type")
	@Test
	void testEqualsDifferentClass() {
		assertFalse(postcodeToBeTested.equals("BH 2"));
	}

	@Test
	void testCloneSortCode() {
		assertEquals(SAMPLE_POST_CODE, new PostCode(postcodeToBeTested).value());
	}

	@Test
	void testFourCharacterPostCode() {
		assertEquals(FOUR_CHARACTER_POST_CODE, new PostCode(FOUR_CHARACTER_POST_CODE).value());
	}

	@Test
	void testBuildElement() {
		assertNotNull(document);
		Element testElement = postcodeToBeTested.buildElement(document);
		assertNotNull(testElement);
		assertNotNull(testElement.getTextContent());
		assertEquals(SAMPLE_POST_CODE, testElement.getTextContent());
	}

	@Test
	void testPostCodeWithElement() {
		assertNotNull(document);
		Element testElement = postcodeToBeTested.buildElement(document);
		assertNotNull(testElement);
		assertNotNull(testElement.getTextContent());
		assertEquals(SAMPLE_POST_CODE, testElement.getTextContent());
		PostCode newPostCode = new PostCode(testElement);
		assertEquals(SAMPLE_POST_CODE, newPostCode.value());
	}

	@Test
	void testMissingPostCode() {
		assertThrows(IllegalArgumentException.class, () -> {
			new PostCode("");
		});
	}

	@Test
	void testBlankPostCode() {
		assertThrows(IllegalArgumentException.class, () -> {
			new PostCode(" ");
		});
	}

	@Test
	void testIllegalSymbolPostCode() {
		assertThrows(IllegalArgumentException.class, () -> {
			new PostCode("CW3-9ST");
		});
	}

	@Test
	void testNonDigitPostCode() {
		assertThrows(IllegalArgumentException.class, () -> {
			new PostCode("CW3 AST");
		});
	}

	@Test
	void testMissingLstValuePostCode() {
		assertThrows(IllegalArgumentException.class, () -> {
			new PostCode("CW3");
		});
	}

	@Test
	void testTooManyDigitsPostCode() {
		assertThrows(IllegalArgumentException.class, () -> {
			new PostCode("CW3 99ST");
		});
	}

	@Test
	void testTooManyValuesPostCode() {
		assertThrows(IllegalArgumentException.class, () -> {
			new PostCode("CW3 9ST 9ST");
		});
	}

	@Test
	void testNullPostCode() {
		assertThrows(IllegalArgumentException.class, () -> {
			PostCode missing = null;
			new PostCode(missing);
		});
	}

	@Test
	void testNullStringPostCode() {
		assertThrows(IllegalArgumentException.class, () -> {
			String missing = null;
			new PostCode(missing);
		});
	}

	@Test
	void testBuildElementNullDocument() {
		assertThrows(IllegalArgumentException.class, () -> {
			postcodeToBeTested.buildElement(null);
		});
	}

	@Test
	void testPostCodeWithElementNullElement() {
		assertThrows(IllegalArgumentException.class, () -> {
			new PostCode((Element) null);
		});
	}

	@Test
	void testInvalidTagName() {
		Element element = ElementBuilder.build("test", "text", document);
		assertThrows(IllegalArgumentException.class, () -> {
			new PostCode(element);
		});

	}
}
