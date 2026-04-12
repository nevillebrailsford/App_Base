package application.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

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

	@Test
	void testDisplayDateLocalDate() {
		LocalDate now = LocalDate.now();
		String displayDate = Util.displayDate(now);
		int uuuu = now.getYear();
		int MM = now.getMonth().getValue();
		int dd = now.getDayOfMonth();
		assertNotNull(displayDate);
		assertEquals(10, displayDate.length());
		assertEquals(uuuu, Integer.parseInt(displayDate.substring(6)));
		assertEquals(MM, Integer.parseInt(displayDate.substring(3, 5)));
		assertEquals(dd, Integer.parseInt(displayDate.substring(0, 2)));
	}

	@Test
	void testDisplayDateDate() {
		Date now = new Date();
		String displayDate = Util.displayDate(now);
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		int uuuu = cal.get(Calendar.YEAR);
		int MM = cal.get(Calendar.MONTH) + 1;
		int dd = cal.get(Calendar.DAY_OF_MONTH);
		assertNotNull(displayDate);
		assertEquals(10, displayDate.length());
		assertEquals(uuuu, Integer.parseInt(displayDate.substring(6)));
		assertEquals(MM, Integer.parseInt(displayDate.substring(3, 5)));
		assertEquals(dd, Integer.parseInt(displayDate.substring(0, 2)));
	}

}
