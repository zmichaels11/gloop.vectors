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
public class StaticMatrixFactory implements MatrixFactory {

    private StaticMatrixFactory() {

    }

    public static StaticMatrixFactory getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final StaticMatrixFactory INSTANCE = new StaticMatrixFactory();
    }

    @Override
    public GLMat2F nextGLMat2F() {
        return new StaticMat2F(this);
    }

    @Override
    public GLMat3F nextGLMat3F() {
        return new StaticMat3F(this);
    }

    @Override
    public GLMat4F nextGLMat4F() {
        return new StaticMat4F(this);
    }

    @Override
    public GLMatNF nextGLMatNF(int size) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public GLMat2D nextGLMat2D() {
        return new StaticMat2D(this);
    }

    @Override
    public GLMat3D nextGLMat3D() {
        return new StaticMat3D(this);
    }

    @Override
    public GLMat4D nextGLMat4D() {
        return new StaticMat4D(this);
    }

    @Override
    public GLMatND nextGLMatND(int size) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
