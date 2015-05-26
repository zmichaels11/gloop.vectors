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
public interface GLMat3 {
    public GLMatF asGLMatF();
    public GLMatD asGLMatD();
    
    default public GLMat3F asGLMat3F() {
        return this.asGLMatF().asGLMat3F();
    }
    
    default public GLMat3D asGLMat3D() {
        return this.asGLMatD().asGLMat3D();
    }
}
