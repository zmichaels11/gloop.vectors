/* 
 * Copyright (c) 2016, Zachary Michaels
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

/**
 * An implementation of GLMat3 that uses a builder pattern.
 *
 * @author zmichaels
 * @since 16.02.39
 */
public class GLMat3Builder implements GLMat3 {

    private final double[] stack;
    private final int stackSize;
    private int current;

    /**
     * Constructs a new GLMat3Builder with the minimum stack space. The identity
     * matrix is used for the base matrix.
     *
     * @since 16.02.29
     */
    public GLMat3Builder() {
        this(3, GLMat3D.create());
    }

    /**
     * Constructs a new GLMat3Builder with the minimum stack space. The
     * specified matrix is used for the base matrix.
     *
     * @param base the base matrix.
     * @since 16.02.29
     */
    public GLMat3Builder(final GLMat3 base) {
        this(3, base);
    }

    /**
     * Constructs a new GLMat3Builder with the specified stack space. The
     * identity matrix is used for the base matrix.
     *
     * @param stackSize the stack space. Must be at least 3 for all operations.
     * @since 16.02.29
     */
    public GLMat3Builder(final int stackSize) {
        this(stackSize, GLMat3D.create());
    }

    /**
     * Constructs a new GLMat3Builder with the specified stack space and base
     * matrix.
     *
     * @param stackSize the stack space. A minimum of 3 is required for most
     * operations.
     * @param base the base matrix.
     * @since 16.02.29
     */
    public GLMat3Builder(final int stackSize, final GLMat3 base) {
        this.stackSize = stackSize;
        this.stack = new double[9 * stackSize];
        this.current = 9 * stackSize - 9;

        base.asGLMat3D().copyToArray(stack, current, 9);
    }

    /**
     * Retrieves the size of the internal matrix stack.
     *
     * @return the stack size.
     * @since 16.02.29
     */
    public final int getStackSize() {
        return this.stackSize;
    }

    private int testBounds(final int next) {
        if (next > this.stack.length - 9) {
            throw new IndexOutOfBoundsException("Stack overflow");
        } else if (next < 0) {
            throw new IndexOutOfBoundsException("Stack underflow");
        } else {
            return next;
        }
    }

    /**
     * Pushes the internal matrix stack. This will preserve the current internal
     * matrix state.
     *
     * @since 16.02.29
     */
    public final void push() {
        final int next = this.testBounds(this.current - 9);

        System.arraycopy(this.stack, this.current, this.stack, next, 9);
    }

    /**
     * Pops the internal matrix stack. This will restore the previous value.
     *
     * @since 16.02.29
     */
    public final void pop() {
        final int next = this.testBounds(this.current + 9);

        this.current = next;
    }

