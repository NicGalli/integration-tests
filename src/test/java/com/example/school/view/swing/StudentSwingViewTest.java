package com.example.school.view.swing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.core.matcher.JButtonMatcher.withText;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import java.util.Arrays;

import javax.swing.DefaultListModel;

import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.school.controller.SchoolController;
import com.example.school.model.Student;

@RunWith(GUITestRunner.class)
public class StudentSwingViewTest extends AssertJSwingJUnitTestCase {

	FrameFixture window;
	StudentSwingView studentSwingView;
	@Mock
	private SchoolController schoolController;
	private AutoCloseable closeable;
	
	private static final int TIME_OUT = 5000;

	@Test
	public void test() {

	}

	@Test
	public void testControlsInitialStates() {
		window.label(JLabelMatcher.withText("id"));
		window.textBox("idTextBox").requireEnabled();
		window.label(JLabelMatcher.withText("name"));
		window.textBox("nameTextBox").requireEnabled();
		window.button(withText("Add")).requireDisabled();
		window.list("studentList");
		window.button(withText("Delete Selected")).requireDisabled();
		window.label("errorMessageLabel").requireText(" ");
	}

	@Test
	public void testWhenIdAndNameAreNotEmptyAddButtonShouldBeEnabled() {
		window.textBox("idTextBox").enterText("1");
		window.textBox("nameTextBox").enterText("john");
		window.button(withText("Add")).requireEnabled();
	}

	@Test
	public void testWhenIdOrNameAreBlankAddButtonShouldBeDisabled() {
		JTextComponentFixture idTextBox = window.textBox("idTextBox");
		JTextComponentFixture nameTextBox = window.textBox("nameTextBox");

		idTextBox.enterText(" ");
		nameTextBox.enterText("john");
		window.button(withText("Add")).requireDisabled();

		idTextBox.deleteText();
		nameTextBox.deleteText();

		idTextBox.enterText("1");
		nameTextBox.enterText(" ");
		window.button(withText("Add")).requireDisabled();
	}

	@Test
	public void testDeleteButtonShouldBeEnabledOnlyWhenAStudentIsSelected() {
		DefaultListModel<Student> list = studentSwingView.getListStudentsModel();
		GuiActionRunner.execute(() -> {
			list.addElement(new Student("1", "test"));
		});

		window.list("studentList").selectItem(0);
		window.button(withText("Delete Selected")).requireEnabled();

		window.list("studentList").clearSelection();
		window.button(withText("Delete Selected")).requireDisabled();
	}

	@Test
	public void testShowAllStudentsShouldAddStudentDescriptionToTheList() {
		Student test1 = new Student("1", "test1");
		Student test2 = new Student("2", "test2");

		GuiActionRunner.execute(() -> {
			studentSwingView.showAllStudents(Arrays.asList(test1, test2));
		});

		String[] list = window.list().contents();
		assertThat(list).containsExactly(test1.toString(), test2.toString());
	}

	@Test
	public void testShowErrorShouldBeDisplayedOnErrorLabel() {
		Student student = new Student("1", "test1");
			studentSwingView.showError("error message", student);
		
		window.label("errorMessageLabel").requireText("error message" + student.toString());
	}

	@Test
	public void testStudentAddedShouldAddStudentToListAndResetErrorMessage() {
		Student student = new Student("1", "test1");
		GuiActionRunner.execute(() -> {
			studentSwingView.getLabel().setText("error message");
		});
		//GuiActionRunner.execute(() -> {
			studentSwingView.studentAdded(new Student("1", "test1"));
		//});
		String[] list = window.list().contents();
		assertThat(list).containsExactly(student.toString());
		window.label("errorMessageLabel").requireText(" ");

	}

	@Test
	public void testStudentRemovedShouldRemoveStudentFromListAndResetErrorLabel() {
		Student student1 = new Student("1", "test1");
		Student student2 = new Student("2", "test2");
		GuiActionRunner.execute(() -> {
			studentSwingView.getLabel().setText("error message");
			studentSwingView.getListStudentsModel().addElement(student1);
			studentSwingView.getListStudentsModel().addElement(student2);

		});
		GuiActionRunner.execute(() -> {
			studentSwingView.studentRemoved(new Student("1", "test1"));
		});
		String[] list = window.list().contents();
		assertThat(list).containsExactly(student2.toString());
		window.label("errorMessageLabel").requireText(" ");
	}

	@Override
	protected void onSetUp() throws Exception {

		closeable = MockitoAnnotations.openMocks(this);
		GuiActionRunner.execute(() -> {
			studentSwingView = new StudentSwingView();

			studentSwingView.setSchoolController(schoolController);
			return studentSwingView;
		});

		window = new FrameFixture(robot(), studentSwingView);
		window.show();
	}

	@Override
	protected void onTearDown() throws Exception {
		closeable.close();
	}

	@Test
	public void testAddButtonShouldDelegateToSchoolControllerNewStudent() {
		window.textBox("idTextBox").enterText("1");
		window.textBox("nameTextBox").enterText("test");
		window.button(withText("Add")).click();
		verify(schoolController, timeout(TIME_OUT)).newStudent(new Student("1", "test"));
	}
	@Test
	public void testDeleteButtonShouldDelegateToSchoolControllerRemoveStudent() {
		Student student1 = new Student("1", "test1");
		Student student2 = new Student("2", "test2");
		GuiActionRunner.execute(() -> {
			studentSwingView.getListStudentsModel().addElement(student1);
			studentSwingView.getListStudentsModel().addElement(student2);

		});
		
		
		window.list().selectItem(student1.toString());
		window.button(withText("Delete Selected")).click();
		verify(schoolController).deleteStudent(new Student("1", "test1"));
	}

}
