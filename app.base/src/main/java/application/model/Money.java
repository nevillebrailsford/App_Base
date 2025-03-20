package application.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class Money implements Comparable<Money> {

	private static RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;
	private static int DECIMAL_PLACES = 2;

	private BigDecimal amount = null;

	public Money(String amount) {
		if (amount == null) {
			throw new IllegalArgumentException("Money: amount is null");
		}
		if (amount.isEmpty()) {
			throw new IllegalArgumentException("Money: amount is blank");
		}
		try {
			this.amount = createRounded(amount);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Money: amount is not numeric");
		}
	}

	public Money(Money that) {
		if (that == null) {
			throw new IllegalArgumentException("Money: money is null");
		}
		this.amount = createRounded(that);
	}

	public Money(BigDecimal amount) {
		if (amount == null) {
			throw new IllegalArgumentException("Money: amount is null");
		}
		this.amount = createRounded(amount);
	}

	public Money(Element moneyElement) {
		if (moneyElement == null) {
			throw new IllegalArgumentException("Money: moneyElement is null");
		}
		if (!ElementChecker.verifyTag(moneyElement, AppXMLConstants.MONEY)) {
			throw new IllegalArgumentException("Money: moneyElement is not for Money");
		}
		this.amount = new BigDecimal(moneyElement.getTextContent());
	}

	public Element buildElement(Document document) {
		if (document == null) {
			throw new IllegalArgumentException("Money: document is null");
		}
		Element result = document.createElement(AppXMLConstants.MONEY);
		result.setTextContent(amount.toString());
		return result;
	}

	public static Money sum(Collection<Money> moneys) {
		return sum(moneys.toArray(new Money[0]));
	}

	public static Money sum(Money[] moneys) {
		Money sum = new Money("0.00");
		for (int i = 0; i < moneys.length; i++) {
			sum = sum.plus(moneys[i]);
		}
		return sum;
	}

	public static Money avg(Collection<Money> moneys) {
		return avg(moneys.toArray(new Money[0]));
	}

	public static Money avg(Money[] moneys) {
		Money sum = sum(moneys);
		BigDecimal avg = sum.amount().divide(new BigDecimal(moneys.length));
		return new Money(avg);
	}

	public static Money max(Money money1, Money money2) {
		return new Money(money1.amount().max(money2.amount()));
	}

	public static Money min(Money money1, Money money2) {
		return new Money(money1.amount().min(money2.amount()));
	}

	public static Money max(List<Money> moneys) {
		if (moneys == null) {
			throw new IllegalArgumentException("Money: moneys is null");
		}
		return max(moneys.toArray(new Money[0]));
	}

	public static Money max(Money[] moneys) {
		if (moneys == null) {
			throw new IllegalArgumentException("Money: moneys is null");
		}
		if (moneys.length == 0) {
			throw new IllegalArgumentException("Money: moneys is zero length");
		}
		Money max = moneys[0];
		for (int i = 1; i < moneys.length; i++) {
			max = max(moneys[i], max);
		}
		return max;
	}

	public static Money min(List<Money> moneys) {
		if (moneys == null) {
			throw new IllegalArgumentException("Money: moneys is null");
		}
		return min(moneys.toArray(new Money[0]));
	}

	public static Money min(Money[] moneys) {
		if (moneys == null) {
			throw new IllegalArgumentException("Money: moneys is null");
		}
		if (moneys.length == 0) {
			throw new IllegalArgumentException("Money: moneys is zero length");
		}
		Money min = moneys[0];
		for (int i = 1; i < moneys.length; i++) {
			min = min(moneys[i], min);
		}
		return min;
	}

	public Money plus(Money addition) {
		return plus(addition.amount());
	}

	public Money plus(BigDecimal addition) {
		return new Money(amount.add(addition));
	}

	public Money minus(Money addition) {
		return minus(addition.amount());
	}

	public Money minus(BigDecimal addition) {
		return new Money(amount.subtract(addition));
	}

	public Money times(int factor) {
		return new Money(amount.multiply(new BigDecimal(factor)));
	}

	public Money times(double factor) {
		return new Money(amount.multiply(new BigDecimal(Double.toString(factor))));
	}

	public Money negate() {
		return times(-1);
	}

	public Money abs() {
		return new Money(amount.abs());
	}

	public boolean isNegative() {
		return amount.compareTo(BigDecimal.ZERO) < 0;
	}

	private BigDecimal createRounded(Money amount) {
		return createRounded(amount.amount());
	}

	private BigDecimal createRounded(String amount) {
		BigDecimal result = null;
		DecimalFormat decimalFormat = new DecimalFormat("###,###,###.###");
		decimalFormat.setParseBigDecimal(true);
		try {
			result = ((BigDecimal) decimalFormat.parse(amount));
		} catch (ParseException e) {
			throw new NumberFormatException();
		}
		return result.setScale(DECIMAL_PLACES, ROUNDING_MODE);
	}

	private BigDecimal createRounded(BigDecimal amount) {
		return amount.setScale(DECIMAL_PLACES, ROUNDING_MODE);
	}

	private BigDecimal amount() {
		return amount;
	}

	@Override
	public int compareTo(Money that) {
		return amount.compareTo(that.amount());
	}

	@Override
	public String toString() {
		return amount.toPlainString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(amount);
	}

	@Override
	public boolean equals(Object that) {
		if (this == that)
			return true;
		if (that == null)
			return false;
		if (getClass() != that.getClass())
			return false;
		Money other = (Money) that;
		return Objects.equals(amount, other.amount());
	}

	public String cost() {
		return "£" + amount.toPlainString();
	}

	public static class Builder {
		private String amount = null;

		public Builder amount(String amount) {
			this.amount = amount;
			return this;
		}

		public Money build() {
			if (amount == null) {
				throw new IllegalArgumentException("Money: amount is null");
			}
			if (amount.isEmpty()) {
				throw new IllegalArgumentException("Money: amount is blank");
			}
			return new Money(amount);
		}
	}

}
