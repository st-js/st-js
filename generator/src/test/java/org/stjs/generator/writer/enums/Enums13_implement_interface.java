package org.stjs.generator.writer.enums;

public enum Enums13_implement_interface implements KeyType {
    NONE("none"),
    FIRST("first"),
    SECOND("second"),
    THIRD("third");

    private final String keyStr;

    Enums13_implement_interface(String keyStr) {
        this.keyStr = keyStr;
    }

    @Override
    public String getKey() {
        return keyStr;
    }
}
