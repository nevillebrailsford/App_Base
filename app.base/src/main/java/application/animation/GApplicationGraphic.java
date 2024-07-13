package application.animation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Stack;

public abstract class GApplicationGraphic extends GApplicationEnvironment {

	public Graphics2D graphicContext = null;

	protected Paint backgroundColor = Color.lightGray;
	protected float strokeWeight = 1;
	protected Paint strokeColor = Color.BLACK;
	protected Paint fillColor = Color.WHITE;
	protected boolean noStroke = false;
	protected boolean noFill = false;
	protected Stack<Matrix> matrixStack;
	protected Matrix matrix;

	public void line(float x1, float y1, float x2, float y2) {
		Line2D.Float line = new Line2D.Float(x1, y1, x2, y2);
		showShape(line);
	}

	public void point(float x, float y) {
		Rectangle2D.Float point = new Rectangle2D.Float(x, y, strokeWeight, strokeWeight);
		showShape(point);
	}

	public void arc(float x, float y, float w, float h, float start, float extent) {
		if (GApplication.app().ellipseMode == CENTER) {
			x -= w / 2;
			y -= h / 2;
		}
		Arc2D.Float arc = new Arc2D.Float(x, y, w, h, start, extent, Arc2D.OPEN);
		showShape(arc);
	}

	public void ellipse(float x, float y, float w, float h) {
		if (GApplication.app().ellipseMode == CENTER) {
			x -= w / 2;
			y -= h / 2;
		}
		Ellipse2D.Float oval = new Ellipse2D.Float(x, y, w, h);
		showShape(oval);
	}

	public void circle(float x, float y, float w) {
		ellipse(x, y, w, w);
	}

	public void rect(float x, float y, float w, float h) {
		if (GApplication.app().rectMode == CENTER) {
			x -= w / 2;
			y -= h / 2;
		}
		Rectangle2D.Float rect = new Rectangle2D.Float(x, y, w, h);
		showShape(rect);
	}

	public void rect(float x, float y, float w, float h, float r) {
		if (GApplication.app().rectMode == CENTER) {
			x -= w / 2;
			y -= h / 2;
		}
		RoundRectangle2D.Float rect = new RoundRectangle2D.Float(x, y, w, h, r, r);
		showShape(rect);
	}

	public void square(float x, float y, float w) {
		rect(x, y, w, w);
	}

	public void quad(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		GeneralPath quad = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
		quad.moveTo(x1, y1);
		quad.lineTo(x2, y2);
		quad.lineTo(x3, y3);
		quad.lineTo(x4, y4);
		quad.closePath();
		showShape(quad);
	}

	public void triangle(float x1, float y1, float x2, float y2, float x3, float y3) {
		GeneralPath quad = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
		quad.moveTo(x1, y1);
		quad.lineTo(x2, y2);
		quad.lineTo(x3, y3);
		quad.closePath();
		showShape(quad);
	}

	protected void showShape(Shape shape) {
		GAppShape gShape = new GAppShape(shape);
		primeShape(gShape);
		drawGShape(gShape);
	}

	protected void showShape(GShape gshape) {
		GAppShape gAppShape = new GAppShape(gshape.shape);
		primeShape(gAppShape, gshape);
		drawGShape(gAppShape);
	}

	protected void primeShape(GAppShape gAppShape, GShape gshape) {
		gAppShape.strokeColor = gshape.strokeColor;
		gAppShape.fillColor = gshape.fillColor;
		gAppShape.noStroke = gshape.noStroke;
		gAppShape.noFill = gshape.noFill;
		AffineTransform transform = new AffineTransform();
		transform.translate(matrix.translateX, matrix.translateY);
		gAppShape.transform = transform;
		gAppShape.stroke = new BasicStroke(strokeWeight, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	}

	private void drawGShape(GAppShape shape) {
		AffineTransform saveXform = graphicContext.getTransform();
		AffineTransform toNewLoc = new AffineTransform();
		toNewLoc.concatenate(shape.transform);
		graphicContext.transform(toNewLoc);
		graphicContext.setStroke(shape.stroke);
		if (!shape.noFill) {
			graphicContext.setColor((Color) shape.fillColor);
			graphicContext.fill(shape.shape);
		}
		if (!shape.noStroke) {
			graphicContext.setColor((Color) shape.strokeColor);
			graphicContext.draw(shape.shape);
		}
		graphicContext.setTransform(saveXform);
	}

	private void primeShape(GAppShape shape) {
		shape.strokeColor = strokeColor;
		shape.fillColor = fillColor;
		shape.noStroke = noStroke;
		shape.noFill = noFill;
		AffineTransform transform = new AffineTransform();
		transform.translate(matrix.translateX, matrix.translateY);
		shape.transform = transform;
		shape.stroke = new BasicStroke(strokeWeight, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	}

	public class Matrix {
		protected float translateX;
		protected float translateY;
	}
}
