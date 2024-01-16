package application.base.app.gui;

import javax.swing.Action;
import javax.swing.JMenuItem;

import application.definition.ApplicationConfiguration;

public class ColoredMenuItem extends JMenuItem {
	private static final long serialVersionUID = 1L;

	public ColoredMenuItem(Action action) {
		super(action);
		setOpaque(true);
		setBackground(ApplicationConfiguration.applicationDefinition().bottomColor().get());
	}
}
