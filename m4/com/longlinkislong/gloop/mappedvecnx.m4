m4_divert(-1)
m4_include(`m4/m4utils/fdef.m4')
m4_include(`m4/com/longlinkislong/gloop/mappedvecnx_def.m4')
m4_divert(0)m4_dnl
package com.longlinkislong.gloop;

/**
 * An arbitrary sized TYPE vector that references an outside array for its
 * vector data.
 *
 * @author zmichaels
 * @since 15.02.27
 */
public class VecT extends BaseT implements MappedVec<VecT> {
    private final TYPE[] data;
    private final VectorFactory vf;
    private int offset;
    private final int length;
    private final int baseOffset;
    private final int vectorSize;

    /**
     * Wraps an outside array as a VecT.
     *
     * @param vf The VectorFactory used to allocate child vectors.
     * @param data the array to reference for vector elements
     * @param offset the offset to start the vector.
     * @param length the number of elements the vector can access from the array.
     * @param vectorSize the number of elements
     * @since 15.02.27
     */
    public VecT (
        final VectorFactory vf,
        final TYPE[] data, final int offset, final int length,
        final int vectorSize) {
        
        this.vf = vf;
        this.data = data;
        this.baseOffset = this.offset = offset;
        this.length = length;
        this.vectorSize = vectorSize;
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
    public final VectorFactory getFactory() {
        return this.vf;
    }

    @Override
    public final int size() {
        return this.vectorSize;
    }

    @Override
    public final VecT shift(final int shiftOffset) {
        return this.remap(this.offset + shiftOffset);
    }

    @Override
    public final VecT push() {
        return this.shift(this.vectorSize);
    }

    @Override
    public final VecT pop() {
        return this.shift(-this.vectorSize);
    }

    @Override
    public final VecT remap(final int offset) {
        if(offset < this.baseOffset || offset > (this.baseOffset + this.length)) {
            throw new IndexOutOfBoundsException();
        }

        this.offset = offset;
        return this;
    }
}