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

import static com.longlinkislong.gloop.VectorArrays.*;
import static java.lang.Math.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * GLVec4FArray is SoA (Struct of Arrays) implementation of 4 element single
 * precision vectors.
 *
 * @author zmichaels
 * @since 15.10.23
 */
public final class GLVec4Array {

    public final int length;
    public final double[] x;
    public final double[] y;
    public final double[] z;
    public final double[] w;

    /**
     * Constructs a new GLVec4FArray with the specified number of elements.
     *
     * @param size the number of vectors the GLVec4FArray will hold.
     * @since 15.10.23
     */
    public GLVec4Array(final int size) {
        this.length = size;

        this.x = new double[size];
        this.y = new double[size];
        this.z = new double[size];
        this.w = new double[size];
    }

    /**
     * Retrieves a GLVec4F from the internal array.
     *
     * @param index the index of the element.
     * @return the GLVec4F.
     * @since 15.10.23
     */
    public GLVec4D get(final int index) {
        return GLVec4D.create(this.x[index], this.y[index], this.z[index], this.w[index]);
    }

    /**
     * Bulk retrieves vectors from the internal array.
     *
     * @param <VecT> the type of vector to write to.
     * @param readIndex the index to begin reading at.
     * @param out the array of vectors to write to.
     * @param writeIndex the offset to begin writing.
     * @param count the number of elements to process.
     * @since 15.10.23
     */
    public <VecT extends GLVec4D> void get(final int readIndex, final VecT[] out, final int writeIndex, final int count) {
        for (int i = 0; i < count; i++) {
            out[writeIndex + i].set(this.x[readIndex + i], this.y[readIndex + i], this.z[readIndex + i], this.w[readIndex + i]);
        }
    }

    public DoubleStream flatStream() {
        return Stream.of(x, y, z, w).flatMapToDouble(Arrays::stream);
    }

    public DoubleStream flatInterlacedStream() {
        final int[] index = {0};

        return DoubleStream.generate(() -> {
            final int idx = index[0];

            index[0]++;
            switch (idx % 4) {
                case 0:
                    return x[idx / 4];
                case 1:
                    return y[idx / 4];
                case 2:
                    return z[idx / 4];
                case 3:
                    return w[idx / 4];
                default:
                    throw new IllegalStateException("# % 4 >= 3!");
            }
        });
    }

    /**
     * Constructs a stream of GLVec4F objects constructed by the data held by
     * this array.
     *
     * @return the stream.
     * @since 15.10.23
     */
    public Stream<GLVec4D> stream() {
        return this.stream(0, this.length);
    }

    /**
     * Constructs a stream of GLVec4F objects on the specified range.
     *
     * @param startInclusive the starting range, inclusive.
     * @param endExclusive the ending range, exclusive.
     * @return the stream.
     * @since 15.10.23
     */
    public Stream<GLVec4D> stream(final int startInclusive, final int endExclusive) {
        return IntStream.range(startInclusive, endExclusive).mapToObj(this::get);
    }

    private static GLVec4Array ensureArray(
            GLVec4Array in,
            final int offset,
            final int neededSize) {

        if (in == null || in.length < offset + neededSize) {
            return new GLVec4Array(offset + neededSize);
        } else {
            return in;
        }
    }

    private static double[] ensureArray(
            final double[] in,
            final int offset,
            final int neededSize) {

        if (in == null || in.length < offset + neededSize) {
            return new double[offset + neededSize];
        } else {
            return in;
        }
    }

    public GLVec4Array cross(final GLVec4Array out, final GLVec4Array other) {
        return cross(out, 0, this, 0, other, 0, this.length);
    }

