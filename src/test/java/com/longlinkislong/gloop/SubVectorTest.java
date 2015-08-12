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
public class SubVectorTest {

    final int tests = 1000;
    final Random rand = new Random();

    @Test
    public void testSubVector2F() {
        for (int i = 0; i < tests; i++) {
            GLMat2F mat = GLMat2F.create();

            Assert.assertEquals(GLVec2F.create(1f, 0f), mat.get(0));
            Assert.assertEquals(GLVec2F.create(0f, 1f), mat.get(1));

            final float e11 = rand.nextFloat();
            final float e12 = rand.nextFloat();
            final float e21 = rand.nextFloat();
            final float e22 = rand.nextFloat();

            mat.map(0).set(e11, e12);
            mat.map(1).set(e21, e22);

            Assert.assertEquals(GLMat2F.create()
                    .set(
                            e11, e12,
                            e21, e22),
                    mat);
        }
    }

    @Test
    public void testSubVector2D() {
        for (int i = 0; i < tests; i++) {
            GLMat2D mat = GLMat2D.create();

            Assert.assertEquals(GLVec2D.create(1d, 0d), mat.get(0));
            Assert.assertEquals(GLVec2D.create(0d, 1d), mat.get(1));

            final double e11 = rand.nextDouble();
            final double e12 = rand.nextDouble();
            final double e21 = rand.nextDouble();
            final double e22 = rand.nextDouble();

            mat.map(0).set(e11, e12);
            mat.map(1).set(e21, e22);

            Assert.assertEquals(GLMat2D.create()
                    .set(
                            e11, e12,
                            e21, e22),
                    mat);
        }
    }

    @Test
    public void testSubVector3F() {
        for (int i = 0; i < tests; i++) {
            GLMat3F mat = GLMat3F.create();

            Assert.assertEquals(GLVec3F.create(1f, 0f, 0f), mat.get(0));
            Assert.assertEquals(GLVec3F.create(0f, 1f, 0f), mat.get(1));
            Assert.assertEquals(GLVec3F.create(0f, 0f, 1f), mat.get(2));

            final float e11 = rand.nextFloat();
            final float e12 = rand.nextFloat();
            final float e13 = rand.nextFloat();
            final float e21 = rand.nextFloat();
            final float e22 = rand.nextFloat();
            final float e23 = rand.nextFloat();
            final float e31 = rand.nextFloat();
            final float e32 = rand.nextFloat();
            final float e33 = rand.nextFloat();

            mat.map(0).set(e11, e12, e13);
            mat.map(1).set(e21, e22, e23);
            mat.map(2).set(e31, e32, e33);

            Assert.assertEquals(
                    GLMat3F.create()
                    .set(
                            e11, e12, e13,
                            e21, e22, e23,
                            e31, e32, e33),
                    mat);
        }
    }

    @Test
    public void testSubVector3D() {
        for (int i = 0; i < tests; i++) {
            GLMat3D mat = GLMat3D.create();

            Assert.assertEquals(GLVec3D.create(1d, 0d, 0d), mat.get(0));
            Assert.assertEquals(GLVec3D.create(0d, 1d, 0d), mat.get(1));
            Assert.assertEquals(GLVec3D.create(0d, 0d, 1d), mat.get(2));

            final double e11 = rand.nextDouble();
            final double e12 = rand.nextDouble();
            final double e13 = rand.nextDouble();
            final double e21 = rand.nextDouble();
            final double e22 = rand.nextDouble();
            final double e23 = rand.nextDouble();
            final double e31 = rand.nextDouble();
            final double e32 = rand.nextDouble();
            final double e33 = rand.nextDouble();

            mat.map(0).set(e11, e12, e13);
            mat.map(1).set(e21, e22, e23);
            mat.map(2).set(e31, e32, e33);

            Assert.assertEquals(
                    GLMat3D.create()
                    .set(
                            e11, e12, e13,
                            e21, e22, e23,
                            e31, e32, e33),
                    mat);
        }
    }

    @Test
    public void testSubVector4F() {
        for (int i = 0; i < tests; i++) {
            GLMat4F mat = GLMat4F.create();

            Assert.assertEquals(GLVec4F.create(1f, 0f, 0f, 0f), mat.get(0));
            Assert.assertEquals(GLVec4F.create(0f, 1f, 0f, 0f), mat.get(1));
            Assert.assertEquals(GLVec4F.create(0f, 0f, 1f, 0f), mat.get(2));
            Assert.assertEquals(GLVec4F.create(0f, 0f, 0f, 1f), mat.get(3));

            final float e11 = rand.nextFloat();
            final float e12 = rand.nextFloat();
            final float e13 = rand.nextFloat();
            final float e14 = rand.nextFloat();
            final float e21 = rand.nextFloat();
            final float e22 = rand.nextFloat();
            final float e23 = rand.nextFloat();
            final float e24 = rand.nextFloat();
            final float e31 = rand.nextFloat();
            final float e32 = rand.nextFloat();
            final float e33 = rand.nextFloat();
            final float e34 = rand.nextFloat();
            final float e41 = rand.nextFloat();
            final float e42 = rand.nextFloat();
            final float e43 = rand.nextFloat();
            final float e44 = rand.nextFloat();

            mat.map(0).set(e11, e12, e13, e14);
            mat.map(1).set(e21, e22, e23, e24);
            mat.map(2).set(e31, e32, e33, e34);
            mat.map(3).set(e41, e42, e43, e44);

            Assert.assertEquals(
                    GLMat4F.create()
                    .set(
                            e11, e12, e13, e14,
                            e21, e22, e23, e24,
                            e31, e32, e33, e34,
                            e41, e42, e43, e44),
                    mat);
        }
    }

