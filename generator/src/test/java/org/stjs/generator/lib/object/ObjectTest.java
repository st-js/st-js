package org.stjs.generator.lib.object;

import junit.framework.Assert;
import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;

public class ObjectTest extends AbstractStjsTest {

    @Test
    public void testCanCreateJavaObject() {
        assertCodeContains(JavaObject1_canCreateJavaObject.class, "" +
                "var o = new stjs.Java.Object()._constructor()");

        Assert.assertEquals(true, execute(JavaObject1_canCreateJavaObject.class));
    }

}
