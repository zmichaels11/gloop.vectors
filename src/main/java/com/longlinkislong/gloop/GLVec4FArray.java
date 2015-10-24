/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longlinkislong.gloop;

import static com.longlinkislong.gloop.VectorArrays.arrayMultiplyAddF;
import static com.longlinkislong.gloop.VectorArrays.arrayMultiplyF;
import static com.longlinkislong.gloop.VectorArrays.arrayMultiplySubtractF;
import static com.longlinkislong.gloop.VectorArrays.arrayScaleF;
import static com.longlinkislong.gloop.VectorArrays.arraySetF;
import static java.lang.Math.sqrt;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * GLVec4FArray is SoA (Struct of Arrays) implementation of 4 element single
 * precision vectors.
 *
 * @author zmichaels
 * @since 15.10.23
 */
public final class GLVec4FArray {

    private static final ExecutorService TASKS = Executors.newCachedThreadPool();
    private final Map<Object, Integer> objMap = new HashMap<>();
    private Deque<Integer> availableVecs;

    private final int size;
    private final float[] x;
    private final float[] y;
    private final float[] z;
    private final float[] w;

    /**
     * Maps an owner object to an indexed vector.
     *
     * @param owner the index to add.
     * @since 15.10.23
     */
    public void mapVector(Object owner) {
        if (this.availableVecs == null) {
            this.availableVecs = new LinkedList<>(IntStream.range(0, size).boxed().collect(Collectors.toList()));
        }

        final int index = this.availableVecs.poll();

        this.objMap.put(owner, index);
    }

    /**
     * Unmaps an owner object from an indexed vector.
     *
     * @param owner the index to remove.
     * @since 15.10.23
     */
    public void unmapVector(final Object owner) {
        final int index = this.objMap.get(owner);

        this.availableVecs.offer(index);
        this.objMap.remove(owner);
    }

    /**
     * Retrieves a vector by its index.
     *
     * @param owner the index.
     * @return the vector.
     * @since 15.10.23
     */
    public GLVec4F get(Object owner) {
        final int index = this.objMap.get(owner);

        return GLVec4F.create(this.x[index], this.y[index], this.z[index], this.w[index]);
    }

    /**
     * Sets a vector by its index.
     *
     * @param owner the index.
     * @param vec the vector to set.
     * @since 15.10.23
     */
    public void set(final Object owner, final GLVec<?> vec) {
        final int index = this.objMap.get(owner);
        final GLVec4F vecF = vec.asGLVecF().asGLVec4F();

        this.x[index] = vecF.x();
        this.y[index] = vecF.y();
        this.z[index] = vecF.z();
        this.w[index] = vecF.w();
    }

    /**
     * Constructs a new GLVec4FArray with the specified number of elements.
     *
     * @param size the number of vectors the GLVec4FArray will hold.
     * @since 15.10.23
     */
    public GLVec4FArray(final int size) {
        this.size = size;

        this.x = new float[size];
        this.y = new float[size];
        this.z = new float[size];
        this.w = new float[size];
    }

    /**
     * Retrieves the number of vectors held by the array.
     *
     * @return the number of GLVec4F objects held.
     * @since 15.10.23
     */
    public int length() {
        return this.size;
    }

