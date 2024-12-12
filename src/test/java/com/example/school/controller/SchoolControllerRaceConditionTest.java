package com.example.school.controller;

import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.school.model.Student;
import com.example.school.repository.StudentRepository;
import com.example.school.view.StudentView;

@ExtendWith(MockitoExtension.class)
class SchoolControllerRaceConditionTest {

	@Mock
	private StudentRepository studentRepository;

	@Mock
	private StudentView studentView;

	@InjectMocks
	private SchoolController schoolController;

	@Test
	@DisplayName("test concurrency for newStudent")
	void test1() {
		List<Student> students = new ArrayList<Student>();
		Student student = new Student("1", "test1");

		when(studentRepository.findById(anyString()))
				.thenAnswer(invocation -> students.stream().findFirst().orElse(null));
		doAnswer(invocation -> {
			students.add(student);
			return null;
		}).when(studentRepository).save(any(Student.class));
		List<Thread> threads = IntStream.range(0, 20)
				.mapToObj(i -> new Thread(() -> schoolController.newStudent(student)))
				.peek(Thread::start)
				.collect(toList());

		await().atMost(10, SECONDS).until(() -> threads.stream().noneMatch(Thread::isAlive));
		assertThat(students).containsExactly(student);
	}

	@Test
	@DisplayName("test concurrency for deleteStudent")
	void test2() {
		List<Student> students = new ArrayList<>();
		students.add(new Student("1", "test1"));
		students.add(new Student("2", "test1"));
		students.add(new Student("3", "test1"));
		students.add(new Student("4", "test1"));
		students.add(new Student("5", "test1"));
		students.add(new Student("6", "test1"));
		students.add(new Student("7", "test1"));
		students.add(new Student("8", "test1"));
		students.add(new Student("9", "test1"));
		students.add(new Student("10", "test1"));
		students.add(new Student("11", "test1"));
		students.add(new Student("12", "test1"));

		when(studentRepository.findById(anyString())).thenAnswer(invocation -> {
			return students.stream()
					.filter(i -> i.getId().equals(invocation.getArgument(0)))
					.findFirst()
					.orElse(null);
		});

		doAnswer(invocation -> {
			students.removeIf(e -> e.getId().equals(invocation.getArgument(0)));
			return null;
		}).when(studentRepository).delete(anyString());

		List<Thread> threads = IntStream.range(0, 36)
				.mapToObj(i -> new Thread(
						() -> schoolController.deleteStudent(new Student(String.valueOf(i/3 + 1), "test1"))))
				.peek(Thread::start)
				.collect(toList());

		await().atMost(10, SECONDS).until(() -> threads.stream().noneMatch(Thread::isAlive));
		assertThat(students).isEmpty();
		verify(studentRepository, times(12)).delete(anyString());
	}

}
