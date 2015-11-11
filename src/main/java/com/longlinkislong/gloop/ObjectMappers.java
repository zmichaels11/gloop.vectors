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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zmichaels
 */
public final class ObjectMappers {
    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectMappers.class);
    
    static {
        final String objMapper = System.getProperty("gloop.object_mapper", "com.longlinkislong.gloop.PassthroughObjectMapper");                

        ObjectMapper mapper;
        try{
            mapper = (ObjectMapper) Class.forName(objMapper).newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            LOGGER.warn("Unable to initialize ObjectMapper: " + objMapper, ex);            
            mapper = new PassthroughObjectMapper();
        }
                
        DEFAULT_INSTANCE = mapper;
        LOGGER.debug("Using {} as ObjectMapper.", mapper.getClass().getSimpleName());
    }
    
    public static final ObjectMapper DEFAULT_INSTANCE;
    
    private ObjectMappers() {}
}
