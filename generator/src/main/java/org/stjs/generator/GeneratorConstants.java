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

import java.util.regex.Pattern;

public final class GeneratorConstants {
	public static final String SPECIAL_THIS = "THIS";

	public static final String STJS = "stjs";

	/**
	 * this is the type to be used when defining an inline type
	 */
	public static final String SPECIAL_INLINE_TYPE = "_InlineType";

	public static final String SUPER = "super";

	public static final String THIS = "this";

	public static final String CLASS = "class";

	public static final String ARGUMENTS_PARAMETER = "arguments";

	public static final String TYPE_DESCRIPTION_PROPERTY = "$typeDescription";

	public static final Pattern NAMESPACE_PATTERN = Pattern.compile("([A-Za-z_][A-Za-z_0-9]*)(?:\\.([A-Za-z_][A-Za-z_0-9]*))*");

	/**
	 * this is the file in which the maven plugin writes the classpath needed to launch tests
	 */
	public static final String CLASSPATH_FILE = "stjs.cp";

	/**
	 * this folder is used by the test runner to write temporary files. the CLASSPATH_FILE is also put in this directory
	 */
	public static final String STJS_TEST_TEMP_FOLDER = "stjs-test";

	public static final String NON_PUBLIC_METHODS_AND_FIELDS_PREFIX = "_";

	public static final String ENUM_NAME_PROPERTY = "_name";

	public static final String ENUM_ORDINAL_PROPERTY = "_ordinal";

	public static final String ENUM_VALUES_PROPERTY = "_values";

	public static final String ENUM_CLASS = "Enum";

	public static final String TRANSPILED_ENUM_CLASS = "JavaEnum";

	public static final String AUTO_GENERATED_ELEMENT_SEPARATOR = "$";

	private GeneratorConstants() {
		//
	}
}
