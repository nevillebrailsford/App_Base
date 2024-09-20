package application.base.app.gui;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import application.definition.ApplicationConfiguration;

/**
 * Provide an adjusted <code>JPanel</code> with the background set to a gradient
 * colour being used for this application. The gradient colour is constructed
 * using the top and bottom colours specified for the application.
 * 
 * @author neville
 * @version 3.0.0
 */
public class ColoredPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel with no layout manager set.
	 */
	public ColoredPanel() {
		setOpaque(false);
	}

	/**
	 * Create the panel with with a layout manager as specified.
	 * 
	 * @param layout - the layout manager to be used for this panel.
	 */
	public ColoredPanel(LayoutManager layout) {
		super();
		setLayout(layout);
	}

	@Override
	protected void paintComponent(Graphics g) {
		if (ApplicationConfiguration.isDefinitionRegistered()) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			Color top = ApplicationConfiguration.applicationDefinition().topColor().orElse(getBackground());
			Color bottom = ApplicationConfiguration.applicationDefinition().bottomColor().orElse(getBackground());
			GradientPaint gp = new GradientPaint(0, 0, top, 0, getHeight(), bottom);
			g2d.setPaint(gp);
			g2d.fillRect(0, 0, getWidth(), getHeight());
		}
		super.paintComponent(g);
	}
}
