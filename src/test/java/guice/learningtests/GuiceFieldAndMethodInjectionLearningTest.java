package guice.learningtests;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;

class GuiceFieldAndMethodInjectionLearningTest {

	private static interface IMyService {

	}

	private static class MyService implements IMyService {
	}

	private static class MyClientWithInjectedField {
		@Inject
		IMyService service;
	}

	private static class MyClientWithInjectedMethod {
		IMyService service;

		@Inject
		public void init(IMyService service) {
			this.service = service;
		}
	}

	@Test
	@DisplayName("field and method injection")
	void test1() {
		Module module = new AbstractModule() {
			@Override
			protected void configure() {
				bind(IMyService.class).to(MyService.class);
			}
		};
		Injector injector = Guice.createInjector(module);
		MyClientWithInjectedField client1 = injector.getInstance(MyClientWithInjectedField.class);
		MyClientWithInjectedMethod client2 = injector.getInstance(MyClientWithInjectedMethod.class);
		assertNotNull(client1);
		assertNotNull(client2);
	}
	@Test
	@DisplayName("field and method injection")
	void test2() {
		Module module = new AbstractModule() {
			@Override
			protected void configure() {
				bind(IMyService.class).to(MyService.class);
			}
		};
		Injector injector = Guice.createInjector(module);
		MyClientWithInjectedField client1 = new MyClientWithInjectedField();
		MyClientWithInjectedMethod client2 = new MyClientWithInjectedMethod();
		injector.injectMembers(client1);
		injector.injectMembers(client2);
		assertNotNull(client1.service);
		assertNotNull(client2.service);
	}
}
