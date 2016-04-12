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
import static java.lang.Math.sqrt;
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
 *
 * @author zmichaels
 */
public class GLVec3Array {

    public final int length;
    public final double[] x;
    public final double[] y;
    public final double[] z;

    public GLVec3Array(final int size) {
        this.length = size;

        this.x = new double[size];
        this.y = new double[size];
        this.z = new double[size];
    }

    public GLVec3D get(final int index) {
        return GLVec3D.create(this.x[index], this.y[index], this.z[index]);
    }

    public <VecT extends GLVec3D> void get(final int readIndex, final VecT[] out, final int writeIndex, final int count) {
        for (int i = 0; i < count; i++) {
            out[writeIndex + i].set(this.x[readIndex + i], this.y[readIndex + i], this.z[readIndex + i]);
        }
    }

    public DoubleStream flatStream() {
        return Stream.of(x, y, z).flatMapToDouble(Arrays::stream);
    }

    public DoubleStream flatInterlacedStream() {
        final int[] index = {0};

        return DoubleStream.generate(() -> {
            final int idx = index[0];

            index[0]++;
            switch (idx % 3) {
                case 0:
                    return x[idx / 3];
                case 1:
                    return y[idx / 3];
                case 2:
                    return z[idx / 3];
                default: // this case cannot happen
                    throw new IllegalStateException("# % 3 >= 2!");
            }

        });
    }

    public Stream<GLVec3D> stream(final int startInclusive, final int endExclusive) {
        return IntStream.range(startInclusive, endExclusive).mapToObj(this::get);
    }

    public Stream<GLVec3D> stream() {
        return this.stream(0, this.length);
    }

    private static GLVec3Array ensureArray(
            final GLVec3Array in,
            final int offset,
            final int neededSize) {

        if (in == null || in.length < offset + neededSize) {
            return new GLVec3Array(offset + neededSize);
        } else {
            return in;
        }
    }

    public static double[] ensureArray(
            final double[] in,
            final int offset,
            final int neededSize) {

        if (in == null || in.length < offset + neededSize) {
            return new double[offset + neededSize];
        } else {
            return in;
        }
    }

    public GLVec3Array cross(final GLVec3Array out, final GLVec3Array other) {
        return cross(out, 0, this, 0, other, 0, this.length);
    }

    public static GLVec3Array cross(
            final GLVec3Array out, final int outOffset,
            final GLVec3Array in0, final int in0Offset,
            final GLVec3Array in1, final int in1Offset,
            final int count) {

        final GLVec3Array res = ensureArray(out, outOffset, count);

        arrayMultiplyD(res.x, outOffset, in0.z, in0Offset, in1.y, in1Offset, count);
        arrayMultiplyD(res.y, outOffset, in0.x, in0Offset, in1.z, in1Offset, count);
        arrayMultiplyD(res.z, outOffset, in0.y, in0Offset, in1.x, in1Offset, count);

        arrayMultiplySubtractD(res.x, outOffset, in0.y, in0Offset, in1.z, in1Offset, res.x, outOffset, count);
        arrayMultiplySubtractD(res.y, outOffset, in0.z, in0Offset, in1.x, in1Offset, res.y, outOffset, count);
        arrayMultiplySubtractD(res.z, outOffset, in0.x, in0Offset, in1.y, in1Offset, res.z, outOffset, count);

        return res;
    }

    public Future<GLVec3Array> crossAsync(final GLVec3Array out, final GLVec3Array other) {
        return crossAsync(out, 0, this, 0, other, 0, this.length);
    }

    public static Future<GLVec3Array> crossAsync(
            final GLVec3Array out, final int outOffset,
            final GLVec3Array in0, final int in0Offset,
            final GLVec3Array in1, final int in1Offset,
            final int count) {

        final GLVec3Array res = ensureArray(out, outOffset, count);

        X_TASKS.submit(() -> arrayMultiplyD(res.x, outOffset, in0.z, in0Offset, in1.y, in1Offset, count));
        Y_TASKS.submit(() -> arrayMultiplyD(res.y, outOffset, in0.x, in0Offset, in1.z, in1Offset, count));
        Z_TASKS.submit(() -> arrayMultiplyD(res.z, outOffset, in0.y, in0Offset, in1.x, in1Offset, count));

        // these tasks wont start until the previous XYZ tasks complete.
        final Future<?> taskX = X_TASKS.submit(() -> arrayMultiplySubtractD(res.x, outOffset, in0.y, in0Offset, in1.z, in1Offset, res.x, outOffset, count));
        final Future<?> taskY = Y_TASKS.submit(() -> arrayMultiplySubtractD(res.y, outOffset, in0.z, in0Offset, in1.x, in1Offset, res.y, outOffset, count));
        final Future<?> taskZ = Z_TASKS.submit(() -> arrayMultiplySubtractD(res.z, outOffset, in0.x, in0Offset, in1.y, in1Offset, res.z, outOffset, count));

        return new FutureGLVec3Array(out, taskX, taskY, taskZ);
    }

