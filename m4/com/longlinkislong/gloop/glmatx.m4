m4_divert(-1)
m4_include(`m4/m4utils/fdef.m4')
m4_include(`m4/m4utils/types.m4')
m4_include(`m4/com/longlinkislong/gloop/glmatx_def.m4')
m4_divert(0)m4_dnl
package com.longlinkislong.gloop;

public abstract class MatT extends BaseT<MatT> {
    public static final int MATRIX_SIZE = MAT_SIZE;
    public static final int MATRIX_WIDTH = (MAT_SIZE * MAT_SIZE) * m4_ifelse(TYPE, `float', 4, 8);
    @Override
    public final _fdef(`GLMat',,OTHER) _fdef(`asGLMat',,OTHER) (){
        final _fdef(`GLMat',,OTHER) out = _next(MAT_SIZE, OTHER);

        for(int i = 0; i < this.size(); i++) {
            for(int j = 0; j < this.size(); j++) {
                out.set(i, (OTHER) this.get(i, j));
            }
        }

        return out;
    }

    _asMat(2)
    _asMat(3)
    _asMat(4)

    @Override
    public final _fdef(`GLMat', `N', TYPE) _fdef(`asGLMat', `N', TYPE)(final int size) {
        final _fdef(`GLMat', `N', TYPE) out = _next(`N', TYPE, size);
        final int length = Math.min(this.size(), size);

        out.zero();
        out.set(0, 0, this.data(), this.offset(), length * length, length);
        return out;
    }

    @Override
    public final TYPE get(final int i, final int j) {
        final int index = this.offset() + i + j * this.size();

        return this.data()[index];
    }

    @Override
    public final MatT set(final int i, final int j, final TYPE value) {
        final int index = this.offset() + i + j * this.size();

        this.data()[index] = value;
        return this;
    }

    @Override
    public final MatT set(
        final int i, final int j,
        final TYPE[] data, final int offset, final int length,
        final int stride) {

        final int scanlineSize = this.size();
        int yOff = i + j * this.size();
        int off = offset;

        for(int yStart = 0; yStart < length; yStart++) {
            System.arraycopy(data, off, this.data(), yOff, length);
            off += length;
            yOff += scanlineSize;
        }

        return this;
    }

    @Override
    public final MatT scale(final TYPE value) {
        final MatT out = _next(MAT_SIZE, TYPE);

        _call(`scale')(
            out.data(), out.offset(),
            this.data(), this.offset(),
            value);

        return out;
    }

    @Override
    public final MatT transpose() {
        final MatT out = _next(MAT_SIZE, TYPE);

        _call(`transpose')(
            out.data(), out.offset(),
            this.data(), this.offset());

        return out;
    }

    @Override
    public final MatT inverse() {
        final MatT out = _next(MAT_SIZE, TYPE);

        _call(`inverse')(
            out.data(), out.offset(),
            this.data(), this.offset());

        return out;
    }

    @Override
    public final TYPE determinant() {
        return _call(`determinant')(this.data(), this.offset());
    }

    @Override
    public final MatT multiply(GLMat other) {
        final MatT out = _next(MAT_SIZE, TYPE);
        final MatT in1 = _cast(_cast(other, BaseT), MatT);

        _call(`multiplyMat')(
            out.data(), out.offset(),
            this.data(), this.offset(),
            in1.data(), in1.offset());

        return out;
    }

    @Override
    public final MatT zero() {
        System.arraycopy(
            _fdef(`Matrices.', `NULL_MATRIX', TYPE), 0,
            this.data(), this.offset(), MAT_SIZE * MAT_SIZE);

        return this;
    }
    
    @Override
    public final int size() {
        return MATRIX_SIZE;
    }

    @Override
    public MatT asStaticMat() {
        return new _fdef(`StaticMat', MAT_SIZE, TYPE)(
            this.getFactory(), this);
    }
}