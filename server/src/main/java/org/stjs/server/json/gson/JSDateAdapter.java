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

import java.lang.reflect.Type;

import org.stjs.javascript.Date;
import org.stjs.server.json.JSDateUtils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * <p>JSDateAdapter class.</p>
 *
 * @author acraciun
 * @version $Id: $Id
 */
public class JSDateAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {

	/** {@inheritDoc} */
	@Override
	public Date deserialize(JsonElement elem, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		if (elem == null) {
			return null;
		}
		return new Date(elem.getAsString());
	}

	/** {@inheritDoc} */
	@Override
	public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
		if (src == null) {
			return new JsonNull();
		}
		return new JsonPrimitive(JSDateUtils.toNormalizedString(src));
	}

}
