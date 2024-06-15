package application.audit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import application.definition.ApplicationConfiguration;
import application.definition.ApplicationDefinition;

class AuditServiceTest {

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
		ApplicationConfiguration.registerApplication(app, rootDirectory.getAbsolutePath());
		AuditService.reset();
	}

	@AfterEach
	void tearDown() throws Exception {
		ApplicationConfiguration.clear();
	}

	@Test
	void test() {
		File f = new File(app.auditFile());
		assertFalse(f.exists());
		AuditService.writeAuditInformation(TestAuditType.Opened, TestAuditObject.File, "message");
		assertTrue(f.exists());
	}

	@Test
	void testSuspended() {
		File f = new File(app.auditFile());
		assertFalse(f.exists());
		AuditService.suspend();
		AuditService.writeAuditInformation(TestAuditType.Opened, TestAuditObject.File, "message");
		assertFalse(f.exists());
	}
}
