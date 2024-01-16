package application.logging;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import application.definition.ApplicationConfiguration;
import application.definition.ApplicationDefinition;

class LogFormatterTest {

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
	}

	@AfterEach
	void tearDown() throws Exception {
		ApplicationConfiguration.clear();
	}

	@Test
	void test() {
		ApplicationDefinition applicationDefinition = new ApplicationDefinition("test");
		ApplicationConfiguration.registerApplication(applicationDefinition, rootDirectory.getAbsolutePath());
		new LogFormatter();
	}

	@Test
	void testNotRegistration() {
		Exception exc = assertThrows(IllegalStateException.class, () -> {
			new LogFormatter();

		});
		assertEquals("ApplicationConfiguration - registeredApplication is null", exc.getMessage());
	}
}
