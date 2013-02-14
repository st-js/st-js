package org.stjs;

import java.io.File;

import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.ResourceExtractor;
import org.junit.Test;

public class DepencyCycleTest {
	@Test
	public void testPackageIt() throws Exception {

		File testDir = ResourceExtractor.simpleExtractResources(getClass(), "/project-with-cycles");

		Verifier verifier = new Verifier(testDir.getAbsolutePath());
		verifier.deleteArtifact("org.st-js", "project-with-cycles", "1.0.0-SNAPSHOT", "jar");

		try {
			verifier.executeGoal("install");
		} catch (Exception e) {
			// it should break here
		}

		verifier.verifyTextInLog("Cycles are detected");

	}
}
