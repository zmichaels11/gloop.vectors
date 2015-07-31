/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longlinkislong.gloop;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;

/**
 *
 * @author zmichaels
 */
public class Profile {

    private double[] genRand(final int size) {
        final double[] out = new double[size];

        for (int i = 0; i < size; i++) {
            out[i] = Math.random();
        }

        return out;
    }

    @Test
    public void profileMat4D() {
        GLMat4D a = GLMat4D.create().set(genRand(16));
        GLMat4D b = GLMat4D.create().set(genRand(16));

        System.out.println("n = 4");
        System.out.print(benchmark(a, b));
    }

    @Test
    public void profileMat5D() {
        GLMatND a = GLMatND.create(5).set(genRand(25));
        GLMatND b = GLMatND.create(5).set(genRand(25));

        System.out.println("n = 5");
        System.out.print(benchmark(a, b));
    }

    @Test
    public void profileMat10D() {
        GLMatND a = GLMatND.create(10).set(genRand(100));
        GLMatND b = GLMatND.create(10).set(genRand(100));

        System.out.println("n = 10");
        System.out.print(benchmark(a, b));
    }
    
    @Test
    public void profileThreadedMat4D() {        
        System.out.println("n = 4 (threaded)");
        System.out.print(benchmarkThreaded(4));
    }

    @Test
    public void profileThreadedMat5D() {       
        System.out.println("n = 5 (threaded)");
        System.out.print(benchmarkThreaded(5));
    }

    @Test
    public void profileThreadedMat10D() {       
        System.out.println("n = 10 (threaded)");
        System.out.print(benchmarkThreaded(10));
    }

    String benchmarkThreaded(final int size) {
        final int processors = Runtime.getRuntime().availableProcessors();
        final ExecutorService pool = Executors.newWorkStealingPool(processors);
        final AtomicInteger totalCounter = new AtomicInteger();
        final AtomicLong totalOps = new AtomicLong();        
        final Future<?>[] tasks = new Future[processors];

        for (int i = 0; i < processors; i++) {            
            tasks[i] = pool.submit(() -> {
                int counter = 0;
                long ops = 0L;
                final Timer t = new Timer();
                final long sz = size * size * size * 2L;
                GLMatD a = GLMatND.create(size).set(genRand(size * size));
                GLMatD b = GLMatND.create(size).set(genRand(size * size));
                
                t.start();

                while (!t.hasRanFor(5.0)) {
                    a.multiply(b);
                    counter++;
                    ops += sz;
                }
                t.stop();

                totalCounter.addAndGet(counter);
                totalOps.addAndGet(ops);
            });
        }
        
        for(int i = 0; i < processors; i++) {
            try {
                tasks[i].get();
            } catch (InterruptedException | ExecutionException ex) {
                Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return String.format("%6.3f GFLOPS (%d iterations in %.1f seconds)%n",
                totalOps.get() / 5.0 / 1e9,
                totalCounter.get(),
                5.0);
    }

    String benchmark(GLMatD a, GLMatD b) {
        int counter = 0;
        long ops = 0L;
        final Timer t = new Timer();
        final long size = a.size() * a.size() * a.size() * 2L;

        t.start();

        while (!t.hasRanFor(5.0)) {
            a.multiply(b);
            counter++;
            ops += size;
        }
        t.stop();

        return String.format("%6.3f GFLOPS (%d iterations in %.1f seconds)%n",
                ops / t.elapsedSeconds() / 1e9,
                counter,
                t.elapsedSeconds());
    }

    class Timer {

        long startTime;
        long stopTime;

        void start() {
            this.startTime = System.nanoTime();
        }

        long stop() {
            this.stopTime = System.nanoTime();
            return this.stopTime - this.startTime;
        }

        boolean hasRanFor(double seconds) {
            return (System.nanoTime() - this.startTime) / 1e9 >= seconds;
        }

        double elapsedSeconds() {
            return (this.stopTime - this.startTime) / 1e9;
        }
    }
}
