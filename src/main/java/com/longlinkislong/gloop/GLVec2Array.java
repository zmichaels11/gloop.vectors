/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
public class GLVec2Array {

    public final int length;
    public final double[] x;
    public final double[] y;

    public GLVec2Array(final int size) {
        this.length = size;
        this.x = new double[size];
        this.y = new double[size];
    }

    public GLVec2D get(final int index) {
        return GLVec2D.create(this.x[index], this.y[index]);
    }

    public <VecT extends GLVec2D> void get(final int readIndex, final VecT[] out, final int writeIndex, final int count) {
        for (int i = 0; i < count; i++) {
            out[writeIndex + i].set(this.x[readIndex + i], this.y[readIndex + i]);
        }
    }

    public DoubleStream flatStream() {
        return Stream.of(x, y).flatMapToDouble(Arrays::stream);
    }

    public DoubleStream flatInterlacedStream() {
        final int[] index = {0};

        return DoubleStream.generate(() -> {
            final int idx = index[0];

            index[0]++;
            if (idx % 2 == 0) {
                return x[idx / 2];
            } else {
                return y[idx / 2];
            }
        });
    }

    public Stream<GLVec2D> stream() {
        return this.stream(0, this.length);
    }

    public Stream<GLVec2D> stream(final int startInclusive, final int endExclusive) {
        return IntStream.range(startInclusive, endExclusive).mapToObj(this::get);
    }

    private static double[] ensureArray(final double[] in, final int offset, final int neededSize) {
        if (in == null || in.length < offset + neededSize) {
            return new double[offset + neededSize];
        } else {
            return in;
        }
    }

    private static GLVec2Array ensureArray(final GLVec2Array in, final int offset, final int neededSize) {
        if (in == null || in.length < offset + neededSize) {
            return new GLVec2Array(offset + neededSize);
        } else {
            return in;
        }
    }

    public double[] cross(final double[] out, final GLVec2Array other) {
        return cross(out, 0, this, 0, other, 0, this.length);
    }

    public static double[] cross(
            final double[] out, final int outOffset,
            final GLVec2Array in0, final int in0Offset,
            final GLVec2Array in1, final int in1Offset,
            final int count) {

        final double[] res = ensureArray(out, outOffset, count);

        arrayMultiplyD(res, outOffset, in0.y, in0Offset, in0.x, in0Offset, count);
        arrayMultiplySubtractD(res, outOffset, in0.x, in0Offset, in1.y, in1Offset, res, outOffset, count);

        return res;
    }

    public double[] dot(final double[] out, final GLVec2Array other) {
        return dot(out, 0, this, 0, other, 0, this.length);
    }

    public static double[] dot(
            double[] out, final int outOffset,
            final GLVec2Array in0, final int in0Offset,
            final GLVec2Array in1, final int in1Offset,
            final int count) {

        final double[] res = ensureArray(out, outOffset, count);

        arrayMultiplyD(res, outOffset, in0.x, in0Offset, in1.x, in1Offset, count);
        arrayMultiplyAddD(res, outOffset, in0.y, in0Offset, in1.y, in1Offset, res, outOffset, count);

        return res;
    }

    public double[] length(final double[] out) {
        return length(out, 0, this, 0, this.length);
    }

    public static double[] length(
            final double[] out, final int outOffset,
            final GLVec2Array in0, final int in0Offset,
            final int count) {

        final double[] res = ensureArray(out, outOffset, count);

        for (int i = 0; i < count; i++) {
            final double x = in0.x[i + in0Offset];
            final double y = in0.y[i + in0Offset];

            res[i + outOffset] = sqrt(x * x + y * y);
        }

        return res;
    }

    public double[] length2(final double[] out) {
        return length2(out, 0, this, 0, this.length);
    }

    public static double[] length2(
            final double[] out, final int outOffset,
            final GLVec2Array in0, final int in0Offset,
            final int count) {

        final double[] res = ensureArray(out, outOffset, count);

        for (int i = 0; i < count; i++) {
            final double x = in0.x[i + in0Offset];
            final double y = in0.y[i + in0Offset];

            res[i + outOffset] = x * x + y * y;
        }

        return res;
    }

    public GLVec2Array normalize(final GLVec2Array out) {
        return normalize(out, 0, this, 0, this.length);
    }

