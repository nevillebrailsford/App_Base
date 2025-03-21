package application.base.app.gui;

import java.awt.Component;

import javax.swing.JTable;

/**
 * A table cell renderer to format String class fields in a consistent way.
 * 
 * @author neville
 * @version 4.0.0
 */
public class StringCellRenderer extends BaseCellRenderer {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor that sets up default behaviour.
	 */
	public StringCellRenderer() {
		super();
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
