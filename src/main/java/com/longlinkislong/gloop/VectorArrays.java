/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longlinkislong.gloop;

import static java.lang.Math.sqrt;
import static java.lang.Math.abs;
import static java.lang.Math.ceil;
import static java.lang.Math.floor;
import static java.lang.Math.log;
import static java.lang.Math.log10;
import static java.lang.Math.round;

/**
 * VectorArrays is a collection of math algorithms that run on each element in
 * an array.
 *
 * @author zmichaels
 * @since 15.10.23
 */
public final class VectorArrays {

    /**
     * A unary operation for applying a math operation to each element in an
     * array.
     *
     * @param <ArrayT> the type of array.
     * @since 15.10.23
     */
    @FunctionalInterface
    public static interface UnaryOp<ArrayT> {

        /**
         * Applies the unary operation.
         *
         * @param out the array to write the results to.
         * @param outOffset the offset to write to.
         * @param in0 the array to read the inputs from.
         * @param in0Offset the offset to read from.
         * @param count the number of elements to process.
         * @since 15.10.23
         */
        void apply(
                ArrayT out, int outOffset,
                ArrayT in0, int in0Offset,
                int count);
    }

    /**
     * A binary operation for applying a math operation to each element in an
     * array.
     *
     * @param <ArrayT> the type of array.
     * @since 15.10.23
     */
    @FunctionalInterface
    public static interface BinaryOp<ArrayT> {

        /**
         * Applies the binary operation.
         *
         * @param out the array to write the results to.
         * @param outOffset the offset for the write.
         * @param in0 the first array to read from.
         * @param in0Offset the offset of the first input.
         * @param in1 the second array to read from.
         * @param in1Offset the offset of the second input.
         * @param count the number of elements to process.
         * @since 15.10.23
         */
        void apply(
                ArrayT out, int outOffset,
                ArrayT in0, int in0Offset,
                ArrayT in1, int in1Offset,
                int count);
    }

    /**
     * A ternary operation for applying a math operation to each element in an
     * array.
     *
     * @param <ArrayT> the type of array.
     * @since 15.10.23
     */
    @FunctionalInterface
    public static interface TernaryOp<ArrayT> {

        /**
         * Applies the ternary operation.
         *
         * @param out the array to write the results to.
         * @param outOffset the offset for the write.
         * @param in0 the first array to read from.
         * @param in0Offset the offset of the first input.
         * @param in1 the second array to read from.
         * @param in1Offset the offset of the second input.
         * @param in2 the third array to read from.
         * @param in2Offset the offset of the third input.
         * @param count the number of elements to process.
         * @since 15.10.23
         */
        void apply(
                ArrayT out, int outOffset,
                ArrayT in0, int in0Offset,
                ArrayT in1, int in1Offset,
                ArrayT in2, int in2Offset,
                int count);
    }

    private VectorArrays() {
    }

    /**
     * Assigns each element within an array to a specific value. This performs
     * the operation:
     * <code>[r0, r1, r2, ..., rn] = [value, value, value, value]</code>
     *
     * @param out the array to write to.
     * @param outOffset the offset for the write.
     * @param value the value to assign.
     * @param count the number of elements to process.
     * @since 15.10.23
     */
    public static void arraySetF(
            final float[] out, final int outOffset,
            final float value,
            final int count) {

        for (int i = 0; i < count; i++) {
            out[i + outOffset] = value;
        }
    }

    /**
     * Assigns each element within an array to a specific value. This performs
     * the operation:
     * <code>[r0, r1, r2, ..., rn] = [value, value, value, value]</code>
     *
     * @param out the array to write to.
     * @param outOffset the offset for the write.
     * @param value the value to assign.
     * @param count the number of elements to process.
     * @since 15.10.23
     */
    public static void arraySetD(
            final double[] out, final int outOffset,
            final double value,
            final int count) {

        for (int i = 0; i < count; i++) {
            out[i + outOffset] = value;
        }
    }

