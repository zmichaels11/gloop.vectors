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
 * The StaticVectorFactory 
 * @author zmichaels
 */
public final class StaticVectorFactory implements VectorFactory {
    private static final class Holder {
        private static final StaticVectorFactory INSTANCE = new StaticVectorFactory();
    }
    
    private StaticVectorFactory() {}
    
    /**
     * Retrieves the instance of the StaticVectorFactory
     * @return the default instance
     * @since 15.05.13
     */
    public static StaticVectorFactory getInstance() {
        return Holder.INSTANCE;
    }
        
    @Override
    public GLVec2D nextGLVec2D() {
        return new StaticVec2D(this);
    }

    @Override
    public GLVec3D nextGLVec3D() {
        return new StaticVec3D(this);
    }

    @Override
    public GLVec4D nextGLVec4D() {
        return new StaticVec4D(this);
    }

    @Override
    public GLVecND nextGLVecND(int size) {
        return new StaticVecND(this, size);
    }

    @Override
    public GLVec2F nextGLVec2F() {
        return new StaticVec2F(this);
    }

    @Override
    public GLVec3F nextGLVec3F() {
        return new StaticVec3F(this);
    }

    @Override
    public GLVec4F nextGLVec4F() {
        return new StaticVec4F(this);
    }

    @Override
    public GLVecNF nextGLVecNF(int size) {
        return new StaticVecNF(this, size);
    }
    
}
