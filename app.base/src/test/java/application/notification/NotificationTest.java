package application.notification;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

class NotificationTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testCreation() {
		assertNotNull(new Notification(TestNotificationType.Test, this, "test"));
	}

	@Test
	void testCreationNoSubject() {
		assertNotNull(new Notification(TestNotificationType.Test, this));
	}

	@Test
	void testToString() {
		Notification not = new Notification(TestNotificationType.Test, this, "test");
		assertEquals("TestNotificationType.Test testcategory NotificationTest test", not.toString());
	}

	@Test
	void testCategory() {
		Notification not = new Notification(TestNotificationType.Test, this, "test");
		assertEquals("testcategory", not.notificationType().category());
	}

	@Test
	void testCreationTooManySubject() {
		Exception exc = assertThrows(IllegalArgumentException.class, () -> {
			new Notification(TestNotificationType.Test, this, "one", "two");
		});
		assertEquals("Notification - too many subject objects", exc.getMessage());
	}

	@Test
	void testCreationMissingType() {
		Exception exc = assertThrows(IllegalArgumentException.class, () -> {
			new Notification(null, this, "test");
		});
		assertEquals("Notification - notification is null", exc.getMessage());
	}

	@Test
	void testCreationMissingSource() {
		Exception exc = assertThrows(IllegalArgumentException.class, () -> {
			new Notification(TestNotificationType.Test, null, "test");
		});
		assertEquals("Notification - source is null", exc.getMessage());
	}

	@Test
	void testNotificationType() {
		Notification notification = new Notification(TestNotificationType.Test, this, "test");
		assertEquals(TestNotificationType.Test, notification.notificationType());
	}

	@Test
	void testNotificationSource() {
		Notification notification = new Notification(TestNotificationType.Test, this, "test");
		assertEquals(this, notification.source());
	}

	@Test
	void testNotificationSubject() {
		Notification notification = new Notification(TestNotificationType.Test, this, "this");
		assertTrue(notification.subject().isPresent());
		assertEquals("this", notification.subject().get());
	}

	@Test
	void testNotificationSubjectEmpty() {
		Notification notification = new Notification(TestNotificationType.Test, this);
		assertTrue(notification.subject().isEmpty());
	}
}
