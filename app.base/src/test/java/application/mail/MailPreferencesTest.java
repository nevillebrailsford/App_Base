package application.mail;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import application.definition.ApplicationConfiguration;
import application.definition.ApplicationDefinition;

class MailPreferencesTest {

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
	}

	@AfterEach
	void tearDown() throws Exception {
		MailPreferences.clear();
		ApplicationConfiguration.clear();
	}

	@Test
	void testMailingList() {
		MailPreferences.setMailingList("test,list");
		assertEquals("test,list", MailPreferences.mailingList());
	}

	@Test
	void testSetMailingList() {
		assertEquals("", MailPreferences.mailingList());
		MailPreferences.setMailingList("test,list");
		assertEquals("test,list", MailPreferences.mailingList());
	}

	@Test
	void testNoList() {
		assertEquals("", MailPreferences.mailingList());
	}
}
