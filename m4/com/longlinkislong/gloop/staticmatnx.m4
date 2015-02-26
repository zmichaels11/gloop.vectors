m4_divert(-1)
m4_include(`m4/m4utils/fdef.m4')
m4_include(`m4/com/longlinkislong/gloop/staticmatnx_def.m4')
m4_divert(0)m4_dnl 
package com.longlinkislong.gloop;

public class MatT extends BaseT implements StaticMat {
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