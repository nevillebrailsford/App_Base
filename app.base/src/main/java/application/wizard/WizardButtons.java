package application.wizard;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import application.base.app.gui.BottomColoredPanel;

class WizardButtons extends BottomColoredPanel {
	private static final long serialVersionUID = 1L;

	/** The left button. */
	private JButton left;

	/** The first button on the right of the panel. */
	private JButton right1;

	/** The second button on the right of the panel. */
	private JButton right2;

	/** The third button on the right of the panel. */
	private JButton right3;

	/**
	 * Standard constructor - creates panel with the specified button labels.
	 * 
	 * @param label1 the label for button 1.
	 * @param label2 the label for button 2.
	 * @param label3 the label for button 3.
	 * @param label4 the label for button 4.
	 */
	public WizardButtons(final String label1, final String label2, final String label3, final String label4) {

		setLayout(new BorderLayout());

		// create the pieces...
		final JPanel panel = new BottomColoredPanel(new BorderLayout());
		final JPanel panel2 = new BottomColoredPanel(new BorderLayout());
		this.left = new JButton(label1);
		this.right1 = new JButton(label2);
		this.right2 = new JButton(label3);
		this.right3 = new JButton(label4);

		// ...and put them together
		panel.add(this.left, BorderLayout.WEST);
		panel2.add(this.right1, BorderLayout.EAST);
		panel.add(panel2, BorderLayout.CENTER);
		panel.add(this.right2, BorderLayout.EAST);
		add(panel, BorderLayout.CENTER);
		add(this.right3, BorderLayout.EAST);

	}

	/**
	 * Returns a reference to button 1, allowing the caller to set labels,
	 * action-listeners etc.
	 * 
	 * @return the left button.
	 */
	public JButton leftButton() {
		return this.left;
	}

	/**
	 * Returns a reference to button 2, allowing the caller to set labels,
	 * action-listeners etc.
	 * 
	 * @return the right button 1.
	 */
	public JButton rightButton1() {
		return this.right1;
	}

	/**
	 * Returns a reference to button 3, allowing the caller to set labels,
	 * action-listeners etc.
	 * 
	 * @return the right button 2.
	 */
	public JButton rightButton2() {
		return this.right2;
	}

	/**
	 * Returns a reference to button 4, allowing the caller to set labels,
	 * action-listeners etc.
	 * 
	 * @return the right button 3.
	 */
	public JButton rightButton3() {
		return this.right3;
	}

}