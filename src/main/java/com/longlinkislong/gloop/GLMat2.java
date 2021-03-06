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
 * A 2x2 matrix of arbitrary precision. Its purpose is to defer processing the
 * type until needed.
 *
 * @author zmichaels
 * @since 15.05.26
 */
public interface GLMat2 {    
    /**
     * Converts the matrix to a single precision matrix. If the matrix is
     * already an instance of GLMatF, this function may return itself.
     *
     * @return a 2x2 single precision matrix.
     * @since 15.05.26
     */
    public GLMatF asGLMatF();

    /**
     * Converts the matrix to a double precision matrix. If the matrix is
     * already an instance of GLMatD, this function may return itself.
     *
     * @return a 2x2 double precision matrix.
     * @since 15.05.26
     */
    public GLMatD asGLMatD();

    /**
     * Converts the matrix to a 2x2 single precision matrix. The default
     * behavior of this matrix is to convert the matrix to single precision and
     * then cast as a GLMat2F.
     *
     * @return the matrix as a GLMat2F.
     * @since 15.05.26
     */
    public GLMat2F asGLMat2F();

    /**
     * Converts the matrix to a 2x2 double precision matrix. The default
     * behavior of this matrix is to convert the matrix to double precision and
     * then cast as a GLMat2D.
     *
     * @return the matrix as a GLMatD.
     * @since 15.05.26
     */
    public GLMat2D asGLMat2D();
}
