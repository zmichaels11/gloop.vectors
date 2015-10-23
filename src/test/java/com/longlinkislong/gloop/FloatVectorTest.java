/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longlinkislong.gloop;

import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author zmichaels
 */
public class FloatVectorTest {

    private static final int TEST_COUNT = 1000000;
    private final Random random = new Random();        
    
    @Test
    public void testLengthVec2() {
        for (int i = 0; i < TEST_COUNT; i++) {
            final float x = random.nextFloat();
            final float y = random.nextFloat();

            final GLVecF vec = GLVec2F.create(x, y);
            final double ex = Math.sqrt(x * x + y * y);
            final double ac = vec.length();

            Assert.assertEquals(ex, ac, GLVecF.EPSILON);
        }
    }

    @Test
    public void testLengthVec3() {
        for (int i = 0; i < TEST_COUNT; i++) {
            final float x = random.nextFloat();
            final float y = random.nextFloat();
            final float z = random.nextFloat();

            final GLVecF vec = GLVec3F.create(x, y, z);
            final double ex = Math.sqrt(x * x + y * y + z * z);
            final double ac = vec.length();

            Assert.assertEquals(ex, ac, GLVecF.EPSILON);
        }
    }

    @Test
    public void testLengthVec4() {
        for (int i = 0; i < TEST_COUNT; i++) {
            final float x = random.nextFloat();
            final float y = random.nextFloat();
            final float z = random.nextFloat();
            final float w = random.nextFloat();

            final GLVecF vec = GLVec4F.create(x, y, z, w);
            final double ex = Math.sqrt(x * x + y * y + z * z + w * w);
            final double ac = vec.length();

            Assert.assertEquals(ex, ac, GLVecF.EPSILON);
        }
    }

    @Test
    public void testStaticVec2() {
        for (int i = 0; i < TEST_COUNT; i++) {
            final float x = random.nextFloat();
            final float y = random.nextFloat();
            final GLVecF v0 = GLVec2F.create(x, y);
            final GLVecF v1 = v0.asStaticVec();

            Assert.assertEquals(v0, v1);
            Assert.assertNotSame(v0, v1);
        }
    }

    @Test
    public void testStaticVec3() {
        for (int i = 0; i < TEST_COUNT; i++) {
            final float x = random.nextFloat();
            final float y = random.nextFloat();
            final float z = random.nextFloat();
            final GLVecF v0 = GLVec3F.create(x, y, z);
            final GLVecF v1 = v0.asStaticVec();

            Assert.assertEquals(v0, v1);
            Assert.assertNotSame(v0, v1);
        }
    }

    @Test
    public void testStaticVec4() {
        for (int i = 0; i < TEST_COUNT; i++) {
            final float x = random.nextFloat();
            final float y = random.nextFloat();
            final float z = random.nextFloat();
            final float w = random.nextFloat();
            final GLVecF v0 = GLVec4F.create(x, y, z, w);
            final GLVecF v1 = v0.asStaticVec();

            Assert.assertEquals(v0, v1);
            Assert.assertNotSame(v0, v1);
        }
    }

    @Test
    public void testAllocateVec2() {

        for (int i = 0; i < TEST_COUNT; i++) {
            final float x0 = random.nextFloat();
            final float y0 = random.nextFloat();
            final float x1 = x0 + 0.1f;
            final float y1 = y0 + 0.1f;

            final GLVecF v0 = GLVec2F.create(x0, y0);
            final GLVecF v1 = GLVec2F.create(x1, y1);
            final GLVecF v2 = GLVec2F.create(x0, y0);

            Assert.assertNotSame(v0.offset(), v1.offset());
            Assert.assertNotSame(v0.offset(), v2.offset());
            Assert.assertNotSame(v1.offset(), v2.offset());
            Assert.assertEquals(v2, v0);
        }
    }

    @Test
    public void testAllocateVec3() {
        for (int i = 0; i < TEST_COUNT; i++) {
            final float x0 = random.nextFloat();
            final float y0 = random.nextFloat();
            final float z0 = random.nextFloat();
            final float x1 = x0 + 0.1f;
            final float y1 = y0 + 0.1f;
            final float z1 = z0 + 0.1f;

            final GLVecF v0 = GLVec3F.create(x0, y0, z0);
            final GLVecF v1 = GLVec3F.create(x1, y1, z1);
            final GLVecF v2 = GLVec3F.create(x0, y0, z0);

            Assert.assertNotSame(v0.offset(), v2.offset());
            Assert.assertNotSame(v1.offset(), v2.offset());
            Assert.assertEquals(v2, v0);
        }
    }

