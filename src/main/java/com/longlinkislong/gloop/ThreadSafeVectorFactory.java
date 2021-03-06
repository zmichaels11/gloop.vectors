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
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.locks.ReentrantLock;

/**
 * An implementation of VectorFactory that allocates a cyclical pool for each
 * thread.
 *
 * @author zmichaels
 * @since 15.07.13
 */
public final class ThreadSafeVectorFactory implements VectorFactory {

    private static final boolean DEBUG;

    static {
        DEBUG = Boolean.getBoolean("debug");
    }

    private final ConcurrentHashMap<Long, PooledFactory> map = new ConcurrentHashMap<>();
    private final Deque<PooledFactory> factoryPool = new ConcurrentLinkedDeque<>();
    private final ReentrantLock lock = new ReentrantLock();
    private final int cacheSize;

    /**
     * Constructs a new ThreadSafeVectorFactory using the default cache size.
     *
     * @throws IllegalArgumentException if default cache size is set to less
     * than 1KB.
     * @since 15.07.13
     */
    public ThreadSafeVectorFactory() {
        this(Integer.getInteger("gloop.vectors.cache", 16));
    }

    /**
     * Constructs a new ThreadSafeVectorFactory using the specified cache size.
     *
     * @param cacheSize the cache size in kilobytes.
     * @throws IllegalArgumentException if cache size specified is less than
     * 1KB.
     * @since 15.07.13
     */
    public ThreadSafeVectorFactory(final int cacheSize) {
        if ((this.cacheSize = cacheSize) < 1) {
            throw new IllegalArgumentException("Invalid cache size! At least 1KB of cache must be allocated for a matrix pool!");
        }
    }

    private VectorFactory getFactory() {
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
            final List<PooledFactory> keep = new ArrayList<>(map.size());

            for (PooledFactory factory : map.values()) {
                if (threadSet.contains(factory.getThread())) {
                    keep.add(factory);
                } else {
                    factoryPool.add(factory);
                }
            }

            map.clear();

            for (PooledFactory factory : keep) {
                map.put(factory.getThreadId(), factory);
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public GLVec2D nextGLVec2D() {
        return this.getFactory().nextGLVec2D();
    }

    @Override
    public GLVec3D nextGLVec3D() {
        return this.getFactory().nextGLVec3D();
    }

    @Override
    public GLVec4D nextGLVec4D() {
        return this.getFactory().nextGLVec4D();
    }

    @Override
    public GLVecND nextGLVecND(int size) {
        return this.getFactory().nextGLVecND(size);
    }

    @Override
    public GLVec2F nextGLVec2F() {
        return this.getFactory().nextGLVec2F();
    }

    @Override
    public GLVec3F nextGLVec3F() {
        return this.getFactory().nextGLVec3F();
    }

    @Override
    public GLVec4F nextGLVec4F() {
        return this.getFactory().nextGLVec4F();
    }

    @Override
    public GLVecNF nextGLVecNF(int size) {
        return this.getFactory().nextGLVecNF(size);
    }

    private final class PooledFactory {

        private final VectorFactory factory;
        private Thread thread;

        PooledFactory(Thread thread) {
            if (DEBUG) {
                System.out.println("Create pool on thread " + thread.getId());
            }

            this.factory = new CyclicalVectorFactory(ThreadSafeVectorFactory.this.cacheSize);
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

        VectorFactory getFactory() {
            return factory;
        }
    }
}
