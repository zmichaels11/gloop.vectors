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
public class GCTest {
    @Test
    public void testGC() throws InstantiationException {
        long start = System.currentTimeMillis();
        Random rand = new Random();
        final VectorFactory vf = new CyclicalVectorFactory();
        
        for(long now = System.currentTimeMillis(); now - start < 300000; now = System.currentTimeMillis()) {
            final float x0 = rand.nextFloat();
            final float y0 = rand.nextFloat();
            final float z0 = rand.nextFloat();
            final float w0 = rand.nextFloat();
            final float x1 = rand.nextFloat();
            final float y1 = rand.nextFloat();
            final float z1 = rand.nextFloat();
            final float w1 = rand.nextFloat();
            
            final GLVec4F v0 = vf.nextGLVec4F().set(x0, y0, z0, w0);
            final GLVec4F v1 = vf.nextGLVec4F().set(x1, y1, z1, w1);
            final GLVec4F sum = v0.plus(v1);
            
            Assert.assertEquals(x0 + x1, sum.x(), GLVecF.EPSILON);
            Assert.assertEquals(y0 + y1, sum.y(), GLVecF.EPSILON);
            Assert.assertEquals(z0 + z1, sum.z(), GLVecF.EPSILON);
            Assert.assertEquals(w0 + w1, sum.w(), GLVecF.EPSILON);
        }
    }
}
