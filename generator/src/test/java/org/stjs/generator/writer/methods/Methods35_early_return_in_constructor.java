package org.stjs.generator.writer.methods;

import org.stjs.javascript.Array;
import org.stjs.javascript.JSCollections;

public class Methods35_early_return_in_constructor {

    public Array trace = JSCollections.$array();

    public Methods35_early_return_in_constructor(boolean b) {
        trace.push("before if");
        if (b) {
            trace.push("inside if and before early return");
            return;
        }
        trace.push("last line of constructor");
    }

    public static String main(String[] args) {
        return "" +
                new Methods35_early_return_in_constructor(false).trace.join(", ") + " ### " +
                new Methods35_early_return_in_constructor(true).trace.join(", ");
    }

}
