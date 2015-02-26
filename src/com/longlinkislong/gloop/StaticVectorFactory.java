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
public final class StaticVectorFactory implements VectorFactory {

    @Override
    public GLVec2D nextGLVec2D() {
        return new StaticVec2D(this);
    }

    @Override
    public GLVec3D nextGLVec3D() {
        return new StaticVec3D(this);
    }

    @Override
    public GLVec4D nextGLVec4D() {
        return new StaticVec4D(this);
    }

    @Override
    public GLVecND nextGLVecND(int size) {
        return new StaticVecND(this, size);
    }

    @Override
    public GLVec2F nextGLVec2F() {
        return new StaticVec2F(this);
    }

    @Override
    public GLVec3F nextGLVec3F() {
        return new StaticVec3F(this);
    }

    @Override
    public GLVec4F nextGLVec4F() {
        return new StaticVec4F(this);
    }

    @Override
    public GLVecNF nextGLVecNF(int size) {
        return new StaticVecNF(this, size);
    }
    
}
