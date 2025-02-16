package application.model;

public class TotalMoney {

	private Money totalMoney;

	public TotalMoney() {
		totalMoney = new Money("0.00");
	}

	public TotalMoney add(Money money) {
		if (money == null) {
			throw new IllegalArgumentException("Total: money is null");
		}
		totalMoney = totalMoney.plus(money);
		return this;
	}

	public TotalMoney add(TotalMoney totalMoney) {
		this.add(totalMoney.money());
		return this;
	}

	public Money money() {
		return totalMoney;
	}

	public String cost() {
		return totalMoney.cost();
	}

	@Override
	public String toString() {
		return totalMoney.toString();
	}

}
