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
public class ThreadLocalVectorFactory implements VectorFactory {

    private final ThreadLocal<VectorFactory> localFactory = new ThreadLocal<VectorFactory>() {
        @Override
        protected VectorFactory initialValue() {
            return new CyclicalVectorFactory();
        }
    };

    @Override
    public GLVec2D nextGLVec2D() {
        return this.localFactory.get().nextGLVec2D();
    }

    @Override
    public GLVec2F nextGLVec2F() {
        return this.localFactory.get().nextGLVec2F();
    }

    @Override
    public GLVec3D nextGLVec3D() {
        return this.localFactory.get().nextGLVec3D();
    }

    @Override
    public GLVec3F nextGLVec3F() {
        return this.localFactory.get().nextGLVec3F();
    }

    @Override
    public GLVec4D nextGLVec4D() {
        return this.localFactory.get().nextGLVec4D();
    }

    @Override
    public GLVec4F nextGLVec4F() {
        return this.localFactory.get().nextGLVec4F();
    }

    @Override
    public GLVecND nextGLVecND(int size) {
        return this.localFactory.get().nextGLVecND(size);
    }

    @Override
    public GLVecNF nextGLVecNF(int size) {
        return this.localFactory.get().nextGLVecNF(size);
    }
}
