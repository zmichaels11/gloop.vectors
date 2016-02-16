/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longlinkislong.gloop;

import com.runouw.util.FastRandom;
import java.util.Random;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zmichaels
 */
public class GLMat4BuilderTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(FastTest.class);
    private static final int TEST_COUNT = 5;
    private static final int TEST_SIZE = 5000000;
    private final Random random = new FastRandom();
    
    @Test
    public void testMatBuilder() {
        GLMat4Builder mat = new GLMat4Builder(6);
        mat.setTranslation(1, 2, 3, 1);        
        assertEquals(mat.asGLMat4D(), GLMat4D.translation(1, 2, 3));
        mat.push();
        mat.appendTranslation(1, 2, 3, 1);
        assertEquals(mat.asGLMat4D(), GLMat4D.translation(1, 2, 3).multiply(GLMat4D.translation(1, 2, 3)));        
        mat.pop();
        assertEquals(mat.asGLMat4D(), GLMat4D.translation(1, 2, 3));
    }
    
    @Test
    public void testSpeed() {
        final GLMat4Builder mat = new GLMat4Builder(6);
        
        for(int j = 0; j < TEST_COUNT; j++) {
            long duration = 0L;
            
            for(int i = 0; i < TEST_SIZE; i++) {
                final double x0 = random.nextFloat();
                final double x1 = random.nextFloat();
                final double y0 = random.nextFloat();
                final double y1 = random.nextFloat();
                final double z0 = random.nextFloat();
                final double z1 = random.nextFloat();
                
                final long start = System.nanoTime();
                
                mat.setTranslation(x0, y0, z0, 1.0);
                mat.appendTranslation(x1, y1, z1, 1.0);
                
                final GLMat4D ex = GLMat4D.translation(x0 + x1, y0 + y1, z0 + z1);
                final GLMat4D ac = mat.asGLMat4D();
                
                duration += (System.nanoTime() - start);
                
                Assert.assertEquals(ex, ac);                
            }
            
            LOGGER.info("[Mat4Builder Speed Test {}] Time: {}s", j, (duration * 1e-9));
        }
    }
}
