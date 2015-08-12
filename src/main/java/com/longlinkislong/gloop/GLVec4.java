/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longlinkislong.gloop;

/**
 * A vector with four elements.
 *
 * @author zmichaels
 * @since 15.06.17
 */
public interface GLVec4 {

    /**
     * Attempts to coerce the vector into a GLVecF. It is allowed to return
     * itself in the case that it is already a GLVecF.
     *
     * @return the GLVecF.
     * @since 15.06.17
     */
    public GLVecF asGLVecF();

    /**
     * Attempts to coerce the vector into a GLVecD. It is allowed to return
     * itself in the case that it is already a GLVecD.
     *
     * @return the GLVecD.
     * @since 15.06.17
     */
    public GLVecD asGLVecD();

    /**
     * Attempts to coerce the vector into a GLVec4F. It is allowed to return
     * itself in the case that it is already a GLVec4F.
     *
     * @return the GLVec4F.
     * @since 15.06.17
     */
    default public GLVec4F asGLVec4F() {
        return this.asGLVecF().asGLVec4F();
    }

    /**
     * Attempts to coerce the vector into a GLVec4D. It is allowed to return
     * itself in the case that it is already a GLVec4D.
     *
     * @return the GLVec4D.
     * @since 15.06.17
     */
    default public GLVec4D asGLVec4D() {
        return this.asGLVecD().asGLVec4D();
    }
}
