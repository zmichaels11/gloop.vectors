/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longlinkislong.gloop;

/**
 *
 * @author zmichaels
 * @param <GLVecT>
 */
public interface GLVec<GLVecT extends GLVec> {

    public GLVecF asGLVecF();        

    public GLVecD asGLVecD();

    public VectorFactory getFactory();

    public double length();

    public int size();

    public GLVecT plus(GLVec other);

    public GLVecT minus(GLVec other);

    public GLVecT cross(GLVec other);

    public GLVecT set(GLVec other);

    default public GLVecT multiply(final GLVec other) {
        return this.cross(other);
    }

    default public GLVecT plusEquals(final GLVec other) {
        return this.set(this.plus(other));
    }

    default public GLVecT minusEquals(final GLVec other) {
        return this.set(this.minus(other));
    }

    default public GLVecT multiplyEquals(final GLVec other) {
        return this.set(this.multiply(other));
    }

    default public GLVecT divideEquals(final GLVec other) {
        return this.multiplyEquals(other.inverse());
    }

    public GLVecT negative();

    public GLVecT reflect(GLVec surfaceNormal);

    public GLVecT inverse();

    public GLVecT asStaticVec();

    public GLVecT zero();

    public GLVecT normalize();
}
