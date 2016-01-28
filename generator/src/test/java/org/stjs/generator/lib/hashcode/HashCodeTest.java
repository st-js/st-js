package org.stjs.generator.lib.hashcode;

import junit.framework.Assert;
import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;

public class HashCodeTest extends AbstractStjsTest {

    @Test
    public void testHashCodesNumbersTypes() throws Exception {
        Assert.assertEquals(true, execute(HashCode1_numbersTypes.class));
    }

    @Test
    public void testHashCodesStrings() throws Exception {
        Assert.assertEquals(true, execute(HashCode2_strings.class));
    }

    @Test
    public void testHashCodesBoolean() throws Exception {
        Assert.assertEquals(true, execute(HashCode3_boolean.class));
    }

    @Test
    public void testHashCodeCustomClassDefaultHashCode() throws Exception {
        Assert.assertEquals(true, execute(HashCode4_customClassDefaultHashCode.class));
    }

    @Test
    public void testHashCodeCustomClassOverridenHashCode() throws Exception {
        Assert.assertEquals(true, execute(HashCode5_customClassOverridenHashCode.class));
    }

    @Test
    public void testClassHashCode() throws Exception {
        Assert.assertEquals(true, execute(HashCode6_javaLangClass_HashCode.class));
    }

    @Test
    public void testClassEquals() throws Exception {
        Assert.assertEquals(true, execute(HashCode6_javaLangClass_HashCode.class));
    }
}
