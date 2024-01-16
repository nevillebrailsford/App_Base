package application.logging;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.logging.Level;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import application.definition.ApplicationConfiguration;
import application.definition.ApplicationDefinition;

class LogConfigurerTest {

	@TempDir
	File rootDirectory;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		ApplicationDefinition app = new ApplicationDefinition("test");
		ApplicationConfiguration.registerApplication(app, rootDirectory.getAbsolutePath());
		ApplicationConfiguration.clear();
	}

	@AfterEach
	void tearDown() throws Exception {
		ApplicationDefinition app = new ApplicationDefinition("test");
		ApplicationConfiguration.registerApplication(app, rootDirectory.getAbsolutePath());
		ApplicationConfiguration.clear();
	}

	@Test
	void testsetup() {
		ApplicationDefinition app = new ApplicationDefinition("test");
		ApplicationConfiguration.registerApplication(app, rootDirectory.getAbsolutePath());
		File f = new File(app.loggerDirectory());
		assertFalse(f.exists());
		LogConfigurer.setUp();
		assertTrue(f.exists());
		LogConfigurer.shutdown();
		ApplicationConfiguration.clear();
	}

	@Test
	void testChangeLevel() {
		ApplicationDefinition app = new ApplicationDefinition("test");
		ApplicationConfiguration.registerApplication(app, rootDirectory.getAbsolutePath());
		File f = new File(app.loggerDirectory());
		assertFalse(f.exists());
		LogConfigurer.setUp();
		assertTrue(f.exists());
		LogConfigurer.changeLevel(Level.OFF);
		LogConfigurer.shutdown();
		ApplicationConfiguration.clear();
	}

	@Test
	void testChangeLevelNull() {
		ApplicationDefinition app = new ApplicationDefinition("test");
		ApplicationConfiguration.registerApplication(app, rootDirectory.getAbsolutePath());
		File f = new File(app.loggerDirectory());
		assertFalse(f.exists());
		LogConfigurer.setUp();
		assertTrue(f.exists());
		Exception exc = assertThrows(IllegalArgumentException.class, () -> {
			LogConfigurer.changeLevel(null);
		});
		assertEquals("LogConfigurer - level was null", exc.getMessage());
		LogConfigurer.shutdown();
		ApplicationConfiguration.clear();
	}

	@Test
	void testSetLevelNoSetup() {
		Exception exc = assertThrows(IllegalStateException.class, () -> {
			LogConfigurer.changeLevel(Level.ALL);
		});
		assertEquals("LogConfigurer - setup has not been called", exc.getMessage());
	}

	@Test
	void testShutdownlNoSetup() {
		Exception exc = assertThrows(IllegalStateException.class, () -> {
			LogConfigurer.shutdown();
		});
		assertEquals("LogConfigurer - setup has not been called", exc.getMessage());
	}

	@Test
	void testNoRegistration() {
		Exception exc = assertThrows(IllegalStateException.class, () -> {
			LogConfigurer.setUp();
		});
		assertEquals("ApplicationConfiguration - registeredApplication is null", exc.getMessage());
	}
}
