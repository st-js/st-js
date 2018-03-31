/**
 *  Copyright 2011 Alexandru Craciun, Eyal Kaspi
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.stjs.generator.executor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import org.stjs.generator.Generator;

import edu.umd.cs.findbugs.annotations.SuppressWarnings;

import javax.script.ScriptException;

/**
 * <p>NodeJSExecutor class.</p>
 *
 * @author acraciun
 * @version $Id: $Id
 */
public class NodeJSExecutor implements Executor {
	private static final String NODE_JS = "node";
	private static final String NODE_JS_WINDOWS = "C:\\Program Files\\nodejs\\node.exe";

	private void addScript(OutputStream out, InputStream in) throws IOException {
		byte[] buf = new byte[1 << 20];

		int b = 0;
		while ( (b = in.read(buf)) >= 0) {
			out.write(buf, 0, b);
			out.flush();
		}

		out.write("\n\n".getBytes(StandardCharsets.UTF_8));
	}

	protected boolean isRunningOnWindows() {
		return System.getProperty("os.name").contains("Windows");
	}

	String getExecutable() {
		return isRunningOnWindows() ? NODE_JS_WINDOWS : NODE_JS;
	}

	/**
	 * <p>run.</p>
	 *
	 * @param srcFiles a {@link java.io.File} object.
	 * @return a {@link org.stjs.generator.executor.ExecutionResult} object.
	 */
	@SuppressWarnings(
			value = "REC_CATCH_EXCEPTION")
	public ExecutionResult run(Collection<File> srcFiles, boolean mainClassDisabled) throws ScriptException {
		try {
			File temp = File.createTempFile("javascript", ".js");

			OutputStream out = new FileOutputStream(temp);

			addScript(out, Thread.currentThread().getContextClassLoader().getResourceAsStream(Generator.STJS_PATH));

			if (mainClassDisabled) {
				out.write("stjs.mainCallDisabled=true;".getBytes(StandardCharsets.UTF_8));
			}

			for (File srcFile : srcFiles) {
				addScript(out, new FileInputStream(srcFile));
			}

			out.close();

			Process p = Runtime.getRuntime().exec(new String[]{ getExecutable(), temp.getAbsolutePath() });
			int exitValue = p.waitFor();

			String output = readStream(p.getInputStream());

			return new ExecutionResult(
					output.split("\\n+$", 2)[0],
					output,
					readStream(p.getErrorStream()),
					exitValue
			);
		}
		catch (IOException e) {
			// TODO : this is not really going to be working on all OS!
			if (e.getMessage().contains("Cannot run program")) {
				String errMsg = "Please install node.js to use this feature https://github.com/joyent/node/wiki/Installation";
				throw new ScriptException(errMsg);
			}
			throw new ScriptException(e);
		}
		catch (InterruptedException e) {
			throw new ScriptException(e);
		}
	}

	private String readStream(InputStream errStream) throws IOException {
		StringBuilder builder = new StringBuilder();
		// XXX: here i may need to get the charset from configuration
		BufferedReader in = new BufferedReader(new InputStreamReader(errStream, "UTF-8"));
		for (String line = in.readLine(); line != null; line = in.readLine()) {
			builder.append(line);
			builder.append('\n');
		}
		return builder.toString();
	}
}
