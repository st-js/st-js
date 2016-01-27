package org.stjs.generator.lib.array;

import junit.framework.Assert;
import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;

public class ArrayMethodsTest extends AbstractStjsTest {

    @Test
    public void testArrayClone() throws Exception {
        Assert.assertEquals("A,B,C - AA,BB,CC", execute(Array1_clone.class));

    }
}
