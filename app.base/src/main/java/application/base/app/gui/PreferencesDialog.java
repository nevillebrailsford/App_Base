package application.base.app.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

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
	private JButton selectTopColourButton;
	private JLabel topColorPreview;
	private JLabel lblNewLabel_2;
	private JButton selectBottomColourButton;
	private JLabel bottomColorPreview;

	private Color topColor;
	private Color bottomColor;

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
//		contentPanel = new ColoredPanel();
		contentPanel = new JPanel();
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			instructions = new JLabel("Complete details below to set preferences");
			instructions.setFont(new Font("Tahoma", Font.PLAIN, 18));
			instructions.setHorizontalAlignment(SwingConstants.CENTER);
			instructions.setBorder(new EmptyBorder(5, 5, 5, 5));
			getContentPane().add(instructions, BorderLayout.NORTH);
		}
		contentPanel.setLayout(new FormLayout(
				new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
						FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), FormSpecs.RELATED_GAP_COLSPEC,
						FormSpecs.DEFAULT_COLSPEC, },
				new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, }));
		{
			lblNewLabel = new JLabel("Logging Level:");
			contentPanel.add(lblNewLabel, "2, 2, right, default");
		}
		{
			loggingLevel = new JComboBox<>();
			contentPanel.add(loggingLevel, "4, 2, fill, default");
		}
		{
			resetToDefault = new JButton("Reset to default");
			contentPanel.add(resetToDefault, "6, 2");
		}
		{
			sendEmailNotifications = new JCheckBox("Send notifications by email");
			contentPanel.add(sendEmailNotifications, "4, 4");
		}
		{
			lblNewLabel_1 = new JLabel("Send notifications to:");
			contentPanel.add(lblNewLabel_1, "2, 6, right, default");
		}
		{
			emailRecipients = new JTextField();
			contentPanel.add(emailRecipients, "4, 6, fill, default");
			emailRecipients.setColumns(10);
			emailRecipients.setEditable(false);
		}
		{
			editEmailList = new JButton("Edit");
			contentPanel.add(editEmailList, "6, 6");
		}
		{
			monitorNotifications = new JCheckBox("Monitor notifications");
			contentPanel.add(monitorNotifications, "4, 8");
		}
		{
			lblNewLabel_3 = new JLabel("Top Colour:");
			contentPanel.add(lblNewLabel_3, "2, 10, right, default");
		}
		{
			selectTopColourButton = new JButton("Select colour");
			contentPanel.add(selectTopColourButton, "4, 10");
		}
		{
			topColorPreview = new JLabel("  ");
			topColorPreview.setOpaque(true);
			contentPanel.add(topColorPreview, "6, 10");
		}
		{
			lblNewLabel_2 = new JLabel("Bottom Colour:");
			contentPanel.add(lblNewLabel_2, "2, 12, right, default");
		}
		{
			selectBottomColourButton = new JButton("Select Colour");
			contentPanel.add(selectBottomColourButton, "4, 12");
		}
		{
			bottomColorPreview = new JLabel("  ");
			bottomColorPreview.setOpaque(true);
			contentPanel.add(bottomColorPreview, "6, 12");
		}
		{
//			JPanel buttonPane = new BottomColoredPanel();
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("Set preferences");
				okButton.setActionCommand("Save");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton = new JButton("Finished");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
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
		selectTopColourButton.addActionListener((e) -> {
			Color tColor = JColorChooser.showDialog(this, "Choose Top Color", topColor);
			if (tColor == null) {
				return;
			}
			topColor = tColor;
			topColorPreview.setBackground(topColor);
		});
		selectBottomColourButton.addActionListener((e) -> {
			Color bColor = JColorChooser.showDialog(this, "Choose Bottom Color", bottomColor);
			if (bColor == null) {
				return;
			}
			bottomColor = bColor;
			bottomColorPreview.setBackground(bottomColor);
		});

		addLoggingLevelChoices();
		initializeFields();
		setButtonsStatus();
		topColorPreview.setBackground(topColor);
		bottomColorPreview.setBackground(bottomColor);

		pack();

		setLocationRelativeTo(parent);
		LOGGER.exiting(CLASS_NAME, "init");
	}

	private void savePreferences() {
		IniFile.store(GUIConstants.LOGGING_LEVEL, loggingLevel.getSelectedItem().toString());
		IniFile.store(GUIConstants.EMAIL_NOTIFICATION, Boolean.toString(sendEmailNotifications.isSelected()));
		IniFile.store(GUIConstants.EMAIL_LIST, emailRecipients.getText());
		IniFile.store(GUIConstants.MONITORING, Boolean.toString(monitorNotifications.isSelected()));
		IniFile.store(GUIConstants.TOP_RED, Integer.toString(topColor.getRed()));
		IniFile.store(GUIConstants.TOP_GREEN, Integer.toString(topColor.getGreen()));
		IniFile.store(GUIConstants.TOP_BLUE, Integer.toString(topColor.getBlue()));
		IniFile.store(GUIConstants.BOTTOM_RED, Integer.toString(bottomColor.getRed()));
		IniFile.store(GUIConstants.BOTTOM_GREEN, Integer.toString(bottomColor.getGreen()));
		IniFile.store(GUIConstants.BOTTOM_BLUE, Integer.toString(bottomColor.getBlue()));
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
		initializeTopColor();
		initializeBottomColor();
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

	private void initializeTopColor() {
		int red = GUIConstants.DEFAULT_TOP_COLOR.getRed();
		int green = GUIConstants.DEFAULT_TOP_COLOR.getGreen();
		int blue = GUIConstants.DEFAULT_TOP_COLOR.getBlue();
		if (!IniFile.value(GUIConstants.TOP_RED).isEmpty()) {
			red = Integer.parseInt(IniFile.value(GUIConstants.TOP_RED));
		}
		if (!IniFile.value(GUIConstants.TOP_GREEN).isEmpty()) {
			green = Integer.parseInt(IniFile.value(GUIConstants.TOP_GREEN));
		}
		if (!IniFile.value(GUIConstants.TOP_BLUE).isEmpty()) {
			blue = Integer.parseInt(IniFile.value(GUIConstants.TOP_BLUE));
		}
		topColor = new Color(red, green, blue);
	}

	private void initializeBottomColor() {
		int red = GUIConstants.DEFAULT_BOTTOM_COLOR.getRed();
		int green = GUIConstants.DEFAULT_BOTTOM_COLOR.getGreen();
		int blue = GUIConstants.DEFAULT_BOTTOM_COLOR.getBlue();
		if (!IniFile.value(GUIConstants.BOTTOM_RED).isEmpty()) {
			red = Integer.parseInt(IniFile.value(GUIConstants.BOTTOM_RED));
		}
		if (!IniFile.value(GUIConstants.BOTTOM_GREEN).isEmpty()) {
			green = Integer.parseInt(IniFile.value(GUIConstants.BOTTOM_GREEN));
		}
		if (!IniFile.value(GUIConstants.BOTTOM_BLUE).isEmpty()) {
			blue = Integer.parseInt(IniFile.value(GUIConstants.BOTTOM_BLUE));
		}
		bottomColor = new Color(red, green, blue);
	}

}
