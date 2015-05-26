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
public interface GLVec2 {
    public GLVecF asGLVecF();
    public GLVecD asGLVecD();
    
    default public GLVec2F asGLVec2F() {
        return this.asGLVecF().asGLVec2F();
    }
    
    default public GLVec2D asGLVec2D() {
        return this.asGLVecD().asGLVec2D();
    }
}
