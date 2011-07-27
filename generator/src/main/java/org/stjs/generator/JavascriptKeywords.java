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
import java.util.HashSet;
import java.util.Set;

/**
 * This class checks if a method or a variable has the name of a Javascript keyword. Even though the Java compiler lets
 * the user use some of the keywords as variable names, the Generator should not generate code with these names.
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 */
public class JavascriptKeywords {
	private static final Set<String> keywords = new HashSet<String>();
	static {
		keywords.add("var");
		keywords.add("outer");
		keywords.add("function");
		// keywords.add("self");
	}

	public static void checkIdentifier(File inputFile, SourcePosition pos, String name) {
		if (keywords.contains(name)) {
			throw new JavascriptGenerationException(inputFile, pos, "Wrong usage of keyword:" + name);
		}
	}

	public static void checkMethod(File inputFile, SourcePosition sourcePosition, String name) {
		if (keywords.contains(name)) {
			throw new JavascriptGenerationException(inputFile, sourcePosition, "Wrong usage of keyword:" + name);
		}
	}
}
