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
 * An implementation of GLMat4 that utilizes a builder pattern.
 *
 * @author zmichaels
 * @since 16.02.29
 */
public class GLMat4Builder implements GLMat4 {

    private final double[] stack;
    private final int stackSize;
    private int current;

    /**
     * Constructs a new GLMat4Builder with no aditional stack.
     *
     * @since 16.02.29
     */
    public GLMat4Builder() {
        this(3, GLMat4D.create());
    }

    /**
     * Constructs a new GLMat4Builder with the specified base matrix.
     *
     * @param base the base matrix.
     * @since 16.02.29
     */
    public GLMat4Builder(final GLMat4 base) {
        this(3, base);
    }

    /**
     * Constructs a new GLMat4Builder with the specified stacksize. A minimum
     * stack size of 3 is required for all operations.
     *
     * @param stackSize the stack depth for the internal matrix.
     * @since 16.02.29
     */
    public GLMat4Builder(final int stackSize) {
        this(stackSize, GLMat4D.create());
    }

    /**
     * Constructs a new GLMat4Builder with the specified
     *
     * @param stackSize the stack depth for the internal matrix.
     * @param base the base matrix.
     * @since 16.02.29
     */
    public GLMat4Builder(final int stackSize, final GLMat4 base) {
        this.stackSize = stackSize;
        this.stack = new double[16 * stackSize];
        this.current = 16 * stackSize - 16;

        base.asGLMat4D().copyToArray(this.stack, this.current, 16);
    }

    /**
     * Retrieves the size of the internal matrix stack.
     *
     * @return the number of matrices deep the stack is.
     * @since 16.02.29
     */
    public final int getStackSize() {
        return this.stackSize;
    }

    private int testBounds(final int next) {
        if (next > this.stack.length - 16) {
            throw new IndexOutOfBoundsException("Stack overflow!");
        } else if (next < 0) {
            throw new IndexOutOfBoundsException("Stack underflow!");
        } else {
            return next;
        }
    }

    /**
     * Pushes the internal stack. This will preserve the current value and
     * retrieve the next matrix position. The next matrix may contain unclean
     * data.
     *
     * @since 16.02.29
     */
    public final void push() {
        final int next = this.testBounds(this.current - 16);

        System.arraycopy(this.stack, this.current, this.stack, next, 16);
        this.current = next;
    }

    /**
     * Pops the internal stack. This will restore the previous value.
     *
     * @since 16.02.29
     */
    public final void pop() {
        final int next = this.testBounds(this.current + 16);

        this.current = next;
    }

