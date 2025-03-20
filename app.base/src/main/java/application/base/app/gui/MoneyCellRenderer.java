package application.base.app.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

import application.model.Money;

public class MoneyCellRenderer extends JLabel implements TableCellRenderer {
	private static final long serialVersionUID = 1L;
	private static final Color originalBackground = UIManager.getColor("Table.Background");
	private static final Color originalSelectionBackground = UIManager.getColor("Table.selectionBackground");

	public MoneyCellRenderer() {
		setFont(getFont().deriveFont(Font.BOLD, 14));
		setOpaque(true);
		setHorizontalAlignment(SwingConstants.RIGHT);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		Money val = (Money) value;
		if (isSelected) {
			setBackground(originalSelectionBackground);
		} else {
			setBackground(originalBackground);
		}
		if (val.isNegative()) {
			setForeground(Color.red.darker());
		} else {
			setForeground(Color.green.darker());
		}
		setText(val.cost());
		return this;
	}
}
