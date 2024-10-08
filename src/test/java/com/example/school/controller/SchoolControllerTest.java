package com.example.school.controller;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.ignoreStubs;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.school.model.Student;
import com.example.school.repository.StudentRepository;
import com.example.school.view.StudentView;

public class SchoolControllerTest {

	@Mock
	StudentRepository studentRepository;

	@Mock
	StudentView studentView;

	@InjectMocks
	SchoolController schoolController;

	AutoCloseable closeable;

	@BeforeEach
	void setup() {
		closeable = MockitoAnnotations.openMocks(this);
	}

	@AfterEach
	void releaseMocks() throws Exception {
		closeable.close();
	}

	@Test
	void testAllStudents() {
		List<Student> allStudents = asList(new Student());
		when(studentRepository.findAll()).thenReturn(allStudents);
		schoolController.allStudents();
		verify(studentView).showAllStudents(allStudents);
	}

	@Test
	public void testNewStudentWhenStudentDoesNotAlreadyExist() {
		Student student = new Student("1", "test");
		when(studentRepository.findById("1")).thenReturn(null);
		schoolController.newStudent(student);
		InOrder inOrder = inOrder(studentRepository, studentView);
		inOrder.verify(studentRepository).save(student);
		inOrder.verify(studentView).studentAdded(student);
	}

	@Test
	public void testNewStudentWhenStudentAlreadyExists() {
		Student studentToAdd = new Student("1", "test");
		Student existingStudent = new Student("1", "name");
		when(studentRepository.findById("1")).thenReturn(existingStudent);
		schoolController.newStudent(studentToAdd);
		verify(studentView).showError("Already existing student with id 1", existingStudent);
		verifyNoMoreInteractions(ignoreStubs(studentRepository));
	}

	@Test
	public void testDeleteStudentWhenStudentExists() {
		Student studentToDelete = new Student("1", "test");
		when(studentRepository.findById("1")).thenReturn(studentToDelete);
		schoolController.deleteStudent(studentToDelete);
		InOrder inOrder = inOrder(studentRepository, studentView);
		inOrder.verify(studentRepository).delete("1");
		inOrder.verify(studentView).studentRemoved(studentToDelete);
	}

	@Test
	public void testDeleteStudentWhenStudentDoesNotExist() {
		Student student = new Student("1", "test");
		when(studentRepository.findById("1")).thenReturn(null);
		schoolController.deleteStudent(student);
		verify(studentView).showError("No existing student with id 1", student);
		verifyNoMoreInteractions(ignoreStubs(studentRepository));
	}

}
