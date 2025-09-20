package guice.learningtests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Singleton;
import com.google.inject.util.Modules;


class GuiceModulesOverrideLearningTest {

//	Modules.override(new DefalutModule()).with(new CustomModule())
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
	@DisplayName("modules override")
	void test1() {
		Module defaultModule = new AbstractModule() {
			@Override
			protected void configure() {
				bind(IMyService.class).to(MyService.class);
			}
		};
		Injector injector = Guice.createInjector(defaultModule);
		
		MyClient client1 = injector.getInstance(MyClient.class);
		MyClient client2 = injector.getInstance(MyClient.class);
		
		assertNotSame(client1.service, client2.service);
		
		Module customModule = new AbstractModule() {
			@Override
			protected void configure() {
				bind(MyService.class).in(Singleton.class);
			}
		};
		injector = Guice.createInjector(Modules.override(defaultModule).with(customModule));
		client1 = injector.getInstance(MyClient.class);
		client2 = injector.getInstance(MyClient.class);
		
		assertNotNull(client1.service);
		assertSame(client1.service, client2.service);

	}

}
