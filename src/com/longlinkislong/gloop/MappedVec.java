/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longlinkislong.gloop;

/**
 *
 * @author zmichaels
 * @param <MappedVecT>
 */
public interface MappedVec <MappedVecT extends MappedVec> {
    public MappedVecT shift(int shiftOffset);
    public MappedVecT push();
    public MappedVecT pop();
    public MappedVecT remap(int offset);
}
