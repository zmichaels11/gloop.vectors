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
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.Future;

import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 *
 * @author zmichaels
 */
public class GLVec3FArray {

    private final int size;
    public final float[] x;
    public final float[] y;
    public final float[] z;

    public GLVec3FArray(final int size) {
        this.size = size;

        this.x = new float[size];
        this.y = new float[size];
        this.z = new float[size];
    }

    public int length() {
        return this.size;
    }

    public GLVec3F get(final int index) {
        return GLVec3F.create(this.x[index], this.y[index], this.z[index]);
    }

    public <VecT extends GLVec3F> void get(final int readIndex, final VecT[] out, final int writeIndex, final int count) {
        for (int i = 0; i < count; i++) {
            out[writeIndex + i].set(this.x[readIndex + i], this.y[readIndex + i], this.z[readIndex + i]);
        }
    }

    public Stream<GLVec3F> stream(final int startInclusive, final int endExclusive) {
        return IntStream.range(startInclusive, endExclusive).mapToObj(this::get);
    }

    public Stream<GLVec3F> stream() {
        return this.stream(0, this.length());
    }

    public void getX(final int readOffset, final float[] out, final int writeOffset, final int count) {
        System.arraycopy(this.x, readOffset, out, writeOffset, count);
    }

    public void getXAsync(final int readOffset, final float[] out, final int writeOffset, final int count, final boolean waitForComplete) {
        final Future<?> xTask = X_TASKS.submit(() -> System.arraycopy(this.x, readOffset, out, writeOffset, count));

        if (waitForComplete) {
            while (!xTask.isDone()) {
                Thread.yield();
            }
        }
    }

    public void getY(final int readOffset, final float[] out, final int writeOffset, final int count) {
        System.arraycopy(this.y, readOffset, out, writeOffset, count);
    }

    public void getYAsync(final int readOffset, final float[] out, final int writeOffset, final int count, final boolean waitForComplete) {
        final Future<?> yTask = Y_TASKS.submit(() -> System.arraycopy(this.y, readOffset, out, writeOffset, count));

        if (waitForComplete) {
            while (!yTask.isDone()) {
                Thread.yield();
            }
        }
    }

    public void getZ(final int readOffset, final float[] out, final int writeOffset, final int count) {
        System.arraycopy(this.z, readOffset, out, writeOffset, count);
    }

    public void getZAsync(final int readOffset, final float[] out, final int writeOffset, final int count, final boolean waitForComplete) {
        final Future<?> zTask = Z_TASKS.submit(() -> System.arraycopy(this.z, readOffset, out, writeOffset, count));

        if (waitForComplete) {
            while (!zTask.isDone()) {
                Thread.yield();
            }
        }
    }

    public void setX(final int offset, final float value, final int count) {
        arraySetF(this.x, offset, value, count);
    }

    public void setXAsync(final int offset, final float value, final int count, final boolean waitForComplete) {
        final Future<?> xTask = X_TASKS.submit(() -> arraySetF(this.x, offset, value, count));

        if (waitForComplete) {
            while (!xTask.isDone()) {
                Thread.yield();
            }
        }
    }

    public void setY(final int offset, final float value, final int count) {
        arraySetF(this.y, offset, value, count);
    }

    public void setYAsync(final int offset, final float value, final int count, final boolean waitForComplete) {
        final Future<?> yTask = Y_TASKS.submit(() -> arraySetF(this.y, offset, value, count));

        if (waitForComplete) {
            while (!yTask.isDone()) {
                Thread.yield();
            }
        }
    }

    public void setZ(final int offset, final float value, final int count) {
        arraySetF(this.z, offset, value, count);
    }

    public void setZAsync(final int offset, final float value, final int count, final boolean waitForComplete) {
        final Future<?> zTask = Z_TASKS.submit(() -> arraySetF(this.z, offset, value, count));

        if (waitForComplete) {
            while (!zTask.isDone()) {
                Thread.yield();
            }
        }
    }

    public static void cross(
            final GLVec3FArray out, final int outOffset,
            final GLVec3FArray in0, final int in0Offset,
            final GLVec3FArray in1, final int in1Offset,
            final int count) {

        arrayMultiplyF(out.x, outOffset, in0.z, in0Offset, in1.y, in1Offset, count);
        arrayMultiplyF(out.y, outOffset, in0.x, in0Offset, in1.z, in1Offset, count);
        arrayMultiplyF(out.z, outOffset, in0.y, in0Offset, in1.x, in1Offset, count);

        arrayMultiplySubtractF(out.x, outOffset, in0.y, in0Offset, in1.z, in1Offset, out.x, outOffset, count);
        arrayMultiplySubtractF(out.y, outOffset, in0.z, in0Offset, in1.x, in1Offset, out.y, outOffset, count);
        arrayMultiplySubtractF(out.z, outOffset, in0.x, in0Offset, in1.y, in1Offset, out.z, outOffset, count);
    }

