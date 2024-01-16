package application.notification;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

class NotificationCentreTest {
	private Object waitForNotify = new Object();
	private boolean notified = false;
	private Notification notificationReceived = null;

	NotificationListener listener = new NotificationListener() {
		@Override
		public void notify(Notification notification) {
			notified = true;
			notificationReceived = notification;
			synchronized (waitForNotify) {
				waitForNotify.notifyAll();
			}
		}
	};

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		NotificationCentre.clear();
		notified = false;
		notificationReceived = null;
	}

	@AfterEach
	void tearDown() throws Exception {
		NotificationCentre.clear();
		NotificationCentre.stop();
		notified = false;
		notificationReceived = null;
	}

	@Test
	void testAddListener() {
		NotificationCentre.addListener(listener);
	}

	@Test
	void testAddListenerWithType() {
		NotificationCentre.addListener(listener, UTNotificationType.Ticked);
	}

	@Test
	void testAddDuplicateListener() {
		NotificationCentre.addListener(listener);
		Exception exc = assertThrows(IllegalArgumentException.class, () -> {
			NotificationCentre.addListener(listener);
		});
		assertEquals("NotificationCenter - listener already registered", exc.getMessage());
	}

	@Test
	void testAddDuplicateListenerWithType() {
		NotificationCentre.addListener(listener, UTNotificationType.Ticked);
		Exception exc = assertThrows(IllegalArgumentException.class, () -> {
			NotificationCentre.addListener(listener, UTNotificationType.Ticked);
		});
		assertEquals("NotificationCenter - listener already registered", exc.getMessage());
	}

	@Test
	void testRemoveListener() {
		NotificationCentre.addListener(listener);
		NotificationCentre.removeListener(listener);
	}

	@Test
	void testRemoveListenerWithType() {
		NotificationCentre.addListener(listener, UTNotificationType.Ticked);
		NotificationCentre.removeListener(listener);
	}

	@Test
	void testRemoveMissingListener() {
		Exception exc = assertThrows(IllegalArgumentException.class, () -> {
			NotificationCentre.removeListener(listener);
		});
		assertEquals("NotificationCenter - listener not registered", exc.getMessage());
	}

	@Test
	void testBroadcastNoSubject() throws InterruptedException {
		NotificationCentre.addListener(listener);
		Notification notification = new Notification(TestNotificationType.Test, this);
		assertFalse(notified);
		assertNull(notificationReceived);
		synchronized (waitForNotify) {
			NotificationCentre.broadcast(notification);
			waitForNotify.wait();
		}
		assertTrue(notified);
		assertNotNull(notificationReceived);
		assertTrue(notificationReceived.subject().isEmpty());
	}

	@Test
	void testBroadcastNoSubjectWithType() throws InterruptedException {
		NotificationCentre.addListener(listener, TestNotificationType.Test);
		Notification notification = new Notification(TestNotificationType.Test, this);
		assertFalse(notified);
		assertNull(notificationReceived);
		synchronized (waitForNotify) {
			NotificationCentre.broadcast(notification);
			waitForNotify.wait();
		}
		assertTrue(notified);
		assertNotNull(notificationReceived);
		assertTrue(notificationReceived.subject().isEmpty());
	}

	@Test
	void testBroadcastWithSubject() throws InterruptedException {
		NotificationCentre.addListener(listener);
		Notification notification = new Notification(TestNotificationType.Test, this, "test");
		assertFalse(notified);
		assertNull(notificationReceived);
		synchronized (waitForNotify) {
			NotificationCentre.broadcast(notification);
			waitForNotify.wait();
		}
		assertTrue(notified);
		assertNotNull(notificationReceived);
		assertTrue(notificationReceived.subject().isPresent());
		assertEquals("test", notification.subject().get());
	}

	@Test
	void testBroadcastWithSubjectWithType() throws InterruptedException {
		NotificationCentre.addListener(listener, TestNotificationType.Test);
		Notification notification = new Notification(TestNotificationType.Test, this, "test");
		assertFalse(notified);
		assertNull(notificationReceived);
		synchronized (waitForNotify) {
			NotificationCentre.broadcast(notification);
			waitForNotify.wait();
		}
		assertTrue(notified);
		assertNotNull(notificationReceived);
		assertTrue(notificationReceived.subject().isPresent());
		assertEquals("test", notification.subject().get());
	}

}
