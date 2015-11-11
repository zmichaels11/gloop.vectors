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
m4_include(`m4/com/longlinkislong/gloop/staticmatnx_def.m4')
m4_divert(0)m4_dnl 
package com.longlinkislong.gloop;

public class MatT extends BaseT implements StaticMat<TYPE[]> {
    private final TYPE[] data;
    private final MatrixFactory mf;
    private final int matSize;

    public MatT (
        final MatrixFactory mf,
        final int matSize,
        final GLMat other) {

        this.mf = mf;
        this.matSize = matSize;
        this.data = new TYPE[matSize * matSize];
        this.set(other);
    }

    public MatT (
        final MatrixFactory mf,
        final int matSize,
        final TYPE[] data, final int offset, final int length) {
        
        this.matSize = matSize;
        this.mf = mf;
        this.data = new TYPE[matSize * matSize];
        System.arraycopy(data, offset, this.data, 0, length);
    }

    public MatT (
        final MatrixFactory mf,
        final int matSize,
        final TYPE... values) {

        this(mf, matSize, values, 0, values.length);
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
    public int size() {
        return this.matSize;
    }
}