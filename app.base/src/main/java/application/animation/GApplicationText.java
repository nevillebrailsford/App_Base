package application.animation;

import java.awt.Font;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;

public abstract class GApplicationText extends GApplicationGraphic {
	protected int textSize = 20;
	protected Font textFont = new Font(Font.SERIF, Font.PLAIN, textSize);
	protected int textAlignX = LEFT;
	protected int textAlignY = BOTTOM;

	public void text(char c, double x, double y) {
		text(String.valueOf(c), x, y);
	}

	public void text(char[] c, double x, double y) {
		text(String.valueOf(c), x, y);
	}

	public void text(String word, double x, double y) {
		TextLayout textTl = new TextLayout(word, textFont, new FontRenderContext(null, false, false));
		AffineTransform textAt = new AffineTransform();
		float xTranslate = (float) x;
		float yTranslate = (float) y;
		if (textAlignX == CENTER) {
			xTranslate -= textTl.getBounds().getWidth() / 2;
		}
		if (textAlignX == RIGHT) {
			xTranslate -= textTl.getBounds().getWidth();
		}
		if (textAlignY == CENTER) {
			yTranslate += textTl.getBounds().getHeight() / 2;
		}
		if (textAlignY == BOTTOM) {
			yTranslate += textTl.getBounds().getHeight();
		}
		textAt.translate(xTranslate, yTranslate);
		Shape shape = textTl.getOutline(textAt);
		showShape(shape);
	}

}
