package guice.learningtests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.FactoryModuleBuilder;

class GuicelearningTestFastProvide {

	private static class MyRepository implements IMyRepository {}

	private interface MyControllerFactory {

		IMyController create(IMyView view);
	}

	private interface IMyRepository {}

	private interface IMyView {}

	private static interface IMyController {}

	private static class MyView implements IMyView {

		IMyController controller;

		void setController(IMyController controller) {
			this.controller = controller;
		}
	}

	private static class MyController implements IMyController {

		IMyView view;
		IMyRepository repository;

		@Inject
		MyController(@Assisted IMyView view, IMyRepository repository) {
			this.view = view;
			this.repository = repository;
		}
	}

	private static class MyViewProvider implements Provider<MyView> {

		@Inject
		private MyControllerFactory controllerFactory;

		@Override
		public MyView get() {
			MyView view = new MyView();
			view.setController(controllerFactory.create(view));
			return view;
		}
	}

	@Test
	@DisplayName("cyclic dependecies")
	void test1() {
		Module module = new AbstractModule() {

			@Provides
			IMyView provider(MyControllerFactory controllerFactory) {
				MyView view = new MyView();
				view.setController(controllerFactory.create(view));
				return view;
			}
			@Override
			protected void configure() {
				bind(IMyRepository.class).to(MyRepository.class);
				
				install(new FactoryModuleBuilder()
					.implement(IMyController.class, MyController.class)
					.build(MyControllerFactory.class));
			}
		};
		Injector injector = Guice.createInjector(module);
		MyView view = injector.getInstance(MyView.class);
		assertSame(view, ((MyController) view.controller).view);
		assertNotNull(((MyController) view.controller).repository);
	}
}
