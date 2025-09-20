package guice.learningtests;

import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.FactoryModuleBuilder;

class GuiceAssistedInjectLearningTest {

	private static interface IMyView {}

	private static class MyView implements IMyView {}

	private static interface IMyRepository {}

	private static class MyRepository implements IMyRepository {}

	private static interface IMyController {}

	private static class MyController implements IMyController {

		IMyView view;
		IMyRepository repository;

		@Inject
		public MyController(@Assisted IMyView view, IMyRepository repository) {
			this.view = view;
			this.repository = repository;
		}
	}

	private static interface MyControllerFactory {

		IMyController create(IMyView view);
	}

	@Test
	@DisplayName("assisted inject")
	void test1() {
		Module module = new AbstractModule() {

			@Override
			protected void configure() {
				bind(IMyRepository.class).to(MyRepository.class);
				install(new FactoryModuleBuilder()
					.implement(IMyController.class, MyController.class)
					.build(MyControllerFactory.class));
			}
		};
		Injector injector = Guice.createInjector(module);
		MyControllerFactory controllerFactory = injector
			.getInstance(MyControllerFactory.class);
		MyController controller = (MyController) controllerFactory.create(new MyView());
		assertNotNull(controller.view);
		assertNotNull(controller.repository);
	}
}
