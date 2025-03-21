package application.base.app.gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.SwingConstants;

import application.model.Money;

/**
 * A table cell renderer to format Money class fields in a consistent way.
 * 
 * @author neville
 * @version 4.0.0
 */
public class MoneyCellRenderer extends BaseCellRenderer {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor that sets up default behaviour and then ensures that the money
	 * value will be displayed right aligned.
	 */
	public MoneyCellRenderer() {
		super();
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
