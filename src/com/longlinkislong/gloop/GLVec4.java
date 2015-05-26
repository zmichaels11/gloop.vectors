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
public interface GLVec4 {
    public GLVecF asGLVecF();
    public GLVecD asGLVecD();
    
    default public GLVec4F asGLVec4F() {
        return this.asGLVecF().asGLVec4F();
    }
    
    default public GLVec4D asGLVec4D() {
        return this.asGLVecD().asGLVec4D();
    }
}
