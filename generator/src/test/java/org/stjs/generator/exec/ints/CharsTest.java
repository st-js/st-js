package org.stjs.generator.exec.ints;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;

public class CharsTest extends AbstractStjsTest {

    @Test
    public void testCastCharToByte() {
        byte expected = CharToByte.method(CharToByte.BIG_CHAR);
        double expectedDouble = (double) expected;
        assertEquals(expectedDouble, executeAndReturnNumber(CharToByte.class), 0);
    }

    @Test
    public void testConcatStringAndChar() {
        String expected = CharPlusString.method("hello", CharPlusString.CYRILLIC_IA);
        assertEquals(expected, execute(CharPlusString.class));
    }
    
    @Test
    public void testAppendCharToString() {
        String expected = CharAppendToString.method("hello", CharPlusString.CYRILLIC_IA);
        assertEquals(expected, execute(CharAppendToString.class));
    }

    @Test
    public void testCastCharLiteralToByte() {
        byte expected = CharLiteralToByte.method(CharLiteralToByte.CYRILLIC_IA);
        double expectedDouble = (double) expected;
        assertEquals(expectedDouble, executeAndReturnNumber(CharLiteralToByte.class), 0);
    }

    @Test
    public void testCastCharToShort() {
        int expected = CharToShort.method(CharToShort.BIG_CHAR);
        double expectedDouble = (double) expected;
        assertEquals(expectedDouble, executeAndReturnNumber(CharToShort.class), 0);
    }

}
