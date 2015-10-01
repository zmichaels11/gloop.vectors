/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longlinkislong.gloop;

/**
 * Applies a map operation to an object.
 *
 * @author zmichaels 
 * @since 15.10.01
 */
public interface ObjectMapper {

    /**
     * Applies a map operation on the object.
     *
     * @param <T> object type.
     * @param obj the object
     * @return the mapped object.
     * @since 15.10.01
     */
    <T> T map(T obj);
}
