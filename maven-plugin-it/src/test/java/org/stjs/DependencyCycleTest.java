package org.stjs;

import java.io.File;
import java.util.Properties;

import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.ResourceExtractor;
import org.junit.Test;

public class DependencyCycleTest {
	@Test
	public void testPackageIt() throws Exception {

		File testDir = ResourceExtractor.simpleExtractResources(getClass(), "/project-with-cycles");

		Verifier verifier = new Verifier(testDir.getAbsolutePath());
		verifier.deleteArtifact("org.st-js", "project-with-cycles", "1.0.0-SNAPSHOT", "jar");

		try {
			Properties props = new Properties(System.getProperties());
			props.put("stjs.version", System.getProperty("stjs.version"));//coming from the configuration of surefire plugin outside
			verifier.executeGoal("install", props);
		}
		catch (Exception e) {
			// it should break here
		}

		verifier.verifyTextInLog("Cycles are detected");

	}
}
