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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * The base class for all matrices that have double precision.
 *
 * @author zmichaels
 * @param <GLMatT> The class that extends GLMatD
 * @param <GLVecT> the associated vector to the child of GLMatD.
 * @since 15.02.26
 */
public abstract class GLMatD<GLMatT extends GLMatD, GLVecT extends GLVecD> implements GLMat<GLMatT, GLVecT> {

    /**
     * Epsilon value used with GLMatD.
     *
     * @since 15.02.26
     */
    public static final double EPSILON = 1.19e-7;

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
    protected abstract double[] data();

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
    public abstract GLMat2D asGLMat2D();

    /**
     * Coerces this matrix into a 3x3 matrix. This is allowed to return itself
     * if it is already a 3x3 matrix.
     *
     * @return a 3x3 matrix.
     * @since 15.02.26
     */
    public abstract GLMat3D asGLMat3D();

    /**
     * Coerces this matrix into a 4x4 matrix. This is allowed to return itself
     * if it is already a 4x4 matrix.
     *
     * @return a 4x4 matrix.
     * @since 15.02.26
     */
    public abstract GLMat4D asGLMat4D();

    /**
     * Coerces this matrix into a square matrix of the specified size. This is
     * allowed to return itself if it is already the specified size.
     *
     * @param size the number of columns and rows to create.
     * @return a square matrix of the specified size.
     * @since 15.02.26
     */
    public abstract GLMatND asGLMatND(int size);

    /**
     * Retrieves the value of the element at the specified location.
     *
     * @param i the column
     * @param j the row
     * @return the value
     * @since 15.02.26
     */
    public abstract double get(int i, int j);

    /**
     * Sets the specified element to the specified value
     *
     * @param i the column
     * @param j the row
     * @param value the value
     * @return self reference
     * @since 15.02.26
     */
    public abstract GLMatT set(int i, int j, double value);

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
    public abstract GLMatT set(int i, int j, double[] data, int offset, int length, int stride);

    /**
     * Sets each element of the matrix with the corresponding value.
     *
     * @param values the values to set
     * @return self reference
     * @since 15.02.26
     */
    public final GLMatT set(double... values) {
        return this.set(0, 0, values, 0, values.length, this.size());
    }

    @Override
    public final GLMatT set(final GLMat other) {
        final int length = Math.min(other.size(), this.size());
        final GLMatD mat = other.asGLMatD();

        this.zero();

        return this.set(0, 0, mat.data(), mat.offset(), length * length, length);
    }

    /**
     * Scales this matrix with the specified constant. This is the same as
     * multiplying each element of this matrix by the specified value.
     *
     * @param value the value to scale by
     * @return self reference
     * @since 15.02.26
     */
    public abstract GLMatT multiply(double value);

    @Override
    public final GLMatD asGLMatD() {
        return this;
    }

    /**
     * Calculates the determinant of this matrix
     *
     * @return the determinant
     * @since 15.02.26
     */
    public abstract double determinant();

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
        } else if (other instanceof GLMatD) {
            final GLMatD mat = (GLMatD) other;

            if (mat.size() != this.size()) {
                return false;
            } else {
                for (int i = 0; i < this.size(); i++) {
                    for (int j = 0; j < this.size(); j++) {
                        final double v0 = this.get(i, j);
                        final double v1 = mat.get(i, j);

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

        out.append("GLMat/D:[");
        for (int i = 0; i < this.size(); i++) {
            out.append("[");
            for (int j = 0; j < this.size(); j++) {
                out.append(this.get(i, j));
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
}
