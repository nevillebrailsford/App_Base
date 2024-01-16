package application.base.app.gui;

import javax.swing.JMenu;

import application.definition.ApplicationConfiguration;

public class ColoredMenu extends JMenu {
	private static final long serialVersionUID = 1L;

	public ColoredMenu(String name) {
		super(name);
		setOpaque(true);
		setBackground(ApplicationConfiguration.applicationDefinition().bottomColor().get());
	}
}
