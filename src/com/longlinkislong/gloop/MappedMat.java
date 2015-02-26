/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longlinkislong.gloop;

/**
 *
 * @author zmichaels
 * @param <MappedMatT>
 */
public interface MappedMat <MappedMatT extends MappedMat> {
    public MappedMatT shift(int shiftOffset);
    public MappedMatT push();
    public MappedMatT pop();
    public MappedMatT remap(int offset);
}