    /**
     * Sets the internal matrix to all 0's.
     *
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat4Builder setZero() {
        return this.setScale(0.0, 0.0, 0.0, 0.0);
    }

    /**
     * Sets the internal matrix to the identity matrix.
     *
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat4Builder setIdentity() {
        return this.setScale(1.0, 1.0, 1.0, 1.0);
    }

    /**
     * Override for [code]setTransform(x, 0.0, 0.0, 1.0)[/code]
     *
     * @param x the translation along the x-axis.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat4Builder setTranslation(final double x) {
        return this.setTranslation(x, 0.0, 0.0, 1.0);
    }

    /**
     * Override for [code]setTransform(x, y, 0.0, 1.0)[/code]
     *
     * @param x the translation along the x-axis.
     * @param y the translation along the y-axis.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat4Builder setTranslation(final double x, final double y) {
        return this.setTranslation(x, y, 0.0, 1.0);
    }

    /**
     * Override for [code]setTransform(x, y, z, 1.0)[/code]
     *
     * @param x the translation along the x-axis.
     * @param y the translation along the y-axis.
     * @param z the translation along the z-axis.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat4Builder setTranslation(final double x, final double y, final double z) {
        return this.setTranslation(x, y, z, 1.0);
    }

    /**
     * Sets the internal matrix to the translation matrix.
     *
     * @param x the translation along the x-axis.
     * @param y the translation along the y-axis.
     * @param z the translation along the z-axis.
     * @param w the translation w-component. Usually this will be 1.0.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat4Builder setTranslation(final double x, final double y, final double z, final double w) {
        stack[current] = 1.0;
        stack[current + 1] = 0.0;
        stack[current + 2] = 0.0;
        stack[current + 3] = 0.0;

        stack[current + 4] = 0.0;
        stack[current + 5] = 1.0;
        stack[current + 6] = 0.0;
        stack[current + 7] = 0.0;

        stack[current + 8] = 0.0;
        stack[current + 9] = 0.0;
        stack[current + 10] = 1.0;
        stack[current + 11] = 0.0;

        stack[current + 12] = x;
        stack[current + 13] = y;
        stack[current + 14] = z;
        stack[current + 15] = w;

        return this;
    }

    /**
     * Override for [code]prependTranslation(x, 0.0, 0.0, 1.0)[/code].
     *
     * @param x the translation along the x-axis.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat4Builder prependTranslation(final double x) {
        return this.prependTranslation(x, 0.0, 0.0, 1.0);
    }

    /**
     * Override for [code]prependTranslation(x, y, 0.0, 1.0)[/code].
     *
     * @param x the translation along the x-axis.
     * @param y the translation along the y-axis.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat4Builder prependTranslation(final double x, final double y) {
        return this.prependTranslation(x, y, 0.0, 1.0);
    }

    /**
     * Override for [code]prependTranslation(x, y, z, 1.0)[/code].
     *
     * @param x the translation along the x-axis.
     * @param y the translation along the y-axis.
     * @param z the translation along the z-axis.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat4Builder prependTranslation(final double x, final double y, final double z) {
        return this.prependTranslation(x, y, z, 1.0);
    }

    /**
     * Sets the internal matrix to the product of a translation matrix and the
     * internal matrix. [code]internal = translation * internal[/code]
     *
     * @param x the translation along the x-axis.
     * @param y the translation along the y-axis.
     * @param z the translation along the z-axis.
     * @param w the w-component of the translation matrix. Usually this will be
     * 1.0.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat4Builder prependTranslation(final double x, final double y, final double z, final double w) {
        final int in1 = this.current - 16;
        final int in2 = this.current;
        final int out = this.current - 32;

        stack[in1] = 1.0;
        stack[in1 + 1] = 0.0;
        stack[in1 + 2] = 0.0;
        stack[in1 + 3] = 0.0;

        stack[in1 + 4] = 0.0;
        stack[in1 + 5] = 1.0;
        stack[in1 + 6] = 0.0;
        stack[in1 + 7] = 0.0;

        stack[in1 + 8] = 0.0;
        stack[in1 + 9] = 0.0;
        stack[in1 + 10] = 1.0;
        stack[in1 + 11] = 0.0;

        stack[in1 + 12] = x;
        stack[in1 + 13] = y;
        stack[in1 + 14] = z;
        stack[in1 + 15] = w;

        Matrices.multiplyMat4D(stack, out, stack, in1, stack, in2);
        System.arraycopy(stack, out, stack, current, 16);
        return this;
    }

    /**
     * Override for [code]appendTranslation(x, 0.0, 0.0, 1.0)[/code].
     *
     * @param x the translation along the x-axis.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat4Builder appendTranslation(final double x) {
        return this.appendTranslation(x, 0.0, 0.0, 1.0);
    }

    /**
     * Override for [code]appendTranslation(x, y, 0.0, 1.0)[/code].
     *
     * @param x the translation along the x-axis.
     * @param y the translation along the y-axis.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat4Builder appendTranslation(final double x, final double y) {
        return this.appendTranslation(x, y, 0.0, 1.0);
    }

    /**
     * Override for [code]appendTranslation(x, y, z, 1.0)[/code]
     *
     * @param x the translation along the x-axis.
     * @param y the translation along the y-axis.
     * @param z the translation along the z-axis.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat4Builder appendTranslation(final double x, final double y, final double z) {
        return this.appendTranslation(x, y, z, 1.0);
    }

    /**
     * Sets the internal matrix to the product of the internal matrix and a
     * translation matrix. Performs [code]internal *= translation[/code].
     *
     * @param x the translation along the x-axis.
     * @param y the translation along the y-axis.
     * @param z the translation along the z-axis.
     * @param w the w-component of the translation matrix. Usually this will be
     * 1.0.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat4Builder appendTranslation(final double x, final double y, final double z, final double w) {
        final int in1 = this.current;
        final int in2 = this.current - 16;
        final int out = this.current - 32;

        stack[in2] = 1.0;
        stack[in2 + 1] = 0.0;
        stack[in2 + 2] = 0.0;
        stack[in2 + 3] = 0.0;

        stack[in2 + 4] = 0.0;
        stack[in2 + 5] = 1.0;
        stack[in2 + 6] = 0.0;
        stack[in2 + 7] = 0.0;

        stack[in2 + 8] = 0.0;
        stack[in2 + 9] = 0.0;
        stack[in2 + 10] = 1.0;
        stack[in2 + 11] = 0.0;

        stack[in2 + 12] = x;
        stack[in2 + 13] = y;
        stack[in2 + 14] = z;
        stack[in2 + 15] = w;

        Matrices.multiplyMat4D(stack, out, stack, in1, stack, in2);
        System.arraycopy(stack, out, stack, current, 16);
        return this;
    }

    /**
     * Override for [code]setScale(x, 1.0, 1.0, 1.0)[/code].
     *
     * @param x the x-axis scale.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat4Builder setScale(final double x) {
        return this.setScale(x, 1.0, 1.0, 1.0);
    }

    /**
     * Override for [code]setScale(x, y, 1.0, 1.0)[/code].
     *
     * @param x the x-axis scale.
     * @param y the y-axis scale.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat4Builder setScale(final double x, final double y) {
        return this.setScale(x, y, 1.0, 1.0);
    }

    /**
     * Override for [code]setScale(x, y, z, 1.0)[/code].
     *
     * @param x the x-axis scale.
     * @param y the y-axis scale.
     * @param z the z-axis scale.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat4Builder setScale(final double x, final double y, final double z) {
        return this.setScale(x, y, z, 1.0);
    }

    /**
     * Sets the internal matrix to a scale matrix.
     *
     * @param x the x-axis scale.
     * @param y the y-axis scale.
     * @param z the z-axis scale.
     * @param w the scale value for the w-axis. Usually this will be 1.0.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat4Builder setScale(final double x, final double y, final double z, final double w) {
        stack[current] = x;
        stack[current + 1] = 0.0;
        stack[current + 2] = 0.0;
        stack[current + 3] = 0.0;

        stack[current + 4] = 0.0;
        stack[current + 5] = y;
        stack[current + 6] = 0.0;
        stack[current + 7] = 0.0;

        stack[current + 8] = 0.0;
        stack[current + 9] = 0.0;
        stack[current + 10] = z;
        stack[current + 11] = 0.0;

        stack[current + 12] = 0.0;
        stack[current + 13] = 0.0;
        stack[current + 14] = 0.0;
        stack[current + 15] = w;

        return this;
    }

    /**
     * Override for [code]prependScale(x, 1.0, 1.0, 1.0)[/code].
     *
     * @param x the x-axis scale.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat4Builder prependScale(final double x) {
        return this.prependScale(x, 1.0, 1.0, 1.0);
    }

    /**
     * Override for [code]prependScale(x, y, 1.0, 1.0)[/code].
     *
     * @param x the x-axis scale.
     * @param y the y-axis scale.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat4Builder prependScale(final double x, final double y) {
        return this.prependScale(x, y, 1.0, 1.0);
    }

    /**
     * Override for [code]prependScale(x, y, z, 1.0)[/code].
     *
     * @param x the x-axis scale.
     * @param y the y-axis scale.
     * @param z the z-axis scale.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat4Builder prependScale(final double x, final double y, final double z) {
        return this.prependScale(x, y, z, 1.0);
    }

    /**
     * Sets the internal matrix to the product of a scale matrix and the
     * internal matrix. Performs [code]internal = scale * internal[/code].
     *
     * @param x the x-axis scale.
     * @param y the y-axis scale.
     * @param z the z-axis scale.
     * @param w the scale along the w-axis. Usually this is 1.0.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat4Builder prependScale(final double x, final double y, final double z, final double w) {
        final int in1 = this.current - 16;
        final int in2 = this.current;
        final int out = this.current - 32;

        stack[in1] = x;
        stack[in1 + 1] = 0.0;
        stack[in1 + 2] = 0.0;
        stack[in1 + 3] = 0.0;

        stack[in1 + 4] = 0.0;
        stack[in1 + 5] = y;
        stack[in1 + 6] = 0.0;
        stack[in1 + 7] = 0.0;

        stack[in1 + 8] = 0.0;
        stack[in1 + 9] = 0.0;
        stack[in1 + 10] = z;
        stack[in1 + 11] = 0.0;

        stack[in1 + 12] = 0.0;
        stack[in1 + 13] = 0.0;
        stack[in1 + 14] = 0.0;
        stack[in1 + 15] = w;

        Matrices.multiplyMat4D(stack, out, stack, in1, stack, in2);
        System.arraycopy(stack, out, stack, current, 16);
        return this;
    }

    /**
     * Override for [code]appendScale(x, 1.0, 1.0, 1.0)[/code].
     *
     * @param x the x-axis scale.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat4Builder appendScale(final double x) {
        return this.appendScale(x, 1.0, 1.0, 1.0);
    }

    /**
     * Override for [code]appendScale(x, y, 1.0, 1.0)[/code].
     *
     * @param x the x-axis scale.
     * @param y the y-axis scale.
     * @return self reference/
     * @since 16.02.29
     */
    public GLMat4Builder appendScale(final double x, final double y) {
        return this.appendScale(x, y, 1.0, 1.0);
    }

