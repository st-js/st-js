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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.stjs.javascript.JSCollections;
import org.stjs.javascript.Map;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class JSMapAdapter implements JsonSerializer<Map<?, ?>>, JsonDeserializer<Map<?, ?>> {

	@Override
	public Map<?, ?> deserialize(JsonElement elem, Type type, JsonDeserializationContext ctx) throws JsonParseException {
		if (elem == null) {
			return null;
		}
		JsonObject js = elem.getAsJsonObject();
		Type valueType = (type instanceof ParameterizedType) ? ((ParameterizedType) type).getActualTypeArguments()[1]
				: Object.class;
		Map<String, Object> map = JSCollections.$map();
		for (java.util.Map.Entry<String, JsonElement> entry : js.entrySet()) {
			map.$put(entry.getKey(), ctx.deserialize(entry.getValue(), valueType));
		}

		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JsonElement serialize(Map<?, ?> map, Type typeOfSrc, JsonSerializationContext ctx) {
		if (map == null) {
			return new JsonNull();
		}
		JsonObject js = new JsonObject();
		for (Object k : map) {
			js.add(k.toString(), ctx.serialize(((Map) map).$get(k)));
		}
		return js;
	}

}
