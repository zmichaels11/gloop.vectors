m4_divert(-1)
m4_include(`m4/m4utils/fdef.m4')
m4_include(`m4/com/longlinkislong/gloop/staticvecnx_def.m4')
m4_divert(0)m4_dnl 
package com.longlinkislong.gloop;

public class VecT extends BaseT implements StaticVec {
    private final TYPE[] data;
    private final VectorFactory vf;
    private final int vecSize;

    public VecT (
        final VectorFactory vf,
        final int vecSize,
        final TYPE[] data, final int offset, final int length) {

        this.vecSize = vecSize;
        this.vf = vf;
        this.data = new TYPE[vecSize];
        System.arraycopy(data, offset, this.data, 0, length);
    }

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
}
