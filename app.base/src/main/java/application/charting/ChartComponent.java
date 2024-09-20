package application.charting;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;

import javax.swing.JComponent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 * Component to display a chart of some kind.
 * 
 * @author neville
 * @version 3.0.0
 */
public abstract class ChartComponent extends JComponent implements TableModelListener {
	private static final long serialVersionUID = 1L;

	protected TableModel model;
	protected ChartPainter cp;
	protected String[] labels;
	protected String[] tips;
	protected NumberFormat formatter = NumberFormat.getPercentInstance();

	/**
	 * Create the chart component.
	 * 
	 * @param tm - a TableModel that provides the x and y values.
	 * @param cp - a ChartPainter that actually draws the chart.
	 */
	public ChartComponent(TableModel tm, ChartPainter cp) {
		setUI(cp);
		this.cp = cp;
		setModel(tm);
	}

	public void setTextFont(Font f) {
		cp.setTextFont(f);
	}

	public Font getTextFont() {
		return cp.getTextFont();
	}

	public void setTextColor(Color c) {
		cp.setTextColor(c);
	}

	public Color getTextColor() {
		return cp.getTextColor();
	}

	public void setColor(Color[] clist) {
		cp.setColor(clist);
	}

	public Color[] getColor() {
		return cp.getColor();
	}

	public void setColor(int index, Color c) {
		cp.setColor(index, c);
	}

	public Color getColor(int index) {
		return cp.getColor(index);
	}

	@Override
	public String getToolTipText(MouseEvent me) {
		if (tips != null) {
			int whichTip = cp.indexOfEntryAt(me);
			if (whichTip != -1) {
				return tips[whichTip];
			}
		}
		return null;
	}

	@Override
	public void tableChanged(TableModelEvent tme) {
		updateLocalValues(tme.getType() != TableModelEvent.UPDATE);
	}

	public void setModel(TableModel tm) {
		if (tm != model) {
			if (model != null) {
				model.removeTableModelListener(this);
			}
		}
		model = tm;
		model.addTableModelListener(this);
		updateLocalValues(true);
	}

	public TableModel getModel() {
		return model;
	}

	public abstract void updateLocalValues(boolean freshStart);
}
