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
 * A QuaternionF that is guaranteed to retain its data.
 *
 * @author zmichaels
 * @since 16.01.13
 */
public class StaticQuaternionD extends GLQuaternionD {

    private final double[] data = new double[4];
    private final QuaternionFactory qf;

    /**
     * Constructs a new StaticQuaternionF and specifies the QuaternionFactory
     * used to create all child quaternion objects.
     *
     * @param qf the QuaternionFactory.
     * @since 16.01.13
     */
    public StaticQuaternionD(final QuaternionFactory qf) {
        if ((this.qf = qf) == null) {
            throw new NullPointerException("QuaternionFactory cannot be null!");
        }
    }

    /**
     * Constructs a new StaticQuaterninF by copying data from another
     * Quaternion.
     *
     * @param qf the QuaternionFactory used to create all child quaternion
     * objects.
     * @param other the Quaternion to copy the data from.
     * @since 16.01.13
     */
    public StaticQuaternionD(final QuaternionFactory qf, final GLQuaternion other) {
        this(qf);
        this.set(other);
    }

    /**
     * Constructs a new StaticQuaternionF by copying data from the supplied
     * array.
     *
     * @param qf the QuaternionFactory used to create all child quaternion
     * objects.
     * @param data the array to copy the data from.
     * @param offset the offset to read the data.
     * @param length the number of elements to read.
     * @since 16.01.13
     */
    public StaticQuaternionD(final QuaternionFactory qf, final double[] data, final int offset, final int length) {
        this(qf);
        this.set(data, offset, length);
    }

    /**
     * Constructs a new StaticQuaternionF and sets all the values.
     *
     * @param qf the QuaternionFactory used to create all child quaternion
     * objects.
     * @param values the values.
     * @since 16.01.13
     */
    public StaticQuaternionD(final QuaternionFactory qf, final double... values) {
        this(qf, values, 0, values.length);
    }

    @Override
    protected final double[] data() {
        return this.data;
    }

    @Override
    protected final int offset() {
        return 0;
    }

    @Override
    public final QuaternionFactory getFactory() {
        return this.qf;
    }

    @Override
    public final GLQuaternionD asStaticQuaternion() {
        return this;
    }

}
