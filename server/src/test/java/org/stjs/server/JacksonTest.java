package org.stjs.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Ignore;
import org.junit.Test;
import org.stjs.javascript.Array;
import org.stjs.javascript.Date;
import org.stjs.javascript.JSCollections;
import org.stjs.javascript.Map;
import org.stjs.server.json.jackson.STJSModule;

public class JacksonTest {
	@Test
	public void testSerializeArray() throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(STJSModule.getModule());

		String s = mapper.writeValueAsString(JSCollections.$array(1, 2, 3));
		assertEquals("[1,2,3]", s);
	}

	@Test
	public void testSerializeArrayPojo() throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(STJSModule.getModule());

		String s = mapper.writeValueAsString(JSCollections.$array(new Pojo("a", 1), new Pojo("b", 2)));
		assertEquals("[{\"s\":\"a\",\"n\":1},{\"s\":\"b\",\"n\":2}]", s);
	}

	@Test
	public void testSerializeMap() throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(STJSModule.getModule());

		String s = mapper.writeValueAsString(JSCollections.$map("a", 1));
		assertEquals("{\"a\":1}", s);
	}

	@Test
	public void testSerializeMapPojo() throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(STJSModule.getModule());

		String s = mapper.writeValueAsString(JSCollections.$map("A", new Pojo("a", 1)));
		assertEquals("{\"A\":{\"s\":\"a\",\"n\":1}}", s);
	}

	@Test
	public void testSerializeDate() throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(STJSModule.getModule());

		String s = mapper.writeValueAsString(new Date(2011, 10, 9, 17, 10, 0, 0));
		assertEquals("\"2011-11-09 17:10:00\"", s);
	}

	@Ignore
	// not yet implemented
	public void testDeserializeArray() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(STJSModule.getModule());
		@SuppressWarnings("unchecked")
		Array<Integer> a = mapper.readValue("[1,2,3]", Array.class);
		assertNotNull(a);
		assertEquals(3, a.$length());
		assertEquals(2, (int) a.$get(1));
	}

	@Ignore
	// not yet implemented
	public void testDeserializeMap() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(STJSModule.getModule());

		@SuppressWarnings("unchecked")
		Map<String, Integer> m = mapper.readValue("{\"a\":1}", Map.class);
		assertNotNull(m);
		assertEquals(1, (int) m.$get("a"));
	}

	@Test
	public void testDeserializeDate() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(STJSModule.getModule());

		Date d = mapper.readValue("\"2011-11-09 17:10:00\"", Date.class);
		assertNotNull(d);
		assertEquals(10, (int) d.getMonth());
	}
}
