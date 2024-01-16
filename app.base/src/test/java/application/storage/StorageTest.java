package application.storage;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import application.definition.ApplicationConfiguration;
import application.definition.ApplicationDefinition;
import application.logging.LogConfigurer;
import application.notification.Notification;
import application.notification.NotificationCentre;
import application.notification.NotificationListener;

class StorageTest {
	boolean loadCalled = false;
	boolean storeCalled = false;
	boolean loadStarted = false;
	boolean loadComplete = false;
	boolean loadFailed = false;
	boolean storeStarted = false;
	boolean storeComplete = false;
	boolean storeFailed = false;
	Object waitForIO = new Object();
	Object waitForCall = new Object();

	LoadData dataLoader = null;
	LoadData dataFailure = null;

	StoreData dataStore = null;
	StoreData dataFailures = null;

	File modelFile = null;

	NotificationListener nListener = new NotificationListener() {
		@Override
		public void notify(Notification notification) {
			assertTrue(notification.subject().isPresent());
			StorageNotificationType type = (StorageNotificationType) notification.notificationType();
			switch (type) {
				case Load -> {
					handleLoad(notification);
				}
				case Store -> {
					handleStore(notification);
				}
			}
		}

		private void handleStore(Notification notification) {
			StoreState state = (StoreState) notification.subject().get();
			switch (state) {
				case Started -> {
					storeStarted = true;
				}
				case Complete -> {
					storeComplete = true;
					tellWaiters();
				}
				case Failed -> {
					storeFailed = true;
					tellWaiters();
				}
			}
		}

		private void handleLoad(Notification notification) {
			LoadState state = (LoadState) notification.subject().get();
			switch (state) {
				case Started -> {
					loadStarted = true;
				}
				case Complete -> {
					loadComplete = true;
					tellWaiters();
				}
				case Failed -> {
					loadFailed = true;
					tellWaiters();
				}
			}
		}
	};

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
		ApplicationDefinition app = new ApplicationDefinition("test") {

			@Override
			public Level level() {
				return Level.OFF;
			}

		};
		ApplicationConfiguration.registerApplication(app, rootDirectory.getAbsolutePath());
		LogConfigurer.setUp();
		dataLoader = new AbstractLoadData() {
			@Override
			public void readData() throws IOException {
				loadCalled = true;
				synchronized (waitForCall) {
					waitForCall.notifyAll();
				}
			}
		};
		dataFailure = new AbstractLoadData() {
			@Override
			public void readData() throws IOException {
				loadCalled = true;
				synchronized (waitForCall) {
					waitForCall.notifyAll();
				}
				throw new IOException("expected exception");
			}
		};
		dataStore = new AbstractStoreData() {

			@Override
			public void storeData() throws IOException {
				synchronized (waitForCall) {
					waitForCall.notifyAll();
				}
				storeCalled = true;
			}
		};
		dataFailures = new AbstractStoreData() {

			@Override
			public void storeData() throws IOException {
				storeCalled = true;
				synchronized (waitForCall) {
					waitForCall.notifyAll();
				}
				throw new IOException("expected exception");
			}
		};
		resetFlags();
	}

	@AfterEach
	void tearDown() throws Exception {
		ApplicationConfiguration.clear();
		LogConfigurer.shutdown();
		resetFlags();
	}

	@Test
	void testLoad() throws IOException, InterruptedException {
		createModelFile();
		dataLoader.setFileName(modelFile.getAbsolutePath());
		Storage storage = new Storage();
		assertNotNull(storage);
		assertFalse(loadCalled);
		synchronized (waitForCall) {
			storage.loadStoredData(dataLoader);
			waitForCall.wait();
		}
		assertTrue(loadCalled);
	}

	@Test
	void testStore() throws IOException, InterruptedException {
		createModelFile();
		dataStore.setFileName(modelFile.getAbsolutePath());
		Storage storage = new Storage();
		assertNotNull(storage);
		assertFalse(storeCalled);
		synchronized (waitForCall) {
			storage.storeData(dataStore);
			waitForCall.wait();
		}
		assertTrue(storeCalled);
	}

	@Test
	void testLoadFailure() throws IOException, InterruptedException {
		createModelFile();
		dataFailure.setFileName(modelFile.getAbsolutePath());
		Storage storage = new Storage();
		assertNotNull(storage);
		assertFalse(loadCalled);
		synchronized (waitForCall) {
			storage.loadStoredData(dataFailure);
			waitForCall.wait();
		}
		assertTrue(loadCalled);
	}

	@Test
	void testStoreFailure() throws IOException, InterruptedException {
		createModelFile();
		dataFailures.setFileName(modelFile.getAbsolutePath());
		Storage storage = new Storage();
		assertNotNull(storage);
		assertFalse(storeCalled);
		synchronized (waitForCall) {
			storage.storeData(dataFailures);
			waitForCall.wait();
		}
		assertTrue(storeCalled);
	}

	@Test
	void testMissingFile() throws IOException, InterruptedException {
		File missingFile = new File(rootDirectory, "missing.dat");
		assertFalse(missingFile.exists());
		dataLoader.setFileName(missingFile.getAbsolutePath());
		Storage storage = new Storage();
		assertNotNull(storage);
		assertThrows(IOException.class, () -> {
			storage.loadStoredData(dataLoader);
		});
	}

	@Test
	void testListener() throws IOException, InterruptedException {
		createModelFile();
		dataLoader.setFileName(modelFile.getAbsolutePath());
		Storage storage = new Storage();
		NotificationCentre.addListener(nListener);
		synchronized (waitForIO) {
			storage.loadStoredData(dataLoader);
			waitForIO.wait();
			assertTrue(loadStarted);
			assertTrue(loadComplete);
			assertFalse(loadFailed);
		}
		NotificationCentre.removeListener(nListener);
		resetFlags();
		dataFailure.setFileName(modelFile.getAbsolutePath());
		NotificationCentre.addListener(nListener);
		synchronized (waitForIO) {
			storage.loadStoredData(dataFailure);
			waitForIO.wait();
			assertTrue(loadStarted);
			assertFalse(loadComplete);
			assertTrue(loadFailed);
		}
		NotificationCentre.removeListener(nListener);
		resetFlags();
		NotificationCentre.addListener(nListener);
		synchronized (waitForIO) {
			storage.storeData(dataStore);
			waitForIO.wait();
		}
		NotificationCentre.removeListener(nListener);
		assertTrue(storeStarted);
		assertTrue(storeComplete);
		assertFalse(storeFailed);
		resetFlags();
		NotificationCentre.addListener(nListener);
		synchronized (waitForIO) {
			storage.storeData(dataFailures);
			waitForIO.wait();
			assertTrue(storeStarted);
			assertFalse(storeComplete);
			assertTrue(storeFailed);
		}
		NotificationCentre.removeListener(nListener);
		resetFlags();
	}

	private void resetFlags() {
		loadCalled = false;
		loadStarted = false;
		loadComplete = false;
		loadFailed = false;
		storeStarted = false;
		storeComplete = false;
		storeFailed = false;
	}

	private void createModelFile() throws IOException {
		modelFile = new File(rootDirectory, "model.dat");
		List<String> lines = Arrays.asList("x", "y", "x");
		assertFalse(modelFile.exists());
		Files.write(modelFile.toPath(), lines);
		assertTrue(modelFile.exists());
		assertLinesMatch(lines, Files.readAllLines(modelFile.toPath()));
	}

	private void tellWaiters() {
		synchronized (waitForIO) {
			waitForIO.notifyAll();
		}
	}
}
