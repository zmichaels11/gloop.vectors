/* 
 * Copyright (c) 2016, Zachary Michaels
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.longlinkislong.gloop;

import static java.lang.Math.*;
import static com.longlinkislong.gloop.Matrices.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * Utility class for all Quaternion functions.
 *
 * @author zmichaels
 * @since 16.01.13
 */
public final class Quaternions {

    private static final Marker MARKER = MarkerFactory.getMarker("GLOOP");
    private static final Logger LOGGER = LoggerFactory.getLogger(Quaternions.class);

    public static final QuaternionFactory DEFAULT_FACTORY;

    static {
        final String def = System.getProperty("gloop.quaternions.factory", "cyclical");
        final int cacheSize = Integer.getInteger("gloop.quaternions.cache", 16);

        switch (def.toLowerCase()) {
            case "static":
                DEFAULT_FACTORY = StaticQuaternionFactory.getInstance();
                break;            
            case "cyclical":
                DEFAULT_FACTORY = new CyclicalQuaternionFactory(cacheSize);
                break;
            default:
            case "smart":
                DEFAULT_FACTORY = SmartFactory.getInstance();
        }

        LOGGER.debug(MARKER, "Quaternions.DEFAULT_FACTORY set to {}", DEFAULT_FACTORY.getClass());
    }

    public static final float UNIT_EPSILONF = 2f;
    public static final double UNIT_EPSILOND = 2d;

    public static final int X = 0;
    public static final int Y = 1;
    public static final int Z = 2;
    public static final int W = 3;

    private Quaternions() {
    }

    /**
     * Calculates the squared length of a quaternion. This is calculated by
     * [code]x*x + y*y + z*z + w*w[/code]
     *
     * @param in0 the array containing the quaternion.
     * @param in0Offset the offset of the first element of the quaternion.
     * @return the squared length.
     * @since 16.01.13
     */
    public static double length2D(final double[] in0, final int in0Offset) {

        final double x = in0[in0Offset + X];
        final double y = in0[in0Offset + Y];
        final double z = in0[in0Offset + Z];
        final double w = in0[in0Offset + W];

        return x * x + y * y + z * z + w * w;
    }

    /**
     * Calculates the squared length of a quaternion. This is calculated by
     * [code]x*x + y*y + z*z + w*w[/code]
     *
     * @param in0 the array containing the quaternion.
     * @param in0Offset the offset of the first element of the quaternion.
     * @return the squared length.
     * @since 16.01.13
     */
    public static float length2F(final float[] in0, final int in0Offset) {
        final float x = in0[in0Offset + X];
        final float y = in0[in0Offset + Y];
        final float z = in0[in0Offset + Z];
        final float w = in0[in0Offset + W];

        return x * x + y * y + z * z + w * w;
    }

    /**
     * Computes the normalized Quaternion and copies the values to the output
     * array.
     *
     * @param out the array to write the normalized quaternion to.
     * @param outOffset the offset of the first output element.
     * @param in0 the input quaternion.
     * @param in0Offset the offset of the first input element.
     * @since 16.01.13
     */
    public static void normalizeF(
            final float[] out, final int outOffset,
            final float[] in0, final int in0Offset) {

        final float invMagnitude = (float) (1.0 / sqrt(length2F(in0, in0Offset)));

        out[outOffset + X] = in0[in0Offset + X] * invMagnitude;
        out[outOffset + Y] = in0[in0Offset + Y] * invMagnitude;
        out[outOffset + Z] = in0[in0Offset + Z] * invMagnitude;
        out[outOffset + W] = in0[in0Offset + W] * invMagnitude;
    }

    /**
     * Computes the normalized Quaternion and copies the values to the output
     * array.
     *
     * @param out the array to write the normalized quaternion to.
     * @param outOffset the offset of the first output element.
     * @param in0 the input quaternion.
     * @param in0Offset the offset of the first input element.
     * @since 16.01.13
     */
    public static void normalizeD(
            final double[] out, final int outOffset,
            final double[] in0, final int in0Offset) {

        final double invMagnitude = 1.0 / sqrt(length2D(in0, in0Offset));

        out[outOffset + X] = in0[in0Offset + X] * invMagnitude;
        out[outOffset + Y] = in0[in0Offset + Y] * invMagnitude;
        out[outOffset + Z] = in0[in0Offset + Z] * invMagnitude;
        out[outOffset + W] = in0[in0Offset + W] * invMagnitude;
    }

