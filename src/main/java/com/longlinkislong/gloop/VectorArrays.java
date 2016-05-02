/* 
 * Copyright (c) 2015, Zachary Michaels
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.longlinkislong.gloop;

import static java.lang.Math.sqrt;
import static java.lang.Math.abs;
import static java.lang.Math.ceil;
import static java.lang.Math.floor;
import static java.lang.Math.log;
import static java.lang.Math.log10;
import static java.lang.Math.round;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.function.IntConsumer;

/**
 * VectorArrays is a collection of math algorithms that run on each element in
 * an array.
 *
 * @author zmichaels
 * @since 15.10.23
 */
public final class VectorArrays {

    private static ThreadFactory newThreadFactory(final String name) {
        return task -> {
            final SecurityManager s = System.getSecurityManager();
            final ThreadGroup group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            final Thread thread = new Thread(group, task, "Vector Arrays - " + name);
            
            if(!thread.isDaemon()) {
                thread.setDaemon(true);
            }
            
            if(thread.getPriority() != Thread.NORM_PRIORITY) {
                thread.setPriority(Thread.NORM_PRIORITY);
            }
            
            return thread;
        };
    }
    
    static final ExecutorService X_TASKS = Executors.newSingleThreadExecutor(newThreadFactory("X - Tasks"));
    static final ExecutorService Y_TASKS = Executors.newSingleThreadExecutor(newThreadFactory("Y - Tasks"));
    static final ExecutorService Z_TASKS = Executors.newSingleThreadExecutor(newThreadFactory("Z - Tasks"));
    static final ExecutorService W_TASKS = Executors.newSingleThreadExecutor(newThreadFactory("W - Tasks"));    

    public static Future<?> submitToTaskQueueX(final Runnable task) {
        return X_TASKS.submit(task);
    }

    public static Future<?> submitToTaskQueueY(final Runnable task) {
        return Y_TASKS.submit(task);
    }

    public static Future<?> submitToTaskQueueZ(final Runnable task) {
        return Z_TASKS.submit(task);
    }

    public static Future<?> submitToTaskQueueW(final Runnable task) {
        return W_TASKS.submit(task);
    }    

    /**
     * A functional interface representing a test on an index.
     *
     * @since 15.10.27
     */
    public static interface Conditional {

        boolean test(int index);

        default void ifThen(final int index, IntConsumer task) {
            if (this.test(index)) {
                task.accept(index);
            }
        }

        default void ifElse(final int index, final IntConsumer tTask, final IntConsumer fTask) {
            if (this.test(index)) {
                tTask.accept(index);
            } else {
                tTask.accept(index);
            }
        }

        default void ifNot(final int index, final IntConsumer nTask) {
            if (!this.test(index)) {
                nTask.accept(index);
            }
        }
    }

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

    @FunctionalInterface
    public static interface QuaternaryOp<ArrayT> {

