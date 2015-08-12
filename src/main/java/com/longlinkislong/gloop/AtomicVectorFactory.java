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
 * A VectorFactory that shares the same data across each vector. This allows for
 * improved vector caching and reduced allocation times. However vectors will be
 * automatically recycled when the end of the buffer is reached. Because of
 * this, vectors created by this factory are best used as temporary values.
 *
 * @author zmichaels
 * @since 15.02.26
 */
public class AtomicVectorFactory implements VectorFactory {

    private final double[] dataD;
    private final float[] dataF;
    private final MappedVec2D[] vec2DCache;
    private final MappedVec2F[] vec2FCache;
    private final MappedVec3D[] vec3DCache;
    private final MappedVec3F[] vec3FCache;
    private final MappedVec4D[] vec4DCache;
    private final MappedVec4F[] vec4FCache;

    private final AtomicInteger dataDOffset = new AtomicInteger();
    private final AtomicInteger dataFOffset = new AtomicInteger();

    private final AtomicInteger vec2DID = new AtomicInteger();
    private final AtomicInteger vec3DID = new AtomicInteger();
    private final AtomicInteger vec4DID = new AtomicInteger();

    private final AtomicInteger vec2FID = new AtomicInteger();
    private final AtomicInteger vec3FID = new AtomicInteger();
    private final AtomicInteger vec4FID = new AtomicInteger();

    /**
     * Constructs a new CyclicalVectorFactory with a cache of 16KB.
     *
     * @since 15.02.27
     */
    public AtomicVectorFactory() {
        this(16);
    }

    /**
     * Constructs a new Cyclical Vector Factory with the specified cache size in
     * kilobytes.
     *
     * @param cacheSize cache size in kilobytes.
     * @since 15.02.09
     */
    public AtomicVectorFactory(final int cacheSize) {
        final int cacheBytes = cacheSize * 1000;

        this.dataD = new double[cacheBytes / 8];
        this.dataF = new float[cacheBytes / 4];

        this.vec2DCache = new MappedVec2D[cacheBytes / GLVec2D.VECTOR_WIDTH];
        this.vec2FCache = new MappedVec2F[cacheBytes / GLVec2F.VECTOR_WIDTH];
        this.vec3DCache = new MappedVec3D[cacheBytes / GLVec3D.VECTOR_WIDTH];
        this.vec3FCache = new MappedVec3F[cacheBytes / GLVec3F.VECTOR_WIDTH];
        this.vec4DCache = new MappedVec4D[cacheBytes / GLVec4D.VECTOR_WIDTH];
        this.vec4FCache = new MappedVec4F[cacheBytes / GLVec4F.VECTOR_WIDTH];

        for (int i = 0; i < vec2DCache.length; i++) {
            this.vec2DCache[i] = new MappedVec2D(this, this.dataD, 0, this.dataD.length - GLVec2D.VECTOR_SIZE);
        }

        for (int i = 0; i < vec2FCache.length; i++) {
            this.vec2FCache[i] = new MappedVec2F(this, this.dataF, 0, this.dataF.length - GLVec2F.VECTOR_SIZE);
        }

        for (int i = 0; i < vec3DCache.length; i++) {
            this.vec3DCache[i] = new MappedVec3D(this, this.dataD, 0, this.dataD.length - GLVec3D.VECTOR_SIZE);
        }

        for (int i = 0; i < vec3FCache.length; i++) {
            this.vec3FCache[i] = new MappedVec3F(this, this.dataF, 0, this.dataF.length - GLVec3F.VECTOR_SIZE);
        }

        for (int i = 0; i < vec4DCache.length; i++) {
            this.vec4DCache[i] = new MappedVec4D(this, this.dataD, 0, this.dataD.length - GLVec4D.VECTOR_SIZE);
        }

        for (int i = 0; i < vec4FCache.length; i++) {
            this.vec4FCache[i] = new MappedVec4F(this, this.dataF, 0, this.dataF.length - GLVec4F.VECTOR_SIZE);
        }
    }

    private int nextVec2DID() {
        return this.vec2DID.getAndUpdate(id -> (id + 1) % this.vec2DCache.length);
    }

    private int nextVec2FID() {
        return this.vec2FID.getAndUpdate(id -> (id + 1) % this.vec2FCache.length);
    }

