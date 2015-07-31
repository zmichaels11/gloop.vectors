package com.longlinkislong.gloop;

/**
 * A statically backed 4 float vector.
 *
 * @author zmichaels
 * @since 15.02.27
 */
public class RealTimeVec4F extends GLVec4F {    
    private final com.longlinkislong.gloop.UnsafeTools.Pointer<float[]> data;
    private final VectorFactory mf;

    public RealTimeVec4F ( VectorFactory factory ) {        
        this.mf = java.util.Objects.requireNonNull(factory);
        this.data = com.longlinkislong.gloop.UnsafeTools.getInstance().fAlloc (4);
    }

    @Override
    public final float[] data() {
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
