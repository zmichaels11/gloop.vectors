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
 * The base class for quaternion structures.
 *
 * @author zmichaels
 * @param <Q> the child class type.
 * @param <MatT> the corresponding GLMat type.
 * @since 16.01.14
 */
public interface GLQuaternion<Q extends GLQuaternion, MatT extends GLMat4> {

    /**
     * Translates the quaternion into the corresponding rotation matrix.
     *
     * @return the rotation matrix.
     * @since 16.01.14
     */
    MatT asMat4();

    /**
     * Normalizes the quaternion.
     *
     * @return the normalized quaternion.
     * @since 16.01.14
     */
    Q normalize();

    /**
     * Multiplies the quaternion with another quaternion. The input values may
     * be normalized prior to multiplication.
     *
     * @param other the other input quaternion.
     * @return the un-normalized result of the multiplication.
     * @since 16.01.14
     */
    Q multiply(GLQuaternion<?, ?> other);

    /**
     * Retrieves the QuaternionFactory used to generate all child quaternions.
     *
     * @return the QuaternionFactory.
     * @since 16.01.14
     */
    QuaternionFactory getFactory();

    /**
     * Retrieves the corresponding single-precision quaternion. No conversions
     * are done if the quaternion is already of type GLQuaternionF.
     *
     * @return single precision representation of this quaternion.
     * @since 16.01.14
     */
    GLQuaternionF asGLQuaternionF();

    /**
     * Retrieves the corresponding double-precision quaternion. No conversions
     * are done if the quaternion is already of type GLQuaternionD.
     *
     * @return double precision representation of this quaternion.
     * @since 16.01.14
     */
    GLQuaternionD asGLQuaternionD();

    /**
     * Creates a copy of this quaternion using the specified QuaternionFactory.
     *
     * @param factory the QuaternionFactory used to allocate the copy.
     * @return the copied quaternion.
     * @since 16.01.14
     */
    Q copyTo(QuaternionFactory factory);

    /**
     * Creates a copy of this quaternion using the QuaternionFactory returned by
     * [code]this.getFactory()[/code]
     *
     * @return the copied quaternion.
     * @since 16.01.14
     */
    Q copyTo();

    /**
     * Sets the values of this quaternion to mirror those of the supplied
     * quaternion. A temporary copy is used if the contents of the two
     * quaternion are not of the same precision.
     *
     * @param other the other quaternion.
     * @return self reference.
     * @since 16.01.14
     */
    Q set(GLQuaternion<?, ?> other);

    /**
     * Returns a static instance of this quaternion. Static references are
     * guaranteed to not have their contents change through QuaternionFactory
     * side effects. No conversions will take place if the quaternion is already
     * of type StaticQuaternionF or StaticQuaternionD. Otherwise a static copy
     * will be created.
     *
     * @return the static instance of this quaternion.
     * @since 16.01.14
     */
    Q asStaticQuaternion();
}
