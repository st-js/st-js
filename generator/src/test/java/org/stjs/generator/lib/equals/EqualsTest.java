package org.stjs.generator.lib.equals;

import junit.framework.Assert;
import org.junit.Test;
import org.stjs.generator.GeneratorConfigurationBuilder;
import org.stjs.generator.utils.AbstractStjsTest;

public class EqualsTest extends AbstractStjsTest {

    @Test
    public void testClassEquals() throws Exception {
        Assert.assertEquals(true, execute(Equals1_javaLangClass_Equals.class));
    }

    @Test
    public void testOverrideEquals() throws Exception {
        Assert.assertEquals(true, execute(Equals2_InterfaceDefineEquals.class,
                new GeneratorConfigurationBuilder().renamedMethodSignature(
                        "org.stjs.generator.lib.equals.Equals2_InterfaceDefineEquals.MyInterface.equals$Object", "$java_equals")
                        .build())
        );
    }

}
