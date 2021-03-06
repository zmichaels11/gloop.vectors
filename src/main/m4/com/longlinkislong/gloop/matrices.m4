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
        final String def = System.getProperty("gloop.matrices.factory", "smart");        
        final int cacheSize = Integer.getInteger("gloop.matrices.cache", 16);

        switch(def.toLowerCase()) {
            case "static":                
                DEFAULT_FACTORY = StaticMatrixFactory.getInstance();
                break;            
            case "threadsafe":                
                DEFAULT_FACTORY = new ThreadSafeMatrixFactory(cacheSize);
                break;            
            case "cyclical":                
                DEFAULT_FACTORY = new CyclicalMatrixFactory(cacheSize);
                break;
            default:
            case "smart":
                DEFAULT_FACTORY = SmartFactory.getInstance();
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
    _makeIdentityN(type)
    _makeTranslationN(type)
    _makeRotation2(type)
    _makeRotationX2(type)
    _makeRotationX3(type)
    _makeRotationX4(type)
    _makeRotationY3(type)
    _makeRotationY4(type)
    _makeRotationZ3(type)
    _makeRotationZ4(type)
', `float', `double')  

forloop(`i', 2, 4, `m4_dnl 
foreach(`type', `m4_dnl 
    _scale(i, type)
    _matmult(i, type)
    _vecmult(i, type)
    _transpose(i, type)
    _makeIdentity(i, type)
    _makeTranslation(i, type)
    _makeScale(i, type)
', `float', `double')')
    
}
