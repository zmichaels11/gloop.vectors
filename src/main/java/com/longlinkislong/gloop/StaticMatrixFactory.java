/* 
 * Copyright (c) 2015, Zachary Michaels
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
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
        return new StaticMatNF(this, size);
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
        return new StaticMatND(this, size);
    }

}
