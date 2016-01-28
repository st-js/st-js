package org.stjs.generator.lib.bool;

import org.stjs.generator.utils.TestUtils;

public class Boolean1_valueOfString {

    public static boolean main(String[] args) {
        TestUtils.assertSameInstance(Boolean.TRUE, Boolean.valueOf("true"), "Boolean.valueOf(\"true\")");
        TestUtils.assertSameInstance(Boolean.TRUE, Boolean.valueOf("TRuE"), "Boolean.valueOf(\"TRuE\")");
        TestUtils.assertSameInstance(Boolean.FALSE, Boolean.valueOf("false"), "Boolean.valueOf(\"false\")");
        TestUtils.assertSameInstance(Boolean.FALSE, Boolean.valueOf("patate"), "Boolean.valueOf(\"patate\")");
        TestUtils.assertSameInstance(Boolean.FALSE, Boolean.valueOf(null), "Boolean.valueOf(null)");

        return true;
    }

}
