/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longlinkislong.gloop;

/**
 *
 * @author zmichaels
 */
public final class ObjectMappers {
    static {
        final String objMapper = System.getProperty("gloop.object_mapper", "com.longlinkislong.gloop.PassthroughObjectMapper");                

        ObjectMapper mapper;
        try{
            mapper = (ObjectMapper) Class.forName(objMapper).newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            System.err.println("[ObjectMappers]: Unable to load ObjectMapper: " + objMapper);
            mapper = new PassthroughObjectMapper();
        }
        
        DEFAULT_INSTANCE = mapper;
    }
    
    public static final ObjectMapper DEFAULT_INSTANCE;
    
    private ObjectMappers() {}
}
