package application.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class TotalMoneyTest {

	private static final String SAMPLE_MONEY = "100.99";
	private static final String SAMPLE_COST = "£100.99";
	private static final String TOTAL_COST = "£302.97";
	private static final String INITIAL_MONEY = "0.00";
	private static final String INITIAL_COST = "£0.00";
	private Money moneyToBeTested = new Money(SAMPLE_MONEY);
	private TotalMoney totalMoney = new TotalMoney();

	@Test
	void testInitialValue() {
		assertEquals(INITIAL_MONEY, totalMoney.toString());
	}

	@Test
	void testInitialCost() {
		assertEquals(INITIAL_COST, totalMoney.cost());
	}

	@Test
	void testAddMoney() {
		totalMoney.add(moneyToBeTested);
		assertEquals(SAMPLE_COST, totalMoney.cost());
	}

	@Test
	void testAddMoreMoney() {
		totalMoney.add(moneyToBeTested);
		totalMoney.add(moneyToBeTested);
		totalMoney.add(moneyToBeTested);
		assertEquals(TOTAL_COST, totalMoney.cost());
	}

	@Test
	void testAddNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			totalMoney.add(null);
		});
	}

}
