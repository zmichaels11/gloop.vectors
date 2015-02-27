m4_divert(-1)
m4_include(`m4/m4utils/fdef.m4')
m4_include(`m4/com/longlinkislong/gloop/mappedmatnx_def.m4')
m4_divert(0)m4_dnl 
package com.longlinkislong.gloop;

/**
 * An arbitrary sized TYPE matrix which references an outside TYPE array for 
 * matrix values.
 *
 * @author zmichaels
 * @since 15.02.27
 */
public class MatT extends BaseT implements MappedMat<MatT> {
    private final TYPE[] data;
    private final MatrixFactory mf;
    private int offset;
    private final int length;
    private final int baseOffset;
    private final int matrixSize;

    /**
     * Wraps an array as a matrix.
     *
     * @param mf the MatrixFactory used to allocate all child matrices.
     * @param data the array to reference for matrix elements
     * @param offset the position of the array to start the matrix
     * @param length the number of elements the matrix can access from the array.
     * @param matrixSize the number of columns and rows
     * @since 15.02.27
     */
    public MatT (
        final MatrixFactory mf,
        final TYPE[] data, final int offset, final int length,
        final int matrixSize) {

        this.mf = mf;
        this.data = data;
        this.baseOffset = this.offset = offset;
        this.length = length;
        this.matrixSize = matrixSize;
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
    public final int size() {
        return this.matrixSize;
    }

    @Override
    public final MatT shift(final int shiftOffset) {
        return this.remap(this.offset + shiftOffset);
    }

    @Override
    public final MatT push() {
        return this.shift(this.matrixSize * this.matrixSize);
    }

    @Override
    public final MatT pop() {
        return this.shift(-this.matrixSize * this.matrixSize);
    }

    @Override
    public final MatT remap(final int offset) {
        if(offset < this.baseOffset || offset > (this.baseOffset + this.length)) {
            throw new IndexOutOfBoundsException();  
        }

        this.offset = offset;
        return this;
    }
}