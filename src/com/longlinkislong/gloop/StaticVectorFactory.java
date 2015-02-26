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
    public GLVec2D nextVec2D() {
        return new StaticVec2D(this);
    }

    @Override
    public GLVec3D nextVec3D() {
        return new StaticVec3D(this);
    }

    @Override
    public GLVec4D nextVec4D() {
        return new StaticVec4D(this);
    }

    @Override
    public GLVecND nextVecND(int size) {
        return new StaticVecND(this, size);
    }

    @Override
    public GLVec2F nextVec2F() {
        return new StaticVec2F(this);
    }

    @Override
    public GLVec3F nextVec3F() {
        return new StaticVec3F(this);
    }

    @Override
    public GLVec4F nextVec4F() {
        return new StaticVec4F(this);
    }

    @Override
    public GLVecNF nextVecNF(int size) {
        return new StaticVecNF(this, size);
    }
    
}
