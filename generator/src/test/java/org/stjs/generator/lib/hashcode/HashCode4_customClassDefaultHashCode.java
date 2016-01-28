package org.stjs.generator.lib.hashcode;

import org.stjs.generator.utils.TestUtils;

public class HashCode4_customClassDefaultHashCode {

    public static boolean main(String[] args) {
        Object obj1 = new HashCode4_customClassDefaultHashCode();
        Object obj2 = new HashCode4_customClassDefaultHashCode();

        TestUtils.assertEquals(obj1.hashCode(), obj1.hashCode(), "obj1.hashCode() === obj1.hashCode()");
        TestUtils.assertEquals(obj2.hashCode(), obj2.hashCode(), "obj2.hashCode() === obj2.hashCode()");
        TestUtils.assertEquals(false, obj1.hashCode() == obj2.hashCode(), obj1.hashCode() + " !== " + obj2.hashCode());
        return true;
    }

}
