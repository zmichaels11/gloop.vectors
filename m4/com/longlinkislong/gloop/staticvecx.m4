m4_divert(-1)
m4_include(`m4/m4utils/fdef.m4')
m4_include(`m4/com/longlinkislong/gloop/staticvecx_def.m4')
m4_divert(0)m4_dnl 
package com.longlinkislong.gloop;

/**
 * A statically backed VEC_SIZE TYPE vector.
 *
 * @author zmichaels
 * @since 15.02.27
 */
public class VecT extends BaseT implements StaticVec<TYPE[]> {
    private final TYPE[] data = new TYPE[VECTOR_SIZE * VECTOR_SIZE];
    private final VectorFactory vf;

    /**
     * Constructs a new statically backed VEC_SIZE TYPE vector. The initial
     * values are set to the zero vector.
     *
     * @param vf the VectorFactory used to allocate child vectors.
     * @since 15.02.27
     */
    public VecT (final VectorFactory vf) {
        this.vf = vf;
    }

    /**
     * Constructs a new statically backed VEC_SIZE TYPE vector. The initial
     * values are set to the same values of the other vector. All unset values
     * are set to the corresponding element in the zero vector.
     *
     * @param vf the VectorFactory used to allocate child vectors
     * @param other the vector to copy values from  
     * @since 15.02.27
     */
    public VecT (final VectorFactory vf, final GLVec other) {
        this.vf = vf;
        this.set(other);
    }

    /**
     * Constructs a new statically backed VEC_SIZE TYPE vector. The initial
     * values are set to the specified values. All unset values are set to the
     * corresponding elements in the zero vector.
     *
     * @param vf the VectorFactory used to allocate child vectors
     * @param values the values to set the elements to
     * @since 15.02.27
     */
    public VecT (final VectorFactory vf, final TYPE... values) {
        this(vf, values, 0, values.length);
    }

    /**
     * Constructs a new statically backed VEC_SIZE TYPE vector. The initial
     * values are set to values from the specified array. All unset values are
     * set to the corresponding elements in the zero vector.
     *
     * @param vf the VectorFactory used to allocate child vectors
     * @param data the array to read values from
     * @param offset the offset to start reading values
     * @param length the number of elements to read
     * @since 15.02.27
     */
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