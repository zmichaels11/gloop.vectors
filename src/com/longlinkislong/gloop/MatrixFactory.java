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
public interface MatrixFactory {
    public GLMat2F nextGLMat2F();
    
    public GLMat3F nextGLMat3F();
    
    public GLMat4F nextGLMat4F();
    
    public GLMatNF nextGLMatNF(int size);
    
    public GLMat2D nextGLMat2D();
    
    public GLMat3D nextGLMat3D();
    
    public GLMat4D nextGLMat4D();
    
    public GLMatND nextGLMatND(int size);
}
