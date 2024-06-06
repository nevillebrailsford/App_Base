package application.base.app.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;

import javax.swing.JPanel;

import application.definition.ApplicationConfiguration;

public class BottomColoredPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	public BottomColoredPanel() {
		setOpaque(false);
	}

	public BottomColoredPanel(LayoutManager layout) {
		super();
		setLayout(layout);
	}

	@Override
	protected void paintComponent(Graphics g) {
		if (ApplicationConfiguration.isDefinitionRegistered()) {
			Graphics2D g2d = (Graphics2D) g;
			Color bottom = ApplicationConfiguration.applicationDefinition().bottomColor().orElse(getBackground());
			g2d.setPaint(bottom);
			g2d.fillRect(0, 0, getWidth(), getHeight());
		}
		super.paintComponent(g);
	}
}