    @Test
    public void testAllocateVec4() {
        for (int i = 0; i < TEST_COUNT; i++) {
            final float x0 = random.nextFloat();
            final float y0 = random.nextFloat();
            final float z0 = random.nextFloat();
            final float w0 = random.nextFloat();
            final float x1 = x0 + 0.1f;
            final float y1 = y0 + 0.1f;
            final float z1 = z0 + 0.1f;
            final float w1 = w0 + 0.1f;

            final GLVecF v0 = GLVec4F.create(x0, y0, z0, w0);
            final GLVecF v1 = GLVec4F.create(x1, y1, z1, w1);
            final GLVecF v2 = GLVec4F.create(x0, y0, z0, w0);

            Assert.assertNotSame(v0.offset(), v1.offset());
            Assert.assertNotSame(v0.offset(), v2.offset());
            Assert.assertNotSame(v1.offset(), v2.offset());
            Assert.assertEquals(v2, v0);
        }
    }

    @Test
    public void testPlusVec2() {
        for (int i = 0; i < TEST_COUNT; i++) {
            final float x0 = random.nextFloat();
            final float y0 = random.nextFloat();
            final float x1 = random.nextFloat();
            final float y1 = random.nextFloat();

            final GLVecF v0 = GLVec2F.create(x0, y0);
            final GLVecF v1 = GLVec2F.create(x1, y1);
            final GLVecF exp = GLVec2F.create(x0 + x1, y0 + y1);

            final GLVec act = v0.plus(v1);

            Assert.assertEquals(exp, act);
        }
    }

    @Test
    public void testPlusVec3() {
        for (int i = 0; i < TEST_COUNT; i++) {
            final float x0 = random.nextFloat();
            final float y0 = random.nextFloat();
            final float z0 = random.nextFloat();
            final float x1 = random.nextFloat();
            final float y1 = random.nextFloat();
            final float z1 = random.nextFloat();

            final GLVecF v0 = GLVec3F.create(x0, y0, z0);
            final GLVecF v1 = GLVec3F.create(x1, y1, z1);
            final GLVecF exp = GLVec3F.create(x0 + x1, y0 + y1, z0 + z1);
            final GLVecF act = v0.plus(v1);

            Assert.assertEquals(exp, act);
        }
    }

    @Test
    public void testPlusVec4() {
        for (int i = 0; i < TEST_COUNT; i++) {
            final float x0 = random.nextFloat();
            final float y0 = random.nextFloat();
            final float z0 = random.nextFloat();
            final float w0 = random.nextFloat();
            final float x1 = random.nextFloat();
            final float y1 = random.nextFloat();
            final float z1 = random.nextFloat();
            final float w1 = random.nextFloat();

            final GLVecF v0 = GLVec4F.create(x0, y0, z0, w0);
            final GLVecF v1 = GLVec4F.create(x1, y1, z1, w1);
            final GLVecF ex = GLVec4F.create(x0 + x1, y0 + y1, z0 + z1, w0 + w1);
            final GLVecF ac = v0.plus(v1);

            Assert.assertEquals(ex, ac);
            Assert.assertNotSame(ex.offset(), ac.offset());
        }
    }

    @Test
    public void testMinusVec2() {
        for (int i = 0; i < TEST_COUNT; i++) {
            final float x0 = random.nextFloat();
            final float y0 = random.nextFloat();
            final float x1 = random.nextFloat();
            final float y1 = random.nextFloat();

            final GLVecF v0 = GLVec2F.create(x0, y0);
            final GLVecF v1 = GLVec2F.create(x1, y1);
            final GLVecF ex = GLVec2F.create(x0 - x1, y0 - y1);
            final GLVecF ac = v0.minus(v1);

            Assert.assertEquals(ex, ac);
            Assert.assertNotSame(ex.offset(), ac.offset());
        }
    }

    @Test
    public void testMinusVec3() {
        for (int i = 0; i < TEST_COUNT; i++) {
            final float x0 = random.nextFloat();
            final float y0 = random.nextFloat();
            final float z0 = random.nextFloat();
            final float x1 = random.nextFloat();
            final float y1 = random.nextFloat();
            final float z1 = random.nextFloat();

            final GLVecF v0 = GLVec3F.create(x0, y0, z0);
            final GLVecF v1 = GLVec3F.create(x1, y1, z1);
            final GLVecF ex = GLVec3F.create(x0 - x1, y0 - y1, z0 - z1);
            final GLVecF ac = v0.minus(v1);

            Assert.assertEquals(ex, ac);
            Assert.assertNotSame(ex.offset(), ac.offset());
        }
    }

