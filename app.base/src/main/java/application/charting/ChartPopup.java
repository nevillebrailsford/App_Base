package application.charting;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.ToolTipManager;
import javax.swing.table.TableModel;

import application.base.app.gui.ColoredPanel;

/**
 * Display a chart in a new window.
 * 
 * @author neville
 * @version 3.0.0
 */
public abstract class ChartPopup extends JDialog {
	private static final long serialVersionUID = 1L;

	/**
	 * Create the window.
	 * 
	 * @param cp    - a ChartComponent that will display the chart
	 * @param tm    - a TableModel that holds the data to be displayed.
	 * @param title - a String that is used for the title of the window.
	 */
	public ChartPopup(ChartComponent cp, TableModel tm, String title) {
		setTitle(title);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setSize(500, 400);
		ColoredPanel panel = new ColoredPanel();
		panel.setLayout(new BorderLayout());
		panel.add(cp, BorderLayout.CENTER);
		getContentPane().add(panel, BorderLayout.CENTER);
		ToolTipManager.sharedInstance().registerComponent(cp);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
}
