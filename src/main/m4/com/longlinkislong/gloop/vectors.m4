m4_divert(-1)
m4_include(`m4/m4utils/forloop.m4')
m4_include(`m4/m4utils/foreach.m4')
m4_include(`m4/com/longlinkislong/gloop/vectors_defs.m4')
m4_divert(0)m4_dnl 
package com.longlinkislong.gloop;

public final class Vectors {
    private Vectors() {}

    public static VectorFactory DEFAULT_FACTORY;
    private static final boolean DEBUG;

    static {
        DEBUG = Boolean.getBoolean("debug") && !System.getProperty("debug.exclude", "").contains("vectors");
        final String def = System.getProperty("gloop.vectors.factory", "cyclical");
        final int cacheSize = Integer.getInteger("gloop.vectors.cache", 16);

        switch(def.toLowerCase()) {
            case "static":
                if(DEBUG) {
                    System.out.println("[Vectors]: Using vector factory: StaticVectorFactory");
                }
                DEFAULT_FACTORY = StaticVectorFactory.getInstance();
                break;
            case "atomic":
                if(DEBUG) {
                    System.out.println("[Vectors]: Using vector factory: AtomicVectorFactory");
                }
                DEFAULT_FACTORY = new AtomicVectorFactory(cacheSize);
                break;
            case "threadsafe":
                if(DEBUG) {
                    System.out.println("[Vectors]: Using vector factory: ThreadSafeVectorFactory");
                }
                DEFAULT_FACTORY = new ThreadSafeVectorFactory(cacheSize);
                break;
            case "realtime":
                if(DEBUG) {
                    System.out.println("[Vectors]: Using vector factory: RealTimeVectorFactory");
                }

                try {
                    DEFAULT_FACTORY = new RealTimeVectorFactory(cacheSize);
                } catch(Exception ex) {
                    throw new RuntimeException("Unable to initialize RealTimeVectorFactory!", ex);
                }
                break;
            default:
            case "cyclical":
                if(DEBUG) {
                    System.out.println("[Vectors]: Using vector factory: CyclicalVectorFactory");
                }
                DEFAULT_FACTORY = new CyclicalVectorFactory(cacheSize);
                break;
        }
    }

    protected static final double[] NULL_VECTORD = {0.0, 0.0, 0.0, 0.0};
    protected static final float[] NULL_VECTORF = {0f, 0f, 0f, 0f};

    public static final int X = 0;
    public static final int Y = 1;
    public static final int Z = 2;
    public static final int W = 3;    

foreach(`type', `m4_dnl 
    _cross2(type)
    _cross3(type)
    _cross4(type)
    _crossN(type)
    _plusN(type)
    _minusN(type)
    _scaleN(type)
    _dotN(type)
    _lengthN(type)
    _negateN(type)
    _inverseN(type)
    _multN(type)
    _maddN(type)
    _divN(type)
    _sqrtN(type)
    _minN(type)
    _maxN(type)
forloop(`i', 2, 4, `m4_dnl
    _plus(i, type)
    _minus(i, type)
    _scale(i, type)
    _dot(i, type)
    _length(i, type)
    _negate(i, type)
    _inverse(i, type)
    _mult(i, type)
    _madd(i, type)
    _div(i, type)
    _sqrt(i, type)
    _min(i, type)
    _max(i, type)
')', `float', `double')       
}
