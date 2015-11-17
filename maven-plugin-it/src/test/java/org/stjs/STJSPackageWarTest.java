package org.stjs;

import java.io.File;
import java.util.Arrays;
import java.util.Set;

import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.ResourceExtractor;
import org.junit.Test;

/**
 * This integration test checks if a war is correctly packaged
 *
 * @author acraciun
 */
public class STJSPackageWarTest extends AbstractPackagingTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testPackageIt() throws Exception {

		File testDir = ResourceExtractor.simpleExtractResources(getClass(), "/package-js-war");

		Verifier verifier = new Verifier(testDir.getAbsolutePath());

		verifier.deleteArtifact("org.st-js", "package-js-war", "1.0.0-SNAPSHOT", "war");

		// coming from the configuration of surefire plugin outside
		verifier.getCliOptions().add("-Dstjs.version=" + System.getProperty("stjs.version"));

		verifier.executeGoals(Arrays.asList("clean", "install"));

		verifier.verifyErrorFreeLog();

		String artifactFile = verifier.getArtifactPath("org.st-js", "package-js-war", "1.0.0-SNAPSHOT", "war");
		Set<String> entryNames = JarUtils.getJarEntries(artifactFile);

		assertContainsEntry(entryNames, "generated-js/stjs.js");
		assertContainsEntry(entryNames, "generated-js/org/stjs/example/lib/stjs/STJSLibExample.js");
		assertContainsEntry(entryNames, "WEB-INF/classes/org/stjs/example/lib/stjs/STJSLibExample.stjs");
		assertContainsEntry(entryNames, "js/stjs/example/stjs-lib-example.js");
		// check that jquery is copied as well
		assertContainsEntry(entryNames, "generated-js/jquery/jquery.js");

		// Check that the paths to the .js files are correctly generated in the .stjs files
		assertEntryContainsText( //
				artifactFile, //
				"WEB-INF/classes/org/stjs/example/lib/stjs/STJSLibExample.stjs", //
				"js=/generated-js/org/stjs/example/lib/stjs/STJSLibExample.js" //
		);
		verifier.resetStreams();

	}
}
