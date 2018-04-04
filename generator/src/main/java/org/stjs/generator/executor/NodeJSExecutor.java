/**
 * Copyright 2011 Alexandru Craciun, Eyal Kaspi
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

import javax.script.ScriptException;

import org.stjs.generator.Generator;

import edu.umd.cs.findbugs.annotations.SuppressWarnings;

/**
 * <p>NodeJSExecutor class.</p>
 *
 * @author acraciun
 * @version $Id: $Id
 */
public class NodeJSExecutor implements Executor {
	private static final String NODE_JS = "node";
	private static final String NODE_JS_WINDOWS = "C:\\Program Files\\nodejs\\node.exe";

	private static final Integer TWENTY = 20;

	private void addScript(OutputStream out, InputStream in) throws IOException {
		byte[] buf = new byte[1 << TWENTY];

		int b = in.read(buf);
		while (b >= 0) {
			out.write(buf, 0, b);
			out.flush();

			b = in.read(buf);
		}

		out.write("\n\n".getBytes(StandardCharsets.UTF_8));
	}

	private boolean isRunningOnWindows() {
		return System.getProperty("os.name").contains("Windows");
	}

	private String getExecutable() {
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
			// Concatenate all source files together
			File concatenated = concatenateSourceFiles(srcFiles);

			// Convert source to TypeScript
			File converted = convertToTS(concatenated);

			// Add stjs.js to source file
			File complete = finalizeConvertedFile(converted, mainClassDisabled);

			return runFile(new String[] {getExecutable(), complete.getAbsolutePath()});
		}
		catch (IOException e) {
			throw new ScriptException(e);
		}
	}

	private File convertToTS(File toConvert) throws IOException, ScriptException {
		File temp = File.createTempFile("tsConverter", ".js");
		temp.deleteOnExit();

		try (OutputStream out = new FileOutputStream(temp)) {
			String tsPath = "META-INF/resources/webjars/typescript/2.7.2/lib/typescript.js";

			addScript(out, Thread.currentThread().getContextClassLoader().getResourceAsStream(tsPath));

			String script = "\n"
					+ "const fs = require('fs');\n"
					+ "const file = '" + toConvert.getAbsolutePath().replace("\\", "\\\\") + "';\n"
					+ "const source = fs.readFileSync(file, 'utf8');\n"
					+ "const compiled = ts.transpileModule(source, { compilerOptions: { module: ts.ModuleKind.CommonJS } });\n"
					+ "if (!compiled.outputText) { process.exit(1); }\n"
					+ "fs.writeFileSync(file.replace('.ts', '.js'), compiled.outputText);\n"
					+ "//console.error(compiled.outputText );\n"
					+ "//process.exit(1);";

			out.write(script.getBytes(StandardCharsets.UTF_8));
		}

		ExecutionResult result = runFile(new String[] {getExecutable(), temp.getAbsolutePath()});

		if (result.getExitValue() != 0) {
			throw new ScriptException("Failed conversion to TypeScript: " + result.toString());
		}

		File converted =  new File(toConvert.getAbsolutePath().replace(".ts", ".js"));
		converted.deleteOnExit();

		return converted;
	}

	private File concatenateSourceFiles(Collection<File> srcFiles) throws IOException {
		File temp = File.createTempFile("javascript", ".ts");
		temp.deleteOnExit();

		try (OutputStream out = new FileOutputStream(temp)) {
			for (File srcFile : srcFiles) {
				addScript(out, new FileInputStream(srcFile));
			}
		}

		return temp;
	}

	private File finalizeConvertedFile(File converted, boolean mainClassDisabled) throws IOException {
		File temp = File.createTempFile("javascriptAll", ".js");
		temp.deleteOnExit();

		try (OutputStream out = new FileOutputStream(temp)) {
			addScript(out, Thread.currentThread().getContextClassLoader().getResourceAsStream(Generator.STJS_PATH));

			if (mainClassDisabled) {
				out.write("stjs.mainCallDisabled=true;".getBytes(StandardCharsets.UTF_8));
			}

			addScript(out, new FileInputStream(converted));
		}

		return temp;

	}

	private ExecutionResult runFile(String[] args) throws ScriptException {
		try {
			Process p = Runtime.getRuntime().exec(args);
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
