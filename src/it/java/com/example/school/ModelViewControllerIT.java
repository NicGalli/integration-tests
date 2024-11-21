package com.example.school;

import static com.example.school.repository.mongo.StudentMongoRepository.SCHOOL_DB_NAME;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.testcontainers.containers.MongoDBContainer;

import com.example.school.controller.SchoolController;
import com.example.school.repository.StudentRepository;
import com.example.school.repository.mongo.StudentMongoRepository;
import com.example.school.view.swing.StudentSwingView;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

@RunWith(GUITestRunner.class)
public class ModelViewControllerIT extends AssertJSwingJUnitTestCase {

	@ClassRule
	public static final MongoDBContainer mongo = new MongoDBContainer("4.4.3");
	private MongoClient mongoClient;

	private FrameFixture window;
	private StudentSwingView studentSwingView;
	private StudentRepository studentRepository;
	private SchoolController schoolController;

	@Override
	protected void onSetUp() throws Exception {
		mongoClient = new MongoClient(new ServerAddress(mongo.getHost(), mongo.getMappedPort(27017)));
		studentRepository = new StudentMongoRepository(mongoClient);
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

}
