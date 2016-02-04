package org.stjs.generator.lib.javaclass;

import junit.framework.Assert;
import org.junit.Test;
import org.stjs.generator.GeneratorConfigurationBuilder;
import org.stjs.generator.utils.AbstractStjsTest;

public class JavaClassTest extends AbstractStjsTest {

    @Test
    public void testClassGetSimpleName() throws Exception {
        Assert.assertEquals("JavaClassTest1_Class_getSimpleName",
                execute(
                        JavaClassTest1_Class_getSimpleName.class,
                        new GeneratorConfigurationBuilder()
                                .namespaces(JavaClassTest1_Class_getSimpleName.class.getCanonicalName(), "stjs.namespaceforclass")
                                .build()
                )
        );
    }

    @Test
    public void testAnObject_GetClass_GetSimpleName() throws Exception {
        Assert.assertEquals("JavaClassTest2_AnObject_getClass_getSimpleName", execute(JavaClassTest2_AnObject_getClass_getSimpleName.class));
    }

    @Test
    public void testClassGetName() throws Exception {
        Assert.assertEquals("JavaClassTest3a_Class_getName", execute(JavaClassTest3a_Class_getName.class));
    }

    @Test
    public void testClassGetNameWhenNamespaceDefined() throws Exception {
        Assert.assertEquals("stjs.myclass.namespace.JavaClassTest4_Class_getName_withNameSpace",
                execute(
                        JavaClassTest4_Class_getName_withNameSpace.class
                )
        );
    }

    @Test
    public void testClass_getCanonicalName() throws Exception {
        Assert.assertEquals("JavaClassTest3b_Class_getCanonicalName", execute(JavaClassTest3b_Class_getCanonicalName.class));
    }

    @Test
    public void testClass_isAssignableFrom() throws Exception {
        Assert.assertEquals(true, execute(JavaClassTest5_Class_isAssignableFrom.class));
    }
}
