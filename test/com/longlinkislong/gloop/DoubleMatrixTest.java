/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longlinkislong.gloop;

import java.util.Arrays;
import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author zmichaels
 */
public class DoubleMatrixTest {

    private static final double EPSILON = 1e-7f;
    private static final int TEST_COUNT = 1000000;
    private final Random random = new Random();

    @Test
    public void testStaticMat2x2() {
        for(int i = 0; i < TEST_COUNT; i++) {
            final float x = random.nextFloat();
            final GLMatD m0 = GLMat2D.translation(x);
            final GLMatD m1 = m0.asStaticMat();
            
            Assert.assertNotSame(m0, m1);
            Assert.assertEquals(m0, m1);
        }
    }
    
    @Test
    public void testStaticMat3x3() {
        for(int i = 0; i < TEST_COUNT; i++) {
            final float x = random.nextFloat();
            final float y = random.nextFloat();
            final float z = random.nextFloat();
            
            final GLMatD m0 = GLMat3D.translation(x, y);
            final GLMatD m1 = m0.asStaticMat();
            
            Assert.assertNotSame(m0, m1);
            Assert.assertEquals(m0, m1);
        }
    }
    
    @Test
    public void testStaticMat4x4() {
        for(int i = 0; i < TEST_COUNT; i++) {
            final float x = random.nextFloat();
            final float y = random.nextFloat();
            final float z = random.nextFloat();
            
            final GLMatD m0 = GLMat4D.translation(x, y, z);
            final GLMatD m1 = m0.asStaticMat();
            
            Assert.assertNotSame(m0, m1);
            Assert.assertEquals(m0, m1);
        }
    }
    
    @Test
    public void testMultiplyVec2x2() {
        for (int i = 0; i < TEST_COUNT; i++) {
            final float x0 = random.nextFloat();
            final float x1 = random.nextFloat();

            final GLMatD m0 = GLMat2D.translation(x0);
            final GLVecD v0 = GLVec2D.create(x1, 1f);
            final GLVecD ac = m0.multiply(v0);
            final GLVecD ex = GLVec2D.create(x0 + x1, 1f);

            Assert.assertNotSame(ex, ac);
            Assert.assertEquals(ex, ac);
        }
    }

    @Test
    public void testMultiplyVec3x3() {
        for (int i = 0; i < TEST_COUNT; i++) {
            final float x0 = random.nextFloat();
            final float y0 = random.nextFloat();
            final float x1 = random.nextFloat();
            final float y1 = random.nextFloat();

            final GLMatD m0 = GLMat3D.translation(x0, y0);
            final GLVecD v0 = GLVec3D.create(x1, y1, 1f);
            final GLVecD ac = m0.multiply(v0);
            final GLVecD ex = GLVec3D.create(x0 + x1, y0 + y1, 1f);

            Assert.assertNotSame(ex, ac);
            Assert.assertEquals(ex, ac);
        }
    }

    @Test
    public void testMultiplyVec4x4() {
        for (int i = 0; i < TEST_COUNT; i++) {
            final float x0 = random.nextFloat();
            final float y0 = random.nextFloat();
            final float z0 = random.nextFloat();
            final float x1 = random.nextFloat();
            final float y1 = random.nextFloat();
            final float z1 = random.nextFloat();

            final GLMatD m0 = GLMat4D.translation(x0, y0, z0);
            final GLVecD v0 = GLVec4D.create(x1, y1, z1, 1f);
            final GLVecD ac = m0.multiply(v0);
            final GLVecD ex = GLVec4D.create(x0 + x1, y0 + y1, z0 + z1, 1f);

            Assert.assertNotSame(ex, ac);
            Assert.assertEquals(ex, ac);
        }
    }
    
    @Test
    public void testTranspose2x2() {
        for(int i = 0; i < TEST_COUNT; i++) {
            final GLMatD m0 = GLMat2D.translation(random.nextDouble());
            final GLMatD m1 = m0.transpose();
            final GLMatD ac = m1.transpose();
            final GLMatD ex = m0;
            
            Assert.assertNotSame(ex, ac);
            Assert.assertEquals(ex, ac);
        }
    }
    
