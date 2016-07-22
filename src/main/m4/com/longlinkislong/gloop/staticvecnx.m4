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
m4_divert(-1)
m4_include(`m4/m4utils/fdef.m4')
m4_include(`m4/com/longlinkislong/gloop/staticvecnx_def.m4')
m4_divert(0)m4_dnl 
package com.longlinkislong.gloop;

/**
 * The base class for all statically-backed arbitrary-sized matrices.
 * @author zmichaels
 * @since 15.02.27
 */
public class VecT extends BaseT implements StaticVec<TYPE[]> {
    private final TYPE[] data;
    private final VectorFactory vf;
    private final int vecSize;

    /**
     * Constructs a new VecT of the specified size with the specified values.
     *
     * @param vf the VectorFactory used to allocate all child vectors.
     * @param vecSize the number of elements wide
     * @param data the array to read the values from
     * @param offset the offset to start reading the data
     * @param length the number of elements to read
     * @since 15.02.27
     */
    public VecT (
        final VectorFactory vf,
        final int vecSize,
        final TYPE[] data, final int offset, final int length) {

        this.vecSize = vecSize;
        this.vf = vf;
        this.data = new TYPE[vecSize];
        System.arraycopy(data, offset, this.data, 0, length);
    }

    /**
     * Constructs a new VecT of the specified size with the specified values.
     *
     * @param vf the VectorFactory used to allocate all child vectors.
     * @param vecSize the number of elements wide
     * @param values the data elements
     * @since 15.02.27
     */
    public VecT (
        final VectorFactory vf, 
        final int vecSize, 
        final TYPE... values) {

        this(vf, vecSize, values, 0, values.length);
    }

    @Override
    public final TYPE[] data() {
        return this.data;
    }

    @Override
    protected final int offset() {
        return 0;
    }

    @Override
    public final VectorFactory getFactory() {
        return this.vf;
    }

    @Override
    public int size() {
        return this.vecSize;
    }

    @Override
    public final BaseT asStaticVec() {
        return this;
    }
}
