/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longlinkislong.gloop;

import com.longlinkislong.gloop.UnsafeTools.Pointer;

/**
 *
 * @author zmichaels
 */
public class RealTimeMatrixFactory implements MatrixFactory {
    private final Pointer<double[]> dataD;
    private final Pointer<float[]> dataF;
    private final Pointer<RealTimeMat2D>[] mat2DCache;
    private final Pointer<RealTimeMat2F>[] mat2FCache;
    private final Pointer<RealTimeMat3D>[] mat3DCache;
    private final Pointer<RealTimeMat3F>[] mat3FCache;
    private final Pointer<RealTimeMat4D>[] mat4DCache;
    private final Pointer<RealTimeMat4F>[] mat4FCache;
    
    private int dataDOffset;
    private int dataFOffset;
    private int mat2DID = 0;
    private int mat3DID = 0;
    private int mat4DID = 0;
    private int mat2FID = 0;
    private int mat3FID = 0;
    private int mat4FID = 0;
    
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        
        this.dataD.free();
        this.dataF.free();
        
        for(Pointer<RealTimeMat2D> mat: this.mat2DCache) {
            mat.instance.free();
            mat.free();
        }
        
        for(Pointer<RealTimeMat2F> mat : this.mat2FCache) {
            mat.instance.free();
            mat.free();
        }
        
        for(Pointer<RealTimeMat3D> mat : this.mat3DCache) {
            mat.instance.free();
            mat.free();
        }
        
        for(Pointer<RealTimeMat3F> mat : this.mat3FCache) {
            mat.instance.free();
            mat.free();
        }
        
        for(Pointer<RealTimeMat4F> mat : this.mat4FCache) {
            mat.instance.free();
            mat.free();
        }
        
        for(Pointer<RealTimeMat4D> mat : this.mat4DCache) {
            mat.instance.free();
            mat.free();
        }
    }
    
    public RealTimeMatrixFactory() throws InstantiationException {
        this(16);
    }
    
    public RealTimeMatrixFactory(final int cacheSize) throws InstantiationException {
        final int cacheBytes = cacheSize * 1000;
        final UnsafeTools unsafe = UnsafeTools.getInstance();
        
        this.dataD = unsafe.dAlloc(cacheSize / Double.BYTES);
        this.dataF = unsafe.fAlloc(cacheSize / Float.BYTES);
        
        this.mat2DCache = new Pointer[cacheBytes / GLMat2D.MATRIX_WIDTH];
        this.mat2FCache = new Pointer[cacheBytes / GLMat2F.MATRIX_WIDTH];
        this.mat3DCache = new Pointer[cacheBytes / GLMat3D.MATRIX_WIDTH];
        this.mat3FCache = new Pointer[cacheBytes / GLMat3F.MATRIX_WIDTH];
        this.mat4DCache = new Pointer[cacheBytes / GLMat4D.MATRIX_WIDTH];
        this.mat4FCache = new Pointer[cacheBytes / GLMat4F.MATRIX_WIDTH];
        
        for(int i = 0; i < this.mat2DCache.length; i++) {
            this.mat2DCache[i] = unsafe.moveOffHeap(new RealTimeMat2D(this));
        }
        
        for(int i = 0; i < this.mat2FCache.length; i++) {
            this.mat2FCache[i] = unsafe.moveOffHeap(new RealTimeMat2F(this));
        }
        
        for(int i = 0; i < this.mat3DCache.length; i++) {
            this.mat3DCache[i] = unsafe.moveOffHeap(new RealTimeMat3D(this));
        }
        
        for(int i = 0; i < this.mat3FCache.length; i++) {
            this.mat3FCache[i] = unsafe.moveOffHeap(new RealTimeMat3F(this));
        }
        
        for(int i = 0; i < this.mat4DCache.length; i++) {
            this.mat4DCache[i] = unsafe.moveOffHeap(new RealTimeMat4D(this));
        }
        
        for(int i = 0; i < this.mat4FCache.length; i++) {
            this.mat4FCache[i] = unsafe.moveOffHeap(new RealTimeMat4F(this));
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

    private int nextMatNFOffset(final int matSize) {
        final int msize = matSize * matSize;
        if(this.dataFOffset + msize < this.dataF.instance.length) {
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

        if (this.dataDOffset + msize < this.dataD.instance.length) {
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
        
        return this.mat2FCache[id].instance;
    }

    @Override
    public GLMat3F nextGLMat3F() {
        final int id = this.nextMat3FID();
        
        return this.mat3FCache[id].instance;
    }

    @Override
    public GLMat4F nextGLMat4F() {
        final int id = this.nextMat4FID();
        
        return this.mat4FCache[id].instance;
    }

    @Override
    public GLMatNF nextGLMatNF(int size) {
        final int offset = this.nextMatNFOffset(size);
        
        return new MappedMatNF(this, this.dataF.instance, offset, size, size);
    }

    @Override
    public GLMat2D nextGLMat2D() {
        final int id = this.nextMat2DID();
        
        return this.mat2DCache[id].instance;
    }

    @Override
    public GLMat3D nextGLMat3D() {
        final int id = this.nextMat3DID();
        
        return this.mat3DCache[id].instance;
    }

    @Override
    public GLMat4D nextGLMat4D() {
        final int id = this.nextMat4DID();
        
        return this.mat4DCache[id].instance;
    }

    @Override
    public GLMatND nextGLMatND(int size) {
        final int offset = this.nextMatNDOffset(size);
        
        return new MappedMatND(this, this.dataD.instance, offset, size, size);
    }
}
