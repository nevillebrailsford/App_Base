package application.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

class MoneyTest {
	private static final String SAMPLE_MONEY = "1000.99";
	private static final String SAMPLE_COST = "£1,000.99";
	private Money moneyToBeTested = new Money(SAMPLE_MONEY);
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
	void testValidStringConstructor() {
		assertNotNull(new Money("10.00"));
	}

	@Test
	void testValidNumericStringWithComma() {
		assertEquals("9999.99", new Money("9,999.99").toString());
	}

	@Test
	void testValidNumericStringWithoutComma() {
		assertEquals("9999.99", new Money("9999.99").toString());
	}

	@Test
	void testValidNumericStringWithCommaOneDigit() {
		assertEquals("9999.90", new Money("9,999.9").toString());
	}

	@Test
	void testValidNumericStringWithoutCommaOneDigit() {
		assertEquals("9999.90", new Money("9999.9").toString());
	}

	@Test
	void testValidBuilder() {
		assertNotNull(new Money.Builder().amount("10.00").build());
	}

	@Test
	void testMaxTwoValues() {
		assertEquals(new Money("22.00"), Money.max(new Money("10.00"), new Money("22.00")));
	}

	@Test
	void testMinTwoValues() {
		assertEquals(new Money("10.00"), Money.min(new Money("10.00"), new Money("22.00")));
	}

	@Test
	void testMaxArrayOneElement() {
		Money[] monies = new Money[] { new Money("9.00") };
		assertEquals(new Money("9.00"), Money.max(monies));
	}

	@Test
	void testMaxArray() {
		Money[] monies = new Money[] { new Money("1.00"), new Money("2.00"), new Money("9.00"), new Money("8.00") };
		assertEquals(new Money("9.00"), Money.max(monies));
	}

	@Test
	void testMinArray() {
		Money[] monies = new Money[] { new Money("1.00"), new Money("2.00"), new Money("9.00"), new Money("8.00") };
		assertEquals(new Money("1.00"), Money.min(monies));
	}

	@Test
	void testMaxList() {
		ArrayList<Money> monies = new ArrayList<>();
		monies.add(new Money("1.00"));
		monies.add(new Money("2.00"));
		monies.add(new Money("9.00"));
		monies.add(new Money("8.00"));
		assertEquals(new Money("9.00"), Money.max(monies));
	}

	@Test
	void testMinList() {
		ArrayList<Money> monies = new ArrayList<>();
		monies.add(new Money("1.00"));
		monies.add(new Money("2.00"));
		monies.add(new Money("9.00"));
		monies.add(new Money("8.00"));
		assertEquals(new Money("1.00"), Money.min(monies));
	}

	@Test
	void testAvgArray() {
		Money[] monies = new Money[] { new Money("1.00"), new Money("2.00"), new Money("9.00"), new Money("8.00"),
				new Money("11.11") };
		assertEquals(new Money("6.22"), Money.avg(monies));
	}

	@Test
	void testAvgList() {
		ArrayList<Money> monies = new ArrayList<>();
		monies.add(new Money("1.00"));
		monies.add(new Money("2.00"));
		monies.add(new Money("9.00"));
		monies.add(new Money("8.00"));
		monies.add(new Money("11.11"));
		assertEquals(new Money("6.22"), Money.avg(monies));
	}

	@Test
	void testValidMoneyConstructor() {
		Money money1 = new Money("10.00");
		assertNotNull(new Money(money1));
	}

	@Test
	void testValidBigDecimalConstructor() {
		BigDecimal amount = new BigDecimal("10.00");
		assertNotNull(new Money(amount));
	}

	@Test
	void testValidConstructorRounding() {
		Money money1 = new Money("10.0051");
		assertEquals("10.01", money1.toString());
		Money money2 = new Money("10.0050");
		assertEquals("10.00", money2.toString());
	}

	@Test
	void testCompareToSameObject() {
		Money money1 = new Money("20.00");
		assertEquals(0, money1.compareTo(money1));
	}

	@Test
	void testCompareToSameAmount() {
		Money money1 = new Money("20.00");
		Money money2 = new Money("20.00");
		assertEquals(0, money1.compareTo(money2));
		assertEquals(0, money2.compareTo(money1));
	}

