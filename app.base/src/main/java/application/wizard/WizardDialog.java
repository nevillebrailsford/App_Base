package application.wizard;

import java.awt.BorderLayout;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import application.base.app.gui.ColoredPanel;

/**
 *
 */
public class WizardDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	/** The end result of the wizard sequence. */
	private Object result;

	/** The current step in the wizard process (starting at step zero). */
	private int step;

	/** A reference to the current page. */
	public WizardPage currentPage;

	/**
	 * A list of references to the pages the user has already seen - used for
	 * navigating through the steps that have already been completed.
	 */
	public List<WizardPage> pages;

	/** A handy reference to the "previous" button. */
	public JButton previousButton;

	/** A handy reference to the "next" button. */
	public JButton nextButton;

	/** A handy reference to the "finish" button. */
	public JButton finishButton;

	/** A handy reference to the "help" button. */
	public JButton helpButton;

	/**
	 * Standard constructor - builds and returns a new WizardDialog.
	 * 
	 * @param owner     the owner.
	 * @param modal     modal?
	 * @param title     the title.
	 * @param firstPage the first page.
	 */
	public WizardDialog(final JDialog owner, final boolean modal, final String title, final WizardPage firstPage) {

		super(owner, title, modal);
		initGUI(firstPage);
	}

	/**
	 * Standard constructor - builds a new WizardDialog owned by the specified
	 * JFrame.
	 * 
	 * @param owner     the owner.
	 * @param modal     modal?
	 * @param title     the title.
	 * @param firstPage the first panel.
	 */
	public WizardDialog(final JFrame owner, final boolean modal, final String title, final WizardPage firstPage) {

		super(owner, title, modal);
		initGUI(firstPage);
	}

	/**
	 * Returns the result of the wizard sequence.
	 * 
	 * @return the result.
	 */
	public Object result() {
		return this.result;
	}

	/**
	 * Returns the total number of steps in the wizard sequence, if this number is
	 * known. Otherwise this method returns zero. Subclasses should override this
	 * method unless the number of steps is not known.
	 * 
	 * @return the number of steps.
	 */
	public int stepCount() {
		return pages.size();
	}

	/**
	 * Returns true if it is possible to back up to the previous page, and false
	 * otherwise.
	 * 
	 * @return boolean.
	 */
	public boolean canDoPreviousPage() {
		return (this.step > 0);
	}

	/**
	 * Returns true if there is a 'next' page, and false otherwise.
	 * 
	 * @return boolean.
	 */
	public boolean canDoNextPage() {
		return this.currentPage.hasNextPage();
	}

	/**
	 * Returns true if it is possible to finish the sequence at this point (possibly
	 * with defaults for the remaining entries).
	 * 
	 * @return boolean.
	 */
	public boolean canFinish() {
		return this.currentPage.canFinish();
	}

	/**
	 * Returns the page for the specified step (steps are numbered from zero).
	 * 
	 * @param step the current step.
	 * 
	 * @return the page.
	 */
	public WizardPage wizardPage(final int step) {
		if (step < this.pages.size()) {
			return this.pages.get(step);
		} else {
			return null;
		}
	}

	/**
	 * Handles a click on the "previous" button, by displaying the previous page in
	 * the sequence.
	 */
	public void previous() {
		if (this.step > 0) {
			final WizardPage previousPage = wizardPage(this.step - 1);
			// tell the page that we are returning
			previousPage.returnFromLaterStep();
			final Container content = getContentPane();
			content.remove(this.currentPage);
			content.add(previousPage);
			this.step = this.step - 1;
			this.currentPage = previousPage;
			enableButtons();
			doLayout();
			update(getGraphics());
			pack();
		}
	}

	/**
	 * Displays the next step in the wizard sequence.
	 */
	public void next() {

		WizardPage nextPage = wizardPage(this.step + 1);
		if (nextPage != null) {
			if (!this.currentPage.canRedisplayNextPage()) {
				nextPage = this.currentPage.nextPage();
			}
		} else {
			nextPage = this.currentPage.nextPage();
		}

		this.step = this.step + 1;
		if (this.step < this.pages.size()) {
			this.pages.set(this.step, nextPage);
		} else {
			this.pages.add(nextPage);
		}

		final Container content = getContentPane();
		content.remove(this.currentPage);
		content.add(nextPage);

		this.currentPage = nextPage;
		enableButtons();
		pack();

	}

	/**
	 * Finishes the wizard.
	 */
	public void finish() {
		this.result = this.currentPage.result();
		setVisible(false);
	}

	/**
	 * Display any help information.
	 */
	public void help() {

	}

	/**
	 * Checks, whether the user cancelled the dialog.
	 * 
	 * @return false.
	 */
	public boolean isCancelled() {
		return true;
	}

	/**
	 * Creates a panel containing the user interface for the dialog.
	 * 
	 * @return the panel.
	 */
	public JPanel createContent() {

		final JPanel content = new ColoredPanel(new BorderLayout());
		content.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		content.add((JPanel) this.pages.get(0));
		final WizardButtons buttons = new WizardButtons("Help", "Previous", "Next", "Finish");

		this.helpButton = buttons.leftButton();
		this.helpButton.setEnabled(false);

		this.previousButton = buttons.rightButton1();
		this.previousButton.setActionCommand("previousButton");
		this.previousButton.setEnabled(false);

		this.nextButton = buttons.rightButton2();
		this.nextButton.setActionCommand("nextButton");
		this.nextButton.setEnabled(true);

		this.finishButton = buttons.rightButton3();
		this.finishButton.setActionCommand("finishButton");
		this.finishButton.setEnabled(false);

		buttons.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
		content.add(buttons, BorderLayout.SOUTH);

		return content;
	}

	/**
	 * Add action listeners to each of the buttons.
	 */
	private void addListeners() {
		helpButton.addActionListener((event) -> {
			help();
		});
		previousButton.addActionListener((event) -> {
			previous();
		});
		nextButton.addActionListener((event) -> {
			next();
		});
		finishButton.addActionListener((event) -> {
			finish();
		});
	}

	/**
	 * Enables/disables the buttons according to the current step. A good idea would
	 * be to ask the panels to return the status...
	 */
	private void enableButtons() {
		this.previousButton.setEnabled(this.step > 0);
		this.nextButton.setEnabled(canDoNextPage());
		this.finishButton.setEnabled(canFinish());
		this.helpButton.setEnabled(false);
	}

	/**
	 * Initialise the view and set up the first page
	 * 
	 * @param firstPage
	 */
	private void initGUI(final WizardPage firstPage) {
		this.result = null;
		this.currentPage = firstPage;
		this.step = 0;
		this.pages = new ArrayList<>();
		this.pages.add(firstPage);
		setContentPane(createContent());
		addListeners();
		enableButtons();
		setLocationRelativeTo(null);
		pack();
	}

}
