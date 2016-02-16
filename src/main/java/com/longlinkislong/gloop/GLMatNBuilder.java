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
public class GLMatNBuilder implements GLMatN {
    private final double[] stack;
    private final int stackSize;
    private final int size;
    private final int size2;
    private int current;    
    
    public GLMatNBuilder(final int size) {
        this(size, 3);
    }
    
    public GLMatNBuilder(final int size, final int stackSize) {
        this(size, stackSize, GLMatND.create(size));
    }
    
    public GLMatNBuilder(final int size, final int stackSize, final GLMatN base) {                
        this.size = size;
        this.size2 = size * size;
        this.stackSize = stackSize;
        this.stack = new double[size2 * stackSize];
        this.current = size2 * stackSize - size2;
        
        base.asGLMatND().copyToArray(stack, current, size2);        
    }
    
    public final int getStackSize() {
        return this.stackSize;
    }
    
    public final int getSize() {
        return this.size;
    }
    
    private int testBounds(final int next) {
        if(next > this.stack.length - this.size) {
            throw new IndexOutOfBoundsException("Stack overflow");
        } else if(next < 0) {
            throw new IndexOutOfBoundsException("Stack underflow");
        } else {
            return next;
        }
    }
    
    public final void push() {
        final int next = this.testBounds(this.current - this.size);
        
        System.arraycopy(this.stack, this.current, this.stack, next, this.size);
    }
    
    public final void pop() {
        final int next = this.testBounds(this.current + this.size);
        
        this.current = next;
    }
    
    public GLMatNBuilder setZero() {
        for(int i = 0; i < this.size2; i++) {
            stack[current + i] = 0.0;
        }
        
        return this;
    }
    
    public GLMatNBuilder setIdentity() {
        for(int i = 0; i < this.size2; i++) {
            stack[current + i] = i % this.size == 0 ? 1.0 : 0.0;
        }
        
        return this;
    }
    
    public GLMatNBuilder setTranslation(final double... values) {
        for(int i = 0; i < this.size2 - this.size; i++) {
            stack[current + i] = i % this.size == 0 ? 1.0 : 0.0;
        }
        
        System.arraycopy(values, 0, stack, current + this.size2 - this.size, this.size);
        
        return this;
    }
    
    public GLMatNBuilder prependTranslation(final double... values) {
        final int in1 = this.current - this.size2;
        final int in2 = this.current;
        final int out = this.current - this.size2 - this.size2;
        
        for(int i = 0; i < this.size2 - this.size; i++) {
            stack[in1 + i] = i % this.size == 0 ? 1.0 : 0.0;
        }
        
        System.arraycopy(values, 0, stack, in1 + this.size2 - this.size, this.size);        
        Matrices.multiplyMatND(stack, out, stack, in1, stack, in2, this.size);
        System.arraycopy(stack, out, stack, current, this.size2);
        
        return this;
    }
    
    public GLMatNBuilder appendTranslation(final double... values) {
        final int in1 = this.current;
        final int in2 = this.current - this.size2;
        final int out = this.current - this.size2 - this.size2;
        
        for(int i = 0; i < this.size2 - this.size; i++) {
            stack[in2 + i] = i % this.size == 0 ? 1.0 : 0.0;
        }
        
        System.arraycopy(values, 0, stack, in2 + this.size2 - this.size, this.size);
        Matrices.multiplyMatND(stack, out, stack, in1, stack, in2, this.size);
        System.arraycopy(stack, out, stack, current, this.size2);
        
        return this;
    }
    
    public GLMatNBuilder setScale(final double... values) {
        int j = 0;
        
        for(int i = 0; i < this.size2; i++) {
            stack[current + i] = i % this.size == 0 ? values[j++] : 0.0;
        }
        
        return this;
    }
    
    public GLMatNBuilder prependScale(final double... values) {
        final int in1 = this.current - this.size2;
        final int in2 = this.current;
        final int out = this.current - this.size2 - this.size2;
        
        int j = 0;
        
        for(int i = 0; i < this.size2; i++) {
            stack[in1 + i] = i % this.size == 0 ? values[j++] : 0.0;
        }
        
        Matrices.multiplyMatND(stack, out, stack, in1, stack, in2, this.size);
        System.arraycopy(stack, out, stack, current, this.size2);
        
        return this;
    }
    
    public GLMatNBuilder appendScale(final double... values) {
        final int in1 = this.current;
        final int in2 = this.current - this.size2;
        final int out = this.current - this.size2 - this.size2;
        
        int j = 0;
        
        for(int i = 0; i < this.size2; i++) {
            stack[in2 + i] = i % this.size == 0 ? values[j++] : 0.0;
        }
        
        Matrices.multiplyMatND(stack, out, stack, in1, stack, in2, this.size);
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
