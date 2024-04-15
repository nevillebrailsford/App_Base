package application.animation;

import java.time.LocalDateTime;

public abstract class GApplicationDate extends GApplicationStrings {
	protected long startTime;

	public int year() {
		return LocalDateTime.now().getYear();
	}

	public int month() {
		return LocalDateTime.now().getMonthValue();
	}

	public int day() {
		return LocalDateTime.now().getDayOfMonth();
	}

	public int hour() {
		return LocalDateTime.now().getHour();
	}

	public int minute() {
		return LocalDateTime.now().getMinute();
	}

	public int second() {
		return LocalDateTime.now().getSecond();
	}

	public int millis() {
		return toInt((startTime - System.currentTimeMillis()));
	}

}