    public double[] dot(final double[] out, final GLVec3Array other) {
        return dot(out, 0, other, 0, this, 0, this.length);
    }

    public static double[] dot(
            final double[] out, final int outOffset,
            final GLVec3Array in0, final int in0Offset,
            final GLVec3Array in1, final int in1Offset,
            final int count) {

        final double[] res = ensureArray(out, outOffset, count);

        // out = in0.x * in1.x
        arrayMultiplyD(res, outOffset, in0.x, in0Offset, in1.x, in1Offset, count);
        // out = in0.y * in1.y + out
        arrayMultiplyAddD(res, outOffset, in0.y, in0Offset, in1.y, in1Offset, res, outOffset, count);
        // out = in0.z * in1.z + out
        arrayMultiplyAddD(res, outOffset, in0.z, in0Offset, in1.z, in1Offset, res, outOffset, count);

        return res;
    }

    public double[] length(final double[] out) {
        return length(out, 0, this, 0, this.length);
    }

    public static double[] length(
            final double[] out, final int outOffset,
            final GLVec3Array in0, final int in0Offset,
            final int count) {

        final double[] res = ensureArray(out, outOffset, count);

        for (int i = 0; i < count; i++) {
            final double x = in0.x[i + in0Offset];
            final double y = in0.y[i + in0Offset];
            final double z = in0.z[i + in0Offset];

            res[i + outOffset] = sqrt(x * x + y * y + z * z);
        }

        return res;
    }

    public double[] length2(final double[] out) {
        return length2(out, 0, this, 0, this.length);
    }

    public static double[] length2(
            final double[] out, final int outOffset,
            final GLVec3Array in0, final int in0Offset,
            final int count) {

        final double[] res = ensureArray(out, outOffset, count);

        for (int i = 0; i < count; i++) {
            final double x = in0.x[i + in0Offset];
            final double y = in0.y[i + in0Offset];
            final double z = in0.z[i + in0Offset];

            res[i + outOffset] = x * x + y * y + z * z;
        }

        return res;
    }

    public static GLVec3Array normalize(
            final GLVec3Array out, final int outOffset,
            final GLVec3Array in0, final int in0Offset,
            final int count) {

        final GLVec3Array res = ensureArray(out, outOffset, count);

        for (int i = 0; i < count; i++) {
            final double x = in0.x[i + in0Offset];
            final double y = in0.y[i + in0Offset];
            final double z = in0.z[i + in0Offset];

            final double scale = 1.0 / sqrt(x * x + y * y + z * z);

            res.x[i + outOffset] = scale * x;
            res.y[i + outOffset] = scale * y;
            res.z[i + outOffset] = scale * z;
        }

        return res;
    }

    public GLVec3Array apply(
            final VectorArrays.UnaryOp<double[]> op,
            final GLVec3Array out) {

        return apply(op, out, 0, this, 0, this.length);
    }

    public static GLVec3Array apply(
            final VectorArrays.UnaryOp<double[]> op,
            final GLVec3Array out, final int outOffset,
            final GLVec3Array in0, final int in0Offset,
            final int count) {

        final GLVec3Array res = ensureArray(out, outOffset, count);

        op.apply(res.x, outOffset, in0.x, in0Offset, count);
        op.apply(res.y, outOffset, in0.y, in0Offset, count);
        op.apply(res.z, outOffset, in0.z, in0Offset, count);

        return res;
    }

    public Future<GLVec3Array> applyAsync(
            final VectorArrays.UnaryOp<double[]> op,
            final GLVec3Array out) {

        return applyAsync(op, out, 0, this, 0, this.length);
    }

