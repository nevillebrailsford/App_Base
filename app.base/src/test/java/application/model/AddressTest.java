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

class AddressTest {

	private static final PostCode POST_CODE = new PostCode("CW3 9ST");
	private static final String LINE1 = "99 The Street";
	private static final String LINE2 = "The Town";
	private static final String LINE3 = "The County";
	private static final int NUMBER_OF_LINES = 3;
	private String[] lines = new String[] { LINE1, LINE2, LINE3 };
	private static final String PRINTED_ADDRESS = "99 The Street, The Town, The County CW3 9ST";

	Address address = new Address(POST_CODE, lines);
	String ADDRESS_TEXT = "CW3 9ST99 The StreetThe TownThe County";
	String ADDRESS_READONLY = "99 The Street, The Town, The County CW3 9ST";
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
	void testAddressPostCodeStringArray() {
		new Address(POST_CODE, lines);
	}

	@Test
	void testAddressAddress() {
		new Address(address);
	}

	@Test
	void testGetPostCode() {
		assertEquals(POST_CODE, address.postCode());
	}

	@Test
	void testEqualsObject() {
		assertEquals(new Address(POST_CODE, lines), address);
	}

	@Test
	void testGetLinesOfCode() {
		assertEquals(NUMBER_OF_LINES, address.linesOfAddress().length);
		assertEquals(LINE1, address.linesOfAddress()[0]);
		assertEquals(LINE2, address.linesOfAddress()[1]);
		assertEquals(LINE3, address.linesOfAddress()[2]);
	}

	@Test
	void testToString() {
		assertEquals(PRINTED_ADDRESS, address.toString());
	}

	@Test
	void testEqualsToSameObject() {
		assertTrue(address.equals(address));
	}

	@Test
	void testEqualsToSameValues() {
		Address address2 = new Address(POST_CODE, lines);
		assertTrue(address.equals(address2));
	}

	@Test
	void testEqualsToNull() {
		assertFalse(address.equals(null));
	}

	@Test
	void testCompareToSameObject() {
		assertTrue(address.compareTo(address) == 0);
	}

	@Test
	void testCompareToSameValues() {
		Address address2 = new Address(POST_CODE, lines);
		assertTrue(address.compareTo(address2) == 0);
	}

	@SuppressWarnings("unlikely-arg-type")
	@Test
	void testEqualsToDifferentClass() {
		assertFalse(address.equals("address"));
	}

	@Test
	void testBuildElement() {
		assertNotNull(document);
		Element testElement = address.buildElement(document);
		assertNotNull(testElement);
		assertNotNull(testElement.getTextContent());
		assertEquals(ADDRESS_TEXT, testElement.getTextContent());
	}

	@Test
	void testAddressWithElement() {
		assertNotNull(document);
		Element testElement = address.buildElement(document);
		assertNotNull(testElement);
		assertNotNull(testElement.getTextContent());
		Address testAddress = new Address(testElement);
		assertNotNull(testAddress);
		assertEquals(POST_CODE, testAddress.postCode());
	}

	@Test
	void testFullAddressProperty() {
		assertEquals(ADDRESS_READONLY, address.fullAddress());
	}

	@Test
	void testNullLinesOfAddress() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Address(POST_CODE, null);
		});
	}

	@Test
	void testTooManyLinesOfAddress() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Address(POST_CODE, new String[] { "line 1", "line 2", "line 3", "line 4" });
		});
	}

	@Test
	void testNullPostCode() {
		assertThrows(IllegalArgumentException.class, () -> {
			PostCode missing = null;
			new Address(missing, lines);
		});
	}

	@Test
	void testEmptyLinesOfAddress() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Address(POST_CODE, new String[0]);
		});
	}

	@Test
	void testNullAddress() {
		assertThrows(IllegalArgumentException.class, () -> {
			Address missing = null;
			new Address(missing);
		});
	}

	@Test
	void testBuildElementNullDocument() {
		assertThrows(IllegalArgumentException.class, () -> {
			address.buildElement(null);
		});
	}

	@Test
	void testAddressWithNullElement() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Address((Element) null);
		});
	}

	@Test
	void testInvalidTagName() {
		Element element = ElementBuilder.build("test", "text", document);
		assertThrows(IllegalArgumentException.class, () -> {
			new Address(element);
		});
	}
}