    @Test
    public void testTranspose3x3() {
        for(int i = 0; i < TEST_COUNT; i++) {
            final GLMatD m0 = GLMat3D.translation(random.nextDouble(), random.nextDouble());
            final GLMatD m1 = m0.transpose();
            final GLMatD ac = m1.transpose();
            final GLMatD ex = m0;
            
            Assert.assertNotSame(ex, ac);
            Assert.assertEquals(ex, ac);
        }
    }
    
    @Test
    public void testTranspose4x4() {
        for(int i = 0; i < TEST_COUNT; i++) {
            final GLMatD m0 = GLMat4D.translation(random.nextDouble(), random.nextDouble(), random.nextDouble());
            final GLMatD m1 = m0.transpose();
            final GLMatD ac = m1.transpose();
            final GLMatD ex = m0;
            
            Assert.assertNotSame(ex, ac);
            Assert.assertEquals(ex, ac);
        }
    }
    
    @Test
    public void testInverseMat2x2() {
        for (int i = 0; i < TEST_COUNT; i++) {
            final GLMatD m0 = GLMat2D.translation(random.nextDouble());
            final GLMatD m1 = m0.inverse();
            final GLMatD ac = m0.multiply(m1);
            final GLMatD ex = GLMat2D.create();

            Assert.assertNotSame(ex, ac);
            Assert.assertEquals(ex, ac);
        }
    }

    //@Test
    public void testInverseMat3x3() {
        for (int i = 0; i < TEST_COUNT; i++) {
            final GLMatD m0 = GLMat3D.translation(random.nextDouble(), random.nextDouble());
            final GLMatD m1 = m0.inverse();
            final GLMatD ac = m0.multiply(m1);
            final GLMatD ex = GLMat3D.create();

            Assert.assertNotSame(ex, ac);
            Assert.assertEquals(ex, ac);
        }
    }

    @Test
    public void testInverseMat4x4() {
        for (int i = 0; i < TEST_COUNT; i++) {
            final GLMatD m0 = GLMat4D.translation(random.nextDouble(), random.nextDouble(), random.nextDouble());
            final GLMatD m1 = m0.inverse();
            final GLMatD ac = m0.multiply(m1);
            final GLMatD ex = GLMat4D.create();

            Assert.assertNotSame(ex, ac);
            Assert.assertEquals(ex, ac);
        }
    }

    @Test
    public void testMultiplyMat2x2() {
        for (int i = 0; i < TEST_COUNT; i++) {
            final double x0 = random.nextDouble();
            final double x1 = random.nextDouble();

            final GLMatD m0 = GLMat2D.translation(x0);
            final GLMatD m1 = GLMat2D.translation(x1);
            final GLMatD ex = GLMat2D.translation(x0 + x1);
            final GLMatD ac0 = m0.multiply(m1);
            final GLMatD ac1 = m1.multiply(m0);

            Assert.assertNotSame(ex, ac0);
            Assert.assertNotSame(ex, ac1);
            Assert.assertNotSame(ac0, ac1);
            Assert.assertEquals(ex, ac0);
            Assert.assertEquals(ex, ac1);
        }
    }

    @Test
    public void testMultiplyMat3x3() {
        for (int i = 0; i < TEST_COUNT; i++) {
            final double x0 = random.nextDouble();
            final double x1 = random.nextDouble();
            final double y0 = random.nextDouble();
            final double y1 = random.nextDouble();

            final GLMatD m0 = GLMat3D.translation(x0, y0);
            final GLMatD m1 = GLMat3D.translation(x1, y1);
            final GLMatD ex = GLMat3D.translation(x0 + x1, y0 + y1);
            final GLMatD ac0 = m0.multiply(m1);
            final GLMatD ac1 = m1.multiply(m0);

            Assert.assertNotSame(ex, ac0);
            Assert.assertNotSame(ex, ac1);
            Assert.assertNotSame(ac0, ac1);
            Assert.assertEquals(ex, ac0);
            Assert.assertEquals(ac0, ac1);
        }
    }

