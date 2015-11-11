/* 
 * Copyright (c) 2015, Zachary Michaels
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
m4_divert(-1)
m4_include(`m4/m4utils/forloop.m4')
m4_include(`m4/m4utils/foreach.m4')
m4_include(`m4/com/longlinkislong/gloop/vectors_defs.m4')
m4_divert(0)m4_dnl 
package com.longlinkislong.gloop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Vectors {
    private static final Logger LOGGER = LoggerFactory.getLogger(Vectors.class);
    private Vectors() {}

    public static final VectorFactory DEFAULT_FACTORY;    

    static {        
        final String def = System.getProperty("gloop.vectors.factory", "cyclical");
        final int cacheSize = Integer.getInteger("gloop.vectors.cache", 16);

        switch(def.toLowerCase()) {
            case "static":                                
                DEFAULT_FACTORY = StaticVectorFactory.getInstance();
                break;
            case "threadsafe":                
                DEFAULT_FACTORY = new ThreadSafeVectorFactory(cacheSize);
                break;            
            default:
            case "cyclical":                
                DEFAULT_FACTORY = new CyclicalVectorFactory(cacheSize);
                break;
        }

        LOGGER.debug("Vectors.DEFAULT_FACTORY set to {}", DEFAULT_FACTORY.getClass());
    }

    static final double[] NULL_VECTORD = {0.0, 0.0, 0.0, 0.0};
    static final float[] NULL_VECTORF = {0f, 0f, 0f, 0f};

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