    /**
     * Adds elements from one array to another. This performs the operation:
     * <code>[r0, r1, r2, ..., rn] = [a0+b0, a1+b1, a2+b2, ..., an+bn]</code>
     *
     * @param out the array to store the results.
     * @param outOffset the offset to begin writing the results.
     * @param in0 the array to read the first input from.
     * @param in0Offset the offset for the first input.
     * @param in1 the array to read the second input from.
     * @param in1Offset the offset for the second input.
     * @param count the number of elements to process.
     * @since 15.10.23
     */
    public static void arrayAddF(
            final float[] out, final int outOffset,
            final float[] in0, final int in0Offset,
            final float[] in1, final int in1Offset,
            final int count) {

        for (int i = 0; i < count; i++) {
            out[outOffset + i] = in0[in0Offset + i] + in1[in1Offset + i];
        }
    }

    /**
     * Adds elements from one array to another. This performs the operation:
     * <code>[r0, r1, r2, ..., rn] = [a0+b0, a1+b1, a2+b2, ..., an+bn]</code>
     *
     * @param out the array to store the results.
     * @param outOffset the offset to begin writing the results.
     * @param in0 the array to read the first input from.
     * @param in0Offset the offset for the first input.
     * @param in1 the array to read the second input from.
     * @param in1Offset the offset for the second input.
     * @param count the number of elements to process.
     * @since 15.10.23
     */
    public static void arrayAddD(
            final double[] out, final int outOffset,
            final double[] in0, final int in0Offset,
            final double[] in1, final int in1Offset,
            final int count) {

        for (int i = 0; i < count; i++) {
            out[outOffset + i] = in0[in0Offset + i] + in1[in1Offset + i];
        }
    }

    /**
     * Subtracts elements from one array by another. This performs the
     * operation:
     * <code>[r0, r1, r2, ..., rn] = [a0-b0, a1-b1, a2-b2, ..., an-bn]</code>
     *
     * @param out the array to store the results.
     * @param outOffset the offset to begin writing the results.
     * @param in0 the array to read the first input from.
     * @param in0Offset the offset for the first input.
     * @param in1 the array to read the second input from.
     * @param in1Offset the offset for the second input.
     * @param count the number of elements to process.
     * @since 15.10.23
     */
    public static void arraySubtractF(
            final float[] out, final int outOffset,
            final float[] in0, final int in0Offset,
            final float[] in1, final int in1Offset,
            final int count) {

        for (int i = 0; i < count; i++) {
            out[outOffset + i] = in0[in0Offset + i] - in1[in1Offset + i];
        }
    }

    /**
     * Subtracts elements from one array by another. This performs the
     * operation:
     * <code>[r0, r1, r2, ..., rn] = [a0-b0, a1-b1, a2-b2, ..., an-bn]</code>
     *
     * @param out the array to store the results.
     * @param outOffset the offset to begin writing the results.
     * @param in0 the array to read the first input from.
     * @param in0Offset the offset for the first input.
     * @param in1 the array to read the second input from.
     * @param in1Offset the offset for the second input.
     * @param count the number of elements to process.
     * @since 15.10.23
     */
    public static void arraySubtractD(
            final double[] out, final int outOffset,
            final double[] in0, final int in0Offset,
            final double[] in1, final int in1Offset,
            final int count) {

        for (int i = 0; i < count; i++) {
            out[outOffset + i] = in0[in0Offset + i] - in1[in1Offset + i];
        }
    }

    /**
     * Multiplies elements from one array by another. This performs the
     * operation:
     * <code>[r0, r1, r2, ..., rn] = [a0*b0, a1*b1,a2*b2, an*bn]</code>
     *
     * @param out the array to store the results.
     * @param outOffset the offset to begin writing the results.
     * @param in0 the array to read the first input from.
     * @param in0Offset the offset for the first input.
     * @param in1 the array to read the second input from.
     * @param in1Offset the offset for the second input.
     * @param count the number of elements to process.
     * @since 15.10.23
     */
    public static void arrayMultiplyF(
            final float[] out, final int outOffset,
            final float[] in0, final int in0Offset,
            final float[] in1, final int in1Offset,
            final int count) {

        for (int i = 0; i < count; i++) {
            out[outOffset + i] = in0[in0Offset + i] * in1[in1Offset + i];
        }
    }

