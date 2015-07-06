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

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Arrays;

/**
 * The base class for all matrices that have single precision.
 *
 * @author zmichaels
 * @param <GLMatT> the class that extends GLMatF
 * @param <GLVecT> the associated vector to the child of GLMatF
 * @since 15.02.26
 */
public abstract class GLMatF<GLMatT extends GLMatF, GLVecT extends GLVecF> implements GLMat<GLMatT, GLVecF> {

    /**
     * Epsilon value used with GLMatF
     *
     * @since 15.02.26
     */
    public static final float EPSILON = 1.19e-7f;

    @Override
    public abstract GLMatT inverse();

    @Override
    public abstract GLMatT transpose();

    @Override
    public abstract GLMatT multiply(GLMat other);

    @Override
    public abstract GLVecT multiply(GLVec vec);

    @Override
    public abstract GLMatT asStaticMat();

    /**
     * Retrieves the array that backs this matrix.
     *
     * @return the backing array
     * @since 15.02.26
     */
    protected abstract float[] data();

    /**
     * Retrieves the offset for when this matrix starts within the backed array.
     *
     * @return the offset for this matrix.
     * @since 15.02.26
     */
    protected abstract int offset();

    /**
     * Coerces this matrix into a 2x2 matrix. This is allowed to return itself
     * if it is already a 2x2 matrix.
     *
     * @return a 2x2 matrix.
     * @since 15.02.26
     */
    public GLMat2F asGLMat2F() {
        if (this instanceof GLMat2F) {
            return (GLMat2F) this;
        }

        final GLMat2F out = this.getFactory().nextGLMat2F();
        final int len = Math.min(this.size(), 2);

        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                out.set(i, j, this.get(i, j));
            }
        }

        return out;
    }

    /**
     * Coerces this matrix into a 3x3 matrix. This is allowed to return itself
     * if it is already a 3x3 matrix.
     *
     * @return a 3x3 matrix.
     * @since 15.02.26
     */
    public final GLMat3F asGLMat3F() {
        if (this instanceof GLMat3F) {
            return (GLMat3F) this;
        }

        final GLMat3F out = this.getFactory().nextGLMat3F();
        final int len = Math.min(this.size(), 3);

        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                out.set(i, j, this.get(i, j));
            }
        }

        return out;
    }

    /**
     * Coerces this matrix into a 4x4 matrix. This is allowed to return itself
     * if it is already a 4x4 matrix.
     *
     * @return a 4x4 matrix.
     * @since 15.02.26
     */
    public GLMat4F asGLMat4F() {
        if (this instanceof GLMat4F) {
            return (GLMat4F) this;
        }

        final GLMat4F out = this.getFactory().nextGLMat4F().identity();
        final int len = Math.min(this.size(), 4);

        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                out.set(i, j, this.get(i, j));
            }
        }

        return out;
    }

    /**
     * Coerces this matrix into a square matrix of the specified size. This is
     * allowed to return itself if it is already the specified size.
     *
     * @param size the number of columns and rows to create.
     * @return a square matrix of the specified size.
     * @since 15.02.26
     */
    public final GLMatNF asGLMatNF(int size) {
        if (this.size() == size && this instanceof GLMatNF) {
            return (GLMatNF) this;
        }

        final GLMatNF out = this.getFactory().nextGLMatNF(size).identity();
        final int len = Math.min(this.size(), size);

        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                out.set(i, j, this.get(i, j));
            }
        }

        return out;
    }

    protected int index(final int i, final int j) {
        return i + j * this.size();
    }

    /**
     * Retrieves the value of the element at the specified location.
     *
     * @param i the column
     * @param j the row
     * @return the value
     * @since 15.02.26
     */
    public abstract float get(int i, int j);

    /**
     * Sets the specified element to the specified value
     *
     * @param i the column
     * @param j the row
     * @param value the value
     * @return self reference
     * @since 15.02.26
     */
    public abstract GLMatT set(int i, int j, float value);

    /**
     * Writes a chunk of the matrix
     *
     * @param i the column to start
     * @param j the row to start
     * @param data the data to write
     * @param offset the offset to start reading from the data array
     * @param length the maximum number of elements to read
     * @param stride stride of the data array.
     * @return self reference
     * @since 15.02.26
     */
    public abstract GLMatT set(int i, int j, float[] data, int offset, int length, int stride);

    /**
     * Sets each element of the matrix with the corresponding value.
     *
     * @param values the values to set
     * @return self reference
     * @since 15.02.26
     */
    public final GLMatT set(float... values) {
        return this.set(0, 0, values, 0, values.length, this.size());
    }

    @Override
    public final GLMatT set(final GLMat other) {
        final GLMatF mat = other.asGLMatF();
        final GLMatT out = this.identity();
        
        if(other.size() == this.size()){
            final int length = this.size();
            return this.set(0, 0, mat.data(), mat.offset(), length * length, length);
        }else{
            final int length = Math.min(other.size(), this.size());
            
            for(int i = 0; i < length; i++) {
                for(int j = 0; j < length; j++) {
                    out.set(i, j, mat.get(i, j));
                }
            }
            return out;
        }
    }

    /**
     * Scales this matrix with the specified constant. This is the same as
     * multiplying each element of this matrix by the specified value.
     *
     * @param value the value to scale by
     * @return self reference
     * @since 15.02.26
     */
    public abstract GLMatT multiply(float value);

    /**
     * Calculates the determinant of this matrix
     *
     * @return the determinant
     * @since 15.02.26
     */
    public abstract float determinant();

    @Override
    public final GLMatF asGLMatF() {
        return this;
    }

    @Override
    public final int hashCode() {
        final int start = this.offset();
        final int end = start + (this.size() * this.size());
        return Arrays.hashCode(
                Arrays.copyOfRange(this.data(), start, end));
    }

    @Override
    public final boolean equals(final Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof GLMatF) {
            final GLMatF mat = (GLMatF) other;

            if (mat.size() != this.size()) {
                return false;
            } else {
                for (int i = 0; i < this.size(); i++) {
                    for (int j = 0; j < this.size(); j++) {
                        final float v0 = this.get(i, j);
                        final float v1 = mat.get(i, j);

                        if (Math.abs(v0 - v1) > EPSILON) {
                            return false;
                        }
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
        int k = 0;

        out.append("GLMat/F:[");
        for (int i = 0; i < this.size(); i++) {
            out.append("[");
            for (int j = 0; j < this.size(); j++) {
                out.append(this.data()[k++ + this.offset()]);
                out.append((j < this.size() - 1) ? ", " : "]");
            }
            out.append((i < this.size() - 1) ? ", " : "]");
        }

        return out.toString();
    }

    /**
     * Copies the matrix to the array
     *
     * @param array the array to copy the matrix to.
     * @param offset the position to start the copy
     * @param length the number of elements to copy. Must be less than size
     * squared.
     * @since 15.05.13
     */
    public void copyToArray(
            final float[] array, final int offset, final int length) {

        if (length > (this.size() * this.size())) {
            throw new IndexOutOfBoundsException();
        }

        System.arraycopy(this.data(), this.offset(), array, offset, length);
    }

    /**
     * Writes a segment of a GLMat to a ByteBuffer.
     *
     * @param out the ByteBuffer to write to.
     * @param outOffset the offset of the ByteBuffer to write to.
     * @param in0 the matrix to read from.
     * @param in0Offset the element offset to read from the matrix.
     * @param writeCount the number of elements to read.
     * @since 15.07.04
     */
    public static void writeTo(final ByteBuffer out, final int outOffset, final GLMatF in0, final int in0Offset, final int writeCount) {
        for (int i = 0; i < writeCount; i++) {
            out.putFloat(outOffset + i * Float.BYTES, in0.data()[in0.offset() + in0Offset]);
        }
    }
}
