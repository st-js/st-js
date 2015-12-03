package org.stjs.generator.writer.enums;

public enum Enums11_with_constructor {
    FIRST(1),
    SECOND(2),
    THIRD(3);

    private int privateFieldValue;

    Enums11_with_constructor(int initialValue) {
        this.privateFieldValue = initialValue;
    }
}
