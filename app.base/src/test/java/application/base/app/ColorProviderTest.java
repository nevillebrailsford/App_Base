package application.base.app;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.awt.Color;

import org.junit.jupiter.api.Test;

import application.base.app.gui.ColorProvider;

class ColorProviderTest {

	@Test
	void testNextColor() {
		Color last = null;
		for (int i = 0; i < ColorProvider.colors(); i++) {
			Color c = ColorProvider.nextColor();
			assertNotNull(c);
			assertNotEquals(c, last);
			last = c;
		}
	}

}
