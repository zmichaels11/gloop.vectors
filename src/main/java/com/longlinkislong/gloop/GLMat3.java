/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longlinkislong.gloop;

/**
 * A 3x3 matrix of arbitrary precision. Its purpose is to defer processing the
 * type until needed.
 *
 * @author zmichaels
 * @since 15.05.26
 */
public interface GLMat3 {

    /**
     * Converts the matrix to a single precision matrix. If the matrix is
     * already an instance of GLMatF, this function may return itself.
     *
     * @return a 3x3 single precision matrix.
     * @since 15.05.26
     */
    public GLMatF asGLMatF();

    /**
     * Converts the matrix to a double precision matrix. If the matrix is
     * already an instance of GLMatD, this function may return itself.
     *
     * @return a 3x3 double precision matrix.
     * @since 15.06.26
     */
    public GLMatD asGLMatD();

    /**
     * Converts the matrix to a 3x3 single precision matrix. The default
     * behavior of this function is to convert the matrix first to a single
     * precision matrix and then convert it to a 3x3 matrix. This function is
     * allowed to return itself.
     *
     * @return the 3x3 single precision matrix.
     * @since 15.06.26
     */
    public GLMat3F asGLMat3F();

    /**
     * Converts the matrix to a 3x3 double precision matrix. The default
     * behavior of this function is to convert the matrix first to a double
     * precision matrix and then convert it to a 3x3 matrix. This function is
     * allowed to return itself.
     *
     * @return the 3x3 double precision matrix.
     * @since 15.06.26
     */
    public GLMat3D asGLMat3D();
}
