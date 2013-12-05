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
package org.stjs.generator.writer;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.stjs.generator.JavascriptFileGenerationException;
import org.stjs.generator.ast.SourcePosition;
import org.stjs.generator.check.Checks;

/**
 * This class checks if a method or a variable has the name of a Javascript keyword. Even though the Java compiler lets
 * the user use some of the keywords as variable names, the Generator should not generate code with these names.
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 */

public final class JavascriptKeywords {

	public static final String PROTOTYPE = "prototype";
	public static final String CONSTRUCTOR = "constructor";
	public static final String VAR = "var";
	public static final String THIS = "this";
	public static final String NULL = "null";

	private static final Set<String> KEYWORDS = new HashSet<String>(Arrays.asList(new String[] { "break", "case", "catch", "continue",
			"debugger", "default", "delete", "do", "else", "finally", "for", "function", "if", "in", "instanceof", "new", "return", "switch",
			"this", "throw", "try", "typeof", "var", "void", "while", "with", "class", "enum", "export", "extends", "import", "super",
			"implements", "interface", "let", "package", "private", "protected", "public", "static", "yield" }));

	private JavascriptKeywords() {
		//
	}

	public static void checkIdentifier(File inputFile, SourcePosition pos, String name) {
	}

	public static void checkIdentifier(File inputFile, SourcePosition pos, String name, Checks checks) {
		if (KEYWORDS.contains(name)) {
			checks.addError(new JavascriptFileGenerationException(inputFile, pos, "Wrong usage of Javascript keyword:" + name));
		}
	}

	public static boolean isReservedWord(String identifier) {
		return KEYWORDS.contains(identifier);
	}

	public static void checkMethod(File inputFile, SourcePosition sourcePosition, String name) {
		if (KEYWORDS.contains(name)) {
			throw new JavascriptFileGenerationException(inputFile, sourcePosition, "Wrong usage of Javascript keyword:" + name);
		}
	}
}
