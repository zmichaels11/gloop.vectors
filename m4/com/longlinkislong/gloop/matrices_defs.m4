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
    public static void _fdef(`_transpose', $1, $2) (
        final $2[] out, final int outOffset,
        final $2[] in0, final int in0Offset) {

        forloop(`i', 0, m4_eval($1-1), `m4_dnl 
forloop(`j', 0, m4_eval($1-1), `m4_dnl 
out[outOffset + _index(i, j, $1)] = in0[in0Offset + _index(j, i, $1)];
        ')')
    }
')