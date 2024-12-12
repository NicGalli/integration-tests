package com.example.school.view.swing;

import static com.example.school.repository.mongo.StudentMongoRepository.SCHOOL_DB_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.core.matcher.JButtonMatcher.withText;
import static org.assertj.swing.timing.Pause.pause;
import static org.assertj.swing.timing.Timeout.timeout;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.assertj.swing.timing.Condition;
import org.awaitility.Awaitility;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.school.controller.SchoolController;
import com.example.school.model.Student;
import com.example.school.repository.StudentRepository;
import com.example.school.repository.mongo.StudentMongoRepository;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;

@RunWith(GUITestRunner.class)
public class StudentSwingViewIT extends AssertJSwingJUnitTestCase {

	private static final long TIME_OUT = 5000;
	private FrameFixture window;

	private StudentSwingView studentSwingView;
	private SchoolController schoolController;

	private static MongoServer server;
	private static InetSocketAddress serverAddress;

	private MongoClient client;
	private StudentRepository studentRepository;

	@Test
	public void testAllStudents() {
		studentRepository.save(new Student("1", "test1"));
		studentRepository.save(new Student("2", "test2"));
		GuiActionRunner.execute(() -> schoolController.allStudents());
		assertThat(window.list().contents()).containsExactly(new Student("1", "test1").toString(),
				new Student("2", "test2").toString());
	}

	@Test
	public void testAddButtonSuccess() {
		window.textBox("idTextBox").enterText("1");
		window.textBox("nameTextBox").enterText("test1");
		window.button(withText("Add")).click();
		Awaitility.await().atMost(5, TimeUnit.SECONDS)
				.untilAsserted(() -> assertThat(window.list().contents()).containsExactly(new Student("1", "test1").toString()));
	}

	@Test
	public void testAddButtonError() {
		studentRepository.save(new Student("1", "test1"));
		studentRepository.save(new Student("2", "test2"));

		window.textBox("idTextBox").enterText("1");
		window.textBox("nameTextBox").enterText("test3");
		window.button(withText("Add")).click();

		pause(new Condition("Error label to contain text") {
			@Override
			public boolean test() {
				return !window.label("errorMessageLabel").text().isBlank();
			}
		}, timeout(TIME_OUT));

		window.label("errorMessageLabel")
				.requireText("Already existing student with id " + "1" + new Student("1", "test1").toString());
	}

	@BeforeClass
	public static void setupServer() {
		server = new MongoServer(new MemoryBackend());
		serverAddress = server.bind();
	}

	@AfterClass
	public static void shutdownServer() {
		server.shutdown();
	}

	@After
	public void teardown() {
		client.close();
	}

	@Override
	protected void onSetUp() throws Exception {

		client = new MongoClient(new ServerAddress(serverAddress));
		studentRepository = new StudentMongoRepository(client);
		MongoDatabase database = client.getDatabase(SCHOOL_DB_NAME);
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
}
