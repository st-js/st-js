package org.stjs;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Set;

import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.ResourceExtractor;
import org.junit.Test;

/**
 * This integration test checks if a jar is correctly packaged
 * 
 * @author acraciun
 */
public class STJSPackageJarTest extends AbstractPackagingTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testPackageIt() throws Exception {

		File testDir = ResourceExtractor.simpleExtractResources(getClass(), "/package-js-jar");

		Verifier verifier = new Verifier(testDir.getAbsolutePath());
		verifier.deleteArtifact("org.st-js", "package-js-jar", "1.0.0-SNAPSHOT", "jar");

		// coming from the configuration of surefire plugin outside
		verifier.getCliOptions().add("-Dstjs.version=" + System.getProperty("stjs.version"));

		verifier.executeGoal("install");

		verifier.verifyErrorFreeLog();

		String artifactFile = verifier.getArtifactPath("org.st-js", "package-js-jar", "1.0.0-SNAPSHOT", "jar");
		Set<String> entryNames = JarUtils.getJarEntries(artifactFile);

		assertContainsEntry(entryNames, "stjs.js");
		assertContainsEntry(entryNames, "org/stjs/example/lib/stjs/STJSLibExample.js");
		assertContainsEntry(entryNames, "org/stjs/example/lib/stjs/STJSLibExample.stjs");
		assertContainsEntry(entryNames, "stjs/example/stjs-lib-example.js");
		assertContainsEntry(entryNames, "DefaultPackageExample.js");


		assertEntryContainsText( //
				artifactFile, //
				"org/stjs/example/lib/stjs/STJSLibExample.stjs", //
				"js=classpath\\:/org/stjs/example/lib/stjs/STJSLibExample.js" //
		);

		verifier.resetStreams();

	}
}
