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
package com.longlinkislong.gloop;

/**
 * A 4x4 matrix of arbitrary precision. Its purpose is to defer processing the
 * type until needed.
 *
 * @author zmichaels
 * @since 15.05.26
 */
public interface GLMat4 {

    /**
     * Converts the matrix to a single precision matrix. This function may
     * return the original instance of the object if it is already a single
     * precision matrix.
     *
     * @return the single precision matrix.
     * @since 15.06.26
     */
    public GLMatF asGLMatF();

    /**
     * Converts the matrix to a double precision matrix. This function may
     * return the original instance of the object if it is already a double
     * precision matrix.
     *
     * @return the double precision matrix.
     * @since 15.06.26
     */
    public GLMatD asGLMatD();

    /**
     * Converts the matrix to a 4x4 single precision matrix. The default
     * implementation of this method first converts the matrix to a single
     * precision matrix and then converts it to a 4x4 matrix. This function may
     * return the original instance of the object if it is already a 4x4 single
     * precision matrix.
     *
     * @return the 4x4 single precision matrix
     * @since 15.06.26
     */
    public GLMat4F asGLMat4F();

    /**
     * Converts the matrix to a 4x4 double precision matrix. The default
     * implementation of this method first converts the matrix to a double
     * precision matrix and then converts it to a 4x4 matrix. This function may
     * return the original instance of the object if it is already a 4x4 double
     * precision matrix.
     *
     * @return the 4x4 double precision matrix
     * @since 15.06.26
     */
    public GLMat4D asGLMat4D();
}