    /**
     * Sets the internal matrix to all 0's.
     *
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat3Builder setZero() {
        return this.setScale(0.0, 0.0, 0.0);
    }

    /**
     * Sets the internal matrix to the identity matrix.
     *
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat3Builder setIdentity() {
        return this.setScale(1.0, 1.0, 1.0);
    }

    /**
     * Override for [code]setTranslation(x, 0.0, 1.0)[/code].
     *
     * @param x the translation along the x-axis.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat3Builder setTranslation(final double x) {
        return this.setTranslation(x, 0.0, 1.0);
    }

    /**
     * Override for [code]setTranslation(x, y, 1.0)[/code].
     *
     * @param x the translation along the x-axis.
     * @param y the translation along the y-axis.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat3Builder setTranslation(final double x, final double y) {
        return this.setTranslation(x, y, 1.0);
    }

    /**
     * Sets the internal matrix to a translation matrix.
     *
     * @param x the translation along the x-axis.
     * @param y the translation along the y-axis.
     * @param z the translation along the z-axis. Usually this will be 1.0.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat3Builder setTranslation(final double x, final double y, final double z) {
        stack[current] = 1.0;
        stack[current] = 0.0;
        stack[current] = 0.0;

        stack[current] = 0.0;
        stack[current] = 1.0;
        stack[current] = 0.0;

        stack[current] = x;
        stack[current] = y;
        stack[current] = z;

        return this;
    }

    /**
     * Override for [code]prependTranslation(x, 0.0, 1.0)[/code].
     *
     * @param x the translation along the x-axis.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat3Builder prependTranslation(final double x) {
        return this.prependTranslation(x, 0.0, 1.0);
    }

    /**
     * Override for [code]prependTranslation(x, y, 1.0)[/code].
     *
     * @param x translation along the x-axis.
     * @param y translation along the y-axis.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat3Builder prependTranslation(final double x, final double y) {
        return this.prependTranslation(x, y, 1.0);
    }

    /**
     * Sets the internal matrix to the product of a translation matrix and the
     * internal matrix. Performs [code]internal = translation * internal[/code].
     *
     * @param x translation along the x-axis.
     * @param y translation along the y-axis.
     * @param z translation along the z-axis. Usually this will be 1.0.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat3Builder prependTranslation(final double x, final double y, final double z) {
        final int in1 = this.current - 9;
        final int in2 = this.current;
        final int out = this.current - 18;

        stack[in1] = 1.0;
        stack[in1 + 1] = 0.0;
        stack[in1 + 2] = 0.0;

        stack[in1 + 3] = 0.0;
        stack[in1 + 4] = 1.0;
        stack[in1 + 5] = 0.0;

        stack[in1 + 6] = x;
        stack[in1 + 7] = y;
        stack[in1 + 8] = z;

        Matrices.multiplyMat3D(stack, out, stack, in1, stack, in2);
        System.arraycopy(stack, out, stack, current, 9);
        return this;
    }

    /**
     * Override for [code]appendTranslation(x, 0.0, 1.0)[/code].
     *
     * @param x translation along the x-axis.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat3Builder appendTranslation(final double x) {
        return this.appendTranslation(x, 0.0, 1.0);
    }

    /**
     * Override for [code]appendTranslation(x, y, 1.0)
     *
     * @param x the translation along the x-axis.
     * @param y the translation along the y-axis.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat3Builder appendTranslation(final double x, final double y) {
        return this.appendTranslation(x, y, 1.0);
    }

    /**
     * Sets the internal matrix to the product of the internal matrix an a
     * translation matrix. Performs [code]internal *= translation[/code].
     *
     * @param x the translation along the x-axis.
     * @param y the translation along the y-axis.
     * @param z the translation along the z-axis. Usually this will be 1.0.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat3Builder appendTranslation(final double x, final double y, final double z) {
        final int in1 = this.current;
        final int in2 = this.current - 9;
        final int out = this.current - 18;

        stack[in2] = 1.0;
        stack[in2 + 1] = 0.0;
        stack[in2 + 2] = 0.0;

        stack[in2 + 3] = 0.0;
        stack[in2 + 4] = 1.0;
        stack[in2 + 5] = 0.0;

        stack[in2 + 6] = x;
        stack[in2 + 7] = y;
        stack[in2 + 8] = z;

        Matrices.multiplyMat3D(stack, out, stack, in1, stack, in2);
        System.arraycopy(stack, out, stack, current, 9);
        return this;
    }

    /**
     * Override for [code]setScale(x, 1.0, 1.0)[/code].
     *
     * @param x the scale along the x-axis.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat3Builder setScale(final double x) {
        return this.setScale(x, 1.0, 1.0);
    }

    /**
     * Override for [code]setScale(x, y, 1.0)[/code].
     *
     * @param x the scale along the x-axis.
     * @param y the scale along the y-axis.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat3Builder setScale(final double x, final double y) {
        return this.setScale(x, y, 1.0);
    }

    /**
     * Sets the internal matrix to the scale matrix.
     *
     * @param x the scale along the x-axis.
     * @param y the scale along the y-axis.
     * @param z the scale along the z-axis. Usually this will be 1.0.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat3Builder setScale(final double x, final double y, final double z) {
        stack[current] = x;
        stack[current + 1] = 0.0;
        stack[current + 2] = 0.0;

        stack[current + 3] = 0.0;
        stack[current + 4] = y;
        stack[current + 5] = 0.0;

        stack[current + 6] = 0.0;
        stack[current + 7] = 0.0;
        stack[current + 8] = z;

        return this;
    }

    /**
     * Override for [code]prependScale(x, 1.0, 1.0)[/code].
     *
     * @param x the scale along the x-axis.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat3Builder prependScale(final double x) {
        return this.prependScale(x, 1.0, 1.0);
    }

    /**
     * Override for [code]prependScale(x, y, 1.0)[/code].
     *
     * @param x the scale along the x-axis.
     * @param y the scale along the y-axis.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat3Builder prependScale(final double x, final double y) {
        return this.prependScale(x, y, 1.0);
    }

    /**
     * Sets the internal matrix to the product of a scale matrix and the
     * internal matrix. Performs [code]internal = scale * internal[/code].
     *
     * @param x the scale along the x-axis.
     * @param y the scale along the y-axis.
     * @param z the scale along the z-axis. Usually this will be 1.0.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat3Builder prependScale(final double x, final double y, final double z) {
        final int in1 = this.current - 9;
        final int in2 = this.current;
        final int out = this.current - 18;

        stack[in1] = x;
        stack[in1 + 1] = 0.0;
        stack[in1 + 2] = 0.0;

        stack[in1 + 3] = 0.0;
        stack[in1 + 4] = y;
        stack[in1 + 5] = 0.0;

        stack[in1 + 6] = 0.0;
        stack[in1 + 7] = 0.0;
        stack[in1 + 8] = z;

        Matrices.multiplyMat3D(stack, out, stack, in1, stack, in2);
        System.arraycopy(stack, out, stack, current, 9);
        return this;
    }

    /**
     * Override for [code]appendScale(x, 1.0, 1.0)[/code].
     *
     * @param x the scale along the x-axis.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat3Builder appendScale(final double x) {
        return this.appendScale(x, 1.0, 1.0);
    }

    /**
     * Override for [code]appendScale(x, y, 1.0)[/code].
     *
     * @param x the scale along the x-axis.
     * @param y the scale along the y-axis.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat3Builder appendScale(final double x, final double y) {
        return this.appendScale(x, y, 1.0);
    }

    /**
     * Sets the internal matrix to the product of the internal matrix and a
     * scale matrix. Performs [code]internal *= scale[/code].
     *
     * @param x the scale along the x-axis.
     * @param y the scale along the y-axis.
     * @param z the scale along the z-axis. Usually this will be 1.0.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat3Builder appendScale(final double x, final double y, final double z) {
        final int in1 = this.current;
        final int in2 = this.current - 9;
        final int out = this.current - 18;

        stack[in2] = x;
        stack[in2 + 1] = 0.0;
        stack[in2 + 2] = 0.0;

        stack[in2 + 3] = 0.0;
        stack[in2 + 4] = y;
        stack[in2 + 5] = 0.0;

        stack[in2 + 6] = 0.0;
        stack[in2 + 7] = 0.0;
        stack[in2 + 8] = z;

        Matrices.multiplyMat3D(stack, out, stack, in1, stack, in2);
        System.arraycopy(stack, out, stack, current, 9);
        return this;
    }

    /**
     * Sets the internal matrix to the rotation matrix.
     *
     * @param angle the angle in radians.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat3Builder setRotate(final double angle) {
        final double sa = Math.sin(angle);
        final double ca = Math.cos(angle);

        stack[current] = ca;
        stack[current + 1] = sa;
        stack[current + 2] = 0.0;

        stack[current + 3] = -sa;
        stack[current + 4] = ca;
        stack[current + 5] = 0.0;

        stack[current + 6] = 0.0;
        stack[current + 7] = 0.0;
        stack[current + 8] = 1.0;

        return this;
    }

    /**
     * Sets the internal matrix to the product of the rotation matrix and the
     * internal matrix. Performs [code]internal = rotation * internal[/code].
     *
     * @param angle the angle in radians.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat3Builder prependRotate(final double angle) {
        final int in1 = this.current - 9;
        final int in2 = this.current;
        final int out = this.current - 18;

        final double sa = Math.sin(angle);
        final double ca = Math.cos(angle);

        stack[in1] = ca;
        stack[in1 + 1] = sa;
        stack[in1 + 2] = 0.0;

        stack[in1 + 3] = -sa;
        stack[in1 + 4] = ca;
        stack[in1 + 5] = 0.0;

        stack[in1 + 6] = 0.0;
        stack[in1 + 7] = 0.0;
        stack[in1 + 8] = 1.0;

        Matrices.multiplyMat3D(stack, out, stack, in1, stack, in2);
        System.arraycopy(stack, out, stack, current, 9);
        return this;
    }

    /**
     * Sets the internal matrix to the product of the internal matrix and the
     * rotation matrix. Performs [code]internal *= rotation[/code].
     *
     * @param angle the angle in radians.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat3Builder appendRotate(final double angle) {
        final int in1 = this.current;
        final int in2 = this.current - 9;
        final int out = this.current - 18;

        final double sa = Math.sin(angle);
        final double ca = Math.cos(angle);

        stack[in2] = ca;
        stack[in2 + 1] = sa;
        stack[in2 + 2] = 0.0;

        stack[in2 + 3] = -sa;
        stack[in2 + 4] = ca;
        stack[in2 + 5] = 0.0;

        stack[in2 + 6] = 0.0;
        stack[in2 + 7] = 0.0;
        stack[in2 + 8] = 1.0;

        Matrices.multiplyMat3D(stack, out, stack, in1, stack, in2);
        System.arraycopy(stack, out, stack, current, 9);
        return this;
    }

    /**
     * Sets the internal matrix to the same values as another matrix.
     *
     * @param other the other matrix.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat3Builder setMatrix(final GLMat3 other) {
        other.asGLMat3D().copyToArray(stack, current, 9);
        return this;
    }

    /**
     * Sets the internal matrix to the product of another matrix and the
     * internal matrix. [code]internal = other * internal[/code].
     *
     * @param other the other matrix.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat3Builder prependMatrix(final GLMat3 other) {
        final GLMat3D in1 = other.asGLMat3D();
        final int in2 = this.current;
        final int out = this.current - 9;

        Matrices.multiplyMat3D(stack, out, in1.data(), in1.offset(), stack, in2);
        System.arraycopy(stack, out, stack, current, 9);
        return this;
    }

    /**
     * Sets the internal matrix to the product of the internal matrix and
     * another matrix. Performs [code]internal *= other[/code].
     *
     * @param other the other matrix.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat3Builder appendMatrix(final GLMat3 other) {
        final int in1 = this.current;
        final GLMat3D in2 = other.asGLMat3D();
        final int out = this.current - 9;

        Matrices.multiplyMat3D(stack, out, stack, in1, in2.data(), in2.offset());
        System.arraycopy(stack, out, stack, current, 9);
        return this;
    }

    /**
     * Multiplies the internal matrix by a vector.
     *
     * @param vec the vector.
     * @return the result of the matrix-vector multiplication.
     * @since 16.02.29
     */
    public GLVec3D multiply(final GLVec<?> vec) {
        final int in1 = this.current;
        final GLVec3D out = GLVec3D.create();
        final GLVec3D in2 = vec.asGLVecD().asGLVec3D();

        Matrices.multiplyVec3D(out.data(), out.offset(), stack, in1, in2.data(), in2.offset());
        return out;
    }

