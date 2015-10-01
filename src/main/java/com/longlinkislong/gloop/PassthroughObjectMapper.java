/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longlinkislong.gloop;

/**
 * An ObjectMapper that applies no operations to the input.
 *
 * @author zmichaels
 * @since 15.10.01 
 */
public class PassthroughObjectMapper implements ObjectMapper {

    @Override
    public <T> T map(T obj) {
        return obj;
    }
}
