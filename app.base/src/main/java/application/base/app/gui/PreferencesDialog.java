package application.base.app.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import application.definition.ApplicationConfiguration;
import application.inifile.IniFile;
import application.mail.MailConfigurer;

public class PreferencesDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	private static final String CLASS_NAME = PreferencesDialog.class.getName();
	private static Logger LOGGER = ApplicationConfiguration.logger();

	private static final String[] loggingChoices = new String[] { "ALL", "SEVERE", "WARNING", "INFO", "CONFIG", "FINE",
			"FINER", "FINEST", "OFF" };
	private static final String DEFAULT_LEVEL = "WARNING";

	private final JPanel contentPanel;
	private JLabel instructions;
	private JButton okButton;
	private JButton cancelButton;
	private JLabel lblNewLabel;
	private JComboBox<String> loggingLevel;
	private JButton resetToDefault;
	private JCheckBox sendEmailNotifications;
	private JLabel lblNewLabel_1;
	private JTextField emailRecipients;
	private JButton editEmailList;
	private JCheckBox monitorNotifications;
	private JLabel lblNewLabel_3;
	private JLabel topColorPreview;
	private JLabel lblNewLabel_2;
	private JLabel bottomColorPreview;

	private Color topColor;
	private Color bottomColor;
	private JComboBox<String> topColourChoice;
	private JComboBox<String> bottomColourChoice;

	private static final Color defaultColor = new Color(214, 227, 223);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			PreferencesDialog dialog = new PreferencesDialog(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public PreferencesDialog(JFrame parent) {
		super();
		LOGGER.entering(CLASS_NAME, "init");
		setModalityType(ModalityType.APPLICATION_MODAL);
		setTitle("Preferences");
		contentPanel = new ColoredPanel();
//		contentPanel = new JPanel();
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		instructions = new JLabel("Complete details below to set preferences");
		instructions.setFont(new Font("Tahoma", Font.PLAIN, 18));
		instructions.setHorizontalAlignment(SwingConstants.CENTER);
		instructions.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(instructions, BorderLayout.NORTH);

		contentPanel.setLayout(new GridLayout(0, 3, 10, 10));
		lblNewLabel = new JLabel("Logging Level:");
		contentPanel.add(lblNewLabel);

		loggingLevel = new JComboBox<>();
		contentPanel.add(loggingLevel);

		resetToDefault = new JButton("Reset to default");
		contentPanel.add(resetToDefault);

		contentPanel.add(new JLabel(""));
		sendEmailNotifications = new JCheckBox("Notify by email");
		contentPanel.add(sendEmailNotifications);
		contentPanel.add(new JLabel(""));

		lblNewLabel_1 = new JLabel("Send notifications to:");
		contentPanel.add(lblNewLabel_1);

		emailRecipients = new JTextField();
		contentPanel.add(emailRecipients);
		emailRecipients.setColumns(10);
		emailRecipients.setEditable(false);

		editEmailList = new JButton("Edit");
		contentPanel.add(editEmailList, "6, 6");

		contentPanel.add(new JLabel(""));
		monitorNotifications = new JCheckBox("Monitor notifications");
		contentPanel.add(monitorNotifications);
		contentPanel.add(new JLabel(""));

		lblNewLabel_3 = new JLabel("Select Top Colour:");
		contentPanel.add(lblNewLabel_3);

		topColourChoice = new JComboBox<>();
		contentPanel.add(topColourChoice);

		topColorPreview = new JLabel("  ");
		topColorPreview.setOpaque(true);
		contentPanel.add(topColorPreview);

		lblNewLabel_2 = new JLabel("Select Bottom Colour:");
		contentPanel.add(lblNewLabel_2);

		bottomColourChoice = new JComboBox<>();
		contentPanel.add(bottomColourChoice);

		bottomColorPreview = new JLabel("  ");
		bottomColorPreview.setOpaque(true);
		contentPanel.add(bottomColorPreview);

		JPanel buttonPane = new BottomColoredPanel();
//			JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		okButton = new JButton("Set preferences");
		okButton.setActionCommand("Save");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		cancelButton = new JButton("Finished");
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);

		additionalGUIItems(contentPanel);

		okButton.addActionListener((e) -> {
			savePreferences();
			setVisible(false);
		});
		cancelButton.addActionListener((e) -> {
			setVisible(false);
		});
		resetToDefault.addActionListener((e) -> {
			loggingLevel.setSelectedItem(DEFAULT_LEVEL);
		});
		sendEmailNotifications.addActionListener((e) -> {
			if (sendEmailNotifications.isSelected()) {
				if (!MailConfigurer.instance().isValidConfiguration()) {
					String title = "Email Configuration Incomplete";
					String message = "Email notification cannot be enabled at this time." + "\n"
							+ "Please create mail.properties file in " + ApplicationConfiguration.rootDirectory();
					JOptionPane.showMessageDialog(this, message, title, JOptionPane.WARNING_MESSAGE);
					sendEmailNotifications.setSelected(false);
				}
			}
			setButtonsStatus();
		});
		editEmailList.addActionListener((e) -> {
			EmailListDialog dialog = new EmailListDialog(this, emailRecipients.getText());
			int result = dialog.displayAndWait();
			if (result == EmailListDialog.OK_PRESSED) {
				List<String> emailList = dialog.emailList();
				StringBuilder sb = new StringBuilder();
				String comma = "";
				for (String email : emailList) {
					sb.append(comma);
					sb.append(email);
					comma = ",";
				}
				emailRecipients.setText(sb.toString());
			}
			dialog.dispose();
		});
		topColourChoice.addActionListener((e) -> {
			topColor = getSelectedColor(topColourChoice);
			topColorPreview.setBackground(topColor);
		});
		bottomColourChoice.addActionListener((e) -> {
			bottomColor = getSelectedColor(bottomColourChoice);
			bottomColorPreview.setBackground(bottomColor);
		});

		additionalActionListeners();

		addLoggingLevelChoices();
		initializeFields();
		setButtonsStatus();

		pack();

		setLocationRelativeTo(parent);
		LOGGER.exiting(CLASS_NAME, "init");
	}

	private void savePreferences() {
		IniFile.store(GUIConstants.LOGGING_LEVEL, loggingLevel.getSelectedItem().toString());
		IniFile.store(GUIConstants.EMAIL_NOTIFICATION, Boolean.toString(sendEmailNotifications.isSelected()));
		IniFile.store(GUIConstants.EMAIL_LIST, emailRecipients.getText());
		IniFile.store(GUIConstants.MONITORING, Boolean.toString(monitorNotifications.isSelected()));
		IniFile.store(GUIConstants.TOP_COLOR, (String) topColourChoice.getSelectedItem());
		IniFile.store(GUIConstants.BOTTOM_COLOR, (String) bottomColourChoice.getSelectedItem());

		saveAdditionalPreferences();
	}

	/**
	 * Add items to the GUI. This method is called during initialisation of the GUI,
	 * and is used to add any additional GUI items that the application may require,
	 * over and above those already saved.
	 * 
	 * @param contentPanel that already contains default preferences.
	 */
	public void additionalGUIItems(JPanel contentPanel) {
	}

	/**
	 * Add action listeners to additional items. This method is called during
	 * initialisation of the GUI, and is used to add action listeners to any items
	 * added by additionalGUIItems.
	 */
	public void additionalActionListeners() {
	}

	/**
	 * Save additional preferences. This method is called when the user requests
	 * that the preferences chosen be saved.
	 */
	public void saveAdditionalPreferences() {
	}

	/**
	 * Additional preferences have valid values. This method can be called by
	 * classes extending this class to allow the preferences to be saved while in a
	 * valid state.
	 */
	public void validInput() {
		okButton.setEnabled(true);
	}

	/**
	 * Additional preferences have invalid values. This method can be called by
	 * classes extending this class to prevent the preferences being saved while in
	 * an invalid state.
	 */
	public void invalidInput() {
		okButton.setEnabled(false);
	}

	private void initializeFields() {
		String applicationLevel = ApplicationConfiguration.applicationDefinition().level().toString();
		loggingLevel.setSelectedItem(applicationLevel);
		if (!IniFile.value(GUIConstants.LOGGING_LEVEL).isEmpty()) {
			loggingLevel.setSelectedItem(IniFile.value(GUIConstants.LOGGING_LEVEL));
		}
		sendEmailNotifications.setSelected(Boolean.valueOf(IniFile.value(GUIConstants.EMAIL_NOTIFICATION)));
		emailRecipients.setText(IniFile.value(GUIConstants.EMAIL_LIST));
		monitorNotifications.setSelected(Boolean.valueOf(IniFile.value(GUIConstants.MONITORING)));
		initializeColorChoice(topColourChoice);
		if (!IniFile.value(GUIConstants.TOP_COLOR).isEmpty()) {
			topColourChoice.setSelectedItem(IniFile.value(GUIConstants.TOP_COLOR));
		}
		initializeColorChoice(bottomColourChoice);
		if (!IniFile.value(GUIConstants.BOTTOM_COLOR).isEmpty()) {
			bottomColourChoice.setSelectedItem(IniFile.value(GUIConstants.BOTTOM_COLOR));
		}
	}

	private void addLoggingLevelChoices() {
		for (int i = 0; i < loggingChoices.length; i++) {
			loggingLevel.addItem(loggingChoices[i]);
		}
	}

	private void setButtonsStatus() {
		if (sendEmailNotifications.isSelected()) {
			editEmailList.setEnabled(true);
		} else {
			editEmailList.setEnabled(false);
		}
	}

	private void initializeColorChoice(JComboBox<String> choice) {
		String[] colors = ColorProvider.name;
		choice.addItem("default");
		for (int i = 0; i < colors.length; i++) {
			choice.addItem(colors[i]);
		}
		choice.setSelectedIndex(0);
	}

	private Color getSelectedColor(JComboBox<String> choice) {
		Color result = null;
		if (choice.getSelectedItem().equals("default")) {
			result = defaultColor;
		} else {
			result = ColorProvider.get((String) choice.getSelectedItem());
		}
		return result;
	}

}
