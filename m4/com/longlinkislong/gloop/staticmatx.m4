m4_divert(-1)
m4_include(`m4/m4utils/fdef.m4')
m4_include(`m4/com/longlinkislong/gloop/staticmatx_def.m4')
m4_divert(0)m4_dnl 
package com.longlinkislong.gloop;

public class MatT extends BaseT implements StaticMat {
    private final TYPE[] data = new TYPE[MAT_SIZE];
    private final MatrixFactory mf;

    public MatT (final MatrixFactory mf) {
        this.mf = mf;
    }

    public MatT (final MatrixFactory mf, final GLMat other) {
        this.mf = mf;
        this.set(other);
    }

    public MatT (final MatrixFactory mf, final TYPE... values) {
        this(mf, values, 0, values.length);
    }

    public MatT (
        final MatrixFactory mf,
        final TYPE[] data, final int offset, final int length) {

        this.mf = mf;
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