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
 * The base interface for all matrices.
 *
 * @author zmichaels
 * @param <GLMatT> the class that extends GLMat
 * @param <GLVecT> the associated vector to the child class of GLMat.
 * @since 15.02.26
 */
public interface GLMat<GLMatT extends GLMat, GLVecT extends GLVec> {

    /**
     * Coerces the GLMat to a single precision matrix. This is allowed to return
     * itself if the matrix is already a single precision matrix.
     *
     * @return A single precision matrix
     * @since 15.02.26
     */
    public GLMatF asGLMatF();

    /**
     * Coerces the GLMat to a double precision matrix. This is allowed to return
     * itself if the matrix is already a double precision matrix.
     *
     * @return A double precision matrix
     * @since 15.02.26
     */
    public GLMatD asGLMatD();

    /**
     * Retrieves the factory that this GLMat uses. The MatrixFactory is used in
     * allocating all matrices that this GLMat may create.
     *
     * @return the MatrixFactory.
     * @since 15.02.26
     */
    public MatrixFactory getFactory();

    /**
     * Sets the data of this matrix to the same values of the other matrix.
     *
     * @param other the other matrix
     * @return self reference
     * @since 15.02.26
     */
    public GLMatT set(GLMat other);

    /**
     * Multiplies this matrix by another matrix and returns the result. This
     * concatenates the properties of this matrix with the other matrix to form
     * a matrix with the features of both matrices.
     *
     * @param other the other matrix
     * @return A matrix with aspects of both matrices.
     * @since 15.02.26
     */
    public GLMatT multiply(GLMat other);        

    /**
     * Multiplies this matrix by a vector of similar length and returns the
     * result. This applies all properties of this matrix to the vector to form
     * a vector with the features of both the input vector and the matrix.
     *
     * @param vec the vector
     * @return A vector with the aspects of both this matrix and the vector
     * @since 15.02.26
     */
    public GLVecT multiply(GLVec vec);

    /**
     * Calculates the transpose of this matrix. The transpose maps each element
     * (i,j) to (j,i) of the output.
     *
     * @return the transpose of this matrix
     * @since 15.02.26
     */
    public GLMatT transpose();

    /**
     * Calculates the inverse of this matrix. The inverse matrix has the
     * property of A * inverse(A) = I.
     *
     * @return the inverse of this matrix
     * @since 15.02.26
     */
    public GLMatT inverse();

    /**
     * Sets each element of this matrix to 0.
     *
     * @return self reference
     * @since 15.02.26
     */
    public GLMatT zero();

    /**
     * Sets the elements of this matrix to the identity matrix. The identity
     * matrix is defined as 1.0 for the diagonal and 0.0 for every other
     * element. The identity matrix has the property of A * I = A.
     *
     * @return self reference.
     * @since 15.02.26
     */
    public GLMatT identity();

    /**
     * Returns the size of the matrix. This is the number of columns or rows
     * that the matrix has.
     *
     * @return the size of the matrix.
     * @since 15.02.26
     */
    public int size();        

    /**
     * Coerces the matrix to a static instance of itself. The static form is
     * guaranteed not to change. This is allowed to return itself if the matrix
     * is already static.
     *
     * @return a static instance of this matrix
     * @since 15.02.26
     */
    public GLMatT asStaticMat();
    
    public GLVecT get(int rowID);
    
    public GLVecT map(int rowID);
    
    public GLMatT set(int rowID, GLVec vec);
    
    /**
     * Copies this matrix to a matrix created by the supplied MatrixFactory.
     * @param factory the factory to create the output matrix
     * @return the output matrix
     * @since 15.05.13
     */
    public GLMatT copyTo(final MatrixFactory factory);       
}
