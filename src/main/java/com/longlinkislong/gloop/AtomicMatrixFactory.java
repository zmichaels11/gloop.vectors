/* 
 * Copyright (c) 2015, zmichaels
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

import java.util.concurrent.atomic.AtomicInteger;

/**
 * A MatrixFactory that shares the same data across each matrix. This allows for
 * improved matrix caching and reduced allocation times. However matrices will
 * be automatically recycled when the end of the buffer is reached. Because of
 * this, matrices created by this factory are best used as temporary values.
 *
 * @author zmichaels
 * @since 15.02.26
 */
public class AtomicMatrixFactory implements MatrixFactory {

    private final double[] dataD;
    private final float[] dataF;
    private final MappedMat2D[] mat2DCache;
    private final MappedMat3D[] mat3DCache;
    private final MappedMat4D[] mat4DCache;
    private final MappedMat2F[] mat2FCache;
    private final MappedMat3F[] mat3FCache;
    private final MappedMat4F[] mat4FCache;

    private final AtomicInteger dataDOffset = new AtomicInteger();
    private final AtomicInteger dataFOffset = new AtomicInteger();

    private final AtomicInteger mat2DID = new AtomicInteger();
    private final AtomicInteger mat3DID = new AtomicInteger();
    private final AtomicInteger mat4DID = new AtomicInteger();

    private final AtomicInteger mat2FID = new AtomicInteger();
    private final AtomicInteger mat3FID = new AtomicInteger();
    private final AtomicInteger mat4FID = new AtomicInteger();

    /**
     * Constructs a new CyclicalMatrixFactory with the default cache size.
     *
     * @since 15.02.26
     */
    public AtomicMatrixFactory() {
        this(16);
    }

    /**
     * Constructs a new CyclicalMatrix factory with the specified cache size.
     *
     * @param cacheSize the cache size in kilobytes.
     * @since 15.02.26
     */
    public AtomicMatrixFactory(final int cacheSize) {
        final int cacheBytes = cacheSize * 1000;

        this.dataD = new double[cacheBytes / 8];
        this.dataF = new float[cacheBytes / 4];

        this.mat2DCache = new MappedMat2D[cacheBytes / GLMat2D.MATRIX_WIDTH];
        this.mat2FCache = new MappedMat2F[cacheBytes / GLMat2F.MATRIX_WIDTH];
        this.mat3DCache = new MappedMat3D[cacheBytes / GLMat3D.MATRIX_WIDTH];
        this.mat3FCache = new MappedMat3F[cacheBytes / GLMat3F.MATRIX_WIDTH];
        this.mat4DCache = new MappedMat4D[cacheBytes / GLMat4D.MATRIX_WIDTH];
        this.mat4FCache = new MappedMat4F[cacheBytes / GLMat4F.MATRIX_WIDTH];

        int msize = GLMat2D.MATRIX_SIZE * GLMat2D.MATRIX_SIZE;
        for (int i = 0; i < this.mat2DCache.length; i++) {
            this.mat2DCache[i] = new MappedMat2D(this, this.dataD, 0, this.dataD.length - msize);
        }

        msize = GLMat2F.MATRIX_SIZE * GLMat2F.MATRIX_SIZE;
        for (int i = 0; i < this.mat2FCache.length; i++) {
            this.mat2FCache[i] = new MappedMat2F(this, this.dataF, 0, this.dataF.length - msize);
        }

        msize = GLMat3D.MATRIX_SIZE * GLMat3D.MATRIX_SIZE;
        for (int i = 0; i < this.mat3DCache.length; i++) {
            this.mat3DCache[i] = new MappedMat3D(this, this.dataD, 0, this.dataD.length - msize);
        }

        msize = GLMat3F.MATRIX_SIZE * GLMat3F.MATRIX_SIZE;
        for (int i = 0; i < this.mat3FCache.length; i++) {
            this.mat3FCache[i] = new MappedMat3F(this, this.dataF, 0, this.dataF.length - msize);
        }

        msize = GLMat4D.MATRIX_SIZE * GLMat4D.MATRIX_SIZE;
        for (int i = 0; i < this.mat4DCache.length; i++) {
            this.mat4DCache[i] = new MappedMat4D(this, this.dataD, 0, this.dataD.length - msize);
        }

        msize = GLMat4F.MATRIX_SIZE * GLMat4F.MATRIX_SIZE;
        for (int i = 0; i < this.mat4FCache.length; i++) {
            this.mat4FCache[i] = new MappedMat4F(this, this.dataF, 0, this.dataF.length - msize);
        }
    }

