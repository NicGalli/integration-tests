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

public class StudentSwingView extends JFrame implements StudentView{

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtIdtextbox;

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
		gbl_contentPane.columnWidths = new int[] {159, 12, 114, 0};
		gbl_contentPane.rowHeights = new int[] {21, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JLabel lblId = new JLabel("id");
		GridBagConstraints gbc_lblId = new GridBagConstraints();
		gbc_lblId.anchor = GridBagConstraints.SOUTHWEST;
		gbc_lblId.insets = new Insets(0, 0, 0, 5);
		gbc_lblId.gridx = 1;
		gbc_lblId.gridy = 0;
		contentPane.add(lblId, gbc_lblId);
		
		txtIdtextbox = new JTextField();
		txtIdtextbox.setText("idTextBox");
		GridBagConstraints gbc_txtIdtextbox = new GridBagConstraints();
		gbc_txtIdtextbox.anchor = GridBagConstraints.NORTHWEST;
		gbc_txtIdtextbox.gridx = 2;
		gbc_txtIdtextbox.gridy = 0;
		contentPane.add(txtIdtextbox, gbc_txtIdtextbox);
		txtIdtextbox.setColumns(10);
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