    /**
     * Override for [code]appendScale(x, y, z, 1.0)[/code].
     *
     * @param x the x-axis scale.
     * @param y the y-axis scale.
     * @param z the z-axis scale.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat4Builder appendScale(final double x, final double y, final double z) {
        return this.appendScale(x, y, z, 1.0);
    }

    /**
     * Sets the internal matrix to the product of the internal matrix and a
     * scale matrix. Performs [code]internal *= scale[/code].
     *
     * @param x the x-axis scale.
     * @param y the y-axis scale.
     * @param z the z-axis scale.
     * @param w scale of the w-component. Usually this is 1.0.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat4Builder appendScale(final double x, final double y, final double z, final double w) {
        final int in1 = this.current;
        final int in2 = this.current - 16;
        final int out = this.current - 32;

        stack[in2] = x;
        stack[in2 + 1] = 0.0;
        stack[in2 + 2] = 0.0;
        stack[in2 + 3] = 0.0;

        stack[in2 + 4] = 0.0;
        stack[in2 + 5] = y;
        stack[in2 + 6] = 0.0;
        stack[in2 + 7] = 0.0;

        stack[in2 + 8] = 0.0;
        stack[in2 + 9] = 0.0;
        stack[in2 + 10] = z;
        stack[in2 + 11] = 0.0;

        stack[in2 + 12] = 0.0;
        stack[in2 + 13] = 0.0;
        stack[in2 + 14] = 0.0;
        stack[in2 + 15] = w;

        Matrices.multiplyMat4D(stack, out, stack, in1, stack, in2);
        System.arraycopy(stack, out, stack, current, 16);
        return this;
    }

    /**
     * Sets the internal matrix to a rotation matrix along the z-axis.
     *
     * @param angle the angle in radians.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat4Builder setRotateZ(final double angle) {
        final double sa = Math.sin(angle);
        final double ca = Math.cos(angle);

        stack[current] = ca;
        stack[current + 1] = sa;
        stack[current + 2] = 0.0;
        stack[current + 3] = 0.0;

        stack[current + 4] = -sa;
        stack[current + 5] = ca;
        stack[current + 6] = 0.0;
        stack[current + 7] = 0.0;

        stack[current + 8] = 0.0;
        stack[current + 9] = 0.0;
        stack[current + 10] = 1.0;
        stack[current + 11] = 0.0;

        stack[current + 12] = 0.0;
        stack[current + 13] = 0.0;
        stack[current + 14] = 0.0;
        stack[current + 15] = 1.0;

        return this;
    }

    /**
     * Sets the internal matrix to the product of a rotation matrix and the
     * internal matrix. Performs [code]internal = rotation * internal[/code]
     *
     * @param angle the angle in radians.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat4Builder prependRotateZ(final double angle) {
        final int in1 = this.current - 16;
        final int in2 = this.current;
        final int out = this.current - 32;

        final double sa = Math.sin(angle);
        final double ca = Math.cos(angle);

        stack[in1] = ca;
        stack[in1 + 1] = sa;
        stack[in1 + 2] = 0.0;
        stack[in1 + 3] = 0.0;

        stack[in1 + 4] = -sa;
        stack[in1 + 5] = ca;
        stack[in1 + 6] = 0.0;
        stack[in1 + 7] = 0.0;

        stack[in1 + 8] = 0.0;
        stack[in1 + 9] = 0.0;
        stack[in1 + 10] = 1.0;
        stack[in1 + 11] = 0.0;

        stack[in1 + 12] = 0.0;
        stack[in1 + 13] = 0.0;
        stack[in1 + 14] = 0.0;
        stack[in1 + 15] = 1.0;

        Matrices.multiplyMat4D(stack, out, stack, in1, stack, in2);
        System.arraycopy(stack, out, stack, current, 16);
        return this;
    }

    /**
     * Sets the internal matrix to the product of the internal matrix and the
     * rotation matrix. Performs [code]internal *= rotation[/code]
     *
     * @param angle the angle in radians.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat4Builder appendRotateZ(final double angle) {
        final int in1 = this.current;
        final int in2 = this.current - 16;
        final int out = this.current - 32;

        final double sa = Math.sin(angle);
        final double ca = Math.cos(angle);

        stack[in2] = ca;
        stack[in2 + 1] = sa;
        stack[in2 + 2] = 0.0;
        stack[in2 + 3] = 0.0;

        stack[in2 + 4] = -sa;
        stack[in2 + 5] = ca;
        stack[in2 + 6] = 0.0;
        stack[in2 + 7] = 0.0;

        stack[in2 + 8] = 0.0;
        stack[in2 + 9] = 0.0;
        stack[in2 + 10] = 1.0;
        stack[in2 + 11] = 0.0;

        stack[in2 + 12] = 0.0;
        stack[in2 + 13] = 0.0;
        stack[in2 + 14] = 0.0;
        stack[in2 + 15] = 1.0;

        Matrices.multiplyMat4D(stack, out, stack, in1, stack, in2);
        System.arraycopy(stack, out, stack, current, 16);
        return this;
    }

    /**
     * Sets the internal matrix to a rotation matrix along the x-axis.
     *
     * @param angle the angle in radians.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat4Builder setRotateX(final double angle) {
        final double sa = Math.sin(angle);
        final double ca = Math.cos(angle);

        stack[current] = 1.0;
        stack[current + 1] = 0.0;
        stack[current + 2] = 0.0;
        stack[current + 3] = 0.0;

        stack[current + 4] = 0.0;
        stack[current + 5] = ca;
        stack[current + 6] = sa;
        stack[current + 7] = 0.0;

        stack[current + 8] = 0.0;
        stack[current + 9] = -sa;
        stack[current + 10] = ca;
        stack[current + 11] = 0.0;

        stack[current + 12] = 0.0;
        stack[current + 13] = 0.0;
        stack[current + 14] = 0.0;
        stack[current + 15] = 1.0;

        return this;
    }

    /**
     * Sets the internal matrix equal to the product of a rotation matrix and
     * the internal matrix. Performs [code]internal = rotation *
     * internal[/code].
     *
     * @param angle the angle in radians.
     * @return self-reference.
     * @since 16.02.29
     */
    public GLMat4Builder prependRotateX(final double angle) {
        final int in1 = this.current - 16;
        final int in2 = this.current;
        final int out = this.current - 32;

        final double sa = Math.sin(angle);
        final double ca = Math.cos(angle);

        stack[in1] = 1.0;
        stack[in1 + 1] = 0.0;
        stack[in1 + 2] = 0.0;
        stack[in1 + 3] = 0.0;

        stack[in1 + 4] = 0.0;
        stack[in1 + 5] = ca;
        stack[in1 + 6] = sa;
        stack[in1 + 7] = 0.0;

        stack[in1 + 8] = 0.0;
        stack[in1 + 9] = -sa;
        stack[in1 + 10] = ca;
        stack[in1 + 11] = 0.0;

        stack[in1 + 12] = 0.0;
        stack[in1 + 13] = 0.0;
        stack[in1 + 14] = 0.0;
        stack[in1 + 15] = 1.0;

        Matrices.multiplyMat4D(stack, out, stack, in1, stack, in2);
        System.arraycopy(stack, out, stack, current, 16);

        return this;
    }

