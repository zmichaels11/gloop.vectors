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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A MatrixFactory that shares the same data across each matrix. This allows for
 * improved matrix caching and reduced allocation times. However matrices will
 * be automatically recycled when the end of the buffer is reached. Because of
 * this, matrices created by this factory are best used as temporary values.
 *
 * @author zmichaels
 * @since 15.02.26
 */
public class CyclicalMatrixFactory implements MatrixFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(CyclicalMatrixFactory.class);
    private final double[] dataD;
    private final float[] dataF;
    private final MappedMat2D[] mat2DCache;
    private final MappedMat3D[] mat3DCache;
    private final MappedMat4D[] mat4DCache;
    private final MappedMat2F[] mat2FCache;
    private final MappedMat3F[] mat3FCache;
    private final MappedMat4F[] mat4FCache;
    private final MappedMatNF[] matNFCache;
    private final MappedMatND[] matNDCache;

    private int dataDOffset;
    private int dataFOffset;

    private int mat2DID = 0;
    private int mat3DID = 0;
    private int mat4DID = 0;

    private int mat2FID = 0;
    private int mat3FID = 0;
    private int mat4FID = 0;
    
    private int matNDID = 0;
    private int matNFID = 0;

    /**
     * Constructs a new CyclicalMatrixFactory with the default cache size.
     *
     * @since 15.02.26
     */
    public CyclicalMatrixFactory() {
        this(16);
    }

    /**
     * Constructs a new CyclicalMatrix factory with the specified cache size.
     *
     * @param cacheSize the cache size in kilobytes.
     * @since 15.02.26
     */
    public CyclicalMatrixFactory(final int cacheSize) {        
        final ObjectMapper map = ObjectMappers.DEFAULT_INSTANCE;        
        final int cacheBytes = cacheSize * 1000;
        
        LOGGER.debug("Constructing matrix cache; size = {}B", cacheBytes);

        this.dataD = map.map(new double[cacheBytes / 8]);
        this.dataF = map.map(new float[cacheBytes / 4]);

        this.mat2DCache = map.map(new MappedMat2D[cacheBytes / GLMat2D.MATRIX_WIDTH]);
        this.mat2FCache = map.map(new MappedMat2F[cacheBytes / GLMat2F.MATRIX_WIDTH]);
        this.mat3DCache = map.map(new MappedMat3D[cacheBytes / GLMat3D.MATRIX_WIDTH]);
        this.mat3FCache = map.map(new MappedMat3F[cacheBytes / GLMat3F.MATRIX_WIDTH]);
        this.mat4DCache = map.map(new MappedMat4D[cacheBytes / GLMat4D.MATRIX_WIDTH]);
        this.mat4FCache = map.map(new MappedMat4F[cacheBytes / GLMat4F.MATRIX_WIDTH]);
        this.matNDCache = map.map(new MappedMatND[this.mat4DCache.length / 2]);
        this.matNFCache = map.map(new MappedMatNF[this.mat4FCache.length / 2]);

        int msize = GLMat2D.MATRIX_SIZE * GLMat2D.MATRIX_SIZE;
        for (int i = 0; i < this.mat2DCache.length; i++) {
            this.mat2DCache[i] = map.map(new MappedMat2D(this, this.dataD, 0, this.dataD.length - msize));
        }

        msize = GLMat2F.MATRIX_SIZE * GLMat2F.MATRIX_SIZE;
        for (int i = 0; i < this.mat2FCache.length; i++) {
            this.mat2FCache[i] = map.map(new MappedMat2F(this, this.dataF, 0, this.dataF.length - msize));
        }

        msize = GLMat3D.MATRIX_SIZE * GLMat3D.MATRIX_SIZE;
        for (int i = 0; i < this.mat3DCache.length; i++) {
            this.mat3DCache[i] = map.map(new MappedMat3D(this, this.dataD, 0, this.dataD.length - msize));
        }

        msize = GLMat3F.MATRIX_SIZE * GLMat3F.MATRIX_SIZE;
        for (int i = 0; i < this.mat3FCache.length; i++) {
            this.mat3FCache[i] = map.map(new MappedMat3F(this, this.dataF, 0, this.dataF.length - msize));
        }

        msize = GLMat4D.MATRIX_SIZE * GLMat4D.MATRIX_SIZE;
        for (int i = 0; i < this.mat4DCache.length; i++) {
            this.mat4DCache[i] = map.map(new MappedMat4D(this, this.dataD, 0, this.dataD.length - msize));
        }

        msize = GLMat4F.MATRIX_SIZE * GLMat4F.MATRIX_SIZE;
        for (int i = 0; i < this.mat4FCache.length; i++) {
            this.mat4FCache[i] = map.map(new MappedMat4F(this, this.dataF, 0, this.dataF.length - msize));
        }
        
        for (int i = 0; i < this.matNFCache.length; i++) {
            this.matNFCache[i] = map.map(new MappedMatNF(this, this.dataF, 0, this.dataF.length - 1, 1));
        }
        
        for(int i = 0; i < this.matNDCache.length; i++) {
            this.matNDCache[i] = map.map(new MappedMatND(this, this.dataD, 0, this.dataD.length - 1, 1));
        }
    }

    private int nextMat2DID() {
        final int testID = this.mat2DID + 1;

        return this.mat2DID = testID % this.mat2DCache.length;
    }

    private int nextMat2FID() {
        final int testID = this.mat2FID + 1;

        return this.mat2FID = testID % this.mat2FCache.length;
    }

    private int nextMat3DID() {
        final int testID = this.mat3DID + 1;

        return this.mat3DID = testID % this.mat3DCache.length;
    }

    private int nextMat3FID() {
        final int testID = this.mat3FID + 1;

        return this.mat3FID = testID % this.mat3FCache.length;
    }

    private int nextMat4DID() {
        final int testID = this.mat4DID + 1;

        return this.mat4DID = testID % this.mat4DCache.length;
    }

    private int nextMat4FID() {
        final int testID = this.mat4FID + 1;

        return this.mat4FID = testID % this.mat4FCache.length;
    }
    
    private int nextMatNDID() {
        final int testID = this.matNDID + 1;
        
        return this.matNDID = testID % this.matNDCache.length;
    }
    
    private int nextMatNFID() {
        final int testID = this.matNFID + 1;
        
        return this.matNFID = testID % this.matNFCache.length;
    }

    private int nextMat2DOffset() {
        final int msize = GLMat2D.MATRIX_SIZE * GLMat2D.MATRIX_SIZE;

        if (this.dataDOffset + msize < this.dataD.length) {
            final int off = this.dataDOffset;

            this.dataDOffset += msize;
            return off;
        } else {
            this.dataDOffset = msize;
            return 0;
        }
    }

    private int nextMat2FOffset() {
        final int msize = GLMat2F.MATRIX_SIZE * GLMat2F.MATRIX_SIZE;

        if (this.dataFOffset + msize < this.dataF.length) {
            final int off = this.dataFOffset;

            this.dataFOffset += msize;
            return off;
        } else {
            this.dataFOffset = msize;
            return 0;
        }
    }

    private int nextMat3DOffset() {
        final int msize = GLMat3D.MATRIX_SIZE * GLMat3D.MATRIX_SIZE;
        if (this.dataDOffset + msize < this.dataD.length) {
            final int off = this.dataDOffset;

            this.dataDOffset += msize;
            return off;
        } else {
            this.dataDOffset = msize;
            return 0;
        }
    }

    private int nextMat3FOffset() {
        final int msize = GLMat3F.MATRIX_SIZE * GLMat3F.MATRIX_SIZE;
        if (this.dataFOffset + msize < this.dataF.length) {
            final int off = this.dataFOffset;

            this.dataFOffset += msize;
            return off;
        } else {
            this.dataFOffset = msize;
            return 0;
        }
    }

    private int nextMat4DOffset() {
        final int msize = GLMat4D.MATRIX_SIZE * GLMat4D.MATRIX_SIZE;
        if (this.dataDOffset + msize < this.dataD.length) {
            final int off = this.dataDOffset;

            this.dataDOffset += msize;
            return off;
        } else {
            this.dataDOffset = msize;
            return 0;
        }
    }

    private int nextMat4FOffset() {
        final int msize = GLMat4F.MATRIX_SIZE * GLMat4F.MATRIX_SIZE;
        if(this.dataFOffset + msize < this.dataF.length) {
            final int off = this.dataFOffset;
            
            this.dataFOffset += msize;
            return off;
        } else {
            this.dataFOffset = msize;
            return 0;
        }
    }

    private int nextMatNFOffset(final int matSize) {
        final int msize = matSize * matSize;
        if(this.dataFOffset + msize < this.dataF.length) {
            final int off = this.dataFOffset;
            
            this.dataFOffset += msize;
            return off;
        } else {
            this.dataFOffset = msize;
            return 0;
        }
    }

    private int nextMatNDOffset(final int matSize) {
        final int msize = matSize * matSize;

        if (this.dataDOffset + msize < this.dataD.length) {
            final int off = this.dataDOffset;

            this.dataDOffset += msize;
            return off;
        } else {
            this.dataDOffset = msize;
            return 0;
        }
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
        final int id = this.nextMatNFID();
        final int offset = this.nextMatNFOffset(size);        
        final MappedMatNF out = this.matNFCache[id];
        
        out.remap(offset).resize(size);

        return out;
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
        final int id = this.nextMatNDID();
        final int offset = this.nextMatNDOffset(size);
        final MappedMatND out = this.matNDCache[id];
        
        out.remap(offset).resize(size);
        
        return out;
    }

    @Override
    public String toString() {
        return String.format("Matrix Factory: [sfp: %d dfp: %d]", this.dataF.length, this.dataD.length);
    }
}
