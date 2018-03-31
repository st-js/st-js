package org.stjs.generator.exec.iterable;

import java.util.Iterator;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class Iterable1 implements Iterable<Integer> {

    public static String main(String[] args) {
        Iterable1 iterable1 = new Iterable1();

        String sequence = "";
        for (Integer i : iterable1) {
            sequence = sequence + i + ",";
        }

        $js("console.log(sequence)");
        return sequence;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new FibonnaciSequenceIterator(21);
    }

    private class FibonnaciSequenceIterator implements Iterator<Integer> {

        private int maxValue;
        private int last;
        private int previousToLast;

        public FibonnaciSequenceIterator(int maxValue) {
            this.maxValue = maxValue;
            last = 1;
            previousToLast = 0;
        }

        @Override
        public boolean hasNext() {
            return last < maxValue;
        }

        @Override
        public Integer next() {
            int nextValue = last + previousToLast;
            previousToLast = last;
            last = nextValue;

            return nextValue;
        }

        @Override
        public void remove() {
            throw new RuntimeException("Unsupprted");
        }

    }
}
