/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longlinkislong.gloop;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Robert
 */
public class ThreadedMatrixTests {
    private static int TESTS = 3;
    private static int NUMBER = 1000000;
    
    @Test
    public void SingleThreadedMatrixTest() throws InterruptedException{
        Matrices.DEFAULT_FACTORY = new CyclicalMatrixFactory();
        
        List<GLMat4F> out = new ArrayList<>(NUMBER);
        IntStream.range(0, NUMBER).forEach(i -> out.add(GLMat4F.create().asStaticMat()));
        
        
        for(int h=0;h<TESTS;h++){
            long time = System.nanoTime();
            
            IntStream.range(0, NUMBER).forEach((int i) -> {
                GLMat4F tr = GLMat4F.translation(i, i*2, i*3);
                GLMat4F rot = GLMat4F.rotateZ(30 * i);
                GLMat4F scale = GLMat4F.scale(1 + i, 2 + i, 3 + i);

                out.get(i).set(tr.multiply(rot).multiply(scale).asStaticMat());
            });
            System.out.println("single threaded time " + (System.nanoTime() - time)/1000000.0);
        }
        
        IntStream.range(0, NUMBER).forEach(i -> {
            GLMat4F tr = GLMat4F.translation(i, i*2, i*3);
            GLMat4F rot = GLMat4F.rotateZ(30 * i);
            GLMat4F scale = GLMat4F.scale(1 + i, 2 + i, 3 + i);

            Assert.assertEquals(out.get(i), tr.multiply(rot).multiply(scale).asStaticMat());
        });
        
    }
    
    @Test
    public void ThreadedSafeMatrixTest() throws InterruptedException{
        Matrices.DEFAULT_FACTORY = new ThreadSafeMatrixFactory();
        
        List<GLMat4F> out = new ArrayList<>(NUMBER);
        IntStream.range(0, NUMBER).forEach(i -> out.add(GLMat4F.create().asStaticMat()));
        
        
        for(int h=0;h<TESTS;h++){
            long time = System.nanoTime();
            //ExecutorService pool = Executors.newWorkStealingPool();
            
            IntStream.range(0, NUMBER).parallel().forEach((int i) -> {
                GLMat4F tr = GLMat4F.translation(i, i*2, i*3);
                GLMat4F rot = GLMat4F.rotateZ(30 * i);
                GLMat4F scale = GLMat4F.scale(1 + i, 2 + i, 3 + i);

                out.get(i).set(tr.multiply(rot).multiply(scale).asStaticMat());
            });
            System.out.println("multi threaded-safe time " + (System.nanoTime() - time)/1000000.0);
        }
        
        IntStream.range(0, NUMBER).forEach(i -> {
            GLMat4F tr = GLMat4F.translation(i, i*2, i*3);
            GLMat4F rot = GLMat4F.rotateZ(30 * i);
            GLMat4F scale = GLMat4F.scale(1 + i, 2 + i, 3 + i);

            Assert.assertEquals(out.get(i), tr.multiply(rot).multiply(scale).asStaticMat());
        });
        
    }
    
    @Test
    public void ThreadedStaticMatrixTest() throws InterruptedException{
        Matrices.DEFAULT_FACTORY = StaticMatrixFactory.getInstance();
        Vectors.DEFAULT_FACTORY = StaticVectorFactory.getInstance();
        
        List<GLMat4F> out = new ArrayList<>(NUMBER);
        IntStream.range(0, NUMBER).forEach(i -> out.add(GLMat4F.create().asStaticMat()));
        
        
        for(int h=0;h<TESTS;h++){
            long time = System.nanoTime();
            //ExecutorService pool = Executors.newWorkStealingPool();
            
            IntStream.range(0, NUMBER).parallel().forEach((int i) -> {
                GLMat4F tr = GLMat4F.translation(i, i*2, i*3);
                GLMat4F rot = GLMat4F.rotateZ(30 * i);
                GLMat4F scale = GLMat4F.scale(1 + i, 2 + i, 3 + i);

                out.get(i).set(tr.multiply(rot).multiply(scale).asStaticMat());
            });
            System.out.println("multi threaded-static time " + (System.nanoTime() - time)/1000000.0);
        }
        
        IntStream.range(0, NUMBER).forEach(i -> {
            GLMat4F tr = GLMat4F.translation(i, i*2, i*3);
            GLMat4F rot = GLMat4F.rotateZ(30 * i);
            GLMat4F scale = GLMat4F.scale(1 + i, 2 + i, 3 + i);

            Assert.assertEquals(out.get(i), tr.multiply(rot).multiply(scale).asStaticMat());
        });
        
    }
}