    public static GLVec2Array normalize(
            final GLVec2Array out, final int outOffset,
            final GLVec2Array in0, final int in0Offset,
            final int count) {

        final GLVec2Array res = ensureArray(out, outOffset, count);

        for (int i = 0; i < count; i++) {
            final double x = in0.x[i + in0Offset];
            final double y = in0.y[i + in0Offset];

            final double scale = 1.0 / sqrt(x * x + y * y);

            res.x[i + outOffset] = scale * x;
            res.y[i + outOffset] = scale * y;
        }

        return res;
    }

    public GLVec2Array scale(final GLVec2Array out, final GLVec2 scale) {
        return scale(out, 0, this, 0, scale, this.length);
    }

    public static GLVec2Array scale(
            final GLVec2Array out, final int outOffset,
            final GLVec2Array in0, final int in0Offset,
            final GLVec2 scale,
            final int count) {

        final GLVec2Array res = ensureArray(out, outOffset, count);
        final GLVec2D scaleD = scale.asGLVec2D();

        arrayScaleD(res.x, outOffset, in0.x, in0Offset, scaleD.x(), count);
        arrayScaleD(res.y, outOffset, in0.y, in0Offset, scaleD.y(), count);

        return res;
    }

    public Future<GLVec2Array> scaleAsync(final GLVec2Array out, final GLVec2 scale) {
        return scaleAsync(out, 0, this, 0, scale, this.length);
    }

    public static Future<GLVec2Array> scaleAsync(
            final GLVec2Array out, final int outOffset,
            final GLVec2Array in0, final int in0Offset,
            final GLVec2 scale,
            final int count) {

        final GLVec2Array res = ensureArray(out, outOffset, count);
        final GLVec2D scaleD = scale.asGLVec2D();
        final Future<?> taskX = X_TASKS.submit(() -> arrayScaleD(res.x, outOffset, in0.x, in0Offset, scaleD.x(), count));
        final Future<?> taskY = Y_TASKS.submit(() -> arrayScaleD(res.y, outOffset, in0.y, in0Offset, scaleD.y(), count));

        return new FutureGLVec2Array(res, taskX, taskY);
    }

    public GLVec2Array addConstant(final GLVec2 vec) {
        return addConstant(this, 0, this, 0, vec, this.length);
    }

    public GLVec2Array addConstant(final GLVec2Array out, final GLVec2 vec) {
        return addConstant(out, 0, this, 0, vec, this.length);
    }

    public static GLVec2Array addConstant(
            final GLVec2Array out, final int outOffset,
            final GLVec2Array in0, final int in0Offset,
            final GLVec2 vec,
            final int count) {

        final GLVec2Array res = ensureArray(out, outOffset, count);
        final GLVec2D vecD = vec.asGLVec2D();

        arrayAddConstantD(res.x, outOffset, in0.x, in0Offset, vecD.x(), count);
        arrayAddConstantD(res.y, outOffset, in0.y, in0Offset, vecD.y(), count);
        return res;
    }

    public Future<GLVec2Array> addConstantAsync(final GLVec2Array out, final GLVec2 vec) {
        return addConstantAsync(out, 0, this, 0, vec, this.length);
    }

    public static Future<GLVec2Array> addConstantAsync(
            final GLVec2Array out, final int outOffset,
            final GLVec2Array in0, final int in0Offset,
            final GLVec2 vec,
            final int count) {

        final GLVec2Array res = ensureArray(out, outOffset, count);
        final GLVec2D vecD = vec.asGLVec2D();
        final double x = vecD.x();
        final double y = vecD.y();

        final Future<?> xTask = X_TASKS.submit(() -> arrayAddConstantD(res.x, outOffset, in0.x, in0Offset, x, count));
        final Future<?> yTask = Y_TASKS.submit(() -> arrayAddConstantD(res.y, outOffset, in0.y, in0Offset, y, count));

        return new FutureGLVec2Array(res, xTask, yTask);
    }

    public GLVec2Array setConstant(final GLVec2 vec) {
        return setConstant(this, 0, vec, this.length);
    }

