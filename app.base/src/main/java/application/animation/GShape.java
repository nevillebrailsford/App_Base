package application.animation;

import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class GShape {
	public int type;
	public Paint fillColor = null;
	public Paint strokeColor = null;
	public boolean noFill = false;
	public boolean noStroke = false;
	public float width;
	public float height;
	public float start;
	public float extent;

	public GShape(int type, float[] args) {
		switch (type) {
			case GApplication.POINT -> checkPoint(args);
			case GApplication.LINE -> checkLine(args);
			case GApplication.RECT -> buildRect(args);
			case GApplication.ARC -> buildArc(args);
			case GApplication.ELLIPSE -> buildEllipse(args);
			default -> throw new IllegalArgumentException("GShape unknown type " + type);
		}
		;
		this.type = type;
		fillColor = GApplication.app().fillColor;
		strokeColor = GApplication.app().strokeColor;
		noFill = GApplication.app().noFill;
		noStroke = GApplication.app().noStroke;
	}

	private void checkPoint(float[] args) {
		if (args.length != 4) {
			throw new IllegalArgumentException("GShape POINT incorrect number of args");
		}
		width = args[2] - args[0];
		height = args[3] - args[1];
	}

	private void checkLine(float[] args) {
		if (args.length != 4) {
			throw new IllegalArgumentException("GShape LINE incorrect number of args");
		}
	}

	private Shape buildRect(float[] args) {
		float x = args[0];
		float y = args[1];
		float w = args[2];
		float h = args[3];
		if (GApplication.app().rectMode == GApplication.CENTER) {
			x -= w / 2;
			y -= h / 2;
		}
		if (args.length != 4) {
			throw new IllegalArgumentException("GShape RECT incorrect number of args");
		}
		return new Rectangle2D.Float(x, y, w, h);
	}

	private Shape buildArc(float[] args) {
		if (args.length != 6) {
			throw new IllegalArgumentException("GShape ARC incorrect number of args");
		}
		float x = args[0];
		float y = args[1];
		float w = args[2];
		float h = args[3];
		float start = args[4];
		float extent = args[5];
		if (GApplication.app().ellipseMode == GApplication.CENTER) {
			x -= w / 2;
			y -= h / 2;
		}
		return new Arc2D.Float(x, y, w, h, start, extent, Arc2D.OPEN);
	}

	private Shape buildEllipse(float[] args) {
		if (args.length != 4) {
			throw new IllegalArgumentException("GShape ELLIPSE incorrect number of args");
		}
		float x = args[0];
		float y = args[1];
		float w = args[2];
		float h = args[3];
		if (GApplication.app().ellipseMode == GApplication.CENTER) {
			x -= w / 2;
			y -= h / 2;
		}
		return new Ellipse2D.Float(x, y, w, h);
	}
}
