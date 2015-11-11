/* 
 * Copyright (c) 2015, Zachary Michaels
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
 * A generic 2 dimensional vector.
 *
 * @author zmichaels
 * @since 15.06.17
 */
public interface GLVec2 {

    /**
     * Attempts to convert the vector to a GLVecF. This is allowed to return
     * itself in the case that it is a GLVecF.
     *
     * @return the vector as a GLVecF.
     * @since 15.06.17
     */
    public GLVecF asGLVecF();

    /**
     * Attempts to convert the vector to a GLVecD. This is allowed to return
     * itself in the case that it is a GLVecD.
     *
     * @return the vector as a GLVecD.
     * @since 15.06.07
     */
    public GLVecD asGLVecD();

    /**
     * Converts the vector into a GLVec2F. This is allowed to return itself in
     * the case that it is a GLVec2F.
     *
     * @return the vector as a GLVec2F.
     * @since 15.06.17
     */
    public GLVec2F asGLVec2F();

    /**
     * Converts the vector into a GLVec2D. This is allowed to return itself in
     * the case that it is a GLVec2D.
     *
     * @return the vector as a GLVec2D.
     * @since 15.06.17
     */
    public GLVec2D asGLVec2D();
}
