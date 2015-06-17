/* 
 * Copyright (c) 2015, zmichaels
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
 * The base interface for all vectors.
 *
 * @author zmichaels
 * @param <GLVecT> the superclass of GLVec.
 * @since 15.02.26
 */
public interface GLVec<GLVecT extends GLVec> {

    /**
     * Coerces the GLVec to a single precision vector. This is allowed to return
     * itself if the vector is already a single precision vector.
     *
     * @return A single precision vector
     * @since 15.02.26
     */
    public GLVecF asGLVecF();

    /**
     * Coerces the GLVec as a double precision vector. This is allowed to return
     * itself if the vector is already a double precision vector.
     *
     * @return A double precision vector
     * @since 15.02.26
     */
    public GLVecD asGLVecD();

    /**
     * Retrieves the factory that this GLVec uses. The VectorFactory is used in
     * allocating all vectors that this GLVec may create.
     *
     * @return the VectorFactory
     * @since 15.02.26
     */
    public VectorFactory getFactory();

    /**
     * Calculates the magnitude of this vector.
     *
     * @return the magnitude
     * @since 15.02.26
     */
    public double length();

    /**
     * Returns the number of elements the vector contains.
     *
     * @return the number of elements
     * @since 15.02.26
     */
    public int size();

    /**
     * Adds this vector and another vector and returns the sum.
     *
     * @param other the other vector
     * @return the sum of the two vectors
     * @since 15.02.26
     */
    public GLVecT plus(GLVec other);

    /**
     * Subtracts another vector from this vector and returns the difference.
     *
     * @param other the difference between the two vectors.
     * @return the difference
     * @since 15.02.26
     */
    public GLVecT minus(GLVec other);

    /**
     * Calculates the cross product between this vector and another vector and
     * returns the result.
     *
     * @param other the other vector
     * @return a vector orthogonal to this vector and the other vector.
     * @since 15.02.26
     */
    public GLVecT cross(GLVec other);

    /**
     * Sets the data of this vector to the same values of the other vector.
     *
     * @param other the other vector
     * @return self reference
     * @since 15.02.26
     */
    public GLVecT set(GLVec other);

    /**
     * Alias for cross product
     *
     * @param other the other vector
     * @return the result of the cross product
     * @since 15.02.26
     * @deprecated ambiguous implementation - does not intuitively describe if
     * it uses cross product or hadamard product.
     */
    @Deprecated
    default public GLVecT multiply(final GLVec other) {
        return this.cross(other);
    }

    /**
     * Calculates the hadamard product between this vector and another vector
     * and returns the result. A hadamard product is a product on two vectors or
     * matrices of the same size that produces a vector or matrix of the same
     * size with each element being the product of the two inputs.
     *
     * @param other the other vector
     * @return a vector with all elements being the product of the inputs.
     * @since 15.06.17
     */
    public GLVecT hadamard(GLVec other);

    /**
     * Alias for set(plus(other)).
     *
     * @param other the other vector
     * @return self reference
     * @since 15.02.26
     */
    default public GLVecT plusEquals(final GLVec other) {
        return this.set(this.plus(other));
    }

    /**
     * Alias for set(minus(other))
     *
     * @param other the other vector
     * @return self reference
     * @since 15.02.26
     */
    default public GLVecT minusEquals(final GLVec other) {
        return this.set(this.minus(other));
    }

    /**
     * Alias for set(multiply(other))
     *
     * @param other the other vector
     * @return self reference
     * @since 15.02.26
     */
    default public GLVecT multiplyEquals(final GLVec other) {
        return this.set(this.multiply(other));
    }

    /**
     * Calculates the negative form of this vector. This is the same as
     * subtracting this vector from the zero-vector.
     *
     * @return the negative vector.
     * @since 15.02.26
     */
    public GLVecT negative();

    /**
     * Reflects this vector against a surface normal.
     *
     * @param surfaceNormal the surface normal
     * @return the reflection of this vector
     * @since 15.02.26
     */
    public GLVecT reflect(GLVec surfaceNormal);

    /**
     * Calculates the inverse of this vector. This is the same as the reciprocal
     * of each element of this vector.
     *
     * @return the inverse of this vector.
     * @since 15.02.26
     */
    public GLVecT inverse();

    /**
     * Creates an instance of this vector that is guaranteed not to change. This
     * is allowed to return itself if the vector is already static.
     *
     * @return a static instance of this vector.
     * @since 15.02.26
     */
    public GLVecT asStaticVec();

    /**
     * Sets all elements of this vector to 0.
     *
     * @return self reference
     * @since 15.02.26
     */
    public GLVecT zero();

    /**
     * Retrieves a unit vector that is pointing in the same direction as this
     * vector.
     *
     * @return the normalized vector.
     * @since 15.02.26
     */
    public GLVecT normalize();

    /**
     * Copies the current vector to a vector created by the supplied
     * VectorFactory.
     *
     * @param factory the factory to create the new vector from.
     * @return the new vector
     * @since 15.05.13
     */
    public GLVecT copyTo(final VectorFactory factory);
}
