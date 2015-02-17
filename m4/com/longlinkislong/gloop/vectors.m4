m4_divert(-1)
m4_include(`m4/m4utils/forloop.m4')
m4_include(`m4/m4utils/foreach.m4')
m4_include(`m4/com/longlinkislong/gloop/vectors_defs.m4')
m4_divert(0)m4_dnl 
package com.longlinkislong.gloop;

public final class Vectors {
    private Vectors() {}

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
forloop(`i', 2, 4, `m4_dnl
    _plus(i, type)
    _minus(i, type)
    _scale(i, type)
    _dot(i, type)
    _length(i, type)
    _negate(i, type)
    _inverse(i, type)
')', `float', `double')    
}