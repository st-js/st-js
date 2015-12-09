package org.stjs.generator.writer.methods;

public class Methods20_overload_with_varargs {
    public void overloadMethod(String firstParam, int... secondParams) {
    }

    public void overloadMethod(String firstParam, String... secondParams) {
    }

    public void overloadMethod(String firstParam, boolean[] secondParams) {
    }

    public void overloadMethod(String firstParam, ArrayString secondParams) {
    }

    private class ArrayString {
    }
}
