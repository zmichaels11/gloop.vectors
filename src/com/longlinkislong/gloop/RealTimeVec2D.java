package com.longlinkislong.gloop;

/**
 * A statically backed 2 double vector.
 *
 * @author zmichaels
 * @since 15.02.27
 */
public class RealTimeVec2D extends GLVec2D {    
    private final com.longlinkislong.gloop.UnsafeTools.Pointer<double[]> data;
    private final VectorFactory mf;

    public RealTimeVec2D ( VectorFactory factory ) {        
        this.mf = java.util.Objects.requireNonNull(factory);
        this.data = com.longlinkislong.gloop.UnsafeTools.getInstance().dAlloc (2);
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