    /**
     * Multiplies two quaternions together. The input quaternions are normalized
     * if the squared length is greater than the threshold value. The output is
     * not guaranteed to be normalized.
     *
     * @param out the array to write the output to.
     * @param outOffset the offset for the first output element.
     * @param in0 the array to read the first quaternion from.
     * @param in0Offset the offset of the first element of the first quaternion.
     * @param in1 the array to read the second quaternion from.
     * @param in1Offset the offset of the first element of the second
     * quaternion.
     * @since 16.01.13
     */
    public static void multiplyF(
            final float[] out, final int outOffset,
            final float[] in0, final int in0Offset,
            final float[] in1, final int in1Offset) {

        final float x1, y1, z1, w1;
        {
            final float x = in0[in0Offset + X];
            final float y = in0[in0Offset + Y];
            final float z = in0[in0Offset + Z];
            final float w = in0[in0Offset + W];
            final float length2 = x * x + y * y + z * z + w * w;

            if (length2 > UNIT_EPSILONF) {
                final float invMagnitude = (float) (1.0 / sqrt(length2));

                x1 = x * invMagnitude;
                y1 = y * invMagnitude;
                z1 = z * invMagnitude;
                w1 = w * invMagnitude;
            } else {
                x1 = x;
                y1 = y;
                z1 = z;
                w1 = w;
            }
        }

        final float x2, y2, z2, w2;
        {
            final float x = in1[in1Offset + X];
            final float y = in1[in1Offset + Y];
            final float z = in1[in1Offset + Z];
            final float w = in1[in1Offset + W];
            final float length2 = x * x + y * y + z * z + w * w;

            if (length2 > UNIT_EPSILONF) {
                final float invMagnitude = (float) (1.0 / sqrt(length2));

                x2 = x * invMagnitude;
                y2 = y * invMagnitude;
                z2 = z * invMagnitude;
                w2 = w * invMagnitude;
            } else {
                x2 = x;
                y2 = y;
                z2 = z;
                w2 = w;
            }
        }

        out[outOffset + W] = w1 * w2 - x1 * x2 - y1 * y2 - z1 * z2;
        out[outOffset + X] = w1 * x2 + x1 * w2 + y1 * z2 - z1 * y2;
        out[outOffset + Y] = w1 * y2 + x1 + z2 + y1 * w2 + z1 * x2;
        out[outOffset + Z] = w1 * z2 + x1 * y2 - y1 * x2 + z1 * w2;
    }

    /**
     * Multiplies two quaternions together. The input quaternions are normalized
     * if the squared length is greater than the threshold value. The output is
     * not guaranteed to be normalized.
     *
     * @param out the array to write the output to.
     * @param outOffset the offset for the first output element.
     * @param in0 the array to read the first quaternion from.
     * @param in0Offset the offset of the first element of the first quaternion.
     * @param in1 the array to read the second quaternion from.
     * @param in1Offset the offset of the first element of the second
     * quaternion.
     * @since 16.01.13
     */
    public static void multiplyD(
            final double[] out, final int outOffset,
            final double[] in0, final int in0Offset,
            final double[] in1, final int in1Offset) {

        final double x1, y1, z1, w1;
        {
            final double x = in0[in0Offset + X];
            final double y = in0[in0Offset + Y];
            final double z = in0[in0Offset + Z];
            final double w = in0[in0Offset + W];
            final double length2 = x * x + y * y + z * z + w * w;

            if (length2 > UNIT_EPSILOND) {
                final double invMagnitude = 1.0 / sqrt(length2);

                x1 = x * invMagnitude;
                y1 = y * invMagnitude;
                z1 = z * invMagnitude;
                w1 = w * invMagnitude;
            } else {
                x1 = x;
                y1 = y;
                z1 = z;
                w1 = w;
            }
        }

        final double x2, y2, z2, w2;
        {
            final double x = in0[in0Offset + X];
            final double y = in0[in0Offset + Y];
            final double z = in0[in0Offset + Z];
            final double w = in0[in0Offset + W];
            final double length2 = x * x + y * y + z * z + w * w;

            if (length2 > UNIT_EPSILOND) {
                final double invMagnitude = 1.0 / sqrt(length2);

                x2 = x * invMagnitude;
                y2 = y * invMagnitude;
                z2 = z * invMagnitude;
                w2 = w * invMagnitude;
            } else {
                x2 = x;
                y2 = y;
                z2 = z;
                w2 = w;
            }
        }

        out[outOffset + W] = w1 * w2 - x1 * x2 - y1 * y2 - z1 * z2;
        out[outOffset + X] = w1 * x2 + x1 * w2 + y1 * z2 - z1 * y2;
        out[outOffset + Y] = w1 * y2 + x1 + z2 + y1 * w2 + z1 * x2;
        out[outOffset + Z] = w1 * z2 + x1 * y2 - y1 * x2 + z1 * w2;
    }

