package org.stjs.generator.sourcemap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.stjs.generator.utils.GeneratorTestHelper.buildClassLoader;
import static org.stjs.generator.utils.GeneratorTestHelper.generateWithSourcemap;

import org.junit.Test;

public class StacktraceTest {

	@Test
	public void testStacktraceBuild() {
		// given
		generateWithSourcemap(Sourcemap1.class);
		generateWithSourcemap(Sourcemap2.class);

		// when
		String jsError = "at prototype.method2 (http://localhost:8055/org/stjs/generator/sourcemap/Sourcemap2.js:4:11)\n" //
				+ "at prototype.method1 (http://localhost:8055/org/stjs/generator/sourcemap/Sourcemap1.js:5:11)\n" //
				+ "at window.onload (http://localhost:8055/url?browserId=2430328:17:16)";
		JavascriptToJava js2java = new JavascriptToJava(buildClassLoader());
		StackTraceElement[] elements = js2java.buildStacktrace(jsError, "\n");

		// then
		assertNotNull(elements);
		assertEquals("elements size", 3, elements.length);
		assertElement(Sourcemap2.class, "/org/stjs/generator/sourcemap/Sourcemap2.java", "method2", 6, elements[0]);
		assertElement(Sourcemap1.class, "/org/stjs/generator/sourcemap/Sourcemap1.java", "method1", 6, elements[1]);
		assertElement(null, "/url?browserId=2430328", "onload", 17, elements[2]);
	}

	private void assertElement(Class<?> clazz, String file, String method, int line, StackTraceElement element) {
		assertNotNull(element);
		if (clazz != null) {
			assertEquals(clazz.getName(), element.getClassName());
		}
		assertEquals(method, element.getMethodName());
		assertEquals(line, element.getLineNumber());
		assertEquals(file, element.getFileName());
	}
}
