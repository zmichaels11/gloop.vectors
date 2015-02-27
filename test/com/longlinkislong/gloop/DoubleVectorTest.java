package com.longlinkislong.gloop;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author zmichaels
 */
public class DoubleVectorTest {

    private static final int TEST_COUNT = 100000;
    
    private final Random random = new Random();

    @Test
    public void testStaticVec2() {
        for (int i = 0; i < TEST_COUNT; i++) {
            final float x = random.nextFloat();
            final float y = random.nextFloat();
            final GLVecD v0 = GLVec2D.create(x, y);
            final GLVecD v1 = v0.asStaticVec();

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
            final GLVecD v0 = GLVec3D.create(x, y, z);
            final GLVecD v1 = v0.asStaticVec();
            
            Assert.assertEquals(v0, v1);
            Assert.assertNotSame(v0, v1);
        }
    }
    
    @Test
    public void testStaticVec4() {
        for(int i = 0; i < TEST_COUNT; i++) {
            final float x=  random.nextFloat();
            final float y = random.nextFloat();
            final float z = random.nextFloat();
            final float w = random.nextFloat();
            final GLVecD v0 = GLVec4D.create(x, y, z, w);
            final GLVecD v1 = v0.asStaticVec();
            
            Assert.assertEquals(v0, v1);
            Assert.assertNotSame(v0, v1);
        }
    }

    @Test
    public void testAllocateVec2() {
        
        for (int i = 0; i < TEST_COUNT; i++) {
            final double x0 = random.nextDouble();
            final double y0 = random.nextDouble();
            final double x1 = x0 + 0.1;
            final double y1 = y0 + 0.1;

            final GLVecD v0 = GLVec2D.create(x0, y0);
            final GLVecD v1 = GLVec2D.create(x1, y1);
            final GLVecD v2 = GLVec2D.create(x0, y0);
            
            Assert.assertNotSame(v0.offset(), v1.offset());
            Assert.assertNotSame(v0.offset(), v2.offset());
            Assert.assertNotSame(v1.offset(), v2.offset());            
            Assert.assertEquals(v2, v0);            
        }
    }

    @Test
    public void testAllocateVec3() {
        for (int i = 0; i < TEST_COUNT; i++) {
            final double x0 = random.nextDouble();
            final double y0 = random.nextDouble();
            final double z0 = random.nextDouble();
            final double x1 = x0 + 0.1;
            final double y1 = y0 + 0.1;
            final double z1 = z0 + 0.1;

            final GLVecD v0 = GLVec3D.create(x0, y0, z0);
            final GLVecD v1 = GLVec3D.create(x1, y1, z1);
            final GLVecD v2 = GLVec3D.create(x0, y0, z0);
            
            Assert.assertNotSame(v0.offset(), v1.offset());
            Assert.assertNotSame(v0.offset(), v2.offset());
            Assert.assertNotSame(v1.offset(), v2.offset());
            Assert.assertEquals(v2, v0);
        }
    }

    @Test
    public void testAllocateVec4() {
        for (int i = 0; i < TEST_COUNT; i++) {
            final double x0 = random.nextDouble();
            final double y0 = random.nextDouble();
            final double z0 = random.nextDouble();
            final double w0 = random.nextDouble();
            final double x1 = x0 + 0.1;
            final double y1 = y0 + 0.1;
            final double z1 = z0 + 0.1;
            final double w1 = w0 + 0.1;

            final GLVecD v0 = GLVec4D.create(x0, y0, z0, w0);
            final GLVecD v1 = GLVec4D.create(x1, y1, z1, w1);
            final GLVecD v2 = GLVec4D.create(x0, y0, z0, w0);
            
            Assert.assertNotSame(v0.offset(), v1.offset());
            Assert.assertNotSame(v0.offset(), v2.offset());
            Assert.assertNotSame(v1.offset(), v2.offset());
            Assert.assertEquals(v2, v0);            
        }
    }

    @Test
    public void testPlusVec2() {
        for (int i = 0; i < TEST_COUNT; i++) {
            final double x0 = random.nextDouble();
            final double y0 = random.nextDouble();
            final double x1 = random.nextDouble();
            final double y1 = random.nextDouble();

            final GLVecD v0 = GLVec2D.create(x0, y0);
            final GLVecD v1 = GLVec2D.create(x1, y1);
            final GLVecD exp = GLVec2D.create(x0 + x1, y0 + y1);
                        
            final GLVec act = v0.plus(v1);

            Assert.assertEquals(exp, act);
        }
    }

    @Test
    public void testPlusVec3() {
        for (int i = 0; i < TEST_COUNT; i++) {
            final double x0 = random.nextDouble();
            final double y0 = random.nextDouble();
            final double z0 = random.nextDouble();
            final double x1 = random.nextDouble();
            final double y1 = random.nextDouble();
            final double z1 = random.nextDouble();

            final GLVecD v0 = GLVec3D.create(x0, y0, z0);
            final GLVecD v1 = GLVec3D.create(x1, y1, z1);
            final GLVecD exp = GLVec3D.create(x0 + x1, y0 + y1, z0 + z1);
            final GLVec act = v0.plus(v1);
            
            Assert.assertEquals(exp, act);
        }
    }

