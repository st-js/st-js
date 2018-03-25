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
package org.stjs.server.json;

import java.text.SimpleDateFormat;

import org.stjs.javascript.Date;

/**
 * <p>JSDateUtils class.</p>
 *
 * @author acraciun
 * @version $Id: $Id
 */
public class JSDateUtils {
	private final static String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

	/**
	 * <p>toNormalizedString.</p>
	 *
	 * @param d a {@link org.stjs.javascript.Date} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String toNormalizedString(Date d) {
		// TODO - improve perf here
		return new SimpleDateFormat(DEFAULT_DATE_PATTERN).format(new java.util.Date((long) d.getTime()));
	}

}
