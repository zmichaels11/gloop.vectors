m4_divert(-1)
m4_include(`m4/m4utils/forloop.m4')
m4_include(`m4/m4utils/foreach.m4')
m4_include(`m4/com/longlinkislong/gloop/matrices_defs.m4')
m4_divert(0)m4_dnl 
package com.longlinkislong.gloop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Matrices {    
    private static final Logger LOGGER = LoggerFactory.getLogger(Matrices.class);
    private Matrices() {}   
    public static final MatrixFactory DEFAULT_FACTORY;

    static {        
        final String def = System.getProperty("gloop.matrices.factory", "cyclical");        
        final int cacheSize = Integer.getInteger("gloop.matrices.cache", 16);

        switch(def.toLowerCase()) {
            case "static":                
                DEFAULT_FACTORY = StaticMatrixFactory.getInstance();
                break;            
            case "threadsafe":                
                DEFAULT_FACTORY = new ThreadSafeMatrixFactory(cacheSize);
                break;
            default:
            case "cyclical":                
                DEFAULT_FACTORY = new CyclicalMatrixFactory(cacheSize);
                break;
        }

        LOGGER.debug("Matrices.DEFAULT_FACTORY set to {}", DEFAULT_FACTORY.getClass());
    }
 
    static final int E11 = 0;
    static final int E12 = 1;
    static final int E13 = 2;
    static final int E14 = 3;
    static final int E21 = 4;
    static final int E22 = 5;
    static final int E23 = 6;
    static final int E24 = 7;
    static final int E31 = 8;
    static final int E32 = 9;
    static final int E33 = 10;
    static final int E34 = 11;
    static final int E41 = 12;
    static final int E42 = 13;
    static final int E43 = 14;
    static final int E44 = 15;

    static final float[] NULL_MATRIXF = {
        0f, 0f, 0f, 0f,
        0f, 0f, 0f, 0f,
        0f, 0f, 0f, 0f,
        0f, 0f, 0f, 0f
    };

    static final double[] NULL_MATRIXD = {
        0d, 0d, 0d, 0d,
        0d, 0d, 0d, 0d,
        0d, 0d, 0d, 0d,
        0d, 0d, 0d, 0d
    };

    static final float[] IDENTITY_MATRIXF = {
        1f, 0f, 0f, 0f,
        0f, 1f, 0f, 0f,
        0f, 0f, 1f, 0f,
        0f, 0f, 0f, 1f
    };

    static final double[] IDENTITY_MATRIXD = {
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
