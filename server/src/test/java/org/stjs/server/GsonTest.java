package org.stjs.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.lang.reflect.Type;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Test;
import org.stjs.javascript.Array;
import org.stjs.javascript.Date;
import org.stjs.javascript.JSCollections;
import org.stjs.javascript.Map;
import org.stjs.server.json.gson.GsonAdapters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class GsonTest {
	@Test
	public void testSerializeArray() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		GsonAdapters.addAll(gsonBuilder);
		Gson gson = gsonBuilder.create();
		String s = gson.toJson(JSCollections.$array(1, 2, 3));
		assertEquals("[1,2,3]", s);
	}

	@Test
	public void testSerializeArrayPojo() throws JsonGenerationException, JsonMappingException, IOException {
		GsonBuilder gsonBuilder = new GsonBuilder();
		GsonAdapters.addAll(gsonBuilder);
		Gson gson = gsonBuilder.create();

		String s = gson.toJson(JSCollections.$array(new Pojo(1), new Pojo(2)));
		assertEquals("[{\"n\":1},{\"n\":2}]", s);
	}

	@Test
	public void testSerializeMap() throws JsonGenerationException, JsonMappingException, IOException {
		GsonBuilder gsonBuilder = new GsonBuilder();
		GsonAdapters.addAll(gsonBuilder);
		Gson gson = gsonBuilder.create();

		String s = gson.toJson(JSCollections.$map("a", 1));
		assertEquals("{\"a\":1}", s);
	}

	@Test
	public void testSerializeMapPojo() throws JsonGenerationException, JsonMappingException, IOException {
		GsonBuilder gsonBuilder = new GsonBuilder();
		GsonAdapters.addAll(gsonBuilder);
		Gson gson = gsonBuilder.create();

		String s = gson.toJson(JSCollections.$map("A", new Pojo(1)));
		assertEquals("{\"A\":{\"n\":1}}", s);
	}

	@Test
	public void testSerializeDate() throws JsonGenerationException, JsonMappingException, IOException {
		GsonBuilder gsonBuilder = new GsonBuilder();
		GsonAdapters.addAll(gsonBuilder);
		Gson gson = gsonBuilder.create();

		String s = gson.toJson(new Date(2011, 10, 9, 17, 10, 0, 0));
		assertEquals("\"2011-11-09 17:10:00\"", s);
	}

	@Test
	public void testDeserializeArray() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		GsonAdapters.addAll(gsonBuilder);
		Gson gson = gsonBuilder.create();
		Type collectionType = new TypeToken<Array<Integer>>() {
		}.getType();
		Array<Integer> a = gson.fromJson("[1,2,3]", collectionType);
		assertNotNull(a);
		assertEquals(3, a.$length());
		assertEquals(2, (int) a.$get(1));
	}

	@Test
	public void testDeserializeMap() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		GsonAdapters.addAll(gsonBuilder);
		Gson gson = gsonBuilder.create();
		Type collectionType = new TypeToken<Map<String, Integer>>() {
		}.getType();
		Map<String, Integer> m = gson.fromJson("{\"a\":1}", collectionType);
		assertNotNull(m);
		assertEquals(1, (int) m.$get("a"));
	}

	@Test
	public void testDeserializeDate() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		GsonAdapters.addAll(gsonBuilder);
		Gson gson = gsonBuilder.create();
		Date d = gson.fromJson("\"2011-11-09 17:10:00\"", Date.class);
		assertNotNull(d);
		assertEquals(10, (int) d.getMonth());
	}

}
