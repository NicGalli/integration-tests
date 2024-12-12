package com.example.school;

import static com.example.school.repository.mongo.StudentMongoRepository.SCHOOL_DB_NAME;
import static com.example.school.repository.mongo.StudentMongoRepository.STUDENT_COLLECTION_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import java.util.concurrent.TimeUnit;

import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.bson.Document;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testcontainers.containers.MongoDBContainer;

import com.example.school.controller.SchoolController;
import com.example.school.model.Student;
import com.example.school.repository.StudentRepository;
import com.example.school.repository.mongo.StudentMongoRepository;
import com.example.school.view.swing.StudentSwingView;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

@RunWith(GUITestRunner.class)
public class ModelViewControllerIT extends AssertJSwingJUnitTestCase {

	@ClassRule
	public static final MongoDBContainer mongo = new MongoDBContainer("mongo:4.4.3");
	private MongoClient mongoClient;

	private FrameFixture window;
	private StudentSwingView studentSwingView;
	private StudentRepository studentRepository;
	private SchoolController schoolController;

	private MongoCollection<Document> studentCollection;

	@Override
	protected void onSetUp() throws Exception {
		mongoClient = new MongoClient(new ServerAddress(mongo.getHost(), mongo.getMappedPort(27017)));
		studentRepository = new StudentMongoRepository(mongoClient);
		studentCollection = mongoClient.getDatabase(SCHOOL_DB_NAME).getCollection(STUDENT_COLLECTION_NAME);

		MongoDatabase database = mongoClient.getDatabase(SCHOOL_DB_NAME);
		database.drop();
		GuiActionRunner.execute(() -> {
			studentSwingView = new StudentSwingView();
			schoolController = new SchoolController(studentSwingView, studentRepository);
			studentSwingView.setSchoolController(schoolController);
			return studentSwingView;
		});
		window = new FrameFixture(robot(), studentSwingView);
		window.show();
	}

	@Override
	protected void onTearDown() throws Exception {
		mongoClient.close();
	}

	@Test
	public void testAddUpdatesDatabase() {
		window.textBox("idTextBox").enterText("1");
		window.textBox("nameTextBox").enterText("test1");
		window.button(JButtonMatcher.withText("Add")).click();

		await().atMost(5, TimeUnit.SECONDS)
				.untilAsserted(() -> assertThat(studentRepository.findById("1")).isEqualTo(new Student("1", "test1")));

	}

	@Test
	public void testDeleteUpdatesDatabase() {
		studentCollection.insertOne(new Document().append("id", "1").append("name", "test1"));
		studentCollection.insertOne(new Document().append("id", "2").append("name", "test2"));
		GuiActionRunner.execute(() -> schoolController.allStudents());

		Student student = new Student("1", "test1");
		window.list().selectItem(student.toString());
		window.button(JButtonMatcher.withText("Delete Selected")).click();
		Document d = studentCollection.find(Filters.eq("id", "1")).first();
		assertThat(d).isNull();

	}
}
