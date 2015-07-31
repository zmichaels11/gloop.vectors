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

import com.longlinkislong.gloop.UnsafeTools.Pointer;

/**
 * A VectorFactory that shares the same data across each vector. This allows for
 * improved vector caching and reduced allocation times. However vectors will be
 * automatically recycled when the end of the buffer is reached. Because of
 * this, vectors created by this factory are best used as temporary values.
 *
 * @author zmichaels
 * @since 15.02.26
 */
public class RealTimeVectorFactory implements VectorFactory {

    private final Pointer<double[]> dataD;
    private final Pointer<float[]> dataF;
    private final Pointer<RealTimeVec2D>[] vec2DCache;
    private final Pointer<RealTimeVec3D>[] vec3DCache;
    private final Pointer<RealTimeVec4D>[] vec4DCache;
    private final Pointer<RealTimeVec2F>[] vec2FCache;
    private final Pointer<RealTimeVec3F>[] vec3FCache;
    private final Pointer<RealTimeVec4F>[] vec4FCache;

    private int dataDOffset;
    private int dataFOffset;
    
    private int vec2DID = 0;
    private int vec3DID = 0;
    private int vec4DID = 0;
    private int vec2FID = 0;
    private int vec3FID = 0;
    private int vec4FID = 0;

    /**
     * Constructs a new CyclicalVectorFactory with a cache of 16KB.
     *
     * @throws java.lang.InstantiationException if off-heap initialization fails.
     * @since 15.02.27
     */
    public RealTimeVectorFactory() throws InstantiationException {
        this(16);
    }

    
    @Override
    public void finalize() throws Throwable {
        super.finalize();
        
        this.dataD.free();
        this.dataF.free();
        
        for (Pointer<RealTimeVec2D> vec : this.vec2DCache) {
            vec.instance.free();
            vec.free();
        }
        
        for(Pointer<RealTimeVec3D> vec : this.vec3DCache) {
            vec.instance.free();
            vec.free();
        }
        
        for(Pointer<RealTimeVec4D> vec : this.vec4DCache) {
            vec.instance.free();
            vec.free();
        }
        
        for(Pointer<RealTimeVec2F> vec : this.vec2FCache) {
            vec.instance.free();
            vec.free();
        }
        
        for(Pointer<RealTimeVec3F> vec : this.vec3FCache) {
            vec.instance.free();
            vec.free();
        }
        
        for(Pointer<RealTimeVec4F> vec : this.vec4FCache) {
            vec.instance.free();
            vec.free();
        }
    }
    /**
     * Constructs a new Cyclical Vector Factory with the specified cache size in
     * kilobytes.
     *
     * @param cacheSize cache size in kilobytes.
     * @throws java.lang.InstantiationException if off-heap initialization fails.
     * @since 15.02.09
     */
    public RealTimeVectorFactory(final int cacheSize) throws InstantiationException {
        final int cacheBytes = cacheSize * 1000;
        final UnsafeTools unsafe = UnsafeTools.getInstance();

        this.dataD = unsafe.dAlloc(cacheBytes / Double.BYTES);
        this.dataF = unsafe.fAlloc(cacheBytes / Float.BYTES);
        
        this.vec2DCache = new Pointer[cacheBytes / GLVec2D.VECTOR_WIDTH];
        this.vec2FCache = new Pointer[cacheBytes / GLVec2F.VECTOR_WIDTH];
        this.vec3DCache = new Pointer[cacheBytes / GLVec3D.VECTOR_WIDTH];
        this.vec3FCache = new Pointer[cacheBytes / GLVec3F.VECTOR_WIDTH];
        this.vec4DCache = new Pointer[cacheBytes / GLVec4D.VECTOR_WIDTH];
        this.vec4FCache = new Pointer[cacheBytes / GLVec4F.VECTOR_WIDTH];
                
        for(int i = 0; i < this.vec2DCache.length; i++) {
            this.vec2DCache[i] = unsafe.moveOffHeap(new RealTimeVec2D(this));
        }
        
        for(int i = 0; i < this.vec2FCache.length; i++) {
            this.vec2FCache[i] = unsafe.moveOffHeap(new RealTimeVec2F(this));
        }
        
        for(int i = 0; i < this.vec3DCache.length; i++) {
            this.vec3DCache[i] = unsafe.moveOffHeap(new RealTimeVec3D(this));
        }
        
        for(int i = 0; i < this.vec3FCache.length; i++) {
            this.vec3FCache[i] = unsafe.moveOffHeap(new RealTimeVec3F(this));
        }
        
        for(int i = 0; i < this.vec4DCache.length; i++) {
            this.vec4DCache[i] = unsafe.moveOffHeap(new RealTimeVec4D(this));
        }
        
        for(int i = 0; i < this.vec4FCache.length; i++) {
            this.vec4FCache[i] = unsafe.moveOffHeap(new RealTimeVec4F(this));
        }
    }
    
