/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longlinkislong.gloop;

import com.runouw.util.FastRandom;
import java.util.Arrays;
import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author zmichaels
 */
public class FloatMatrixTest {

    private static final float EPSILON = 1e-7f;
    private static final int TEST_COUNT = 1000000;
    private final Random random = new FastRandom();
    
    @Test
    public void testStaticMat2x2() {
        for(int i = 0; i < TEST_COUNT; i++) {
            final float x = random.nextFloat();
            final GLMatF m0 = GLMat2F.translation(x);
            final GLMatF m1 = m0.asStaticMat();
            
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
            
            final GLMatF m0 = GLMat3F.translation(x, y);
            final GLMatF m1 = m0.asStaticMat();
            
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
            final float w = random.nextFloat();
            
            final GLMatF m0 = GLMat4F.translation(x, y, z, w);
            final GLMatF m1 = m0.asStaticMat();
            
            Assert.assertNotSame(m0, m1);
            Assert.assertEquals(m0, m1);
        }
    }
    
    @Test
    public void testMultiplyVec2x2() {
        for (int i = 0; i < TEST_COUNT; i++) {
            final float x0 = random.nextFloat();
            final float x1 = random.nextFloat();

            final GLMatF m0 = GLMat2F.translation(x0);
            final GLVecF v0 = GLVec2F.create(x1, 1f);
            final GLVecF ac = m0.multiply(v0);
            final GLVecF ex = GLVec2F.create(x0 + x1, 1f);

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

            final GLMatF m0 = GLMat3F.translation(x0, y0);
            final GLVecF v0 = GLVec3F.create(x1, y1, 1f);
            final GLVecF ac = m0.multiply(v0);
            final GLVecF ex = GLVec3F.create(x0 + x1, y0 + y1, 1f);

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

            final GLMatF m0 = GLMat4F.translation(x0, y0, z0);
            final GLVecF v0 = GLVec4F.create(x1, y1, z1, 1f);
            final GLVecF ac = m0.multiply(v0);
            final GLVecF ex = GLVec4F.create(x0 + x1, y0 + y1, z0 + z1, 1f);

            Assert.assertNotSame(ex, ac);
            Assert.assertEquals(ex, ac);
        }
    }

    @Test
    public void testTranspose2x2() {
        for (int i = 0; i < TEST_COUNT; i++) {
            final GLMatF m0 = GLMat2F.translation(random.nextFloat());
            final GLMatF m1 = m0.transpose();
            final GLMatF ac = m1.transpose();
            final GLMatF ex = m0;

            Assert.assertNotSame(ex, ac);
            Assert.assertEquals(ex, ac);
        }
    }

    @Test
    public void testTranspose3x3() {
        for (int i = 0; i < TEST_COUNT; i++) {
            final GLMatF m0 = GLMat3F.translation(random.nextFloat(), random.nextFloat());
            final GLMatF m1 = m0.transpose();
            final GLMatF ac = m1.transpose();
            final GLMatF ex = m0;

            Assert.assertNotSame(ex, ac);
            Assert.assertEquals(ex, ac);
        }
    }

    @Test
    public void testTranspose4x4() {
        for (int i = 0; i < TEST_COUNT; i++) {
            final GLMatF m0 = GLMat4F.translation(random.nextFloat(), random.nextFloat(), random.nextFloat());
            final GLMatF m1 = m0.transpose();
            final GLMatF ac = m1.transpose();
            final GLMatF ex = m0;

            Assert.assertNotSame(ex, ac);
            Assert.assertEquals(ex, ac);
        }
    }

    @Test
    public void testInverseMat2x2() {
        for (int i = 0; i < TEST_COUNT; i++) {
            final GLMatF m0 = GLMat2F.translation(random.nextFloat());
            final GLMatF m1 = m0.inverse();
            final GLMatF ac = m0.multiply(m1);
            final GLMatF ex = GLMat2F.create();

            Assert.assertNotSame(ex, ac);
            Assert.assertEquals(ex, ac);
        }
    }

    //@Test
    public void testInverseMat3x3() {
        for (int i = 0; i < TEST_COUNT; i++) {
            final GLMatF m0 = GLMat3F.translation(random.nextFloat(), random.nextFloat());
            final GLMatF m1 = m0.inverse();
            final GLMatF ac = m0.multiply(m1);
            final GLMatF ex = GLMat3F.create();

            Assert.assertNotSame(ex, ac);
            Assert.assertEquals(ex, ac);
        }
    }

