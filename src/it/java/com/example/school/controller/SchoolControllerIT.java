package com.example.school.controller;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.school.model.Student;
import com.example.school.repository.StudentRepository;
import com.example.school.repository.mongo.StudentMongoRepository;
import com.example.school.view.StudentView;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

public class SchoolControllerIT {
	@Mock
	StudentView studentView;
	StudentRepository studentRepository;
	SchoolController schoolController;

	AutoCloseable closeable;
	static int mongoPort = portNumber();

	private static int portNumber() {
		if (System.getProperty("mongo.port") == null) {
			return 27017;
		} else {
			return Integer.parseInt(System.getProperty("mongo.port", "27017"));
		}
	}

	@BeforeEach
	void setup() {
		closeable = MockitoAnnotations.openMocks(this);
		studentRepository = new StudentMongoRepository(new MongoClient(new ServerAddress("localhost", mongoPort)));
		for (Student student : studentRepository.findAll()) {
			studentRepository.delete(student.getId());
		}
		schoolController = new SchoolController(studentView, studentRepository);
	}

	@Test
	void testAllStudents() {
		Student student = new Student("1", "test1");
		studentRepository.save(student);
		schoolController.allStudents();
		verify(studentView).showAllStudents(asList(student));
	}

	@Test
	void testNewStudent() {
		Student student = new Student("1", "test1");
		schoolController.newStudent(student);
		verify(studentView).studentAdded(student);
	}

	@Test
	void testDeleteStudent() {
		Student student = new Student("1", "test1");
		studentRepository.save(student);
		schoolController.deleteStudent(student);
		verify(studentView).studentRemoved(student);
	}

	@AfterEach
	void close() throws Exception {
		closeable.close();
	}
}
