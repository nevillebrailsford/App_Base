package application.replicate;

import java.util.Vector;
import java.util.logging.Logger;

import application.change.ChangeManager;
import application.definition.ApplicationConfiguration;

public class CopyAndPaste {

	private static final String CLASS_NAME = ChangeManager.class.getName();
	private static final Logger LOGGER = ApplicationConfiguration.logger();

	private static CopyAndPaste instance = null;
	private Object object = null;
	private Vector<CopyAndPasteListener> listeners = null;

	public synchronized static CopyAndPaste instance() {
		if (instance == null) {
			instance = new CopyAndPaste();
		}
		return instance;
	}

	private CopyAndPaste() {
		listeners = new Vector<>();
	}

	public void addListener(CopyAndPasteListener l) {
		listeners.addElement(l);
	}

	public void removeListener(CopyAndPasteListener l) {
		listeners.removeElement(l);
	}

	public void copy(Object object) {
		LOGGER.entering(CLASS_NAME, "copy", object);
		this.object = object;
		fireCopyChanged();
		LOGGER.exiting(CLASS_NAME, "copy");
	}

	public Object paste() {
		LOGGER.entering(CLASS_NAME, "paste");
		LOGGER.exiting(CLASS_NAME, "paste");
		return object;
	}

	public void reset() {
		LOGGER.entering(CLASS_NAME, "reset");
		object = null;
		LOGGER.exiting(CLASS_NAME, "reset");
	}

	private void fireCopyChanged() {
		LOGGER.entering(CLASS_NAME, "fireCopyChanged");
		for (CopyAndPasteListener cpl : listeners) {
			cpl.copyChanged();
		}
		LOGGER.exiting(CLASS_NAME, "fireCopyChanged");
	}

}
