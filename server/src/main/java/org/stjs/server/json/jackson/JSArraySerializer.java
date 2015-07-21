/**
 * Copyright 2011 Alexandru Craciun, Eyal Kaspi
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.stjs.server.json.jackson;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.stjs.javascript.Array;

public class JSArraySerializer extends JsonSerializer<Array<?>> {

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public Class<Array<?>> handledType() {
		return (Class) Array.class;
	}

	@Override
	public void serialize(Array<?> array, JsonGenerator gen, SerializerProvider provider) throws IOException, JsonProcessingException {
		if (array == null) {
			gen.writeNull();
			return;
		}

		List<?> list;
		// validate that we have a packed array (no unset elements) and that we do not
		// have any non-array indices. JSON supports none of these features, and toList()
		// detects them and rejects them too.
		try {
			list = array.toList();
		}
		catch (IllegalStateException ise) {
			throw new JsonGenerationException("Could not serialize Array", ise);
		}

		provider.defaultSerializeValue(list, gen);
	}

}
