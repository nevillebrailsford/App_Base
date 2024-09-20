package application.base.app.gui;

import java.awt.Color;

import application.definition.BaseConstants;
import application.mail.MailConstants;
import application.storage.StorageConstants;

/**
 * Provide the constants needed for graphical functions.
 * 
 * @author neville
 * @version 3.0.0
 */
public interface GUIConstants extends BaseConstants, StorageConstants, MailConstants {
	public static final String LAST_TIME = "lastTime";
	public static final String dateFormatForCalendarView = "EEE dd LLL uuuu";

	public static final String TOP_COLOR = "topcolor";
	public static final String BOTTOM_COLOR = "bottomcolor";

	// lightblue #add8e6
	public static final Color LIGHT_BLUE = new Color(173, 216, 230, 155);
	// sandybrown #f4a460
	public static final Color SANDY_BROWN = new Color(244, 164, 96, 155);
}