    @Test
    public void testInverseMat4x4() {
        for (int i = 0; i < TEST_COUNT; i++) {
            final GLMatF m0 = GLMat4F.translation(random.nextFloat(), random.nextFloat(), random.nextFloat());
            final GLMatF m1 = m0.inverse();
            final GLMatF ac = m0.multiply(m1);
            final GLMatF ex = GLMat4F.create();

            Assert.assertNotSame(ex, ac);
            Assert.assertEquals(ex, ac);
        }
    }

    @Test
    public void testMultiplyMat2x2() {
        for (int i = 0; i < TEST_COUNT; i++) {
            final float x0 = random.nextFloat();
            final float x1 = random.nextFloat();

            final GLMatF m0 = GLMat2F.translation(x0);
            final GLMatF m1 = GLMat2F.translation(x1);
            final GLMatF ex = GLMat2F.translation(x0 + x1);
            final GLMatF ac0 = m0.multiply(m1);
            final GLMatF ac1 = m1.multiply(m0);

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
            final float x0 = random.nextFloat();
            final float x1 = random.nextFloat();
            final float y0 = random.nextFloat();
            final float y1 = random.nextFloat();

            final GLMatF m0 = GLMat3F.translation(x0, y0);
            final GLMatF m1 = GLMat3F.translation(x1, y1);
            final GLMatF ex = GLMat3F.translation(x0 + x1, y0 + y1);
            final GLMatF ac0 = m0.multiply(m1);
            final GLMatF ac1 = m1.multiply(m0);

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
            final float x0 = random.nextFloat();
            final float x1 = random.nextFloat();
            final float y0 = random.nextFloat();
            final float y1 = random.nextFloat();
            final float z0 = random.nextFloat();
            final float z1 = random.nextFloat();

            final GLMatF m0 = GLMat4F.translation(x0, y0, z0);
            final GLMatF m1 = GLMat4F.translation(x1, y1, z1);
            final GLMatF ex = GLMat4F.translation(x0 + x1, y0 + y1, z0 + z1);
            final GLMatF ac0 = m0.multiply(m1);
            final GLMatF ac1 = m1.multiply(m0);

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
            final GLMatF m0 = GLMat2F.create();
            final GLMatF m1 = GLMat2F.create();

            Assert.assertNotSame(m0.offset(), m1.offset());
            Assert.assertEquals(m0, m1);
        }
    }

    @Test
    public void testAllocateMat3x3() {
        for (int i = 0; i < TEST_COUNT; i++) {
            final GLMatF m0 = GLMat3F.create();
            final GLMatF m1 = GLMat3F.create();

            Assert.assertNotSame(m0.offset(), m1.offset());
            Assert.assertEquals(m0, m1);
        }

    }

    @Test
    public void testAllocateMat4x4() {
        for (int i = 0; i < TEST_COUNT; i++) {
            final GLMatF m0 = GLMat4F.create();
            final GLMatF m1 = GLMat4F.create();

            Assert.assertNotSame(m0.offset(), m1.offset());
            Assert.assertEquals(m0, m1);
        }
    }

    @Test
    public void testTranslationMat2x2() {
        for (int i = 0; i < TEST_COUNT; i++) {
            final float x = random.nextFloat();
            final GLMatF m0 = GLMat2F.translation(x);
            final float[] exp = {
                1f, 0f,
                x, 1f};
            final float[] act = Arrays.copyOfRange(m0.data(), m0.offset(), m0.offset() + 4);

            Assert.assertArrayEquals(exp, act, EPSILON);
        }
    }

    @Test
    public void testTranslationMat3x3() {
        for (int i = 0; i < TEST_COUNT; i++) {
            final float x = random.nextFloat();
            final float y = random.nextFloat();
            final GLMatF m0 = GLMat3F.translation(x, y);
            final float[] exp = {
                1f, 0f, 0,
                0f, 1f, 0,
                x, y, 1f
            };
            final float[] act = Arrays.copyOfRange(m0.data(), m0.offset(), m0.offset() + 9);

            Assert.assertArrayEquals(exp, act, EPSILON);
        }
    }

    @Test
    public void testTranslationMat4x4() {
        for (int i = 0; i < TEST_COUNT; i++) {
            final float x = random.nextFloat();
            final float y = random.nextFloat();
            final float z = random.nextFloat();
            final GLMatF m0 = GLMat4F.translation(x, y, z);
            final float[] exp = {
                1f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f,
                0f, 0f, 1f, 0f,
                x, y, z, 1f
            };
            final float[] act = Arrays.copyOfRange(m0.data(), m0.offset(), m0.offset() + 16);

            Assert.assertArrayEquals(exp, act, EPSILON);
        }
    }
}