    public static void crossAsync(
            final GLVec3FArray out, final int outOffset,
            final GLVec3FArray in0, final int in0Offset,
            final GLVec3FArray in1, final int in1Offset,
            final int count,
            final boolean waitForComplete) {

        X_TASKS.submit(() -> arrayMultiplyF(out.x, outOffset, in0.z, in0Offset, in1.y, in1Offset, count));
        Y_TASKS.submit(() -> arrayMultiplyF(out.y, outOffset, in0.x, in0Offset, in1.z, in1Offset, count));
        Z_TASKS.submit(() -> arrayMultiplyF(out.z, outOffset, in0.y, in0Offset, in1.x, in1Offset, count));

        // these tasks wont start until the previous XYZ tasks complete.
        final Future<?> taskX = X_TASKS.submit(() -> arrayMultiplySubtractF(out.x, outOffset, in0.y, in0Offset, in1.z, in1Offset, out.x, outOffset, count));
        final Future<?> taskY = Y_TASKS.submit(() -> arrayMultiplySubtractF(out.y, outOffset, in0.z, in0Offset, in1.x, in1Offset, out.y, outOffset, count));
        final Future<?> taskZ = Z_TASKS.submit(() -> arrayMultiplySubtractF(out.z, outOffset, in0.x, in0Offset, in1.y, in1Offset, out.z, outOffset, count));

        if (waitForComplete) {
            while (!taskX.isDone() || !taskY.isDone() || !taskZ.isDone()) {
                Thread.yield();
            }
        }
    }

    public static void dot(
            final float[] out, final int outOffset,
            final GLVec3FArray in0, final int in0Offset,
            final GLVec3FArray in1, final int in1Offset,
            final int count) {

        // out = in0.x * in1.x
        arrayMultiplyF(out, outOffset, in0.x, in0Offset, in1.x, in1Offset, count);
        // out = in0.y * in1.y + out
        arrayMultiplyAddF(out, outOffset, in0.y, in0Offset, in1.y, in1Offset, out, outOffset, count);
        // out = in0.z * in1.z + out
        arrayMultiplyAddF(out, outOffset, in0.z, in0Offset, in1.z, in1Offset, out, outOffset, count);
    }

    public static void length(
            final float[] out, final int outOffset,
            final GLVec3FArray in0, final int in0Offset,
            final int count) {

        for (int i = 0; i < count; i++) {
            final float x = in0.x[i + in0Offset];
            final float y = in0.y[i + in0Offset];
            final float z = in0.z[i + in0Offset];

            out[i + outOffset] = (float) sqrt(x * x + y * y + z * z);
        }
    }

    public static void normalize(
            final GLVec3FArray out, final int outOffset,
            final GLVec3FArray in0, final int in0Offset,
            final int count) {

        for (int i = 0; i < count; i++) {
            final float x = in0.x[i + in0Offset];
            final float y = in0.y[i + in0Offset];
            final float z = in0.z[i + in0Offset];

            final float scale = 1.0f / (float) sqrt(x * x + y * y + z * z);

            out.x[i + outOffset] = scale * x;
            out.y[i + outOffset] = scale * y;
            out.z[i + outOffset] = scale * z;
        }
    }        

    public static void apply(
            final VectorArrays.UnaryOp<float[]> op,
            final GLVec3FArray out, final int outOffset,
            final GLVec3FArray in0, final int in0Offset,
            final int count) {

        op.apply(out.x, outOffset, in0.x, in0Offset, count);
        op.apply(out.y, outOffset, in0.y, in0Offset, count);
        op.apply(out.z, outOffset, in0.z, in0Offset, count);
    }
    
    public static void applyAsync(
    final VectorArrays.UnaryOp<float[]> op,
            final GLVec3FArray out, final int outOffset,
            final GLVec3FArray in0, final int in0Offset,
            final int count,
            final boolean waitForComplete) {
        
        final Future<?> taskX = X_TASKS.submit(() -> op.apply(out.x, outOffset, in0.x, in0Offset, count));
        final Future<?> taskY = Y_TASKS.submit(() -> op.apply(out.y, outOffset, in0.y, in0Offset, count));
        final Future<?> taskZ = Z_TASKS.submit(() -> op.apply(out.z, outOffset, in0.z, in0Offset, count));        

        if (waitForComplete) {
            while (!taskX.isDone() || !taskY.isDone() || !taskZ.isDone()) {
                Thread.yield();
            }
        }
    }
    