    private int nextMat2DID() {
        return this.mat2DID.getAndUpdate(id -> (id + 1) % this.mat2DCache.length);
    }

    private int nextMat2FID() {
        return this.mat2FID.getAndUpdate(id -> (id + 1) % this.mat2FCache.length);
    }

    private int nextMat3DID() {
        return this.mat3DID.getAndUpdate(id -> (id + 1) % this.mat3DCache.length);
    }

    private int nextMat3FID() {
        return this.mat3FID.getAndUpdate(id -> (id + 1) % this.mat3FCache.length);
    }

    private int nextMat4DID() {
        return this.mat4DID.getAndUpdate(id -> (id + 1) % this.mat4DCache.length);
    }

    private int nextMat4FID() {
        return this.mat4FID.getAndUpdate(id -> (id + 1) % this.mat4FCache.length);
    }

    private int nextMat2DOffset() {
        final int msize = GLMat2D.MATRIX_SIZE * GLMat2D.MATRIX_SIZE;

        return this.dataDOffset.updateAndGet(off -> off + msize < this.dataD.length ? off + msize : 0);
    }

    private int nextMat2FOffset() {
        final int msize = GLMat2F.MATRIX_SIZE * GLMat2F.MATRIX_SIZE;

        return this.dataFOffset.updateAndGet(off -> off + msize < this.dataF.length ? off + msize : 0);
    }

    private int nextMat3DOffset() {
        final int msize = GLMat3D.MATRIX_SIZE * GLMat3D.MATRIX_SIZE;

        return this.dataDOffset.updateAndGet(off -> off + msize < this.dataD.length ? off + msize : 0);
    }

    private int nextMat3FOffset() {
        final int msize = GLMat3F.MATRIX_SIZE * GLMat3F.MATRIX_SIZE;

        return this.dataFOffset.updateAndGet(off -> off + msize < this.dataF.length ? off + msize : 0);
    }

    private int nextMat4DOffset() {
        final int msize = GLMat4D.MATRIX_SIZE * GLMat4D.MATRIX_SIZE;
        
        return this.dataDOffset.updateAndGet(off -> off + msize < this.dataD.length ? off + msize : 0);
    }

    private int nextMat4FOffset() {
        final int msize = GLMat4F.MATRIX_SIZE * GLMat4F.MATRIX_SIZE;
        
        return this.dataFOffset.updateAndGet(off -> off + msize < this.dataF.length ? off + msize : 0);
    }

    private int nextMatNFOffset(final int matSize) {
        final int msize = matSize * matSize;
        
        return this.dataFOffset.updateAndGet(off -> off + msize < this.dataF.length ? off + msize : 0);
    }

    private int nextMatNDOffset(final int matSize) {
        final int msize = matSize * matSize;

        return this.dataDOffset.updateAndGet(off -> off + msize < this.dataD.length ? off + msize : 0);
    }

    @Override
    public GLMat2F nextGLMat2F() {
        final int id = this.nextMat2FID();
        final int offset = this.nextMat2FOffset();

        return this.mat2FCache[id].remap(offset);
    }

    @Override
    public GLMat3F nextGLMat3F() {
        final int id = this.nextMat3FID();
        final int offset = this.nextMat3FOffset();

        return this.mat3FCache[id].remap(offset);
    }

    @Override
    public GLMat4F nextGLMat4F() {
        final int id = this.nextMat4FID();
        final int offset = this.nextMat4FOffset();

        return this.mat4FCache[id].remap(offset);
    }

    @Override
    public GLMatNF nextGLMatNF(int size) {
        final int offset = this.nextMatNFOffset(size);

        return new MappedMatNF(this, this.dataF, offset, size * size, size);
    }

    @Override
    public GLMat2D nextGLMat2D() {
        final int id = this.nextMat2DID();
        final int offset = this.nextMat2DOffset();

        return this.mat2DCache[id].remap(offset);
    }

    @Override
    public GLMat3D nextGLMat3D() {
        final int id = this.nextMat3DID();
        final int offset = this.nextMat3DOffset();

        return this.mat3DCache[id].remap(offset);
    }

    @Override
    public GLMat4D nextGLMat4D() {
        final int id = this.nextMat4DID();
        final int offset = this.nextMat4DOffset();

        return this.mat4DCache[id].remap(offset);
    }

    @Override
    public GLMatND nextGLMatND(int size) {
        final int offset = this.nextMatNDOffset(size);

        return new MappedMatND(this, this.dataD, offset, size * size, size);
    }

    @Override
    public String toString() {
        return String.format("Matrix Factory: [sfp: %d dfp: %d]", this.dataF.length, this.dataD.length);
    }
}