    /**
     * Sets the internal matrix to the product of the internal matrix and a
     * rotation matrix. Performs [code]internal *= rotation[/code].
     *
     * @param angle the angle in radians.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat4Builder appendRotateX(final double angle) {
        final int in1 = this.current;
        final int in2 = this.current - 16;
        final int out = this.current - 32;

        final double sa = Math.sin(angle);
        final double ca = Math.cos(angle);

        stack[in2] = 1.0;
        stack[in2 + 1] = 0.0;
        stack[in2 + 2] = 0.0;
        stack[in2 + 3] = 0.0;

        stack[in2 + 4] = 0.0;
        stack[in2 + 5] = ca;
        stack[in2 + 6] = sa;
        stack[in2 + 7] = 0.0;

        stack[in2 + 8] = 0.0;
        stack[in2 + 9] = -sa;
        stack[in2 + 10] = ca;
        stack[in2 + 11] = 0.0;

        stack[in2 + 12] = 0.0;
        stack[in2 + 13] = 0.0;
        stack[in2 + 14] = 0.0;
        stack[in2 + 15] = 1.0;

        Matrices.multiplyMat4D(stack, out, stack, in1, stack, in2);
        System.arraycopy(stack, out, stack, current, 16);
        return this;
    }

    /**
     * Sets the internal matrix to a rotation matrix along the y-axis.
     *
     * @param angle the angle in radians.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat4Builder setRotateY(final double angle) {
        final double sa = Math.sin(angle);
        final double ca = Math.cos(angle);

        stack[current] = ca;
        stack[current + 1] = 0.0;
        stack[current + 2] = -sa;
        stack[current + 3] = 0.0;

        stack[current + 4] = 0.0;
        stack[current + 5] = 1.0;
        stack[current + 6] = 0.0;
        stack[current + 7] = 0.0;

        stack[current + 8] = sa;
        stack[current + 9] = 0.0;
        stack[current + 10] = ca;
        stack[current + 11] = 0.0;

        stack[current + 12] = 0.0;
        stack[current + 13] = 0.0;
        stack[current + 14] = 0.0;
        stack[current + 15] = 1.0;

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
    public GLMat4Builder prependRotateY(final double angle) {
        final int in1 = this.current - 16;
        final int in2 = this.current;
        final int out = this.current - 32;

        final double sa = Math.sin(angle);
        final double ca = Math.cos(angle);

        stack[in1] = ca;
        stack[in1 + 1] = 0.0;
        stack[in1 + 2] = -sa;
        stack[in1 + 3] = 0.0;

        stack[in1 + 4] = 0.0;
        stack[in1 + 5] = 1.0;
        stack[in1 + 6] = 0.0;
        stack[in1 + 7] = 0.0;

        stack[in1 + 8] = sa;
        stack[in1 + 9] = 0.0;
        stack[in1 + 10] = ca;
        stack[in1 + 11] = 0.0;

        stack[in1 + 12] = 0.0;
        stack[in1 + 13] = 0.0;
        stack[in1 + 14] = 0.0;
        stack[in1 + 15] = 1.0;

        Matrices.multiplyMat4D(stack, out, stack, in1, stack, in2);
        System.arraycopy(stack, out, stack, current, 16);
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
    public GLMat4Builder appendRotateY(final double angle) {
        final int in1 = this.current;
        final int in2 = this.current - 16;
        final int out = this.current - 32;

        final double sa = Math.sin(angle);
        final double ca = Math.cos(angle);

        stack[in2] = ca;
        stack[in2 + 1] = 0.0;
        stack[in2 + 2] = -sa;
        stack[in2 + 3] = 0.0;

        stack[in2 + 4] = 0.0;
        stack[in2 + 5] = 1.0;
        stack[in2 + 6] = 0.0;
        stack[in2 + 7] = 0.0;

        stack[in2 + 8] = sa;
        stack[in2 + 9] = 0.0;
        stack[in2 + 10] = ca;
        stack[in2 + 11] = 0.0;

        stack[in2 + 12] = 0.0;
        stack[in2 + 13] = 0.0;
        stack[in2 + 14] = 0.0;
        stack[in2 + 15] = 1.0;

        Matrices.multiplyMat4D(stack, out, stack, in1, stack, in2);
        System.arraycopy(stack, out, stack, current, 16);

        return this;
    }

    /**
     * Sets the internal matrix to an ortho matrix.
     *
     * @param left leftmost value.
     * @param right rightmost value.
     * @param bottom bottommost value.
     * @param top topmost value.
     * @param near near clipping.
     * @param far far clipping.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat4Builder setOrtho(
            final double left, final double right,
            final double bottom, final double top,
            final double near, final double far) {

        Matrices.ortho4D(stack, current, left, right, bottom, top, near, far);
        return this;
    }

    /**
     * Sets the internal matrix to the product of the ortho matrix and the
     * internal matrix. Performs [code]internal = ortho * internal[/code].
     *
     * @param left the leftmost value.
     * @param right the rightmost value.
     * @param bottom the bottommost value.
     * @param top the topmost value.
     * @param near the near clipping.
     * @param far the far clipping.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat4Builder prependOrtho(
            final double left, final double right,
            final double bottom, final double top,
            final double near, final double far) {

        final int in1 = this.current - 16;
        final int in2 = this.current;
        final int out = this.current - 32;

        Matrices.ortho4D(stack, in1, left, right, bottom, top, near, far);
        Matrices.multiplyMat4D(stack, out, stack, in1, stack, in2);
        System.arraycopy(stack, out, stack, current, 16);

        return this;
    }

    /**
     * Sets the internal matrix to the product of the internal matrix and the
     * ortho matrix. Performs [code]internal *= ortho[/code].
     *
     * @param left the leftmost value.
     * @param right the rightmost value.
     * @param bottom the bottommost value.
     * @param top the topmost value.
     * @param near the near clipping.
     * @param far the far clipping.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat4Builder appendOrtho(
            final double left, final double right,
            final double bottom, final double top,
            final double near, final double far) {

        final int in1 = this.current;
        final int in2 = this.current - 16;
        final int out = this.current - 32;

        Matrices.ortho4D(stack, in2, left, right, bottom, top, near, far);
        Matrices.multiplyMat4D(
                stack, out,
                stack, in1,
                stack, in2);
        System.arraycopy(stack, out, stack, current, 16);
        return this;
    }

    /**
     * Sets the internal matrix to the perspective matrix.
     *
     * @param fov the field of view in degrees.
     * @param aspect the aspect ratio.
     * @param near the near clipping.
     * @param far the far clipping.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat4Builder setPerspective(
            final double fov, final double aspect,
            final double near, final double far) {

        Matrices.perspective4D(stack, current, fov, aspect, near, far);
        return this;
    }

    /**
     * Sets the internal matrix to the product of the perspective matrix and the
     * internal matrix. Performs [code]internal = perspective * internal[/code].
     *
     * @param fov the field of view in degrees.
     * @param aspect the aspect ratio.
     * @param near the near clipping.
     * @param far the far clipping.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat4Builder prependPerspective(
            final double fov, final double aspect,
            final double near, final double far) {

        final int in1 = this.current - 16;
        final int in2 = this.current;
        final int out = this.current - 32;

        Matrices.perspective4D(stack, in1, fov, aspect, near, far);
        Matrices.multiplyMat4D(stack, out, stack, in1, stack, in2);
        System.arraycopy(stack, out, stack, current, 16);

        return this;
    }

    /**
     * Sets the internal matrix to the product of the internal matrix and the
     * perspective matrix. Performs [code]internal *= perspective[/code].
     *
     * @param fov the field of view.
     * @param aspect the aspect ratio.
     * @param near the near clipping.
     * @param far the far clipping.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat4Builder appendPerspective(
            final double fov, final double aspect,
            final double near, final double far) {

        final int in1 = this.current;
        final int in2 = this.current - 16;
        final int out = this.current - 32;

        Matrices.perspective4D(stack, in2, fov, aspect, near, far);
        Matrices.multiplyMat4D(
                stack, out,
                stack, in1,
                stack, in2);
        System.arraycopy(stack, out, stack, current, 16);
        return this;
    }

    /**
     * Sets the perspective matrix using infinity as the far clipping.
     *
     * @param fov the field of view.
     * @param aspect the aspect ratio.
     * @param near the near clipping.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat4Builder setPerspective(
            final double fov, final double aspect,
            final double near) {

        Matrices.perspective4D(stack, current, fov, aspect, near);
        return this;
    }

    /**
     * Sets the internal matrix to the product of the perspective matrix and the
     * internal matrix. Performs [code]internal = perspective * internal[/code].
     * The perspective matrix used uses infinity for the far clipping.
     *
     * @param fov the field of view.
     * @param aspect the aspect ratio.
     * @param near the near clipping.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat4Builder prependPerspective(
            final double fov, final double aspect,
            final double near) {

        final int in1 = this.current - 16;
        final int in2 = this.current;
        final int out = this.current - 32;

        Matrices.perspective4D(stack, in1, fov, aspect, near);
        Matrices.multiplyMat4D(stack, out, stack, in1, stack, in2);
        System.arraycopy(stack, out, stack, current, 16);

        return this;
    }

    /**
     * Sets the internal matrix to the product of the internal matrix and the
     * perspective matrix. Performs [code]internal *= perspective[/code]. The
     * perspective matrix uses infinity as the far clipping.
     *
     * @param fov the field of view.
     * @param aspect the aspect ratio.
     * @param near the near clipping.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat4Builder appendPerspective(
            final double fov, final double aspect,
            final double near) {

        final int in1 = this.current;
        final int in2 = this.current - 16;
        final int out = this.current - 32;

        Matrices.perspective4D(stack, in2, fov, aspect, near);
        Matrices.multiplyMat4D(stack, out, stack, in1, stack, in2);
        System.arraycopy(stack, out, stack, current, 16);

        return this;
    }

    /**
     * Sets the internal matrix to the value of another matrix.
     *
     * @param other the other matrix.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat4Builder setMatrix(final GLMat4 other) {
        other.asGLMat4D().copyToArray(stack, current, 16);
        return this;
    }

    /**
     * Sets the internal matrix to the product of another matrix and the
     * internal matrix. Performs [code]internal = matrix * internal[/code].
     *
     * @param other the other matrix.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat4Builder prependMatrix(final GLMat4 other) {
        final GLMat4D in1 = other.asGLMat4D();
        final int in2 = this.current;
        final int out = this.current - 16;

        Matrices.multiplyVec4D(stack, out, in1.data(), in1.offset(), stack, in2);
        System.arraycopy(stack, out, stack, current, 16);
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
    public GLMat4Builder appendMatrix(final GLMat4 other) {
        final int in1 = this.current;
        final int out = this.current - 16;
        final GLMat4D in2 = other.asGLMat4D();

        Matrices.multiplyMat4D(stack, out, stack, in1, in2.data(), in2.offset());
        System.arraycopy(stack, out, stack, current, 16);
        return this;
    }

    /**
     * Multiplies the internal matrix by a vector.
     *
     * @param vec the vector.
     * @return the result of the matrix-vector product.
     * @since 16.0.29.29
     */
    public GLVec4D multiply(final GLVec<?> vec) {
        final int in1 = this.current;
        final GLVec4D out = GLVec4D.create();
        final GLVec4D in2 = vec.asGLVecD().exGLVec4D();

        Matrices.multiplyVec4D(out.data(), out.offset(), stack, in1, in2.data(), in2.offset());
        return out;
    }

