package application.base.app.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;

import javax.swing.JPanel;

import application.definition.ApplicationConfiguration;

/**
 * Provide an adjusted JPanel with the background set to the bottom colour being
 * used for this application.
 */
public class BottomColoredPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel with no layout manager set.
	 */
	public BottomColoredPanel() {
		setOpaque(false);
	}

	/**
	 * Create the panel with a layout manager as specified.
	 * 
	 * @param layout - the layout manager to be used for this panel.
	 */
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
