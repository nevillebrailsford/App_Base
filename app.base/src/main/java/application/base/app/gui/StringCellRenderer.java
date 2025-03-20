package application.base.app.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

public class StringCellRenderer extends JLabel implements TableCellRenderer {
	private static final long serialVersionUID = 1L;
	private static final Color originalForeground = UIManager.getColor("Table.Foreground");
	private static final Color originalBackground = UIManager.getColor("Table.Background");
	private static final Color originalSelectionForeground = UIManager.getColor("Table.selectionForeground");
	private static final Color originalSelectionBackground = UIManager.getColor("Table.selectionBackground");

	public StringCellRenderer() {
		setFont(getFont().deriveFont(Font.PLAIN, 14));
		setOpaque(true);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		String val = (String) value;
		if (isSelected) {
			setForeground(originalSelectionForeground);
			setBackground(originalSelectionBackground);
		} else {
			setForeground(originalForeground);
			setBackground(originalBackground);
		}
		setText(val);
		return this;
	}
}
