package application.base.app.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

public class DateCellRenderer extends JLabel implements TableCellRenderer {
	private static final long serialVersionUID = 1L;
	private static final Color originalForeground = UIManager.getColor("Table.Foreground");
	private static final Color originalBackground = UIManager.getColor("Table.Background");
	private static final Color originalSelectionForeground = UIManager.getColor("Table.selectionForeground");
	private static final Color originalSelectionBackground = UIManager.getColor("Table.selectionBackground");
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu");

	public DateCellRenderer() {
		setFont(getFont().deriveFont(Font.PLAIN, 14));
		setOpaque(true);
		setHorizontalAlignment(SwingConstants.CENTER);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		LocalDate val = (LocalDate) value;
		if (isSelected) {
			setForeground(originalSelectionForeground);
			setBackground(originalSelectionBackground);
		} else {
			setForeground(originalForeground);
			setBackground(originalBackground);
		}
		setText(val.format(formatter));
		return this;
	}
}
