package org.stjs.generator.lib.hashcode;

import org.stjs.generator.utils.TestUtils;

public class HashCode3_boolean {

    public static boolean main(String[] args) {
        validateHashCode(Boolean.TRUE, Boolean.FALSE, "Boolean");
        validateHashCode(Boolean.valueOf(true), Boolean.valueOf(false), "Boolean");
        return true;
    }

    private static void validateHashCode(Object obj1, Object obj2, String message) {
        TestUtils.assertEquals(true, obj1.hashCode() == obj1.hashCode(), message + ": obj1.hashCode() == obj1.hashCode()");
        TestUtils.assertEquals(true, obj2.hashCode() == obj2.hashCode(), message + ": obj2.hashCode() == obj2.hashCode()");
        TestUtils.assertEquals(false, obj1.hashCode() == obj2.hashCode(), message + ": obj1.hashCode() != obj2.hashCode()");
    }

}
