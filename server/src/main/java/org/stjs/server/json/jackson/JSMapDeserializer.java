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
import java.lang.reflect.InvocationTargetException;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.KeyDeserializer;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.deser.ContainerDeserializer;
import org.codehaus.jackson.type.JavaType;
import org.stjs.javascript.JSCollections;
import org.stjs.javascript.Map;

public class JSMapDeserializer extends ContainerDeserializer<Map<String, Object>> {

	final protected JavaType _mapType;

	/**
	 * Key deserializer used, if not null. If null, String from JSON content is used as is.
	 */
	final protected KeyDeserializer _keyDeserializer;

	/**
	 * Value deserializer.
	 */
	final protected JsonDeserializer<Object> _valueDeserializer;

	/**
	 * If value instances have polymorphic type information, this is the type deserializer that can handle it
	 */
	final protected TypeDeserializer _valueTypeDeserializer;

	/*
	 * /********************************************************** /* Life-cycle
	 * /**********************************************************
	 */

	public JSMapDeserializer(JavaType mapType, KeyDeserializer keyDeser, JsonDeserializer<Object> valueDeser, TypeDeserializer valueTypeDeser) {
		super(Map.class);
		_mapType = mapType;
		_keyDeserializer = keyDeser;
		_valueDeserializer = valueDeser;
		_valueTypeDeserializer = valueTypeDeser;
	}

	/*
	 * /********************************************************** /* ContainerDeserializer API
	 * /**********************************************************
	 */

	@Override
	public JavaType getContentType() {
		return _mapType.getContentType();
	}

	@Override
	public JsonDeserializer<Object> getContentDeserializer() {
		return _valueDeserializer;
	}

	/*
	 * /********************************************************** /* JsonDeserializer API
	 * /**********************************************************
	 */

	@Override
	public Map<String, Object> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		// Ok: must point to START_OBJECT, FIELD_NAME or END_OBJECT
		JsonToken t = jp.getCurrentToken();
		if ((t != JsonToken.START_OBJECT) && (t != JsonToken.FIELD_NAME) && (t != JsonToken.END_OBJECT)) {
			throw ctxt.mappingException(getMapClass());
		}

		Map<String, Object> result = JSCollections.$map();

		_readAndBind(jp, ctxt, result);
		return result;
	}

	@Override
	public Map<String, Object> deserialize(JsonParser jp, DeserializationContext ctxt, Map<String, Object> result) throws IOException,
			JsonProcessingException {
		// Ok: must point to START_OBJECT or FIELD_NAME
		JsonToken t = jp.getCurrentToken();
		if ((t != JsonToken.START_OBJECT) && (t != JsonToken.FIELD_NAME)) {
			throw ctxt.mappingException(getMapClass());
		}
		_readAndBind(jp, ctxt, result);
		return result;
	}

	@Override
	public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException,
			JsonProcessingException {
		// In future could check current token... for now this should be enough:
		return typeDeserializer.deserializeTypedFromObject(jp, ctxt);
	}

	/*
	 * /********************************************************** /* Other public accessors
	 * /**********************************************************
	 */

	public final Class<?> getMapClass() {
		return _mapType.getRawClass();
	}

	@Override
	public JavaType getValueType() {
		return _mapType;
	}

	/*
	 * /********************************************************** /* Internal methods
	 * /**********************************************************
	 */

	protected final void _readAndBind(JsonParser jp, DeserializationContext ctxt, Map<String, Object> result) throws IOException,
			JsonProcessingException {
		JsonToken t = jp.getCurrentToken();
		if (t == JsonToken.START_OBJECT) {
			t = jp.nextToken();
		}
		final KeyDeserializer keyDes = _keyDeserializer;
		final JsonDeserializer<Object> valueDes = _valueDeserializer;
		final TypeDeserializer typeDeser = _valueTypeDeserializer;
		for (; t == JsonToken.FIELD_NAME; t = jp.nextToken()) {
			// Must point to field name
			String fieldName = jp.getCurrentName();
			Object key = (keyDes == null) ? fieldName : keyDes.deserializeKey(fieldName, ctxt);
			// And then the value...
			t = jp.nextToken();

			// Note: must handle null explicitly here; value deserializers won't
			Object value;
			if (t == JsonToken.VALUE_NULL) {
				value = null;
			} else if (typeDeser == null) {
				value = valueDes.deserialize(jp, ctxt);
			} else {
				value = valueDes.deserializeWithType(jp, ctxt, typeDeser);
			}
			/*
			 * !!! 23-Dec-2008, tatu: should there be an option to verify that there are no duplicate field names?
			 * (and/or what to do, keep-first or keep-last)
			 */
			result.$put(key.toString(), value);
		}
	}

	// note: copied form BeanDeserializer; should try to share somehow...
	protected void wrapAndThrow(Throwable t, Object ref) throws IOException {
		// to handle StackOverflow:
		while ((t instanceof InvocationTargetException) && (t.getCause() != null)) {
			t = t.getCause();
		}
		// Errors and "plain" IOExceptions to be passed as is
		if (t instanceof Error) {
			throw (Error) t;
		}
		// ... except for mapping exceptions
		if ((t instanceof IOException) && !(t instanceof JsonMappingException)) {
			throw (IOException) t;
		}
		throw JsonMappingException.wrapWithPath(t, ref, null);
	}

}
