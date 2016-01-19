package org.stjs.generator.lib.bool;

import org.junit.Assert;
import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;

import static org.junit.Assert.assertEquals;

public class BooleanMethodsTest extends AbstractStjsTest {

    @Test
    public void testValueOfString() {
        Assert.assertEquals(true, execute(Boolean1_valueOfString.class));
    }

    @Test
    public void testValueOfBoolean() {
        Assert.assertEquals(true, execute(Boolean2_valueOfBoolean.class));
    }

    @Test
    public void testBooleanValue() {
        Assert.assertEquals(true, execute(Boolean3_booleanValue.class));
    }


}
