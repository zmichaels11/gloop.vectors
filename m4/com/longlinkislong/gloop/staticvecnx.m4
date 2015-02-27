m4_divert(-1)
m4_include(`m4/m4utils/fdef.m4')
m4_include(`m4/com/longlinkislong/gloop/staticvecnx_def.m4')
m4_divert(0)m4_dnl 
package com.longlinkislong.gloop;

/**
 * The base class for all statically-backed arbitrary-sized matrices.
 * @author zmichaels
 * @since 15.02.27
 */
public class VecT extends BaseT implements StaticVec<TYPE[]> {
    private final TYPE[] data;
    private final VectorFactory vf;
    private final int vecSize;

    /**
     * Constructs a new VecT of the specified size with the specified values.
     *
     * @param vf the VectorFactory used to allocate all child vectors.
     * @param vecSize the number of elements wide
     * @param data the array to read the values from
     * @param offset the offset to start reading the data
     * @param length the number of elements to read
     * @since 15.02.27
     */
    public VecT (
        final VectorFactory vf,
        final int vecSize,
        final TYPE[] data, final int offset, final int length) {

        this.vecSize = vecSize;
        this.vf = vf;
        this.data = new TYPE[vecSize];
        System.arraycopy(data, offset, this.data, 0, length);
    }

    /**
     * Constructs a new VecT of the specified size with the specified values.
     *
     * @param vf the VectorFactory used to allocate all child vectors.
     * @param vecSize the number of elements wide
     * @param values the data elements
     * @since 15.02.27
     */
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
