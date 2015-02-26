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
public class CyclicalMatrixFactory implements MatrixFactory {

    private final double[] dataD;
    private final float[] dataF;
    private final MappedMat2D[] mat2DCache;
    private final MappedMat3D[] mat3DCache;
    private final MappedMat4D[] mat4DCache;
    private final MappedMat2F[] mat2FCache;
    private final MappedMat3F[] mat3FCache;
    private final MappedMat4F[] mat4FCache;

    private int dataDOffset;
    private int dataFOffset;

    private int mat2DID = 0;
    private int mat3DID = 0;
    private int mat4DID = 0;

    private int mat2FID = 0;
    private int mat3FID = 0;
    private int mat4FID = 0;

    public CyclicalMatrixFactory() {
        this(16);
    }

    public CyclicalMatrixFactory(final int cacheSize) {
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
    
    private int nextMat2DOffset() {
        final int msize = GLMat2D.MATRIX_SIZE * GLMat2D.MATRIX_SIZE;
        final int testOffset = this.dataDOffset + msize;
        final int dataEnd = this.dataD.length - msize;
        
        return this.dataDOffset = testOffset % dataEnd;
    }
    
    private int nextMat2FOffset() {
        final int msize = GLMat2F.MATRIX_SIZE * GLMat2F.MATRIX_SIZE;
        final int testOffset = this.dataFOffset + msize;
        final int dataEnd = this.dataF.length - msize;
        
        return this.dataFOffset = testOffset % dataEnd;
    }
    
    private int nextMat3DOffset() {
        final int msize = GLMat3D.MATRIX_SIZE * GLMat3D.MATRIX_SIZE;
        final int testOffset = this.dataDOffset + msize;
        final int dataEnd = this.dataD.length - msize;
        
        return this.dataDOffset = testOffset % dataEnd;
    }
    
    private int nextMat3FOffset() {
        final int msize = GLMat3F.MATRIX_SIZE * GLMat3F.MATRIX_SIZE;
        final int testOffset = this.dataFOffset + msize;
        final int dataEnd = this.dataF.length - msize;
        
        return this.dataFOffset = testOffset % dataEnd;
    }
    
    private int nextMat4DOffset() {
        final int msize = GLMat4D.MATRIX_SIZE * GLMat4D.MATRIX_SIZE;
        final int testOffset = this.dataDOffset + msize;
        final int dataEnd = this.dataD.length - msize;
        
        return this.dataDOffset = testOffset % dataEnd;
    }
    
    private int nextMat4FOffset() {
        final int msize = GLMat4F.MATRIX_SIZE * GLMat4F.MATRIX_SIZE;
        final int testOffset = this.dataFOffset + msize;
        final int dataEnd = this.dataF.length - msize;
        
        return this.dataFOffset = testOffset % dataEnd;
    }
    
    private int nextMatNFOffset(final int matSize) {
        final int testOffset = this.dataFOffset + (matSize * matSize);
        
        return this.dataFOffset = testOffset < this.dataF.length
                ? testOffset
                : 0;
    }
    
    private int nextMatNDOffset(final int matSize) {
        final int testOffset = this.dataDOffset + (matSize * matSize);
        
        return this.dataFOffset = testOffset < this.dataF.length
                ? testOffset
                : 0;
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
        
        return new MappedMatNF(this, this.dataF, offset, size, size*size);
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
        
        return new MappedMatND(this, this.dataD, offset, size, size*size);
    }
    
    @Override
    public String toString() {
        return String.format("Matrix Factory: [sfp: %d dfp: %d]", this.dataF.length, this.dataD.length);
    }
}
