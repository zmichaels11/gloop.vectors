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
public interface VectorFactory {
    public GLVec2D nextGLVec2D();
    
    public GLVec3D nextGLVec3D();
    
    public GLVec4D nextGLVec4D();
    
    public GLVecND nextGLVecND(int size);
    
    public GLVec2F nextGLVec2F();
    
    public GLVec3F nextGLVec3F();
    
    public GLVec4F nextGLVec4F();
    
    public GLVecNF nextGLVecNF(int size);
}
