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
 * An implementation of GLMat2 that uses a builder pattern.
 *
 * @author zmichaels
 * @since 16.02.29
 */
public class GLMat2Builder implements GLMat2 {

    private final double[] stack;
    private final int stackSize;
    private int current;

    /**
     * Constructs a new GLMat2Builder with the minimum stack size and the
     * identity matrix as the base matrix.
     *
     * @since 16.02.29
     */
    public GLMat2Builder() {
        this(3, GLMat2D.create());
    }

    /**
     * Constructs a new GLMat2Builder with the specified stack size and the
     * identity matrix as the base matrix.
     *
     * @param stackSize the stack size. Must be at least 3 for access to all
     * operations.
     * @since 16.02.29
     */
    public GLMat2Builder(final int stackSize) {
        this(stackSize, GLMat2D.create());
    }

    /**
     * Constructs a new GLMat2Builder with the specified stack size and the
     * specified base matrix.
     *
     * @param stackSize the stack size. Must be at least 3 for access to all
     * operations.
     * @param base the base matrix.
     * @since 16.02.29
     */
    public GLMat2Builder(final int stackSize, final GLMat2 base) {
        this.stackSize = stackSize;
        this.stack = new double[4 * stackSize];
        this.current = 4 * stackSize - 4;

        base.asGLMat2D().copyToArray(stack, current, 4);
    }

    /**
     * Retrieves the size of the internal matrix stack.
     *
     * @return the internal matrix stack size.
     * @since 16.02.29
     */
    public final int getStackSize() {
        return this.stackSize;
    }

    private int testBounds(final int next) {
        if (next > this.stack.length - 4) {
            throw new IndexOutOfBoundsException("stack overflow");
        } else if (next < 0) {
            throw new IndexOutOfBoundsException("stack underflow");
        } else {
            return next;
        }
    }

    /**
     * Pushes the current internal matrix onto the stack. This will preserve the
     * current internal matrix.
     *
     * @since 16.02.29
     */
    public final void push() {
        final int next = this.testBounds(this.current - 4);

        System.arraycopy(this.stack, this.current, this.stack, next, 4);
    }

    /**
     * Pops the internal matrix stack. This will restore the previous internal
     * matrix values.
     *
     * @since 16.02.29
     */
    public final void pop() {
        final int next = this.testBounds(this.current + 4);

        this.current = next;
    }

