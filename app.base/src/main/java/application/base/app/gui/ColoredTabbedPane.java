package application.base.app.gui;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JTabbedPane;

import application.definition.ApplicationConfiguration;

public class ColoredTabbedPane extends JTabbedPane {
	private static final long serialVersionUID = 1L;

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Color top = ApplicationConfiguration.applicationDefinition().topColor().orElse(null);
		Color bottom = ApplicationConfiguration.applicationDefinition().bottomColor().orElse(null);
		GradientPaint gp = new GradientPaint(0, 0, top, 0, getHeight(), bottom);
		g2d.setPaint(gp);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		super.paintComponent(g);
	}

}
