package org.stjs.generator.writer.trycatch;

import com.sun.tools.internal.ws.processor.generator.GeneratorException;
import org.junit.Assert;
import org.junit.Test;
import org.stjs.generator.GeneratorConfigurationBuilder;
import org.stjs.generator.STJSRuntimeException;
import org.stjs.generator.utils.AbstractStjsTest;

public class TryCatchTest extends AbstractStjsTest {
    @Test
    public void testTryCatchWithCatchFinallyClause() {
        assertCodeContains(TryCatch1_simpleCatchClause.class, "" +
                "        array.push(\"before try\");\n" +
                "        try {\n" +
                "            array.push(\"before throw\");\n" +
                "            if (true) {\n" +
                "                 throw new stjs.Java.RuntimeException()._constructor();\n" +
                "            }\n" +
                "            array.push(\"after throw\");\n" +
                "        }catch (t) {\n" +
                "            array.push(\"inside catch\");\n" +
                "        } finally {\n" +
                "            array.push(\"inside finally\");\n" +
                "        }" +
                "        array.push(\"after try catch\");\n");

        Assert.assertEquals("before try,before throw,inside catch,inside finally,after try catch", execute(TryCatch1_simpleCatchClause.class));
    }

    @Test
    public void testTryCatchWithMultipleCatchClauses() {
        assertCodeContains(TryCatch2_multipleCatchClauses.class, "" +
                "        try {\n" +
                "             throw new TryCatch2_multipleCatchClauses.CustomExceptionA(\"A RuntimeException for test purpose\");\n" +
                "        }catch ($exception) {\n" +
                "            if (stjs.isInstanceOf($exception, TryCatch2_multipleCatchClauses.CustomExceptionA)) {\n" +
                "                var customA = $exception;\n" +
                "                array.push(\"CustomExceptionA: \" + customA.getMessage());\n" +
                "            } else if (stjs.isInstanceOf($exception, TryCatch2_multipleCatchClauses.CustomExceptionB)) {\n" +
                "                var customb = $exception;\n" +
                "                array.push(\"CustomExceptionB: \" + customb.getMessage());\n" +
                "            } else {\n" +
                "                 throw $exception;\n" +
                "            }\n" +
                "        }\n");

        Assert.assertEquals("CustomExceptionA: A RuntimeException for test purpose", execute(TryCatch2_multipleCatchClauses.class));
    }

    @Test
    public void testTryCatchWithMultipleCatchClauses_NotOneOfTheCatchedException() {
        try {
            execute(TryCatch3_multipleCatchClauses_NotExpectedException.class);
            Assert.fail("Should not get here: an exception was expected");
        } catch (STJSRuntimeException e) {
            Assert.assertTrue(e.getMessage().contains("at line number 27"));
        }
    }

    @Test
    public void testTryCatchExceptionUsingExceptionBaseClass() {
        Assert.assertEquals("RuntimeException: A RuntimeException for test purpose... this should be catched!!", execute(TryCatch4_catchExceptionUsingExceptionBaseClass.class));
    }

    @Test
    public void testTryCatchMultipleExceptionTypesOnCatchClause() {
        assertCodeContains(TryCatch5_multipleExceptionTypesOnCatchClause.class, "" +
                "        catch ($exception) {\n" +
                "            if (stjs.isInstanceOf($exception, TryCatch5_multipleExceptionTypesOnCatchClause.CustomExceptionA) || stjs.isInstanceOf($exception, TryCatch5_multipleExceptionTypesOnCatchClause.CustomExceptionB)) {\n" +
                "                var e = $exception;\n" +
                "                array.push(\"CustomExceptionA or CustomExceptionB: \" + e.getMessage());\n" +
                "            } else if (stjs.isInstanceOf($exception, TryCatch5_multipleExceptionTypesOnCatchClause.CustomExceptionC)) {\n" +
                "                var e = $exception;\n" +
                "                array.push(\"CustomExceptionC: \" + e.getMessage());\n" +
                "            } else {\n" +
                "                 throw $exception;\n" +
                "            }\n" +
                "        }");

        Assert.assertEquals("CustomExceptionA or CustomExceptionB: A RuntimeException for test purpose... this should be catched!!", execute(TryCatch5_multipleExceptionTypesOnCatchClause.class));
    }

    @Test
    public void testBlockTryCatchWithResourceCheck() {
        try {
            generate(TryCatch6_tryWithResources.class);
            Assert.fail("Should not get here: an exception was expected");
        } catch (STJSRuntimeException e) {
            Assert.assertTrue(e.getMessage().contains("try-with-resources is not supported"));
        }
    }

}