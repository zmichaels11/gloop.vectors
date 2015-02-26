m4_divert(-1)
m4_include(`m4/m4utils/forloop.m4')
m4_include(`m4/m4utils/foreach.m4')
m4_include(`m4/com/longlinkislong/gloop/matrices_defs.m4')
m4_divert(0)m4_dnl 
package com.longlinkislong.gloop;

public final class Matrices {    
    private Matrices() {}    

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

foreach(`type', `m4_dnl 
    _det2(type)
    _det3(type)
    _det4(type)
    _inverse2(type)
    _inverse3(type)
    _inverse4(type)
', `float', `double')  

forloop(`i', 2, 4, `m4_dnl 
foreach(`type', `m4_dnl 
    _scale(i, type)
    _matmult(i, type)
    _vecmult(i, type)
    _transpose(i, type)
', `float', `double')')

}