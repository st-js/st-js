import org.junit.Test;
import org.junit.runner.RunWith;
import org.stjs.testing.driver.STJSTestDriverRunner;

import static org.junit.Assert.assertEquals;

@RunWith(STJSTestDriverRunner.class)
public class TestDefaultPackageExample {

	@Test
	public void testAdd(){
		DefaultPackageExample ex = new DefaultPackageExample();
		assertEquals(5, ex.add(2, 3));
	}
}
