package application.base.app.gui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

/**
 * Abstract cell renderer to be used for all tables to give a consistent look
 * and feel to all application derived tables.
 * 
 * @author neville
 * @version 4.0.0
 */
public abstract class BaseCellRenderer extends JLabel implements TableCellRenderer {
	private static final long serialVersionUID = 1L;
	static final Color originalForeground = UIManager.getColor("Table.Foreground");
	static final Color originalBackground = UIManager.getColor("Table.Background");
	static final Color originalSelectionForeground = UIManager.getColor("Table.selectionForeground");
	static final Color originalSelectionBackground = UIManager.getColor("Table.selectionBackground");

	/**
	 * Constructor sets the font and sets opacity so that background colours can be
	 * used.
	 */
	public BaseCellRenderer() {
		setFont(getFont().deriveFont(Font.PLAIN, 14));
		setOpaque(true);
	}

}
