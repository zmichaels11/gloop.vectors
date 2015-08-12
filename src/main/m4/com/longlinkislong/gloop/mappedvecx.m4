m4_divert(-1)
m4_include(`m4/m4utils/fdef.m4')
m4_include(`m4/com/longlinkislong/gloop/mappedvecx_def.m4')
m4_divert(0)m4_dnl 
package com.longlinkislong.gloop;

/**
 * A VEC_SIZE`D' TYPE vector that references an outside array for vector elements.
 *
 * @author zmichaels
 * @since 15.02.27
 */
public class VecT extends BaseT implements MappedVec<VecT> {
    private final TYPE[] data;
    private final int length;
    private final VectorFactory vf;
    private final int baseOffset;
    private int offset;

    /**
     * Wraps an array as a VecT.
     *
     * @param vf the VectorFactory used to allocate the child vectors.
     * @param data the array to reference for vector elements
     * @param offset the offset to start the vector
     * @param length the number of elements the vector can access from the array.
     * @since 15.02.27
     */
    public VecT (
        final VectorFactory vf,
        final TYPE[] data, final int offset, final int length) {

        this.data = data;
        this.baseOffset = this.offset = offset;
        this.length = length;
        this.vf = vf;        
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
    public final VecT shift(final int shiftOffset) {
        return this.remap(this.offset + shiftOffset);
    }

    @Override
    public final VecT push() {
        return this.shift(VEC_SIZE);
    }

    @Override
    public final VecT pop() {
        return this.shift(-VEC_SIZE);
    }

    @Override
    public final VecT remap(final int offset) {
        if(offset < this.baseOffset || offset > (this.baseOffset + this.length)) {
            throw new IndexOutOfBoundsException(
                String.format("Tried %d on range [%d, %d]", 
                    offset, 
                    this.baseOffset, 
                    (this.baseOffset + this.length)));
        }

        this.offset = offset;
        return this;
    }
}