	@Test
	void testCompareToDifferingAmounts() {
		Money money1 = new Money("20.00");
		Money money2 = new Money("20.01");
		assertTrue(money1.compareTo(money2) < 0);
		assertTrue(money2.compareTo(money1) > 0);
	}

	@Test
	void testEqualsSameObject() {
		Money money1 = new Money("20.00");
		assertTrue(money1.equals(money1));
	}

	@Test
	void testEqualsNull() {
		Money money1 = new Money("20.00");
		assertFalse(money1.equals(null));
	}

	@Test
	void testIsZero() {
		Money money1 = new Money("0.00");
		assertTrue(money1.isZero());
	}

	@Test
	void testZero() {
		Money money1 = Money.zero();
		assertNotNull(money1);
		Money money2 = new Money("0.00");
		assertEquals(money1, money2);
	}

	@Test
	void testIsPositive() {
		Money money1 = new Money("0.00");
		assertTrue(money1.isPositive());
		money1 = new Money("0.01");
		assertTrue(money1.isPositive());
	}

	@Test
	void testIsNegative() {
		Money money1 = new Money("0.01");
		Money money2 = new Money("0.00");
		Money money3 = money2.minus(money1);
		assertTrue(money3.isNegative());
	}

	@SuppressWarnings("unlikely-arg-type")
	@Test
	void testEqualsDifferentClass() {
		Money money1 = new Money("20.00");
		assertFalse(money1.equals("20.00"));
	}

	@Test
	void testHashCodeSameObject() {
		Money money1 = new Money("20.00");
		assertEquals(money1.hashCode(), money1.hashCode());
	}

	@Test
	void testHashCodeSameValues() {
		Money money1 = new Money("20.00");
		Money money2 = new Money("20.00");
		assertEquals(money1.hashCode(), money2.hashCode());
	}

	@Test
	void testEqualsSameAmount() {
		Money money1 = new Money("20.00");
		Money money2 = new Money("20.00");
		assertTrue(money1.equals(money2));
		assertTrue(money2.equals(money1));
	}

	@Test
	void testEqualsDifferingAmounts() {
		Money money1 = new Money("20.00");
		Money money2 = new Money("20.01");
		assertFalse(money1.equals(money2));
		assertFalse(money2.equals(money1));
	}

	@Test
	void testBigDecimalAddition() {
		Money money1 = new Money("10.00");
		BigDecimal amount = new BigDecimal("10.00");
		Money money3 = new Money("20.00");
		Money money4 = money1.plus(amount);
		assertTrue(money3.equals(money4));
	}

	@Test
	void testMoneyAddition() {
		Money money1 = new Money("10.00");
		Money money2 = new Money("10.00");
		Money money3 = new Money("20.00");
		Money money4 = money1.plus(money2);
		assertTrue(money3.equals(money4));
	}

	@Test
	void testBigDecimalSubtraction() {
		Money money1 = new Money("15.00");
		BigDecimal amount = new BigDecimal("10.00");
		Money money3 = new Money("5.00");
		Money money4 = money1.minus(amount);
		assertTrue(money3.equals(money4));
	}

	@Test
	void testMoneySubtraction() {
		Money money1 = new Money("10.00");
		Money money2 = new Money("10.00");
		Money money3 = new Money("0.00");
		Money money4 = money1.minus(money2);
		assertTrue(money3.equals(money4));
	}

	@Test
	void testIntegerMultiplication() {
		Money money1 = new Money("15.00");
		int factor = 2;
		Money money2 = money1.times(factor);
		Money money3 = new Money("30.00");
		assertTrue(money2.equals(money3));
	}

	@Test
	void testDoubleMultiplication() {
		Money money1 = new Money("10.00");
		double factor = 2.5;
		Money money2 = money1.times(factor);
		Money money3 = new Money("25.00");
		assertTrue(money2.equals(money3));
	}

	@Test
	void testNegate() {
		Money money1 = new Money("100.00");
		Money money2 = new Money("-100.00");
		assertEquals(money2, money1.negate());
		assertEquals(money1, money2.negate());
	}

