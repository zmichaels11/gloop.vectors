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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zmichaels
 */
public class FastTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(FastTest.class);

    private static final int TEST_COUNT = 5;
    private static final int TEST_SIZE = 5000000;
    private final Random random = new FastRandom();

    @Test
    public void testSpeed() {
        for (int j = 0; j < TEST_COUNT; j++) {
            long duration = 0;

            for (int i = 0; i < TEST_SIZE; i++) {
                final double x0 = random.nextFloat();
                final double x1 = random.nextFloat();
                final double y0 = random.nextFloat();
                final double y1 = random.nextFloat();
                final double z0 = random.nextFloat();
                final double z1 = random.nextFloat();

                final long start = System.nanoTime();

                final GLMatD m0 = GLMat4D.translation(x0, y0, z0);
                final GLMatD m1 = GLMat4D.translation(x1, y1, z1);
                final GLMatD ex = GLMat4D.translation(x0 + x1, y0 + y1, z0 + z1);
                final GLMatD ac0 = m0.multiply(m1);
                final GLMatD ac1 = m1.multiply(m0);

                duration += (System.nanoTime() - start);

                Assert.assertNotSame(ex, ac0);
                Assert.assertNotSame(ex, ac1);
                Assert.assertNotSame(ac0, ac1);
                Assert.assertEquals(ex, ac0);
                Assert.assertEquals(ex, ac1);
            }

            LOGGER.info("[Normal Test {}] Time: {}s", j, (duration * 1e-9));            
        }
    }

    //@Test
    public void testFastSpeed() {
        for (int j = 0; j < TEST_COUNT; j++) {
            try (SmartFactory f = SmartFactory.getInstance().withFastMatrices()) {
                long duration = 0;

                for (int i = 0; i < TEST_SIZE; i++) {
                    final double x0 = random.nextDouble();
                    final double x1 = random.nextDouble();
                    final double y0 = random.nextDouble();
                    final double y1 = random.nextDouble();
                    final double z0 = random.nextDouble();
                    final double z1 = random.nextDouble();

                    final long start = System.nanoTime();

                    final GLMatD m0 = GLMat4D.translation(x0, y0, z0);
                    final GLMatD m1 = GLMat4D.translation(x1, y1, z1);
                    final GLMatD ex = GLMat4D.translation(x0 + x1, y0 + y1, z0 + z1);
                    final GLMatD ac0 = m0.multiply(m1);
                    final GLMatD ac1 = m1.multiply(m0);

                    duration += (System.nanoTime() - start);

                    Assert.assertNotSame(ex, ac0);
                    Assert.assertNotSame(ex, ac1);
                    Assert.assertNotSame(ac0, ac1);
                    Assert.assertEquals(ex, ac0);
                    Assert.assertEquals(ex, ac1);
                }

                LOGGER.info("[Fast Test {}] Time: {}s", j, (duration * 1e-9));
            }
        }
    }
}
