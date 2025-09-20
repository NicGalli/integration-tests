package guice.learningtests;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.Module;

class GuiceProviderLearningTest {

	private static interface IMyService {

	}

	private static class MyService implements IMyService {

	}

	private static class MyClientWithInjectedProvider {
		@Inject
		Provider<IMyService> serviceProvider;

		IMyService getService() {
			return serviceProvider.get();
		}
	}

	private static class MyFileWrapper {
		@Inject
		File file;
	}

	@Test
	@DisplayName("inject provider example")
	void test1() {
		Module module = new AbstractModule() {
			@Override
			protected void configure() {
				bind(IMyService.class).to(MyService.class);
			}
		};
		Injector injector = Guice.createInjector(module);
		MyClientWithInjectedProvider client = injector.getInstance(MyClientWithInjectedProvider.class);
		assertNotNull(client.getService());
	}

	@Test
	@DisplayName("provider binding")
	void test2() {
		Module module = new AbstractModule() {
			@Override
			protected void configure() {
				bind(File.class).toProvider(() -> new File("src/test/resources/afile.txt"));
			}
		};
		Injector injector = Guice.createInjector(module);
		MyFileWrapper fileWrapper = injector.getInstance(MyFileWrapper.class);
		assertTrue(fileWrapper.file.exists());
	}
}
