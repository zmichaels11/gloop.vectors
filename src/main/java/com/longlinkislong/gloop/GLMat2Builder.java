/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longlinkislong.gloop;

/**
 *
 * @author zmichaels
 */
public class GLMat2Builder implements GLMat2 {

    private final double[] stack;
    private final int stackSize;
    private int current;

    public GLMat2Builder() {
        this(3);
    }

    public GLMat2Builder(final int stackSize) {
        this(stackSize, GLMat2D.create());
    }

    public GLMat2Builder(final int stackSize, final GLMat2 base) {
        this.stackSize = stackSize;
        this.stack = new double[4 * stackSize];
        this.current = 4 * stackSize - 4;

        base.asGLMat2D().copyToArray(stack, current, 4);
    }

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

    public final void push() {
        final int next = this.testBounds(this.current - 4);

        System.arraycopy(this.stack, this.current, this.stack, next, 4);
    }

    public final void pop() {
        final int next = this.testBounds(this.current + 4);

        this.current = next;
    }

    public GLMat2Builder setZero() {
        return this.setScale(0.0, 0.0);
    }

    public GLMat2Builder setIdentity() {
        return this.setScale(1.0, 1.0);
    }

    public GLMat2Builder setTranslation(final double x, final double y) {
        stack[current] = 1.0;
        stack[current + 1] = 0.0;

        stack[current + 2] = x;
        stack[current + 3] = y;

        return this;
    }

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

    public GLMat2Builder setScale(final double x, final double y) {
        stack[current] = x;
        stack[current + 1] = 0.0;
        stack[current + 2] = 0.0;
        stack[current + 3] = y;

        return this;
    }

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

    public GLMat2Builder setMatrix(final GLMat2 other) {
        other.asGLMat2D().copyToArray(stack, current, 4);
        return this;
    }

    public GLMat2Builder prependMatrix(final GLMat2 other) {
        final GLMat2D in1 = other.asGLMat2D();
        final int in2 = this.current;
        final int out = this.current - 4;

        Matrices.multiplyMat2D(stack, out, in1.data(), in1.offset(), stack, in2);
        System.arraycopy(stack, out, stack, current, 4);
        return this;
    }

    public GLMat2Builder appendMatrix(final GLMat2 other) {
        final int in1 = this.current;
        final GLMat2D in2 = other.asGLMat2D();
        final int out = this.current - 4;

        Matrices.multiplyMat2D(stack, out, stack, in1, in2.data(), in2.offset());
        System.arraycopy(stack, out, stack, current, 4);
        return this;
    }
    
    public GLVec2D multiply(final GLVec<?> vec) {
        final int in1 = this.current;
        final GLVec2D in2 = vec.asGLVecD().asGLVec2D();
        final GLVec2D out = GLVec2D.create();
        
        Matrices.multiplyVec2D(out.data(), out.offset(), stack, in1, in2.data(), in2.offset());
        return out;
    }
    
    public GLMat2D multiply(final GLMat<?, ?> mat) {
        final int in1 = this.current;
        final GLMat2D in2 = mat.asGLMatD().asGLMat2D();
        final GLMat2D out = GLMat2D.create();
        
        Matrices.multiplyMat2D(out.data(), out.offset(), stack, in1, in2.data(), in2.offset());
        return out;
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
