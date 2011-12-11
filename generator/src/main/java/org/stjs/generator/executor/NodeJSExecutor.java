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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class NodeJSExecutor {
	private static final String nodeJS = "node";

	public ExecutionResult run(File srcFile) {
		try {
			Process p = Runtime.getRuntime().exec(new String[] { nodeJS, srcFile.getAbsolutePath() });
			int exitValue = p.waitFor();
			return new ExecutionResult(null, readStream(p.getInputStream()), readStream(p.getErrorStream()), exitValue);
		} catch (Exception e) {
			// TODO : this is not really going to be working on all OS!
			if (e.getMessage().contains("Cannot run program")) {
				String errMsg = "Please install node.js to use this feature https://github.com/joyent/node/wiki/Installation";
				System.err.println(errMsg);
				throw new RuntimeException(errMsg);
			}
			throw new RuntimeException(e);
		}
	}

	private String readStream(InputStream errStream) throws IOException {
		StringBuilder builder = new StringBuilder();
		BufferedReader in = new BufferedReader(new InputStreamReader(errStream));
		String line;
		while ((line = in.readLine()) != null) {
			builder.append(line);
			builder.append("\n");
		}
		return builder.toString();
	}
}
