m4_divert(-1)
m4_include(`m4/m4utils/fdef.m4')
m4_include(`m4/com/longlinkislong/gloop/mappedvecx_def.m4')
m4_divert(0)m4_dnl 
package com.longlinkislong.gloop;

public class VecT extends BaseT implements MappedVec<VecT> {
    private final TYPE[] data;
    private final int length;
    private final VectorFactory vf;
    private final int baseOffset;
    private int offset;

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
            throw new IndexOutOfBoundsException();
        }

        this.offset = offset;
        return this;
    }
}