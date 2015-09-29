/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
