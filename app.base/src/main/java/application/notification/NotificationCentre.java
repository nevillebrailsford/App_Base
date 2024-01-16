package application.notification;

import java.util.*;

public class NotificationCentre {
	public static final Map<NotificationType, List<NotificationListener>> mapListeners = new HashMap<>();

	private static LinkedList<Notification> queue = new LinkedList<>();
	private static final int capacity = 3;

	private static Thread consumer = null;
	private static Object readyObject = new Object();
	private static Object threadStart = new Object();
	private static boolean keepRunning = false;

	private static enum AllNotifications implements NotificationType {
		All;

		@Override
		public String category() {
			return "all";
		}

	}

	public static void addListener(NotificationListener listener, NotificationType... notificationType) {
		synchronized (mapListeners) {
			if (notificationType.length == 0) {
				NotificationType all = AllNotifications.All;
				addToMapListeners(listener, all);
			} else {
				for (int i = 0; i < notificationType.length; i++) {
					addToMapListeners(listener, notificationType[i]);
				}
			}
		}
	}

	private static void addToMapListeners(NotificationListener listener, NotificationType notificationType) {
		List<NotificationListener> list = mapListeners.get(notificationType);
		if (list == null) {
			list = new ArrayList<>();
			mapListeners.put(notificationType, list);
		} else if (list.contains(listener)) {
			throw new IllegalArgumentException("NotificationCenter - listener already registered");
		}
		list.add(listener);

	}

	public static void removeListener(NotificationListener listener) {
		synchronized (mapListeners) {
			if (!removeFromMapListeners(listener)) {
				throw new IllegalArgumentException("NotificationCenter - listener not registered");
			}
		}
	}

	private static boolean removeFromMapListeners(NotificationListener listener) {
		boolean removed = false;
		for (NotificationType k : mapListeners.keySet()) {
			List<NotificationListener> list = mapListeners.get(k);
			if (list.contains(listener)) {
				list.remove(listener);
				removed = true;
			}
		}
		;
		return removed;
	}

	public static void broadcast(Notification notification) {
		if (consumer == null) {
			keepRunning = true;
			consumer = new Thread(() -> {
				try {
					consume();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
			consumer.start();
			synchronized (threadStart) {
				try {
					threadStart.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		synchronized (readyObject) {
			while (queue.size() == capacity) {
				try {
					readyObject.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			queue.add(notification);
			readyObject.notify();
		}
	}

	public static void clear() {
		mapListeners.clear();
	}

	public static void stop() {
		keepRunning = false;
		synchronized (readyObject) {
			readyObject.notify();
		}
		consumer = null;
	}

	private static void consume() throws InterruptedException {
		synchronized (threadStart) {
			threadStart.notify();
		}
		while (keepRunning) {
			synchronized (readyObject) {
				while (keepRunning && queue.size() == 0) {
					readyObject.wait();
				}
				if (keepRunning) {
					Notification notification = queue.removeFirst();
					readyObject.notify();
					List<NotificationListener> listeners = new ArrayList<>();
					synchronized (mapListeners) {
						if (mapListeners.get(AllNotifications.All) != null) {
							listeners.addAll(mapListeners.get(AllNotifications.All));
						}
						mapListeners.keySet().stream().filter(t -> t != AllNotifications.All)
								.filter(n -> n == notification.notificationType()).forEach(k -> {
									listeners.addAll(mapListeners.get(k));
								});
					}
					listeners.stream().forEach(listener -> {
						listener.notify(notification);
					});
				}
			}
		}
	}

}