    public static GLVec2Array setConstant(
            final GLVec2Array out, final int outOffset,
            final GLVec2 vec,
            final int count) {

        final GLVec2D vecD = vec.asGLVec2D();
        final double x = vecD.x();
        final double y = vecD.y();

        arraySetD(out.x, outOffset, x, count);
        arraySetD(out.y, outOffset, y, count);

        return out;
    }

    public Future<GLVec2Array> setConstantAsync(final GLVec2 vec) {
        return setConstantAsync(this, 0, vec, this.length);
    }

    public static Future<GLVec2Array> setConstantAsync(
            final GLVec2Array out, final int outOffset,
            final GLVec2 vec,
            final int count) {

        final GLVec2D vecD = vec.asGLVec2D();
        final double x = vecD.x();
        final double y = vecD.y();

        final Future<?> xTask = X_TASKS.submit(() -> arraySetD(out.x, outOffset, x, count));
        final Future<?> yTask = Y_TASKS.submit(() -> arraySetD(out.y, outOffset, y, count));

        return new FutureGLVec2Array(out, xTask, yTask);
    }

    public double[] flatten(final double[] out) {
        return flatten(out, 0, this, 0, this.length);
    }

    public static double[] flatten(
            final double[] out, final int outOffset,
            final GLVec2Array in0, final int in0Offset,
            final int count) {

        final double[] res = ensureArray(out, outOffset, count);

        int offset = outOffset;
        for (int i = 0; i < count; i++) {
            res[offset + i] = in0.x[in0Offset + i];
        }

        offset += count;
        for (int i = 0; i < count; i++) {
            res[offset + i] = in0.y[in0Offset + i];
        }

        return res;
    }

    public double[] flattenInterlaced(final double[] out) {
        return flattenInterlaced(out, 0, this, 0, this.length);
    }

    public static double[] flattenInterlaced(
            final double[] out, final int outOffset,
            final GLVec2Array in0, final int in0Offset,
            final int count) {

        final double[] res = ensureArray(out, outOffset, count);

        for (int i = 0; i < count; i++) {
            res[outOffset + i * 2] = in0.x[in0Offset + i];
            res[outOffset + i * 2 + 1] = in0.y[in0Offset + i];
        }

        return res;
    }

    public GLVec2Array apply(final VectorArrays.UnaryOp<double[]> op, final GLVec2Array out) {
        return apply(op, out, 0, this, 0, this.length);
    }

    public static GLVec2Array apply(
            final VectorArrays.UnaryOp<double[]> op,
            final GLVec2Array out, final int outOffset,
            final GLVec2Array in0, final int in0Offset,
            final int count) {

        final GLVec2Array res = ensureArray(out, outOffset, count);

        op.apply(res.x, outOffset, in0.x, in0Offset, count);
        op.apply(res.y, outOffset, in0.y, in0Offset, count);

        return res;
    }

    public GLVec2Array apply(
            final VectorArrays.BinaryOp<double[]> op,
            final GLVec2Array out,
            final GLVec2Array other) {

        return apply(op, out, 0, this, 0, other, 0, this.length);
    }

    public static GLVec2Array apply(
            final VectorArrays.BinaryOp<double[]> op,
            final GLVec2Array out, final int outOffset,
            final GLVec2Array in0, final int in0Offset,
            final GLVec2Array in1, final int in1Offset,
            final int count) {

        final GLVec2Array res = ensureArray(out, outOffset, count);

        op.apply(res.x, outOffset, in0.x, in0Offset, in1.x, in1Offset, count);
        op.apply(res.y, outOffset, in0.y, in0Offset, in1.y, in1Offset, count);

        return res;
    }

    public Future<GLVec2Array> applyAsync(
            final VectorArrays.BinaryOp<double[]> op,
            final GLVec2Array out,
            final GLVec2Array other) {

        return applyAsync(op, out, 0, this, 0, other, 0, this.length);
    }

    public static Future<GLVec2Array> applyAsync(
            final VectorArrays.BinaryOp<double[]> op,
            final GLVec2Array out, final int outOffset,
            final GLVec2Array in0, final int in0Offset,
            final GLVec2Array in1, final int in1Offset,
            final int count) {

        final GLVec2Array res = ensureArray(out, outOffset, count);
        final Future<?> taskX = X_TASKS.submit(() -> op.apply(res.x, outOffset, in0.x, in0Offset, in1.x, in1Offset, count));
        final Future<?> taskY = Y_TASKS.submit(() -> op.apply(res.y, outOffset, in0.y, in0Offset, in1.y, in1Offset, count));

        return new FutureGLVec2Array(res, taskX, taskY);
    }