    /**
     * Calculates a cross product on each element within the array of vectors.
     *
     * @param out the output array of vectors.
     * @param outOffset the offset to begin writing the outputs.
     * @param in0 the first input array of vectors.
     * @param in0Offset the offset to begin reading the inputs.
     * @param in1 the second input array of vectors.
     * @param in1Offset the offset to begin reading the inputs.
     * @param count the number of inputs to process.
     * @return the result array
     * @since 15.10.23
     */
    public static GLVec4Array cross(
            final GLVec4Array out, final int outOffset,
            final GLVec4Array in0, final int in0Offset,
            final GLVec4Array in1, final int in1Offset,
            final int count) {

        final GLVec4Array res = ensureArray(out, outOffset, count);

        arrayMultiplyD(res.x, outOffset, in0.z, in0Offset, in1.y, in1Offset, count);
        arrayMultiplyD(res.y, outOffset, in0.x, in0Offset, in1.z, in1Offset, count);
        arrayMultiplyD(res.z, outOffset, in0.y, in0Offset, in1.x, in1Offset, count);

        arrayMultiplySubtractD(res.x, outOffset, in0.y, in0Offset, in1.z, in1Offset, res.x, outOffset, count);
        arrayMultiplySubtractD(res.y, outOffset, in0.z, in0Offset, in1.x, in1Offset, res.y, outOffset, count);
        arrayMultiplySubtractD(res.z, outOffset, in0.x, in0Offset, in1.y, in1Offset, res.z, outOffset, count);
        arraySetD(res.w, outOffset, 1.0, count);

        return res;
    }

    public Future<GLVec4Array> crossAsync(final GLVec4Array out, final GLVec4Array other) {
        return crossAsync(out, 0, this, 0, other, 0, this.length);
    }

    /**
     * Multi-threaded implementation of cross.
     *
     * @param out the output array of vectors.
     * @param outOffset the offset to begin writing the outputs.
     * @param in0 the first input array of vectors.
     * @param in0Offset the offset to begin reading the inputs.
     * @param in1 the second input array of vectors.
     * @param in1Offset the offset to begin reading the inputs.
     * @param count the number of inputs to process.
     * @return the result array
     * @since 15.10.23
     */
    public static Future<GLVec4Array> crossAsync(
            final GLVec4Array out, final int outOffset,
            final GLVec4Array in0, final int in0Offset,
            final GLVec4Array in1, final int in1Offset,
            final int count) {

        final GLVec4Array res = ensureArray(out, outOffset, count);

        X_TASKS.submit(() -> arrayMultiplyD(res.x, outOffset, in0.z, in0Offset, in1.y, in1Offset, count));
        Y_TASKS.submit(() -> arrayMultiplyD(res.y, outOffset, in0.x, in0Offset, in1.z, in1Offset, count));
        Z_TASKS.submit(() -> arrayMultiplyD(res.z, outOffset, in0.y, in0Offset, in1.x, in1Offset, count));

        // these tasks wont start until the previous XYZ tasks complete.
        final Future<?> taskX = X_TASKS.submit(() -> arrayMultiplySubtractD(res.x, outOffset, in0.y, in0Offset, in1.z, in1Offset, res.x, outOffset, count));
        final Future<?> taskY = Y_TASKS.submit(() -> arrayMultiplySubtractD(res.y, outOffset, in0.z, in0Offset, in1.x, in1Offset, res.y, outOffset, count));
        final Future<?> taskZ = Z_TASKS.submit(() -> arrayMultiplySubtractD(res.z, outOffset, in0.x, in0Offset, in1.y, in1Offset, res.z, outOffset, count));

        // this task is independent
        final Future<?> taskW = W_TASKS.submit(() -> arraySetD(res.w, outOffset, 1.0, count));

        return new FutureGLVec4Array(res, taskX, taskY, taskZ, taskW);
    }

    public double[] dot(final double[] out, final GLVec4Array other) {
        return dot(out, 0, this, 0, other, 0, this.length);
    }

    /**
     * Calculates the dot product of each vector.
     *
     * @param out the output array of vectors.
     * @param outOffset the offset to begin writing the outputs.
     * @param in0 the first input array of vectors.
     * @param in0Offset the offset to begin reading the inputs.
     * @param in1 the second input array of vectors.
     * @param in1Offset the offset to begin reading the inputs.
     * @param count the number of elements to process.
     * @return the result array
     * @since 15.10.23
     */
    public static double[] dot(
            final double[] out, final int outOffset,
            final GLVec4Array in0, final int in0Offset,
            final GLVec4Array in1, final int in1Offset,
            final int count) {

        final double[] res = ensureArray(out, outOffset, count);

        // out = in0.x * in1.x
        arrayMultiplyD(res, outOffset, in0.x, in0Offset, in1.x, in1Offset, count);
        // out = in0.y * in1.y + out
        arrayMultiplyAddD(res, outOffset, in0.y, in0Offset, in1.y, in1Offset, res, outOffset, count);
        // out = in0.z * in1.z + out
        arrayMultiplyAddD(res, outOffset, in0.z, in0Offset, in1.z, in1Offset, res, outOffset, count);
        // out = in0.w * in1.w + out
        arrayMultiplyAddD(res, outOffset, in0.w, in0Offset, in1.w, in1Offset, res, outOffset, count);

        return res;
    }

