package application.animation;

import java.awt.Color;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

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
	public Shape shape;
	public List<GVector> vertices = null;
	public List<GShape> shapes = null;
	public float[] args;

	public GShape() {
		fillColor = GApplication.app().fillColor;
		strokeColor = GApplication.app().strokeColor;
		noFill = GApplication.app().noFill;
		noStroke = GApplication.app().noStroke;
	}

	public GShape(int type) {
		super();
		switch (type) {
			case GApplication.GROUP -> setGroup();
			default -> throw new IllegalArgumentException("GShape unknown type " + type);
		}
		this.type = type;
	}

	public GShape(int type, float[] args) {
		super();
		switch (type) {
			case GApplication.GROUP -> setGroup();
			case GApplication.LINE -> saveArgs(args);
			case GApplication.RECT -> saveArgs(args);
			case GApplication.ELLIPSE -> saveArgs(args);
			default -> throw new IllegalArgumentException("GShape unknown type " + type);
		}
		this.type = type;
	}

	private void saveArgs(float[] args) {
		this.args = new float[args.length];
		for (int i = 0; i < args.length; i++) {
			this.args[i] = args[i];
		}
	}

	private void line(float[] args) {
		line(args, 0, 0);
	}

	private void line(float[] args, int xPos, int yPos) {
		if (args.length != 4) {
			throw new IllegalArgumentException("GShape LINE incorrect arguments " + args);
		}
		float x1 = args[0];
		float y1 = args[1];
		float x2 = args[2];
		float y2 = args[3];
		x1 += xPos;
		y1 += yPos;
		x2 += xPos;
		y2 += yPos;
		shape = new Line2D.Float(x1, y1, x2, y2);
	}

	private void rect(float[] args) {
		rect(args, 0, 0);
	}

	private void rect(float[] args, int xPos, int yPos) {
		if (args.length != 4) {
			throw new IllegalArgumentException("GShape ELLIPSE incorrect arguments " + args);
		}
		float x = args[0];
		float y = args[1];
		float w = args[2];
		float h = args[3];
		if (GApplication.app().rectMode == GApplication.CENTER) {
			x -= w / 2;
			y -= h / 2;
		}
		x += xPos;
		y += yPos;
		shape = new Rectangle2D.Float(x, y, w, h);
	}

	private void ellipse(float[] args) {
		ellipse(args, 0, 0);
	}

	private void ellipse(float[] args, int xPos, int yPos) {
		if (args.length != 4) {
			throw new IllegalArgumentException("GShape ELLIPSE incorrect arguments " + args);
		}
		float x = args[0];
		float y = args[1];
		float w = args[2];
		float h = args[3];
		if (GApplication.app().ellipseMode == GApplication.CENTER) {
			x -= w / 2;
			y -= h / 2;
		}
		x += xPos;
		y += yPos;
		shape = new Ellipse2D.Float(x, y, w, h);
	}

	public void addChild(GShape child) {
		shapes.add(child);
	}

	public void draw() {
		if (type == GApplication.GROUP) {
			for (GShape shape : shapes) {
				shape.draw();
			}
		} else {
			switch (type) {
				case GApplication.LINE -> line(args);
				case GApplication.RECT -> rect(args);
				case GApplication.ELLIPSE -> ellipse(args);
			}

			GApplication.app().showShape(this);
		}
	}

	public void draw(int x, int y) {
		if (type == GApplication.GROUP) {
			for (GShape shape : shapes) {
				shape.draw();
			}
		} else {
			switch (type) {
				case GApplication.GROUP -> setGroup();
				case GApplication.LINE -> line(args);
				case GApplication.RECT -> rect(args, x, y);
				case GApplication.ELLIPSE -> ellipse(args, x, y);
			}
			GApplication.app().showShape(this);
		}
	}

	private void setGroup() {
		shapes = new ArrayList<>();
	}

	public void beginShape() {
		vertices = new ArrayList<>();
	}

	public void endShape() {

	}

	public void setFill(int mono) {
		setFill(mono, mono, mono);
	}

	public void setFill(int red, int green, int blue) {
		setFill(red, green, blue, 255);
	}

	public void fill(int mono, int opacity) {
		setFill(mono, mono, mono, opacity);
	}

	public void setFill(int v1, int v2, int v3, int opacity) {
		int red = v1;
		int green = v2;
		int blue = v3;
		int alpha = opacity;
		setFill(new Color(red, green, blue, alpha));
	}

	public void setFill(Color fill) {
		this.fillColor = fill;
	}

	public void setStroke(boolean stroke) {
		noStroke = !stroke;
	}

}