    public GLVec2Array apply(
            final VectorArrays.TernaryOp<double[]> op,
            final GLVec2Array out,
            final GLVec2Array other0,
            final GLVec2Array other1) {

        return apply(op, out, 0, this, 0, other0, 0, other1, 0, this.length);
    }

    public static GLVec2Array apply(
            final VectorArrays.TernaryOp<double[]> op,
            final GLVec2Array out, final int outOffset,
            final GLVec2Array in0, final int in0Offset,
            final GLVec2Array in1, final int in1Offset,
            final GLVec2Array in2, final int in2Offset,
            final int count) {

        final GLVec2Array res = ensureArray(out, outOffset, count);

        op.apply(res.x, outOffset, in0.x, in0Offset, in1.x, in1Offset, in2.x, in2Offset, count);
        op.apply(res.y, outOffset, in0.y, in0Offset, in1.y, in1Offset, in2.y, in2Offset, count);

        return res;
    }

    public Future<GLVec2Array> applyAsync(
            final VectorArrays.TernaryOp<double[]> op,
            final GLVec2Array out,
            final GLVec2Array other0,
            final GLVec2Array other1) {

        return applyAsync(op, out, 0, this, 0, other0, 0, other1, 0, this.length);
    }

    public static Future<GLVec2Array> applyAsync(
            final VectorArrays.TernaryOp<double[]> op,
            final GLVec2Array out, final int outOffset,
            final GLVec2Array in0, final int in0Offset,
            final GLVec2Array in1, final int in1Offset,
            final GLVec2Array in2, final int in2Offset,
            final int count) {

        final GLVec2Array res = ensureArray(out, outOffset, count);
        final Future<?> taskX = X_TASKS.submit(() -> op.apply(res.x, outOffset, in0.x, in0Offset, in1.x, in1Offset, in2.x, in2Offset, count));
        final Future<?> taskY = Y_TASKS.submit(() -> op.apply(res.y, outOffset, in0.y, in0Offset, in1.y, in1Offset, in2.y, in2Offset, count));

        return new FutureGLVec2Array(res, taskX, taskY);
    }

    public double[] flatten(
            final VectorArrays.BinaryOp<double[]> op,
            final double[] out) {

        return flatten(op, out, 0, this, 0, this.length);
    }

    public static double[] flatten(
            final VectorArrays.BinaryOp<double[]> op,
            final double[] out, final int outOffset,
            final GLVec2Array in0, final int in0Offset,
            final int count) {

        final double[] res = ensureArray(out, outOffset, count);

        op.apply(res, outOffset, in0.x, in0Offset, in0.y, in0Offset, count);

        return res;
    }       

    private static class FutureGLVec2Array implements Future<GLVec2Array> {

        private final Future<?> xTask;
        private final Future<?> yTask;
        private final GLVec2Array out;

        FutureGLVec2Array(GLVec2Array out, final Future<?> xTask, final Future<?> yTask) {
            this.xTask = xTask;
            this.yTask = yTask;
            this.out = Objects.requireNonNull(out);
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

            return cancelled;
        }

        @Override
        public GLVec2Array get() throws InterruptedException, ExecutionException {
            if (xTask != null) {
                xTask.get();
            }

            if (yTask != null) {
                yTask.get();
            }

            return this.out;
        }

        @Override
        public GLVec2Array get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            if (xTask != null) {
                xTask.get(timeout, unit);
            }

            if (yTask != null) {
                yTask.get(timeout, unit);
            }

            return this.out;
        }

        @Override
        public boolean isCancelled() {
           boolean result = true;
            
            if(xTask != null) {
                result &= xTask.isCancelled();
            }
            
            if(yTask != null) {
                result &= yTask.isCancelled();
            }
            
            return result;
        }

        @Override
        public boolean isDone() {
            if (xTask != null && yTask != null) {
                return xTask.isDone() && yTask.isDone();
            } else if (xTask != null && yTask == null) {
                return xTask.isDone();
            } else if (xTask == null && yTask != null) {
                return yTask.isDone();
            } else {
                return true;
            }
        }
    }
}