    /**
     * Multiplies elements from one array by another. This performs the
     * operation:
     * <code>[r0, r1, r2, ..., rn] = [a0*b0, a1*b1,a2*b2, an*bn]</code>
     *
     * @param out the array to store the results.
     * @param outOffset the offset to begin writing the results.
     * @param in0 the array to read the first input from.
     * @param in0Offset the offset for the first input.
     * @param in1 the array to read the second input from.
     * @param in1Offset the offset for the second input.
     * @param count the number of elements to process.
     * @since 15.10.23
     */
    public static void arrayMultiplyD(
            final double[] out, final int outOffset,
            final double[] in0, final int in0Offset,
            final double[] in1, final int in1Offset,
            final int count) {

        for (int i = 0; i < count; i++) {
            out[outOffset + i] = in0[in0Offset + i] * in1[in1Offset + i];
        }
    }

    /**
     * Divides elements from one array by another. This performs the operation:
     * <code>[r0, r1, r2, ..., rn] = [a0/b0, a1/b1, a2/b2, an/bn]</code>
     *
     * @param out the array to store the results.
     * @param outOffset the offset to begin writing the results.
     * @param in0 the array to read the first input from.
     * @param in0Offset the offset for the first input.
     * @param in1 the array to read the second input from.
     * @param in1Offset the offset for the second input.
     * @param count the number of elements to process.
     * @since 15.10.23
     */
    public static void arrayDivideF(
            final float[] out, final int outOffset,
            final float[] in0, final int in0Offset,
            final float[] in1, final int in1Offset,
            final int count) {

        for (int i = 0; i < count; i++) {
            out[outOffset + i] = in0[in0Offset + i] / in1[in1Offset + i];
        }
    }

    /**
     * Divides elements from one array by another. This performs the operation:
     * <code>[r0, r1, r2, ..., rn] = [a0/b0, a1/b1, a2/b2, an/bn]</code>
     *
     * @param out the array to store the results.
     * @param outOffset the offset to begin writing the results.
     * @param in0 the array to read the first input from.
     * @param in0Offset the offset for the first input.
     * @param in1 the array to read the second input from.
     * @param in1Offset the offset for the second input.
     * @param count the number of elements to process.
     * @since 15.10.23
     */
    public static void arrayDivideD(
            final double[] out, final int outOffset,
            final double[] in0, final int in0Offset,
            final double[] in1, final int in1Offset,
            final int count) {

        for (int i = 0; i < count; i++) {
            out[outOffset + i] = in0[in0Offset + i] / in1[in1Offset + i];
        }
    }

    /**
     * Computes the square root on each element in an array. This performs the
     * operation:
     * <code>[r0, r1, r2, ..., rn] = [sqrt(a0), sqrt(a1), sqrt(a2), ... sqrt(an)]</code>
     *
     * @param out the array to store the results.
     * @param outOffset the offset to begin writing the results.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading the inputs.
     * @param count the number of elements to process.
     * @since 15.10.23
     */
    public static void arraySqrtF(
            final float[] out, final int outOffset,
            final float[] in0, final int in0Offset,
            final int count) {

        for (int i = 0; i < count; i++) {
            out[outOffset + i] = (float) sqrt(in0[in0Offset + i]);
        }
    }

    /**
     * Computes the square root on each element in an array. This performs the
     * operation:
     * <code>[r0, r1, r2, ..., rn] = [sqrt(a0), sqrt(a1), sqrt(a2), ... sqrt(an)]</code>
     *
     * @param out the array to store the results.
     * @param outOffset the offset to begin writing the results.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading the inputs.
     * @param count the number of elements to process.
     * @since 15.10.23
     */
    public static void arraySqrtD(
            final double[] out, final int outOffset,
            final double[] in0, final int in0Offset,
            final int count) {

        for (int i = 0; i < count; i++) {
            out[outOffset + i] = sqrt(in0[in0Offset + i]);
        }
    }

    /**
     * Calculates the sum of all elements within an array. This performs the
     * operation: <code>sum = a0+a1+a2+...+an</code>
     *
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @return the sum of all elements processed.
     * @since 15.10.23
     */
    public static float arraySumF(
            final float[] in0, final int in0Offset,
            final int count) {

        float accum = 0f;

        for (int i = 0; i < count; i++) {
            accum += in0[i + in0Offset];
        }

        return accum;
    }

