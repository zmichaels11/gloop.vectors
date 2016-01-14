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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * An implementation of QuaternionFactory that allocates all quaternion objects
 * on initialization. Those quaternion objects are then reused periodically.
 *
 * @author zmichaels
 * @since 16.01.14
 */
public class CyclicalQuaternionFactory implements QuaternionFactory {

    private static final Marker MARKER = MarkerFactory.getMarker("GLOOP");
    private static final Logger LOGGER = LoggerFactory.getLogger(CyclicalQuaternionFactory.class);

    private final double[] dataD;
    private final float[] dataF;

    private final MappedQuaternionF[] quatFCache;
    private final MappedQuaternionD[] quatDCache;

    private int dataDOffset;
    private int dataFOffset;

    private int quatFID = 0;
    private int quatDID = 0;

    /**
     * Constructs a new CyclicalQuaternionFactory using the default cache size
     * of 16KB of floats and 16KB for doubles.
     *
     * @since 16.01.14
     */
    public CyclicalQuaternionFactory() {
        this(16);
    }

    /**
     * Constructs a new CyclicalQuaternionFactory with the specified cache size.
     *
     * @param cacheSize the float and double cache sizes in KB.
     * @since 16.01.14
     */
    public CyclicalQuaternionFactory(final int cacheSize) {
        final ObjectMapper map = ObjectMappers.DEFAULT_INSTANCE;
        final int cacheBytes = cacheSize * 1000;

        LOGGER.debug(MARKER, "Constructing quaternion cache; size = {}B", cacheSize);

        this.dataD = map.map(new double[cacheBytes / Double.BYTES]);
        this.dataF = map.map(new float[cacheBytes / Float.BYTES]);

        this.quatFCache = map.map(new MappedQuaternionF[cacheBytes / GLQuaternionF.QUATERNION_WIDTH]);
        this.quatDCache = map.map(new MappedQuaternionD[cacheBytes / GLQuaternionD.QUATERNION_WIDTH]);

        for (int i = 0; i < this.quatFCache.length; i++) {
            this.quatFCache[i] = map.map(new MappedQuaternionF(this, this.dataF, 0, this.dataF.length - 4));
        }

        for (int i = 0; i < this.quatDCache.length; i++) {
            this.quatDCache[i] = map.map(new MappedQuaternionD(this, this.dataD, 0, this.dataD.length - 4));
        }
    }

    private int nextQuatFID() {
        final int testID = this.quatFID + 1;

        return this.quatFID = testID % this.quatFCache.length;
    }

    private int nextQUatDID() {
        final int testID = this.quatDID + 1;

        return this.quatDID = testID % this.quatDCache.length;
    }

    private int nextQuatFOffset() {
        if (this.dataFOffset + 4 < this.dataF.length) {
            final int off = this.dataFOffset;

            this.dataFOffset += 4;
            return off;
        } else {
            this.dataFOffset = 4;
            return 0;
        }
    }

    private int nextQuatDOffset() {
        if (this.dataDOffset + 4 < this.dataD.length) {
            final int off = this.dataDOffset;

            this.dataDOffset += 4;
            return off;
        } else {
            this.dataDOffset = 4;
            return 0;
        }
    }

    @Override
    public final GLQuaternionF nextGLQuaternionF() {
        final int id = this.nextQuatFID();
        final int offset = this.nextQuatFOffset();

        return this.quatFCache[id].remap(offset);
    }

    @Override
    public final GLQuaternionD nextGLQuaternionD() {
        final int id = this.nextQUatDID();
        final int offset = this.nextQuatDOffset();

        return this.quatDCache[id].remap(offset);
    }
}
