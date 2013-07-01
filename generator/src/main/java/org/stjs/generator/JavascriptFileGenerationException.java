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
package org.stjs.generator;

import java.io.File;

import org.stjs.generator.ast.SourcePosition;

/**
 * This is the exception thrown by the Generator.
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 */
public class JavascriptFileGenerationException extends STJSRuntimeException {
	private static final long serialVersionUID = 1L;

	private final File inputFile;

	private final SourcePosition sourcePosition;

	public JavascriptFileGenerationException(File inputFile, SourcePosition sourcePosition, String message,
			Throwable cause) {
		super(message, cause);
		this.inputFile = inputFile;
		this.sourcePosition = sourcePosition;
	}

	public JavascriptFileGenerationException(File inputFile, SourcePosition sourcePosition, String message) {
		super(message);
		this.inputFile = inputFile;
		this.sourcePosition = sourcePosition;
	}

	public JavascriptFileGenerationException(File inputFile, SourcePosition sourcePosition, Throwable cause) {
		super(cause);
		this.inputFile = inputFile;
		this.sourcePosition = sourcePosition;
	}

	public File getInputFile() {
		return inputFile;
	}

	public SourcePosition getSourcePosition() {
		return sourcePosition;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append('(').append(inputFile.getName());
		if (sourcePosition != null) {
			sb.append(':').append(sourcePosition.getLine());
		}
		sb.append(')');

		return sb.toString();
	}
}
