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
package org.stjs.server.json.jackson;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.stjs.javascript.Map;

/**
 * <p>JSMapSerializer class.</p>
 *
 * @author acraciun
 * @version $Id: $Id
 */
public class JSMapSerializer extends JsonSerializer<Map<?, ?>> {
	/** {@inheritDoc} */
	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public Class<Map<?, ?>> handledType() {
		return (Class) Map.class;
	}

	/** {@inheritDoc} */
	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public void serialize(Map<?, ?> map, JsonGenerator gen, SerializerProvider provider) throws IOException, JsonProcessingException {
		if (map == null) {
			gen.writeNull();
			return;
		}

		gen.writeStartObject();
		for (Object k : map) {
			provider.defaultSerializeField(k.toString(), ((Map) map).$get(k.toString()), gen);
		}
		gen.writeEndObject();

	}

}
