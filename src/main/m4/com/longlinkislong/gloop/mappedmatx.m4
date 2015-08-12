m4_divert(-1)
m4_include(`m4/m4utils/fdef.m4')
m4_include(`m4/com/longlinkislong/gloop/mappedmatx_def.m4')
m4_divert(0)m4_dnl 
package com.longlinkislong.gloop;

/**
 * A MAT_SIZE`x'MAT_SIZE TYPE matrix which references an outside TYPE array for 
 * matrix values.
 *
 * @author zmichaels
 * @since 15.02.27
 */
public class MatT extends BaseT implements MappedMat<MatT> {
    private final TYPE[] data;
    private final int length;
    private final MatrixFactory mf;
    private final int baseOffset;
    private int offset;

    /**
     * Wraps an array as a MAT_SIZE`x'MAT_SIZE matrix.
     *
     * @param mf the MatrixFactory used to allocate all child matrices.
     * @param data the array to reference for matrix values
     * @param offset the position inside the array to start the matrix.
     * @param length the maximum number of elements the matrix can access from the array.
     * @since 15.02.27
     */
    public MatT (
        final MatrixFactory mf,
        final TYPE[] data, final int offset, final int length) {

        this.data = data;
        this.baseOffset = this.offset = offset;
        this.length = length;
        this.mf = mf;
    }

    @Override
    protected final TYPE[] data() {
        return this.data;
    }

    @Override
    protected final int offset() {
        return this.offset;
    }

    @Override
    public final MatrixFactory getFactory() {
        return this.mf;
    }

    @Override
    public final MatT shift(final int shiftOffset) {
        return this.remap(this.offset + shiftOffset);
    }

    @Override
    public final MatT push() {
        return this.shift(m4_eval(MAT_SIZE * MAT_SIZE));
    }

    @Override
    public final MatT pop() {
        return this.shift(-m4_eval(MAT_SIZE * MAT_SIZE));
    }

    @Override
    public final MatT remap(final int offset) {
        if(offset < this.baseOffset || offset > (this.baseOffset + this.length)) {
            throw new IndexOutOfBoundsException(
                String.format("Tried %d on range [%d, %d]", 
                    offset, 
                    this.baseOffset, 
                    (this.baseOffset + length)));
        }

        this.offset = offset;
        return this;
    }
}