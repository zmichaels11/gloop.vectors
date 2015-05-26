/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longlinkislong.gloop;

/**
 *
 * @author zmichaels 
 */
public interface GLVec3 {
    public GLVecF asGLVecF();
    public GLVecD asGLVecD();
    
    default public GLVec3F asGLVec3F() {
        return this.asGLVecF().asGLVec3F();
    }
    
    default public GLVec3D asGLVec3D() {
        return this.asGLVecD().asGLVec3D();
    }
}