    /**
     * Constructs a quaternion from an angle and axis.
     *
     * @param out the array to write the quaternion to.
     * @param outOffset the offset of the first element of the output.
     * @param angle the angle in radians.
     * @param x the x-offset of the axis.
     * @param y the y-offset of the axis.
     * @param z the z-offset of the axis.
     * @since 16.01.13
     */
    public static void rotationAxisF(
            final float[] out, final int outOffset,
            final double angle,
            final double x, final double y, final double z) {

        final double hAngle = angle * 0.5;
        final double sinAngle = sin(hAngle);

        out[outOffset + X] = (float) (x * sinAngle);
        out[outOffset + Y] = (float) (y * sinAngle);
        out[outOffset + Z] = (float) (z * sinAngle);
        out[outOffset + W] = (float) cos(hAngle);
    }

    /**
     * Constructs a quaternion from an angle and axis.
     *
     * @param out the array to write the quaternion to.
     * @param outOffset the offset of the first element of the output.
     * @param angle the angle in radians.
     * @param x the x-offset of the axis.
     * @param y the y-offset of the axis.
     * @param z the z-offset of the axis.
     * @since 16.01.13
     */
    public static void rotationAxisD(
            final double[] out, final int outOffset,
            final double angle,
            final double x, final double y, final double z) {

        final double hAngle = angle * 0.5;
        final double sinAngle = sin(hAngle);

        out[outOffset + X] = x * sinAngle;
        out[outOffset + Y] = y * sinAngle;
        out[outOffset + Z] = z * sinAngle;
        out[outOffset + W] = cos(hAngle);
    }

    /**
     * Converts a quaternion into a rotation matrix.
     *
     * @param out the array to write the matrix to.
     * @param outOffset the offset of the first output element.
     * @param in0 the array to read the quaternion from.
     * @param in0Offset the offset of the first input element.
     * @since 16.01.13
     */
    public static void rotationMatF(
            final float[] out, final int outOffset,
            final float[] in0, final int in0Offset) {

        final float x = in0[in0Offset + X];
        final float y = in0[in0Offset + Y];
        final float z = in0[in0Offset + Z];
        final float w = in0[in0Offset + W];

        out[outOffset + E11] = 1f - 2f * y * y - 2f * z * z;
        out[outOffset + E12] = 2f * x * y - 2f * w * z;
        out[outOffset + E13] = 2f * x * z + 2f * w * y;
        out[outOffset + E14] = 0f;

        out[outOffset + E21] = 2f * x * y + 2f * w * z;
        out[outOffset + E22] = 1f - 2f * x * x - 2f * z * z;
        out[outOffset + E23] = 2f * y * z + 2f * w * x;
        out[outOffset + E24] = 0f;

        out[outOffset + E31] = 2f * x * z - 2f * w * y;
        out[outOffset + E32] = 2f * y * z - 2f * w * x;
        out[outOffset + E33] = 1f - 2f * x * x - 2f * y * y;
        out[outOffset + E34] = 0f;

        out[outOffset + E41] = 0f;
        out[outOffset + E42] = 0f;
        out[outOffset + E43] = 0f;
        out[outOffset + E44] = 1f;
    }

    /**
     * Converts a quaternion into a rotation matrix.
     *
     * @param out the array to write the matrix to.
     * @param outOffset the offset of the first output element.
     * @param in0 the array to read the quaternion from.
     * @param in0Offset the offset of the first input element.
     * @since 16.01.13
     */
    public static void rotationMatD(
            final double[] out, final int outOffset,
            final double[] in0, final int in0Offset) {

        final double x = in0[in0Offset + X];
        final double y = in0[in0Offset + Y];
        final double z = in0[in0Offset + Z];
        final double w = in0[in0Offset + W];

        out[outOffset + E11] = 1.0 - 2.0 * y * y - 2.0 * z * z;
        out[outOffset + E12] = 2.0 * x * y - 2.0 * w * z;
        out[outOffset + E13] = 2.0 * x * z + 2.0 * w * y;
        out[outOffset + E14] = 0.0;

        out[outOffset + E21] = 2.0 * x * y + 2.0 * w * z;
        out[outOffset + E22] = 1.0 - 2.0 * x * x - 2.0 * z * z;
        out[outOffset + E23] = 2.0 * y * z + 2.0 * w * x;
        out[outOffset + E24] = 0.0;

        out[outOffset + E31] = 2.0 * x * z - 2.0 * w * y;
        out[outOffset + E32] = 2.0 * y * z - 2.0 * w * x;
        out[outOffset + E33] = 1.0 - 2.0 * x * x - 2.0 * y * y;
        out[outOffset + E34] = 0.0;

        out[outOffset + E41] = 0.0;
        out[outOffset + E42] = 0.0;
        out[outOffset + E43] = 0.0;
        out[outOffset + E44] = 1.0;
    }
}
