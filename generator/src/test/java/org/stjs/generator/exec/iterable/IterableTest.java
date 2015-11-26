package org.stjs.generator.exec.iterable;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;

import static org.junit.Assert.assertEquals;

public class IterableTest extends AbstractStjsTest {

    @Test
    public void testCanIterate() {
        assertEquals("1,2,3,5,8,13,21,", execute(Iterable1.class));
    }

}