    public double[] length(final double[] out) {
        return length(out, 0, this, 0, this.length);
    }

    /**
     * Calculates the length of each vector.
     *
     * @param out the array to store the vector length in.
     * @param outOffset the offset to begin writing the outputs.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading the inputs.
     * @param count the number of elements to process.
     * @return the result array
     * @since 15.10.23
     */
    public static double[] length(
            final double[] out, final int outOffset,
            final GLVec4Array in0, final int in0Offset,
            final int count) {

        final double[] res = ensureArray(out, outOffset, count);

        for (int i = 0; i < count; i++) {
            final double x = in0.x[i + in0Offset];
            final double y = in0.y[i + in0Offset];
            final double z = in0.z[i + in0Offset];
            final double w = in0.w[i + in0Offset];

            res[i + outOffset] = sqrt(x * x + y * y + z * z + w * w);
        }

        return res;
    }

    public double[] length2(final double[] out) {
        return length2(out, 0, this, 0, this.length);
    }

    public static double[] length2(
            final double[] out, final int outOffset,
            final GLVec4Array in0, final int in0Offset,
            final int count) {

        final double[] res = ensureArray(out, outOffset, count);

        for (int i = 0; i < count; i++) {
            final double x = in0.x[i + in0Offset];
            final double y = in0.y[i + in0Offset];
            final double z = in0.z[i + in0Offset];
            final double w = in0.w[i + in0Offset];

            res[i + outOffset] = x * x + y * y + z * z + w * w;
        }

        return res;
    }

    public GLVec4Array normalize(final GLVec4Array out) {
        return normalize(out, 0, this, 0, this.length);
    }

    /**
     * Normalizes the vectors.
     *
     * @param out the array of vectors to write the results to.
     * @param outOffset the offset to begin writing.
     * @param in0 the input array of vectors.
     * @param in0Offset the offset to begin reading.
     * @param count the number of elements to process.
     * @return the result array
     * @since 15.10.23
     */
    public static GLVec4Array normalize(
            final GLVec4Array out, final int outOffset,
            final GLVec4Array in0, final int in0Offset,
            final int count) {

        final GLVec4Array res = ensureArray(out, outOffset, count);

        for (int i = 0; i < count; i++) {
            final double x = in0.x[i + in0Offset];
            final double y = in0.y[i + in0Offset];
            final double z = in0.z[i + in0Offset];
            final double w = in0.w[i + in0Offset];

            final double scale = 1.0 / sqrt(x * x + y * y + z * z + w * w);

            res.x[i + outOffset] = scale * x;
            res.y[i + outOffset] = scale * y;
            res.z[i + outOffset] = scale * z;
            res.w[i + outOffset] = scale * w;
        }

        return res;
    }

    public GLVec4Array apply(final VectorArrays.UnaryOp<double[]> op, final GLVec4Array out) {
        return apply(op, out, 0, this, 0, this.length);
    }

    /**
     * Applies an unary array of vectors operation using GLVec4FArray objects.
     *
     * @param op the unary operation to apply.
     * @param out the array of vectors to write the result to.
     * @param outOffset the offset to begin the write.
     * @param in0 the first array of vectors to read from.
     * @param in0Offset the read offset for the first input.
     * @param count the number of elements to process.
     * @return the result array
     * @since 15.10.23
     */
    public static GLVec4Array apply(
            final VectorArrays.UnaryOp<double[]> op,
            final GLVec4Array out, final int outOffset,
            final GLVec4Array in0, final int in0Offset,
            final int count) {

        final GLVec4Array res = ensureArray(out, outOffset, count);

        op.apply(res.x, outOffset, in0.x, in0Offset, count);
        op.apply(res.y, outOffset, in0.y, in0Offset, count);
        op.apply(res.z, outOffset, in0.z, in0Offset, count);
        op.apply(res.w, outOffset, in0.w, in0Offset, count);

        return res;
    }

