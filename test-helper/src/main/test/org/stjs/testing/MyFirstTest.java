package org.stjs.testing;

import static junit.framework.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(NodeJSTestRunner.class)
@SourceFiles(files = { "src/main/test/org/stjs/testing/MyFirstTest.java",
    "src/main/test/org/stjs/testing/MyPojo.java" })
public class MyFirstTest {

  @Test 
  public void compilationIsCool() throws Exception {
    MyPojo pojo = new MyPojo();
    pojo.x = 3;
    pojo.y = "hello";
    assertEquals(3, pojo.x);
    assertEquals("hello2", pojo.y);
  }
}
