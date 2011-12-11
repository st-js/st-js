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

public class ExecutionResult {
	private final String stdOut;
	private final String stdErr;
	private final int exitValue;
	private final Object result;

	public ExecutionResult(Object result, String stdOut, String stdErr, int exitValue) {
		this.stdOut = stdOut;
		this.stdErr = stdErr;
		this.exitValue = exitValue;
		this.result = result;
	}

	public String getStdOut() {
		return stdOut;
	}

	public String getStdErr() {
		return stdErr;
	}

	public int getExitValue() {
		return exitValue;
	}

	public Object getResult() {
		return result;
	}

	@Override
	public String toString() {
		if (stdOut.isEmpty() && stdErr.isEmpty()) {
			return "Execution was sucessful";
		}
		return String.format("result: %s, exitValue : %s\nstdout : %s\nstderr :%s", result != null ? result.toString()
				: "null", exitValue, stdOut, stdErr);
	}
}
