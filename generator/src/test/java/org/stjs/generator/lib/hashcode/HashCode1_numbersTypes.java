package org.stjs.generator.lib.hashcode;

import org.stjs.generator.utils.TestUtils;

public class HashCode1_numbersTypes {

    public static boolean main(String[] args) {
        validateHashCode(Integer.valueOf(1), Integer.valueOf(2), "Integer");
        validateHashCode(Long.valueOf(1), Long.valueOf(2), "Long");
        validateHashCode(Short.valueOf("1"), Short.valueOf("2"), "Short");
        return true;
    }

    private static void validateHashCode(Object obj1, Object obj2, String message) {
        TestUtils.assertEquals(true, obj1.hashCode() == obj1.hashCode(), message + ": obj1.hashCode() == obj1.hashCode()");
        TestUtils.assertEquals(true, obj2.hashCode() == obj2.hashCode(), message + ": obj2.hashCode() == obj2.hashCode()");
        TestUtils.assertEquals(false, obj1.hashCode() == obj2.hashCode(), message + ": obj1.hashCode() != obj2.hashCode()");
    }

}