    /**
     * Calculates the sum of all elements within an array. This performs the
     * operation: <code>sum = a0+a1+a2+...+an</code>
     *
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @return the sum of all elements processed.
     * @since 15.10.23
     */
    public static double arraySumD(
            final double[] in0, final int in0Offset,
            final int count) {

        double accum = 0f;

        for (int i = 0; i < count; i++) {
            accum += in0[in0Offset + i];
        }

        return accum;
    }

    /**
     * Calculates the average on all elements within an array. This performs the
     * operation: <code>avg = (a0+a1+a2+...+an)/count</code>
     *
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @return the average of all elements processed.
     * @since 15.10.23
     */
    public static float arrayAverageF(
            final float[] in0, final int in0Offset,
            final int count) {

        return arraySumF(in0, in0Offset, count) / count;
    }

    /**
     * Calculates the average on all elements within an array. This performs the
     * operation: <code>avg = (a0+a1+a2+...+an)/count</code>
     *
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @return the average of all elements processed.
     * @since 15.10.23
     */
    public static double arrayAverageD(
            final double[] in0, final int in0Offset,
            final int count) {

        return arraySumD(in0, in0Offset, count) / count;
    }

    /**
     * Calculates the minimum value of an array.
     *
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @return the minimum of all elements processed.
     * @since 15.10.23
     */
    public static float arrayMinF(
            final float[] in0, final int in0Offset,
            final int count) {

        float min = Float.POSITIVE_INFINITY;
        for (int i = 0; i < count; i++) {
            final float test = in0[i + in0Offset];

            min = test < min ? test : min;
        }

        return min;
    }

    /**
     * Calculates the minimum value of an array.
     *
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @return the minimum of all elements processed.
     * @since 15.10.23
     */
    public static double arrayMinD(
            final double[] in0, final int in0Offset,
            final int count) {

        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < count; i++) {
            final double test = in0[in0Offset + i];

            min = test < min ? test : min;
        }

