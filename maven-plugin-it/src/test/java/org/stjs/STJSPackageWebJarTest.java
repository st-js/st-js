package org.stjs;

import java.io.File;
import java.util.Set;

import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.ResourceExtractor;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This integration test checks if a web jar is correctly packaged, and if tests on this webjar can run.
 *
 * @author acraciun
 */
public class STJSPackageWebJarTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testPackageIt() throws Exception {

		File testDir = ResourceExtractor.simpleExtractResources(getClass(), "/package-js-webjar");

		Verifier verifier = new Verifier(testDir.getAbsolutePath());
		verifier.deleteArtifact("org.st-js", "package-js-webjar", "1.0.0-SNAPSHOT", "jar");

		// coming from the configuration of surefire plugin outside
		verifier.getCliOptions().add("-Dstjs.version=" + System.getProperty("stjs.version"));

		verifier.executeGoal("install");

		verifier.verifyErrorFreeLog();

		String artifactFile = verifier.getArtifactPath("org.st-js", "package-js-webjar", "1.0.0-SNAPSHOT", "jar");
		Set<String> entryNames = JarUtils.getJarEntries(artifactFile);

		// all resources that must be loaded by a browser must be packaged in the folder
		// specified in <generatedSourcesDirectory>
		assertContainsEntry(entryNames, "META-INF/resources/webjars/package-js-webjar/1.0.0-SNAPSHOT/org/stjs/example/lib/stjs/STJSLibExample.js");
		assertContainsEntry(entryNames, "META-INF/resources/webjars/package-js-webjar/1.0.0-SNAPSHOT/org/stjs/example/lib/stjs/STJSLibExample.map");
		assertContainsEntry(entryNames, "META-INF/resources/webjars/package-js-webjar/1.0.0-SNAPSHOT/org/stjs/example/lib/stjs/STJSLibExample.java");
		assertContainsEntry(entryNames, "META-INF/resources/webjars/package-js-webjar/1.0.0-SNAPSHOT/stjs/example/stjs-lib-example.js");
		assertContainsEntry(entryNames, "META-INF/resources/webjars/package-js-webjar/1.0.0-SNAPSHOT/DefaultPackageExample.js");
		assertContainsEntry(entryNames, "META-INF/resources/webjars/package-js-webjar/1.0.0-SNAPSHOT/DefaultPackageExample.map");
		assertContainsEntry(entryNames, "META-INF/resources/webjars/package-js-webjar/1.0.0-SNAPSHOT/DefaultPackageExample.java");
		assertNotContainsEntry(entryNames, "org/stjs/example/lib/stjs/STJSLibExample.js");
		assertNotContainsEntry(entryNames, "org/stjs/example/lib/stjs/STJSLibExample.map");
		assertNotContainsEntry(entryNames, "org/stjs/example/lib/stjs/STJSLibExample.java");
		assertNotContainsEntry(entryNames, "stjs/example/stjs-lib-example.js");
		assertNotContainsEntry(entryNames, "DefaultPackageExample.js");
		assertNotContainsEntry(entryNames, "DefaultPackageExample.map");
		assertNotContainsEntry(entryNames, "DefaultPackageExample.java");

		// Class files must be packaged at the root of the web jar
		assertContainsEntry(entryNames, "org/stjs/example/lib/stjs/STJSLibExample.class");
		assertContainsEntry(entryNames, "DefaultPackageExample.class");
		assertNotContainsEntry(entryNames, "META-INF/resources/webjars/package-js-webjar/1.0.0-SNAPSHOT/org/stjs/example/lib/stjs/STJSLibExample.class");
		assertNotContainsEntry(entryNames, "META-INF/resources/webjars/package-js-webjar/1.0.0-SNAPSHOT/DefaultPackageExample.class");

		// the .stjs properties files must be in both places (classpath and webjar path), so that we can easily do bidirectional mapping between
		// .js and .class file locations
		assertContainsEntry(entryNames, "org/stjs/example/lib/stjs/STJSLibExample.stjs");
		assertContainsEntry(entryNames, "DefaultPackageExample.stjs");
		assertContainsEntry(entryNames, "META-INF/resources/webjars/package-js-webjar/1.0.0-SNAPSHOT/org/stjs/example/lib/stjs/STJSLibExample.stjs");
		assertContainsEntry(entryNames, "META-INF/resources/webjars/package-js-webjar/1.0.0-SNAPSHOT/DefaultPackageExample.stjs");

		// stjs.js should not be included in a webjar (it is declared as a dependency instead)
		assertNotContainsEntry(entryNames, "stjs.js");
		assertNotContainsEntry(entryNames, "META-INF/resources/webjars/package-js-webjar/1.0.0-SNAPSHOT/stjs.js");

		verifier.resetStreams();
	}

	private static void assertContainsEntry(Set<String> entries, String entry) {
		assertTrue("The set " + entries + " should contain entry: " + entry, entries.contains(entry));
	}

	private static void assertNotContainsEntry(Set<String> entries, String entry){
		assertFalse("The set " + entries + " should not contain entry: " + entry, entries.contains(entry));
	}
}
