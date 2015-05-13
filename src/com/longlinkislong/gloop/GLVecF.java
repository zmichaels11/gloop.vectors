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

import java.util.Arrays;

/**
 * The base class for all vectors that use single precision.
 *
 * @author zmichaels
 * @param <GLVecT> the class that extends GLVecF
 * @since 15.02.26
 */
public abstract class GLVecF<GLVecT extends GLVecF> implements GLVec<GLVecT> {

    /**
     * Epsilon value used with GLVecF
     *
     * @since 15.02.26
     */
    public static final float EPSILON = 1.19e-07f;

    /**
     * Retrieves the array that backs this vector.
     *
     * @return the backing array.
     * @since 15.02.26
     */
    protected abstract float[] data();

    /**
     * Retrieves the offset for when this vector starts within the backed array.
     *
     * @return the backing array
     * @since 15.02.26
     */
    protected abstract int offset();

    @Override
    public final GLVecT normalize() {
        final double scale = 1.0 / this.length();

        return this.scale((float) scale);
    }

    /**
     * Scales this vector by the specified constant.
     *
     * @param value the value to scale by
     * @return the scaled vector
     * @since 15.02.26
     */
    public abstract GLVecT scale(float value);

    @Override
    public final GLVecF asGLVecF() {
        return this;
    }

    @Override
    public abstract GLVecT plus(GLVec other);

    @Override
    public abstract GLVecT minus(GLVec other);

    @Override
    public abstract GLVecT cross(GLVec other);

    @Override
    public abstract GLVecT asStaticVec();

    /**
     * Coerces this vector into a 2D vector. This is allowed to return itself if
     * it is already a 2D vector.
     *
     * @return a 2D vector
     * @since 15.02.26
     */
    public abstract GLVec2F asGLVec2F();

    /**
     * Expands the vector to a 2D vector. This will set the last element to 1 if
     * this vector has a size less than 2.
     * @return a 2D vector
     * @since 15.02.27
     */
    public final GLVec2F exGLVec2F() {
        if(this.size() < 2) {
            return this.asGLVec2F().set(Vectors.Y, 1.0f);
        } else {
            return this.asGLVec2F();
        }
    }

    /**
     * Coerces this vector into a 3D vector. This is allowed to return itself if
     * it is already a 3D vector.
     *
     * @return a 3D vector
     * @since 15.02.26
     */
    public abstract GLVec3F asGLVec3F();

    /**
     * Expands the vector to a 3D vector. This will set the last element to 1 if
     * this vector has a size less than 3.
     *
     * @return a 3D vector.
     * @since 15.02.27
     */
    public final GLVec3F exGLVec3F() {
        if(this.size() < 3) {
            return this.asGLVec3F().set(Vectors.Z, 1.0f);
        } else {
            return this.asGLVec3F();
        }
    }

    /**
     * Coerces this vector into a 4D vector. This is allowed to return itself if
     * it is already a 4D vector.
     *
     * @return a 4D vector
     * @since 15.02.26
     */
    public abstract GLVec4F asGLVec4F();

    /**
     * Expands this vector to a 4D vector. This will set the last element to 1
     * if this vector has a size less than 4.
     *
     * @return a 4D vector.
     * @since 15.02.27
     */
    public final GLVec4F exGLVec4F() {
        if(this.size() < 4) {
            return this.asGLVec4F().set(Vectors.W, 1.0f);
        } else {
            return this.asGLVec4F();
        }
    }

    /**
     * Coerces this vector into a vector of the specified size. This is allowed
     * to return itself if it is already the specified size.
     *
     * @param size the number of elements of the vector
     * @return the vector
     * @since 15.02.26
     */
    public abstract GLVecNF asGLVecNF(int size);

    /**
     * Expands the vector to a ND vector of the specified size. This will set
     * the last element to 1 if this vector has a size less than the specified
     * size.
     *
     * @param size the number of elements
     * @return a the vector
     * @since 15.02.27
     */
    public final GLVecNF exGLVecNF(final int size) {
        if(this.size() < size) {
            return this.asGLVecNF(size).set(size - 1, 1.0f);
        } else {
            return this.asGLVecNF(size);
        }
    }
    
    /**
     * Retrieves the value of the element at the specified index.
     *
     * @param index the index
     * @return the value
     * @since 15.02.26
     */
    public abstract float get(int index);

    /**
     * Sets the value at the specified index
     *
     * @param index the index
     * @param value the value
     * @return self reference
     * @since 15.02.26
     */
    public abstract GLVecT set(int index, float value);

    /**
     * Sets multiple elements to the specified values
     *
     * @param values the values to set
     * @param offset the offset to start reading from values
     * @param length the number of values to set
     * @return self reference
     * @since 15.02.26
     */
    public abstract GLVecT set(float[] values, int offset, int length);

    /**
     * Sets multiple elements to the specied values.
     *
     * @param values the values to set
     * @return self reference
     * @since 15.02.26
     */
    public final GLVecT set(final float... values) {
        return this.set(values, 0, values.length);
    }

    @Override
    public final GLVecT set(final GLVec other) {
        final int length = Math.min(other.size(), this.size());
        final GLVecF vec = other.asGLVecF();

        this.zero();

        return this.set(vec.data(), vec.offset(), length);
    }        

    /**
     * Calculates the dot product of this vector and the other vector.
     *
     * @param other the other vector
     * @return the dot product
     * @since 15.02.26
     */
    public abstract float dot(final GLVec other);

    @Override
    public final int hashCode() {
        final int start = this.offset();
        final int end = start + this.size();
        
        return Arrays.hashCode(
                Arrays.copyOfRange(this.data(), start, end));
    }

    @Override
    public final boolean equals(final Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof GLVecF) {
            final GLVecF v = (GLVecF) other;

            if (v.size() != this.size()) {
                return false;
            } else {
                for (int i = 0; i < this.size(); i++) {
                    final float v0 = this.data()[this.offset() + i];
                    final float v1 = v.data()[v.offset() + i];

                    if (Math.abs(v0 - v1) > EPSILON) {
                        return false;
                    }
                }

                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public final String toString() {
        final StringBuilder out = new StringBuilder();

        out.append("GLVecF: <");

        for (int i = 0; i < this.size() - 1; i++) {
            out.append(this.get(i));
            out.append(", ");
        }

        out.append(this.get(this.size() - 1));
        out.append(">");

        return out.toString();
    }
    
    /**
     * Copies the vector to the array
     * @param array the array to copy the vector to.
     * @param offset the position to start the copy
     * @param length the number of elements to copy. Must be less than size.
     * @since 15.05.13
     */
    public void copyToArray(
            final float[] array, final int offset, final int length) {
        
        if(length > this.size()) {
            throw new IndexOutOfBoundsException();
        }
        
        System.arraycopy(this.data(), this.offset(), array, offset, length);
    }
}