    private int nextVec3DID() {
        return this.vec3DID.getAndUpdate(id -> (id + 1) % this.vec3DCache.length);
    }

    private int nextVec3FID() {
        return this.vec3FID.getAndUpdate(id -> (id + 1) % this.vec3FCache.length);
    }

    private int nextVec4DID() {
        return this.vec4DID.getAndUpdate(id -> (id + 1) % this.vec4DCache.length);
    }

    private int nextVec4FID() {
        return this.vec4FID.getAndUpdate(id -> (id + 1) % this.vec4FCache.length);
    }

    private int nextVec2DOffset() {
        final int vsize = GLVec2D.VECTOR_SIZE;

        return this.dataDOffset.updateAndGet(off -> off + vsize < this.dataD.length ? off + vsize : 0);
    }

    private int nextVec2FOffset() {
        final int vsize = GLVec2F.VECTOR_SIZE;

        return this.dataFOffset.updateAndGet(off -> off + vsize < this.dataF.length ? off + vsize : 0);
    }

    private int nextVec3DOffset() {
        final int vsize = GLVec3D.VECTOR_SIZE;

        return this.dataDOffset.updateAndGet(off -> off + vsize < this.dataD.length ? off + vsize : 0);
    }

    private int nextVec3FOffset() {
        final int vsize = GLVec3F.VECTOR_SIZE;

        return this.dataFOffset.updateAndGet(off -> off + vsize < this.dataF.length ? off + vsize : 0);
    }

    private int nextVec4DOffset() {
        final int vsize = GLVec4D.VECTOR_SIZE;

        return this.dataDOffset.updateAndGet(off -> off + vsize < this.dataD.length ? off + vsize : 0);
    }

    private int nextVec4FOffset() {
        final int vsize = GLVec4F.VECTOR_SIZE;

        return this.dataFOffset.updateAndGet(off -> off + vsize < this.dataF.length ? off + vsize : 0);
    }

    private int nextVecNDOffset(final int vecSize) {
        final int vsize = vecSize;

        return this.dataDOffset.updateAndGet(off -> off + vsize < this.dataD.length ? off + vsize : 0);
    }

    private int nextVecNFOffset(final int vecSize) {
        final int vsize = vecSize;

        return this.dataFOffset.updateAndGet(off -> off + vsize < this.dataF.length ? off + vsize : 0);
    }

    @Override
    public GLVec2D nextGLVec2D() {
        final int id = this.nextVec2DID();
        final int offset = this.nextVec2DOffset();

        return this.vec2DCache[id].remap(offset);
    }

    @Override
    public GLVec2F nextGLVec2F() {
        final int id = this.nextVec2FID();
        final int offset = this.nextVec2FOffset();

        return this.vec2FCache[id].remap(offset);
    }

    @Override
    public GLVec3D nextGLVec3D() {
        final int id = this.nextVec3DID();
        final int offset = this.nextVec3DOffset();

        return this.vec3DCache[id].remap(offset);
    }

    @Override
    public GLVec3F nextGLVec3F() {
        final int id = this.nextVec3FID();
        final int offset = this.nextVec3FOffset();

        return this.vec3FCache[id].remap(offset);
    }

    @Override
    public GLVec4D nextGLVec4D() {
        final int id = this.nextVec4DID();
        final int offset = this.nextVec4DOffset();

        return this.vec4DCache[id].remap(offset);
    }

    @Override
    public GLVec4F nextGLVec4F() {
        final int id = this.nextVec4FID();
        final int offset = this.nextVec4FOffset();

        return this.vec4FCache[id].remap(offset);
    }

    @Override
    public GLVecND nextGLVecND(final int vecSize) {
        final int offset = this.nextVecNDOffset(vecSize);

        return new MappedVecND(this, this.dataD, offset, vecSize, vecSize);
    }

    @Override
    public GLVecNF nextGLVecNF(final int vecSize) {
        final int offset = this.nextVecNFOffset(vecSize);

        return new MappedVecNF(this, this.dataF, offset, vecSize, vecSize);
    }

    @Override
    public String toString() {
        return String.format("Vector Factory: [sfp: %d dfp: %d]", this.dataF.length, this.dataD.length);
    }
}
