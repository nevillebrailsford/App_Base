package application.base.app.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import application.security.DBAccess;
import application.security.PasswordEncryptor;

public class RegisterFormGUI extends JDialog {
	private static final long serialVersionUID = 1L;

	private final JPanel contentPanel;

	public RegisterFormGUI(String title) {
		super();
		setModalityType(ModalityType.APPLICATION_MODAL);
		setTitle(title);
		contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(0, 2, 10, 10));
		addGUIComponents();
		setLocationRelativeTo(null);
		pack();
		setResizable(false);
	}

	private void addGUIComponents() {

		JLabel usernameLabel = new JLabel("Username:");
		JTextField usernameField = new JTextField();

		contentPanel.add(usernameLabel);
		contentPanel.add(usernameField);

		JLabel passwordLabel = new JLabel("Password:");
		JPasswordField passwordField = new JPasswordField();

		contentPanel.add(passwordLabel);
		contentPanel.add(passwordField);

		JLabel rePasswordLabel = new JLabel("Re-enter Password:");
		JPasswordField rePasswordField = new JPasswordField();

		contentPanel.add(rePasswordLabel);
		contentPanel.add(rePasswordField);

		JButton registerButton = new JButton("Register");
		registerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String username = usernameField.getText();
				String password = new String(passwordField.getPassword());
				String rePassword = new String(rePasswordField.getPassword());

				if (validateUserInput(username, password, rePassword)) {
					try {
						String encryptedPass = PasswordEncryptor.instance().encrypt(password);
						if (DBAccess.register(username, encryptedPass)) {

							RegisterFormGUI.this.dispose();

							LoginFormGUI loginFormGUI = new LoginFormGUI("Login");
							loginFormGUI.setUsername(username);
							loginFormGUI.setPassword(password);
							loginFormGUI.setVisible(true);

						} else {
							JOptionPane.showMessageDialog(RegisterFormGUI.this, "Error: Username already taken.");
						}
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(registerButton, "Exception caught: " + e1.getMessage());
						e1.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(RegisterFormGUI.this,
							"Error: Username must be at least 6 characters \nand/or password must match.");
				}
			}
		});
		contentPanel.add(registerButton);
		contentPanel.add(new JLabel(" "));

		JLabel loginLabel = new JLabel("Have an account? Login Here.");
		loginLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				RegisterFormGUI.this.dispose();

				new LoginFormGUI("Login").setVisible(true);
			}
		});
		contentPanel.add(loginLabel);
	}

	private boolean validateUserInput(String username, String password, String rePassword) {
		if (username.length() == 0 || password.length() == 0 || rePassword.length() == 0) {
			return false;
		}
		if (username.length() < 6) {
			return false;
		}
		if (!password.equals(rePassword)) {
			return false;
		}
		return true;
	}
}
