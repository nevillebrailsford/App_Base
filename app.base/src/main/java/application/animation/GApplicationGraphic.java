package application.animation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
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
		gAppShape.stroke = new BasicStroke(strokeWeight, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
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
		shape.stroke = new BasicStroke(strokeWeight, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
	}

	public class Matrix {
		protected float translateX;
		protected float translateY;
	}
}