    public static void apply(
            final VectorArrays.BinaryOp<float[]> op,
            final GLVec3FArray out, final int outOffset,
            final GLVec3FArray in0, final int in0Offset,
            final GLVec3FArray in1, final int in1Offset,
            final int count) {

        op.apply(out.x, outOffset, in0.x, in0Offset, in1.x, in1Offset, count);
        op.apply(out.y, outOffset, in0.y, in0Offset, in1.y, in1Offset, count);
        op.apply(out.z, outOffset, in0.z, in0Offset, in1.z, in1Offset, count);        
    }
    
    public static void applyAsync(
            final VectorArrays.BinaryOp<float[]> op,
            final GLVec3FArray out, final int outOffset,
            final GLVec3FArray in0, final int in0Offset,
            final GLVec3FArray in1, final int in1Offset,
            final int count, final boolean waitForComplete) {

        final Future<?> taskX = X_TASKS.submit(() -> op.apply(out.x, outOffset, in0.x, in0Offset, in1.x, in1Offset, count));
        final Future<?> taskY = Y_TASKS.submit(() -> op.apply(out.y, outOffset, in0.y, in0Offset, in1.y, in1Offset, count));
        final Future<?> taskZ = Z_TASKS.submit(() -> op.apply(out.z, outOffset, in0.z, in0Offset, in1.z, in1Offset, count));        

        if (waitForComplete) {
            while (!taskX.isDone() || !taskY.isDone() || !taskZ.isDone()) {
                Thread.yield();
            }
        }
    }
    
    public static void apply(
            final VectorArrays.TernaryOp<float[]> op,
            final GLVec3FArray out, final int outOffset,
            final GLVec3FArray in0, final int in0Offset,
            final GLVec3FArray in1, final int in1Offset,
            final GLVec3FArray in2, final int in2Offset,
            final int count) {

        op.apply(out.x, outOffset, in0.x, in0Offset, in1.x, in1Offset, in2.x, in1Offset, count);
        op.apply(out.y, outOffset, in0.y, in0Offset, in1.y, in1Offset, in2.y, in2Offset, count);
        op.apply(out.z, outOffset, in0.z, in0Offset, in1.z, in1Offset, in2.z, in2Offset, count);        
    }
    
    public static void applyAsync(
            final VectorArrays.TernaryOp<float[]> op,
            final GLVec3FArray out, final int outOffset,
            final GLVec3FArray in0, final int in0Offset,
            final GLVec3FArray in1, final int in1Offset,
            final GLVec3FArray in2, final int in2Offset,
            final int count, final boolean waitForComplete) {

        final Future<?> taskX = X_TASKS.submit(() -> op.apply(out.x, outOffset, in0.x, in0Offset, in1.x, in1Offset, in2.x, in2Offset, count));
        final Future<?> taskY = Y_TASKS.submit(() -> op.apply(out.y, outOffset, in0.y, in0Offset, in1.y, in1Offset, in2.y, in2Offset, count));
        final Future<?> taskZ = Z_TASKS.submit(() -> op.apply(out.z, outOffset, in0.z, in0Offset, in1.z, in1Offset, in2.z, in2Offset, count));        

        if (waitForComplete) {
            while (!taskX.isDone() || !taskY.isDone() || !taskZ.isDone()) {
                Thread.yield();
            }
        }
    }
    
    public static void scale(
            final GLVec3FArray out, final int outOffset,
            final GLVec3FArray in0, final int in0Offset,
            final GLVec3 scale,
            final int count) {

        final GLVec3F scaleF = scale.asGLVec3F();

        arrayScaleF(out.x, outOffset, in0.x, in0Offset, scaleF.x(), count);
        arrayScaleF(out.y, outOffset, in0.y, in0Offset, scaleF.y(), count);
        arrayScaleF(out.z, outOffset, in0.z, in0Offset, scaleF.z(), count);       
    }
    
