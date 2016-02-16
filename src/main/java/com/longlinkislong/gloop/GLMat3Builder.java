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
public class GLMat3Builder implements GLMat3 {
    private final double[] stack;
    private final int stackSize;
    private int current;
    
    public GLMat3Builder() {
        this(3);
    }
    
    public GLMat3Builder(final int stackSize) {
        this(stackSize, GLMat3D.create());
    }
    
    public GLMat3Builder(final int stackSize, final GLMat3 base) {
        this.stackSize = stackSize;
        this.stack = new double[9 * stackSize];
        this.current = 9 * stackSize - 9;
        
        base.asGLMat3D().copyToArray(stack, current, 9);
    }
    
    public final int getStackSize() {
        return this.stackSize;
    }
    
    private int testBounds(final int next) {
        if(next > this.stack.length - 9) {
            throw new IndexOutOfBoundsException("Stack overflow");
        } else if(next < 0) {
            throw new IndexOutOfBoundsException("Stack underflow");
        } else {
            return next;
        }        
    }
    
    public final void push() {
        final int next = this.testBounds(this.current - 9);
        
        System.arraycopy(this.stack, this.current, this.stack, next, 9);
    }
    
    public final void pop() {
        final int next = this.testBounds(this.current + 9);
        
        this.current = next;
    }
    
    public GLMat3Builder setZero() {
        return this.setScale(0.0, 0.0, 0.0);
    }
    
    public GLMat3Builder setIdentity() {
        return this.setScale(1.0, 1.0, 1.0);
    }
    
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
        
    public GLMat3Builder setMatrix(final GLMat3 other) {
        other.asGLMat3D().copyToArray(stack, current, 9);
        return this;
    }
    
    public GLMat3Builder prependMatrix(final GLMat3 other) {
        final GLMat3D in1 = other.asGLMat3D();
        final int in2 = this.current;
        final int out = this.current - 9;
        
        Matrices.multiplyMat3D(stack, out, in1.data(), in1.offset(), stack, in2);
        System.arraycopy(stack, out, stack, current, 9);
        return this;
    }
    
    public GLMat3Builder appendMatrix(final GLMat3 other) {
        final int in1 = this.current;
        final GLMat3D in2 = other.asGLMat3D();
        final int out = this.current - 9;
        
        Matrices.multiplyMat3D(stack, out, stack, in1, in2.data(), in2.offset());
        System.arraycopy(stack, out, stack, current, 9);
        return this;
    }
    
    public GLVec3D multiply(final GLVec<?> vec) {
        final int in1 = this.current;
        final GLVec3D out = GLVec3D.create();
        final GLVec3D in2 = vec.asGLVecD().asGLVec3D();
        
        Matrices.multiplyVec3D(out.data(), out.offset(), stack, in1, in2.data(), in2.offset());
        return out;
    }
    
    public GLMat3D multiply(final GLMat<?, ?> mat) {
        final int in1 = this.current;
        final GLMat3D out = GLMat3D.create();
        final GLMat3D in2 = mat.asGLMatD().asGLMat3D();
        
        Matrices.multiplyMat3D(out.data(), out.offset(), stack, in1, in2.data(), in2.offset());
        return out;
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