    public static Future<GLVec3Array> applyAsync(
            final VectorArrays.UnaryOp<double[]> op,
            final GLVec3Array out, final int outOffset,
            final GLVec3Array in0, final int in0Offset,
            final int count) {

        final GLVec3Array res = ensureArray(out, outOffset, count);
        final Future<?> taskX = X_TASKS.submit(() -> op.apply(res.x, outOffset, in0.x, in0Offset, count));
        final Future<?> taskY = Y_TASKS.submit(() -> op.apply(res.y, outOffset, in0.y, in0Offset, count));
        final Future<?> taskZ = Z_TASKS.submit(() -> op.apply(res.z, outOffset, in0.z, in0Offset, count));

        return new FutureGLVec3Array(res, taskX, taskY, taskZ);
    }

    public GLVec3Array apply(
            final VectorArrays.BinaryOp<double[]> op,
            final GLVec3Array out,
            final GLVec3Array other) {

        return apply(op, out, 0, this, 0, other, 0, this.length);
    }

    public static GLVec3Array apply(
            final VectorArrays.BinaryOp<double[]> op,
            final GLVec3Array out, final int outOffset,
            final GLVec3Array in0, final int in0Offset,
            final GLVec3Array in1, final int in1Offset,
            final int count) {

        final GLVec3Array res = ensureArray(out, outOffset, count);

        op.apply(res.x, outOffset, in0.x, in0Offset, in1.x, in1Offset, count);
        op.apply(res.y, outOffset, in0.y, in0Offset, in1.y, in1Offset, count);
        op.apply(res.z, outOffset, in0.z, in0Offset, in1.z, in1Offset, count);

        return res;
    }

    public Future<GLVec3Array> applyAsync(
            final VectorArrays.BinaryOp<double[]> op,
            final GLVec3Array out,
            final GLVec3Array other) {

        return applyAsync(op, out, 0, this, 0, other, 0, this.length);
    }

    public static Future<GLVec3Array> applyAsync(
            final VectorArrays.BinaryOp<double[]> op,
            final GLVec3Array out, final int outOffset,
            final GLVec3Array in0, final int in0Offset,
            final GLVec3Array in1, final int in1Offset,
            final int count) {

        final GLVec3Array res = ensureArray(out, outOffset, count);
        final Future<?> taskX = X_TASKS.submit(() -> op.apply(res.x, outOffset, in0.x, in0Offset, in1.x, in1Offset, count));
        final Future<?> taskY = Y_TASKS.submit(() -> op.apply(res.y, outOffset, in0.y, in0Offset, in1.y, in1Offset, count));
        final Future<?> taskZ = Z_TASKS.submit(() -> op.apply(res.z, outOffset, in0.z, in0Offset, in1.z, in1Offset, count));

        return new FutureGLVec3Array(res, taskX, taskY, taskZ);
    }

    public GLVec3Array apply(
            final VectorArrays.TernaryOp<double[]> op,
            final GLVec3Array out,
            final GLVec3Array other0,
            final GLVec3Array other1) {

        return apply(op, out, 0, this, 0, other0, 0, other1, 0, this.length);
    }

    public static GLVec3Array apply(
            final VectorArrays.TernaryOp<double[]> op,
            final GLVec3Array out, final int outOffset,
            final GLVec3Array in0, final int in0Offset,
            final GLVec3Array in1, final int in1Offset,
            final GLVec3Array in2, final int in2Offset,
            final int count) {

        final GLVec3Array res = ensureArray(out, outOffset, count);

        op.apply(res.x, outOffset, in0.x, in0Offset, in1.x, in1Offset, in2.x, in1Offset, count);
        op.apply(res.y, outOffset, in0.y, in0Offset, in1.y, in1Offset, in2.y, in2Offset, count);
        op.apply(res.z, outOffset, in0.z, in0Offset, in1.z, in1Offset, in2.z, in2Offset, count);

        return res;
    }

    public Future<GLVec3Array> applyAsync(
            final VectorArrays.TernaryOp<double[]> op,
            final GLVec3Array out,
            final GLVec3Array other0,
            final GLVec3Array other1) {

        return applyAsync(op, out, 0, this, 0, other0, 0, other1, 0, this.length);
    }

