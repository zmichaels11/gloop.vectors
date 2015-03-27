/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longlinkislong.gloop;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author zmichaels
 */
public class VectorCompare {

    private final Random rand = new Random();
    private final int VEC_SIZE = 4;
    private final int VEC_COUNT = 1000000;
    private final int CACHE_SIZE = VEC_COUNT * VEC_SIZE;
    private final int TEST_COUNT = 10;

    private void generate(float[] in0, float[] in1, int len) {
        for (int i = 0; i < len; i++) {
            in0[i] = rand.nextFloat();
            in1[i] = rand.nextFloat();
        }
    }

    private void generate(FloatBuffer in0, FloatBuffer in1, int len) {
        for (int i = 0; i < len; i++) {
            in0.put(i, rand.nextFloat());
            in1.put(i, rand.nextFloat());
        }
    }

    @Test
    public void testJavaAdd() {
        final float[] out = new float[CACHE_SIZE];
        final float[] in0 = new float[CACHE_SIZE];
        final float[] in1 = new float[CACHE_SIZE];

        double vpsTotal = 0;
        for (int testID = 0; testID < TEST_COUNT; testID++) {
            generate(in0, in1, CACHE_SIZE);
            long start = System.currentTimeMillis();

            for (int i = 0; i < VEC_COUNT; i++) {
                final int offset = i * VEC_SIZE;

                Vectors.plus4F(out, offset, in0, offset, in1, offset);
            }

            final long elapsed = System.currentTimeMillis() - start;
            final double seconds = elapsed / 1000.0;
            final double vps = VEC_COUNT / seconds * 1e-6;

            for (int i = 0; i < VEC_COUNT; i++) {
                final int offset = i * VEC_SIZE;

                for (int j = 0; j < VEC_SIZE; j++) {
                    final float sum = in0[offset + j] + in1[offset + j];

                    Assert.assertEquals(sum, out[offset + j], GLVecF.EPSILON);
                }
            }

            vpsTotal += vps;
        }
        System.out.printf("java add: %.2fx10^6 vecs per second\n", (vpsTotal / TEST_COUNT));
    }

    @Test
    public void testBufferAdd() {
        FloatBuffer out = ByteBuffer.allocate(CACHE_SIZE * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        FloatBuffer in0 = ByteBuffer.allocate(CACHE_SIZE * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        FloatBuffer in1 = ByteBuffer.allocate(CACHE_SIZE * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();

        double vpsTotal = 0;
        for (int testID = 0; testID < TEST_COUNT; testID++) {
            this.generate(in0, in1, CACHE_SIZE);
            final long start = System.currentTimeMillis();

            for (int i = 0; i < VEC_COUNT; i++) {
                final int offset = i * VEC_SIZE;

                Vectors.plus4F(out, offset, in0, offset, in1, offset);
            }

            final long elapsed = System.currentTimeMillis() - start;
            final double seconds = elapsed / 1000.0;
            final double vps = VEC_COUNT / seconds * 1e-6;

            for (int i = 0; i < VEC_COUNT; i++) {
                final int offset = i * VEC_SIZE;

                for (int j = 0; j < VEC_SIZE; j++) {
                    final float sum = in0.get(offset + j) + in1.get(offset + j);

                    Assert.assertEquals(sum, out.get(offset + j), GLVecF.EPSILON);
                }
            }
            vpsTotal += vps;
        }

        System.out.printf("buffer add: %.2fx10^6 vecs per second\n", (vpsTotal / TEST_COUNT));
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
