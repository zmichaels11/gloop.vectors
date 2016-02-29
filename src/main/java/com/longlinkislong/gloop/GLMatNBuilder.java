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
 * An implementation of GLMatN that uses a builder pattern.
 *
 * @author zmichaels
 * @since 16.02.29
 */
public class GLMatNBuilder implements GLMatN {

    private final double[] stack;
    private final int stackSize;
    private final int size;
    private final int size2;
    private int current;

    /**
     * Constructs a new instance of GLMatNBuilder with the specified matrix
     * size. A stack size of 3 is used and the identity matrix is used for the
     * base matrix.
     *
     * @param size the size of the matrix.
     * @since 16.02.29
     */
    public GLMatNBuilder(final int size) {
        this(size, 3, GLMatND.create(size));
    }

    /**
     * Constructs a new instance of GLMatNBuilder with the specified matrix size
     * and the specified base matrix. A stack size of 3 is used.
     *
     * @param size the size of the
     * @param base the base matrix.
     * @since 16.02.29
     */
    public GLMatNBuilder(final int size, final GLMatN base) {
        this(size, 3, base);
    }

    /**
     * Constructs a new instance of GLMatNBuilder with the specified size and
     * stack size. The identity matrix is used for the base matrix. A minimum
     * stack size of 3 is required for access to all operations.
     *
     * @param size the matrix size.
     * @param stackSize the stack size.
     * @since 16.02.29
     */
    public GLMatNBuilder(final int size, final int stackSize) {
        this(size, stackSize, GLMatND.create(size));
    }

    /**
     * Constructs a new instance of GLMatNBuilder with the specified size, stack
     * size, and base matrix. A minimum stack size of 3 is required for access
     * to all operations.
     *
     * @param size the matrix size.
     * @param stackSize the stack size.
     * @param base the base matrix.
     * @since 16.02.29
     */
    public GLMatNBuilder(final int size, final int stackSize, final GLMatN base) {
        this.size = size;
        this.size2 = size * size;
        this.stackSize = stackSize;
        this.stack = new double[size2 * stackSize];
        this.current = size2 * stackSize - size2;

        base.asGLMatND().copyToArray(stack, current, size2);
    }

    /**
     * Retrieves the internal matrix stack size.
     *
     * @return the stack size.
     * @since 16.02.29
     */
    public final int getStackSize() {
        return this.stackSize;
    }

    /**
     * Retrieves the size of the internal matrix.
     *
     * @return the size of the matrix.
     * @since 16.02.29
     */
    public final int getSize() {
        return this.size;
    }

    private int testBounds(final int next) {
        if (next > this.stack.length - this.size) {
            throw new IndexOutOfBoundsException("Stack overflow");
        } else if (next < 0) {
            throw new IndexOutOfBoundsException("Stack underflow");
        } else {
            return next;
        }
    }

    /**
     * Pushes the internal matrix stack. This will preserve the current matrix
     * values.
     *
     * @since 16.02.29
     */
    public final void push() {
        final int next = this.testBounds(this.current - this.size);

        System.arraycopy(this.stack, this.current, this.stack, next, this.size);
    }

    /**
     * Pops the internal matrix stack. This will restore the previous matrix
     * values.
     *
     * @since 16.02.29
     */
    public final void pop() {
        final int next = this.testBounds(this.current + this.size);

        this.current = next;
    }

    /**
     * Sets all of the matrix values to 0's.
     *
     * @return self reference.
     * @since 16.02.29
     */
    public GLMatNBuilder setZero() {
        for (int i = 0; i < this.size2; i++) {
            stack[current + i] = 0.0;
        }

        return this;
    }

    /**
     * Sets the internal matrix to the identity matrix.
     *
     * @return self reference.
     * @since 16.02.29
     */
    public GLMatNBuilder setIdentity() {
        for (int i = 0; i < this.size2; i++) {
            stack[current + i] = i % this.size == 0 ? 1.0 : 0.0;
        }

        return this;
    }

