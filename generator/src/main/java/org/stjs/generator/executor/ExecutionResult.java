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

/**
 * <p>ExecutionResult class.</p>
 *
 * @author acraciun
 * @version $Id: $Id
 */
public class ExecutionResult {
	private final String stdOut;
	private final String stdErr;
	private final int exitValue;
	private final Object result;

	/**
	 * <p>Constructor for ExecutionResult.</p>
	 *
	 * @param result a {@link java.lang.Object} object.
	 * @param stdOut a {@link java.lang.String} object.
	 * @param stdErr a {@link java.lang.String} object.
	 * @param exitValue a int.
	 */
	public ExecutionResult(Object result, String stdOut, String stdErr, int exitValue) {
		this.stdOut = stdOut;
		this.stdErr = stdErr;
		this.exitValue = exitValue;
		this.result = result;
	}

	/**
	 * <p>Getter for the field <code>stdOut</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getStdOut() {
		return stdOut;
	}

	/**
	 * <p>Getter for the field <code>stdErr</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getStdErr() {
		return stdErr;
	}

	/**
	 * <p>Getter for the field <code>exitValue</code>.</p>
	 *
	 * @return a int.
	 */
	public int getExitValue() {
		return exitValue;
	}

	/**
	 * <p>Getter for the field <code>result</code>.</p>
	 *
	 * @return a {@link java.lang.Object} object.
	 */
	public Object getResult() {
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		if (stdOut.isEmpty() && stdErr.isEmpty()) {
			return "Execution was successful";
		}
		return String.format("result: %s, exitValue : %s%nstdout : %s%nstderr :%s", result == null ? "null" : result.toString(), exitValue,
				stdOut, stdErr);
	}
}
