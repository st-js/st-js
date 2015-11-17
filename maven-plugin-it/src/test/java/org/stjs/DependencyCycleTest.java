package org.stjs;

import java.io.File;
import java.util.Arrays;

import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.ResourceExtractor;
import org.junit.Test;

public class DependencyCycleTest {
	@SuppressWarnings("unchecked")
	@Test
	public void testPackageIt() throws Exception {
		File testDir = ResourceExtractor.simpleExtractResources(getClass(), "/project-with-cycles");

		Verifier verifier = new Verifier(testDir.getAbsolutePath());
		verifier.deleteArtifact("org.st-js", "project-with-cycles", "1.0.0-SNAPSHOT", "jar");

		try {
			verifier.getCliOptions().add("-Dstjs.version=" + System.getProperty("stjs.version"));
			verifier.executeGoals(Arrays.asList("clean", "install"));
		}
		catch (Exception e) {
			// it should break here
		}

		verifier.verifyTextInLog("cycles are detected");

	}
}
