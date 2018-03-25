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

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.deser.ContainerDeserializer;
import org.codehaus.jackson.type.JavaType;
import org.stjs.javascript.Array;
import org.stjs.javascript.JSCollections;

/**
 * <p>JSArrayDeserializer class.</p>
 *
 * @author acraciun
 * @version $Id: $Id
 */
public class JSArrayDeserializer extends ContainerDeserializer<Array<Object>> {

	// // Configuration

	protected final JavaType _collectionType;

	/**
	 * Value deserializer.
	 */
	final JsonDeserializer<Object> _valueDeserializer;

	/**
	 * If element instances have polymorphic type information, this is the type deserializer that can handle it
	 */
	final TypeDeserializer _valueTypeDeserializer;

	/**
	 * <p>Constructor for JSArrayDeserializer.</p>
	 *
	 * @param collectionType a {@link org.codehaus.jackson.type.JavaType} object.
	 * @param valueDeser a {@link org.codehaus.jackson.map.JsonDeserializer} object.
	 * @param valueTypeDeser a {@link org.codehaus.jackson.map.TypeDeserializer} object.
	 */
	public JSArrayDeserializer(JavaType collectionType, JsonDeserializer<Object> valueDeser,
			TypeDeserializer valueTypeDeser) {
		super(collectionType.getRawClass());
		_collectionType = collectionType;
		_valueDeserializer = valueDeser;
		_valueTypeDeserializer = valueTypeDeser;
	}

	/*
	 * /********************************************************** /* ContainerDeserializer API
	 * /**********************************************************
	 */

	/** {@inheritDoc} */
	@Override
	public JavaType getContentType() {
		return _collectionType.getContentType();
	}

	/** {@inheritDoc} */
	@Override
	public JsonDeserializer<Object> getContentDeserializer() {
		return _valueDeserializer;
	}

	/*
	 * /********************************************************** /* JsonDeserializer API
	 * /**********************************************************
	 */

	/** {@inheritDoc} */
	@Override
	public Array<Object> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException,
			JsonProcessingException {
		Array<Object> result = JSCollections.$array();

		return deserialize(jp, ctxt, result);
	}

	/** {@inheritDoc} */
	@Override
	public Array<Object> deserialize(JsonParser jp, DeserializationContext ctxt, Array<Object> result)
			throws IOException, JsonProcessingException {
		// Ok: must point to START_ARRAY (or equivalent)
		if (!jp.isExpectedStartArrayToken()) {
			return handleNonArray(jp, ctxt, result);
		}

		JsonDeserializer<Object> valueDes = _valueDeserializer;
		JsonToken t;
		final TypeDeserializer typeDeser = _valueTypeDeserializer;

		while ((t = jp.nextToken()) != JsonToken.END_ARRAY) {
			Object value;

			if (t == JsonToken.VALUE_NULL) {
				value = null;
			} else if (typeDeser == null) {
				value = valueDes.deserialize(jp, ctxt);
			} else {
				value = valueDes.deserializeWithType(jp, ctxt, typeDeser);
			}
			result.push(value);
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
			throws IOException, JsonProcessingException {
		// In future could check current token... for now this should be enough:
		return typeDeserializer.deserializeTypedFromArray(jp, ctxt);
	}

	/**
	 * Helper method called when current token is no START_ARRAY. Will either throw an exception, or try to handle value
	 * as if member of implicit array, depending on configuration.
	 */
	private final Array<Object> handleNonArray(JsonParser jp, DeserializationContext ctxt, Array<Object> result)
			throws IOException, JsonProcessingException {
		// [JACKSON-526]: implicit arrays from single values?
		if (!ctxt.isEnabled(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
			throw ctxt.mappingException(_collectionType.getRawClass());
		}
		JsonDeserializer<Object> valueDes = _valueDeserializer;
		final TypeDeserializer typeDeser = _valueTypeDeserializer;
		JsonToken t = jp.getCurrentToken();

		Object value;

		if (t == JsonToken.VALUE_NULL) {
			value = null;
		} else if (typeDeser == null) {
			value = valueDes.deserialize(jp, ctxt);
		} else {
			value = valueDes.deserializeWithType(jp, ctxt, typeDeser);
		}
		result.push(value);
		return result;
	}
}
