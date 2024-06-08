package application.wizard;

import java.awt.LayoutManager;

import application.base.app.gui.ColoredPanel;

public abstract class WizardPage extends ColoredPanel {
	private static final long serialVersionUID = 1L;

	/** The owner. */
	private WizardDialog owner;

	public Object result = null;

	/**
	 * Creates a new panel.
	 * 
	 * @param layout the layout manager.
	 */
	protected WizardPage(final LayoutManager layout) {
		super(layout);
	}

	/**
	 * Returns a reference to the dialog that owns the panel.
	 * 
	 * @return the owner.
	 */
	public WizardDialog owner() {
		return this.owner;
	}

	/**
	 * Sets the reference to the dialog that owns the panel (this is called
	 * automatically by the dialog when the panel is added to the dialog).
	 * 
	 * @param owner the owner.
	 */
	public void setOwner(final WizardDialog owner) {
		this.owner = owner;
	}

	/**
	 * Returns the result.
	 * 
	 * @return the result.
	 */
	public Object result() {
		return result;
	}

	/**
	 * Tell the current page to save its data.
	 */
	public abstract void save();

	/**
	 * This method is called when the dialog redisplays this panel as a result of
	 * the user clicking the "Previous" button. Inside this method, subclasses
	 * should make a note of their current state, so that they can decide what to do
	 * when the user hits "Next".
	 */
	public abstract void returnFromLaterStep();

	/**
	 * Returns true if it is OK to redisplay the last version of the next page, or
	 * false if a new version is required.
	 * 
	 * @return boolean.
	 */
	public abstract boolean canRedisplayNextPage();

	/**
	 * Returns true if there is a next page.
	 * 
	 * @return boolean.
	 */
	public abstract boolean hasNextPage();

	/**
	 * Returns true if it is possible to finish from this page.
	 * 
	 * @return boolean.
	 */
	public abstract boolean canFinish();

	/**
	 * Returns the next page in the sequence, given the current user input. Returns
	 * null if this page is the last one in the sequence.
	 * 
	 * @return the next page in the sequence.
	 */
	public abstract WizardPage nextPage();

}