	@Test
	void testSumOfACollection() {
		ArrayList<Money> moneys = new ArrayList<>();
		moneys.add(new Money("12.34"));
		moneys.add(new Money("56.78"));
		moneys.add(new Money("99.99"));
		Money sum = Money.sum(moneys);
		assertEquals("169.11", sum.toString());
	}

	@Test
	void testBuildElement() {
		assertNotNull(document);
		Element testElement = moneyToBeTested.buildElement(document);
		assertNotNull(testElement);
		assertNotNull(testElement.getTextContent());
		assertEquals(SAMPLE_MONEY, testElement.getTextContent());
	}

	@Test
	void testMoneyWithElement() {
		assertNotNull(document);
		Element testElement = moneyToBeTested.buildElement(document);
		assertNotNull(testElement);
		assertNotNull(testElement.getTextContent());
		assertEquals(SAMPLE_MONEY, testElement.getTextContent());
		Money newMoney = new Money(testElement);
		assertEquals(SAMPLE_MONEY, newMoney.toString());
	}

	@Test
	void testCost() {
		assertEquals(SAMPLE_COST, moneyToBeTested.cost());
	}

	@Test
	void testNegative() {
		Money testMoney = new Money("1.00");
		assertEquals(false, testMoney.isNegative());
		testMoney = testMoney.negate();
		assertEquals(true, testMoney.isNegative());
		testMoney = new Money("0.00");
		assertEquals(false, testMoney.isNegative());
	}

	@Test
	void testAbs() {
		Money testMoney = new Money("1.00");
		assertEquals(new Money("1.00"), testMoney.abs());
		Money negMoney = testMoney.negate();
		assertEquals(new Money("1.00"), negMoney.abs());
	}

	@Test
	void testNullString() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Money((String) null);
		});
	}

	@Test
	void testEmptyString() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Money("");
		});
	}

	@Test
	void testBlankString() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Money("    ");
		});
	}

	@Test
	void testInvalidString() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Money("aa.aa");
		});
	}

	@Test
	void testValidNumericStringWithoutCommaThreeDigits() {
		assertNotEquals("9999.99", new Money("9,999.999").toString());
	}

	@Test
	void testBuilderMissingAmount() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Money.Builder().build();
		});
	}

	@Test
	void testBuilderNullAmount() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Money.Builder().amount(null).build();
		});
	}

	@Test
	void testBuilderEmptyAmount() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Money.Builder().amount("").build();
		});
	}

	@Test
	void testBuilderBlankAmount() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Money.Builder().amount("   ").build();
		});
	}

	@Test
	void testBuilderInvalidAmount() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Money.Builder().amount("aa.aa").build();
		});
	}

	@Test
	void testNullMoney() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Money((Money) null);
		});
	}

	@Test
	void testNullBigDecimal() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Money((BigDecimal) null);
		});
	}

	@Test
	void testInvalidTagName() {
		Element element = ElementBuilder.build("test", "text", document);
		assertThrows(IllegalArgumentException.class, () -> {
			new Address(element);
		});

	}

	@Test
	void testMaxNullArray() {
		assertThrows(IllegalArgumentException.class, () -> {
			Money[] monies = null;
			Money.max(monies);
		});
	}

	@Test
	void testMaxZeroArray() {
		assertThrows(IllegalArgumentException.class, () -> {
			Money[] monies = new Money[0];
			Money.max(monies);
		});
	}

	@Test
	void testMaxNullList() {
		assertThrows(IllegalArgumentException.class, () -> {
			ArrayList<Money> monies = null;
			Money.max(monies);
		});
	}

	@Test
	void testMinNullArray() {
		assertThrows(IllegalArgumentException.class, () -> {
			Money[] monies = null;
			Money.min(monies);
		});
	}

	@Test
	void testMinZeroArray() {
		assertThrows(IllegalArgumentException.class, () -> {
			Money[] monies = new Money[0];
			Money.min(monies);
		});
	}

	@Test
	void testMinNullList() {
		assertThrows(IllegalArgumentException.class, () -> {
			ArrayList<Money> monies = null;
			Money.min(monies);
		});
	}
}
