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

/**
 * A GLQuaternion that uses floats.
 *
 * @author zmichaels
 * @since 16.01.13
 */
public abstract class GLQuaternionF implements GLQuaternion<GLQuaternionF, GLMat4F> {

    /**
     * The number of bytes occupied by the GLQuaternionF.
     *
     * @since 16.01.13
     */
    public static final int QUATERNION_WIDTH = Float.BYTES * 4;

    /**
     * Creates a new quaternion using an angle and axis. The default quaternion
     * factory is used.
     *
     * @param angle the angle in radians.
     * @param axisX the x-axis.
     * @param axisY the y-axis.
     * @param axisZ the z-axis.
     * @return the new quaternion.
     * @since 16.01.13
     */
    public static GLQuaternionF createFromAngleAxis(
            final double angle,
            final double axisX, final double axisY, final double axisZ) {

        final GLQuaternionF out = Quaternions.DEFAULT_FACTORY.nextGLQuaternionF();

        Quaternions.rotationAxisF(out.data(), out.offset(), angle, axisZ, axisY, axisZ);

        return out;
    }

    /**
     * Constructs a new quaternion using the specified values.
     *
     * @param x the x-axis.
     * @param y the y-axis.
     * @param z the z-axis.
     * @param w the angle
     * @return the new quaternion.
     * @since 16.01.13
     */
    public static GLQuaternionF create(final float x, final float y, final float z, final float w) {
        final GLQuaternionF out = Quaternions.DEFAULT_FACTORY.nextGLQuaternionF();

        return out.set(x, y, z, w);
    }

    /**
     * Creates a new quaternion using the specified values. W is set to 1.0.
     *
     * @param x the x-axis.
     * @param y the y-axis.
     * @param z the z-axis.
     * @return the new quaternion
     * @since 16.01.13
     */
    public static GLQuaternionF create(final float x, final float y, final float z) {
        return create(x, y, z, 1.0F);
    }

    /**
     * Creates a new quaternion. The default values of &lt;0,0,0,1&gt; are used.
     *
     * @return the quaternion.
     * @since 16.01.13
     */
    public static GLQuaternionF create() {
        return create(0f, 0f, 0f, 1f);
    }

    /**
     * Sets the values of the quaternion.
     *
     * @param values the new values.
     * @return self reference.
     * @since 16.01.13
     */
    public final GLQuaternionF set(final float... values) {
        return this.set(values, 0, values.length);
    }

    /**
     * Sets the values of the quaternion from an array.
     *
     * @param data the array to read the values from.
     * @param offset the offset of the first element to read.
     * @param size the number of elements to read.
     * @return self reference.
     * @since 16.01.13
     */
    public final GLQuaternionF set(final float[] data, final int offset, final int size) {
        if (size > 4) {
            throw new IndexOutOfBoundsException();
        }

        System.arraycopy(data, offset, this.data(), this.offset(), size);
        return this;
    }

    /**
     * Sets the values of the quaternion.
     *
     * @param index the index to set.
     * @param value the value to set.
     * @return self reference.
     * @since 16.01.13
     */
    public final GLQuaternionF set(final int index, final float value) {
        this.data()[this.offset() + index] = value;
        return this;
    }

    @Override
    public final GLQuaternionF set(final GLQuaternion<?, ?> other) {
        final GLQuaternionF in0 = other.asGLQuaternionF();

        return this.set(in0.data(), in0.offset(), 4);
    }

    /**
     * Retrieves the x value.
     *
     * @return the x-value.
     * @since 16.01.13
     */
    public final float x() {
        return this.data()[this.offset() + Quaternions.X];
    }

    /**
     * Retrieves the y value.
     *
     * @return the y-value.
     * @since 16.01.13
     */
    public final float y() {
        return this.data()[this.offset() + Quaternions.Y];
    }

    /**
     * Retrieves the z value.
     *
     * @return the z-value.
     * @since 16.01.13
     */
    public final float z() {
        return this.data()[this.offset() + Quaternions.Z];
    }

    /**
     * Retrieves the w value.
     *
     * @return the w-value.
     * @since 16.01.13
     */
    public final float w() {
        return this.data()[this.offset() + Quaternions.W];
    }

    /**
     * Retrieves the angle.
     *
     * @return the angle
     * @since 16.01.13
     */
    public final float angle() {
        double angle = 2.0 * Math.acos(this.w());

        if (angle < Math.PI) {
            return (float) angle;
        } else {
            return (float) (2.0 * Math.PI - angle);
        }
    }

    /**
     * Retrieves the data array.
     *
     * @return the backing data.
     * @since 16.01.13
     */
    protected abstract float[] data();

    /**
     * Retrieves the offset within the array.
     *
     * @return the offset.
     * @since 16.01.13
     */
    protected abstract int offset();

    @Override
    public final GLMat4F asMat4() {
        final GLMat4F out = Matrices.DEFAULT_FACTORY.nextGLMat4F();

        Quaternions.rotationMatF(out.data(), out.offset(), this.data(), this.offset());

        return out;
    }

    @Override
    public final GLQuaternionF normalize() {
        final GLQuaternionF out = this.getFactory().nextGLQuaternionF();

        Quaternions.normalizeF(out.data(), out.offset(), this.data(), this.offset());

        return out;
    }

    @Override
    public final GLQuaternionF multiply(final GLQuaternion other) {
        final GLQuaternionF in1 = other.asGLQuaternionF();
        final GLQuaternionF out = this.getFactory().nextGLQuaternionF();

        Quaternions.multiplyF(
                out.data(), out.offset(),
                this.data(), this.offset(),
                in1.data(), in1.offset());

        return out;
    }

    @Override
    public final GLQuaternionF asGLQuaternionF() {
        return this;
    }

    @Override
    public final GLQuaternionD asGLQuaternionD() {
        final GLQuaternionD out = this.getFactory().nextGLQuaternionD();

        out.set(Quaternions.X, this.x());
        out.set(Quaternions.Y, this.y());
        out.set(Quaternions.Z, this.z());
        out.set(Quaternions.W, this.w());

        return out;
    }

    @Override
    public GLQuaternionF asStaticQuaternion() {
        return new StaticQuaternionF(this.getFactory(), this);
    }

    @Override
    public final GLQuaternionF copyTo(final QuaternionFactory factory) {
        return factory.nextGLQuaternionF().set(this.data(), this.offset(), 4);
    }

    @Override
    public final GLQuaternionF copyTo() {
        return this.copyTo(this.getFactory());
    }
}
