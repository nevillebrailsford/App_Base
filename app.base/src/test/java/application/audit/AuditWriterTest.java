package application.audit;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import application.definition.*;

class AuditWriterTest {
	@TempDir
	File rootDirectory;
	ApplicationDefinition x;

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
		ApplicationConfiguration.clear();
	}

	@Test
	void testWriterCreation() {
		ApplicationDefinition app = new ApplicationDefinition("test");
		ApplicationConfiguration.registerApplication(app, rootDirectory.getAbsolutePath());
		File f = new File(app.auditFile());
		assertFalse(f.exists());
		new AuditWriter();
		assertFalse(f.exists());
	}

	@Test
	void testWriterWriter() {
		ApplicationDefinition app = new ApplicationDefinition("test");
		ApplicationConfiguration.registerApplication(app, rootDirectory.getAbsolutePath());
		File f = new File(app.auditFile());
		assertFalse(f.exists());
		AuditWriter writer = new AuditWriter();
		AuditRecord<?, ?> record = new AuditRecord<>(TestAuditType.Opened, TestAuditObject.File, "message");
		writer.write(record);
		assertTrue(f.exists());
	}
}