    @Test
    public void testMultiplyMat4x4() {
        for (int i = 0; i < TEST_COUNT; i++) {
            final double x0 = random.nextDouble();
            final double x1 = random.nextDouble();
            final double y0 = random.nextDouble();
            final double y1 = random.nextDouble();
            final double z0 = random.nextDouble();
            final double z1 = random.nextDouble();

            final GLMatD m0 = GLMat4D.translation(x0, y0, z0);
            final GLMatD m1 = GLMat4D.translation(x1, y1, z1);
            final GLMatD ex = GLMat4D.translation(x0 + x1, y0 + y1, z0 + z1);
            final GLMatD ac0 = m0.multiply(m1);
            final GLMatD ac1 = m1.multiply(m0);

            Assert.assertNotSame(ex, ac0);
            Assert.assertNotSame(ex, ac1);
            Assert.assertNotSame(ac0, ac1);
            Assert.assertEquals(ex, ac0);
            Assert.assertEquals(ex, ac1);
        }
    }

    @Test
    public void testAllocateMat2x2() {
        for (int i = 0; i < TEST_COUNT; i++) {
            final GLMatD m0 = GLMat2D.create();
            final GLMatD m1 = GLMat2D.create();

            Assert.assertNotSame(m0.offset(), m1.offset());
            Assert.assertEquals(m0, m1);
        }
    }

    @Test
    public void testAllocateMat3x3() {
        for (int i = 0; i < TEST_COUNT; i++) {
            final GLMatD m0 = GLMat2D.create();
            final GLMatD m1 = GLMat2D.create();

            Assert.assertNotSame(m0.offset(), m1.offset());
            Assert.assertEquals(m0, m1);
        }

    }

    @Test
    public void testAllocateMat4x4() {
        for (int i = 0; i < TEST_COUNT; i++) {
            final GLMatD m0 = GLMat2D.create();
            final GLMatD m1 = GLMat2D.create();

            Assert.assertNotSame(m0.offset(), m1.offset());
            Assert.assertEquals(m0, m1);
        }
    }

    @Test
    public void testTranslationMat2x2() {
        for (int i = 0; i < TEST_COUNT; i++) {
            final double x = random.nextDouble();
            final GLMatD m0 = GLMat2D.translation(x);
            final double[] exp = {
                1f, 0f,
                x, 1f};
            final double[] act = Arrays.copyOfRange(m0.data(), m0.offset(), m0.offset() + 4);

            Assert.assertArrayEquals(exp, act, EPSILON);
        }
    }

    @Test
    public void testTranslationMat3x3() {
        for (int i = 0; i < TEST_COUNT; i++) {
            final double x = random.nextDouble();
            final double y = random.nextDouble();
            final GLMatD m0 = GLMat3D.translation(x, y);
            final double[] exp = {
                1f, 0f, 0,
                0f, 1f, 0,
                x, y, 1f
            };
            final double[] act = Arrays.copyOfRange(m0.data(), m0.offset(), m0.offset() + 9);

            Assert.assertArrayEquals(exp, act, EPSILON);
        }
    }

    @Test
    public void testTranslationMat4x4() {
        for (int i = 0; i < TEST_COUNT; i++) {
            final double x = random.nextDouble();
            final double y = random.nextDouble();
            final double z = random.nextDouble();
            final GLMatD m0 = GLMat4D.translation(x, y, z);
            final double[] exp = {
                1f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f,
                0f, 0f, 1f, 0f,
                x, y, z, 1f
            };
            final double[] act = Arrays.copyOfRange(m0.data(), m0.offset(), m0.offset() + 16);

            Assert.assertArrayEquals(exp, act, EPSILON);
        }
    }
}
