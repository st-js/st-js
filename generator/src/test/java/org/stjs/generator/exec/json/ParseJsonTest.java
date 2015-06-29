package org.stjs.generator.exec.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;
import org.stjs.javascript.Array;
import org.stjs.javascript.Date;
import org.stjs.javascript.Map;

public class ParseJsonTest extends AbstractStjsTest {
	@SuppressWarnings("unchecked")
	private Object getProperty(Object obj, String... props) {
		Object bean = obj;
		for (String p : props) {
			assertNotNull(bean);
			if (bean instanceof Map) {
				bean = ((Map<String, ?>) bean).$get(p);
			} else if (bean instanceof Array) {
				bean = ((Array<?>) bean).$get(p);
			} else {
				fail(bean + " is not a map or array. Type is:" + bean.getClass().getName());
				return null;
			}
		}
		return bean;
	}

	private void assertProperty(Object expected, Object obj, String... props) {
		Object bean = getProperty(obj, props);
		if (bean instanceof Number) {
			bean = new Double(((Number) bean).doubleValue());
		}
		assertEquals(expected, bean);
	}

	@Test
	public void testSimple() {
		Object result = execute(Json1.class);
		assertProperty(1.0, result, "a");
		assertProperty(2.0, result, "children", "0", "i");
		assertProperty("Inner", result, "children", "0", "type");
		assertProperty("Class1", result, "type");
	}

	@Test
	public void testMap() {
		Object result = execute(Json2.class);
		assertProperty("Class2", result, "type");
		assertProperty(1.0, result, "map", "key", "a");
		assertProperty("Class1", result, "map", "key", "type");
	}

	@Test
	public void testMapOfMap() {
		Object result = execute(Json3.class);
		assertProperty("Class3", result, "type");
		assertProperty(1.0, result, "map", "key1", "key2", "a");
		assertProperty("Class1", result, "map", "key1", "key2", "type");
	}

	@Test
	public void testDate() {
		Object result = execute(Json4.class);
		assertProperty("Class4", result, "type");
		Date d = (Date) getProperty(result, "date");
		assertEquals(11, d.getMonth(), 0.1);
		assertEquals(18, d.getHours(), 0.1);
	}

	@Test
	public void testDateWithTypefy() {
		Object result = execute(Json4b.class);
		assertProperty("Class4", result, "type");
		Date d = (Date) getProperty(result, "date");
		assertEquals(11, d.getMonth(), 0.1);
		assertEquals(18, d.getHours(), 0.1);
	}

	@Test
	public void testEnum() {
		Object result = execute(Json5.class);
		assertProperty("Class5", result, "type");
		assertProperty(1.0, result, "e", "_ordinal");
		assertProperty("b", result, "e", "_name");
	}

	@Test
	public void testEnumWithTypefy() {
		Object result = execute(Json5b.class);
		assertProperty("Class5", result, "type");
		assertProperty(1.0, result, "e", "_ordinal");
		assertProperty("b", result, "e", "_name");
	}

	@Test
	public void testEnumWithStringify() {
		Object result = execute(Json5c.class);
		assertProperty("Class5", result, "type");
		assertProperty(2.0, result, "number");
		assertProperty("b", result, "e");
		assertProperty(4.0, result, "child", "number");
		assertProperty(null, result, "equals");
	}

	@Test
	public void testSuperClass() {
		Object result = execute(Json6.class);
		assertProperty(1.0, result, "a");
		assertProperty(2.0, result, "children", "0", "i");
		assertProperty("Inner", result, "children", "0", "type");
		assertProperty("Class6", result, "type");

		assertProperty(3.0, result, "child", "i");
		assertProperty("Inner", result, "child", "type");
	}
}
