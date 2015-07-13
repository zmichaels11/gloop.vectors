/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longlinkislong.gloop;

import java.util.ArrayList;
import java.util.Deque;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Robert
 */
public class ThreadSafeMatrixFactory implements MatrixFactory{
    private final ConcurrentHashMap<Long, PooledFactory> map = new ConcurrentHashMap<>();
    private final Deque<PooledFactory> factoryPool = new ConcurrentLinkedDeque<>();
    
    private ReentrantLock lock = new ReentrantLock();
    
    private MatrixFactory getFactory(){
        while(lock.isLocked()){
            Thread.yield();
        }
        
        Thread currentThread = Thread.currentThread();
        long threadID = currentThread.getId();
        
        PooledFactory factory = map.get(threadID);
        
        if(factory == null){
            repoolOldThreads();

            factory = getOrCreatePooledFactory(currentThread);
            map.put(threadID, factory);
        }
        
        return factory.getFactory();
    }
    
    private PooledFactory getOrCreatePooledFactory(Thread thread){
        PooledFactory polledFactory = factoryPool.poll();
        if(polledFactory == null){
            return new PooledFactory(thread);
        }
        polledFactory.reassignThread(thread);
        return polledFactory;
    }
    
    private void repoolOldThreads(){
        lock.lock();
        
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();

        ArrayList<PooledFactory> keep = new ArrayList<>(map.size());

        map.values().forEach((PooledFactory pooledFactory) -> {
            if(threadSet.contains(pooledFactory.getThread())){
                keep.add(pooledFactory);
            }else{
                factoryPool.add(pooledFactory);
            }
        });

        map.clear();

        keep.forEach((pooledFactory) -> {
            map.put(pooledFactory.getThreadId(), pooledFactory);
        });
        lock.unlock();
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
    
    private static class PooledFactory{
        private final MatrixFactory factory;
        private Thread thread;

        public PooledFactory(Thread thread) {
            System.out.println("Create pool on thread " + thread.getId());
            factory = new CyclicalMatrixFactory();
            this.thread = thread;
        }

        public Thread getThread() {
            return thread;
        }
        
        public long getThreadId() {
            return thread.getId();
        }

        public void reassignThread(Thread thread) {
            this.thread = thread;
        }

        public MatrixFactory getFactory() {
            return factory;
        }
    }
}
