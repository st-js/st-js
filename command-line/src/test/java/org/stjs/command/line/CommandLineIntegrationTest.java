package org.stjs.command.line;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.io.CharStreams;

public class CommandLineIntegrationTest {
	private static final String WINDOWS_PATH = "target\\appassembler\\bin\\st-js.bat";
	private static final String UNIX_PATH = "target/appassembler/bin/st-js";
	private static final String OUTPUT_DIR = "target/test1-out";
	private static boolean windows;

	@BeforeClass
	public static void setupClass() {
		windows = System.getProperty("os.name").toLowerCase().contains("windows");
	}

	@Test
	public void testSimpleExecute() throws IOException, URISyntaxException {
		URL projectUrl = Thread.currentThread().getContextClassLoader().getResource("test-project");
		File projectRoot = new File(projectUrl.toURI());
		assertNotNull(projectUrl);

		ProcessBuilder pb;
		if(windows ){
			pb = new ProcessBuilder(
					WINDOWS_PATH,
					new File(projectRoot, "src").getAbsolutePath(),
					new File(projectRoot, "lib").getAbsolutePath(),
					OUTPUT_DIR
			);
		} else {
			//unix - need to call /bin/sh because the script does not have the execution flags set
			pb = new ProcessBuilder(
					"/bin/sh",
					UNIX_PATH,
					new File(projectRoot, "src").getAbsolutePath(),
					new File(projectRoot, "lib").getAbsolutePath(),
					OUTPUT_DIR
			);
		}
					
		

		try {
			Process p = pb.start();
			String errors = CharStreams.toString(new InputStreamReader(p.getErrorStream()));
			assertEquals("", errors);

			assertTrue(new File(OUTPUT_DIR, "org/stjs/hello/HelloWorld.js").exists());
		}
		catch (IOException e) {
			throw e;
		}
	}
}
