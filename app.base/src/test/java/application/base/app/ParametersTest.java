package application.base.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ParametersTest {

	Parameters parms;
	String[] args = null;

	@Test
	void testRawParameters() {
		args = new String[] { "one" };
		parms = new Parameters(args);
		assertNotNull(parms.getNamed());
		assertNotNull(parms.getRaw());
		assertTrue(parms.getNamed().size() == 0);
		assertTrue(parms.getRaw().size() == 1);
		assertEquals("one", parms.getRaw().get(0));
	}

	@Test
	void testNamedParameters() {
		args = new String[] { "--test=one" };
		parms = new Parameters(args);
		assertNotNull(parms.getNamed());
		assertNotNull(parms.getRaw());
		assertTrue(parms.getNamed().size() == 1);
		assertTrue(parms.getRaw().size() == 1);
		assertEquals("--test=one", parms.getRaw().get(0));
		assertTrue(parms.getNamed().containsKey("test"));
		assertEquals("one", parms.getNamed().get("test"));
	}

	@Test
	void testNoParameters() {
		parms = new Parameters(args);
		assertNotNull(parms.getNamed());
		assertNotNull(parms.getRaw());
		assertTrue(parms.getNamed().size() == 0);
		assertTrue(parms.getRaw().size() == 0);
	}

	@Test
	void testDuplicateKey() {
		args = new String[] { "--test=one", "--test=two" };
		Exception exc = assertThrows(DuplicateParameterException.class, () -> {
			parms = new Parameters(args);
		});
		assertEquals("Parameters: duplicate key = test", exc.getMessage());
	}

}
