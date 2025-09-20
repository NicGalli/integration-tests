package guice.learningtests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;

class GuiceLearningTest {

	private static interface IMyService {

	}

	private static class MyService implements IMyService {

	}

	private static class MyClient {
		IMyService service;

		@Inject
		public MyClient(IMyService service) {
			this.service = service;
		}
	}

	@Test
	@DisplayName("can instantiate concrete classes without configuration")
	void test1() {
		Module module = new AbstractModule() {
			@Override
			protected void configure() {
				bind(IMyService.class).to(MyService.class);
			}
		};
		Injector injector = Guice.createInjector(module);
		MyClient client = injector.getInstance(MyClient.class);
		assertNotNull(client);
		assertEquals(MyService.class, client.service.getClass());
	}
		@Test
		@DisplayName("bind to instance")
		void test2() {
			Module module = new AbstractModule() {
				@Override
				protected void configure() {
					bind(IMyService.class).toInstance(new MyService());;
				}
			};
			Injector injector = Guice.createInjector(module);
			MyClient client1 = injector.getInstance(MyClient.class);
			MyClient client2 = injector.getInstance(MyClient.class);
			assertNotNull(client1.service);
			assertSame(client1.service, client2.service);
	}
}
