package application.archive;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import application.definition.ApplicationConfiguration;
import application.definition.ApplicationDefinition;

class ArchiveTest {

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
		ApplicationConfiguration.clear();
	}

	@Test
	void test() throws IOException {
		File modelFile = new File(rootDirectory, "model.dat");
		List<String> lines = Arrays.asList("x", "y", "x");
		assertFalse(modelFile.exists());
		Files.write(modelFile.toPath(), lines);
		assertTrue(modelFile.exists());
		assertLinesMatch(lines, Files.readAllLines(modelFile.toPath()));
		File archiveFile = Archive.archive(modelFile);
		assertTrue(archiveFile.exists());
		assertLinesMatch(lines, Files.readAllLines(archiveFile.toPath()));
	}

	@Test
	void testNullModelFile() {
		Exception exc = assertThrows(IllegalArgumentException.class, () -> {
			Archive.archive((File) null);
		});
		assertEquals("Archive - modelFile is null", exc.getMessage());
	}

	@Test
	void testMissingModelFile() {
		Exception exc = assertThrows(IllegalArgumentException.class, () -> {
			Archive.archive(new File(rootDirectory, "missing"));
		});
		assertEquals("Archive - modelFile does not exist", exc.getMessage());
	}
}
