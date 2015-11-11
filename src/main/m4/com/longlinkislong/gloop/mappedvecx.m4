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
m4_include(`m4/com/longlinkislong/gloop/mappedvecx_def.m4')
m4_divert(0)m4_dnl 
package com.longlinkislong.gloop;

/**
 * A VEC_SIZE`D' TYPE vector that references an outside array for vector elements.
 *
 * @author zmichaels
 * @since 15.02.27
 */
public class VecT extends BaseT implements MappedVec<VecT> {
    private final TYPE[] data;
    private final int length;
    private final VectorFactory vf;
    private final int baseOffset;
    private int offset;

    /**
     * Wraps an array as a VecT.
     *
     * @param vf the VectorFactory used to allocate the child vectors.
     * @param data the array to reference for vector elements
     * @param offset the offset to start the vector
     * @param length the number of elements the vector can access from the array.
     * @since 15.02.27
     */
    public VecT (
        final VectorFactory vf,
        final TYPE[] data, final int offset, final int length) {

        this.data = data;
        this.baseOffset = this.offset = offset;
        this.length = length;
        this.vf = vf;        
    }

    @Override
    protected final TYPE[] data() {
        return this.data;
    }

    @Override
    protected final int offset() {
        return this.offset;
    }

    @Override
    public final VectorFactory getFactory() {
        return this.vf;
    }

    @Override
    public final VecT shift(final int shiftOffset) {
        return this.remap(this.offset + shiftOffset);
    }

    @Override
    public final VecT push() {
        return this.shift(VEC_SIZE);
    }

    @Override
    public final VecT pop() {
        return this.shift(-VEC_SIZE);
    }

    @Override
    public final VecT remap(final int offset) {
        if(offset < this.baseOffset || offset > (this.baseOffset + this.length)) {
            throw new IndexOutOfBoundsException(
                String.format("Tried %d on range [%d, %d]", 
                    offset, 
                    this.baseOffset, 
                    (this.baseOffset + this.length)));
        }

        this.offset = offset;
        return this;
    }
}