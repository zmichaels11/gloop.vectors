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
public interface GLMat4 {
    public GLMatF asGLMatF();
    public GLMatD asGLMatD();
    
    default public GLMat4F asGLMat4F() {
        return this.asGLMatF().asGLMat4F();
    }
    
    default public GLMat4D asGLMat4D() {
        return this.asGLMatD().asGLMat4D();
    }
}
