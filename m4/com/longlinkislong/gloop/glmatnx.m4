m4_divert(-1)
m4_include(`m4/m4utils/fdef.m4')
m4_include(`m4/m4utils/types.m4')
m4_include(`m4/com/longlinkislong/gloop/glmatnx_def.m4')
m4_divert(0)m4_dnl 
package com.longlinkislong.gloop;

/**
 * The base class for all arbitrary sized matrices.
 * @author zmichaels
 * @since 15.02.27
 */
public abstract class MatT extends BaseT<MatT, VecT> {

    /**
     * Creates a new identity matrix.
     * @return the identity matrix
     * @since 15.02.27
     */
    public static MatT create(final int size) {
        return Matrices.DEFAULT_FACTORY._fdef(`nextGLMat', `N', TYPE)(size).identity();
    }

    /**
     * Creates a new translation matrix with the specified values.
     * @param size the number of rows and columns 
     * @param data the values to set the translation elements to
     * @param offset the offset to start reading the data   
     * @param length the number of elements to read
     * @return the translation matrix
     * @since 15.02.27
     */
    public static MatT translate(
        final int size,
        final TYPE[] data, final int offset, final int length) {

        final MatT out = create(size);
        final int off = out.offset() + (size * size - size);

        System.arraycopy(data, offset, out.data(), off, length);

        return out;
    }

    /**
     * Creates a new translation matrix with the specified values.
     * @param size the number of rows and columns
     * @param values the values to set the translation elements to.
     * @return the translation matrix
     * @since 15.02.27
     */
    public static MatT translate(final int size, final TYPE... values) {
        return translate(size, values, 0, values.length);
    }

    /**
     * Creates a new scale matrix with the specified data. All unset values will
     * be set to 1.0.
     * @param size the number of rows and columns
     * @param data the data to use
     * @param offset the offset to start reading the data
     * @param length the number of elements to read
     * @return the scale matrix
     * @since 15.02.27
     */
    public static MatT scale(
        final int size,
        final TYPE[] data, final int offset, final int length) {

        final MatT out = create(size);
        for(int i = 0; i < length; i++) {
            out.set(i, i, data[offset + i]);
        }

        return out;
    }

    /**
     * Creates a new scale matrix with the specified data. All unset values will
     * be set to 1.0.
     * @param size the number of rows and columns
     * @param values the values to set
     * @return the scale matrix
     * @since 15.02.27
     */
    public static MatT scale(final int size, final TYPE... values) {
        return scale(size, values, 0, values.length);
    }

    @Override
    public final _fdef(`GLMat',,OTHER) _fdef(`asGLMat',,OTHER)() {
        final _fdef(`GLMat',,OTHER) out = _next(`N', OTHER, this.size());

        for(int i = 0; i < this.size(); i++) {
            for(int j = 0; j < this.size(); j++) {
                out.set(i, j, (OTHER) this.get(i, j));
            }
        }

        return out;
    }

    _asMat(2)
    _asMat(3)
    _asMat(4)

    @Override
    public final _fdef(`GLMat', `N', TYPE) _fdef(`asGLMat', `N', TYPE)(final int size) {
        final MatT out = _next(`N', TYPE, size);
        final int length = Math.min(this.size(), size);

        out.zero();
        out.set(0, 0, this.data(), this.offset(), length * length, length);
        return out;
    }

    @Override
    public final MatT set(final int i, final int j, final TYPE value) {
        if(i < 0 || j < 0 || i >= this.size() || j >= this.size()) {
            throw new IndexOutOfBoundsException();
        }
    
        final int index = this.offset() + i + this.size() * j;

        this.data()[index] = value;
        return this;
    }

    @Override
    public final TYPE get(final int i, final int j) {
        if(i < 0 || j < 0 || i >= this.size() || j >= this.size()) {
            throw new IndexOutOfBoundsException();
        }

        final int index = this.offset() + i + this.size() * j;

        return this.data()[index];
    }

    @Override
    public final MatT multiply(final GLMat other) {
        final MatT out = _next(`N', TYPE, this.size());
        final MatT in1 = _cast(_cast(other, BaseT), MatT, this.size());

        _call(`multiplyMat')(
            out.data(), out.offset(),
            this.data(), this.offset(),
            in1.data(), in1.offset(),
            this.size());

        return out;
    }

    @Override
    public final _fdef(`GLVec', `N', TYPE) multiply(final GLVec vec) {
        final VecT out = Vectors.DEFAULT_FACTORY._fdef(`nextGLVec',`N',TYPE)(this.size());
        final VecT in1 = vec.m4_ifelse(TYPE,`float',`asGLVecF',`asGLVecD')().`ex'VecT (this.size());

        _call(`multiplyVec')(
            out.data(), out.offset(),
            this.data(), this.offset(),
            in1.data(), in1.offset(),
            this.size());

        return out;
    }
    
    @Override
    public final MatT zero() {
        for(int i = 0; i < (this.size() * this.size()); i++) {
            this.data()[this.offset() + i] = _real(TYPE, 0.0);
        }

        return this;
    }

    @Override
    public final MatT identity() {
        for(int i = 0; i < this.size(); i++) {
            for(int j = 0; j < this.size(); j++) {
                this.set(i, j, (i == j) ? _real(TYPE, 1.0) : _real(TYPE, 0.0));
            }
        }

        return this;
    }

    @Override
    public final MatT inverse() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final MatT transpose() {
        final MatT out = _next(`N', TYPE, this.size());

        _call(`transpose')(
            out.data(), out.offset(),
            this.data(), this.offset(),
            this.size());

        return out;
    }

    @Override
    public final MatT multiply(final TYPE value) {
        final MatT out = _next(`N', TYPE, this.size());

        _call(`scale')(
            out.data(), out.offset(),
            this.data(), this.offset(),
            value, this.size());

        return out;
    }

    @Override
    public TYPE determinant() {
        throw new UnsupportedOperationException();
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
    public MatT asStaticMat() {
        return new _fdef(`StaticMat',`N',TYPE)(
            this.getFactory(), this.size(), this);
    }
}