    public static Future<GLVec3Array> applyAsync(
            final VectorArrays.TernaryOp<double[]> op,
            final GLVec3Array out, final int outOffset,
            final GLVec3Array in0, final int in0Offset,
            final GLVec3Array in1, final int in1Offset,
            final GLVec3Array in2, final int in2Offset,
            final int count) {

        final GLVec3Array res = ensureArray(out, outOffset, count);
        final Future<?> taskX = X_TASKS.submit(() -> op.apply(res.x, outOffset, in0.x, in0Offset, in1.x, in1Offset, in2.x, in2Offset, count));
        final Future<?> taskY = Y_TASKS.submit(() -> op.apply(res.y, outOffset, in0.y, in0Offset, in1.y, in1Offset, in2.y, in2Offset, count));
        final Future<?> taskZ = Z_TASKS.submit(() -> op.apply(res.z, outOffset, in0.z, in0Offset, in1.z, in1Offset, in2.z, in2Offset, count));

        return new FutureGLVec3Array(res, taskX, taskY, taskZ);
    }

    public GLVec3Array scale(final GLVec3Array out, final GLVec3 scale) {
        return scale(out, 0, this, 0, scale, this.length);
    }

    public static GLVec3Array scale(
            final GLVec3Array out, final int outOffset,
            final GLVec3Array in0, final int in0Offset,
            final GLVec3 scale,
            final int count) {

        final GLVec3Array res = ensureArray(out, outOffset, count);
        final GLVec3D scaleD = scale.asGLVec3D();
        final double x = scaleD.x();
        final double y = scaleD.y();
        final double z = scaleD.z();

        arrayScaleD(res.x, outOffset, in0.x, in0Offset, x, count);
        arrayScaleD(res.y, outOffset, in0.y, in0Offset, y, count);
        arrayScaleD(res.z, outOffset, in0.z, in0Offset, z, count);

        return res;
    }

    public Future<GLVec3Array> scaleAsync(final GLVec3Array out, final GLVec3 scale) {
        return scaleAsync(out, 0, this, 0, scale, this.length);
    }

    public static Future<GLVec3Array> scaleAsync(
            final GLVec3Array out, final int outOffset,
            final GLVec3Array in0, final int in0Offset,
            final GLVec3 scale,
            final int count) {

        final GLVec3Array res = ensureArray(out, outOffset, count);
        final GLVec3D scaleD = scale.asGLVec3D();
        final double x = scaleD.x();
        final double y = scaleD.y();
        final double z = scaleD.z();

        final Future<?> taskX = X_TASKS.submit(() -> arrayScaleD(res.x, outOffset, in0.x, in0Offset, x, count));
        final Future<?> taskY = Y_TASKS.submit(() -> arrayScaleD(res.y, outOffset, in0.y, in0Offset, y, count));
        final Future<?> taskZ = Z_TASKS.submit(() -> arrayScaleD(res.z, outOffset, in0.z, in0Offset, z, count));

        return new FutureGLVec3Array(out, taskX, taskY, taskZ);
    }

    public GLVec3Array addConstant(final GLVec3Array out, final GLVec3 vec) {
        return addConstant(out, 0, this, 0, vec, this.length);
    }

    public static GLVec3Array addConstant(
            final GLVec3Array out, final int outOffset,
            final GLVec3Array in0, final int in0Offset,
            final GLVec3 vec,
            final int count) {

        final GLVec3Array res = ensureArray(out, outOffset, count);
        final GLVec3D vecF = vec.asGLVec3D();

        arrayAddConstantD(res.x, outOffset, in0.x, in0Offset, vecF.x(), count);
        arrayAddConstantD(res.y, outOffset, in0.y, in0Offset, vecF.y(), count);
        arrayAddConstantD(res.z, outOffset, in0.z, in0Offset, vecF.z(), count);

        return res;
    }

    public Future<GLVec3Array> addConstantAsync(final GLVec3Array out, final GLVec3 vec) {
        return addConstantAsync(out, 0, this, 0, vec, this.length);
    }

