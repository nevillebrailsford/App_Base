package application.base.app.gui;

import java.awt.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.JTable;
import javax.swing.SwingConstants;

/**
 * A table cell renderer to format LocalDate class fields in a consistent way.
 * 
 * @author neville
 * @version 4.0.0
 */
public class DateCellRenderer extends BaseCellRenderer {
	private static final long serialVersionUID = 1L;
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu");

	/**
	 * Constructor that sets up default behaviour and then ensures that the date
	 * will be displayed centrally.
	 */
	public DateCellRenderer() {
		super();
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
