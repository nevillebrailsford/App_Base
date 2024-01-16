package application.audit;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import application.definition.*;

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
	}

	@AfterEach
	void tearDown() throws Exception {
		ApplicationConfiguration.clear();
	}

	@Test
	void test() {
		ApplicationConfiguration.registerApplication(app, rootDirectory.getAbsolutePath());
		File f = new File(app.auditFile());
		assertFalse(f.exists());
		AuditService.writeAuditInformation(TestAuditType.Opened, TestAuditObject.File, "message");
		assertTrue(f.exists());
	}
}