        return min;
    }

    /**
     * Calculates the maximum value of an array.
     *
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @return the minimum of all elements processed.
     * @since 15.10.23
     */
    public static float arrayMaxF(
            final float[] in0, final int in0Offset,
            final int count) {

        float max = Float.NEGATIVE_INFINITY;
        for (int i = 0; i < count; i++) {
            final float test = in0[i + in0Offset];

            max = test > max ? test : max;
        }

        return max;
    }

    /**
     * Calculates the maximum value of an array.
     *
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @return the minimum of all elements processed.
     * @since 15.10.23
     */
    public static double arrayMaxF(
            final double[] in0, final int in0Offset,
            final int count) {

        double max = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < count; i++) {
            final double test = in0[in0Offset + i];

            max = test > max ? test : max;
        }

        return max;
    }

    /**
     * Calculates the absolute value of each element in an array. This performs
     * the operation:
     * <code>[r0, r1, r2, ..., r3] = [abs(a0), abs(a1), abs(a2), ..., abs(an)]</code>
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @since 15.10.23
     */
    public static void arrayAbsF(
            final float[] out, final int outOffset,
            final float[] in0, final int in0Offset,
            final int count) {

        for (int i = 0; i < count; i++) {
            out[i + outOffset] = abs(in0[i + in0Offset]);
        }
    }

    /**
     * Calculates the absolute value of each element in an array. This performs
     * the operation:
     * <code>[r0, r1, r2, ..., r3] = [abs(a0), abs(a1), abs(a2), ..., abs(an)]</code>
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @since 15.10.23
     */
    public static void arrayAbsD(
            final double[] out, final int outOffset,
            final double[] in0, final int in0Offset,
            final int count) {

        for (int i = 0; i < count; i++) {
            out[outOffset + i] = abs(in0[in0Offset + i]);
        }
    }

    /**
     * Performs a compound multiply-add on each element in three arrays. This
     * performs the operation:
     * <code>[r0, r1, r2, ..., r3] = [a0*b0+c0, a1*b1+c1, a2*b2+c2, ..., an*bn+cn]</code>
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the first array to read inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param in1 the second array to read inputs from.
     * @param in1Offset the offset to begin reading inputs.
     * @param in2 the third array to read inputs from.
     * @param in2Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @since 15.10.23
     */
    public static void arrayMultiplyAddF(
            final float[] out, final int outOffset,
            final float[] in0, final int in0Offset,
            final float[] in1, final int in1Offset,
            final float[] in2, final int in2Offset,
            final int count) {

        for (int i = 0; i < count; i++) {
            out[outOffset + i] = in0[in0Offset + i] * in1[in1Offset + i] + in2[in2Offset + i];
        }
    }

    /**
     * Performs a compound multiply-add on each element in three arrays. This
     * performs the operation:
     * <code>[r0, r1, r2, ..., r3] = [a0*b0+c0, a1*b1+c1, a2*b2+c2, ..., an*bn+cn]</code>
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the first array to read inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param in1 the second array to read inputs from.
     * @param in1Offset the offset to begin reading inputs.
     * @param in2 the third array to read inputs from.
     * @param in2Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @since 15.10.23
     */
    public static void arrayMultiplyAddD(
            final double[] out, final int outOffset,
            final double[] in0, final int in0Offset,
            final double[] in1, final int in1Offset,
            final double[] in2, final int in2Offset,
            final int count) {

        for (int i = 0; i < count; i++) {
            out[outOffset + i] = in0[in0Offset + i] * in1[in1Offset + i] + in2[in2Offset + i];
        }
    }

    /**
     * Performs a compound multiply-subtract on each element in three arrays.
     * This performs the operation:
     * <code>[r0, r1, r2, ..., r3] = [a0*b0-c0, a1*b1-c1, a2*b2-c2, ..., an*bn-cn]</code>
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the first array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param in1 the second array to read the inputs from.
     * @param in1Offset the offset to begin reading inputs.
     * @param in2 the third array to read the inputs from.
     * @param in2Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @since 15.10.23
     */
    public static void arrayMultiplySubtractF(
            final float[] out, final int outOffset,
            final float[] in0, final int in0Offset,
            final float[] in1, final int in1Offset,
            final float[] in2, final int in2Offset,
            final int count) {

        for (int i = 0; i < count; i++) {
            out[outOffset + i] = in0[in0Offset + i] * in1[in1Offset + i] - in2[in2Offset + i];
        }
    }

    /**
     * Performs a compound multiply-subtract on each element in three arrays.
     * This performs the operation:
     * <code>[r0, r1, r2, ..., r3] = [a0*b0-c0, a1*b1-c1, a2*b2-c2, ..., an*bn-cn]</code>
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the first array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param in1 the second array to read the inputs from.
     * @param in1Offset the offset to begin reading inputs.
     * @param in2 the third array to read the inputs from.
     * @param in2Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @since 15.10.23
     */
    public static void arrayMultiplySubtractD(
            final double[] out, final int outOffset,
            final double[] in0, final int in0Offset,
            final double[] in1, final int in1Offset,
            final double[] in2, final int in2Offset,
            final int count) {

        for (int i = 0; i < count; i++) {
            out[outOffset + i] = in0[in0Offset + i] * in1[in1Offset + i] - in2[in2Offset + i];
        }
    }

    /**
     * Calculates the negative of each element of an array. This performs the
     * operation: <code>[r0, r1, r2, ..., rn] = [-a0, -a1, -a2, ..., -an]</code>
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @since 15.10.23
     */
    public static void arrayNegateF(
            final float[] out, final int outOffset,
            final float[] in0, final int in0Offset,
            final int count) {

        for (int i = 0; i < count; i++) {
            out[outOffset + i] = -in0[in0Offset + i];
        }
    }

    /**
     * Calculates the negative of each element of an array. This performs the
     * operation: <code>[r0, r1, r2, ..., rn] = [-a0, -a1, -a2, ..., -an]</code>
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @since 15.10.23
     */
    public static void arrayNegateD(
            final double[] out, final int outOffset,
            final double[] in0, final int in0Offset,
            final int count) {

        for (int i = 0; i < count; i++) {
            out[outOffset + i] = -in0[in0Offset + i];
        }
    }

    /**
     * Calculates a scaled array. This performs the operation:
     * <code>[r0, r1, r2, ..., rn] = [a0*s, a1*s, a2*s, ..., an*s]</code>
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param scale the constant to scale each element by.
     * @param count the number of elements to process.
     * @since 15.10.23
     */
    public static void arrayScaleF(
            final float[] out, final int outOffset,
            final float[] in0, final int in0Offset,
            final float scale,
            final int count) {

        for (int i = 0; i < count; i++) {
            out[outOffset + i] = in0[in0Offset + i] * scale;
        }
    }

    /**
     * Calculates a scaled array. This performs the operation:
     * <code>[r0, r1, r2, ..., rn] = [a0*s, a1*s, a2*s, ..., an*s]</code>
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param scale the constant to scale each element by.
     * @param count the number of elements to process.
     * @since 15.10.23
     */
    public static void arrayScaleD(
            final double[] out, final int outOffset,
            final double[] in0, final int in0Offset,
            final double scale,
            final int count) {

        for (int i = 0; i < count; i++) {
            out[outOffset + i] = in0[in0Offset + i] * scale;
        }
    }

    /**
     * Calculates the reciprocal of an array. This performs the operation:
     * <code>[r0, r1, r2, ..., rn] = [1/a0, 1/a1, 1/a2, ..., 1/an]</code>
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @since 15.10.23
     */
    public static void arrayReciprocalF(
            final float[] out, final int outOffset,
            final float[] in0, final int in0Offset,
            final int count) {

        for (int i = 0; i < count; i++) {
            out[outOffset + i] = 1.0f / in0[in0Offset + i];
        }
    }

    /**
     * Calculates the reciprocal of an array. This performs the operation:
     * <code>[r0, r1, r2, ..., rn] = [1/a0, 1/a1, 1/a2, ..., 1/an]</code>
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @since 15.10.23
     */
    public static void arrayReciprocalD(
            final double[] out, final int outOffset,
            final double[] in0, final int in0Offset,
            final int count) {

        for (int i = 0; i < count; i++) {
            out[outOffset + i] = 1.0 / in0[in0Offset + i];
        }
    }

    /**
     * Calculates the inverse square root of an array. This performs the
     * operation:
     * <code>[r0, r1, r2, ..., r3] = [a0^-0.5, a1^-0.5, a2^-0.5, ..., an^-0.5]</code>
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @since 15.10.23
     */
    public static void arrayInverseSqrtF(
            final float[] out, final int outOffset,
            final float[] in0, final int in0Offset,
            final int count) {

        for (int i = 0; i < count; i++) {
            out[outOffset + i] = 1.0f / (float) sqrt(in0[in0Offset + i]);
        }
    }

    /**
     * Calculates the inverse square root of an array. This performs the
     * operation:
     * <code>[r0, r1, r2, ..., r3] = [a0^-0.5, a1^-0.5, a2^-0.5, ..., an^-0.5]</code>
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @since 15.10.23
     */
    public static void arrayInverseSqrtD(
            final double[] out, final int outOffset,
            final double[] in0, final int in0Offset,
            final int count) {

        for (int i = 0; i < count; i++) {
            out[outOffset + i] = 1.0 / sqrt(in0[in0Offset + i]);
        }
    }

    /**
     * Rounds each element in an array down. This performs the operation:
     * <code>[r0, r1, r2, ..., rn] = [floor(a0), floor(a1), floor(a2), ..., floor(an)]</code>
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @since 15.10.23
     */
    public static void arrayFloorF(
            final float[] out, final int outOffset,
            final float[] in0, final int in0Offset,
            final int count) {

        for (int i = 0; i < count; i++) {
            out[outOffset + i] = (float) floor(in0[in0Offset + i]);
        }
    }

    /**
     * Rounds each element in an array down. This performs the operation:
     * <code>[r0, r1, r2, ..., rn] = [floor(a0), floor(a1), floor(a2), ..., floor(an)]</code>
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @since 15.10.23
     */
    public static void arrayFloorD(
            final double[] out, final int outOffset,
            final double[] in0, final int in0Offset,
            final int count) {

        for (int i = 0; i < count; i++) {
            out[outOffset + i] = floor(in0[in0Offset + i]);
        }
    }

    /**
     * Rounds each element in an array up. This performs the operation:
     * <code>[r0, r1, r2, ..., rn] = [ceil(a0), ceil(a1), ceil(a2), ..., ceil(an)]</code>
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @since 15.10.23
     */
    public static void arrayCeilF(
            final float[] out, final int outOffset,
            final float[] in0, final int in0Offset,
            final int count) {

        for (int i = 0; i < count; i++) {
            out[outOffset + i] = (float) ceil(in0[in0Offset + i]);
        }
    }

    /**
     * Rounds each element in an array up. This performs the operation:
     * <code>[r0, r1, r2, ..., rn] = [ceil(a0), ceil(a1), ceil(a2), ..., ceil(an)]</code>
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @since 15.10.23
     */
    public static void arrayCeilD(
            final double[] out, final int outOffset,
            final double[] in0, final int in0Offset,
            final int count) {

        for (int i = 0; i < count; i++) {
            out[outOffset + i] = ceil(in0[in0Offset + i]);
        }
    }

    /**
     * Rounds each element in an array. This performs the operation:
     * <code>[r0, r1, r2, ..., rn] = [round(a0), round(r1), round(r2), ..., round(rn)]</code>
     * where <code>round = fract(in) < 0.5 ? floor(in) : ceil(in)</code>
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @since 15.10.23
     */
    public static void arrayRoundF(
            final float[] out, final int outOffset,
            final float[] in0, final int in0Offset,
            final int count) {

        for (int i = 0; i < count; i++) {
            out[outOffset + i] = round(in0[in0Offset + i]);
        }
    }

    /**
     * Rounds each element in an array. This performs the operation:
     * <code>[r0, r1, r2, ..., rn] = [round(a0), round(r1), round(r2), ..., round(rn)]</code>
     * where <code>round = fract(in) < 0.5 ? floor(in) : ceil(in)</code>
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @since 15.10.23
     */
    public static void arrayRoundD(
            final double[] out, final int outOffset,
            final double[] in0, final int in0Offset,
            final int count) {

        for (int i = 0; i < count; i++) {
            out[outOffset + i] = round(in0[in0Offset + i]);
        }
    }

    /**
     * Calculates the natural log of each element in an array. This performs the
     * operation:
     * <code>[r0, r1, r2, ..., rn] = [log(a0), log(a1), log(a2), ..., log(an)]</code>
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @since 15.10.23
     */
    public static void arrayLogF(
            final float[] out, final int outOffset,
            final float[] in0, final int in0Offset,
            final int count) {

        for (int i = 0; i < count; i++) {
            out[outOffset + i] = (float) log(in0[in0Offset + i]);
        }
    }

    /**
     * Calculates the natural log of each element in an array. This performs the
     * operation:
     * <code>[r0, r1, r2, ..., rn] = [log(a0), log(a1), log(a2), ..., log(an)]</code>
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @since 15.10.23
     */
    public static void arrayLogD(
            final double[] out, final int outOffset,
            final double[] in0, final int in0Offset,
            final int count) {

        for (int i = 0; i < count; i++) {
            out[outOffset + i] = log(in0[in0Offset + i]);
        }
    }

    /**
     * Calculates the base10 log of each element in an array. This performs the
     * operation:
     * <code>[r0, r1, r2, ..., rn] = [log10(a0), log10(a1), log10(a2), ..., log10(an)]</code>
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @since 15.10.23
     */
    public static void arrayLog10F(
            final float[] out, final int outOffset,
            final float[] in0, final int in0Offset,
            final int count) {

        for (int i = 0; i < count; i++) {
            out[outOffset + i] = (float) log10(in0[in0Offset + i]);
        }
    }

    /**
     * Calculates the base10 log of each element in an array. This performs the
     * operation:
     * <code>[r0, r1, r2, ..., rn] = [log10(a0), log10(a1), log10(a2), ..., log10(an)]</code>
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @since 15.10.23
     */
    public static void arrayLog10D(
            final double[] out, final int outOffset,
            final double[] in0, final int in0Offset,
            final int count) {

        for (int i = 0; i < count; i++) {
            out[outOffset + i] = log10(in0[in0Offset + i]);
        }
    }

    /**
     * Copies elements from one array to another.
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @since 15.10.23
     */
    public static void arrayCopyF(
            final float[] out, final int outOffset,
            final float[] in0, final int in0Offset,
            final int count) {

        System.arraycopy(in0, in0Offset, out, outOffset, count);
    }

    /**
     * Copies elements from one array to another.
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @since 15.10.23
     */
    public static void arrayCopyD(
            final double[] out, final int outOffset,
            final double[] in0, final int in0Offset,
            final int count) {

        System.arraycopy(in0, in0Offset, out, outOffset, count);
    }
}
