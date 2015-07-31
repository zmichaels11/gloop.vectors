m4_divert(-1)
m4_include(`m4/m4utils/fdef.m4')
m4_include(`m4/com/longlinkislong/gloop/realtimematx_def.m4')
m4_divert(0)m4_dnl 
package com.longlinkislong.gloop;

public class MatT extends BaseT implements StaticMat<TYPE[]> {
    private final com.longlinkislong.gloop.UnsafeTools.Pointer<TYPE[]> data;
    private final MatrixFactory mf;    

    public MatT ( final MatrixFactory factory ) {
        this.mf = java.util.Objects.requireNonNull(factory);
        this.data = com.longlinkislong.gloop.UnsafeTools.getInstance().m4_ifelse(TYPE, `float', `fAlloc', `dAlloc') (VEC_SIZE);
    }

    @Override
    public final TYPE[] data() {
        return this.data.instance;
    }

    @Override
    protected final int offset() {
        return 0;
    }

    @Override
    public final MatrixFactory getFactory() {
        return this.mf;
    }

    public void free() {
        this.data.free();
    }
}