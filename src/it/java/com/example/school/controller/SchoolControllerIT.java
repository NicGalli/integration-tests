package com.example.school.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.school.model.Student;
import com.example.school.repository.StudentRepository;
import com.example.school.repository.mongo.StudentMongoRepository;
import com.example.school.view.StudentView;
import com.mongodb.MongoClient;

public class SchoolControllerIT {
@Mock
private StudentView studentView;
private StudentRepository studentRepository;
private SchoolController schoolController;

private AutoCloseable closeable;

@BeforeEach
void setup() {
	closeable = MockitoAnnotations.openMocks(this);
	studentRepository = new StudentMongoRepository(new MongoClient("localhost"));
	for(Student student:studentRepository.findAll()) {
		studentRepository.delete(student.getId());
	}
	schoolController = new SchoolController(studentView, studentRepository);
}

@AfterEach
void close() throws Exception {
	closeable.close();
}
}