    public Future<GLVec4Array> applyAsync(final VectorArrays.UnaryOp<double[]> op, final GLVec4Array out) {
        return applyAsync(op, out, 0, this, 0, this.length);
    }

    /**
     * Multi-threaded implementation of the unary operation implementation of
     * apply.
     *
     * @param op the unary operation to apply.
     * @param out the array of vectors to write.
     * @param outOffset the offset to begin writing to.
     * @param in0 the first array to read inputs from.
     * @param in0Offset the offset to begin reading from.
     * @param count the number of elements to process.
     * @return the result array
     * @since 15.10.23
     */
    public static Future<GLVec4Array> applyAsync(
            final VectorArrays.UnaryOp<double[]> op,
            final GLVec4Array out, final int outOffset,
            final GLVec4Array in0, final int in0Offset,
            final int count) {

        final GLVec4Array res = ensureArray(out, outOffset, count);

        final Future<?> taskX = X_TASKS.submit(() -> op.apply(res.x, outOffset, in0.x, in0Offset, count));
        final Future<?> taskY = Y_TASKS.submit(() -> op.apply(res.y, outOffset, in0.y, in0Offset, count));
        final Future<?> taskZ = Z_TASKS.submit(() -> op.apply(res.z, outOffset, in0.z, in0Offset, count));
        final Future<?> taskW = W_TASKS.submit(() -> op.apply(res.w, outOffset, in0.w, in0Offset, count));

        return new FutureGLVec4Array(res, taskX, taskY, taskZ, taskW);
    }

    public GLVec4Array apply(final VectorArrays.BinaryOp<double[]> op, final GLVec4Array out, final GLVec4Array other) {
        return apply(op, out, 0, this, 0, other, 0, this.length);
    }

    /**
     * Applies a binary array of vectors operation using GLVec4FArray objects.
     *
     * @param op the binary operation to apply.
     * @param out the array of vectors to write the result to.
     * @param outOffset the offset to begin the write.
     * @param in0 the first array of vectors to read from.
     * @param in0Offset the read offset for the first input.
     * @param in1 the second array of vectors to read from.
     * @param in1Offset the read offset for the second input.
     * @param count the number of elements to process.
     * @return the result array
     * @since 15.10.23
     */
    public static GLVec4Array apply(
            final VectorArrays.BinaryOp<double[]> op,
            final GLVec4Array out, final int outOffset,
            final GLVec4Array in0, final int in0Offset,
            final GLVec4Array in1, final int in1Offset,
            final int count) {

        final GLVec4Array res = ensureArray(out, outOffset, count);

        op.apply(res.x, outOffset, in0.x, in0Offset, in1.x, in1Offset, count);
        op.apply(res.y, outOffset, in0.y, in0Offset, in1.y, in1Offset, count);
        op.apply(res.z, outOffset, in0.z, in0Offset, in1.z, in1Offset, count);
        op.apply(res.w, outOffset, in0.w, in0Offset, in1.w, in1Offset, count);

        return res;
    }

    public Future<GLVec4Array> applyAsync(
            final VectorArrays.BinaryOp<double[]> op,
            final GLVec4Array out,
            final GLVec4Array other) {

        return applyAsync(op, out, 0, this, 0, other, 0, this.length);
    }

