package application.definition;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.logging.Level;

import org.junit.jupiter.api.Test;

class ApplicationDefinitionTest {

	@Test
	void testValidConstructor() {
		new ApplicationDefinition("valid");
	}

	@Test
	void testApplicationName() {
		ApplicationDefinition test = new ApplicationDefinition("test");
		assertEquals("test", test.applicationName());
	}

	@Test
	void testLoggerName() {
		ApplicationDefinition test = new ApplicationDefinition("logger");
		assertEquals("logger", test.loggerName());
	}

	@Test
	void testLoggerDirectory() {
		ApplicationDefinition test = new ApplicationDefinition("test");
		ApplicationConfiguration.registerApplication(test, System.getProperty("user.home"));
		File f = new File(System.getProperty("user.home"));
		File f2 = new File(f, "test");
		File f3 = new File(f2, "logs");
		assertEquals(f3.getAbsolutePath(), test.loggerDirectory());
		ApplicationConfiguration.clear();
	}

	@Test
	void testLoggerFile() {
		ApplicationDefinition test = new ApplicationDefinition("test");
		ApplicationConfiguration.registerApplication(test, System.getProperty("user.home"));
		File f = new File(System.getProperty("user.home"));
		File f2 = new File(f, "test");
		File f3 = new File(f2, "logs");
		File f4 = new File(f3, "test.trace");
		assertEquals(f4.getAbsolutePath(), test.loggerFile());
		ApplicationConfiguration.clear();
	}

	@Test
	void testAuditDirectory() {
		ApplicationDefinition test = new ApplicationDefinition("test");
		ApplicationConfiguration.registerApplication(test, System.getProperty("user.home"));
		File f = new File(System.getProperty("user.home"));
		File f2 = new File(f, "test");
		File f3 = new File(f2, "audits");
		assertEquals(f3.getAbsolutePath(), test.auditDirectory());
		ApplicationConfiguration.clear();
	}

	@Test
	void testAuditFile() {
		ApplicationDefinition test = new ApplicationDefinition("test");
		ApplicationConfiguration.registerApplication(test, System.getProperty("user.home"));
		File f = new File(System.getProperty("user.home"));
		File f2 = new File(f, "test");
		File f3 = new File(f2, "audits");
		File f4 = new File(f3, "test.audit");
		assertEquals(f4.getAbsolutePath(), test.auditFile());
		ApplicationConfiguration.clear();
	}

	@Test
	void testArchiveDirectory() {
		ApplicationDefinition test = new ApplicationDefinition("test");
		ApplicationConfiguration.registerApplication(test, System.getProperty("user.home"));
		File f = new File(System.getProperty("user.home"));
		File f2 = new File(f, "test");
		File f3 = new File(f2, "archives");
		assertEquals(f3.getAbsolutePath(), test.archiveDirectory());
		ApplicationConfiguration.clear();
	}

	@Test
	void testArchiveFile() {
		ApplicationDefinition test = new ApplicationDefinition("test");
		ApplicationConfiguration.registerApplication(test, System.getProperty("user.home"));
		File f = new File(System.getProperty("user.home"));
		File f2 = new File(f, "test");
		File f3 = new File(f2, "archives");
		File f4 = new File(f3, "test.archive-");
		String prefix = f4.getAbsolutePath();
		String archiveFileName = test.archiveFile();
		assertTrue(archiveFileName.startsWith(f4.getAbsolutePath()));
		String pattern = "[0-9]{4}[-][0-9]{2}[-][0-9]{2}[-][0-9]{2}[-][0-9]{2}[-][0-9]{2}";
		assertTrue(archiveFileName.substring(prefix.length()).matches(pattern));
		ApplicationConfiguration.clear();
	}

	@Test
	void testLevel() {
		ApplicationDefinition test = new ApplicationDefinition("test");
		assertEquals(Level.ALL, test.level());
	}

	@Test
	void testChangedLevel() {
		ApplicationDefinition test = new ApplicationDefinition("test") {
			public Level level() {
				return Level.OFF;
			}
		};
		assertEquals(Level.OFF, test.level());
	}

	@Test
	void testVersion() {
		ApplicationDefinition test = new ApplicationDefinition("test");
		assertEquals("Could not be determined", test.version());
	}

	@Test
	void testChangedVersion() {
		ApplicationDefinition test = new ApplicationDefinition("test") {
			public String version() {
				return "2.0.1";
			}
		};
		assertEquals("2.0.1", test.version());
	}

	@Test
	void testNullArgumentConstructor() {
		Exception exc = assertThrows(IllegalArgumentException.class, () -> {
			new ApplicationDefinition(null);
		});
		assertEquals("ApplicationDefinition - applicationName is null", exc.getMessage());
	}

	@Test
	void testEmptyArgumentConstructor() {
		Exception exc = assertThrows(IllegalArgumentException.class, () -> {
			new ApplicationDefinition("");
		});
		assertEquals("ApplicationDefinition - applicationName is empty", exc.getMessage());
	}
}
