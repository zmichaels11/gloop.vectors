m4_divert(-1)
m4_include(`m4/m4utils/fdef.m4')
m4_include(`m4/com/longlinkislong/gloop/realtimevecx_def.m4')
m4_divert(0)m4_dnl 
package com.longlinkislong.gloop;

/**
 * A statically backed VEC_SIZE TYPE vector.
 *
 * @author zmichaels
 * @since 15.02.27
 */
public class VecT extends BaseT {    
    private final com.longlinkislong.gloop.UnsafeTools.Pointer<TYPE[]> data;
    private final VectorFactory mf;

    public VecT ( VectorFactory factory ) {        
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
    public final VectorFactory getFactory() {
        return this.mf;
    }    

    public void free() {
        this.data.free();
    }
}