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
package org.stjs.generator.handlers;

public class JavascriptWriter {
	private int level = 0;

	private boolean indented = false;

	private final StringBuilder buf = new StringBuilder();

	public JavascriptWriter indent() {
		level++;
		return this;
	}

	public JavascriptWriter unindent() {
		level--;
		return this;
	}

	private void makeIndent() {
		for (int i = 0; i < level; i++) {
			buf.append("    ");
		}
	}

	public JavascriptWriter printLiteral(String value) {
		//
		print(value);
		return this;
	}

	public JavascriptWriter printStringLiteral(String value) {
		print("\"");
		print(value);
		print("\"");
		return this;
	}

	public JavascriptWriter print(String arg) {
		if (!indented) {
			makeIndent();
			indented = true;
		}
		buf.append(arg);
		return this;
	}

	public JavascriptWriter printLn(String arg) {
		print(arg);
		printLn();
		return this;
	}

	public JavascriptWriter printLn() {
		buf.append("\n");
		indented = false;
		return this;
	}

	public String getSource() {
		return buf.toString();
	}

	@Override
	public String toString() {
		return getSource();
	}

}
