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
 * @param <GLVecT>
 */
public interface GLMat<GLMatT extends GLMat, GLVecT extends GLVec> {

    public GLMatF asGLMatF();

    public GLMatD asGLMatD();

    public MatrixFactory getFactory();

    public GLMatT set(GLMat other);

    public GLMatT multiply(GLMat other);
    
    public GLVecT multiply(GLVec vec);

    public GLMatT transpose();

    public GLMatT inverse();
    
    public GLMatT zero();
    
    public GLMatT identity();
    
    public int size();
    
    public GLMatT asStaticMat();        
}
