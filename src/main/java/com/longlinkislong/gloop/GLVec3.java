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
 * A vector that contains three elements.
 *
 * @author zmichaels
 * @since 15.06.17
 */
public interface GLVec3 {

    /**
     * Coerces the vector into a GLVecF. If the vector is already a GLVecF, it
     * is allowed to return itself.
     *
     * @return the GLVecF.
     * @since 15.06.17
     */
    public GLVecF asGLVecF();

    /**
     * Coerces the vector into a GLVecD. If the vector is already a GLVecD, it
     * is allowed to return itself.
     *
     * @return the GLVecD.
     * @since 15.06.17
     */
    public GLVecD asGLVecD();

    /**
     * Coerces the vector into a GLVec3F. If the vector is already a GLVec3F, it
     * is allowed to return itself.
     *
     * @return the GLVec3F.
     * @since 15.06.17
     */
    public GLVec3F asGLVec3F();

    /**
     * Coerces the vector into a GLVec3D. If the vector is already a GLVec3D, it
     * is allowed to return itself.
     *
     * @return the GLVec3D.
     * @since 15.06.17
     */
    public GLVec3D asGLVec3D();
}