        void apply(
                ArrayT out, int outOffset,
                ArrayT in0, int in0Offset,
                ArrayT in1, int in1Offset,
                ArrayT in2, int in2Offset,
                ArrayT in3, int in3Offset,
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
     * Assigns a value if the conditional passes.
     *
     * @param out the array to write to.
     * @param outOffset the offset for the write.
     * @param value the value to assign.
     * @param count the number of elements to process.
     * @param cnd the conditional to test.
     * @since 15.10.27
     */
    public static void arraySetDCnd(
            final double[] out, final int outOffset,
            final double value,
            final int count,
            final Conditional cnd) {

        for (int i = 0; i < count; i++) {
            cnd.ifThen(i, index -> out[outOffset + index] = value);
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
     * Adds elements if the condition passes
     *
     * @param out the array to store the results.
     * @param outOffset the offset to begin writing the results.
     * @param in0 the array to read the first input from.
     * @param in0Offset the offset for the first input.
     * @param in1 the array to read the second input from.
     * @param in1Offset the offset for the second input.
     * @param count the number of elements to process.
     * @param cnd the conditional to test.
     * @since 15.10.28
     */
    public static void arrayAddFCnd(
            final float[] out, final int outOffset,
            final float[] in0, final int in0Offset,
            final float[] in1, final int in1Offset,
            final int count,
            final Conditional cnd) {

        for (int i = 0; i < count; i++) {
            cnd.ifThen(i, index -> out[outOffset + index] = in0[in0Offset + index] + in1[in1Offset + index]);
        }
    }

    /**
     * Assigns a value if the conditional passes.
     *
     * @param out the array to write to.
     * @param outOffset the offset for the write.
     * @param value the value to assign.
     * @param count the number of elements to process.
     * @param cnd the conditional to test.
     * @since 15.10.27
     */
    public static void arraySetFCnd(
            final float[] out, final int outOffset,
            final float value,
            final int count,
            final Conditional cnd) {

        for (int i = 0; i < count; i++) {
            cnd.ifThen(i, index -> out[outOffset + index] = value);
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
     * Adds elements if the condition passes
     *
     * @param out the array to store the results.
     * @param outOffset the offset to begin writing the results.
     * @param in0 the array to read the first input from.
     * @param in0Offset the offset for the first input.
     * @param in1 the array to read the second input from.
     * @param in1Offset the offset for the second input.
     * @param count the number of elements to process.
     * @param cnd the conditional to test.
     * @since 15.10.28
     */
    public static void arrayAddDCnd(
            final double[] out, final int outOffset,
            final double[] in0, final int in0Offset,
            final double[] in1, final int in1Offset,
            final int count,
            final Conditional cnd) {

        for (int i = 0; i < count; i++) {
            cnd.ifThen(i, index -> out[outOffset + index] = in0[in0Offset + index] + in1[in1Offset + index]);
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
     * Subtracts one element from another if the conditional passes.
     *
     * @param out the array to store the results.
     * @param outOffset the offset to begin writing the results.
     * @param in0 the array to read the first input from.
     * @param in0Offset the offset for the first input.
     * @param in1 the array to read the second input from.
     * @param in1Offset the offset for the second input.
     * @param count the number of elements to process.
     * @param cnd the conditional to test.
     * @since 15.10.27
     */
    public static void arraySubtractFCnd(
            final float[] out, final int outOffset,
            final float[] in0, final int in0Offset,
            final float[] in1, final int in1Offset,
            final int count,
            final Conditional cnd) {

        for (int i = 0; i < count; i++) {
            cnd.ifThen(i, index -> out[outOffset + index] = in0[in0Offset + index] - in1[in1Offset + index]);
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
     * Subtracts one element from another if the conditional passes.
     *
     * @param out the array to store the results.
     * @param outOffset the offset to begin writing the results.
     * @param in0 the array to read the first input from.
     * @param in0Offset the offset for the first input.
     * @param in1 the array to read the second input from.
     * @param in1Offset the offset for the second input.
     * @param count the number of elements to process.
     * @param cnd the conditional to test.
     * @since 15.10.27
     */
    public static void arraySubtractDCnd(
            final double[] out, final int outOffset,
            final double[] in0, final int in0Offset,
            final double[] in1, final int in1Offset,
            final int count,
            final Conditional cnd) {

        for (int i = 0; i < count; i++) {
            cnd.ifThen(i, index -> out[outOffset + index] = in0[in0Offset + index] - in1[in1Offset + index]);
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
     * Multiplies the elements if the conditional passes.
     *
     * @param out the array to store the results.
     * @param outOffset the offset to begin writing the results.
     * @param in0 the array to read the first input from.
     * @param in0Offset the offset for the first input.
     * @param in1 the array to read the second input from.
     * @param in1Offset the offset for the second input.
     * @param count the number of elements to process.
     * @param cnd the conditional to test.
     * @since 15.10.27
     */
    public static void arrayMultiplyFCnd(
            final float[] out, final int outOffset,
            final float[] in0, final int in0Offset,
            final float[] in1, final int in1Offset,
            final int count,
            final Conditional cnd) {

        for (int i = 0; i < count; i++) {
            cnd.ifThen(i, index -> out[outOffset + index] = in0[in0Offset + index] * in1[in1Offset + index]);
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
     * Multiplies the elements if the conditional passes.
     *
     * @param out the array to store the results.
     * @param outOffset the offset to begin writing the results.
     * @param in0 the array to read the first input from.
     * @param in0Offset the offset for the first input.
     * @param in1 the array to read the second input from.
     * @param in1Offset the offset for the second input.
     * @param count the number of elements to process.
     * @param cnd the conditional to test.
     * @since 15.10.27
     */
    public static void arrayMultiplyDCnd(
            final double[] out, final int outOffset,
            final double[] in0, final int in0Offset,
            final double[] in1, final int in1Offset,
            final int count,
            final Conditional cnd) {

        for (int i = 0; i < count; i++) {
            cnd.ifThen(i, index -> out[outOffset + index] = in0[in0Offset + index] * in1[in1Offset + index]);
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
     * Divides the elements if the conditional passes.
     *
     * @param out the array to store the results.
     * @param outOffset the offset to begin writing the results.
     * @param in0 the array to read the first input from.
     * @param in0Offset the offset for the first input.
     * @param in1 the array to read the second input from.
     * @param in1Offset the offset for the second input.
     * @param count the number of elements to process.
     * @param cnd the conditional to test.
     * @since 15.10.27
     */
    public static void arrayDivideFCnd(
            final float[] out, final int outOffset,
            final float[] in0, final int in0Offset,
            final float[] in1, final int in1Offset,
            final int count,
            final Conditional cnd) {

        for (int i = 0; i < count; i++) {
            cnd.ifThen(i, index -> out[outOffset + index] = in0[in0Offset + index] / in1[in1Offset + index]);
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
     * Divides the elements if the conditional passes.
     *
     * @param out the array to store the results.
     * @param outOffset the offset to begin writing the results.
     * @param in0 the array to read the first input from.
     * @param in0Offset the offset for the first input.
     * @param in1 the array to read the second input from.
     * @param in1Offset the offset for the second input.
     * @param count the number of elements to process.
     * @param cnd the conditional to test.
     * @since 15.10.27
     */
    public static void arrayDivideDCnd(
            final double[] out, final int outOffset,
            final double[] in0, final int in0Offset,
            final double[] in1, final int in1Offset,
            final int count,
            final Conditional cnd) {

        for (int i = 0; i < count; i++) {
            cnd.ifThen(i, index -> out[outOffset + index] = in0[in0Offset + index] / in1[in1Offset + index]);
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
     * Computes the square root if the conditional passes.
     *
     * @param out the array to store the results.
     * @param outOffset the offset to begin writing the results.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading the inputs.
     * @param count the number of elements to process.
     * @param cnd the conditional to test.
     * @since 15.10.27
     */
    public static void arraySqrtFCnd(
            final float[] out, final int outOffset,
            final float[] in0, final int in0Offset,
            final int count,
            final Conditional cnd) {

        for (int i = 0; i < count; i++) {
            cnd.ifThen(i, index -> out[outOffset + index] = (float) sqrt(in0[in0Offset + index]));
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
     * Computes the square root if the conditional passes.
     *
     * @param out the array to store the results.
     * @param outOffset the offset to begin writing the results.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading the inputs.
     * @param count the number of elements to process.
     * @param cnd the conditional to test.
     * @since 15.10.27
     */
    public static void arraySqrtDCnd(
            final double[] out, final int outOffset,
            final double[] in0, final int in0Offset,
            final int count,
            final Conditional cnd) {

        for (int i = 0; i < count; i++) {
            cnd.ifThen(i, index -> out[outOffset + index] = sqrt(in0[in0Offset + index]));
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
    public static double arrayMaxD(
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
     * This calculates the absolute value if the conditional is satisfied.
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @param cnd the conditional statement.
     * @since 15.10.27
     */
    public static void arrayAbsFCnd(
            final float[] out, final int outOffset,
            final float[] in0, final int in0Offset,
            final int count,
            final Conditional cnd) {

        for (int i = 0; i < count; i++) {
            cnd.ifThen(i, index -> out[index + outOffset] = abs(in0[index + in0Offset]));
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
     * This calculates the absolute value if the conditional is satisfied.
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @param cnd the conditional statement.
     * @since 15.10.27
     */
    public static void arrayAbsDCnd(
            final double[] out, final int outOffset,
            final double[] in0, final int in0Offset,
            final int count,
            final Conditional cnd) {

        for (int i = 0; i < count; i++) {
            cnd.ifThen(i, index -> out[index + outOffset] = abs(in0[index + in0Offset]));
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
     * Performs a compound multiply-add if the conditional passes.
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
     * @param cnd the conditional to test.
     * @since 15.10.27
     */
    public static void arrayMultiplyAddFCnd(
            final float[] out, final int outOffset,
            final float[] in0, final int in0Offset,
            final float[] in1, final int in1Offset,
            final float[] in2, final int in2Offset,
            final int count,
            final Conditional cnd) {

        for (int i = 0; i < count; i++) {
            cnd.ifThen(i, index -> out[outOffset + index] = in0[in0Offset + index] * in1[in1Offset + index] + in2[in2Offset + index]);
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
     * Performs a compound multiply-add if the conditional passes.
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
     * @param cnd the conditional to test.
     * @since 15.10.27
     */
    public static void arrayMultiplyAddDCnd(
            final double[] out, final int outOffset,
            final double[] in0, final int in0Offset,
            final double[] in1, final int in1Offset,
            final double[] in2, final int in2Offset,
            final int count,
            final Conditional cnd) {

        for (int i = 0; i < count; i++) {
            cnd.ifThen(i, index -> out[outOffset + index] = in0[in0Offset + index] * in1[in1Offset + index] + in2[in2Offset + index]);
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
     * Performs a compound multiply-subtract if the conditional passes.
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
     * @param cnd the conditional to test.
     * @since 15.10.27
     */
    public static void arrayMultiplySubtractFCnd(
            final float[] out, final int outOffset,
            final float[] in0, final int in0Offset,
            final float[] in1, final int in1Offset,
            final float[] in2, final int in2Offset,
            final int count,
            final Conditional cnd) {

        for (int i = 0; i < count; i++) {
            cnd.ifThen(i, index -> out[outOffset + index] = in0[in0Offset + index] * in1[in1Offset + index] - in2[in2Offset + index]);
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
     * Performs a compound multiply-subtract if the conditional passes.
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
     * @param cnd the conditional to test.
     * @since 15.10.27
     */
    public static void arrayMultiplySubtractDCnd(
            final double[] out, final int outOffset,
            final double[] in0, final int in0Offset,
            final double[] in1, final int in1Offset,
            final double[] in2, final int in2Offset,
            final int count,
            final Conditional cnd) {

        for (int i = 0; i < count; i++) {
            cnd.ifThen(i, index -> out[outOffset + index] = in0[in0Offset + index] * in1[in1Offset + index] - in2[in2Offset + index]);
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
     * Calculates the negative if the conditional passes.
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @param cnd the conditional to test.
     * @since 15.10.27
     */
    public static void arrayNegateFCnd(
            final float[] out, final int outOffset,
            final float[] in0, final int in0Offset,
            final int count,
            final Conditional cnd) {

        for (int i = 0; i < count; i++) {
            cnd.ifThen(i, index -> out[outOffset + index] = -in0[in0Offset + index]);
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
     * Calculates the negative if the conditional passes.
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @param cnd the conditional to test.
     * @since 15.10.27
     */
    public static void arrayNegateDCnd(
            final double[] out, final int outOffset,
            final double[] in0, final int in0Offset,
            final int count,
            final Conditional cnd) {

        for (int i = 0; i < count; i++) {
            cnd.ifThen(i, index -> out[outOffset + index] = -in0[in0Offset + index]);
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
     * Calculates a scaled value if the conditional passes.
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param scale the constant to scale each element by.
     * @param count the number of elements to process.
     * @param cnd the conditional to test.
     * @since 15.10.27
     */
    public static void arrayScaleFCnd(
            final float[] out, final int outOffset,
            final float[] in0, final int in0Offset,
            final float scale,
            final int count,
            final Conditional cnd) {

        for (int i = 0; i < count; i++) {
            cnd.ifThen(i, index -> out[outOffset + index] = in0[in0Offset + index] * scale);
        }
    }

    /**
     * Adds a constant to every element inside an array. This performs the
     * operation
     * <code>[r0, r1, r2, ..., rn] = [a0+k, a1+k, a2+k, ..., an+k]</code>
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset ot begin reading inputs.
     * @param constant the constant to increment each element by.
     * @param count the number of elements to process.
     * @since 15.10.26
     */
    public static void arrayAddConstantF(
            final float[] out, final int outOffset,
            final float[] in0, final int in0Offset,
            final float constant,
            final int count) {

        for (int i = 0; i < count; i++) {
            out[outOffset + i] = in0[in0Offset + i] + constant;
        }
    }

    /**
     * Adds a constant if the conditional passes.
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset ot begin reading inputs.
     * @param constant the constant to increment each element by.
     * @param count the number of elements to process.
     * @param cnd the conditional to test.
     * @since 15.10.27
     */
    public static void arrayAddConstantFCnd(
            final float[] out, final int outOffset,
            final float[] in0, final int in0Offset,
            final float constant,
            final int count,
            final Conditional cnd) {

        for (int i = 0; i < count; i++) {
            cnd.ifThen(i, index -> out[outOffset + index] = in0[in0Offset + index] + constant);
        }
    }

    /**
     * Adds a constant to every element inside an array. This performs the
     * operation
     * <code>[r0, r1, r2, ..., rn] = [a0+k, a1+k, a2+k, ..., an+k]</code>
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset ot begin reading inputs.
     * @param constant the constant to increment each element by.
     * @param count the number of elements to process.
     * @since 15.10.26
     */
    public static void arrayAddConstantD(
            final double[] out, final int outOffset,
            final double[] in0, final int in0Offset,
            final double constant,
            final int count) {

        for (int i = 0; i < count; i++) {
            out[outOffset + i] = in0[in0Offset + i] + constant;
        }
    }

    /**
     * Adds a constant if the conditional passes.
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset ot begin reading inputs.
     * @param constant the constant to increment each element by.
     * @param count the number of elements to process.
     * @param cnd the conditional to test.
     * @since 15.10.27
     */
    public static void arrayAddConstantDCnd(
            final double[] out, final int outOffset,
            final double[] in0, final int in0Offset,
            final double constant,
            final int count,
            final Conditional cnd) {

        for (int i = 0; i < count; i++) {
            cnd.ifThen(i, index -> out[outOffset + index] = in0[in0Offset + index] + constant);
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
     * Calculates a scaled value if the conditional passes.
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param scale the constant to scale each element by.
     * @param count the number of elements to process.
     * @param cnd the conditional to test.
     * @since 15.10.27
     */
    public static void arrayScaleDCnd(
            final double[] out, final int outOffset,
            final double[] in0, final int in0Offset,
            final double scale,
            final int count,
            final Conditional cnd) {

        for (int i = 0; i < count; i++) {
            cnd.ifThen(i, index -> out[outOffset + index] = in0[in0Offset + index] * scale);
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
     * Calculates a reciprocal if the conditional passes.
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @param cnd the conditional to test.
     * @since 15.10.27
     */
    public static void arrayReciprocalFCnd(
            final float[] out, final int outOffset,
            final float[] in0, final int in0Offset,
            final int count,
            final Conditional cnd) {

        for (int i = 0; i < count; i++) {
            cnd.ifThen(i, index -> out[outOffset + index] = 1.0f / in0[in0Offset + index]);
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
     * Calculates a reciprocal if the conditional passes.
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @param cnd the conditional to test.
     * @since 15.10.27
     */
    public static void arrayReciprocalDCnd(
            final double[] out, final int outOffset,
            final double[] in0, final int in0Offset,
            final int count,
            final Conditional cnd) {

        for (int i = 0; i < count; i++) {
            cnd.ifThen(i, index -> out[outOffset + index] = 1.0 / in0[in0Offset + index]);
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
     * Calculates the inverse square root if the conditional passes.
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @param cnd the conditional to test.
     * @since 15.10.27
     */
    public static void arrayInverseSqrtFCnd(
            final float[] out, final int outOffset,
            final float[] in0, final int in0Offset,
            final int count,
            final Conditional cnd) {

        for (int i = 0; i < count; i++) {
            cnd.ifThen(i, index -> out[outOffset + index] = (float) (1.0 / sqrt(in0[in0Offset + index])));
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
     * Calculates the inverse square root if the conditional passes.
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @param cnd the conditional to test.
     * @since 15.10.27
     */
    public static void arrayInverseSqrtDCnd(
            final double[] out, final int outOffset,
            final double[] in0, final int in0Offset,
            final int count,
            final Conditional cnd) {

        for (int i = 0; i < count; i++) {
            cnd.ifThen(i, index -> out[outOffset + index] = 1.0 / sqrt(in0[in0Offset + index]));
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
     * Rounds each element if the conditional passes.
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @param cnd the conditional to test.
     * @since 15.10.27
     */
    public static void arrayFloorFCnd(
            final float[] out, final int outOffset,
            final float[] in0, final int in0Offset,
            final int count,
            final Conditional cnd) {

        for (int i = 0; i < count; i++) {
            cnd.ifThen(i, index -> out[outOffset + index] = (float) floor(in0[in0Offset + index]));
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
     * Rounds each element if the conditional passes.
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @param cnd the conditional to test.
     * @since 15.10.27
     */
    public static void arrayFloorDCnd(
            final double[] out, final int outOffset,
            final double[] in0, final int in0Offset,
            final int count,
            final Conditional cnd) {

        for (int i = 0; i < count; i++) {
            cnd.ifThen(i, index -> out[outOffset + index] = floor(in0[in0Offset + index]));
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
     * Rounds up if the conditional passes.
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @param cnd the conditional to test.
     * @since 15.10.27
     */
    public static void arrayCeilFCnd(
            final float[] out, final int outOffset,
            final float[] in0, final int in0Offset,
            final int count,
            final Conditional cnd) {

        for (int i = 0; i < count; i++) {
            cnd.ifThen(i, index -> out[outOffset + index] = (float) ceil(in0[in0Offset + index]));
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
     * Rounds up if the conditional passes.
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @param cnd the conditional to test.
     * @since 15.10.27
     */
    public static void arrayCeilDCnd(
            final double[] out, final int outOffset,
            final double[] in0, final int in0Offset,
            final int count,
            final Conditional cnd) {

        for (int i = 0; i < count; i++) {
            cnd.ifThen(i, index -> out[outOffset + index] = ceil(in0[in0Offset + index]));
        }
    }

    /**
     * Rounds each element in an array. This performs the operation:
     * <code>[r0, r1, r2, ..., rn] = [round(a0), round(r1), round(r2), ..., round(rn)]</code>
     * where <code>round = fract(in) lessThan(0.5) ? floor(in) : ceil(in)</code>
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
     * Rounds the element if the conditional passes.
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @param cnd the conditional to test.
     * @since 15.10.27
     */
    public static void arrayRoundFCnd(
            final float[] out, final int outOffset,
            final float[] in0, final int in0Offset,
            final int count,
            final Conditional cnd) {

        for (int i = 0; i < count; i++) {
            cnd.ifThen(i, index -> out[outOffset + index] = round(in0[in0Offset + index]));
        }
    }

    /**
     * Rounds each element in an array. This performs the operation:
     * <code>[r0, r1, r2, ..., rn] = [round(a0), round(r1), round(r2), ..., round(rn)]</code>
     * where <code>round = fract(in) lessThan(0.5) ? floor(in) : ceil(in)</code>
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
     * Rounds the element if the conditional passes.
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @param cnd the conditional to test.
     * @since 15.10.27
     */
    public static void arrayRoundDCnd(
            final double[] out, final int outOffset,
            final double[] in0, final int in0Offset,
            final int count,
            final Conditional cnd) {

        for (int i = 0; i < count; i++) {
            cnd.ifThen(i, index -> out[outOffset + index] = round(in0[in0Offset + index]));
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
     * Calculates the natural log if the conditional passes.
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @param cnd the conditional to test.
     * @since 15.10.27
     */
    public static void arrayLogFCnd(
            final float[] out, final int outOffset,
            final float[] in0, final int in0Offset,
            final int count,
            final Conditional cnd) {

        for (int i = 0; i < count; i++) {
            cnd.ifThen(i, index -> out[outOffset + index] = (float) log(in0[in0Offset + index]));
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
     * Calculates the natural log if the conditional passes.
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @param cnd the conditional to test.
     * @since 15.10.27
     */
    public static void arrayLogDCnd(
            final double[] out, final int outOffset,
            final double[] in0, final int in0Offset,
            final int count,
            final Conditional cnd) {

        for (int i = 0; i < count; i++) {
            cnd.ifThen(i, index -> out[outOffset + index] = log(in0[in0Offset + index]));
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
     * Calculates the base10 log if the conditional passes.
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @param cnd the conditional to test.
     * @since 15.10.27
     */
    public static void arrayLog10FCnd(
            final float[] out, final int outOffset,
            final float[] in0, final int in0Offset,
            final int count,
            final Conditional cnd) {

        for (int i = 0; i < count; i++) {
            cnd.ifThen(i, index -> out[outOffset + index] = (float) log10(in0[in0Offset + index]));
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
     * Calculates the base10 log if the conditional passes.
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @param cnd the conditional to test.
     * @since 15.10.27
     */
    public static void arrayLog10DCnd(
            final double[] out, final int outOffset,
            final double[] in0, final int in0Offset,
            final int count,
            final Conditional cnd) {

        for (int i = 0; i < count; i++) {
            cnd.ifThen(i, index -> out[outOffset + index] = log10(in0[in0Offset + index]));
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
     * Copies the element if the conditional passes.
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @param cnd the conditional to test.
     * @since 15.10.27
     */
    public static void arrayCopyFCnd(
            final float[] out, final int outOffset,
            final float[] in0, final int in0Offset,
            final int count,
            final Conditional cnd) {

        for (int i = 0; i < count; i++) {
            cnd.ifThen(i, index -> out[outOffset + index] = in0[in0Offset + index]);
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
    public static void arrayCopyD(
            final double[] out, final int outOffset,
            final double[] in0, final int in0Offset,
            final int count) {

        System.arraycopy(in0, in0Offset, out, outOffset, count);
    }

    /**
     * Copies the element if the conditional passes.
     *
     * @param out the array to write the outputs to.
     * @param outOffset the offset to begin writing outputs.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading inputs.
     * @param count the number of elements to process.
     * @param cnd the conditional to test.
     * @since 15.10.27
     */
    public static void arrayCopyDCnd(
            final double[] out, final int outOffset,
            final double[] in0, final int in0Offset,
            final int count,
            final Conditional cnd) {

        for (int i = 0; i < count; i++) {
            cnd.ifThen(i, index -> out[outOffset + index] = in0[in0Offset + index]);
        }
    }
}
