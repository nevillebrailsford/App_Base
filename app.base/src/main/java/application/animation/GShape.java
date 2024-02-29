package application.animation;

import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;

public class GShape {
	public Shape shape;
	public Paint fillColor = null;
	public Paint strokeColor = null;
	public boolean noStroke = false;
	public boolean noFill = false;
	public AffineTransform transform = null;
	public Stroke stroke = null;

	public GShape(Shape shape) {
		this.shape = shape;
	}
}
