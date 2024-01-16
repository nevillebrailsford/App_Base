package application.inifile;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import application.definition.*;

class IniFileTest {

	@TempDir
	File rootDirectory;

	private ApplicationDefinition app;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		app = new ApplicationDefinition("test");
	}

	@AfterEach
	void tearDown() throws Exception {
		ApplicationConfiguration.clear();
		IniFile.clear();
	}

	@Test
	void testInitialState() {
		ApplicationConfiguration.registerApplication(app, rootDirectory.getAbsolutePath());
		File d = new File(app.iniFileDirectory());
		assertFalse(d.exists());
		File f = new File(app.iniFile());
		assertFalse(f.exists());
	}

	@Test
	void testGetMissingKey() {
		ApplicationConfiguration.registerApplication(app, rootDirectory.getAbsolutePath());
		assertEquals("", IniFile.value("key"));
	}

	@Test
	void testStoreOneKeyValuePair() {
		ApplicationConfiguration.registerApplication(app, rootDirectory.getAbsolutePath());
		IniFile.store("key", "value");
		assertEquals("value", IniFile.value("key"));
	}

	@Test
	void testStoreTwoKeyValuePairs() {
		ApplicationConfiguration.registerApplication(app, rootDirectory.getAbsolutePath());
		IniFile.store("key1", "value1");
		IniFile.store("key2", "value2");
		assertEquals("value1", IniFile.value("key1"));
		assertEquals("value2", IniFile.value("key2"));
	}

	@Test
	void testStoreKeyValuePairTwice() {
		ApplicationConfiguration.registerApplication(app, rootDirectory.getAbsolutePath());
		IniFile.store("key1", "value1");
		assertEquals("value1", IniFile.value("key1"));
		IniFile.store("key1", "value2");
		assertEquals("value2", IniFile.value("key1"));
	}

	@Test
	void testNullKeyStore() {
		ApplicationConfiguration.registerApplication(app, rootDirectory.getAbsolutePath());
		Exception exc = assertThrows(IllegalArgumentException.class, () -> {
			IniFile.store(null, "value");
		});
		assertEquals("IniFile: key was null", exc.getMessage());
	}

	@Test
	void testEmptyKeyStore() {
		ApplicationConfiguration.registerApplication(app, rootDirectory.getAbsolutePath());
		Exception exc = assertThrows(IllegalArgumentException.class, () -> {
			IniFile.store("", "value");
		});
		assertEquals("IniFile: key was empty", exc.getMessage());
	}

	@Test
	void testNullValueStore() {
		ApplicationConfiguration.registerApplication(app, rootDirectory.getAbsolutePath());
		Exception exc = assertThrows(IllegalArgumentException.class, () -> {
			IniFile.store("key", null);
		});
		assertEquals("IniFile: value was null", exc.getMessage());
	}

	@Test
	void testEmptyValueStore() {
		ApplicationConfiguration.registerApplication(app, rootDirectory.getAbsolutePath());
		Exception exc = assertThrows(IllegalArgumentException.class, () -> {
			IniFile.store("key", "");
		});
		assertEquals("IniFile: value was empty", exc.getMessage());
	}

	@Test
	void testNullKeyValueStore() {
		ApplicationConfiguration.registerApplication(app, rootDirectory.getAbsolutePath());
		Exception exc = assertThrows(IllegalArgumentException.class, () -> {
			IniFile.store(null, null);
		});
		assertEquals("IniFile: key was null", exc.getMessage());
	}

	@Test
	void testNullKeyValue() {
		ApplicationConfiguration.registerApplication(app, rootDirectory.getAbsolutePath());
		Exception exc = assertThrows(IllegalArgumentException.class, () -> {
			IniFile.value(null);
		});
		assertEquals("IniFile: key was null", exc.getMessage());
	}

	@Test
	void testEmptyKeyValue() {
		ApplicationConfiguration.registerApplication(app, rootDirectory.getAbsolutePath());
		Exception exc = assertThrows(IllegalArgumentException.class, () -> {
			IniFile.value("");
		});
		assertEquals("IniFile: key was empty", exc.getMessage());
	}
}