    public static Future<GLVec3Array> addConstantAsync(
            final GLVec3Array out, final int outOffset,
            final GLVec3Array in0, final int in0Offset,
            final GLVec3 vec,
            final int count) {

        final GLVec3Array res = ensureArray(out, outOffset, count);
        final GLVec3D vecD = vec.asGLVec3D();
        final double x = vecD.x();
        final double y = vecD.y();
        final double z = vecD.z();

        final Future<?> taskX = X_TASKS.submit(() -> arrayAddConstantD(res.x, outOffset, in0.x, in0Offset, x, count));
        final Future<?> taskY = Y_TASKS.submit(() -> arrayAddConstantD(res.y, outOffset, in0.y, in0Offset, y, count));
        final Future<?> taskZ = Z_TASKS.submit(() -> arrayAddConstantD(res.z, outOffset, in0.z, in0Offset, z, count));

        return new FutureGLVec3Array(res, taskX, taskY, taskZ);
    }

    public GLVec3Array setConstant(final GLVec3 vec) {
        return setConstant(this, 0, vec, this.length);
    }

    public static GLVec3Array setConstant(
            final GLVec3Array out, final int outOffset,
            final GLVec3 vec,
            final int count) {

        final GLVec3Array res = ensureArray(out, outOffset, count);
        final GLVec3D vecD = vec.asGLVec3D();
        final double x = vecD.x();
        final double y = vecD.y();
        final double z = vecD.z();

        VectorArrays.arraySetD(res.x, outOffset, x, count);
        VectorArrays.arraySetD(res.y, outOffset, y, count);
        VectorArrays.arraySetD(res.z, outOffset, z, count);

        return res;
    }

    public Future<GLVec3Array> setConstantAsync(final GLVec3 vec) {

        return setConstantAsync(this, 0, vec, this.length);
    }

    public static Future<GLVec3Array> setConstantAsync(
            final GLVec3Array out, final int outOffset,
            final GLVec3 vec,
            final int count) {

        final GLVec3Array res = ensureArray(out, outOffset, count);
        final GLVec3D vecF = vec.asGLVec3D();

        final Future<?> taskX = X_TASKS.submit(() -> arraySetD(res.x, outOffset, vecF.x(), count));
        final Future<?> taskY = Y_TASKS.submit(() -> arraySetD(res.y, outOffset, vecF.y(), count));
        final Future<?> taskZ = Z_TASKS.submit(() -> arraySetD(res.z, outOffset, vecF.z(), count));

        return new FutureGLVec3Array(res, taskX, taskY, taskZ);
    }    
    
    public double[] flatten(
            final VectorArrays.TernaryOp<double[]> op,
            final double[] out) {

        return flatten(op, out, 0, this, 0, this.length);
    }

    public static double[] flatten(
            final VectorArrays.TernaryOp<double[]> op,
            final double[] out, final int outOffset,
            final GLVec3Array in0, final int in0Offset,
            final int count) {

        final double[] res = ensureArray(out, outOffset, count);

        op.apply(res, outOffset, in0.x, in0Offset, in0.y, in0Offset, in0.z, in0Offset, count);

        return res;
    }        

    private static class FutureGLVec3Array implements Future<GLVec3Array> {

        private final Future<?> xTask;
        private final Future<?> yTask;
        private final Future<?> zTask;
        private final GLVec3Array out;

        FutureGLVec3Array(GLVec3Array out, final Future<?> xTask, final Future<?> yTask, final Future<?> zTask) {
            this.out = Objects.requireNonNull(out);
            this.xTask = xTask;
            this.yTask = yTask;
            this.zTask = zTask;
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            boolean cancelled = false;

            if (xTask != null) {
                cancelled = xTask.cancel(mayInterruptIfRunning);
            }

            if (yTask != null) {
                cancelled |= yTask.cancel(mayInterruptIfRunning);
            }

            if (zTask != null) {
                cancelled |= zTask.cancel(mayInterruptIfRunning);
            }

            return cancelled;
        }

        @Override
        public GLVec3Array get() throws InterruptedException, ExecutionException {
            if (xTask != null) {
                xTask.get();
            }

            if (yTask != null) {
                yTask.get();
            }

            if (zTask != null) {
                zTask.get();
            }

            return this.out;
        }

        @Override
        public GLVec3Array get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            if (xTask != null) {
                xTask.get(timeout, unit);
            }

            if (yTask != null) {
                yTask.get(timeout, unit);
            }

            if (zTask != null) {
                zTask.get(timeout, unit);
            }

            return this.out;
        }

        @Override
        public boolean isCancelled() {
            if (xTask == null && yTask == null && zTask == null) {
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

            return result;
        }
    }
}
