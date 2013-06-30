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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Collection;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.google.common.base.Charsets;
import com.google.common.io.Closeables;
import com.google.common.io.Files;

public class RhinoExecutor {
	private Object addScript(ScriptEngine engine, File scriptFile) throws ScriptException {
		Reader input = null;
		try {
			// XXX: here i may need to get the charset from configuration
			input = Files.newReader(scriptFile, Charsets.UTF_8);
			return engine.eval(input);
		} catch (FileNotFoundException e) {
			throw new ScriptException(e);
		} finally {
			Closeables.closeQuietly(input);
		}
	}

	public ExecutionResult run(Collection<File> srcFiles, boolean mainClassDisabled) throws ScriptException {
		ScriptEngineManager factory = new ScriptEngineManager();
		ScriptEngine engine = factory.getEngineByName("JavaScript");
		Reader reader = null;
		try {
			reader = new InputStreamReader(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("stjs.js"), "UTF-8");
			engine.eval(reader);
			if (mainClassDisabled) {
				engine.eval("stjs.mainCallDisabled=true;");
			}
			Object result = null;
			for (File srcFile : srcFiles) {
				// keep the result of last evaluation
				result = addScript(engine, srcFile);
			}
			return new ExecutionResult(result, null, null, 0);
		} catch (UnsupportedEncodingException e) {
			throw new ScriptException(e);
		} finally {
			Closeables.closeQuietly(reader);
		}
	}
}
