m4_divert(-1)
m4_include(`m4/m4utils/fdef.m4')
m4_include(`m4/com/longlinkislong/gloop/staticmatx_def.m4')
m4_divert(0)m4_dnl 
package com.longlinkislong.gloop;

/**
 * A MAT_SIZE`x'MAT_SIZE TYPE statically-backed matrix.
 *
 * @author zmichaels
 * @since 15.02.27
 */
public class MatT extends BaseT implements StaticMat<TYPE[]> {
    private final TYPE[] data = new TYPE[m4_eval(MAT_SIZE * MAT_SIZE)];
    private final MatrixFactory mf;

    /**
     * Constructs a new statically backed MAT_SIZE`x'MAT_SIZE matrix. The initial 
     * values of the elements are set to the identity matrix.
     *
     * @param mf the MatrixFactory used to allocate all child matrices.
     * @since 15.02.27
     */
    public MatT (final MatrixFactory mf) {
        this.mf = mf;
        this.identity();
    }

    /**
     * Constructs a new statically backed MAT_SIZE`x'MAT_SIZE matrix. The initial
     * values are set to the same values as the reference matrix.
     *
     * @param mf the MatrixFactory used to allocate all child matrices.
     * @param other the matrix to copy values from
     * @since 15.02.27
     */
    public MatT (final MatrixFactory mf, final GLMat other) {
        this.mf = mf;
        this.set(other);
    }

    /**
     * Constructs a new statically backed MAT_SIZE`x'MAT_SIZE matrix. The initial
     * values are set to the defined values. All unset values will take on the
     * pattern of the identity matrix.
     *
     * @param mf the MatrixFactory used to allocate all child matrices.
     * @param values the values to write to the matrix.
     * @since 15.02.27
     */
    public MatT (final MatrixFactory mf, final TYPE... values) {
        this(mf, values, 0, values.length);
    }

    /**
     * Constructs a new statically backed MAT_SIZE`x'MAT_SIZE matrix. The initial
     * values are set to values read from the specified array. All unset values
     * will take on the pattern of the identity matrix.
     *
     * @param mf the MatrixFactory used to allocate all child matrices.
     * @param data the array to read the values from
     * @param offset the offset to start reading the values
     * @param length the number of elements to read.
     * @since 15.02.27
     */
    public MatT (
        final MatrixFactory mf,
        final TYPE[] data, final int offset, final int length) {

        this.mf = mf;
        this.identity();
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
    public final MatrixFactory getFactory() {
        return this.mf;
    }

    @Override
    public final BaseT asStaticMat() {
        return this;
    }
}