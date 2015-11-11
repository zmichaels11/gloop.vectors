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
m4_include(`m4/com/longlinkislong/gloop/staticmatx_def.m4')
m4_divert(0)m4_dnl 
package com.longlinkislong.gloop;

/**
 * A MAT_SIZE`x'MAT_SIZE TYPE statically-backed matrix.
 *
 * @author zmichaels
 * @since 15.02.27
 */
public class MatT extends BaseT implements StaticMat<TYPE[]> {
    private final TYPE[] data = new TYPE[m4_eval(MAT_SIZE * MAT_SIZE)];
    private final MatrixFactory mf;

    /**
     * Constructs a new statically backed MAT_SIZE`x'MAT_SIZE matrix. The initial 
     * values of the elements are set to the identity matrix.
     *
     * @param mf the MatrixFactory used to allocate all child matrices.
     * @since 15.02.27
     */
    public MatT (final MatrixFactory mf) {
        this.mf = mf;
        this.identity();
    }

    /**
     * Constructs a new statically backed MAT_SIZE`x'MAT_SIZE matrix. The initial
     * values are set to the same values as the reference matrix.
     *
     * @param mf the MatrixFactory used to allocate all child matrices.
     * @param other the matrix to copy values from
     * @since 15.02.27
     */
    public MatT (final MatrixFactory mf, final GLMat other) {
        this.mf = mf;
        this.set(other);
    }

    /**
     * Constructs a new statically backed MAT_SIZE`x'MAT_SIZE matrix. The initial
     * values are set to the defined values. All unset values will take on the
     * pattern of the identity matrix.
     *
     * @param mf the MatrixFactory used to allocate all child matrices.
     * @param values the values to write to the matrix.
     * @since 15.02.27
     */
    public MatT (final MatrixFactory mf, final TYPE... values) {
        this(mf, values, 0, values.length);
    }

    /**
     * Constructs a new statically backed MAT_SIZE`x'MAT_SIZE matrix. The initial
     * values are set to values read from the specified array. All unset values
     * will take on the pattern of the identity matrix.
     *
     * @param mf the MatrixFactory used to allocate all child matrices.
     * @param data the array to read the values from
     * @param offset the offset to start reading the values
     * @param length the number of elements to read.
     * @since 15.02.27
     */
    public MatT (
        final MatrixFactory mf,
        final TYPE[] data, final int offset, final int length) {

        this.mf = mf;
        this.identity();
        System.arraycopy(data, offset, this.data, 0, length);
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
    public final MatrixFactory getFactory() {
        return this.mf;
    }

    @Override
    public final BaseT asStaticMat() {
        return this;
    }
}