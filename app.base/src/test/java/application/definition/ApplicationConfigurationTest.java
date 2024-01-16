package application.definition;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ApplicationConfigurationTest {

	private static ApplicationDefinition test;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		test = new ApplicationDefinition("test");
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
		ApplicationConfiguration.clear();
	}

	@Test
	void testRegister() {
		ApplicationConfiguration.registerApplication(test, "root");
	}

	@Test
	void testApplication() {
		ApplicationConfiguration.registerApplication(test, "root");
		assertEquals(test, ApplicationConfiguration.applicationDefinition());
	}

	@Test
	void testRootDirectory() {
		ApplicationConfiguration.registerApplication(test, "root");
		assertNotNull(ApplicationConfiguration.rootDirectory());
		assertEquals("root", ApplicationConfiguration.rootDirectory().getName());
	}

	@Test
	void testNullAppParameter() {
		Exception exc = assertThrows(IllegalArgumentException.class, () -> {
			ApplicationConfiguration.registerApplication(null, "root");
		});
		assertEquals("ApplicationConfiguration - application is null", exc.getMessage());
	}

	@Test
	void testNullDirParameter() {
		Exception exc = assertThrows(IllegalArgumentException.class, () -> {
			ApplicationConfiguration.registerApplication(test, null);
		});
		assertEquals("ApplicationConfiguration - rootDirectory is null", exc.getMessage());
	}

	@Test
	void testEmptyDirParameter() {
		Exception exc = assertThrows(IllegalArgumentException.class, () -> {
			ApplicationConfiguration.registerApplication(test, "");
		});
		assertEquals("ApplicationConfiguration - rootDirectory is empty", exc.getMessage());
	}

	@Test
	void testBlankDirParameter() {
		Exception exc = assertThrows(IllegalArgumentException.class, () -> {
			ApplicationConfiguration.registerApplication(test, "   ");
		});
		assertEquals("ApplicationConfiguration - rootDirectory is empty", exc.getMessage());
	}

	@Test
	void testDuplicateRegister() {
		Exception exc = assertThrows(IllegalStateException.class, () -> {
			ApplicationConfiguration.registerApplication(test, "root");
			ApplicationConfiguration.registerApplication(test, "root");
		});
		assertEquals("ApplicationConfiguration - registeredApplication is not null", exc.getMessage());

	}

	@Test
	void testNeverRegistered() {
		Exception exc = assertThrows(IllegalStateException.class, () -> {
			ApplicationConfiguration.applicationDefinition();
		});
		assertEquals("ApplicationConfiguration - registeredApplication is null", exc.getMessage());
	}

}
