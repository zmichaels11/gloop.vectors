/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longlinkislong.gloop;

/**
 * An implementation of GLMat4 that utilizes a builder pattern.
 *
 * @author zmichaels
 */
public class GLMat4Builder implements GLMat4 {

    private final double[] stack;
    private final int stackSize;
    private int current;

    public GLMat4Builder() {
        this(3);
    }

    public GLMat4Builder(final int stackSize) {
        this(stackSize, GLMat4D.create());
    }

    public GLMat4Builder(final int stackSize, final GLMat4 base) {
        this.stackSize = stackSize;
        this.stack = new double[16 * stackSize];
        this.current = 16 * stackSize - 16;

        base.asGLMat4D().copyToArray(this.stack, this.current, 16);
    }

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

    public final void push() {
        final int next = this.testBounds(this.current - 16);

        System.arraycopy(this.stack, this.current, this.stack, next, 16);
        this.current = next;
    }

    public final void pop() {
        final int next = this.testBounds(this.current + 16);

        this.current = next;
    }    
    
    public GLMat4Builder setZero() {
        return this.setScale(0.0, 0.0, 0.0, 0.0);
    }
    
    public GLMat4Builder setIdentity() {
        return this.setScale(1.0, 1.0, 1.0, 1.0);
    }

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

    public void appendRotateY(final double angle) {
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
    }

    public GLMat4Builder setOrtho(
            final double left, final double right,
            final double bottom, final double top,
            final double near, final double far) {

        Matrices.ortho4D(stack, current, left, right, bottom, top, near, far);
        return this;
    }

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

    public void appendOrtho(
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
    }

    public GLMat4Builder setPerspective(
            final double fov, final double aspect,
            final double near, final double far) {

        Matrices.perspective4D(stack, current, fov, aspect, near, far);
        return this;
    }

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

    public GLMat4Builder setPerspective(
            final double fov, final double aspect,
            final double near) {

        Matrices.perspective4D(stack, current, fov, aspect, near);
        return this;
    }

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
    
    public GLMat4Builder setMatrix(final GLMat4 other) {        
        other.asGLMat4D().copyToArray(stack, current, 16);
        return this;
    }
    
    public GLMat4Builder prependMatrix(final GLMat4 other) {
        final GLMat4D in1 = other.asGLMat4D();
        final int in2 = this.current;
        final int out = this.current - 16;
        
        Matrices.multiplyVec4D(stack, out, in1.data(), in1.offset(), stack, in2);
        System.arraycopy(stack, out, stack, current, 16);
        return this;
    }

    public GLMat4Builder appendMatrix(final GLMat4 other) {
        final int in1 = this.current;
        final int out = this.current - 16;
        final GLMat4D in2 = other.asGLMat4D();

        Matrices.multiplyMat4D(stack, out, stack, in1, in2.data(), in2.offset());
        System.arraycopy(stack, out, stack, current, 16);
        return this;
    }        

    public GLVec4D multiply(final GLVec<?> vec) {
        final int in1 = this.current;
        final GLVec4D out = GLVec4D.create();
        final GLVec4D in2 = vec.asGLVecD().exGLVec4D();

        Matrices.multiplyVec4D(out.data(), out.offset(), stack, in1, in2.data(), in2.offset());
        return out;
    }

    public GLMat4D multiply(final GLMat<?, ?> mat) {
        final int in1 = this.current;
        final GLMat4D out = GLMat4D.create();
        final GLMat4D in2 = mat.asGLMatD().asGLMat4D();

        Matrices.multiplyMat4D(out.data(), out.offset(), stack, in1, in2.data(), in2.offset());
        return out;
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