    /**
     * Multi-threaded implementation of the binary operation implementation of
     * apply.
     *
     * @param op the binary operation to apply.
     * @param out the array of vectors to write.
     * @param outOffset the offset to begin writing to.
     * @param in0 the first array to read inputs from.
     * @param in0Offset the offset to begin reading from.
     * @param in1 the second array to read inputs from.
     * @param in1Offset the offset to begin reading from.
     * @param count the number of elements to process.
     * @return the result array
     * @since 15.10.23
     */
    public static Future<GLVec4Array> applyAsync(
            final VectorArrays.BinaryOp<double[]> op,
            final GLVec4Array out, final int outOffset,
            final GLVec4Array in0, final int in0Offset,
            final GLVec4Array in1, final int in1Offset,
            final int count) {

        final GLVec4Array res = ensureArray(out, outOffset, count);

        final Future<?> taskX = X_TASKS.submit(() -> op.apply(res.x, outOffset, in0.x, in0Offset, in1.x, in1Offset, count));
        final Future<?> taskY = Y_TASKS.submit(() -> op.apply(res.y, outOffset, in0.y, in0Offset, in1.y, in1Offset, count));
        final Future<?> taskZ = Z_TASKS.submit(() -> op.apply(res.z, outOffset, in0.z, in0Offset, in1.z, in1Offset, count));
        final Future<?> taskW = W_TASKS.submit(() -> op.apply(res.w, outOffset, in0.w, in0Offset, in1.w, in1Offset, count));

        return new FutureGLVec4Array(res, taskX, taskY, taskZ, taskW);
    }

    public GLVec4Array apply(
            final VectorArrays.TernaryOp<double[]> op,
            final GLVec4Array out,
            final GLVec4Array other0,
            final GLVec4Array other1) {

        return apply(op, out, 0, this, 0, other0, 0, other1, 0, this.length);
    }

    /**
     * Applies a ternary array of vectors operation using GLVec4FArray objects.
     *
     * @param op the ternary operation to apply.
     * @param out the array of vectors to write the result to.
     * @param outOffset the offset to begin the write.
     * @param in0 the first array of vectors to read from.
     * @param in0Offset the read offset for the first input.
     * @param in1 the second array of vectors to read from.
     * @param in1Offset the read offset for the second input.
     * @param in2 the third array of vectors to read from.
     * @param in2Offset the read offset for the third input.
     * @param count the number of elements to process.
     * @return the result array
     * @since 15.10.23
     */
    public static GLVec4Array apply(
            final VectorArrays.TernaryOp<double[]> op,
            final GLVec4Array out, final int outOffset,
            final GLVec4Array in0, final int in0Offset,
            final GLVec4Array in1, final int in1Offset,
            final GLVec4Array in2, final int in2Offset,
            final int count) {

        final GLVec4Array res = ensureArray(out, outOffset, count);

        op.apply(res.x, outOffset, in0.x, in0Offset, in1.x, in1Offset, in2.x, in1Offset, count);
        op.apply(res.y, outOffset, in0.y, in0Offset, in1.y, in1Offset, in2.y, in2Offset, count);
        op.apply(res.z, outOffset, in0.z, in0Offset, in1.z, in1Offset, in2.z, in2Offset, count);
        op.apply(res.w, outOffset, in0.w, in0Offset, in1.w, in1Offset, in2.w, in2Offset, count);

        return res;
    }

    public Future<GLVec4Array> applyAsync(
            final VectorArrays.TernaryOp<double[]> op,
            final GLVec4Array out,
            final GLVec4Array other0,
            final GLVec4Array other1) {

        return applyAsync(op, out, 0, this, 0, other0, 0, other1, 0, this.length);
    }

    /**
     * Multi-threaded implementation of the ternary operation apply function.
     *
     * @param op the ternary operation to apply.
     * @param out the array of vectors to write the result to.
     * @param outOffset the offset to begin the write.
     * @param in0 the first array of vectors to read from.
     * @param in0Offset the read offset for the first input.
     * @param in1 the second array of vectors to read from.
     * @param in1Offset the read offset for the second input.
     * @param in2 the third array of vectors to read from.
     * @param in2Offset the read offset for the third input.
     * @param count the number of elements to process.
     * @return the result array
     * @since 15.10.23
     */
    public static Future<GLVec4Array> applyAsync(
            final VectorArrays.TernaryOp<double[]> op,
            final GLVec4Array out, final int outOffset,
            final GLVec4Array in0, final int in0Offset,
            final GLVec4Array in1, final int in1Offset,
            final GLVec4Array in2, final int in2Offset,
            final int count) {

        final GLVec4Array res = ensureArray(out, outOffset, count);

        final Future<?> taskX = X_TASKS.submit(() -> op.apply(res.x, outOffset, in0.x, in0Offset, in1.x, in1Offset, in2.x, in2Offset, count));
        final Future<?> taskY = Y_TASKS.submit(() -> op.apply(res.y, outOffset, in0.y, in0Offset, in1.y, in1Offset, in2.y, in2Offset, count));
        final Future<?> taskZ = Z_TASKS.submit(() -> op.apply(res.z, outOffset, in0.z, in0Offset, in1.z, in1Offset, in2.z, in2Offset, count));
        final Future<?> taskW = W_TASKS.submit(() -> op.apply(res.w, outOffset, in0.w, in0Offset, in1.w, in1Offset, in2.w, in2Offset, count));

        return new FutureGLVec4Array(res, taskX, taskY, taskZ, taskW);
    }

