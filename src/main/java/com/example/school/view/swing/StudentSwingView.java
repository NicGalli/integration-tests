package com.example.school.view.swing;

import static javax.swing.SwingUtilities.invokeLater;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.example.school.controller.SchoolController;
import com.example.school.model.Student;
import com.example.school.view.StudentView;

public class StudentSwingView extends JFrame implements StudentView {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtIdtextbox;
	private JLabel lblName;
	private JTextField nameTextField;
	private JButton btnAdd;
	private JList<Student> listStudents;
	private DefaultListModel<Student> listStudentsModel;

	DefaultListModel<Student> getListStudentsModel() {
		return listStudentsModel;
	}

	private JButton btnDeleteSelected;
	private JLabel label;

	JLabel getLabel() {
		return label;
	}

	private JScrollPane scrollPane;
	private SchoolController schoolController;

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
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Exception", e);
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public StudentSwingView() {
		listStudentsModel = new DefaultListModel<>();
		listStudents = new JList<>(listStudentsModel);
		setTitle("Student View");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 159, 12, 114, 0 };
		gbl_contentPane.rowHeights = new int[] { 21, 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 1.0, 1.0, 0.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JLabel lblId = new JLabel("id");
		GridBagConstraints gbc_lblId = new GridBagConstraints();
		gbc_lblId.insets = new Insets(0, 0, 5, 5);
		gbc_lblId.gridx = 0;
		gbc_lblId.gridy = 1;
		contentPane.add(lblId, gbc_lblId);

		txtIdtextbox = new JTextField();
		KeyAdapter addBtnEnabler = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				btnAdd.setEnabled(!txtIdtextbox.getText().isBlank() && !nameTextField.getText().isBlank());
			}

		};
		txtIdtextbox.addKeyListener(addBtnEnabler);
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

		nameTextField = new JTextField();
		nameTextField.addKeyListener(addBtnEnabler);
		nameTextField.setName("nameTextBox");
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 2;
		contentPane.add(nameTextField, gbc_textField);
		nameTextField.setColumns(10);

		btnAdd = new JButton("Add");
		btnAdd.setEnabled(false);
		GridBagConstraints gbc_btnAdd = new GridBagConstraints();
		gbc_btnAdd.insets = new Insets(0, 0, 5, 5);
		gbc_btnAdd.gridx = 0;
		gbc_btnAdd.gridy = 3;
		contentPane.add(btnAdd, gbc_btnAdd);

		btnAdd.addActionListener(
				e -> schoolController.newStudent(new Student(txtIdtextbox.getText(), nameTextField.getText())));

		btnDeleteSelected = new JButton("Delete Selected");
		btnDeleteSelected.setEnabled(false);
		GridBagConstraints gbc_btnDeleteSelected = new GridBagConstraints();
		gbc_btnDeleteSelected.insets = new Insets(0, 0, 5, 5);
		gbc_btnDeleteSelected.gridx = 1;
		gbc_btnDeleteSelected.gridy = 3;
		contentPane.add(btnDeleteSelected, gbc_btnDeleteSelected);

		label = new JLabel(" ");
		label.setEnabled(false);
		label.setName("errorMessageLabel");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 1;
		gbc_label.gridy = 4;
		contentPane.add(label, gbc_label);

		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 5;
		contentPane.add(scrollPane, gbc_scrollPane);

		scrollPane.setViewportView(listStudents);
		listStudents.setName("studentList");

		listStudents.addListSelectionListener(e -> btnDeleteSelected.setEnabled(listStudents.getSelectedIndex() != -1));
		btnDeleteSelected.addActionListener(e -> schoolController.deleteStudent(listStudents.getSelectedValue()));
	}

	@Override
	public void showAllStudents(List<Student> students) {
		listStudentsModel.addAll(students);
	}

	@Override
	public void showError(String message, Student student) {
		invokeLater(() -> label.setText(message + student.toString()));

	}

	@Override
	public void studentAdded(Student student) {
		invokeLater(() -> {
			resetErrorLabel();
			listStudentsModel.addElement(student);
		});

	}

	private void resetErrorLabel() {
		label.setText(" ");
	}

	@Override
	public void studentRemoved(Student student) {
		resetErrorLabel();
		listStudentsModel.removeElement(student);
	}

	public void setSchoolController(SchoolController schoolController) {
		this.schoolController = schoolController;
	}

}
