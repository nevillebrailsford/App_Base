package application.charting;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

public abstract class ChartPainter extends ComponentUI {
	protected Font textFont = new Font("Serif", Font.PLAIN, 12);
	protected Color textColor = Color.black;

	protected Color[] colors = new Color[] { Color.decode("#ffa07a"), Color.decode("#87cefa"), Color.decode("#ffd700"),
			Color.decode("#3cb371"), Color.white, Color.gray, Color.decode("#add8e6"), Color.decode("#fa8072"),
			Color.lightGray, Color.black };

	protected double[] values = new double[0];
	protected String[] labels = new String[0];

	public void setTextFont(Font f) {
		textFont = f;
	}

	public Font getTextFont() {
		return textFont;
	}

	public void setColor(Color[] clist) {
		colors = clist;
	}

	public Color[] getColor() {
		return colors;
	}

	public void setColor(int index, Color c) {
		colors[index] = c;
	}

	public Color getColor(int index) {
		return colors[index];
	}

	public void setTextColor(Color c) {
		textColor = c;
	}

	public Color getTextColor() {
		return textColor;
	}

	public void setLabels(String[] l) {
		labels = l;
	}

	public void setValues(double[] v) {
		values = v;
	}

	public abstract int indexOfEntryAt(MouseEvent me);

	@Override
	public abstract void paint(Graphics g, JComponent c);

}
