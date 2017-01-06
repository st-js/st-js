package org.stjs.javascript.functions;

public class Functions {
    public static <T1, T2, T3, R> Function3<T1, T2, T3, R> f3(final Function2<T1, T2, R> f2) {
        return new Function3<T1, T2, T3, R>() {

            @Override
            public R $invoke(T1 p1, T2 p2, T3 p3) {
                return f2.$invoke(p1, p2);
            }
        };
    }

    public static <T1, T2, T3, R> Function3<T1, T2, T3, R> f3(final Function1<T1, R> f2) {
        return new Function3<T1, T2, T3, R>() {

            @Override
            public R $invoke(T1 p1, T2 p2, T3 p3) {
                return f2.$invoke(p1);
            }
        };
    }

    public static <T1, T2, T3, T4, R> Function4<T1, T2, T3, T4, R> f4(final Function1<T1, R> f2) {
        return new Function4<T1, T2, T3, T4, R>() {

            @Override
            public R $invoke(T1 p1, T2 p2, T3 p3, T4 p4) {
                return f2.$invoke(p1);
            }
        };
    }
    
    public static <T1, T2, T3, T4, R> Function4<T1, T2, T3, T4, R> f4(final Function2<T1, T2, R> f2) {
        return new Function4<T1, T2, T3, T4, R>() {

            @Override
            public R $invoke(T1 p1, T2 p2, T3 p3, T4 p4) {
                return f2.$invoke(p1, p2);
            }
        };
    }

}