    /**
     * Multiplies the internal matrix by another matrix.
     *
     * @param mat the other matrix.
     * @return the result of the matrix multiplication.
     * @since 16.02.29
     */
    public GLMat4D multiply(final GLMat<?, ?> mat) {
        final int in1 = this.current;
        final GLMat4D out = GLMat4D.create();
        final GLMat4D in2 = mat.asGLMatD().asGLMat4D();

        Matrices.multiplyMat4D(out.data(), out.offset(), stack, in1, in2.data(), in2.offset());
        return out;
    }

    /**
     * Sets the internal matrix to the inverse of itself.
     *
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat4Builder inverse() {
        final int in = this.current;
        final int out = this.current - 16;

        Matrices.inverse4D(stack, out, stack, in);
        System.arraycopy(stack, out, stack, current, 16);
        return this;
    }

    /**
     * Sets the internal matrix to the transpose of itself.
     *
     * @return self reference.
     * @since 16.02.29
     */
    public GLMat4Builder transpose() {
        final int in = this.current;
        final int out = this.current - 16;

        Matrices.transpose4D(stack, out, stack, in);
        System.arraycopy(stack, out, stack, current, 16);
        return this;
    }

    @Override
    public GLMatF asGLMatF() {
        return this.asGLMat4F();
    }

    @Override
    public GLMatD asGLMatD() {
        return this.asGLMat4D();
    }

    @Override
    public GLMat4F asGLMat4F() {
        return this.asGLMat4D().asGLMat4F();
    }

    @Override
    public GLMat4D asGLMat4D() {
        return GLMat4D.create().set(0, 0, stack, current, 16, 4);
    }

    @Override
    public String toString() {
        return this.asGLMat4D().toString();
    }
}
