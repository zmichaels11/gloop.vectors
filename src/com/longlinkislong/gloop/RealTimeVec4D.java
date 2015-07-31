package com.longlinkislong.gloop;

/**
 * A statically backed 4 double vector.
 *
 * @author zmichaels
 * @since 15.02.27
 */
public class RealTimeVec4D extends GLVec4D {    
    private final com.longlinkislong.gloop.UnsafeTools.Pointer<double[]> data;
    private final VectorFactory mf;

    public RealTimeVec4D ( VectorFactory factory ) {        
        this.mf = java.util.Objects.requireNonNull(factory);
        this.data = com.longlinkislong.gloop.UnsafeTools.getInstance().dAlloc (4);
    }

    @Override
    public final double[] data() {
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
