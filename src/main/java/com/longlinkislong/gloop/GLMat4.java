/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
