m4_divert(-1)
m4_include(`m4/m4utils/fdef.m4')
m4_include(`m4/m4utils/types.m4')
m4_include(`m4/com/longlinkislong/gloop/glmatx_def.m4')
m4_divert(0)m4_dnl
package com.longlinkislong.gloop;

public abstract class MatT extends BaseT<MatT, VecT> {
    public static final int MATRIX_SIZE = MAT_SIZE;
    public static final int MATRIX_WIDTH = (MAT_SIZE * MAT_SIZE) * m4_ifelse(TYPE, `float', 4, 8);    

    public static MatT create() {
        return Matrices.DEFAULT_FACTORY._fdef(`nextGLMat', MAT_SIZE, TYPE)().identity();
    }

    public static MatT translate(final TYPE... values) {
        return translate(values, 0, values.length);
    }

    public static MatT translate(final TYPE[] data, final int offset, final int length) {
        final MatT out = create();
        final int off = out.offset() + MAT_SIZE * MAT_SIZE - MAT_SIZE;
        
        for(int i = offset; i < data.length; i++) {
            out.data()[off + i] = data[i];
        }

        return out;
    }

m4_ifelse(MAT_SIZE, 2,, `m4_dnl 
    public static MatT rotateZ(final TYPE angle) {
        final TYPE sa = (TYPE) Math.sin(angle);
        final TYPE ca = (TYPE) Math.cos(angle);
        
        return create()
            .set(0, 0, ca).set(1,0,sa)
            .set(0, 1, -sa).set(1,1,ca);
    }')

m4_ifelse(MAT_SIZE,4,`m4_dnl
    public static MatT rotateX(final TYPE angle) {
        final TYPE sa = (TYPE) Math.sin(angle);
        final TYPE ca = (TYPE) Math.cos(angle);

        return create()
            .set(1, 1, ca).set(2, 1, sa)
            .set(0, 1, -sa).set(2, 2, ca);
    }
    
    public static MatT rotateY(final TYPE angle) {
        final TYPE sa = (TYPE) Math.sin(angle);
        final TYPE ca = (TYPE) Math.cos(angle);

        return create()
            .set(0, 0, ca).set(2, 0, -sa)
            .set(0, 2, sa).set(2, 2, ca);
    }')

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
        final int index = this.offset() + i * this.size() + j;

        return this.data()[index];
    }

    @Override
    public final MatT set(final int i, final int j, final TYPE value) {
        final int index = this.offset() + i * this.size() + j;

        this.data()[index] = value;
        return this;
    }

    @Override
    public final MatT set(
        final int i, final int j,
        final TYPE[] data, final int offset, final int length,
        final int stride) {

        final int scanlineSize = this.size();
        int yOff = this.offset() + i * this.size() + j;
        int off = offset;

        for(int yStart = 0; yStart < this.size(); yStart++) {
            System.arraycopy(data, off, this.data(), yOff, this.size());
            off += stride;
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
    public final VecT multiply(final GLVec vec) {
        final VecT out = Vectors.DEFAULT_FACTORY._fdef(`nextGLVec', MAT_SIZE, TYPE)();
        final VecT in1 = vec.m4_ifelse(TYPE,`float',`asGLVecF',`asGLVecD')().`as'VecT ();

        _call(`multiplyVec')(
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
    public final MatT identity() {
        this.set(0, 0,
            _fdef(`Matrices.', `IDENTITY_MATRIX', TYPE), 0,
            MAT_SIZE * MAT_SIZE, 4);
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