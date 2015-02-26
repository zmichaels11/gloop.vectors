/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longlinkislong.gloop;

/**
 *
 * @author zmichaels
 * @param <GLMatT>
 */
public interface GLMat<GLMatT extends GLMat> {

    public GLMatF asGLMatF();

    public GLMatD asGLMatD();

    public MatrixFactory getFactory();

    public GLMatT set(GLMat other);

    public GLMatT multiply(GLMat other);

    public GLMatT transpose();

    public GLMatT inverse();
    
    public GLMatT zero();
    
    public int size();
    
    public GLMatT asStaticMat();
}