    @Test
    public void testMinusVec4() {
        for (int i = 0; i < TEST_COUNT; i++) {
            final float x0 = random.nextFloat();
            final float y0 = random.nextFloat();
            final float z0 = random.nextFloat();
            final float w0 = random.nextFloat();
            final float x1 = random.nextFloat();
            final float y1 = random.nextFloat();
            final float z1 = random.nextFloat();
            final float w1 = random.nextFloat();

            final GLVecF v0 = GLVec4F.create(x0, y0, z0, w0);
            final GLVecF v1 = GLVec4F.create(x1, y1, z1, w1);
            final GLVecF ex = GLVec4F.create(x0 - x1, y0 - y1, z0 - z1, w0 - w1);
            final GLVecF ac = v0.minus(v1);

            Assert.assertEquals(ex, ac);
            Assert.assertNotSame(ex.offset(), ac.offset());
        }
    }
    
    @Test
    public void testArrayAddSpeed() {
        final int arraySize = TEST_COUNT;
        System.out.println("Testing array add speed...");
        
        GLVec4FArray in0 = new GLVec4FArray(arraySize);
        GLVec4FArray in1 = new GLVec4FArray(arraySize);        
        
        in0.setX(0, random.nextFloat(), arraySize);
        in0.setY(0, random.nextFloat(), arraySize);
        in0.setZ(0, random.nextFloat(), arraySize);
        in0.setW(0, random.nextFloat(), arraySize);
                
        long start = System.currentTimeMillis();
        for(int i = 0; i < TEST_COUNT / arraySize; i++) {
            in1.setX(0, random.nextFloat(), arraySize);
            in1.setY(0, random.nextFloat(), arraySize);
            in1.setZ(0, random.nextFloat(), arraySize);
            in1.setW(0, random.nextFloat(), arraySize);
        
           GLVec4FArray.apply(VectorArrays::arrayAddF, in0, 0, in0, 0, in1, 0, arraySize);
        }
        long end = System.currentTimeMillis();
        double elapsedSeconds = (end - start) * 1e-7;
        double vps = TEST_COUNT / elapsedSeconds * 1e-10;
        System.out.printf("GLVec4FArray: %.2fE10 per second\n", vps);
        
        System.out.println("Testing array add async speed...");
        
        in0.setX(0, random.nextFloat(), arraySize);
        in0.setY(0, random.nextFloat(), arraySize);
        in0.setZ(0, random.nextFloat(), arraySize);
        in0.setW(0, random.nextFloat(), arraySize);
        
        start = System.currentTimeMillis();
        for(int i = 0; i < TEST_COUNT / arraySize; i++) {
            in1.setX(0, random.nextFloat(), arraySize);
            in1.setY(0, random.nextFloat(), arraySize);
            in1.setZ(0, random.nextFloat(), arraySize);
            in1.setW(0, random.nextFloat(), arraySize);
            
            GLVec4FArray.applyAsync(VectorArrays::arrayAddF, in0, 0, in0, 0, in1, 0, arraySize, true);
        }
        end = System.currentTimeMillis();
        elapsedSeconds = (end - start) * 1e-7;
        vps = TEST_COUNT / elapsedSeconds * 1e-10;
        System.out.printf("GLVec4FArray (async): %.2fE10 per second\n", vps);
    }

    @Test
    public void testAddSpeed() {
        System.out.println("Testing add speed...");

        GLVec a = GLVec2F.create(random.nextFloat(), random.nextFloat());
        GLVec b;

        long start = System.currentTimeMillis();
        for (int i = 0; i < TEST_COUNT; i++) {
            b = GLVec2F.create(random.nextFloat(), random.nextFloat());
            a = b.plus(a);
        }
        long end = System.currentTimeMillis();
        double elapsedSeconds = (end - start) * 1e-7;
        double vps = TEST_COUNT / elapsedSeconds * 1e-10;
        System.out.printf("GLVec2F: %.2fE10 per second\n", vps);

        a = GLVec3F.create(random.nextFloat(), random.nextFloat(), random.nextFloat());
        start = System.currentTimeMillis();

        for (int i = 0; i < TEST_COUNT; i++) {
            b = GLVec3F.create(random.nextFloat(), random.nextFloat(), random.nextFloat());
            a = b.plus(a);
        }
        end = System.currentTimeMillis();
        elapsedSeconds = (end - start) * 1e-7;
        vps = TEST_COUNT / elapsedSeconds * 1e-10;
        System.out.printf("GLVec3F: %.2fE10 per second\n", vps);

        a = GLVec4F.create(random.nextFloat(), random.nextFloat(), random.nextFloat(), random.nextFloat());
        start = System.currentTimeMillis();
        for (int i = 0; i < TEST_COUNT; i++) {
            b = GLVec4F.create(random.nextFloat(), random.nextFloat(), random.nextFloat(), random.nextFloat());
            a = b.plus(a);
        }
        end = System.currentTimeMillis();
        elapsedSeconds = (end - start) * 1e-7;
        vps = TEST_COUNT / elapsedSeconds * 1e-10;
        System.out.printf("GLVec4F: %.2fe10 per second\n", vps);
    }    
}