    /**
     * Sets the internal matrix to a translation matrix. Any missing values will
     * be interpreted as the corresponding value in the identity matrix.
     *
     * @param values the values of the translation matrix.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMatNBuilder setTranslation(final double... values) {
        for (int i = 0; i < this.size2 - this.size; i++) {
            stack[current + i] = i % this.size == 0 ? 1.0 : 0.0;
        }

        System.arraycopy(values, 0, stack, current + this.size2 - this.size, this.size);

        return this;
    }

    /**
     * Sets the internal matrix to the product of a translation matrix and the
     * internal matrix. Performs [code]internal = translation * internal[/code].
     *
     * @param values the values of the translation matrix. Any missing value
     * will be interpreted as the corresponding value in the identity matrix.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMatNBuilder prependTranslation(final double... values) {
        final int in1 = this.current - this.size2;
        final int in2 = this.current;
        final int out = this.current - this.size2 - this.size2;

        for (int i = 0; i < this.size2 - this.size; i++) {
            stack[in1 + i] = i % this.size == 0 ? 1.0 : 0.0;
        }

        System.arraycopy(values, 0, stack, in1 + this.size2 - this.size, this.size);
        Matrices.multiplyMatND(stack, out, stack, in1, stack, in2, this.size);
        System.arraycopy(stack, out, stack, current, this.size2);

        return this;
    }

    /**
     * Sets the internal matrix to the product of the internal matrix and a
     * translation matrix. Performs [code]internal *= translation[/code].
     *
     * @param values the internal matrix. Any missing value will be interpreted
     * as the corresponding value in an identity matrix.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMatNBuilder appendTranslation(final double... values) {
        final int in1 = this.current;
        final int in2 = this.current - this.size2;
        final int out = this.current - this.size2 - this.size2;

        for (int i = 0; i < this.size2 - this.size; i++) {
            stack[in2 + i] = i % this.size == 0 ? 1.0 : 0.0;
        }

        System.arraycopy(values, 0, stack, in2 + this.size2 - this.size, this.size);
        Matrices.multiplyMatND(stack, out, stack, in1, stack, in2, this.size);
        System.arraycopy(stack, out, stack, current, this.size2);

        return this;
    }

    /**
     * Sets the internal matrix to a scale matrix.
     *
     * @param values the scale values.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMatNBuilder setScale(final double... values) {
        int j = 0;

        for (int i = 0; i < this.size2; i++) {
            stack[current + i] = i % this.size == 0 ? values[j++] : 0.0;
        }

        return this;
    }

    /**
     * Sets the internal matrix to the product of a scale matrix and the
     * internal matrix. Performs [code]internal = scale * internal[/code].
     *
     * @param values the scale matrix.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMatNBuilder prependScale(final double... values) {
        final int in1 = this.current - this.size2;
        final int in2 = this.current;
        final int out = this.current - this.size2 - this.size2;

        int j = 0;

        for (int i = 0; i < this.size2; i++) {
            stack[in1 + i] = i % this.size == 0 ? values[j++] : 0.0;
        }

        Matrices.multiplyMatND(stack, out, stack, in1, stack, in2, this.size);
        System.arraycopy(stack, out, stack, current, this.size2);

        return this;
    }

    /**
     * Sets the internal matrix to the product of the internal matrix and a
     * scale matrix. Performs [code]internal *= scale[/code].
     *
     * @param values the scale matrix.
     * @return self reference.
     * @since 16.02.29
     */
    public GLMatNBuilder appendScale(final double... values) {
        final int in1 = this.current;
        final int in2 = this.current - this.size2;
        final int out = this.current - this.size2 - this.size2;

        int j = 0;

        for (int i = 0; i < this.size2; i++) {
            stack[in2 + i] = i % this.size == 0 ? values[j++] : 0.0;
        }

        Matrices.multiplyMatND(stack, out, stack, in1, stack, in2, this.size);
        System.arraycopy(stack, out, stack, current, this.size2);

        return this;
    }

    /**
     * Sets the internal matrix to the transpose of itself.
     *
     * @return self reference.
     * @since 16.02.29
     */
    public GLMatNBuilder transpose() {
        final int in = this.current;
        final int out = this.current - this.size2;

        Matrices.transposeND(stack, out, stack, in, this.size2);
        System.arraycopy(stack, out, stack, current, this.size2);
        return this;
    }

    @Override
    public GLMatF asGLMatF() {
        return this.asGLMatNF();
    }

    @Override
    public GLMatD asGLMatD() {
        return this.asGLMatND();
    }

    @Override
    public GLMatNF asGLMatNF() {
        return this.asGLMatND().asGLMatNF();
    }

    @Override
    public GLMatND asGLMatND() {
        return GLMatND.create(this.size).set(0, 0, stack, current, this.size2, this.size);
    }

}