    /**
     * Multiplies the internal matrix by another matrix.
     *
     * @param mat the other matrix.
     * @return the result of the matrix-matrix multiplication.
     * @since 16.02.29
     */
    public GLMat3D multiply(final GLMat<?, ?> mat) {
        final int in1 = this.current;
        final GLMat3D out = GLMat3D.create();
        final GLMat3D in2 = mat.asGLMatD().asGLMat3D();

        Matrices.multiplyMat3D(out.data(), out.offset(), stack, in1, in2.data(), in2.offset());
        return out;
    }

    /**
     * Sets the internal matrix to the transpose of itself.
     *
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat3Builder transpose() {
        final int in = this.current;
        final int out = this.current - 9;

        Matrices.transpose3D(stack, out, stack, in);
        System.arraycopy(stack, out, stack, current, 9);
        return this;
    }

    /**
     * Sets the internal matrix to the inverse of itself.
     *
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat3Builder inverse() {
        final int in = this.current;
        final int out = this.current - 9;

        Matrices.inverse3D(stack, out, stack, in);
        System.arraycopy(stack, out, stack, current, 9);
        return this;
    }

    @Override
    public GLMatF asGLMatF() {
        return this.asGLMat3F();
    }

    @Override
    public GLMatD asGLMatD() {
        return this.asGLMat3D();
    }

    @Override
    public GLMat3F asGLMat3F() {
        return this.asGLMat3D().asGLMat3F();
    }

    @Override
    public GLMat3D asGLMat3D() {
        return GLMat3D.create().set(0, 0, stack, current, 9, 3);
    }

    @Override
    public String toString() {
        return this.asGLMat3D().toString();
    }
}
