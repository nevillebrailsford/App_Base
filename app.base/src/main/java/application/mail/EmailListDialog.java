package application.mail;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import application.base.app.gui.BottomColoredPanel;
import application.base.app.gui.ColoredPanel;
import application.definition.ApplicationConfiguration;

/**
 * Obtain email list information.
 * 
 * @author neville
 * @version 3.0.0
 */
public class EmailListDialog extends JDialog {
	public static final int OK_PRESSED = 1;
	public static final int CANCEL_PRESSED = 0;
	private static final long serialVersionUID = 1L;

	private static final String CLASS_NAME = EmailListDialog.class.getName();
	private static Logger LOGGER = ApplicationConfiguration.logger();

	private final JPanel contentPanel;
	private JScrollPane scrollPane;
	private JList<String> emailJList;
	private JButton okButton;
	private JButton cancelButton;
	private JLabel instructions;
	private List<String> emailList;
	private int result = CANCEL_PRESSED;
	private JPanel mainPanel;
	private JPanel editPanel;
	private JTextField emailRecipient;
	private JButton addButton;
	private DefaultListModel<String> listModel;
	private JButton removeButton;

	/**
	 * Create the dialog.
	 */
	public EmailListDialog(JDialog parent, String emailRecipients) {
		super();
		LOGGER.entering(CLASS_NAME, "init");
		setTitle("Email Recipients");
		setModalityType(ModalityType.APPLICATION_MODAL);
		getContentPane().setLayout(new BorderLayout());
		contentPanel = new ColoredPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			instructions = new JLabel("Enter email recipients ");
			instructions.setFont(new Font("Tahoma", Font.PLAIN, 14));
			instructions.setHorizontalAlignment(SwingConstants.CENTER);
			instructions.setBorder(new EmptyBorder(5, 5, 5, 5));
			contentPanel.add(instructions, BorderLayout.NORTH);
		}
		{
			mainPanel = new JPanel();
			contentPanel.add(mainPanel, BorderLayout.CENTER);
			mainPanel.setLayout(new BorderLayout(0, 0));
			{
				scrollPane = new JScrollPane();
				scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
				scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
				scrollPane.setPreferredSize(new Dimension(300, 100));
				mainPanel.add(scrollPane);
				{
					listModel = new DefaultListModel<>();
					emailJList = new JList<>(listModel);
					scrollPane.setViewportView(emailJList);
				}
			}
			{
				editPanel = new BottomColoredPanel();
				editPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
				mainPanel.add(editPanel, BorderLayout.SOUTH);
				{
					emailRecipient = new JTextField();
					emailRecipient.setAlignmentX(Component.LEFT_ALIGNMENT);
					editPanel.add(emailRecipient);
					emailRecipient.setColumns(30);
				}
				{
					addButton = new JButton("Add");
					editPanel.add(addButton);
				}
				{
					removeButton = new JButton("Remove");
					editPanel.add(removeButton);
				}
			}
		}
		{
			JPanel buttonPane = new BottomColoredPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		emailRecipient.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				changeStatus();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				changeStatus();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				changeStatus();
			}
		});
		emailJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		emailJList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (emailJList.getSelectedValue() != null) {
					emailRecipient.setText(emailJList.getSelectedValue());
				} else {
					emailRecipient.setText("");
				}
			}
		});
		addButton.addActionListener((e) -> {
			if (!emailRecipient.getText().isEmpty() && !modelHasRecipient(emailRecipient.getText())) {
				((DefaultListModel<String>) emailJList.getModel()).addElement(emailRecipient.getText());
			}
			emailRecipient.setText("");

		});
		removeButton.addActionListener((e) -> {
			if (!emailRecipient.getText().isEmpty() && modelHasRecipient(emailRecipient.getText())) {
				((DefaultListModel<String>) emailJList.getModel()).removeElement(emailRecipient.getText());
			}
			emailRecipient.setText("");
		});
		okButton.addActionListener((e) -> {
			emailList = new ArrayList<>();
			for (int i = 0; i < emailJList.getModel().getSize(); i++) {
				emailList.add(emailJList.getModel().getElementAt(i));
			}
			result = OK_PRESSED;
			setVisible(false);
		});
		cancelButton.addActionListener((e) -> {
			result = CANCEL_PRESSED;
			setVisible(false);
		});
		pack();
		setLocationRelativeTo(parent);
		initializeList(emailRecipients);
		changeStatus();
		LOGGER.exiting(CLASS_NAME, "init");
	}

	public int displayAndWait() {
		LOGGER.exiting(CLASS_NAME, "displayAndWait");
		setVisible(true);
		LOGGER.exiting(CLASS_NAME, "displayAndWait", result);
		return result;
	}

	public List<String> emailList() {
		LOGGER.entering(CLASS_NAME, "emailList");
		LOGGER.exiting(CLASS_NAME, "emailList", emailList);
		return emailList;

	}

	private boolean modelHasRecipient(String recipient) {
		LOGGER.entering(recipient, "modelHasRecipient", recipient);
		boolean modelHasRecipient = false;
		for (int i = 0; i < emailJList.getModel().getSize() && !modelHasRecipient; i++) {
			if (emailJList.getModel().getElementAt(i).equalsIgnoreCase(recipient)) {
				modelHasRecipient = true;
			}
		}
		LOGGER.exiting(recipient, "modelHasRecipient", modelHasRecipient);
		return modelHasRecipient;
	}

	private void changeStatus() {
		if (emailRecipient.getText().isEmpty()) {
			addButton.setEnabled(false);
			removeButton.setEnabled(false);
		} else {
			if (emailRecipient.getText().indexOf("@") > 0) {
				addButton.setEnabled(true);
				removeButton.setEnabled(true);
			} else {
				addButton.setEnabled(false);
				removeButton.setEnabled(false);
			}
		}
	}

	private void initializeList(String emailRecipients) {
		StringTokenizer st = new StringTokenizer(emailRecipients, ",");
		while (st.hasMoreTokens()) {
			((DefaultListModel<String>) emailJList.getModel()).addElement(st.nextToken());
		}
	}
}
