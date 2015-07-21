package org.stjs;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Set;

public class AbstractPackagingTest {
	static void assertContainsEntry(Set<String> entries, String entry) {
		assertTrue("The set " + entries + " should contain entry: " + entry, entries.contains(entry));
	}

	static void assertEntryContainsText(String jarFile, String entry, String text) throws IOException {
		String body = JarUtils.getJarEntryBody(jarFile, entry);
		assertTrue( //
				"The entry " + entry + " should contain the text: " + text + "\nactual content:\n" + body, //
				body.contains(text) //
		);
	}

	static void assertNotContainsEntry(Set<String> entries, String entry) {
		assertFalse("The set " + entries + " should not contain entry: " + entry, entries.contains(entry));
	}
}
