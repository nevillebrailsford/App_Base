package application.model;

public class TotalMoney {

	private Money totalMoney;

	public TotalMoney() {
		totalMoney = new Money("0.00");
	}

	public void add(Money money) {
		if (money == null) {
			throw new IllegalArgumentException("Total: money is null");
		}
		totalMoney = totalMoney.plus(money);
	}

	public String cost() {
		return totalMoney.cost();
	}

	@Override
	public String toString() {
		return totalMoney.toString();
	}

}
