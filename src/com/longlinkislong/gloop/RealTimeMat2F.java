package com.longlinkislong.gloop;

public class RealTimeMat2F extends GLMat2F implements StaticMat<float[]> {
    private final com.longlinkislong.gloop.UnsafeTools.Pointer<float[]> data;
    private final MatrixFactory mf;    

    public RealTimeMat2F ( final MatrixFactory factory ) {
        this.mf = java.util.Objects.requireNonNull(factory);
        this.data = com.longlinkislong.gloop.UnsafeTools.getInstance().fAlloc (2);
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
    public final MatrixFactory getFactory() {
        return this.mf;
    }

    public void free() {
        this.data.free();
    }
}