    public GLVec4Array scale(final GLVec4Array out, final GLVec4 scale) {
        return scale(out, 0, this, 0, scale, this.length);
    }

    /**
     * Scales a section of the array of vectors by a constant.
     *
     * @param out the array of vectors to write the changes to.
     * @param outOffset the position of the array to write to.
     * @param in0 the array of vectors to read the values from.
     * @param in0Offset the offset of the array to read from.
     * @param scale the constant to scale by.
     * @param count the number of elements to scale.
     * @return the result array
     * @since 15.10.23
     */
    public static GLVec4Array scale(
            final GLVec4Array out, final int outOffset,
            final GLVec4Array in0, final int in0Offset,
            final GLVec4 scale,
            final int count) {

        final GLVec4Array res = ensureArray(out, outOffset, count);
        final GLVec4D scaleD = scale.asGLVec4D();
        final double x = scaleD.x();
        final double y = scaleD.y();
        final double z = scaleD.z();
        final double w = scaleD.w();

        arrayScaleD(res.x, outOffset, in0.x, in0Offset, x, count);
        arrayScaleD(res.y, outOffset, in0.y, in0Offset, y, count);
        arrayScaleD(res.z, outOffset, in0.z, in0Offset, z, count);
        arrayScaleD(res.w, outOffset, in0.w, in0Offset, w, count);

        return res;
    }

    public Future<GLVec4Array> scaleAsync(final GLVec4Array out, final GLVec4 scale) {
        return scaleAsync(out, 0, this, 0, scale, this.length);
    }

    /**
     * Multithreaded implementation of scale.
     *
     * @param out the array of vectors to write the changes to.
     * @param outOffset the position of the array to write to.
     * @param in0 the array of vectors to read the values from.
     * @param in0Offset the offset of the array to read from.
     * @param scale the constant to scale by.
     * @param count the number of elements to scale.
     * @return the return array
     * @since 15.10.23
     */
    public static Future<GLVec4Array> scaleAsync(
            final GLVec4Array out, final int outOffset,
            final GLVec4Array in0, final int in0Offset,
            final GLVec4 scale,
            final int count) {

        final GLVec4Array res = ensureArray(out, outOffset, count);
        final GLVec4D scaleD = scale.asGLVec4D();
        final double x = scaleD.x();
        final double y = scaleD.y();
        final double z = scaleD.z();
        final double w = scaleD.w();

        final Future<?> taskX = X_TASKS.submit(() -> arrayScaleD(res.x, outOffset, in0.x, in0Offset, x, count));
        final Future<?> taskY = Y_TASKS.submit(() -> arrayScaleD(res.y, outOffset, in0.y, in0Offset, y, count));
        final Future<?> taskZ = Z_TASKS.submit(() -> arrayScaleD(res.z, outOffset, in0.z, in0Offset, z, count));
        final Future<?> taskW = W_TASKS.submit(() -> arrayScaleD(res.w, outOffset, in0.w, in0Offset, w, count));

        return new FutureGLVec4Array(res, taskX, taskY, taskZ, taskW);
    }

    public GLVec4Array addConstant(final GLVec4Array out, final GLVec4 vec) {
        return addConstant(out, 0, this, 0, vec, this.length);
    }

