/* 
 * Copyright (c) 2015, Robert Hewitt
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

import java.util.ArrayList;
import java.util.Deque;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.locks.ReentrantLock;

/**
 * An implementation of MatrixFactory that utilizes a separate matrix pool per
 * thread.
 *
 * @author Robert
 * @since 15.07.12
 */
public final class ThreadSafeMatrixFactory implements MatrixFactory {

    private static final boolean DEBUG;

    static {
        DEBUG = Boolean.getBoolean("debug");
    }

    private final ConcurrentHashMap<Long, PooledFactory> map = new ConcurrentHashMap<>();
    private final Deque<PooledFactory> factoryPool = new ConcurrentLinkedDeque<>();
    private final ReentrantLock lock = new ReentrantLock();
    private final int cacheSize;

    /**
     * Constructs a new ThreadSafeMatrixFactory using the default cache size.
     *
     * @throws IllegalArgumentException if default cache size is set to less
     * than 1KB.
     * @since 15.07.12
     */
    public ThreadSafeMatrixFactory() {
        this(Integer.getInteger("gloop.matrices.cache", 16));
    }

    /**
     * Constructs a new ThreadSafeMatrixFactory with the specified cache size.
     *
     * @param cacheSize the inner cache size in kilobytes.
     * @throws IllegalArgumentException if cache size is less than 1KB.
     * @since 15.07.12
     */
    public ThreadSafeMatrixFactory(final int cacheSize) {
        if ((this.cacheSize = cacheSize) < 1) {
            throw new IllegalArgumentException("Invalid cache size! At least 1KB of cache must be allocated for a matrix pool!");
        }
    }

    private MatrixFactory getFactory() {
        while (lock.isLocked()) {
            Thread.yield();
        }

        Thread currentThread = Thread.currentThread();
        long threadID = currentThread.getId();

        PooledFactory factory = map.get(threadID);

        if (factory == null) {
            repoolOldThreads();

            factory = getOrCreatePooledFactory(currentThread);
            map.put(threadID, factory);
        }

        return factory.getFactory();
    }

    private PooledFactory getOrCreatePooledFactory(Thread thread) {
        PooledFactory polledFactory = factoryPool.poll();
        if (polledFactory == null) {
            return new PooledFactory(thread);
        }
        polledFactory.reassignThread(thread);
        return polledFactory;
    }

    private void repoolOldThreads() {
        try {
            lock.lock();

            final Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
            final ArrayList<PooledFactory> keep = new ArrayList<>(map.size());

            for(PooledFactory factory : map.values()) {
                if(threadSet.contains(factory.getThread())) {
                    keep.add(factory);
                } else {
                    factoryPool.add(factory);
                }
            }            

            map.clear();

            for(PooledFactory factory : keep) {
                map.put(factory.getThreadId(), factory);
            }            
        } finally {
            lock.unlock();
        }
    }

    @Override
    public GLMat2F nextGLMat2F() {
        return getFactory().nextGLMat2F();
    }

    @Override
    public GLMat3F nextGLMat3F() {
        return getFactory().nextGLMat3F();
    }

    @Override
    public GLMat4F nextGLMat4F() {
        return getFactory().nextGLMat4F();
    }

    @Override
    public GLMatNF nextGLMatNF(int size) {
        return getFactory().nextGLMatNF(size);
    }

    @Override
    public GLMat2D nextGLMat2D() {
        return getFactory().nextGLMat2D();
    }

    @Override
    public GLMat3D nextGLMat3D() {
        return getFactory().nextGLMat3D();
    }

    @Override
    public GLMat4D nextGLMat4D() {
        return getFactory().nextGLMat4D();
    }

    @Override
    public GLMatND nextGLMatND(int size) {
        return getFactory().nextGLMatND(size);
    }

    private final class PooledFactory {

        private final MatrixFactory factory;
        private Thread thread;

        PooledFactory(Thread thread) {
            if (DEBUG) {
                System.out.println("Create pool on thread " + thread.getId());
            }

            this.factory = new CyclicalMatrixFactory(ThreadSafeMatrixFactory.this.cacheSize);
            this.thread = thread;
        }

        Thread getThread() {
            return thread;
        }

        long getThreadId() {
            return thread.getId();
        }

        void reassignThread(Thread thread) {
            this.thread = thread;
        }

        MatrixFactory getFactory() {
            return factory;
        }
    }
}