    @Test
    public void testPlusVec4() {
        for (int i = 0; i < TEST_COUNT; i++) {
            final double x0 = random.nextDouble();
            final double y0 = random.nextDouble();
            final double z0 = random.nextDouble();
            final double w0 = random.nextDouble();
            final double x1 = random.nextDouble();
            final double y1 = random.nextDouble();
            final double z1 = random.nextDouble();
            final double w1 = random.nextDouble();

            final GLVecD v0 = GLVec4D.create(x0, y0, z0, w0);
            final GLVecD v1 = GLVec4D.create(x1, y1, z1, w1);
            final GLVecD ex = GLVec4D.create(x0 + x1, y0 + y1, z0 + z1, w0 + w1);
            final GLVecD ac = v0.plus(v1);
            
            Assert.assertEquals(ex, ac);
            Assert.assertNotSame(ex.offset(), ac.offset());
        }
    }
    
    @Test
    public void testMinusVec2() {
        for (int i = 0; i < TEST_COUNT; i++) {
            final double x0 = random.nextDouble();
            final double y0 = random.nextDouble();
            final double x1 = random.nextDouble();
            final double y1 = random.nextDouble();
            
            final GLVecD v0 = GLVec2D.create(x0, y0);
            final GLVecD v1 = GLVec2D.create(x1, y1);
            final GLVecD ex = GLVec2D.create(x0 - x1, y0 - y1);
            final GLVecD ac = v0.minus(v1);
            
            Assert.assertEquals(ex, ac);
            Assert.assertNotSame(ex.offset(), ac.offset());
        }
    }
    
    @Test
    public void testMinusVec3() {
        for (int i = 0; i < TEST_COUNT; i++) {
            final double x0 = random.nextDouble();
            final double y0 = random.nextDouble();
            final double z0 = random.nextDouble();
            final double x1 = random.nextDouble();
            final double y1 = random.nextDouble();
            final double z1 = random.nextDouble();
            
            final GLVecD v0 = GLVec3D.create(x0, y0, z0);
            final GLVecD v1 = GLVec3D.create(x1, y1, z1);
            final GLVecD ex = GLVec3D.create(x0 - x1, y0 - y1, z0 - z1);
            final GLVecD ac = v0.minus(v1);
            
            Assert.assertEquals(ex, ac);
            Assert.assertNotSame(ex.offset(), ac.offset());
        }
    }
    
    @Test
    public void testMinusVec4() {
        for (int i = 0; i < TEST_COUNT; i++) {
            final double x0 = random.nextDouble();
            final double y0 = random.nextDouble();
            final double z0 = random.nextDouble();
            final double w0 = random.nextDouble();
            final double x1 = random.nextDouble();
            final double y1 = random.nextDouble();
            final double z1 = random.nextDouble();
            final double w1 = random.nextDouble();
            
            final GLVecD v0 = GLVec4D.create(x0, y0, z0, w0);
            final GLVecD v1 = GLVec4D.create(x1, y1, z1, w1);
            final GLVecD ex = GLVec4D.create(x0 - x1, y0 - y1, z0 - z1, w0 - w1);
            final GLVecD ac = v0.minus(v1);
            
            Assert.assertEquals(ex, ac);
            Assert.assertNotSame(ex.offset(), ac.offset());
        }
    }
    
    @Test
    public void testAddSpeed() {
        System.out.println("Testing add speed...");        
        
        GLVec a = GLVec2D.create(random.nextDouble(), random.nextDouble());
        GLVec b;
        
        long start = System.currentTimeMillis();
        for(int i = 0; i < TEST_COUNT; i++) {            
            b = GLVec2D.create(random.nextDouble(), random.nextDouble());
            a = b.plus(a);            
        }
        long end = System.currentTimeMillis();
        double elapsedSeconds = (end - start) * 1e-7;
        double vps = TEST_COUNT / elapsedSeconds * 1e-10;
        System.out.printf("GLVec2D: %.2fE10 per second\n", vps);
        
        a = GLVec3D.create(random.nextDouble(), random.nextDouble(), random.nextDouble());
        start = System.currentTimeMillis();
        
        for(int i = 0; i < TEST_COUNT; i++) {
            b = GLVec3D.create(random.nextDouble(), random.nextDouble(), random.nextDouble());
            a = b.plus(a);            
        }
        end = System.currentTimeMillis();
        elapsedSeconds = (end - start) * 1e-7;
        vps = TEST_COUNT / elapsedSeconds * 1e-10;
        System.out.printf("GLVec3D: %.2fE10 per second\n", vps);
        
        a = GLVec4D.create(random.nextDouble(), random.nextDouble(), random.nextDouble(), random.nextDouble());
        start = System.currentTimeMillis();        
        for(int i = 0; i < TEST_COUNT; i++) {
            b = GLVec4D.create(random.nextDouble(), random.nextDouble(), random.nextDouble(), random.nextDouble());
            a = b.plus(a);
        }
        end = System.currentTimeMillis();
        elapsedSeconds = (end - start) * 1e-7;
        vps = TEST_COUNT / elapsedSeconds * 1e-10;
        System.out.printf("GLVec4D: %.2fe10 per second\n", vps);
    }
}
