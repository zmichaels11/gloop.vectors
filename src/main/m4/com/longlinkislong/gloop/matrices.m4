m4_divert(-1)
m4_include(`m4/m4utils/forloop.m4')
m4_include(`m4/m4utils/foreach.m4')
m4_include(`m4/com/longlinkislong/gloop/matrices_defs.m4')
m4_divert(0)m4_dnl 
package com.longlinkislong.gloop;

public final class Matrices {    
    private Matrices() {}   
    public static MatrixFactory DEFAULT_FACTORY;
    public static final boolean DEBUG;

    static {
        DEBUG = Boolean.getBoolean("debug") && !System.getProperty("debug.exclude", "").contains("matrices");
        final String def = System.getProperty("gloop.matrices.factory", "cyclical");        
        final int cacheSize = Integer.getInteger("gloop.matrices.cache", 16);

        switch(def.toLowerCase()) {
            case "static":
                System.out.println("[Matrices]: Using matrix factory: StaticMatrixFactory");
                DEFAULT_FACTORY = StaticMatrixFactory.getInstance();
                break;            
            case "threadsafe":
                if(DEBUG) {
                    System.out.println("[Matrices]: Using matrix factory: ThreadSafeMatrixFactory");
                }
                DEFAULT_FACTORY = new ThreadSafeMatrixFactory(cacheSize);
                break;
            default:
            case "cyclical":
                System.out.println("[Matrices]: Using matrix factory: CyclicalMatrixFactory");
                DEFAULT_FACTORY = new CyclicalMatrixFactory(cacheSize);
                break;
        }
    }
 
    protected static final int E11 = 0;
    protected static final int E12 = 1;
    protected static final int E13 = 2;
    protected static final int E14 = 3;
    protected static final int E21 = 4;
    protected static final int E22 = 5;
    protected static final int E23 = 6;
    protected static final int E24 = 7;
    protected static final int E31 = 8;
    protected static final int E32 = 9;
    protected static final int E33 = 10;
    protected static final int E34 = 11;
    protected static final int E41 = 12;
    protected static final int E42 = 13;
    protected static final int E43 = 14;
    protected static final int E44 = 15;

    protected static final float[] NULL_MATRIXF = {
        0f, 0f, 0f, 0f,
        0f, 0f, 0f, 0f,
        0f, 0f, 0f, 0f,
        0f, 0f, 0f, 0f
    };

    protected static final double[] NULL_MATRIXD = {
        0d, 0d, 0d, 0d,
        0d, 0d, 0d, 0d,
        0d, 0d, 0d, 0d,
        0d, 0d, 0d, 0d
    };

    protected static final float[] IDENTITY_MATRIXF = {
        1f, 0f, 0f, 0f,
        0f, 1f, 0f, 0f,
        0f, 0f, 1f, 0f,
        0f, 0f, 0f, 1f
    };

    protected static final double[] IDENTITY_MATRIXD = {
        1d, 0d, 0d, 0d,
        0d, 1d, 0d, 0d,
        0d, 0d, 1d, 0d,
        0d, 0d, 0d, 1d
    };

foreach(`type', `m4_dnl 
    _det2(type)
    _det3(type)
    _det4(type)
    _inverse2(type)
    _inverse3(type)
    _inverse4(type)
    _matmultN(type)
    _scaleN(type)
    _transposeN(type)
    _vecmultN(type)
    _perspective(type)
    _ortho(type)
    _lookat(type)
', `float', `double')  

forloop(`i', 2, 4, `m4_dnl 
foreach(`type', `m4_dnl 
    _scale(i, type)
    _matmult(i, type)
    _vecmult(i, type)
    _transpose(i, type)
', `float', `double')')
    
}