    /**
     * Increments a section of the array of vectors by a constant.
     *
     * @param out the array of vectors to write the changes to.
     * @param outOffset the position of the array to write to.
     * @param in0 the array of vectors to read the values from.
     * @param in0Offset the offset of the array to read from.
     * @param vec the constant to increment each element by.
     * @param count the number of elements to scale.
     * @return the return result
     * @since 15.10.23
     */
    public static GLVec4Array addConstant(
            final GLVec4Array out, final int outOffset,
            final GLVec4Array in0, final int in0Offset,
            final GLVec4 vec,
            final int count) {

        final GLVec4Array res = ensureArray(out, outOffset, count);
        final GLVec4D vecD = vec.asGLVec4D();
        final double x = vecD.x();
        final double y = vecD.y();
        final double z = vecD.z();
        final double w = vecD.w();

        arrayAddConstantD(res.x, outOffset, in0.x, in0Offset, x, count);
        arrayAddConstantD(res.y, outOffset, in0.y, in0Offset, y, count);
        arrayAddConstantD(res.z, outOffset, in0.z, in0Offset, z, count);
        arrayAddConstantD(res.w, outOffset, in0.w, in0Offset, w, count);

        return res;
    }

    public Future<GLVec4Array> addConstantAsync(final GLVec4Array out, final GLVec4 vec) {
        return addConstantAsync(out, 0, this, 0, vec, this.length);
    }

    /**
     * Multi-threaded implementation of addConstant
     *
     * @param out the array of vectors to write the changes to.
     * @param outOffset the position of the array to write to.
     * @param in0 the array of vectors to read the values from.
     * @param in0Offset the offset of the array to read from.
     * @param vec the constant to increment each element by.
     * @param count the number of elements to scale.
     * @return the result array
     * @since 15.10.23
     */
    public static Future<GLVec4Array> addConstantAsync(
            final GLVec4Array out, final int outOffset,
            final GLVec4Array in0, final int in0Offset,
            final GLVec4 vec,
            final int count) {

        final GLVec4Array res = ensureArray(out, outOffset, count);
        final GLVec4D vecD = vec.asGLVec4D();
        final double x = vecD.x();
        final double y = vecD.y();
        final double z = vecD.z();
        final double w = vecD.w();

        final Future<?> taskX = X_TASKS.submit(() -> arrayAddConstantD(res.x, outOffset, in0.x, in0Offset, x, count));
        final Future<?> taskY = Y_TASKS.submit(() -> arrayAddConstantD(res.y, outOffset, in0.y, in0Offset, y, count));
        final Future<?> taskZ = Z_TASKS.submit(() -> arrayAddConstantD(res.z, outOffset, in0.z, in0Offset, z, count));
        final Future<?> taskW = W_TASKS.submit(() -> arrayAddConstantD(res.w, outOffset, in0.w, in0Offset, w, count));

        return new FutureGLVec4Array(res, taskX, taskY, taskZ, taskW);
    }

    public GLVec4Array setConstant(final GLVec4 vec) {
        return setConstant(this, 0, vec, this.length);
    }

    /**
     * Sets a section of the array of vectors to a constant.
     *
     * @param out the array of vectors to write the changes to.
     * @param outOffset the position of the array to write to.
     * @param vec the constant to assign.
     * @param count the number of elements to scale.
     * @return the return array
     * @since 15.10.23
     */
    public static GLVec4Array setConstant(
            final GLVec4Array out, final int outOffset,
            final GLVec4 vec,
            final int count) {

        final GLVec4Array res = ensureArray(out, outOffset, count);
        final GLVec4D vecD = vec.asGLVec4D();
        final double x = vecD.x();
        final double y = vecD.y();
        final double z = vecD.z();
        final double w = vecD.w();

        VectorArrays.arraySetD(res.x, outOffset, x, count);
        VectorArrays.arraySetD(res.y, outOffset, y, count);
        VectorArrays.arraySetD(res.z, outOffset, z, count);
        VectorArrays.arraySetD(res.w, outOffset, w, count);

        return res;
    }

    public Future<GLVec4Array> setConstantAsync(final GLVec4 vec) {
        return setConstantAsync(this, 0, vec, this.length);
    }

