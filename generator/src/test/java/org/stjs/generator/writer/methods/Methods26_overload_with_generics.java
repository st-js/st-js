package org.stjs.generator.writer.methods;

public class Methods26_overload_with_generics<E> {
    public void overloadMethod(E firstParam) {
    }

    public void overloadMethod(E firstParam, int secondParam) {
        overloadMethod(firstParam);
    }
}