    public static void scaleAsync(
            final GLVec3FArray out, final int outOffset,
            final GLVec3FArray in0, final int in0Offset,
            final GLVecF scale,
            final int count, final boolean waitForComplete) {

        final GLVec3F scaleF = scale.asGLVec3F();

        final Future<?> taskX = X_TASKS.submit(() -> arrayScaleF(out.x, outOffset, in0.x, in0Offset, scaleF.x(), count));
        final Future<?> taskY = Y_TASKS.submit(() -> arrayScaleF(out.y, outOffset, in0.y, in0Offset, scaleF.y(), count));
        final Future<?> taskZ = Z_TASKS.submit(() -> arrayScaleF(out.z, outOffset, in0.z, in0Offset, scaleF.z(), count));        

        if (waitForComplete) {
            while (!taskX.isDone() || !taskY.isDone() || !taskZ.isDone()) {
                Thread.yield();
            }
        }
    }
    
    public static void addConstant(
            final GLVec3FArray out, final int outOffset,
            final GLVec3FArray in0, final int in0Offset,
            final GLVec3 vec,
            final int count) {

        final GLVec3F vecF = vec.asGLVec3F();

        arrayAddConstantF(out.x, outOffset, in0.x, in0Offset, vecF.x(), count);
        arrayAddConstantF(out.y, outOffset, in0.y, in0Offset, vecF.y(), count);
        arrayAddConstantF(out.z, outOffset, in0.z, in0Offset, vecF.z(), count);        
    }
    
    public static void addConstantAsync(
            final GLVec3FArray out, final int outOffset,
            final GLVec3FArray in0, final int in0Offset,
            final GLVec3 vec,
            final int count,
            final boolean waitForComplete) {

        final GLVec3F vecF = vec.asGLVec3F();
        
        final Future<?> taskX = X_TASKS.submit(() -> arrayAddConstantF(out.x, outOffset, in0.x, in0Offset, vecF.x(), count));
        final Future<?> taskY = Y_TASKS.submit(() -> arrayAddConstantF(out.y, outOffset, in0.y, in0Offset, vecF.y(), count));
        final Future<?> taskZ = Z_TASKS.submit(() -> arrayAddConstantF(out.z, outOffset, in0.z, in0Offset, vecF.z(), count));        

        if (waitForComplete) {
            while (!taskX.isDone() || !taskY.isDone() || !taskZ.isDone()) {
                Thread.yield();
            }
        }
    }
    
    public static void setConstant(
            final GLVec3FArray out, final int outOffset,
            final GLVec3 vec,
            final int count) {

        final GLVec3F vecF = vec.asGLVec3F();

        VectorArrays.arraySetF(out.x, outOffset, vecF.x(), count);
        VectorArrays.arraySetF(out.y, outOffset, vecF.y(), count);
        VectorArrays.arraySetF(out.z, outOffset, vecF.z(), count);        
    }
    
    public static void setConstantAsync(
            final GLVec3FArray out, final int outOffset,
            final GLVec3 vec,
            final int count,
            final boolean waitForComplete) {

        final GLVec3F vecF = vec.asGLVec3F();

        final Future<?> taskX = X_TASKS.submit(() -> arraySetF(out.x, outOffset, vecF.x(), count));
        final Future<?> taskY = Y_TASKS.submit(() -> arraySetF(out.y, outOffset, vecF.y(), count));
        final Future<?> taskZ = Z_TASKS.submit(() -> arraySetF(out.z, outOffset, vecF.z(), count));        

        if (waitForComplete) {
            while (!taskX.isDone() || !taskY.isDone() || !taskZ.isDone()) {
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
    
    /**
     * Sets all values within the given range to 0.0
     *
     * @param start the position to start.
     * @param count the number of elements to process.
     * @since 15.10.26
     */
    public void zero(final int start, final int count) {
        VectorArrays.arraySetF(this.x, start, 0f, count);
        VectorArrays.arraySetF(this.y, start, 0f, count);
        VectorArrays.arraySetF(this.z, start, 0f, count);        
    }

    /**
     * Multi-threaded implementation of zero.
     *
     * @param start the position to start.
     * @param count the number of elements to process.
     * @param waitForComplete Signals that the operation should wait before
     * returning.
     * @since 15.10.26
     */
    public void zeroAsync(final int start, final int count, final boolean waitForComplete) {
        final Future<?> taskX = X_TASKS.submit(() -> arraySetF(this.x, start, 0f, count));
        final Future<?> taskY = Y_TASKS.submit(() -> arraySetF(this.y, start, 0f, count));
        final Future<?> taskZ = Z_TASKS.submit(() -> arraySetF(this.z, start, 0f, count));        

        if (waitForComplete) {
            while (!taskX.isDone() || !taskY.isDone() || !taskZ.isDone()) {
                Thread.yield();
            }
        }
    }
}