    /**
     * Sets the internal matrix to all 0's.
     *
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat2Builder setZero() {
        return this.setScale(0.0, 0.0);
    }

    /**
     * Sets the internal matrix to the identity matrix.
     *
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat2Builder setIdentity() {
        return this.setScale(1.0, 1.0);
    }

    /**
     * Override for [code]setTranslation(x, 1.0)[/code].
     *
     * @param x the translation along the x-axis.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat2Builder setTranslation(final double x) {
        return this.setTranslation(x, 1.0);
    }

    /**
     * Sets the internal matrix to a translation matrix.
     *
     * @param x the translation along the x-axis.
     * @param y the translation along the y-axis. Usually is 1.0.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat2Builder setTranslation(final double x, final double y) {
        stack[current] = 1.0;
        stack[current + 1] = 0.0;

        stack[current + 2] = x;
        stack[current + 3] = y;

        return this;
    }

    /**
     * Override for [code]prependTranslation(x, 1.0)[/code].
     *
     * @param x the translation along the x-axis.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat2Builder prependTranslation(final double x) {
        return this.prependTranslation(x, 1.0);
    }

    /**
     * Sets the internal matrix to the product of a translation matrix and the
     * internal matrix. Performs [code]internal = translation * internal[/code].
     *
     * @param x the translation along the x-axis.
     * @param y the translation along the y-axis. Usually this will be 1.0.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat2Builder prependTranslation(final double x, final double y) {
        final int in1 = this.current - 4;
        final int in2 = this.current;
        final int out = this.current - 8;

        stack[in1] = 1.0;
        stack[in1 + 1] = 0.0;

        stack[in1 + 2] = x;
        stack[in1 + 3] = y;

        Matrices.multiplyMat2D(stack, out, stack, in1, stack, in2);
        System.arraycopy(stack, out, stack, current, 4);
        return this;
    }

    /**
     * Override for [code]appendTranslation(x, 1.0)[/code].
     *
     * @param x the translation along the x-axis.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat2Builder appendTranslation(final double x) {
        return this.appendTranslation(x, 1.0);
    }

    /**
     * Sets the internal matrix to the product of the internal matrix and a
     * translation matrix. Performs [code]internal *= translation[/code].
     *
     * @param x the translation along the x-axis.
     * @param y the translation along the y-axis.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat2Builder appendTranslation(final double x, final double y) {
        final int in1 = this.current;
        final int in2 = this.current - 4;
        final int out = this.current - 8;

        stack[in2] = 1.0;
        stack[in2 + 2] = 0.0;

        stack[in2 + 3] = x;
        stack[in2 + 4] = y;

        Matrices.multiplyMat2D(stack, out, stack, in1, stack, in2);
        System.arraycopy(stack, out, stack, current, 4);
        return this;
    }

    /**
     * Override for [code]setScale(x, 1.0)[/code].
     *
     * @param x the scale along the x-axis.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat2Builder setScale(final double x) {
        return this.setScale(x, 1.0);
    }

    /**
     * Sets the internal matrix to a scale matrix.
     *
     * @param x the scale along the x-axis.
     * @param y the scale along the y-axis.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat2Builder setScale(final double x, final double y) {
        stack[current] = x;
        stack[current + 1] = 0.0;
        stack[current + 2] = 0.0;
        stack[current + 3] = y;

        return this;
    }

    /**
     * Override for [code]prependScale(x, 1.0)[/code].
     *
     * @param x the scale along the x-axis.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat2Builder prependScale(final double x) {
        return this.prependScale(x, 1.0);
    }

    /**
     * Sets the internal matrix to the product of the scale matrix and the
     * internal matrix. Performs [code]internal = scale * internal[/code].
     *
     * @param x the scale along the x-axis.
     * @param y the scale along the y-axis.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat2Builder prependScale(final double x, final double y) {
        final int in1 = this.current - 4;
        final int in2 = this.current;
        final int out = this.current - 8;

        stack[in1] = x;
        stack[in1 + 1] = 0.0;
        stack[in1 + 2] = 0.0;
        stack[in1 + 3] = y;

        Matrices.multiplyMat2D(stack, out, stack, in1, stack, in2);
        System.arraycopy(stack, out, stack, current, 4);
        return this;
    }

    /**
     * Override for [code]appendScale(x, 1.0)[/code].
     *
     * @param x the scale along the x-axis.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat2Builder appendScale(final double x) {
        return this.appendScale(x, 1.0);
    }

    /**
     * Sets the internal matrix to the product of the internal matrix and a
     * scale matrix. Performs [code]internal *= scale[/code].
     *
     * @param x the scale along the x-axis.
     * @param y the scale along the y-axis. Usually this will be 1.0.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat2Builder appendScale(final double x, final double y) {
        final int in1 = this.current;
        final int in2 = this.current - 4;
        final int out = this.current - 8;

        stack[in2] = x;
        stack[in2 + 1] = 0.0;

        stack[in2 + 2] = 0.0;
        stack[in2 + 3] = y;

        Matrices.multiplyMat3D(stack, out, stack, in1, stack, in2);
        System.arraycopy(stack, out, stack, current, 4);
        return this;
    }

    /**
     * Sets the internal matrix to the values of another matrix.
     *
     * @param other the other matrix.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat2Builder setMatrix(final GLMat2 other) {
        other.asGLMat2D().copyToArray(stack, current, 4);
        return this;
    }

    /**
     * Sets the internal matrix to the product of another matrix and the
     * internal matrix. Performs [code]internal = other * internal[/code].
     *
     * @param other the other matrix.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat2Builder prependMatrix(final GLMat2 other) {
        final GLMat2D in1 = other.asGLMat2D();
        final int in2 = this.current;
        final int out = this.current - 4;

        Matrices.multiplyMat2D(stack, out, in1.data(), in1.offset(), stack, in2);
        System.arraycopy(stack, out, stack, current, 4);
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
    public GLMat2Builder appendMatrix(final GLMat2 other) {
        final int in1 = this.current;
        final GLMat2D in2 = other.asGLMat2D();
        final int out = this.current - 4;

        Matrices.multiplyMat2D(stack, out, stack, in1, in2.data(), in2.offset());
        System.arraycopy(stack, out, stack, current, 4);
        return this;
    }

    /**
     * Multiplies the internal matrix by a vector.
     *
     * @param vec the vector.
     * @return the result of the matrix-vector multiplication.
     * @since 16.02.29
     */
    public GLVec2D multiply(final GLVec<?> vec) {
        final int in1 = this.current;
        final GLVec2D in2 = vec.asGLVecD().asGLVec2D();
        final GLVec2D out = GLVec2D.create();

        Matrices.multiplyVec2D(out.data(), out.offset(), stack, in1, in2.data(), in2.offset());
        return out;
    }

    /**
     * Multiplies the internal matrix by another matrix.
     *
     * @param mat the other matrix.
     * @return the result of the matrix-matrix multiplication.
     * @since 16.02.29
     */
    public GLMat2D multiply(final GLMat<?, ?> mat) {
        final int in1 = this.current;
        final GLMat2D in2 = mat.asGLMatD().asGLMat2D();
        final GLMat2D out = GLMat2D.create();

        Matrices.multiplyMat2D(out.data(), out.offset(), stack, in1, in2.data(), in2.offset());
        return out;
    }

    /**
     * Sets the internal matrix to the inverse of itself.
     *
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat2Builder inverse() {
        final int in = this.current;
        final int out = this.current - 4;

        Matrices.inverse2D(stack, out, stack, in);
        System.arraycopy(stack, out, stack, current, 4);
        return this;
    }

    /**
     * Sets the internal matrix to the transpose of itself.
     *
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat2Builder transpose() {
        final int in = this.current;
        final int out = this.current - 4;

        Matrices.transpose2D(stack, out, stack, in);
        System.arraycopy(stack, out, stack, current, 4);
        return this;
    }

    @Override
    public GLMatF asGLMatF() {
        return this.asGLMat2F();
    }

    @Override
    public GLMatD asGLMatD() {
        return this.asGLMat2D();
    }

    @Override
    public GLMat2F asGLMat2F() {
        return this.asGLMat2D().asGLMat2F();
    }

    @Override
    public GLMat2D asGLMat2D() {
        return GLMat2D.create().set(0, 0, stack, current, 4, 2);
    }
}
