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

import java.io.Closeable;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * An implementation of VectorFactory, MatrixFactory, and QuaternionFactory that
 * will pick the default factory based on the current configuration.
 *
 * @author zmichaels
 * @since 16.01.28
 */
public final class SmartFactory implements
        Closeable,
        VectorFactory,
        MatrixFactory,
        QuaternionFactory {

    private final Map<Thread, MatrixFactory> currentMatFactory
            = Collections.synchronizedMap(new WeakHashMap<>());
    private final Map<Thread, VectorFactory> currentVecFactory
            = Collections.synchronizedMap(new WeakHashMap<>());
    private final Map<Thread, QuaternionFactory> currentQuatFactory
            = Collections.synchronizedMap(new WeakHashMap<>());
    private final Map<Thread, MatrixFactory> fastMatFactory
            = Collections.synchronizedMap(new WeakHashMap<>());
    private final Map<Thread, VectorFactory> fastVecFactory
            = Collections.synchronizedMap(new WeakHashMap<>());
    private final Map<Thread, QuaternionFactory> fastQuatFactory
            = Collections.synchronizedMap(new WeakHashMap<>());

    /**
     * Reverts the default factories for the current thread.
     *
     * @since 16.01.28
     */
    @Override
    public void close() {
        final Thread thread = Thread.currentThread();

        this.currentMatFactory.remove(thread);
        this.currentVecFactory.remove(thread);
        this.currentQuatFactory.remove(thread);
    }

    /**
     * Retrieves the current matrix factory for the current thread.
     *
     * @return the current matrix factory.
     * @since 16.01.28
     */
    public MatrixFactory getCurrentMatrixFactory() {
        return this.currentMatFactory.getOrDefault(
                Thread.currentThread(),
                StaticMatrixFactory.getInstance());
    }

    /**
     * Retrieves the current vector factory for the current thread.
     *
     * @return the current vector factory.
     * @since 16.01.28
     */
    public VectorFactory getCurrentVectorFactory() {
        return this.currentVecFactory.getOrDefault(
                Thread.currentThread(),
                StaticVectorFactory.getInstance());
    }

    /**
     * Retrieves the current quaternion factory for the current thread.
     *
     * @return the current quaternion factory.
     * @since 16.01.28
     */
    public QuaternionFactory getCurrentQuaternionFactory() {
        return this.currentQuatFactory.getOrDefault(
                Thread.currentThread(),
                StaticQuaternionFactory.getInstance());
    }

    /**
     * Enables pooled matrices on the current thread. This will allocate a new
     * CyclicalMatrixFactory for the current thread if one does not already
     * exist.
     *
     * @return self reference for use within a try-with-resources block.
     * @since 16.01.28
     */
    public SmartFactory withFastMatrices() {
        if (this.getCurrentMatrixFactory() instanceof CyclicalMatrixFactory) {
            return this;
        } else {
            final Thread currentThread = Thread.currentThread();

            this.currentMatFactory.put(
                    currentThread,
                    this.fastMatFactory.computeIfAbsent(
                            currentThread,
                            thread -> new CyclicalMatrixFactory())
            );

            return this;
        }
    }

    /**
     * Switches the current thread to using the StaticVectorFactory.
     *
     * @return self reference for use within a try-with-resources block.
     * @since 16.01.28
     */
    public SmartFactory withStaticVectors() {
        this.currentVecFactory.remove(Thread.currentThread());
        return this;
    }

    /**
     * Switches the current thread to using the StaticMatrixFactory.
     *
     * @return self reference for use within a try-with-resources block.
     * @since 16.01.28
     */
    public SmartFactory withStaticMatrices() {
        this.currentMatFactory.remove(Thread.currentThread());
        return this;
    }

    /**
     * Switches the current thread to using the StaticQuaternionFactory.
     *
     * @return self reference for use within a try-with-resources block.
     * @since 16.01.28
     */
    public SmartFactory withStaticQuaternions() {
        this.currentQuatFactory.remove(Thread.currentThread());
        return this;
    }

    /**
     * Enables pooled vectors on the current thread. This will allocate a new
     * CyclicalVectorFactory for the current thread if one does not already
     * exist.
     *
     * @return self reference for use within a try-with-resources block.
     * @since 16.01.28
     */
    public SmartFactory withFastVectors() {
        if (this.getCurrentVectorFactory() instanceof CyclicalVectorFactory) {
            return this;
        } else {
            final Thread currentThread = Thread.currentThread();

            this.currentVecFactory.put(
                    currentThread,
                    this.fastVecFactory.computeIfAbsent(
                            currentThread,
                            thread -> new CyclicalVectorFactory()));

            return this;
        }
    }

    /**
     * Enables pooled quaternions on the current thread. This will allocate a
     * new CyclicalQuaternionFactory for the current thread if one does not
     * already exist.
     *
     * @return self reference for use within a try-with-resources block.
     * @since 16.01.18
     */
    public SmartFactory withFastQuaternions() {
        if (this.getCurrentQuaternionFactory() instanceof CyclicalQuaternionFactory) {
            return this;
        } else {
            final Thread currentThread = Thread.currentThread();

            this.currentQuatFactory.put(
                    currentThread,
                    this.fastQuatFactory.computeIfAbsent(
                            currentThread,
                            thread -> new CyclicalQuaternionFactory()));

            return this;
        }
    }

    @Override
    public GLVec2D nextGLVec2D() {
        return this.getCurrentVectorFactory().nextGLVec2D();
    }

    @Override
    public GLVec3D nextGLVec3D() {
        return this.getCurrentVectorFactory().nextGLVec3D();
    }

    @Override
    public GLVec4D nextGLVec4D() {
        return this.getCurrentVectorFactory().nextGLVec4D();
    }

    @Override
    public GLVecND nextGLVecND(int size) {
        return this.getCurrentVectorFactory().nextGLVecND(size);
    }

    @Override
    public GLVec2F nextGLVec2F() {
        return this.getCurrentVectorFactory().nextGLVec2F();
    }

    @Override
    public GLVec3F nextGLVec3F() {
        return this.getCurrentVectorFactory().nextGLVec3F();
    }

    @Override
    public GLVec4F nextGLVec4F() {
        return this.getCurrentVectorFactory().nextGLVec4F();
    }

    @Override
    public GLVecNF nextGLVecNF(int size) {
        return this.getCurrentVectorFactory().nextGLVecNF(size);
    }

    @Override
    public GLMat2F nextGLMat2F() {
        return this.getCurrentMatrixFactory().nextGLMat2F();
    }

    @Override
    public GLMat3F nextGLMat3F() {
        return this.getCurrentMatrixFactory().nextGLMat3F();
    }

    @Override
    public GLMat4F nextGLMat4F() {
        return this.getCurrentMatrixFactory().nextGLMat4F();
    }

    @Override
    public GLMatNF nextGLMatNF(int size) {
        return this.getCurrentMatrixFactory().nextGLMatNF(size);
    }

    @Override
    public GLMat2D nextGLMat2D() {
        return this.getCurrentMatrixFactory().nextGLMat2D();
    }

    @Override
    public GLMat3D nextGLMat3D() {
        return this.getCurrentMatrixFactory().nextGLMat3D();
    }

    @Override
    public GLMat4D nextGLMat4D() {
        return this.getCurrentMatrixFactory().nextGLMat4D();
    }

    @Override
    public GLMatND nextGLMatND(int size) {
        return this.getCurrentMatrixFactory().nextGLMatND(size);
    }

    @Override
    public GLQuaternionF nextGLQuaternionF() {
        return this.getCurrentQuaternionFactory().nextGLQuaternionF();
    }

    @Override
    public GLQuaternionD nextGLQuaternionD() {
        return this.getCurrentQuaternionFactory().nextGLQuaternionD();
    }

    private static final class Holder {

        private static final SmartFactory INSTANCE = new SmartFactory();
    }

    public static SmartFactory getInstance() {
        return Holder.INSTANCE;
    }
}
