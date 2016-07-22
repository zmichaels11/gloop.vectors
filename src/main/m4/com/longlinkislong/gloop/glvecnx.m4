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
m4_include(`m4/m4utils/types.m4')
m4_include(`m4/com/longlinkislong/gloop/glvecnx_def.m4')
m4_divert(0)m4_dnl
package com.longlinkislong.gloop;

public abstract class VecT extends BaseT<VecT> {

    /**
     * Creates a new vector with the defined number of elements.
     *
     * @param size the number of elements.
     * @param values the values for the vector.
     * @return the vector.
     * @since 15.02.27
     */
    public static VecT create(final int size, final TYPE... values) {
        return create(size, values, 0, values.length);
    }

    /**
     * Creates a new vector with the defined number of elements
     *
     * @param size the number of elements
     * @param data the values for the vector
     * @param offset the offset to start reading the values
     * @param length the number of elements to read.
     * @return the vector
     * @since 15.02.27
     */
    public static VecT create(
        final int size,
        final TYPE[] data, final int offset, final int length) {

        return Vectors.DEFAULT_FACTORY._fdef(`nextGLVec', `N', TYPE)(size)
            .zero()
            .set(data, offset, length);
    }

    @Override
    public final _fdef(`GLVec',,OTHER) _fdef(`asGLVec',,OTHER)() {
        final _fdef(`GLVec',,OTHER) out = _next(`N', OTHER, this.size());
        
        for(int i = 0; i < this.size(); i++) {
            out.set(i, (OTHER) this.get(i));
        }

        return out;
    }

    _asVec(2)
    _asVec(3)
    _asVec(4)

    @Override
    public final VecT plus(final GLVec other) {
        final VecT out = _next(`N', TYPE, this.size());
        final VecT in1 = _cast(_cast(other, BaseT), VecT, this.size());

        _call(`plus')(
            out.data(), out.offset(),
            this.data(), this.offset(),
            in1.data(), in1.offset(),
            this.size());

        return out;
    }

    @Override
    public final VecT minus(final GLVec other) {
        final VecT out = _next(`N', TYPE, this.size());
        final VecT in1 = _cast(_cast(other, BaseT), VecT, this.size());
    
        _call(`minus')(
            out.data(), out.offset(),
            this.data(), this.offset(),
            in1.data(), in1.offset(),
            this.size());

        return out;
    }

    @Override
    public final VecT _fdef(`asGLVec', `N', TYPE)(final int size) {
        final VecT out = _next(`N', TYPE, size);
        final int length = Math.min(this.size(), size);

        out.zero();
        out.set(this.data(), this.offset(), length);
        return out;
    }

    @Override
    public final TYPE dot(final GLVec other) {
        final VecT in1 = _cast(_cast(other, BaseT), VecT, this.size());

        return _call(`dot')(
            this.data(), this.offset(),
            in1.data(), in1.offset(),
            this.size());
    }

    @Override
    public VecT set(final TYPE[] values, final int offset, final int length) {
        if(offset < 0 || length < 0 || length < this.size() || values.length - offset < length) {
            throw new IndexOutOfBoundsException();
        }
    
        System.arraycopy(values, offset, this.data(), this.offset(), length);
        return this;
    }

    @Override
    public VecT set(final int index, final TYPE value) {
        if(index < 0 || index > this.size()) {
            throw new IndexOutOfBoundsException();
        }

        this.data()[this.offset() + index] = value;
        return this;
    }

    @Override
    public final TYPE get(final int index) {
        if(index < 0 || index > this.size()) {
            throw new IndexOutOfBoundsException();
        }

        return this.data()[this.offset() + index];
    }

    @Override
    public final VecT scale(final TYPE value) {
        final VecT out = _next(`N', TYPE, this.size());

        _call(`scale')(
            out.data(), out.offset(),
            this.data(), this.offset(),
            value,
            this.size());

        return out;
    }

    @Override
    public final double length() {
        return _call(`length')(this.data(), this.offset(), this.size());
    }

    @Override
    public final VecT negative() {
        final VecT out = _next(`N', TYPE, this.size());

        _call(`negative')(
            out.data(), out.offset(),
            this.data(), this.offset(),
            this.size());

        return out;
    }

    @Override
    public final VecT reflect(final GLVec surfaceNormal) {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public final VecT inverse() {
        final VecT out = _next(`N', TYPE, this.size());

        _call(`inverse')(
            out.data(), out.offset(),
            this.data(), this.offset(),
            this.size());

        return out;
    }

    @Override
    public VecT asStaticVec() {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public final VecT zero() {
        for(int i = 0; i < this.size(); i++) {
            this.set(i, _real(TYPE, 0.0));
        }

        return this;
    }

    @Override
    public final VecT cross(final GLVec other) {
        final VecT out = _next(`N', TYPE, this.size());
        final VecT in1 = _cast(_cast(other, BaseT), VecT, this.size());

        _call(`cross')(
            out.data(), out.offset(),
            this.data(), this.offset(),
            in1.data(), in1.offset(),
            this.size());

        return out;
    }

    @Override
    public final VecT hadamard(final GLVec other) {
        final VecT out = _next(`N', TYPE, this.size());
        final VecT in1 = _cast(_cast(other, BaseT), VecT, this.size());

        _call(`multiply')(
            out.data(), out.offset(),
            this.data(), this.offset(),
            in1.data(), in1.offset(),
            this.size());

        return out;
    }

    @Override
    public final VecT copyTo(final VectorFactory factory) {
        final VecT out = _fdef(`factory.nextGLVec', VEC_SIZE, TYPE) (this.size());

        out.set(this);

        return out;
    }
}