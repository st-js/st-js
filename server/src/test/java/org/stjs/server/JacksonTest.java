package org.stjs.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.stjs.javascript.Array;
import org.stjs.javascript.Date;
import org.stjs.javascript.JSCollections;
import org.stjs.javascript.Map;
import org.stjs.server.json.jackson.STJSModule;

public class JacksonTest {

	private ObjectMapper mapper;

	public JacksonTest(){
		mapper = new ObjectMapper();
		mapper.registerModule(STJSModule.getModule());
	}

	@Test
	public void testSerializeArray() throws JsonGenerationException, JsonMappingException, IOException {
		String s = mapper.writeValueAsString(JSCollections.$array(1, 2, 3));
		assertEquals("[1,2,3]", s);
	}

	@Test
	public void testSerializeArrayPojo() throws JsonGenerationException, JsonMappingException, IOException {
		String s = mapper.writeValueAsString(JSCollections.$array(new Pojo(1), new Pojo(2)));
		assertEquals("[{\"n\":1},{\"n\":2}]", s);
	}

	@Test(expected = JsonGenerationException.class)
	public void testSerializeArraySparse() throws IOException {
		Array<Integer> holes = new Array<>();
		holes.$set(0, 0);
		holes.$set(1, null);
		holes.$set(4, 1); // array is now sparse

		mapper.writeValueAsString(holes);
	}

	@Test
	public void testSerializeArrayPojoChildren() throws JsonGenerationException, JsonMappingException, IOException {
		Pojo2 p = new Pojo2();
		p.setChildren(JSCollections.$array(new Pojo(1)));
		String s = mapper.writeValueAsString(p);
		assertEquals("{\"children\":[{\"n\":1}]}", s);
	}

	@Test
	public void testSerializeMap() throws JsonGenerationException, JsonMappingException, IOException {
		String s = mapper.writeValueAsString(JSCollections.$map("a", 1));
		assertEquals("{\"a\":1}", s);
	}

	@Test
	public void testSerializeMapPojo() throws JsonGenerationException, JsonMappingException, IOException {
		String s = mapper.writeValueAsString(JSCollections.$map("A", new Pojo(1)));
		assertEquals("{\"A\":{\"n\":1}}", s);
	}

	@Test
	public void testSerializeMapPojoChildren() throws JsonGenerationException, JsonMappingException, IOException {
		Pojo3 p = new Pojo3();
		p.setChildren(JSCollections.$map("b", new Pojo(1)));
		String s = mapper.writeValueAsString(p);
		assertEquals("{\"children\":{\"b\":{\"n\":1}}}", s);
	}

	@Test
	public void testSerializeDate() throws JsonGenerationException, JsonMappingException, IOException {
		String s = mapper.writeValueAsString(new Date(2011, 10, 9, 17, 10, 0, 0));
		assertEquals("\"2011-11-09 17:10:00\"", s);
	}

	@Test
	public void testDeserializeArray() throws JsonParseException, JsonMappingException, IOException {
		Array<Integer> a = mapper.readValue("[1,2,3]",
				mapper.getTypeFactory().constructCollectionLikeType(Array.class, Integer.class));
		assertNotNull(a);
		assertEquals(3, a.$length());
		assertEquals(2, (int) a.$get(1));
	}

	@Test
	public void testDeserializeArrayChildren() throws JsonParseException, JsonMappingException, IOException {
		Pojo2 p = mapper.readValue("{\"children\":[{\"n\":1}]}", Pojo2.class);
		assertNotNull(p);
		assertNotNull(p.getChildren());
		assertEquals(1, p.getChildren().$length());
		assertEquals(1, p.getChildren().$get(0).getN());
	}

	@Test
	public void testDeserializeMap() throws JsonParseException, JsonMappingException, IOException {
		Map<String, Integer> m = mapper.readValue("{\"a\":1}",
				mapper.getTypeFactory().constructMapLikeType(Map.class, String.class, Integer.class));
		assertNotNull(m);
		assertEquals(1, (int) m.$get("a"));
	}

	@Test
	public void testDeserializeMapChildren() throws JsonParseException, JsonMappingException, IOException {
		Pojo3 p = mapper.readValue("{\"children\":{\"b\":{\"n\":1}}}", Pojo3.class);
		assertNotNull(p);
		assertNotNull(p.getChildren());
		assertNotNull(p.getChildren().$get("b"));
		assertEquals(1, p.getChildren().$get("b").getN());
	}

	@Test
	public void testDeserializeDate() throws JsonParseException, JsonMappingException, IOException {
		Date d = mapper.readValue("\"2011-11-09 17:10:00\"", Date.class);
		assertNotNull(d);
		assertEquals(10, (int) d.getMonth());
	}
}
