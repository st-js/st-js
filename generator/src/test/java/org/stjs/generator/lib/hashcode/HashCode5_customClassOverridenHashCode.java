package org.stjs.generator.lib.hashcode;

import org.stjs.generator.utils.TestUtils;

public class HashCode5_customClassOverridenHashCode {

    private int hashCode;

    public HashCode5_customClassOverridenHashCode(int hashCode) {
        this.hashCode = hashCode;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    public static boolean main(String[] args) {
        Object obj1 = new HashCode5_customClassOverridenHashCode(1234);
        Object obj2 = new HashCode5_customClassOverridenHashCode(5678);

        TestUtils.assertEquals(new HashCode5_customClassOverridenHashCode(1234).hashCode(), new HashCode5_customClassOverridenHashCode(1234).hashCode(), "same");
        TestUtils.assertEquals(false,
                new HashCode5_customClassOverridenHashCode(1234).hashCode() == new HashCode5_customClassOverridenHashCode(5678).hashCode(),
                "different");

        return true;
    }
}
