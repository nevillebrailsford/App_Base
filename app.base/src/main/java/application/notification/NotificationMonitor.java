package application.notification;

import java.io.PrintStream;

public class NotificationMonitor implements NotificationListener {
	private PrintStream out;

	public NotificationMonitor(PrintStream out) {
		if (out == null) {
			throw new IllegalArgumentException("NotificationMonitor: out was null");
		}
		this.out = out;
		NotificationCentre.addListener(this);
	}

	public void stop() {
		NotificationCentre.removeListener(this);
	}

	@Override
	public void notify(Notification notification) {
		out.println(notification.toString());
	}

}
