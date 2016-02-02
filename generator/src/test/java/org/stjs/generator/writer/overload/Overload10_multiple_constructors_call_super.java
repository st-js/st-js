package org.stjs.generator.writer.overload;

import org.stjs.javascript.Array;
import org.stjs.javascript.JSCollections;

import java.util.ArrayList;
import java.util.List;

import static org.stjs.javascript.JSCollections.$array;

public class Overload10_multiple_constructors_call_super {

    public static String main(String[] args) {
        Array<String> completeTrace = JSCollections.$array();

        completeTrace.push(new BaseClass().trace.join(" "));
        completeTrace.push(new BaseClass(111).trace.join(" "));

        completeTrace.push(new SubClassA("AAA").trace.join(" "));
        completeTrace.push(new SubClassA(222).trace.join(" "));

        completeTrace.push(new SubClassB("BBB").trace.join(" "));

        return completeTrace.join (" ### ");
    }

    public static class BaseClass {
        protected Array<String> trace = JSCollections.$array();

        public BaseClass() {
            trace.push("BaseClass()");
        }

        public BaseClass(int i) {
            trace.push("BaseClass(int " + i + ")");
        }

        public String traceToString() {
            return null;
        }
    }

    // Class with multiple constructors extending class with multiple constructors
    public static class SubClassA extends BaseClass {
        public SubClassA(String s) {
            super(999);
            trace.push("SubClassA(String " + s + ")");
        }
        public SubClassA(int i) {
            super(i);
            trace.push("SubClassA(int " + i + ")");
        }
    }

    // Class with only one constructor extending class with multiple constructors
    public static class SubClassB extends BaseClass {
        public SubClassB(String s) {
            // call super default constructor
            trace.push("SubClassB(String " + s + ")");
        }
    }

}
