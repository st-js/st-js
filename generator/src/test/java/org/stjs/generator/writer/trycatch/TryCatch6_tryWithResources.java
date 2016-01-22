package org.stjs.generator.writer.trycatch;

import java.io.IOException;

public class TryCatch6_tryWithResources {

    public static class MyCloseable implements org.stjs.generator.writer.trycatch.ducktyping.AutoCloseable, java.lang.AutoCloseable {

        @Override
        public void close() throws IOException {
            // noop
        }

        public void dummy() {
            // noop
        }
    }

    public static void main(String[] args) throws IOException {
        try (
                MyCloseable closeable = new MyCloseable();
        ) {
            closeable.dummy();
        } catch(Throwable t) {

        }
    }


}
