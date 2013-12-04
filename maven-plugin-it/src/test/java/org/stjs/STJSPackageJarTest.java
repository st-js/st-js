package org.stjs;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Properties;
import java.util.Set;

import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.ResourceExtractor;
import org.junit.Test;

/**
 * This integration test checks if a jar is correctly packaged
 * @author acraciun
 */
public class STJSPackageJarTest {

	@Test
	public void testPackageIt() throws Exception {

		File testDir = ResourceExtractor.simpleExtractResources(getClass(), "/package-js-jar");

		Verifier verifier = new Verifier(testDir.getAbsolutePath());
		verifier.deleteArtifact("org.st-js", "package-js-jar", "1.0.0-SNAPSHOT", "jar");

		Properties props = new Properties(System.getProperties());
		props.put("stjs.version", System.getProperty("stjs.version"));//coming from the configuration of surefire plugin outside
		verifier.executeGoal("install", props);

		verifier.verifyErrorFreeLog();

		String artifactFile = verifier.getArtifactPath("org.st-js", "package-js-jar", "1.0.0-SNAPSHOT", "jar");
		Set<String> entryNames = JarUtils.getJarEntries(artifactFile);

		assertContainsEntry(entryNames, "stjs.js");
		assertContainsEntry(entryNames, "org/stjs/example/lib/stjs/STJSLibExample.js");
		assertContainsEntry(entryNames, "org/stjs/example/lib/stjs/STJSLibExample.stjs");
		assertContainsEntry(entryNames, "stjs/example/stjs-lib-example.js");
		assertContainsEntry(entryNames, "DefaultPackageExample.js");
		verifier.resetStreams();

	}

	private static void assertContainsEntry(Set<String> entries, String entry) {
		assertTrue("The set " + entries + " should contain entry: " + entry, entries.contains(entry));
	}
}
