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
    public GLVec2D nextVec2D();
    
    public GLVec3D nextVec3D();
    
    public GLVec4D nextVec4D();
    
    public GLVecND nextVecND(int size);
    
    public GLVec2F nextVec2F();
    
    public GLVec3F nextVec3F();
    
    public GLVec4F nextVec4F();
    
    public GLVecNF nextVecNF(int size);
}
