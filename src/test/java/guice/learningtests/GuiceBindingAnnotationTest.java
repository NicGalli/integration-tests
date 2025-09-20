package guice.learningtests;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;

class GuiceBindingAnnotationTest {

	@BindingAnnotation
	@Target({FIELD, PARAMETER, METHOD})
	@Retention(RUNTIME)
	private static @interface FilePath {}
	
	@BindingAnnotation
	@Target({FIELD, PARAMETER, METHOD})
	@Retention(RUNTIME)
	private static @interface FileName {}
	
	private static class MyFileWrapper2{
		File file;
		
		@Inject
		public MyFileWrapper2(@FilePath String path, @FileName String name) {
			file = new File(path, name);
		}
		
	}
	
	@Test
	@DisplayName("custom binding annotations")
	void test1() {
		Module module = new AbstractModule() {
			@Override
			protected void configure() {
				bind(String.class).annotatedWith(FilePath.class).toInstance("src/test/resources");
				bind(String.class).annotatedWith(FileName.class).toInstance("afile.txt");				
			}
		};
		Injector injector = Guice.createInjector(module);
		MyFileWrapper2 myFileWrapper = injector.getInstance(MyFileWrapper2.class);
		assertTrue(myFileWrapper.file.exists());
	}

}
