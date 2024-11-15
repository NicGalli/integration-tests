package com.example.school.view.swing;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.example.school.model.Student;
import com.example.school.view.StudentView;
import javax.swing.JButton;
import javax.swing.JList;

public class StudentSwingView extends JFrame implements StudentView {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtIdtextbox;
	private JLabel lblName;
	private JTextField textField;
	private JButton btnAdd;
	private JList list;
	private JButton btnDeleteSelected;
	private JLabel label;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StudentSwingView frame = new StudentSwingView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public StudentSwingView() {
		setTitle("Student View");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 159, 12, 114, 0 };
		gbl_contentPane.rowHeights = new int[] { 21, 0, 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JLabel lblId = new JLabel("id");
		GridBagConstraints gbc_lblId = new GridBagConstraints();
		gbc_lblId.insets = new Insets(0, 0, 5, 5);
		gbc_lblId.gridx = 0;
		gbc_lblId.gridy = 1;
		contentPane.add(lblId, gbc_lblId);

		txtIdtextbox = new JTextField();
		txtIdtextbox.setName("idTextBox");
		GridBagConstraints gbc_txtIdtextbox = new GridBagConstraints();
		gbc_txtIdtextbox.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtIdtextbox.insets = new Insets(0, 0, 5, 5);
		gbc_txtIdtextbox.gridx = 1;
		gbc_txtIdtextbox.gridy = 1;
		contentPane.add(txtIdtextbox, gbc_txtIdtextbox);
		txtIdtextbox.setColumns(10);

		lblName = new JLabel("name");
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.gridx = 0;
		gbc_lblName.gridy = 2;
		contentPane.add(lblName, gbc_lblName);

		textField = new JTextField();
		textField.setName("nameTextBox");
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 2;
		contentPane.add(textField, gbc_textField);
		textField.setColumns(10);
		
		btnAdd = new JButton("Add");
		btnAdd.setEnabled(false);
		GridBagConstraints gbc_btnAdd = new GridBagConstraints();
		gbc_btnAdd.insets = new Insets(0, 0, 5, 5);
		gbc_btnAdd.gridx = 0;
		gbc_btnAdd.gridy = 3;
		contentPane.add(btnAdd, gbc_btnAdd);
		
		btnDeleteSelected = new JButton("Delete Selected");
		GridBagConstraints gbc_btnDeleteSelected = new GridBagConstraints();
		gbc_btnDeleteSelected.insets = new Insets(0, 0, 5, 5);
		gbc_btnDeleteSelected.gridx = 1;
		gbc_btnDeleteSelected.gridy = 3;
		contentPane.add(btnDeleteSelected, gbc_btnDeleteSelected);
		
		label = new JLabel(" ");
		label.setName("errorMessageLabel");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(0, 0, 5, 0);
		gbc_label.gridx = 2;
		gbc_label.gridy = 3;
		contentPane.add(label, gbc_label);
		
		list = new JList();
		list.setName("studentList");
		GridBagConstraints gbc_list = new GridBagConstraints();
		gbc_list.insets = new Insets(0, 0, 0, 5);
		gbc_list.fill = GridBagConstraints.BOTH;
		gbc_list.gridx = 1;
		gbc_list.gridy = 4;
		contentPane.add(list, gbc_list);
	}

	@Override
	public void showAllStudents(List<Student> students) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showError(String message, Student student) {
		// TODO Auto-generated method stub

	}

	@Override
	public void studentAdded(Student student) {
		// TODO Auto-generated method stub

	}

	@Override
	public void studentRemoved(Student student) {
		// TODO Auto-generated method stub

	}

}
