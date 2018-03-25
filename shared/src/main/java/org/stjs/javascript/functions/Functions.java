package org.stjs.javascript.functions;

/**
 * <p>Functions class.</p>
 *
 * @author acraciun
 * @version $Id: $Id
 */
public class Functions {
    /**
     * <p>f3.</p>
     *
     * @param f2 a {@link org.stjs.javascript.functions.Function2} object.
     * @param <T1> a T1 object.
     * @param <T2> a T2 object.
     * @param <T3> a T3 object.
     * @param <R> a R object.
     * @return a {@link org.stjs.javascript.functions.Function3} object.
     */
    public static <T1, T2, T3, R> Function3<T1, T2, T3, R> f3(final Function2<T1, T2, R> f2) {
        return new Function3<T1, T2, T3, R>() {

            @Override
            public R $invoke(T1 p1, T2 p2, T3 p3) {
                return f2.$invoke(p1, p2);
            }
        };
    }

    /**
     * <p>f3.</p>
     *
     * @param f2 a {@link org.stjs.javascript.functions.Function1} object.
     * @param <T1> a T1 object.
     * @param <T2> a T2 object.
     * @param <T3> a T3 object.
     * @param <R> a R object.
     * @return a {@link org.stjs.javascript.functions.Function3} object.
     */
    public static <T1, T2, T3, R> Function3<T1, T2, T3, R> f3(final Function1<T1, R> f2) {
        return new Function3<T1, T2, T3, R>() {

            @Override
            public R $invoke(T1 p1, T2 p2, T3 p3) {
                return f2.$invoke(p1);
            }
        };
    }

    /**
     * <p>f4.</p>
     *
     * @param f2 a {@link org.stjs.javascript.functions.Function1} object.
     * @param <T1> a T1 object.
     * @param <T2> a T2 object.
     * @param <T3> a T3 object.
     * @param <T4> a T4 object.
     * @param <R> a R object.
     * @return a {@link org.stjs.javascript.functions.Function4} object.
     */
    public static <T1, T2, T3, T4, R> Function4<T1, T2, T3, T4, R> f4(final Function1<T1, R> f2) {
        return new Function4<T1, T2, T3, T4, R>() {

            @Override
            public R $invoke(T1 p1, T2 p2, T3 p3, T4 p4) {
                return f2.$invoke(p1);
            }
        };
    }
    
    /**
     * <p>f4.</p>
     *
     * @param f2 a {@link org.stjs.javascript.functions.Function2} object.
     * @param <T1> a T1 object.
     * @param <T2> a T2 object.
     * @param <T3> a T3 object.
     * @param <T4> a T4 object.
     * @param <R> a R object.
     * @return a {@link org.stjs.javascript.functions.Function4} object.
     */
    public static <T1, T2, T3, T4, R> Function4<T1, T2, T3, T4, R> f4(final Function2<T1, T2, R> f2) {
        return new Function4<T1, T2, T3, T4, R>() {

            @Override
            public R $invoke(T1 p1, T2 p2, T3 p3, T4 p4) {
                return f2.$invoke(p1, p2);
            }
        };
    }

}
