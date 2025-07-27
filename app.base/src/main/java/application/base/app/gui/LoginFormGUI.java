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
import application.security.DBUser;
import application.security.PasswordEncryptor;

public class LoginFormGUI extends JDialog {
	private static final long serialVersionUID = 1L;

	private final JPanel contentPanel;
	private JTextField usernameField;
	private JPasswordField passwordField;

	public LoginFormGUI(String title) {
		super();
		setModalityType(ModalityType.APPLICATION_MODAL);
		setTitle(title);
		contentPanel = new JPanel();
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
		JLabel usernameLabel = new JLabel("Username:");
		usernameField = new JTextField();
		contentPanel.add(usernameLabel);
		contentPanel.add(usernameField);

		JLabel passwordLabel = new JLabel("Password:");
		passwordField = new JPasswordField();

		contentPanel.add(passwordLabel);
		contentPanel.add(passwordField);

		JButton loginButton = new JButton("Login");
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String username = usernameField.getText();
				String password = new String(passwordField.getPassword());
				try {
					String encryptedPassword = PasswordEncryptor.instance().encrypt(password);
					if (DBAccess.validateLogin(username, encryptedPassword)) {
						DBUser.instance().setUsername(usernameField.getText());
						DBUser.instance().setPassword(encryptedPassword);
						setVisible(false);
						LoginFormGUI.this.dispose();
					} else {
						DBUser.instance().setUsername(null);
						DBUser.instance().setPassword(null);
						JOptionPane.showMessageDialog(LoginFormGUI.this, "Login Failed...please register");
					}
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(LoginFormGUI.this, "Exception caught " + e1.getMessage());
				}
			}
		});
		contentPanel.add(loginButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DBUser.instance().setUsername(null);
				DBUser.instance().setPassword(null);
				setVisible(false);
				LoginFormGUI.this.dispose();
			}
		});
		contentPanel.add(cancelButton);
		JLabel registerLabel = new JLabel("Not a user? Register Here.");
		registerLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				LoginFormGUI.this.dispose();

				new RegisterFormGUI("Register").setVisible(true);
			}
		});
		contentPanel.add(registerLabel);
	}

	public void setUsername(String username) {
		usernameField.setText(username);
	}

	public void setPassword(String password) {
		passwordField.setText(password);
	}

}
