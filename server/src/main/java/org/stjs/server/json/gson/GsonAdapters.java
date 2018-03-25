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
package org.stjs.server.json.gson;

import org.stjs.javascript.Array;
import org.stjs.javascript.Date;
import org.stjs.javascript.Map;

import com.google.gson.GsonBuilder;

/**
 * <p>GsonAdapters class.</p>
 *
 * @author acraciun
 * @version $Id: $Id
 */
public class GsonAdapters {
	/**
	 * <p>addAll.</p>
	 *
	 * @param builder a {@link com.google.gson.GsonBuilder} object.
	 */
	public static void addAll(GsonBuilder builder) {
		builder.registerTypeAdapter(Map.class, new JSMapAdapter());
		builder.registerTypeAdapter(Array.class, new JSArrayAdapter());
		builder.registerTypeAdapter(Date.class, new JSDateAdapter());
	}
}