    /**
     * Multi-threaded implementation of setConstant.
     *
     * @param out the array of vectors to write the changes to.
     * @param outOffset the position of the array to write to.
     * @param vec the constant to assign.
     * @param count the number of elements to scale.
     * @return the result array
     * @since 15.10.23
     */
    public static Future<GLVec4Array> setConstantAsync(
            final GLVec4Array out, final int outOffset,
            final GLVec4 vec,
            final int count) {

        final GLVec4Array res = ensureArray(out, outOffset, count);
        final GLVec4D vecD = vec.asGLVec4D();
        final double x = vecD.x();
        final double y = vecD.y();
        final double z = vecD.z();
        final double w = vecD.w();

        final Future<?> taskX = X_TASKS.submit(() -> arraySetD(res.x, outOffset, x, count));
        final Future<?> taskY = Y_TASKS.submit(() -> arraySetD(res.y, outOffset, y, count));
        final Future<?> taskZ = Z_TASKS.submit(() -> arraySetD(res.z, outOffset, z, count));
        final Future<?> taskW = W_TASKS.submit(() -> arraySetD(res.w, outOffset, w, count));

        return new FutureGLVec4Array(res, taskX, taskY, taskZ, taskW);
    }

    public double[] flatten(final VectorArrays.QuaternaryOp<double[]> op, final double[] out) {
        return flatten(op, out, 0, this, 0, this.length);
    }

    public static double[] flatten(
            final VectorArrays.QuaternaryOp<double[]> op,
            final double[] out, final int outOffset,
            final GLVec4Array in0, final int in0Offset,
            final int count) {

        final double[] res = ensureArray(out, outOffset, count);

        op.apply(res, outOffset, in0.x, in0Offset, in0.y, in0Offset, in0.z, in0Offset, in0.w, in0Offset, count);

        return res;
    }

    private static class FutureGLVec4Array implements Future<GLVec4Array> {

        private final Future<?> xTask;
        private final Future<?> yTask;
        private final Future<?> zTask;
        private final Future<?> wTask;
        private final GLVec4Array out;

        FutureGLVec4Array(GLVec4Array out, final Future<?> xTask, final Future<?> yTask, final Future<?> zTask, final Future<?> wTask) {
            this.out = Objects.requireNonNull(out);
            this.xTask = xTask;
            this.yTask = yTask;
            this.zTask = zTask;
            this.wTask = wTask;
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            boolean cancel = false;

            if (xTask != null) {
                cancel = xTask.cancel(mayInterruptIfRunning);
            }

            if (yTask != null) {
                cancel |= yTask.cancel(mayInterruptIfRunning);
            }

            if (zTask != null) {
                cancel |= zTask.cancel(mayInterruptIfRunning);
            }

            if (wTask != null) {
                cancel |= wTask.cancel(mayInterruptIfRunning);
            }

            return cancel;
        }

        @Override
        public GLVec4Array get() throws InterruptedException, ExecutionException {
            if (xTask != null) {
                xTask.get();
            }

            if (yTask != null) {
                yTask.get();
            }

            if (zTask != null) {
                zTask.get();
            }

            if (wTask != null) {
                wTask.get();
            }

            return this.out;
        }

        @Override
        public GLVec4Array get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            if (xTask != null) {
                xTask.get(timeout, unit);
            }

            if (yTask != null) {
                yTask.get(timeout, unit);
            }

            if (zTask != null) {
                zTask.get(timeout, unit);
            }

            if (wTask != null) {
                wTask.get(timeout, unit);
            }

            return this.out;
        }

        @Override
        public boolean isCancelled() {
            if (xTask == null && yTask == null && zTask == null && wTask == null) {
                return false;
            }

            boolean result = true;

            if (xTask != null) {
                result &= xTask.isCancelled();
            }

            if (yTask != null) {
                result &= yTask.isCancelled();
            }

            if (zTask != null) {
                result &= zTask.isCancelled();
            }

            if (wTask != null) {
                result &= wTask.isCancelled();
            }

            return result;
        }

        @Override
        public boolean isDone() {
            boolean result = true;

            if (xTask != null) {
                result &= xTask.isDone();
            }

            if (yTask != null) {
                result &= yTask.isDone();
            }

            if (zTask != null) {
                result &= zTask.isDone();
            }

            if (wTask != null) {
                result &= wTask.isDone();
            }

            return result;
        }

    }
}
