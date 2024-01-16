package application.change;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.logging.Level;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import application.change.Change.State;
import application.definition.*;

class ChangeTest {
	private Change stateErrorChange = null;

	@TempDir
	File directory;

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
		ApplicationConfiguration.registerApplication(app, directory.getAbsolutePath());
		stateErrorChange = new UTChange(State.STUCK, 0);
	}

	@AfterEach
	void tearDown() throws Exception {
		ApplicationConfiguration.clear();
	}

	@Test
	void testGetState() {
		assertEquals(State.STUCK, stateErrorChange.state());
	}

	@Test
	void testExecute() {
		assertThrows(AssertionError.class, () -> {
			stateErrorChange.execute();
		});
	}

	@Test
	void testUndo() {
		assertThrows(AssertionError.class, () -> {
			stateErrorChange.undo();
		});
	}

	@Test
	void testRedo() {
		assertThrows(AssertionError.class, () -> {
			stateErrorChange.redo();
		});
	}

	@Test
	void testExecuteFailure() {
		Change failureChange = new UTChange(State.READY, 1);
		assertEquals(State.READY, failureChange.state());
		failureChange.execute();
		assertEquals(State.STUCK, failureChange.state());
	}

	@Test
	void testUndoFailure() {
		Change failureChange = new UTChange(State.DONE, 2);
		assertEquals(State.DONE, failureChange.state());
		failureChange.undo();
		assertEquals(State.STUCK, failureChange.state());
	}

	@Test
	void testRedoFailure() {
		Change failureChange = new UTChange(State.UNDONE, 3);
		assertEquals(State.UNDONE, failureChange.state());
		failureChange.redo();
		assertEquals(State.STUCK, failureChange.state());
	}

	private class UTChange extends AbstractChange {
		private int failurePoint = 0;

		private UTChange(State state, int failurePoint) {
			this.state = state;
			this.failurePoint = failurePoint;
		}

		@Override
		protected void doHook() throws Failure {
			if (failurePoint == 1) {
				throw new Failure();
			}
		}

		@Override
		protected void undoHook() throws Failure {
			if (failurePoint == 2) {
				throw new Failure();
			}
		}

		@Override
		protected void redoHook() throws Failure {
			if (failurePoint == 3) {
				throw new Failure();
			}
		}

	}
}
