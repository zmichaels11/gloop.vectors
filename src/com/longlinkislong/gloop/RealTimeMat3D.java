package com.longlinkislong.gloop;

public class RealTimeMat3D extends GLMat3D implements StaticMat<double[]> {
    private final com.longlinkislong.gloop.UnsafeTools.Pointer<double[]> data;
    private final MatrixFactory mf;    

    public RealTimeMat3D ( final MatrixFactory factory ) {
        this.mf = java.util.Objects.requireNonNull(factory);
        this.data = com.longlinkislong.gloop.UnsafeTools.getInstance().dAlloc (9);
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
    public final MatrixFactory getFactory() {
        return this.mf;
    }

    public void free() {
        this.data.free();
    }
}