    @Test
    public void testSubVector4D() {
        for (int i = 0; i < tests; i++) {
            GLMat4D mat = GLMat4D.create();

            Assert.assertEquals(GLVec4D.create(1d, 0d, 0d, 0d), mat.get(0));
            Assert.assertEquals(GLVec4D.create(0d, 1d, 0d, 0d), mat.get(1));
            Assert.assertEquals(GLVec4D.create(0d, 0d, 1d, 0d), mat.get(2));
            Assert.assertEquals(GLVec4D.create(0d, 0d, 0d, 1d), mat.get(3));

            final double e11 = rand.nextDouble();
            final double e12 = rand.nextDouble();
            final double e13 = rand.nextDouble();
            final double e14 = rand.nextDouble();
            final double e21 = rand.nextDouble();
            final double e22 = rand.nextDouble();
            final double e23 = rand.nextDouble();
            final double e24 = rand.nextDouble();
            final double e31 = rand.nextDouble();
            final double e32 = rand.nextDouble();
            final double e33 = rand.nextDouble();
            final double e34 = rand.nextDouble();
            final double e41 = rand.nextDouble();
            final double e42 = rand.nextDouble();
            final double e43 = rand.nextDouble();
            final double e44 = rand.nextDouble();

            mat.map(0).set(e11, e12, e13, e14);
            mat.map(1).set(e21, e22, e23, e24);
            mat.map(2).set(e31, e32, e33, e34);
            mat.map(3).set(e41, e42, e43, e44);

            Assert.assertEquals(
                    GLMat4D.create()
                    .set(
                            e11, e12, e13, e14,
                            e21, e22, e23, e24,
                            e31, e32, e33, e34,
                            e41, e42, e43, e44),
                    mat);
        }
    }

    @Test
    public void testSubVectorsNF() {
        for (int i = 0; i < tests; i++) {
            GLMatNF mat = GLMatNF.create(5);

            Assert.assertEquals(GLVecNF.create(5, 1f, 0f, 0f, 0f, 0f), mat.get(0));
            Assert.assertEquals(GLVecNF.create(5, 0f, 1f, 0f, 0f, 0f), mat.get(1));
            Assert.assertEquals(GLVecNF.create(5, 0f, 0f, 1f, 0f, 0f), mat.get(2));
            Assert.assertEquals(GLVecNF.create(5, 0f, 0f, 0f, 1f, 0f), mat.get(3));
            Assert.assertEquals(GLVecNF.create(5, 0f, 0f, 0f, 0f, 1f), mat.get(4));

            final float e11 = rand.nextFloat();
            final float e12 = rand.nextFloat();
            final float e13 = rand.nextFloat();
            final float e14 = rand.nextFloat();
            final float e15 = rand.nextFloat();
            final float e21 = rand.nextFloat();
            final float e22 = rand.nextFloat();
            final float e23 = rand.nextFloat();
            final float e24 = rand.nextFloat();
            final float e25 = rand.nextFloat();
            final float e31 = rand.nextFloat();
            final float e32 = rand.nextFloat();
            final float e33 = rand.nextFloat();
            final float e34 = rand.nextFloat();
            final float e35 = rand.nextFloat();
            final float e41 = rand.nextFloat();
            final float e42 = rand.nextFloat();
            final float e43 = rand.nextFloat();
            final float e44 = rand.nextFloat();
            final float e45 = rand.nextFloat();
            final float e51 = rand.nextFloat();
            final float e52 = rand.nextFloat();
            final float e53 = rand.nextFloat();
            final float e54 = rand.nextFloat();
            final float e55 = rand.nextFloat();

            mat.map(0).set(e11, e12, e13, e14, e15);
            mat.map(1).set(e21, e22, e23, e24, e25);
            mat.map(2).set(e31, e32, e33, e34, e35);
            mat.map(3).set(e41, e42, e43, e44, e45);
            mat.map(4).set(e51, e52, e53, e54, e55);

            Assert.assertEquals(
                    GLMatNF.create(5)
                    .set(
                            e11, e12, e13, e14, e15,
                            e21, e22, e23, e24, e25,
                            e31, e32, e33, e34, e35,
                            e41, e42, e43, e44, e45,
                            e51, e52, e53, e54, e55),
                    mat);
        }
    }
}
