package org.stjs.generator.writer.methods;

import org.stjs.javascript.annotation.JSOverloadName;

public class Methods18_overload {
    @JSOverloadName
    private void overloadMethod() {
    }

    @JSOverloadName("overloadMethodWithString")
    public void overloadMethod(String firstParam) {
        overloadMethod();
    }

    @JSOverloadName
    public void overloadMethod(String firstParam, int secondParam) {
        overloadMethod(firstParam);
    }

    @JSOverloadName
    public void overloadMethod(String firstParam, int secondParam, CustomClass thirdParam) {
        overloadMethod(firstParam, secondParam);
    }

    private class CustomClass {

    }
}
