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
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.stjs.javascript.Array;
import org.stjs.javascript.JSCollections;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class JSArrayAdapter implements JsonSerializer<Array<?>>, JsonDeserializer<Array<?>> {

	@Override
	public Array<?> deserialize(JsonElement elem, Type type, JsonDeserializationContext ctx) throws JsonParseException {
		if (elem == null) {
			return null;
		}
		JsonArray js = elem.getAsJsonArray();
		Type elementType = (type instanceof ParameterizedType) ? ((ParameterizedType) type).getActualTypeArguments()[0] : Object.class;
		Array<Object> array = JSCollections.$array();
		for (int i = 0; i < js.size(); ++i) {
			array.push(ctx.deserialize(js.get(i), elementType));
		}

		return array;
	}

	@Override
	public JsonElement serialize(Array<?> array, Type type, JsonSerializationContext ctx) {
		if (array == null) {
			return new JsonNull();
		}
		JsonArray js = new JsonArray();

		// validate that we have a packed array (no unset elements) and that we do not
		// have any non-array indices. JSON supports none of these features, and toList()
		// detects them and rejects them too.
		List<?> list = array.toList();
		for (Object o : list) {
			js.add(ctx.serialize(o));
		}
		return js;
	}

}
