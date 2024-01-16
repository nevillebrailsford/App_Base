package application.timer;

import static org.junit.jupiter.api.Assertions.*;

import java.util.TimerTask;

import org.junit.jupiter.api.*;

import application.notification.*;

class TimerServiceTest {
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
		TimerService.instance().stop();
	}

	@Test
	void testTimerServiceInstance() {
		assertNotNull(TimerService.instance());
	}

	@Test
	void testTimerServiceStart() {
		TimerService.instance().start();
		assertTrue(TimerService.instance().started());
	}

	@Test
	void testTimerStop() {
		TimerService.instance().start();
		assertTrue(TimerService.instance().started());
		TimerService.instance().stop();
		assertFalse(TimerService.instance().started());
	}

	@Test
	void testNotificationWorks() throws InterruptedException {
		NotificationCentre.addListener(listener);
		assertFalse(notified);
		assertNull(notificationReceived);
		synchronized (waitForNotify) {
			TimerService.instance().start(1000, 1000);
			waitForNotify.wait();
		}
		assertTrue(notified);
		assertNotNull(notificationReceived);
		assertFalse(notificationReceived.subject().isEmpty());
	}

	@Test
	void testAddTimer() throws InterruptedException {
		int key = TimerService.instance().timer(new TimerTask() {
			@Override
			public void run() {
				synchronized (waitForNotify) {
					notified = true;
					waitForNotify.notifyAll();
				}
			}
		}, 100, 100);
		assertFalse(notified);
		synchronized (waitForNotify) {
			waitForNotify.wait();
		}
		assertTrue(notified);
		assertTrue(TimerService.instance().started(key));
		TimerService.instance().stop(key);
		assertFalse(TimerService.instance().started(key));
	}
}
