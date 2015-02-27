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
 * An interface that defines how vectors are allocated.
 *
 * @author zmichaels
 * @since 15.02.26
 */
public interface VectorFactory {

    /**
     * Creates a new 2D double precision vector.
     *
     * @return 2D vector
     * @since 15.02.26
     */
    public GLVec2D nextGLVec2D();

    /**
     * Creates a new 3D double precision vector.
     *
     * @return 3D double precision vector.
     * @since 15.02.26
     */
    public GLVec3D nextGLVec3D();

    /**
     * Creates a new 4D double precision vector.
     *
     * @return 4D double precision vector.
     * @since 15.02.26
     */
    public GLVec4D nextGLVec4D();

    /**
     * Creates a new double precision vector of the specified size.
     *
     * @param size the number of elements
     * @return the vector
     * @since 15.0.26
     */
    public GLVecND nextGLVecND(int size);

    /**
     * Creates a new 2D single precision vector
     *
     * @return 2D single precision vector.
     * @since 15.02.26
     */
    public GLVec2F nextGLVec2F();

    /**
     * Creates a new 3D single precision vector
     *
     * @return 3D single precision vector.
     * @since 15.02.26
     */
    public GLVec3F nextGLVec3F();

    /**
     * Creates a new 4D single precision vector
     *
     * @return 4D single precision vector.
     * @since 15.02.26
     */
    public GLVec4F nextGLVec4F();

    /**
     * Creates a new single precision vector of the spcified size.
     *
     * @param size the number of elements
     * @return the vector
     * @since 15.02.26
     */
    public GLVecNF nextGLVecNF(int size);
}
