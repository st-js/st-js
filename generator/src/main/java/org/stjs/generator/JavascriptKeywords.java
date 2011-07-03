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
