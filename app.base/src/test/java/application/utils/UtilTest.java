package application.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

class UtilTest {

	@Test
	void test() {
		if (System.getProperty("os.name").toLowerCase().contains("mac")) {
			assertEquals(Util.OS.MAC, Util.getOS());
		} else if (System.getProperty("os.name").toLowerCase().contains("win")) {
			assertEquals(Util.OS.WINDOWS, Util.getOS());
		} else {
			fail("Unknown system");
		}
	}

}
