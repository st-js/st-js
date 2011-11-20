package org.stjs.server.json.jackson;

import org.codehaus.jackson.map.BeanDescription;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializerProvider;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.KeyDeserializer;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.module.SimpleDeserializers;
import org.codehaus.jackson.map.type.CollectionLikeType;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.MapLikeType;
import org.codehaus.jackson.map.type.MapType;
import org.codehaus.jackson.type.JavaType;
import org.stjs.javascript.Array;
import org.stjs.javascript.Map;

public class STJSDeserializers extends SimpleDeserializers {
	@Override
	public JsonDeserializer<?> findBeanDeserializer(JavaType type, DeserializationConfig config,
			DeserializerProvider provider, BeanDescription beanDesc, BeanProperty property) throws JsonMappingException {
		// TODO Auto-generated method stub
		return super.findBeanDeserializer(type, config, provider, beanDesc, property);
	}

	@Override
	public JsonDeserializer<?> findCollectionDeserializer(CollectionType type, DeserializationConfig config,
			DeserializerProvider provider, BeanDescription beanDesc, BeanProperty property,
			TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer)
			throws JsonMappingException {
		if (type.getRawClass() == Array.class) {
			return new JSArrayDeserializer(type,
					provider.findValueDeserializer(config, type.getContentType(), property), elementTypeDeserializer);
		}
		return super.findCollectionDeserializer(type, config, provider, beanDesc, property, elementTypeDeserializer,
				elementDeserializer);
	}

	@Override
	public JsonDeserializer<?> findCollectionLikeDeserializer(CollectionLikeType type, DeserializationConfig config,
			DeserializerProvider provider, BeanDescription beanDesc, BeanProperty property,
			TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer)
			throws JsonMappingException {
		if (type.getRawClass() == Array.class) {
			return new JSArrayDeserializer(type,
					provider.findValueDeserializer(config, type.getContentType(), property), elementTypeDeserializer);
		}

		return super.findCollectionLikeDeserializer(type, config, provider, beanDesc, property,
				elementTypeDeserializer, elementDeserializer);
	}

	@Override
	public JsonDeserializer<?> findMapDeserializer(MapType type, DeserializationConfig config,
			DeserializerProvider provider, BeanDescription beanDesc, BeanProperty property,
			KeyDeserializer keyDeserializer, TypeDeserializer elementTypeDeserializer,
			JsonDeserializer<?> elementDeserializer) throws JsonMappingException {
		if (type.getRawClass() == Map.class) {
			return new JSMapDeserializer(type, keyDeserializer, provider.findValueDeserializer(config,
					type.getContentType(), property), elementTypeDeserializer);
		}
		return super.findMapDeserializer(type, config, provider, beanDesc, property, keyDeserializer,
				elementTypeDeserializer, elementDeserializer);
	}

	@Override
	public JsonDeserializer<?> findMapLikeDeserializer(MapLikeType type, DeserializationConfig config,
			DeserializerProvider provider, BeanDescription beanDesc, BeanProperty property,
			KeyDeserializer keyDeserializer, TypeDeserializer elementTypeDeserializer,
			JsonDeserializer<?> elementDeserializer) throws JsonMappingException {
		if (type.getRawClass() == Map.class) {
			return new JSMapDeserializer(type, keyDeserializer, provider.findValueDeserializer(config,
					type.getContentType(), property), elementTypeDeserializer);
		}
		return super.findMapLikeDeserializer(type, config, provider, beanDesc, property, keyDeserializer,
				elementTypeDeserializer, elementDeserializer);
	}

}
