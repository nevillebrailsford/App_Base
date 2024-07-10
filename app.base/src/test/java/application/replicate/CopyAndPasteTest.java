package application.replicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.File;
import java.util.logging.Level;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import application.definition.ApplicationConfiguration;
import application.definition.ApplicationDefinition;

class CopyAndPasteTest {

	@TempDir
	File directory;
	Object test1 = new Object();
	Object test2 = new Object();

	@BeforeEach
	void setUp() throws Exception {
		ApplicationDefinition app = new ApplicationDefinition("test") {

			@Override
			public Level level() {
				return Level.OFF;
			}
		};
		ApplicationConfiguration.registerApplication(app, directory.getAbsolutePath());
	}

	@AfterEach
	void tearDown() throws Exception {
		CopyAndPaste.instance().reset();
		ApplicationConfiguration.clear();
	}

	@Test
	void testGetInstance() {
		assertNotNull(CopyAndPaste.instance());
	}

	@Test
	void testEmptyOnCreate() {
		assertNull(CopyAndPaste.instance().paste());
	}

	@Test
	void testAddOneObject() {
		CopyAndPaste.instance().copy(test1);
		assertNotNull(CopyAndPaste.instance().paste());
		assertEquals(test1, CopyAndPaste.instance().paste());
	}

	@Test
	void testAddTwoObjects() {
		CopyAndPaste.instance().copy(test1);
		assertNotNull(CopyAndPaste.instance().paste());
		assertEquals(test1, CopyAndPaste.instance().paste());
		CopyAndPaste.instance().copy(test2);
		assertNotNull(CopyAndPaste.instance().paste());
		assertEquals(test2, CopyAndPaste.instance().paste());
	}

}
