m4_include(`m4/m4utils/forloop.m4')
m4_include(`m4/m4utils/fdef.m4')
m4_include(`m4/m4utils/types.m4')

m4_define(`_plus', `m4_dnl 
/**
     * Adds two $1x1 $2 vectors and writes the sum to another $1x1 $2 vector.
     * @param out the output vector array.
     * @param outOffset the offset to the output vector.
     * @param in0 the first input vector array.
     * @param in0Offset the offset to the first input vector.
     * @param in1 the second input vector array.
     * @param in1Offset the offset to the second input vector.
     * @since 15.02.13
     */
    public static void _fdef(`plus', $1, $2) (
        final $2[] out, final int outOffset,
        final $2[] in0, final int in0Offset,
        final $2[] in1, final int in1Offset) {

        forloop(`i', 0, m4_eval($1 - 1), `m4_dnl
out[outOffset + i] = in0[in0Offset + i] + in1[in1Offset + i];
        ')
    }
')

m4_define(`_minus', `m4_dnl
/**
     * Subtracts one $1x1 $2 vector from another $1x1 $2 vector and writes the
     * difference to another $1x1 $2 vector.
     * @param out the output vector array.
     * @param outOffset the offset to the output vector.
     * @param in0 the first input vector array.
     * @param in0Offset the offset to the second input vector
     * @param in1 the second input vector array.     
     * @param in1Offset the offset to the second input vector
     * @since 15.02.13     
     */
    public static void _fdef(`minus', $1, $2) (
        final $2[] out, final int outOffset,
        final $2[] in0, final int in0Offset,
        final $2[] in1, final int in1Offset) {

        forloop(`i', 0, m4_eval($1 - 1), `m4_dnl 
out[outOffset + i] = in0[in0Offset + i] - in1[in1Offset + i];
        ')
    }
')

m4_define(`_scale', `m4_dnl
/**
     * Multiplies each element of a $1x1 $2 vector by a scalar value and then
     * writes the result to another $1x1 $2 vector.
     * @param out the output vector array.
     * @param outOffset the offset to the output vector.
     * @param in0 the input vector array.
     * @param in0Offset the offset to the input vector.
     * @param scaleValue the scalar value.
     * @since 15.02.13
     */
    public static void _fdef(`scale', $1, $2) (
        final $2[] out, final int outOffset,
        final $2[] in0, final int in0Offset,
        final $2 scaleValue) {

forloop(`i', 0, m4_eval($1 - 1), `m4_dnl 
        out[outOffset + i] = in0[in0Offset + i] * scaleValue;
        ')
    }
')

m4_define(`_dot', `m4_dnl
/**
     * Calculates the dot product of two $1x1 $2 vectors.
     * @param in0 the first input vector
     * @param in0Offset the offset for the first input vector
     * @param in1 the second input vector
     * @param in1Offset the offset for the second input vector
     * @return the dot product of the two vectors
     * @since 15.02.13
     */
    public static $2 _fdef(`dot', $1, $2) (
        final $2[] in0, final int in0Offset,
        final $2[] in1, final int in1Offset) {

        return m4_dnl 
forloop(`i', 0, m4_eval($1 - 1), `m4_dnl 
in0[in0Offset + i] * in1[in1Offset + i] + ')0;
    }
')

m4_define(`_length', `m4_dnl 
/**
     * Calculates the length of a $1x1 $2 vector.
     * @param in0 the input vector
     * @param in0Offset the offset to the input vector
     * @return the magnitude of the vector.
     * @since 15.02.13
     */
    public static double _fdef(`length', $1, $2) (
        final $2[] in0, final int in0Offset) {

        final $2 d = _fdef(`dot', $1, $2)(in0, in0Offset, in0, in0Offset);

        return Math.sqrt(d * d);
    }
')

m4_define(`_inverse', `m4_dnl 
/**
     * Calculates the inverse of the vector. This is the same as 1.0 / [vector]
     * @param out the output vector array
     * @param outOffset the offset to the output vector
     * @param in0 the input vector array
     * @param in0Offset the offset for the input vector
     * @since 15.02.13
     */
    public static void _fdef(`inverse', $1, $2) (
        final $2[] out, final int outOffset,
        final $2[] in0, final int in0Offset) {

forloop(`i', 0, m4_eval($1 - 1), `m4_dnl 
        out[outOffset + i] = _real($2, 1.0) / in0[in0Offset + i];
')
    }
')

m4_define(`_negate', `m4_dnl 
/**
     * Creates the negative of the supplied $1x1 $2 vector and writes the results
     * to another $1x1 $2 vector. This is the same as [output] = [zero vector] - [input]
     * @param out the output vector array
     * @param outOffset the offset to the output vector
     * @param in0 the input vector array
     * @param in0Offset the offset for the input vector
     * @since 15.02.13
     */
    public static void _fdef(`negative', $1, $2) (
        final $2[] out, final int outOffset,
        final $2[] in0, final int in0Offset) {

forloop(`i', 0, m4_eval($1 - 1), `m4_dnl 
        out[outOffset + i] = -in0[in0Offset + i];
')
    }
')

m4_define(`_cross2', `m4_dnl
/**
     * Calculates the cross product between two 2x1 $1 vectors and returns the
     * result.
     * @param out the output vector
     * @param outOffset offset to the output
     * @param in0 the first input vector array.
     * @param in0Offset the offset to the input vector.
     * @param in1 the second input vector array.
     * @param in1Offset the offset to the second input vector.     
     * @since 15.02.13
     */
    public static void _fdef(`cross', 2, $1) (
        final $1[] out, final int outOffset,
        final $1[] in0, final int in0Offset,
        final $1[] in1, final int in1Offset) {

        out[outOffset] = out[outOffset + 1] = 
            in0[in0Offset + X] * in1[in1Offset + Y] 
            - in0[in0Offset + Y] * in1[in1Offset + X];        
    }
')

m4_define(`_cross3', `m4_dnl
/**
     * Calculates the cross product between two 3x1 $1 vectors and writes the 
     * result in another 3x1 $1 vector.
     * @param out the output vector array.
     * @param outOffset the offset to the output vector.
     * @param in0 the first input vector array.
     * @param in0Offset the offset to the first input vector.
     * @param in1 the second input vector array.
     * @param in1Offset the offset to the second input vector.     
     * @since 15.02.13
     */
    public static void _fdef(`cross', 3, $1) (
        final $1[] out, final int outOffset,
        final $1[] in0, final int in0Offset,
        final $1[] in1, final int in1Offset) {

        out[outOffset + X] = in0[in0Offset + Z] * in1[in1Offset + Y];
        out[outOffset + Y] = in0[in0Offset + X] * in1[in1Offset + Z];
        out[outOffset + Z] = in0[in0Offset + Y] * in1[in1Offset + X];

        out[outOffset + X] = in0[in0Offset + Y] * in1[in1Offset + Z] - out[outOffset + X];
        out[outOffset + Y] = in0[in0Offset + Z] * in1[in1Offset + X] - out[outOffset + Y];
        out[outOffset + Z] = in0[in0Offset + X] * in1[in1Offset + Y] - out[outOffset + Z];
    }
')

m4_define(`_cross4', `m4_dnl
/**
     * Calculates the cross product between two 4x1 $1 vectors by treating them
     * as 3x1 $1 vectors. W will allways be set to 1.0.
     * @param out the output vector array.
     * @param outOffset the offset to the output vector.
     * @param in0 the input vector array
     * @param in0Offset the offset to the input vector
     * @param in1 the second input vector array
     * @param in1Offset the offset to the second inptu vect
     * @since 15.02.17
     */
    public static void _fdef(`cross', 4, $1) (
        final $1[] out, final int outOffset,
        final $1[] in0, final int in0Offset,
        final $1[] in1, final int in1Offset) {

        out[outOffset + X] = in0[in0Offset + Z] * in1[in1Offset + Y];
        out[outOffset + Y] = in0[in0Offset + X] * in1[in1Offset + Z];
        out[outOffset + Z] = in0[in0Offset + Y] * in1[in1Offset + X];

        out[outOffset + X] = in0[in0Offset + Y] * in1[in1Offset + Z] - out[outOffset + X];
        out[outOffset + Y] = in0[in0Offset + Z] * in1[in1Offset + X] - out[outOffset + Y];
        out[outOffset + Z] = in0[in0Offset + X] * in1[in1Offset + Y] - out[outOffset + Z];
        out[outOffset + W] = _real($1, 1.0);
    }
')

m4_define(`_plusN', `m4_dnl
/**
     * Adds two Nx1 $1 vectors and writes the sum to another Nx1 $1 vector.
     * @param out the output vector array.
     * @param outOffset the offset to the output vector.
     * @param in0 the first input vector array.
     * @param in0Offset the offset to the first input vector.
     * @param in1 the second input vector array.
     * @param in1Offset the offset to the second input vector.
     * @param size the length of the vector
     * @since 15.02.13
     */
    public static void _fdef(`plus', N, $1) (
        final $1[] out, final int outOffset,
        final $1[] in0, final int in0Offset,
        final $1[] in1, final int in1Offset,
        final int size) {

        for(int i = 0; i < size; i++) {
            out[outOffset + i] = in0[in0Offset + i] + in1[in1Offset + i];
        }
    }
')

m4_define(`_minusN', `m4_dnl
/**
     * Subtracts one Nx1 $1 vector from another Nx1 $1 vector and writes the
     * difference to another Nx1 $1 vector.
     * @param out the output vector array.
     * @param outOffset the offset to the output vector.
     * @param in0 the first input vector array.
     * @param in0Offset the offset to the second input vector
     * @param in1 the second input vector array.     
     * @param in1Offset the offset to the second input vector
     * @param size the length of the vector
     * @since 15.02.13     
     */
    public static void _fdef(`minus', N, $1) (
        final $1[] out, final int outOffset,
        final $1[] in0, final int in0Offset,
        final $1[] in1, final int in1Offset,
        final int size) {

        for(int i = 0; i < size; i++) {
            out[outOffset + i] = in0[in0Offset + i] - in1[in1Offset + i];
        }
    }
')

m4_define(`_scaleN', `m4_dnl
/**
     * Multiplies each element of a Nx1 $1 vector by a scalar value and then
     * writes the result to another Nx1 $1 vector.
     * @param out the output vector array.
     * @param outOffset the offset to the output vector.
     * @param in0 the input vector array.
     * @param in0Offset the offset to the input vector.
     * @param scaleValue the scalar value.
     * @param size the length of the vector.
     * @since 15.02.13
     */
    public static void _fdef(`scale', N, $1) (
        final $1[] out, final int outOffset,
        final $1[] in0, final int in0Offset,
        final $1 scaleValue,
        final int size) {

        for(int i = 0; i < size; i++) {
            out[outOffset + i] = in0[in0Offset + i] * scaleValue;
        }
    }
')

m4_define(`_dotN', `m4_dnl
/**
     * Calculates the dot product of two Nx1 $1 vectors.
     * @param in0 the first input vector
     * @param in0Offset the offset for the first input vector
     * @param in1 the second input vector
     * @param in1Offset the offset for the second input vector
     * @param size the length of the vector
     * @return the dot product of the two vectors     
     * @since 15.02.13
     */
    public static $1 _fdef(`dot', N, $1) (
        final $1[] in0, final int in0Offset,
        final $1[] in1, final int in1Offset,
        final int size) {

        $1 sum = _real($1, 0.0);
        for(int i = 0; i < size; i++) {
            sum += in0[in0Offset + i] * in1[in1Offset + i];
        }

        return sum;
    }
')

m4_define(`_lengthN', `m4_dnl 
/**
     * Calculates the length of a Nx1 $1 vector.
     * @param in0 the input vector
     * @param in0Offset the offset to the input vector
     * @param size the length of the vector
     * @return the magnitude of the vector.
     * @since 15.02.13
     */
    public static double _fdef(`length', N, $1) (
        final $1[] in0, final int in0Offset,
        final int size) {

        final $1 d = _fdef(`dot', N, $1)(in0, in0Offset, in0, in0Offset, size);

        return Math.sqrt(d * d);
    }
')

m4_define(`_inverseN', `m4_dnl 
/**
     * Calculates the inverse of the vector. This is the same as 1.0 / [vector]
     * @param out the output vector array
     * @param outOffset the offset to the output vector
     * @param in0 the input vector array
     * @param in0Offset the offset for the input vector
     * @param size the length of the vector
     * @since 15.02.13
     */
    public static void _fdef(`inverse', N, $1) (
        final $1[] out, final int outOffset,
        final $1[] in0, final int in0Offset,
        final int size) {

        for(int i = 0; i < size; i++) {
            out[outOffset + i] = _real($1, 1.0) / in0[in0Offset + i];
        }
    }
')

m4_define(`_negateN', `m4_dnl 
/**
     * Creates the negative of the supplied 2x1 $1 vector and writes the results
     * to another 2x1 $1 vector. This is the same as [output] = [zero vector] - [input]
     * @param out the output vector array
     * @param outOffset the offset to the output vector
     * @param in0 the input vector array
     * @param in0Offset the offset for the input vector
     * @param size the length of the vector
     * @since 15.02.13
     */
    public static void _fdef(`negative', N, $1) (
        final $1[] out, final int outOffset,
        final $1[] in0, final int in0Offset,
        final int size) {

        for(int i = 0; i < size; i++) {
            out[outOffset + i] = -in0[in0Offset + i];
        }
    }
')

m4_define(`_crossN', `m4_dnl 
/**
     * Calculates the cross product between two Nx1 vectors.
     * @param out the output vector array
     * @param outOffset the offset for the output vector array
     * @param in0 the first input vector array
     * @param in0Offset the offset to the first input vector
     * @param in1 the second input vector array
     * @param in1Offset the offset to the second input vector
     * @param size the length of the vector
     * @since 15.02.17
     */
    public static void _fdef(`cross', N, $1) (
        final $1[] out, final int outOffset,
        final $1[] in0, final int in0Offset,
        final $1[] in1, final int in1Offset,
        final int size) {

        switch(size) {
            case 2:
                _fdef(`cross', 2, $1)(
                    out, outOffset,
                    in0, in0Offset,
                    in1, in1Offset);
                return;
            case 3:
                _fdef(`cross', 3, $1)(
                    out, outOffset,
                    in0, in0Offset,
                    in1, in1Offset);
                return;
            case 4:
                _fdef(`cross', 4, $1)(
                    out, outOffset,
                    in0, in0Offset,
                    in1, in1Offset);
                return;
            default:
                throw new UnsupportedOperationException();
        }
    }
')    

m4_define(`_mult', `m4_dnl
/**
     * Multiplies each element of a $1x1 vector by another $1x1 vector and stores
     * the results in another $1x1 vector
     * @param out the output vector array
     * @param outOffset the offset to the output vector
     * @param in0 the first input vector array
     * @param in0Offset the offset to the first input vector
     * @param in1 the second input vector array
     * @param in1Offset the offset to teh second input vector
     * @since 15.02.20
     */
    public static void _fdef(`multiply', $1, $2) (
        final $2[] out, final int outOffset,
        final $2[] in0, final int in0Offset,
        final $2[] in1, final int in1Offset) {

forloop(`i', 0, m4_eval($1 - 1), `m4_dnl
        out[outOffset + i] = in0[in0Offset + i] * in1[in1Offset + i];
')
    }
')

m4_define(`_multN', `m4_dnl
/**
     * Multiplies each element of a Nx1 vector by another Nx1 vector and stores
     * the results in another Nx1 vector
     * @param out the output vector array
     * @param outOffset the offset to the output vector
     * @param in0 the first input vector array
     * @param in0Offset the offset to the first input vector
     * @param in1 the second input vector array
     * @param in1Offset the offset to the second input vector
     * @param size the length of the vector
     * @since 15.02.20
     */
    public static void _fdef(`multiply', `N', $1) (
        final $1[] out, final int outOffset,
        final $1[] in0, final int in0Offset,
        final $1[] in1, final int in1Offset,
        final int size) {

        for(int i = 0; i < size; i++) {
            out[outOffset + i] = in0[in0Offset + i] * in1[in1Offset + i];
        }
    }
')

m4_define(`_madd', `m4_dnl
/**
     * Multiplies two $1x1 vectors together then adds a third $1x1 vector. The
     * results are stored in another $1x1 vector.
     * @param out the output vector array
     * @param outOffset the offset to the output vector
     * @param in0 the first input vector array
     * @param in0Offset the offset to the first input vector
     * @param in1 the second input vector array
     * @param in1Offset the offset to the second input vector
     * @param in2 the third input vector array
     * @param in2Offset the offset to the third input vector
     * @since 15.02.20
     */
    public static void _fdef(`multiplyAdd', $1, $2) (
        final $2[] out, final int outOffset,
        final $2[] in0, final int in0Offset,
        final $2[] in1, final int in1Offset,
        final $2[] in2, final int in2Offset) {

forloop(`i', 0, m4_eval($1 - 1), `m4_dnl
        out[outOffset + i] = in0[in0Offset + i] * in1[in1Offset + i] + in2[in2Offset + i];
')
    }
')

m4_define(`_maddN', `m4_dnl
/**
     * Multiplies each element of a Nx1 $1 vector by another $1Nx1 vector and 
     * stores the results in another Nx1 $1 vector
     * @param out the output vector array
     * @param outOffset the offset to the output vector
     * @param in0 the first input vector array
     * @param in0Offset the offset to the first input vector
     * @param in1 the second input vector array
     * @param in1Offset the offset to the second input vector
     * @param in2 the third input vector array
     * @param in2Offset the offset to the third input vector
     * @param size the length of the vector
     * @since 15.02.20
     */
    public static void _fdef(`multiplyAdd', `N', $1) (
        final $1[] out, final int outOffset,
        final $1[] in0, final int in0Offset,
        final $1[] in1, final int in1Offset,
        final $1[] in2, final int in2Offset,
        final int size) {

        for(int i = 0; i < size; i++) {
            out[outOffset + i] = in0[in0Offset + i] * in1[in1Offset + i] + in2[in2Offset + i];
        }
    }
')

m4_define(`_div', `m4_dnl
/**
     * Divides each element of a $1x1 $2 vector by another $1x1 $2 vector and 
     * stores the results in another $1x1 $2 vector.
     * @param out the output vector array
     * @param outOffset the offset to the output vector
     * @param in0 the first input vector array
     * @param in0Offset the offset to the first vector
     * @param in1 the second input vector array
     * @param in1Offset the offset ot the second vector
     * @since 15.02.24
     */
    public static void _fdef(`divide', $1, $2) (
        final $2[] out, final int outOffset,
        final $2[] in0, final int in0Offset,
        final $2[] in1, final int in1Offset) {

forloop(`i', 0, m4_eval($1 - 1), `m4_dnl
        out[outOffset + i] = in0[in0Offset + i] / in1[in1Offset + i];
')
    }
')

m4_define(`_divN', `m4_dnl
/**
     * Divides each element of a Nx1 $1 vector by another Nx1 $1 vector and
     * stores the results in another Nx1 $1 vector.
     * @param out the output vector array
     * @param outOffset the offset to the output vector
     * @param in0 the first input vector array
     * @param in0Offset the offset to the first vector
     * @param in1 the second input vector array
     * @param in1Offset the offfset to the second vector
     * @param size the number of elemenets
     * @since 15.02.24
     */
    public static void _fdef(`divide', `N', $1) (
        final $1[] out, final int outOffset,
        final $1[] in0, final int in0Offset,
        final $1[] in1, final int in1Offset,
        final int size) {

        for(int i = 0; i < size; i++) {
            out[outOffset + i] = in0[in0Offset + i] / in1[in1Offset + i];
        }
    }
')

m4_define(`_sqrt', `m4_dnl
/**
     * Computes the square root on each element of the $1x1 $2 vector and stores
     * the results in another $1x1 $2 vector.
     * @param out the output vector array
     * @param outOffset the offset to the output vector
     * @param in0 the input vector array
     * @param in0Offset the offset to the input vector     
     * @since 15.02.24
     */
    public static void _fdef(`sqrt', $1, $2) (
        final $2[] out, final int outOffset,
        final $2[] in0, final int in0Offset) {

forloop(`i', 0, m4_eval($1 - 1), `m4_dnl
        out[outOffset + i] = ($2) Math.sqrt(in0[in0Offset + i]);
')
    }
')

m4_define(`_sqrtN', `m4_dnl
/**
     * Computes the square root on each element of the Nx1 $1 vector and stores
     * the results in another Nx1 $1 vector.
     * @param out the output vector array
     * @param outOffset the offset to the output vector
     * @param in0 the input vector array
     * @param in0Offset the offset to the input vector
     * @param size the number of elements in the vector.
     * @since 15.02.24
     */
    public static void _fdef(`sqrt', `N', $1) (
        final $1[] out, final int outOffset,
        final $1[] in0, final int in0Offset,
        final int size) {

        for(int i = 0; i < size; i++) {
            out[outOffset + i] = ($1) Math.sqrt(in0[in0Offset + i]);
        }
    }
')

m4_define(`_min', `m4_dnl
/**
     * Computes the minimum of two $1x1 $2 vectors and stores the results in
     * another $1x1 $2 vector.
     * @param out the output vector
     * @param outOffset the offset to the output vector
     * @param in0 the first input vector
     * @param in0Offset the offset to the first input vector
     * @param in1 the second input vector
     * @param in1Offset the offset to the second input vector
     * @since 15.02.24
     */
    public static void _fdef(`min', $1, $2) (
        final $2[] out, final int outOffset,
        final $2[] in0, final int in0Offset,
        final $2[] in1, final int in1Offset) {

forloop(`i', 0, m4_eval($1 - 1), `m4_dnl
        out[outOffset + i] = Math.min(in0[in0Offset + i], in1[in1Offset + i]);
')
    }
')

m4_define(`_max', `m4_dnl
/**
     * Computes the maximum of two $1x1 $2 vectors and stores the results in
     * another $1x1 $2 vector.
     * @param out the output vector
     * @param outOffset the offset to the output vector
     * @param in0 the first input vector
     * @param in0Offset the offset to the first input vector
     * @param in1 the second input vector
     * @param in1Offset the offset to the second input vector
     * @since 15.02.24
     */
    public static void _fdef(`max', $1, $2) (
        final $2[] out, final int outOffset,
        final $2[] in0, final int in0Offset,
        final $2[] in1, final int in1Offset) {

forloop(`i', 0, m4_eval($1 - 1), `m4_dnl
        out[outOffset + i] = Math.max(in0[in0Offset + i], in1[in1Offset + i]);
')
    }
')

m4_define(`_minN', `m4_dnl
/**
     * Computes the minimum of two Nx1 $1 vectors and stores the results in
     * another Nx1 $2 vector.
     * @param out the output vector
     * @param outOffset the offset to the output vector
     * @param in0 the first input vector
     * @param in0Offset the offset to the first input vector
     * @param in1 the second input vector
     * @param in1Offset the offset to the second input vector
     * @param size the number of elements in the vector
     * @since 15.02.24
     */
    public static void _fdef(`min', N, $1) (
        final $1[] out, final int outOffset,
        final $1[] in0, final int in0Offset,
        final $1[] in1, final int in1Offset,
        final int size) {

        for(int i = 0; i < size; i++) {
            out[outOffset + i] = Math.min(in0[in0Offset + i], in1[in1Offset + i]);
        }
    }
')

m4_define(`_maxN', `m4_dnl
/**
     * Computes the maximum of two Nx1 $1 vectors and stores the results in
     * another Nx1 $2 vector.
     * @param out the output vector
     * @param outOffset the offset to the output vector
     * @param in0 the first input vector
     * @param in0Offset the offset to the first input vector
     * @param in1 the second input vector
     * @param in1Offset the offset to the second input vector
     * @param size the number of elements in the vector
     * @since 15.02.24
     */
    public static void _fdef(`max', N, $1) (
        final $1[] out, final int outOffset,
        final $1[] in0, final int in0Offset,
        final $1[] in1, final int in1Offset,
        final int size) {

        for(int i = 0; i < size; i++) {
            out[outOffset + i] = Math.max(in0[in0Offset + i], in1[in1Offset + i]);
        }
    }
')