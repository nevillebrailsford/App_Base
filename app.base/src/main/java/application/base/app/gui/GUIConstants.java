package application.base.app.gui;

import java.awt.Color;

import application.definition.BaseConstants;
import application.mail.MailConstants;
import application.storage.StorageConstants;

public class GUIConstants implements BaseConstants, StorageConstants, MailConstants {
	public static final String LAST_TIME = "lastTime";
	public static final String dateFormatForCalendarView = "EEE dd LLL uuuu";

	public static final String TOP_RED = "topRed";
	public static final String TOP_GREEN = "topGreen";
	public static final String TOP_BLUE = "topBlue";
	public static final String BOTTOM_RED = "bottomRed";
	public static final String BOTTOM_GREEN = "bottomGreen";
	public static final String BOTTOM_BLUE = "bottomBlue";

	public static final String DEFAULT_TOP_RED = "211";
	public static final String DEFAULT_TOP_GREEN = "211";
	public static final String DEFAULT_TOP_BLUE = "211";

	public static final String DEFAULT_BOTTOM_RED = "211";
	public static final String DEFAULT_BOTTOM_GREEN = "211";
	public static final String DEFAULT_BOTTOM_BLUE = "211";

	public static final Color DEFAULT_TOP_COLOR = new Color(Integer.parseInt(DEFAULT_TOP_RED),
			Integer.parseInt(DEFAULT_TOP_GREEN), Integer.parseInt(DEFAULT_TOP_BLUE));
	public static final Color DEFAULT_BOTTOM_COLOR = new Color(Integer.parseInt(DEFAULT_BOTTOM_RED),
			Integer.parseInt(DEFAULT_BOTTOM_GREEN), Integer.parseInt(DEFAULT_BOTTOM_BLUE));

	// lightblue #add8e6
	public static final Color LIGHT_BLUE = new Color(173, 216, 230, 155);
	// sandybrown #f4a460
	public static final Color SANDY_BROWN = new Color(244, 164, 96, 155);
}
