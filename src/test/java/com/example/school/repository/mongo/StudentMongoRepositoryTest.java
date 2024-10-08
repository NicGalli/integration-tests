package com.example.school.repository.mongo;

import static com.example.school.repository.mongo.StudentMongoRepository.SCHOOL_DB_NAME;
import static com.example.school.repository.mongo.StudentMongoRepository.STUDENT_COLLECTION_NAME;
import static org.assertj.core.api.Assertions.assertThat;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.school.model.Student;
import com.example.school.repository.StudentRepository;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;;

class StudentMongoRepositoryTest {

	private static MongoServer server;
	private static InetSocketAddress serverAddress;

	@BeforeAll
	static void setupServer() {
		server = new MongoServer(new MemoryBackend());
		serverAddress = server.bind();
	}

	@AfterAll
	static void shutdownServer() {
		server.shutdown();
	}

	private MongoClient client;
	private StudentRepository studentRepository;
	private MongoCollection<Document> studentCollection;

	@BeforeEach
	void setup() {
		client = new MongoClient(new ServerAddress(serverAddress));
		studentRepository = new StudentMongoRepository(client);
		MongoDatabase database = client.getDatabase(SCHOOL_DB_NAME);
		database.drop();
		studentCollection = database.getCollection(STUDENT_COLLECTION_NAME);
	}

	@AfterEach
	void teardown() {
		client.close();
	}

	@Test
	void testFindAllWhenDatabaseIsEmpty() {
		assertThat(studentRepository.findAll()).isEmpty();
	}

	@Test
	void testFindAllWhenDatabaseIsNotEmpty() {
		addTestStudentToDatabase("1", "test1");
		addTestStudentToDatabase("2", "test2");
		assertThat(studentRepository.findAll()).containsExactly(new Student("1", "test1"), new Student("2", "test2"));
	}

	@Test
	void testFindByIdNotFound() {
		assertThat(studentRepository.findById("1")).isNull();
	}

	@Test
	void testFindByIdFound() {
		addTestStudentToDatabase("1", "test1");
		assertThat(studentRepository.findById("1")).isEqualTo(new Student("1", "test1"));
	}

	@Test
	void testSave() {
		Student student = new Student("1", "test1");
		studentRepository.save(student);
		assertThat(readAllStudentFromDatabase()).containsExactly(student);
	}

	@Test
	void testDelete() {
		addTestStudentToDatabase("1", "name");
		studentRepository.delete("1");
		assertThat(readAllStudentFromDatabase()).isEmpty();
	}

	private void addTestStudentToDatabase(String id, String name) {
		studentCollection.insertOne(new Document().append("id", id).append("name", name));
	}

	private List<Student> readAllStudentFromDatabase() {
		return StreamSupport.stream(studentCollection.find().spliterator(), false)
				.map(d -> new Student("" + d.get("id"), "" + d.get("name"))).collect(Collectors.toList());
	}
}
