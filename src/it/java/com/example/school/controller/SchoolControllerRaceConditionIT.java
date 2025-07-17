package com.example.school.controller;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

class SchoolControllerRaceConditionIT {

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
	void testNewStudentConcurrency() {
		Student student = new Student("1", "test");

		List<Thread> threads = IntStream.range(0, 10).mapToObj(i -> new Thread(() -> {
			new SchoolController(studentView, studentRepository).newStudent(student);
		})).peek(t -> t.start()).collect(Collectors.toList());
		await().atMost(10, SECONDS).until(()->threads.stream().noneMatch(Thread::isAlive));
		
	}
}




















