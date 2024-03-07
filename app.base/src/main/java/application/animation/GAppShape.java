package application.animation;

import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;

public class GAppShape {
	public Shape shape;
	public Paint fillColor = null;
	public Paint strokeColor = null;
	public boolean noStroke = false;
	public boolean noFill = false;
	public AffineTransform transform = null;
	public Stroke stroke = null;

	public GAppShape(Shape shape) {
		this.shape = shape;
	}

}
