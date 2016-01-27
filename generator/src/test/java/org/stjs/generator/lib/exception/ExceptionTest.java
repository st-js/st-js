package org.stjs.generator.lib.exception;

import org.junit.Assert;
import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;

public class ExceptionTest extends AbstractStjsTest{

    @Test
    public void testCanCallGetMessageOnExceptions() throws Exception {
        Assert.assertEquals("This is the exception message", execute(Exception1_ExceptionGetMessage.class));
    }

}
