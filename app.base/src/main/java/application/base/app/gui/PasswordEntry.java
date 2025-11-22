package application.base.app.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import application.security.PasswordEncryptor;
import application.security.PasswordHandler;

public class PasswordEntry extends JDialog {
	private static final long serialVersionUID = 1L;

	private final JPanel contentPanel;
	private JPasswordField passwordField;
	private JButton authorizeButton;
	private JButton cancelButton;

	public PasswordEntry(String title) {

		super();
		setModalityType(ModalityType.APPLICATION_MODAL);
		setTitle(title);
		contentPanel = new ColoredPanel();
		setLayout(new BorderLayout(10, 10));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(0, 2, 10, 10));
		addGUIComponents();
		setLocationRelativeTo(null);
		pack();
		setResizable(false);
	}

	private void addGUIComponents() {
		JLabel passwordLabel = new JLabel("Password:");
		passwordField = new JPasswordField();
		passwordField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				validatePassword();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				validatePassword();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				validatePassword();
			}
		});

		contentPanel.add(passwordLabel);
		contentPanel.add(passwordField);

		authorizeButton = new JButton("Authorize");
		authorizeButton.setEnabled(false);
		authorizeButton.addActionListener(e -> {
			if (validPassword()) {
				closeWindow();
			}
		});
		contentPanel.add(authorizeButton);
		getRootPane().setDefaultButton(authorizeButton);

		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(e -> {
			closeWindow();
		});
		contentPanel.add(cancelButton);
	}

	private void closeWindow() {
		setVisible(false);
		PasswordEntry.this.dispose();
	}

	private void validatePassword() {
		if (passwordField.getPassword().length >= 8) {
			authorizeButton.setEnabled(true);
		} else {
			authorizeButton.setEnabled(false);
		}
	}

	private boolean validPassword() {
		try {
			String checkPassword = PasswordEncryptor.instance().encrypt(String.valueOf(passwordField.getPassword()));
			return PasswordHandler.instance().validatePassword(checkPassword);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public String getPassword() throws Exception {
		return PasswordEncryptor.instance().encrypt(String.valueOf(passwordField.getPassword()));
	}
}
