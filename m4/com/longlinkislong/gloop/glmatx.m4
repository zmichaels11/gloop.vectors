m4_divert(-1)
m4_include(`m4/m4utils/fdef.m4')
m4_include(`m4/m4utils/types.m4')
m4_include(`m4/com/longlinkislong/gloop/glmatx_def.m4')
m4_divert(0)m4_dnl
package com.longlinkislong.gloop;

/**
 * The base class for all matrices with MAT_SIZE elements.
 * @author zmichaels
 * @since 15.02.27
 */
public abstract class MatT extends BaseT<MatT, VecT> {
    /**
     * The size of the matrix
     * @since 15.02.27
     */
    public static final int MATRIX_SIZE = MAT_SIZE;
    /**
     * The number of bytes that the matrix occupies
     * @since 15.02.27
     */
    public static final int MATRIX_WIDTH = m4_eval((MAT_SIZE * MAT_SIZE) * m4_ifelse(TYPE, `float', 4, 8));

    /**
     * Creates a new identity matrix.
     * @return the identity matrix
     * @since 15.02.27
     */
    public static MatT create() {
        return Matrices.DEFAULT_FACTORY._fdef(`nextGLMat', MAT_SIZE, TYPE)().identity();
    }

    /**
     * Creates a new translation matrix with the specified values.
     * @param values the values to set the translation elements to.
     * @return the translation matrix
     * @since 15.02.27
     */
    public static MatT translation(final TYPE... values) {
        return translation(values, 0, values.length);
    }

    /**
     * Creates a new translation matrix with the specified data. This overwrites
     * up to the last MAT_SIZE elements of the identity matrix.
     * @param data the data to read
     * @param offset the offset to start reading the data
     * @param length the number of elements to read
     * @return the translation matrix
     * @since 15.02.27
     */
    public static MatT translation(
        final TYPE[] data, final int offset, final int length) {

        final MatT out = create();
        final int off = out.offset() + m4_eval(MAT_SIZE * MAT_SIZE - MAT_SIZE);
        
        System.arraycopy(data, offset, out.data(), off, length);        

        return out;
    }

    /**
     * Creates a new scale matrix with the specified data. All unset values will
     * be set to 1.0.
     * @param values the values to set the diagonal of the matrix to.
     * @return the scale matrix
     * @since 15.02.27
     */
    public static MatT scale(final TYPE... values) {
        return scale(values, 0, values.length);
    }

    /**
     * Creates a new scale matrix with the specified data. The length must be
     * less than or equal to MATRIX_SIZE. All unset values will be set to 1.0.
     * @param data the data used for the scale matrix.
     * @param offset the offset to start reading the data
     * @param length the number of elements to write.
     * @return the scale matrix.
     * @since 15.02.27
     */
    public static MatT scale(final TYPE[] data, final int offset, final int length) {
        final MatT out = create();

        for(int i = 0; i < length; i++) {
            out.set(i, i, data[offset + i]);
        }

        return out;
    }

m4_ifelse(MAT_SIZE, 4, `m4_dnl
    /**
     * Creates a new ortho matrix.
     * @param left the left boundary of the matrix
     * @param right the right boundary of the matrix
     * @param bottom the bottom boundary of the matrix
     * @param top the top boundary of the matrix
     * @param near the near clip
     * @param far the far clip
     * @return the ortho matrix
     * @since 15.02.27
     */
    public static MatT ortho(
        final TYPE left, final TYPE right, final TYPE bottom, final TYPE top,
        final TYPE near, final TYPE far) {

        final MatT out = create();

        _call(`ortho')(
            out.data(), out.offset(),
            left, right, bottom, top,
            near, far);

        return out.asStaticMat();
    }

    /**
     * Creates a new perspective matrix with near and far clipping.
     *
     * @param fov the field of vision (in degrees)
     * @param aspect the aspect ratio
     * @param near the near clip
     * @param far the far clip
     * @return the perspective matrix
     * @since 15.02.27
     */
    public static MatT perspective(
        final TYPE fov, final TYPE aspect,
        final TYPE near, final TYPE far) {

        final MatT out = create();

        _call(`perspective')(
            out.data(), out.offset(),
            fov, aspect,
            near, far);

        return out.asStaticMat();
    }

    /**
     * Creates a new perspective matrix with near clipping. Far clipping is
     * effectively infinity.
     *
     * @param fov the field of vision (in degrees)
     * @param aspect the aspect ratio
     * @param near the near clip
     * @return the perspective matrix
     * @since 15.02.27
     */
    public static MatT perspective(
        final TYPE fov, final TYPE aspect,
        final TYPE near) {

        final MatT out = create();

        _call(`perspective')(
            out.data(), out.offset(),
            fov, aspect,
            near);

        return out.asStaticMat();
    }

    /**
     * Creates a new lookat matrix.
     *
     * @param eye the position of the camera
     * @param center the point where the camera is looking
     * @param up the relative "up" direction.
     * @return the lookat matrix
     * @since 15.02.27
     */
    public static MatT lookat(final GLVec eye, final GLVec center, final GLVec up) {
        m4_define(`VEC_T', `m4_ifelse(TYPE, `float', `GLVecF', `GLVecD')')m4_dnl 
        m4_define(`VEC3_T', `m4_ifelse(TYPE, `float', `GLVec3F', `GLVec3D')')m4_dnl 
        final VEC3_T e = eye.`as'VEC_T ().`as'VEC3_T ();
        final VEC3_T c = center.`as'VEC_T ().`as'VEC3_T ();
        final VEC3_T u = up.`as'VEC_T ().`as'VEC3_T ();
        final MatT out = create();

        _call(`lookat')(
            out.data(), out.offset(),
            e, c, u);

        return out.asStaticMat();
    }
',`m4_dnl')

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
            .set(1, 2, -sa).set(2, 2, ca);
    }
    
    public static MatT rotateY(final TYPE angle) {
        final TYPE sa = (TYPE) Math.sin(angle);
        final TYPE ca = (TYPE) Math.cos(angle);

        return create()
            .set(0, 0, ca).set(2, 0, -sa)
            .set(0, 2, sa).set(2, 2, ca);
    }',`m4_dnl')

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

    @Override
    public final TYPE get(final int i, final int j) {
        final int index = this.index(i, j);

        return this.data()[index];
    }

    @Override
    public final MatT set(final int i, final int j, final TYPE value) {
        final int index = this.index(i, j);

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
    public final MatT set(final int rowID, final GLVec vec) {
        final _fdef(`GLVec',,TYPE) v = vec.m4_ifelse(TYPE,`float',`asGLVecF',`asGLVecD')();
        final int length = Math.min(this.size(), v.size());

        System.arraycopy(v.data(), v.offset(), this.data(), this.offset() + rowID * MAT_SIZE, length);
        return this;
    }

    @Override
    public final MatT multiply(final TYPE value) {
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
        final VecT in1 = vec.m4_ifelse(TYPE,`float',`asGLVecF',`asGLVecD')().`ex'VecT ();

        _call(`multiplyVec')(
            out.data(), out.offset(),
            this.data(), this.offset(),
            in1.data(), in1.offset());

        return out;
    }

    @Override
    public final VecT get(final int rowId) {
        final VecT out = Vectors.DEFAULT_FACTORY._fdef(`nextGLVec', MAT_SIZE, TYPE)();
        
        out.set(this.data(), this.offset() + rowId * MAT_SIZE, MAT_SIZE);
        return out;
    }

    @Override
    public final VecT map(final int rowId) {
        return new _fdef(`MappedVec', MAT_SIZE, TYPE) (
            Vectors.DEFAULT_FACTORY,
            this.data(),
            this.offset() + rowId * MAT_SIZE,
            MAT_SIZE);
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

    @Override
    public MatT copyTo(final MatrixFactory factory) {
        final MatT out = _fdef(`factory.nextGLMat', MAT_SIZE, TYPE) ();

        out.set(this);
        return out;
    }
}