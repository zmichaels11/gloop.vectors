m4_include(`m4/m4utils/forloop.m4')
m4_include(`m4/m4utils/fdef.m4')

m4_define(`_scale', `m4_dnl 
/**
     * Scales each element inside a $1x$1 $2 matrix by a constant value.
     * @param out the output matrix array
     * @param outOffset the offset to the output matrix
     * @param in0 the input matrix array
     * @param in0Offset the offset to the input matrix
     * @param value the constant scalar.
     * @since 15.02.12
     */
    public static void _fdef(`scale', $1, $2) (
        final $2[] out, final int outOffset,
        final $2[] in0, final int in0Offset,
        final $2 value) {

        forloop(`i', 0, m4_eval($1*$1-1), `m4_dnl 
out[outOffset + i] = in0[in0Offset + i] * value;
        ')
    }
')

m4_define(`_scaleN', `m4_dnl
/**
     * Scales each element inside a NxN $1 matrix by a constant value.
     * @param out the output matrix array
     * @param outOffset the offset to the output matrix
     * @param in0 the input matrix array
     * @param in0Offset the offset to the input matrix
     * @param value the constant scalar.
     * @param size the size of the matrix.
     * @since 15.02.26
     */
    public static void _fdef(`scale', `N', $1) (
        final $1[] out, final int outOffset,
        final $1[] in0, final int in0Offset,
        final $1 value,
        final int size) {
        
        for(int i = 0; i < size * size; i++) {
            out[outOffset + i] = in0[in0Offset + i] * value;
        }
    }
')

m4_define(`_index', `m4_eval($2 * $3 + $1)')

m4_define(`_matmult', `m4_dnl
/**
     * Multiplies two $1x$1 $2 matrices together and stores the results inside 
     * another $1x$1 $2 matrix.
     * @param out the output matrix array
     * @param outOffset the offset to the output matrix
     * @param in0 the first input matrix array
     * @param in0Offset the offset to the first input matrix
     * @param in1 the second input matrix array
     * @param in1Offset the offset to the second input matrix
     * @since 15.02.12
     */
public static void _fdef(`multiplyMat', $1, $2) (
        final $2[] out, final int outOffset,
        final $2[] in0, final int in0Offset,
        final $2[] in1, final int in1Offset) {

        forloop(`i', 0, m4_eval($1-1), `m4_dnl
forloop(`j', 0, m4_eval($1-1), `m4_dnl
out[outOffset + _index(i, j, $1)] = m4_dnl
forloop(`k', 0, m4_eval($1-1), `m4_dnl
in0[in0Offset + _index(k,j, $1)] * in1[in1Offset + _index(i, k, $1)] + ')0;
        ')')
    }
')

m4_define(`_matmultN', `m4_dnl
/**
     * Multiplies two NxN $1 matrices together and stores the results inside
     * another NxN $1 matrix.
     * @param out the output matrix array
     * @param outOffset hte offset to the output matrix
     * @param in0 the first input matrix array
     * @param in0Offset the offset to the first matrix
     * @param in1 the second input matrix array
     * @param in1Offset the offset to the second matrix
     * @param size the size of the matrix
     * @since 15.02.26
     */
public static void _fdef(`multiplyMat', `N', $1) (
        final $1[] out, final int outOffset,
        final $1[] in0, final int in0Offset,
        final $1[] in1, final int in1Offset,
        final int size) {

        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                out[outOffset + (j*size+i)] = 0;
                for(int k = 0; k < size; k++) {
                    out[outOffset + (j*size+i)] += in0[in0Offset + (j*size+k)] * in1[in1Offset + (k*size+i)];
                }
            }
        }
    }
')

m4_define(`_vecmult', `m4_dnl 
/**
     * Multiplies a $1x$1 $2 matrix by a $1x1 $2 vector and stores the results in 
     * another $2 vector.
     * @param out the output vector array
     * @param outOffset the offset to the output vector
     * @param in0 the input matrix array
     * @param in0Offset the offset to the input matrix
     * @param in1 the input vector array
     * @param in1Offset the offset to the input vector
     * @since 15.02.12
     */
    public static void _fdef(`multiplyVec', $1, $2) (
        final $2[] out, final int outOffset,
        final $2[] in0, final int in0Offset,
        final $2[] in1, final int in1Offset) {

        forloop(`i', 0, m4_eval($1-1), `m4_dnl 
out[outOffset + i] = m4_dnl 
forloop(`j', 0, m4_eval($1-1), `m4_dnl 
in0[in0Offset + _index(i, j, $1)] * in1[in1Offset + j] + ')0;
        ')
    }
')

m4_define(`_vecmultN', `m4_dnl
/**
     * Multiplies a NxN $1 matrix by a Nx1 vector and stores the results in
     * another Nx1 $1 vector.
     * @param out the output vector array
     * @param outOffset the offset to the output vector
     * @param in0 the input matrix array
     * @param in0Offset the offset to the input matrix
     * @param in1 the input vector array
     * @param in1Offset the offset to the input vector
     * @param size the size of the matrix
     * @since 15.02.26
     */
    public static void _fdef(`multiplyVec', `N', $1) (
        final $1[] out, final int outOffset,
        final $1[] in0, final int in0Offset,
        final $1[] in1, final int in1Offset,
        final int size) {

        for(int i = 0; i < size; i++) {
            out[outOffset + i] = 0;
            for(int j = 0; j < size; j++) {
                out[outOffset + j] += in0[in0Offset + (j*size+i)] * in1[in1Offset + j];
            }
        }
    }
')

m4_define(`_det2', `m4_dnl
/**
    * Calculates the determinant of a 2x2 $1 matrix
    * @param in the input matrix array
    * @param off the offset to the input matrix
    * @return the determinant
    * @since 15.02.12
    */
    public static $1 _fdef(`determinant', 2, $1) (
        final $1[] in, final int off) {

        return in[off + _index(0,0,2)] * in[off + _index(1,1,2)] - in[off + _index(0,1,2)] * in[off + _index(1,0,2)];
    }
')

m4_define(`_det3', `m4_dnl
/**
    * Calculates the determinant of a 3x3 $1 matrix
    * @param in the input matrix array
    * @param off the offset to the input matrix
    * @return the determinant
    * @since 15.02.12
    */
    public static $1 _fdef(`determinant', 3, $1) (
        final $1[] in, final int off) {

        final $1 d0 = in[off + _index(1,1,3)] * in[off + _index(2,2,3)] - in[off + _index(2,1,3)] * in[off + _index(1,2,3)];
        final $1 d1 = in[off + _index(0,1,3)] * in[off + _index(2,2,3)] - in[off + _index(2,1,3)] * in[off + _index(0,2,3)];
        final $1 d2 = in[off + _index(0,1,3)] * in[off + _index(1,2,3)] - in[off + _index(1,1,3)] * in[off + _index(0,2,3)];

        return in[off + _index(0,0,3)] * d0 + in[off + _index(1,0,3)] * d1 + in[off + _index(2,0,3)] * d2;
    }
')

m4_define(`_det4', `m4_dnl 
/**
    * Calculates the determinant of a 4x4 $1 matrix
    * @param in the input matrix array
    * @param off the offset to the input matrix
    * @return the determinant
    * @since 15.02.12
    */
    public static $1 _fdef(`determinant', 4, $1) (
        final $1[] in, final int off) {

        final $1 m12 = in[off + _index(0,1,4)] * in[off + _index(1,2,4)] - in[off + _index(1,1,4)] * in[off + _index(0,2,4)];
        final $1 m13 = in[off + _index(0,1,4)] * in[off + _index(2,2,4)] - in[off + _index(2,1,4)] * in[off + _index(0,2,4)];
        final $1 m14 = in[off + _index(0,1,4)] * in[off + _index(3,2,4)] - in[off + _index(3,1,4)] * in[off + _index(0,2,4)];
        final $1 m23 = in[off + _index(1,1,4)] * in[off + _index(2,2,4)] - in[off + _index(2,1,4)] * in[off + _index(1,2,4)];
        final $1 m24 = in[off + _index(1,1,4)] * in[off + _index(3,2,4)] - in[off + _index(3,1,4)] * in[off + _index(1,2,4)];
        final $1 m34 = in[off + _index(2,1,4)] * in[off + _index(3,2,4)] - in[off + _index(3,1,4)] * in[off + _index(2,2,4)];
        final $1 d1 = in[off + _index(0,1,4)] * m34 - in[off + _index(2,0,4)] * m24 + in[off + _index(3,0,4)] * m23;
        final $1 d2 = in[off + _index(0,0,4)] * m34 - in[off + _index(2,0,4)] * m14 + in[off + _index(3,0,4)] * m13;
        final $1 d3 = in[off + _index(0,0,4)] * m24 - in[off + _index(1,0,4)] * m14 + in[off + _index(3,0,4)] * m12;
        final $1 d4 = in[off + _index(0,0,4)] * m23 - in[off + _index(1,0,4)] * m13 + in[off + _index(2,0,4)] * m12;

        return -in[off + _index(0,3,4)] * d1 + in[off + _index(1,3,4)] * d2 + in[off + _index(2,3,4)] * d3 + in[off + _index(3,3,4)] * d4;
    }
')

m4_define(`_transpose', `m4_dnl 
/**
     * Calculates the transpose of a $1x$1 $2 matrix.
     * @param out the output matrix array
     * @param outOffset the offset to the output matrix
     * @param in0 the first input matrix array
     * @param in0Offset the offset to the first input matrix
     * @since 15.02.13
     */
    public static void _fdef(`transpose', $1, $2) (
        final $2[] out, final int outOffset,
        final $2[] in0, final int in0Offset) {

        forloop(`i', 0, m4_eval($1-1), `m4_dnl 
forloop(`j', 0, m4_eval($1-1), `m4_dnl 
out[outOffset + _index(i, j, $1)] = in0[in0Offset + _index(j, i, $1)];
        ')')
    }
')

m4_define(`_transposeN', `m4_dnl
/**
     * Calculates the transpose of a NxN $1 matrix.
     * @param out the output matrix array
     * @param outOffset the offset to the output matrix
     * @param in0 the input matrix array
     * @param in0Offset the offset to the input matrix
     * @param size the size of the matrix
     * @since 15.02.26
     */
    public static void _fdef(`transpose', `N', $1) (
        final $1[] out, final int outOffset,
        final $1[] in0, final int in0Offset,
        final int size) {

        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                out[outOffset + (j*size+i)] = in0[in0Offset + (i*size+j)];
            }
        }
    }
')

m4_define(`_inverse2', `m4_dnl 
/**
     * Calculates the inverse of a 2x2 $1 matrix.
     * @param out the output matrix array
     * @param outOffset the offset to the output matrix
     * @param in0 the first input matrix
     * @param in0Offset the offset for the first input matrix     
     * @since 15.02.20
     */
    public static void _fdef(`inverse', 2, $1) (
        final $1[] out, final int outOffset,
        final $1[] in0, final int in0Offset) {

        final $1 det = _fdef(`determinant', 2, $1) (in0, in0Offset);
        
        out[outOffset + 0] =  in0[in0Offset + 3] / det;
        out[outOffset + 1] = -in0[in0Offset + 1] / det;
        out[outOffset + 2] = -in0[in0Offset + 2] / det;
        out[outOffset + 3] =  in0[in0Offset + 0] / det;
    }
')

m4_define(`_inverse3', `m4_dnl
/**
     * Calculates the inverse of a 3x3 $1 matrix.
     * @param out the output matrix array
     * @param outOffset the offset to the output matrix
     * @param in0 the first input matrix
     * @param in0Offset the offset for the first matrix
     * @since 15.02.20
     */
    public static void _fdef(`inverse', 3, $1) (
        final $1[] out, final int outOffset,
        final $1[] in0, final int in0Offset) {

        final $1 det = _fdef(`determinant', 3, $1) (in0, in0Offset);

        _fdef(`transpose', 3, $1) (out, outOffset, in0, in0Offset);
forloop(`i', 0, 8, `m4_dnl
        out[outOffset + i] /= det;
')
    }
')

m4_define(`_inverse4', `m4_dnl
/**
     * Calculates the inverse of a 4x4 $1 matrix using Cramers rule.
     * @param out the output matrix array
     * @param outOffset the offset to the output matrix
     * @param in0 the input matrix array
     * @param in0Offset the offset to the input matrix
     * @since 15.02.20
     */
    public static void _fdef(`inverse', 4, $1) (
        final $1[] out, final int outOffset,
        final $1[] in0, final int in0Offset) {

        final $1[] tmp = new $1[12];
        final $1[] src = new $1[16];
        
        _fdef(`transpose', 4, $1) (src, 0, in0, in0Offset);

        // calculates pairs for first 8 elements
        tmp[ 0] = src[10] * src[15];
        tmp[ 1] = src[11] * src[14];
        tmp[ 2] = src[ 9] * src[15];
        tmp[ 3] = src[11] * src[13];
        tmp[ 4] = src[ 9] * src[14];
        tmp[ 5] = src[10] * src[13];
        tmp[ 6] = src[ 8] * src[15];
        tmp[ 7] = src[11] * src[12];
        tmp[ 8] = src[ 8] * src[14];
        tmp[ 9] = src[10] * src[12];
        tmp[10] = src[ 8] * src[13];
        tmp[11] = src[ 9] * src[12];

        // calculates first 8 elements
        out[outOffset + 0]  = tmp[0]*src[5] + tmp[3]*src[6] + tmp[ 4]*src[7];
        out[outOffset + 0] -= tmp[1]*src[5] + tmp[2]*src[6] + tmp[ 5]*src[7];
        out[outOffset + 1]  = tmp[1]*src[4] + tmp[6]*src[6] + tmp[ 9]*src[7];
        out[outOffset + 1] -= tmp[0]*src[4] + tmp[7]*src[6] + tmp[ 8]*src[7];
        out[outOffset + 2]  = tmp[2]*src[4] + tmp[7]*src[5] + tmp[10]*src[7];
        out[outOffset + 2] -= tmp[3]*src[4] + tmp[6]*src[5] + tmp[11]*src[7];
        out[outOffset + 3]  = tmp[5]*src[4] + tmp[8]*src[5] + tmp[11]*src[6];
        out[outOffset + 3] -= tmp[4]*src[4] + tmp[9]*src[5] + tmp[10]*src[6];
        out[outOffset + 4]  = tmp[1]*src[1] + tmp[2]*src[2] + tmp[ 5]*src[3];
        out[outOffset + 4] -= tmp[0]*src[1] + tmp[3]*src[2] + tmp[ 4]*src[3];
        out[outOffset + 5]  = tmp[0]*src[0] + tmp[7]*src[2] + tmp[ 8]*src[3];
        out[outOffset + 5] -= tmp[1]*src[0] + tmp[6]*src[2] + tmp[ 9]*src[3];
        out[outOffset + 6]  = tmp[3]*src[0] + tmp[6]*src[1] + tmp[11]*src[3];
        out[outOffset + 6] -= tmp[2]*src[0] + tmp[7]*src[1] + tmp[10]*src[3];
        out[outOffset + 7]  = tmp[4]*src[0] + tmp[9]*src[1] + tmp[10]*src[2];
        out[outOffset + 7] -= tmp[5]*src[0] + tmp[8]*src[1] + tmp[11]*src[2];

        // calculate pairs for second 8 elements
        tmp[ 0] = src[2]*src[7];
        tmp[ 1] = src[3]*src[6];
        tmp[ 2] = src[1]*src[7];
        tmp[ 3] = src[3]*src[5];
        tmp[ 4] = src[1]*src[6];
        tmp[ 5] = src[2]*src[5];
        tmp[ 6] = src[0]*src[7];
        tmp[ 7] = src[3]*src[4];
        tmp[ 8] = src[0]*src[6];
        tmp[ 9] = src[2]*src[4];
        tmp[10] = src[0]*src[5];
        tmp[11] = src[1]*src[4];

        out[outOffset +  8] =  tmp[ 0]*src[13] + tmp[ 3]*src[14] + tmp[ 4]*src[15];
        out[outOffset +  8] -= tmp[ 1]*src[13] + tmp[ 2]*src[14] + tmp[ 5]*src[15];
        out[outOffset +  9] =  tmp[ 1]*src[12] + tmp[ 6]*src[14] + tmp[ 9]*src[15];
        out[outOffset +  9] -= tmp[ 0]*src[12] + tmp[ 7]*src[14] + tmp[ 8]*src[15];
        out[outOffset + 10] =  tmp[ 2]*src[12] + tmp[ 7]*src[13] + tmp[10]*src[15];
        out[outOffset + 10]-=  tmp[ 3]*src[12] + tmp[ 6]*src[13] + tmp[11]*src[15];
        out[outOffset + 11] =  tmp[ 5]*src[12] + tmp[ 8]*src[13] + tmp[11]*src[14];
        out[outOffset + 11]-=  tmp[ 4]*src[12] + tmp[ 9]*src[13] + tmp[10]*src[14];
        out[outOffset + 12] =  tmp[ 2]*src[10] + tmp[ 5]*src[11] + tmp[ 1]*src[ 9];
        out[outOffset + 12]-=  tmp[ 4]*src[11] + tmp[ 0]*src[ 9] + tmp[ 3]*src[10];
        out[outOffset + 13] =  tmp[ 8]*src[11] + tmp[ 0]*src[ 8] + tmp[ 7]*src[10];
        out[outOffset + 13]-=  tmp[ 6]*src[10] + tmp[ 9]*src[11] + tmp[ 1]*src[ 8];
        out[outOffset + 14] =  tmp[ 6]*src[ 9] + tmp[11]*src[11] + tmp[ 3]*src[ 8];
        out[outOffset + 14]-=  tmp[10]*src[11] + tmp[ 2]*src[ 8] + tmp[ 7]*src[ 9];
        out[outOffset + 15] =  tmp[10]*src[10] + tmp[ 4]*src[ 8] + tmp[ 9]*src[ 9];
        out[outOffset + 15]-=  tmp[ 8]*src[ 9] + tmp[11]*src[10] + tmp[ 5]*src[ 8];

        $1 det = src[0]*out[outOffset + 0]
                +src[1]*out[outOffset + 1]
                +src[2]*out[outOffset + 2]
                +src[3]*out[outOffset + 3];

        det = 1 / det;

        for(int j = 0; j < 16; j++) {
            out[outOffset + j] *= det;
        }
    }
')

m4_define(`_perspective', `m4_dnl
/**
     * Calculates a perspective 4x4 $1 matrix with the given fov, aspect, and 
     * near clipping. Far clipping is effectively infinity.
     *
     * @param out the output matrix array
     * @param outOffset the offset to start writing the array
     * @param fov the field of vision (in degrees)
     * @param aspect the aspect ratio
     * @param near the near clipping value
     * @since 15.02.27
    */
    public static void _fdef(`perspective',4,$1) (
        final $1[] out, final int outOffset,
        $1 fov,
        final $1 aspect,
        final $1 near) {

        fov = ($1) (1.0 / Math.tan(fov * Math.PI / 180.0 * 0.5));
        final $1[] m = {
            fov / aspect, 0, 0, 0,
            0, fov, 0, 0,
            0, 0, 0, -1,
            0, 0, -near, 0
        };

        System.arraycopy(m, 0, out, outOffset, 16);
    }

    /**
     * Calculates a perspective 4x4 $1 matrix with the given fov, aspect, near
     * clipping and far clipping.
     *
     * @param out the output matrix array
     * @param outOffset the offset to the output matrix
     * @param fov the field of vision (in degrees)
     * @param aspect the aspect ratio
     * @param near the near clipping distance
     * @param far the far clipping distance
     * @since 15.02.27
     */
    public static void _fdef(`perspective',4,$1) (
        final $1[] out, final int outOffset,
        $1 fov,
        final $1 aspect,
        final $1 near, final $1 far) {

        fov = ($1) (1.0 / Math.tan(fov * Math.PI / 180 * 0.5));
        final $1[] m = {
            fov / aspect, 0, 0, 0,
            0, fov, 0, 0,
            0, 0, (far + near) / (near - far), -1,
            0, 0, (2 * far * near) / (near - far), 0
        };

        System.arraycopy(m, 0, out, outOffset, 16);
    }
')

m4_define(`_lookat', `m4_dnl 
m4_define(`VecT', _fdef(`GLVec', 3, $1))m4_dnl 
/**
     * Calculates a 4x4 $1 lookat matrix
     *
     * @param out the output matrix array
     * @param outOffset the offset to the output matrix
     * @param eye the position of the camera
     * @param eyeOffset the offset to start reading values from eye
     * @param center the position the camera is looking at
     * @param centerOffset the offset to start reading values from center
     * @param up the direction that is considered up.
     * @param upOffset the offset to start reading values from up.
     */
    public static void _fdef(`lookat',4,$1) (
        final $1[] out, final int outOffset,
        final VecT eye, final VecT center, final VecT up) {

        final VecT z = eye.multiply(center).normalize();
        final VecT x = up.multiply(z).normalize();
        final VecT y = z.multiply(x);

        final $1[] m = {
                x.x(), y.x(), z.x(), 0,
                x.y(), y.y(), z.y(), 0,
                x.z(), y.z(), z.z(), 0,
                0, 0, 0, 1};

        System.arraycopy(m, 0, out, outOffset, 16);
        
        final VecT v = eye.negative();

        out[outOffset + E41] += v.x() * out[outOffset + E11] + v.y() * out[outOffset + E21] + v.z() * out[outOffset + E31];
        out[outOffset + E42] += v.x() * out[outOffset + E12] + v.y() * out[outOffset + E22] + v.z() * out[outOffset + E32];
        out[outOffset + E43] += v.x() * out[outOffset + E13] + v.y() * out[outOffset + E23] + v.z() * out[outOffset + E33];
    }
')

m4_define(`_ortho', `m4_dnl
/**
     * Calculates an ortho matrix.
     * @param out the output matrix array
     * @param outOffset the offset to the output matrix
     * @param left the left
     * @param right the right
     * @param bottom bottom
     * @param top top
     * @param near near clipping
     * @param far far clipping
     * @since 15.02.27
     */
    public static void _fdef(`ortho',4,$1) (
        final $1[] out, final int outOffset,
        final $1 left, final $1 right, final $1 bottom, final $1 top,
        final $1 near, final $1 far) {

        final $1[] m = {
            2 / (right - left), 0, 0, 0,
            0, 2 / (top - bottom), 0, 0,
            0, 0, -2 / (far - near), 0,
            -(right + left) / (right - left), -(top + bottom) / (top - bottom), -(far + near) / (far - near), 1
        };

        System.arraycopy(m, 0, out, outOffset, 16);
    }
')