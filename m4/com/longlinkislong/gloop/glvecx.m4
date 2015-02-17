m4_divert(-1)
m4_include(`m4/m4utils/fdef.m4')
m4_include(`m4/com/longlinkislong/gloop/glvecx_def.m4')
m4_divert(0)m4_dnl
package com.longlinkislong.gloop;

public abstract class VecT extends BaseT <VecT> {
    public static final int VECTOR_SIZE = VEC_SIZE;
    public static final int VECTOR_WIDTH = VECTOR_SIZE * m4_ifelse(TYPE, `float', 4, 8);

    public final TYPE x() {
        return this.get(Vectors.X);
    }

    public final TYPE y() {
        return this.get(Vectors.Y);
    }

m4_ifelse(m4_eval(VEC_SIZE > 2), 1,m4_dnl 
    `public final TYPE z() {
        return this.get(Vectors.Z);
    }')m4_dnl 

m4_ifelse(m4_eval(VEC_SIZE > 3), 1,m4_dnl
    `public final TYPE w() {
        return this.get(Vectors.W);
    }')m4_dnl 

    @Override
    public final VecT plus(final GLVec other) {
        final VecT out = _next(VEC_SIZE, TYPE);
        final VecT in1 = _cast(_cast(other, BaseT), VecT);

        _call(`plus')(
            out.data(), out.offset(),
            this.data(), this.offset(),
            in1.data(), in1.offset());
        
        return out;
    }

    @Override
    public final VecT minus(final GLVec other) {
        final VecT out = _next(VEC_SIZE, TYPE);
        final VecT in1 = _cast(_cast(other, BaseT), VecT);

        _call(`minus')(
            out.data(), out.offset(),
            this.data(), this.offset(),
            in1.data(), in1.offset());

        return out;
    }

    _asVec(2)
    _asVec(3)
    _asVec(4)

    @Override
    public final _fdef(`GLVec', `N', TYPE) _fdef(`asGLVec', `N', TYPE)(final int size) {
        final _fdef(`GLVec', `N', TYPE) out = _next(`N', TYPE, size);
        final int length = Math.min(this.size(), size);

        out.zero();
        out.set(this.data(), this.offset(), length);
        return out;
    }

    @Override
    public final TYPE dot(final GLVec other) {
        final VecT in1 = _cast(_cast(other, BaseT), VecT);

        return _call(`dot')(
            this.data(), this.offset(),
            in1.data(), in1.offset());
    }

    @Override
    public final VecT set(final TYPE[] values, final int offset, final int length) {
        if(offset < 0 || length < 0 || length < VEC_SIZE || values.length - offset < length) {
            throw new IndexOutOfBoundsException();
        }

        System.arraycopy(values, offset, this.data(), this.offset(), length);
        return this;
    }

    @Override
    public final VecT set(final int index, final TYPE value) {
        if(index < 0 || index > this.size()) {
            throw new IndexOutOfBoundsException();
        }

        this.data()[this.offset() + index] = value;
        return this;
    }

    @Override
    public final TYPE get(final int index) {
        if(index < 0 || index > this.size()) {
            throw new IndexOutOfBoundsException();
        }

        return this.data()[this.offset() + index];
    }

    @Override
    public final VecT scale(final TYPE value) {
        final VecT out = _next(VEC_SIZE, TYPE);

        _call(`scale')(
            out.data(), out.offset(),
            this.data(), this.offset(),
            value);

        return out;
    }

    @Override
    public final _fdef(`GLVec',,OTHER) _fdef(`asGLVec',,OTHER)() {
        final _fdef(`GLVec', VEC_SIZE, OTHER) out = _next(VEC_SIZE, OTHER);

        for(int i = 0; i < VEC_SIZE; i++) {
            out.set(i, (OTHER) this.get(i));
        }

        return out;
    }

    @Override
    public final double length() {
        return _call(`length')(this.data(), this.offset());
    }

    @Override
    public final int size() {
        return VECTOR_SIZE;
    }

    @Override
    public final VecT negative() {
        final VecT out = _next(VEC_SIZE, TYPE);

        _call(`negative')(
            out.data(), out.offset(),
            this.data(), this.offset());

        return out;
    }

    @Override
    public final VecT reflect(final GLVec surfaceNormal) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public final VecT inverse() {
        final VecT out = _next(VEC_SIZE, TYPE);
            
        _call(`inverse')(
            out.data(), out.offset(),
            this.data(), this.offset());

        return out;
    }

    @Override
    public VecT asStaticVec() {
        return new _fdef(`StaticVec', VEC_SIZE, TYPE)(
            this.getFactory(), this);
    }

    @Override
    public final VecT zero() {
        System.arraycopy(
            _fdef(`Vectors.', `NULL_VECTOR', TYPE), 0, 
            this.data(), this.offset(), VEC_SIZE);

        return this;
    }

    @Override
    public final VecT cross(final GLVec other) {
        final VecT out = _next(VEC_SIZE, TYPE);
        final VecT in1 = _cast(_cast(other, BaseT), VecT);

        _call(`cross')(
            out.data(), out.offset(),
            this.data(), this.offset(),
            in1.data(), in1.offset());

        return out;
    }
}