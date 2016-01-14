/* 
 * Copyright (c) 2016, Zachary Michaels
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
 * A GLQuaternionF that wraps an external float array.
 *
 * @author zmichaels
 * @since 16.01.13
 */
public class MappedQuaternionF extends GLQuaternionF {

    private final float[] data;
    private final int length;
    private final QuaternionFactory qf;
    private final int baseOffset;
    private int offset;

    /**
     * Constructs a new MappedQuaternionF by wrapping a float array. Each
     * quaternion occupies a minimum of 4 elements. MappedQuaternionF allows
     * shifting the base element along the storage array.
     *
     * @param qf the QuaternionFactory used to generate all child quaternion
     * objects.
     * @param data the array used for storing the data.
     * @param offset the offset of the first element of the quaternion.
     * @param length the number of valid elements for the quaternion.
     * @since 16.01.13
     */
    public MappedQuaternionF(
            final QuaternionFactory qf,
            final float[] data, final int offset, final int length) {

        if ((this.data = data) == null) {
            throw new NullPointerException("Data cannot be null!");
        } else if ((this.qf = qf) == null) {
            throw new NullPointerException("QuaternionFactory cannot be null!");
        } else {
            this.baseOffset = this.offset = offset;
            this.length = length;
        }
    }

    @Override
    protected final float[] data() {
        return this.data;
    }

    @Override
    protected final int offset() {
        return this.offset;
    }

    @Override
    public final QuaternionFactory getFactory() {
        return this.qf;
    }

    /**
     * Shifts the offset of the quaternion. The new quaternarion must fit within
     * the valid element range of the storage array.
     *
     * @param shiftOffset the new offset of the first element.
     * @return self reference.
     * @since 16.01.13
     */
    public final MappedQuaternionF shift(final int shiftOffset) {
        return this.remap(this.offset + shiftOffset);
    }

    /**
     * Resets the offset to its original value.
     *
     * @return self reference.
     * @since 16.01.13
     */
    public final MappedQuaternionF reset() {
        return this.remap(this.baseOffset);
    }

    /**
     * Assigns the specified value as the first element of the quaternion. The
     * new quaternarion must fit within the valid element range of the storage
     * array.
     *
     * @param offset the new first offset value.
     * @return self reference.
     * @since 16.01.13
     */
    public final MappedQuaternionF remap(final int offset) {
        if (offset < this.baseOffset || offset > (this.baseOffset + this.length)) {
            throw new IndexOutOfBoundsException("Tried " + offset + " on range [" + this.baseOffset + ", " + (this.baseOffset + this.length) + "]");
        } else {
            this.offset = offset;
            return this;
        }
    }

    /**
     * Increments the quaternion offset by 4. The old quaternion values will
     * remain untouched.
     *
     * @return self reference.
     * @since 16.01.13
     */
    public final MappedQuaternionF push() {
        return this.shift(4);
    }

    /**
     * Decrements the quaternion offset by 4. The old quaternion values will
     * remain untouched.
     *
     * @return self reference.
     * @since 16.01.13
     */
    public final MappedQuaternionF pop() {
        return this.shift(-4);
    }
}
