package org.stjs.generator.writer.overload;

import org.stjs.javascript.Array;

public class Overload7_multiple_constructors {
    private String param1 = "test";
    private Array<String> param2 = new Array<>();

    public Overload7_multiple_constructors() {
        this("ok");
        param2.push("1");
    }

    public Overload7_multiple_constructors(String param1) {
        this(param1, new Array<String>());
        param2.push("2");
    }

    public Overload7_multiple_constructors(String param1, Array<String> param2) {
        this.param1 = param1;
        this.param2 = param2;
        param2.push("3");
    }

    public Array<String> getArray() {
        return param2;
    }

    public class Caller {
        public Caller() {
            Overload7_multiple_constructors clazz = new Overload7_multiple_constructors("2");
        }
    }
}
