package application.timer;

import java.util.*;

import application.notification.*;

public class TimerService {
	private static TimerService instance = null;
	private Timer timer = null;

	private Map<Integer, Timer> timers = null;
	private int key = 1;

	private TimerService() {
		timers = new HashMap<>();
	}

	public synchronized static TimerService instance() {
		if (instance == null) {
			instance = new TimerService();
		}
		return instance;
	}

	public int timer(TimerTask task, long delay, long interval) {
		int result = key;
		Timer userTimer = new Timer();
		timers.put(key++, userTimer);
		userTimer.scheduleAtFixedRate(task, delay, interval);
		return result;
	}

	/**
	 * Main timer start for whole application. Other timers will be returned as an
	 * int that serves as a key to a particular timer.
	 * 
	 * @param times
	 */
	public void start(int... times) {
		timer = new Timer();
		long delay = 60000;
		long interval = 60000;
		if (times.length == 0) {
		} else {
			delay = times[0];
			if (times.length == 2) {
				interval = times[1];
			}
		}
		timer.scheduleAtFixedRate(new Ticker(), delay, interval);
	}

	public boolean started() {
		return timer != null;
	}

	public boolean started(int key) {
		return timers.get(key) != null;
	}

	public void stop(int key) {
		Timer timer = timers.get(key);
		if (timer != null) {
			timer.cancel();
		}
		timers.put(key, null);
	}

	public void stop() {
		if (timer != null) {
			timer.cancel();
		}
		timer = null;
		// make sure all user timers are stopped
		for (Timer userTimer : timers.values()) {
			if (userTimer != null) {
				userTimer.cancel();
			}
		}
	}

	private class Ticker extends TimerTask {

		@Override
		public void run() {
			Notification notification = new Notification(TimerNotificationType.Ticked, this, "timer");
			NotificationCentre.broadcast(notification);
		}

	}

}
