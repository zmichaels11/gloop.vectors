/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longlinkislong.gloop;

/**
 * A generic 2 dimensional vector.
 *
 * @author zmichaels
 * @since 15.06.17
 */
public interface GLVec2 {

    /**
     * Attempts to convert the vector to a GLVecF. This is allowed to return
     * itself in the case that it is a GLVecF.
     *
     * @return the vector as a GLVecF.
     * @since 15.06.17
     */
    public GLVecF asGLVecF();

    /**
     * Attempts to convert the vector to a GLVecD. This is allowed to return
     * itself in the case that it is a GLVecD.
     *
     * @return the vector as a GLVecD.
     * @since 15.06.07
     */
    public GLVecD asGLVecD();

    /**
     * Converts the vector into a GLVec2F. This is allowed to return itself in
     * the case that it is a GLVec2F.
     *
     * @return the vector as a GLVec2F.
     * @since 15.06.17
     */
    default public GLVec2F asGLVec2F() {
        return this.asGLVecF().asGLVec2F();
    }

    /**
     * Converts the vector into a GLVec2D. This is allowed to return itself in
     * the case that it is a GLVec2D.
     *
     * @return the vector as a GLVec2D.
     * @since 15.06.17
     */
    default public GLVec2D asGLVec2D() {
        return this.asGLVecD().asGLVec2D();
    }
}
