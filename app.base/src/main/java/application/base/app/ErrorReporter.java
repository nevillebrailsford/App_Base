package application.base.app;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class ErrorReporter {
	public static void displayError(String message) {
		System.out.println(message);
		if (SwingUtilities.isEventDispatchThread()) {
			JOptionPane.showMessageDialog(null, message, "Error has occurred", JOptionPane.ERROR_MESSAGE);
		} else {
			try {
				SwingUtilities.invokeAndWait(() -> {
					JOptionPane.showMessageDialog(null, message, "Error has occurred", JOptionPane.ERROR_MESSAGE);
				});
			} catch (Exception e1) {
			}
		}
	}
}
