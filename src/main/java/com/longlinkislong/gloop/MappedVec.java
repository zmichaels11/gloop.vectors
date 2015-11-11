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
 * The base interface for all vectors that use an outside array for its data.
 *
 * @author zmichaels
 * @param <MappedVecT> the class that implements MappedVec
 * @since 15.02.26
 */
public interface MappedVec<MappedVecT extends MappedVec> {

    /**
     * A relative move of the offset of this vector by the specified number of
     * elements. The new offset cannot be less than the base offset nor can it
     * exceed the allowed range.
     *
     * @param shiftOffset the number of elements to move the offset.
     * @return self reference
     * @since 15.02.26
     */
    public MappedVecT shift(int shiftOffset);

    /**
     * Pushes the vector. This treats the MappedVec as a stack and moves the
     * offset exactly one vector worth of elements forwards.
     *
     * @return self reference
     * @since 15.02.26
     */
    public MappedVecT push();

    /**
     * Pops the vector. This treats the MappedVec as a stack and moves the
     * offset exactly one vector worth of elements backwards. This will result
     * in restoring the vector to the values before the push.
     *
     * @return self reference
     * @since 15.02.26
     */
    public MappedVecT pop();

    /**
     * Moves the offset to the specified position. The new offset must be within
     * the bounds of the allowed space.
     *
     * @param offset the new offset value
     * @return self reference
     * @since 15.02.26
     */
    public MappedVecT remap(int offset);
}