    private int nextVec2DID() {
        final int testID = this.vec2DID + 1;

        return this.vec2DID = testID % this.vec2DCache.length;
    }

    private int nextVec2FID() {
        final int testID = this.vec2FID + 1;

        return this.vec2FID = testID % this.vec2FCache.length;
    }

    private int nextVec3DID() {
        final int testID = this.vec3DID + 1;

        return this.vec3DID = testID % this.vec3DCache.length;
    }

    private int nextVec3FID() {
        final int testID = this.vec3FID + 1;

        return this.vec3FID = testID % this.vec3FCache.length;
    }

    private int nextVec4DID() {
        final int testID = this.vec4DID + 1;

        return this.vec4DID = testID % this.vec4DCache.length;
    }

    private int nextVec4FID() {
        final int testID = this.vec4FID + 1;

        return this.vec4FID = testID % this.vec4FCache.length;
    }    
    private int nextVecNDOffset(final int vecSize) {
        final int vsize = vecSize;

        if (this.dataDOffset + vsize < this.dataD.instance.length) {
            final int off = this.dataDOffset;

            this.dataDOffset += vsize;
            return off;
        } else {
            this.dataDOffset = vsize;
            return 0;
        }
    }

    private int nextVecNFOffset(final int vecSize) {
        final int vsize = vecSize;

        if (this.dataFOffset + vsize < this.dataF.instance.length) {
            final int off = this.dataFOffset;

            this.dataFOffset += vsize;
            return off;
        } else {
            this.dataFOffset = vsize;
            return 0;
        }
    }

    @Override
    public GLVec2D nextGLVec2D() {
        final int id = this.nextVec2DID();        

        return this.vec2DCache[id].instance;
    }

    @Override
    public GLVec2F nextGLVec2F() {
        final int id = this.nextVec2FID();
        
        return this.vec2FCache[id].instance;
    }

    @Override
    public GLVec3D nextGLVec3D() {
        final int id = this.nextVec3DID();
        
        return this.vec3DCache[id].instance;
    }

    @Override
    public GLVec3F nextGLVec3F() {
        final int id = this.nextVec3FID();
        
        return this.vec3FCache[id].instance;
    }

    @Override
    public GLVec4D nextGLVec4D() {
        final int id = this.nextVec4DID();
        
        return this.vec4DCache[id].instance;
    }

    @Override
    public GLVec4F nextGLVec4F() {
        final int id = this.nextVec4FID();
        
        return this.vec4FCache[id].instance;
    }

    @Override
    public GLVecND nextGLVecND(final int vecSize) {
        final int offset = this.nextVecNDOffset(vecSize);

        return new MappedVecND(this, this.dataD.instance, offset, vecSize, vecSize);
    }

    @Override
    public GLVecNF nextGLVecNF(final int vecSize) {
        final int offset = this.nextVecNFOffset(vecSize);

        return new MappedVecNF(this, this.dataF.instance, offset, vecSize, vecSize);
    }

    @Override
    public String toString() {
        return String.format("Vector Factory: [sfp: %d dfp: %d]", this.dataF.instance.length, this.dataD.instance.length);
    }
}