    /**
     * Retrieves a GLVec4F from the internal array.
     *
     * @param index the index of the element.
     * @return the GLVec4F.
     * @since 15.10.23
     */
    public GLVec4F get(final int index) {
        return GLVec4F.create(this.x[index], this.y[index], this.z[index], this.w[index]);
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
    public <VecT extends GLVec4F> void get(final int readIndex, final VecT[] out, final int writeIndex, final int count) {
        for (int i = 0; i < count; i++) {
            out[writeIndex + i].set(this.x[readIndex + i], this.y[readIndex + i], this.z[readIndex + i], this.w[readIndex + i]);
        }
    }

    /**
     * Constructs a stream of GLVec4F objects constructed by the data held by
     * this array.
     *
     * @return the stream.
     * @since 15.10.23
     */
    public Stream<GLVec4F> stream() {
        return this.stream(0, this.length());
    }

    /**
     * Constructs a stream of GLVec4F objects on the specified range.
     *
     * @param startInclusive the starting range, inclusive.
     * @param endExclusive the ending range, exclusive.
     * @return the stream.
     * @since 15.10.23
     */
    public Stream<GLVec4F> stream(final int startInclusive, final int endExclusive) {
        return IntStream.range(startInclusive, endExclusive).mapToObj(this::get);
    }

    /**
     * Retrieves a chunk of x-elements.
     *
     * @param readOffset the offset to begin reading.
     * @param out the array to write the elements to.
     * @param writeOffset the offset to begin writing.
     * @param count the number of elements to read.
     * @since 15.10.23
     */
    public void getX(final int readOffset, final float[] out, final int writeOffset, final int count) {
        System.arraycopy(this.x, readOffset, out, writeOffset, count);
    }

    /**
     * Retrieves a chunk of y-elements.
     *
     * @param readOffset the offset to begin reading.
     * @param out the array to write the elements to.
     * @param writeOffset the offset to begin writing.
     * @param count the number of elements to read.
     * @since 15.10.23
     */
    public void getY(final int readOffset, final float[] out, final int writeOffset, final int count) {
        System.arraycopy(this.y, readOffset, out, writeOffset, count);
    }

    /**
     * Retrieves a chunk of z-elements.
     *
     * @param readOffset the offset to begin reading.
     * @param out the array to write the elements to.
     * @param writeOffset the offset to begin writing.
     * @param count the number of elements to read.
     * @since 15.10.23
     */
    public void getZ(final int readOffset, final float[] out, final int writeOffset, final int count) {
        System.arraycopy(this.z, readOffset, out, writeOffset, count);
    }

    /**
     * Retrieves a chunk of w-elements.
     *
     * @param readOffset the offset to begin reading.
     * @param out the array to write the elements to.
     * @param writeOffset the offset to begin writing.
     * @param count the number of elements to read.
     * @since 15.10.23
     */
    public void getW(final int readOffset, final float[] out, final int writeOffset, final int count) {
        System.arraycopy(this.w, readOffset, out, writeOffset, count);
    }

    /**
     * Sets each x-element to the specified value.
     *
     * @param offset the offset to begin writing.
     * @param value the value to set.
     * @param count the number of elements to set.
     * @since 15.10.23
     */
    public void setX(final int offset, final float value, final int count) {
        arraySetF(this.x, offset, value, count);
    }

    /**
     * Sets each y-element to the specified value.
     *
     * @param offset the offset to begin writing.
     * @param value the value to set.
     * @param count the number of elements to set.
     * @since 15.10.23
     */
    public void setY(final int offset, final float value, final int count) {
        arraySetF(this.y, offset, value, count);
    }

    /**
     * Sets each z-element to the specified value.
     *
     * @param offset the offset to begin writing.
     * @param value the value to set.
     * @param count the number of elements to set.
     * @since 15.10.23
     */
    public void setZ(final int offset, final float value, final int count) {
        arraySetF(this.z, offset, value, count);
    }

    /**
     * Sets each y-element to the specified value.
     *
     * @param offset the offset to begin writing.
     * @param value the value to set.
     * @param count the number of elements to set.
     * @since 15.10.23
     */
    public void setW(final int offset, final float value, final int count) {
        arraySetF(this.w, offset, value, count);
    }

    /**
     * Assigns a segment of vectors to the specified value.
     *
     * @param offset the offset to begin writing.
     * @param vec the vector to assign.
     * @param count the number of sequential vectors to assign.
     * @since 15.10.23
     */
    public void set(final int offset, final GLVec<?> vec, final int count) {
        final GLVec4F vecF = vec.asGLVecF().asGLVec4F();

        for (int i = 0; i < count; i++) {
            this.x[offset + i] = vecF.x();
            this.y[offset + i] = vecF.y();
            this.z[offset + i] = vecF.z();
            this.w[offset + i] = vecF.w();
        }
    }

    /**
     * Assigns a segment of vectors to values read from an array of vectors.
     *
     * @param <VecT> the type of vector array.
     * @param writeIndex the offset to begin writing.
     * @param read the array of vectors to read from.
     * @param readIndex the offset to begin reading.
     * @param count the number of elements to process.
     * @since 15.10.23
     */
    public <VecT extends GLVec<?>> void set(final int writeIndex, final VecT[] read, final int readIndex, final int count) {
        for (int i = 0; i < count; i++) {
            this.set(writeIndex + i, read[readIndex + i], 1);
        }
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
     * @since 15.10.23
     */
    public static void cross(
            final GLVec4FArray out, final int outOffset,
            final GLVec4FArray in0, final int in0Offset,
            final GLVec4FArray in1, final int in1Offset,
            final int count) {

        arrayMultiplyF(out.x, outOffset, in0.z, in0Offset, in1.y, in1Offset, count);
        arrayMultiplyF(out.y, outOffset, in0.x, in0Offset, in1.z, in1Offset, count);
        arrayMultiplyF(out.z, outOffset, in0.y, in0Offset, in1.x, in1Offset, count);

        arrayMultiplySubtractF(out.x, outOffset, in0.y, in0Offset, in1.z, in1Offset, out.x, outOffset, count);
        arrayMultiplySubtractF(out.y, outOffset, in0.z, in0Offset, in1.x, in1Offset, out.y, outOffset, count);
        arrayMultiplySubtractF(out.z, outOffset, in0.x, in0Offset, in1.y, in1Offset, out.z, outOffset, count);
        arraySetF(out.w, outOffset, 1.0f, count);
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
     * @param waitForComplete signals if the operation should busy wait until
     * the task completes.
     * @since 15.10.23
     */
    public static void crossAsync(
            final GLVec4FArray out, final int outOffset,
            final GLVec4FArray in0, final int in0Offset,
            final GLVec4FArray in1, final int in1Offset,
            final int count, final boolean waitForComplete) {

        {
            final Future<?> taskX = TASKS.submit(() -> arrayMultiplyF(out.x, outOffset, in0.z, in0Offset, in1.y, in1Offset, count));
            final Future<?> taskY = TASKS.submit(() -> arrayMultiplyF(out.y, outOffset, in0.x, in0Offset, in1.z, in1Offset, count));
            final Future<?> taskZ = TASKS.submit(() -> arrayMultiplyF(out.z, outOffset, in0.y, in0Offset, in1.x, in1Offset, count));

            while (!taskX.isDone() || !taskY.isDone() || !taskZ.isDone()) {
                Thread.yield();
            }
        }

        final Future<?> taskX = TASKS.submit(() -> arrayMultiplySubtractF(out.x, outOffset, in0.y, in0Offset, in1.z, in1Offset, out.x, outOffset, count));
        final Future<?> taskY = TASKS.submit(() -> arrayMultiplySubtractF(out.y, outOffset, in0.z, in0Offset, in1.x, in1Offset, out.y, outOffset, count));
        final Future<?> taskZ = TASKS.submit(() -> arrayMultiplySubtractF(out.z, outOffset, in0.x, in0Offset, in1.y, in1Offset, out.z, outOffset, count));
        final Future<?> taskW = TASKS.submit(() -> arraySetF(out.w, outOffset, 1.0f, count));

        if (waitForComplete) {
            while (!taskX.isDone() || !taskY.isDone() || !taskZ.isDone() || !taskW.isDone()) {
                Thread.yield();
            }
        }
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
     * @since 15.10.23
     */
    public static void dot(
            final float[] out, final int outOffset,
            final GLVec4FArray in0, final int in0Offset,
            final GLVec4FArray in1, final int in1Offset,
            final int count) {

        // out = in0.x * in1.x
        arrayMultiplyF(out, outOffset, in0.x, in0Offset, in1.x, in1Offset, count);
        // out = in0.y * in1.y + out
        arrayMultiplyAddF(out, outOffset, in0.y, in0Offset, in1.y, in1Offset, out, outOffset, count);
        // out = in0.z * in1.z + out
        arrayMultiplyAddF(out, outOffset, in0.z, in0Offset, in1.z, in1Offset, out, outOffset, count);
        // out = in0.w * in1.w + out
        arrayMultiplyAddF(out, outOffset, in0.w, in0Offset, in1.w, in1Offset, out, outOffset, count);
    }

    /**
     * Calculates the length of each vector.
     *
     * @param out the array to store the vector length in.
     * @param outOffset the offset to begin writing the outputs.
     * @param in0 the array to read the inputs from.
     * @param in0Offset the offset to begin reading the inputs.
     * @param count the number of elements to process.
     * @since 15.10.23
     */
    public static void length(
            final float[] out, final int outOffset,
            final GLVec4FArray in0, final int in0Offset,
            final int count) {

        for (int i = 0; i < count; i++) {
            final float x = in0.x[i + in0Offset];
            final float y = in0.y[i + in0Offset];
            final float z = in0.z[i + in0Offset];
            final float w = in0.w[i + in0Offset];

            out[i + outOffset] = (float) sqrt(x * x + y * y + z * z + w * w);
        }
    }

    /**
     * Normalizes the vectors.
     *
     * @param out the array of vectors to write the results to.
     * @param outOffset the offset to begin writing.
     * @param in0 the input array of vectors.
     * @param in0Offset the offset to begin reading.
     * @param count the number of elements to process.
     * @since 15.10.23
     */
    public static void normalize(
            final GLVec4FArray out, final int outOffset,
            final GLVec4FArray in0, final int in0Offset,
            final int count) {

        for (int i = 0; i < count; i++) {
            final float x = in0.x[i + in0Offset];
            final float y = in0.y[i + in0Offset];
            final float z = in0.z[i + in0Offset];
            final float w = in0.w[i + in0Offset];

            final float scale = 1.0f / (float) sqrt(x * x + y * y + z * z + w * w);

            out.x[i + outOffset] = scale * x;
            out.y[i + outOffset] = scale * y;
            out.z[i + outOffset] = scale * z;
            out.w[i + outOffset] = scale * w;
        }
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
     * @since 15.10.23
     */
    public static void apply(
            final VectorArrays.UnaryOp<float[]> op,
            final GLVec4FArray out, final int outOffset,
            final GLVec4FArray in0, final int in0Offset,
            final int count) {

        op.apply(out.x, outOffset, in0.x, in0Offset, count);
        op.apply(out.y, outOffset, in0.y, in0Offset, count);
        op.apply(out.z, outOffset, in0.z, in0Offset, count);
        op.apply(out.w, outOffset, in0.w, in0Offset, count);
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
     * @param waitForComplete signals if the operation should busy wait until
     * the task completes.
     * @since 15.10.23
     */
    public static void applyAsync(
            final VectorArrays.UnaryOp<float[]> op,
            final GLVec4FArray out, final int outOffset,
            final GLVec4FArray in0, final int in0Offset,
            final int count, final boolean waitForComplete) {

        final Future<?> taskX = TASKS.submit(() -> op.apply(out.x, outOffset, in0.x, in0Offset, count));
        final Future<?> taskY = TASKS.submit(() -> op.apply(out.y, outOffset, in0.y, in0Offset, count));
        final Future<?> taskZ = TASKS.submit(() -> op.apply(out.z, outOffset, in0.z, in0Offset, count));
        final Future<?> taskW = TASKS.submit(() -> op.apply(out.w, outOffset, in0.w, in0Offset, count));

        if (waitForComplete) {
            while (!taskX.isDone() || !taskY.isDone() || !taskZ.isDone() || !taskW.isDone()) {
                Thread.yield();
            }
        }
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
     * @since 15.10.23
     */
    public static void apply(
            final VectorArrays.BinaryOp<float[]> op,
            final GLVec4FArray out, final int outOffset,
            final GLVec4FArray in0, final int in0Offset,
            final GLVec4FArray in1, final int in1Offset,
            final int count) {

        op.apply(out.x, outOffset, in0.x, in0Offset, in1.x, in1Offset, count);
        op.apply(out.y, outOffset, in0.y, in0Offset, in1.y, in1Offset, count);
        op.apply(out.z, outOffset, in0.z, in0Offset, in1.z, in1Offset, count);
        op.apply(out.w, outOffset, in0.w, in0Offset, in1.w, in1Offset, count);
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
     * @param waitForComplete signals if the operation should busy wait until
     * the task completes.
     * @since 15.10.23
     */
    public static void applyAsync(
            final VectorArrays.BinaryOp<float[]> op,
            final GLVec4FArray out, final int outOffset,
            final GLVec4FArray in0, final int in0Offset,
            final GLVec4FArray in1, final int in1Offset,
            final int count, final boolean waitForComplete) {

        final Future<?> taskX = TASKS.submit(() -> op.apply(out.x, outOffset, in0.x, in0Offset, in1.x, in1Offset, count));
        final Future<?> taskY = TASKS.submit(() -> op.apply(out.y, outOffset, in0.y, in0Offset, in1.y, in1Offset, count));
        final Future<?> taskZ = TASKS.submit(() -> op.apply(out.z, outOffset, in0.z, in0Offset, in1.z, in1Offset, count));
        final Future<?> taskW = TASKS.submit(() -> op.apply(out.w, outOffset, in0.w, in0Offset, in1.w, in1Offset, count));

        if (waitForComplete) {
            while (!taskX.isDone() || !taskY.isDone() || !taskZ.isDone() || !taskW.isDone()) {
                Thread.yield();
            }
        }
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
     * @since 15.10.23
     */
    public static void apply(
            final VectorArrays.TernaryOp<float[]> op,
            final GLVec4FArray out, final int outOffset,
            final GLVec4FArray in0, final int in0Offset,
            final GLVec4FArray in1, final int in1Offset,
            final GLVec4FArray in2, final int in2Offset,
            final int count) {

        op.apply(out.x, outOffset, in0.x, in0Offset, in1.x, in1Offset, in2.x, in1Offset, count);
        op.apply(out.y, outOffset, in0.y, in0Offset, in1.y, in1Offset, in2.y, in2Offset, count);
        op.apply(out.z, outOffset, in0.z, in0Offset, in1.z, in1Offset, in2.z, in2Offset, count);
        op.apply(out.w, outOffset, in0.w, in0Offset, in1.w, in1Offset, in2.w, in2Offset, count);
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
     * @param waitForComplete signals if the operation should busy wait until
     * the task completes.
     * @since 15.10.23
     */
    public static void applyAsync(
            final VectorArrays.TernaryOp<float[]> op,
            final GLVec4FArray out, final int outOffset,
            final GLVec4FArray in0, final int in0Offset,
            final GLVec4FArray in1, final int in1Offset,
            final GLVec4FArray in2, final int in2Offset,
            final int count, final boolean waitForComplete) {

        final Future<?> taskX = TASKS.submit(() -> op.apply(out.x, outOffset, in0.x, in0Offset, in1.x, in1Offset, in2.x, in2Offset, count));
        final Future<?> taskY = TASKS.submit(() -> op.apply(out.y, outOffset, in0.y, in0Offset, in1.y, in1Offset, in2.y, in2Offset, count));
        final Future<?> taskZ = TASKS.submit(() -> op.apply(out.z, outOffset, in0.z, in0Offset, in1.z, in1Offset, in2.z, in2Offset, count));
        final Future<?> taskW = TASKS.submit(() -> op.apply(out.w, outOffset, in0.w, in0Offset, in1.w, in1Offset, in2.w, in2Offset, count));

        if (waitForComplete) {
            while (!taskX.isDone() || !taskY.isDone() || !taskZ.isDone() || !taskW.isDone()) {
                Thread.yield();
            }
        }
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
     * @since 15.10.23
     */
    public static void scale(
            final GLVec4FArray out, final int outOffset,
            final GLVec4FArray in0, final int in0Offset,
            final float scale,
            final int count) {

        arrayScaleF(out.x, outOffset, in0.x, in0Offset, scale, count);
        arrayScaleF(out.y, outOffset, in0.y, in0Offset, scale, count);
        arrayScaleF(out.z, outOffset, in0.z, in0Offset, scale, count);
        arrayScaleF(out.w, outOffset, in0.w, in0Offset, scale, count);
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
     * @param waitForComplete signals if the operation should busy wait until
     * the task completes.
     * @since 15.10.23
     */
    public static void scaleAsync(
            final GLVec4FArray out, final int outOffset,
            final GLVec4FArray in0, final int in0Offset,
            final float scale,
            final int count, final boolean waitForComplete) {

        final Future<?> taskX = TASKS.submit(() -> arrayScaleF(out.x, outOffset, in0.x, in0Offset, scale, count));
        final Future<?> taskY = TASKS.submit(() -> arrayScaleF(out.y, outOffset, in0.y, in0Offset, scale, count));
        final Future<?> taskZ = TASKS.submit(() -> arrayScaleF(out.z, outOffset, in0.z, in0Offset, scale, count));
        final Future<?> taskW = TASKS.submit(() -> arrayScaleF(out.w, outOffset, in0.w, in0Offset, scale, count));

        if (waitForComplete) {
            while (!taskX.isDone() || !taskY.isDone() || !taskZ.isDone() || !taskW.isDone()) {
                Thread.yield();
            }
        }
    }

    public ByteBuffer wrapX(final int offset, final int count) {
        final ByteBuffer out = ByteBuffer.allocateDirect(Float.BYTES * count).order(ByteOrder.nativeOrder());

        for (int i = 0; i < count; i++) {
            out.putFloat(this.x[offset + i]);
        }

        out.flip();

        return out;
    }

    public void setX(final int writeOffset, final ByteBuffer data, final int count) {
        for (int i = 0; i < count; i++) {
            this.x[writeOffset + i] = data.getFloat();
        }
    }

    public ByteBuffer wrapY(final int offset, final int count) {
        final ByteBuffer out = ByteBuffer.allocateDirect(Float.BYTES * count).order(ByteOrder.nativeOrder());

        for (int i = 0; i < count; i++) {
            out.putFloat(this.y[offset + i]);
        }

        out.flip();

        return out;
    }

    public void setY(final int writeOffset, final ByteBuffer data, final int count) {
        for (int i = 0; i < count; i++) {
            this.y[writeOffset + i] = data.getFloat();
        }
    }

    public ByteBuffer wrapZ(final int offset, final int count) {
        final ByteBuffer out = ByteBuffer.allocateDirect(Float.BYTES * count).order(ByteOrder.nativeOrder());

        for (int i = 0; i < count; i++) {
            out.putFloat(this.z[offset + i]);
        }

        out.flip();

        return out;
    }

    public void setZ(final int writeOffset, final ByteBuffer data, final int count) {
        for (int i = 0; i < count; i++) {
            this.z[writeOffset + i] = data.getFloat();
        }
    }

    public ByteBuffer wrapW(final int offset, final int count) {
        final ByteBuffer out = ByteBuffer.allocateDirect(Float.BYTES * count).order(ByteOrder.nativeOrder());

        for (int i = 0; i < count; i++) {
            out.putFloat(this.w[offset + i]);
        }

        out.flip();

        return out;
    }

    public void setW(final int writeOffset, final ByteBuffer data, final int count) {
        for (int i = 0; i < count; i++) {
            this.w[writeOffset + i] = data.getFloat();
        }
    }
}
