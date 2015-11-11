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
 * An interface that defines how matrices are allocated.
 * @author zmichaels
 * @since 15.02.26
 */
public interface MatrixFactory {
    /**
     * Creates a new 2x2 single precision matrix.
     * @return the matrix
     * @since 15.02.26
     */
    public GLMat2F nextGLMat2F();
    
    /**
     * Creates a new 3x3 single precision matrix.
     * @return the matrix
     * @since 15.02.26
     */
    public GLMat3F nextGLMat3F();
    
    /**
     * Creates a new 4x4 single precision matrix
     * @return the matrix
     * @since 15.02.26
     */
    public GLMat4F nextGLMat4F();
    
    /**
     * Creates a new single precision matrix of the specified size
     * @param size the size
     * 
     * @return the matrix
     * @since 15.02.26
     */
    public GLMatNF nextGLMatNF(int size);
    
    /**
     * Creates a new 2x2 double precision matrix
     * @return the matrix
     * @since 15.02.26
     */
    public GLMat2D nextGLMat2D();
    
    /**
     * Creates a new 3x3 double precision matrix
     * @return the matrix
     * @since 15.02.26
     */
    public GLMat3D nextGLMat3D();
    
    /**
     * Creates a new 4x4 double precision matrix
     * @return the matrix
     * @since 15.02.26
     */
    public GLMat4D nextGLMat4D();
    
    /**
     * Creates a new double precision matrix of the specified size
     * @param size the size
     * @return the matrix
     * @since 15.02.26
     */
    public GLMatND nextGLMatND(int size);
}
