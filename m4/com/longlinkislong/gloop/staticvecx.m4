m4_divert(-1)
m4_include(`m4/m4utils/fdef.m4')
m4_include(`m4/com/longlinkislong/gloop/staticvecx_def.m4')
m4_divert(0)m4_dnl 
package com.longlinkislong.gloop;

public class VecT extends BaseT implements StaticVec {
    private final TYPE[] data = new TYPE[VEC_SIZE];
    private final VectorFactory vf;

    public VecT (final VectorFactory vf) {
        this.vf = vf;
    }

    public VecT (final VectorFactory vf, final GLVec other) {
        this.vf = vf;
        this.set(other);
    }

    public VecT (final VectorFactory vf, final TYPE... values) {
        this(vf, values, 0, values.length);
    }

    public VecT (
        final VectorFactory vf,
        final TYPE[] data, final int offset, final int length) {

        this.vf = vf;
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
    public final VectorFactory getFactory() {
        return this.vf;
    }

    @Override
    public final BaseT asStaticVec() {
        return this;
    }
}