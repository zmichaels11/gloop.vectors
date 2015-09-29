/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longlinkislong.gloop;

/**
 * A vector that contains three elements.
 *
 * @author zmichaels
 * @since 15.06.17
 */
public interface GLVec3 {

    /**
     * Coerces the vector into a GLVecF. If the vector is already a GLVecF, it
     * is allowed to return itself.
     *
     * @return the GLVecF.
     * @since 15.06.17
     */
    public GLVecF asGLVecF();

    /**
     * Coerces the vector into a GLVecD. If the vector is already a GLVecD, it
     * is allowed to return itself.
     *
     * @return the GLVecD.
     * @since 15.06.17
     */
    public GLVecD asGLVecD();

    /**
     * Coerces the vector into a GLVec3F. If the vector is already a GLVec3F, it
     * is allowed to return itself.
     *
     * @return the GLVec3F.
     * @since 15.06.17
     */
    public GLVec3F asGLVec3F();

    /**
     * Coerces the vector into a GLVec3D. If the vector is already a GLVec3D, it
     * is allowed to return itself.
     *
     * @return the GLVec3D.
     * @since 15.06.17
     */
    public GLVec3D asGLVec3D();
}
