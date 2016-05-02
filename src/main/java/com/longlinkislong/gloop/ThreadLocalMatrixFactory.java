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
public class ThreadLocalMatrixFactory implements MatrixFactory {
    private final ThreadLocal<MatrixFactory> localFactory = new ThreadLocal<MatrixFactory> () {
        @Override
        protected MatrixFactory initialValue() {
            return new CyclicalMatrixFactory();
        }
    };

    @Override
    public GLMat2D nextGLMat2D() {
        return localFactory.get().nextGLMat2D();
    }

    @Override
    public GLMat2F nextGLMat2F() {
        return localFactory.get().nextGLMat2F();
    }

    @Override
    public GLMat3D nextGLMat3D() {
        return localFactory.get().nextGLMat3D();
    }

    @Override
    public GLMat3F nextGLMat3F() {
        return localFactory.get().nextGLMat3F();
    }

    @Override
    public GLMat4D nextGLMat4D() {
        return localFactory.get().nextGLMat4D();
    }

    @Override
    public GLMat4F nextGLMat4F() {
        return localFactory.get().nextGLMat4F();
    }

    @Override
    public GLMatND nextGLMatND(int size) {
        return localFactory.get().nextGLMatND(size);
    }

    @Override
    public GLMatNF nextGLMatNF(int size) {
        return localFactory.get().nextGLMatNF(size);
    }
}
