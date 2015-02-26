package com.longlinkislong.gloop;

import java.util.Arrays;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author zmichaels
 * @param <GLMatT>
 * @param <GLVecT>
 */
public abstract class GLMatD<GLMatT extends GLMatD, GLVecT extends GLVecD> implements GLMat<GLMatT, GLVecT> {

    public static final double EPSILON = 1.19e-7;
    
    public static GLMat2D createGLMat2D() {
        return Matrices.DEFAULT_FACTORY.nextGLMat2D().identity();
    }
    
    public static GLMat3D createGLMat3D() {
        return Matrices.DEFAULT_FACTORY.nextGLMat3D().identity();
    }
    
    public static GLMat4D createGLMat4D() {
        return Matrices.DEFAULT_FACTORY.nextGLMat4D().identity();
    }
    
    public static GLMatND createGLMatND(final int size) {
        return Matrices.DEFAULT_FACTORY.nextGLMatND(size).identity();
    }
    
    @Override
    public abstract GLMatT inverse();
    
    @Override
    public abstract GLMatT transpose();
    
    @Override
    public abstract GLMatT multiply(GLMat other);
    
    @Override
    public abstract GLVecT multiply(GLVec vec);
    
    @Override
    public abstract GLMatT asStaticMat();

    protected abstract double[] data();

    protected abstract int offset();

    public abstract GLMat2D asGLMat2D();

    public abstract GLMat3D asGLMat3D();

    public abstract GLMat4D asGLMat4D();

    public abstract GLMatND asGLMatND(int size);

    public abstract double get(int i, int j);

    public abstract GLMatT set(int i, int j, double value);

    public abstract GLMatT set(int i, int j, double[] data, int offset, int length, int stride);

    public final GLMatT set(double... values) {
        return this.set(0, 0, values, 0, values.length, this.size());
    }

    @Override
    public final GLMatT set(final GLMat other) {
        final int length = Math.min(other.size(), this.size());
        final GLMatD mat = other.asGLMatD();

        this.zero();

        return this.set(0, 0, mat.data(), mat.offset(), length * length, length);
    }

    public abstract GLMatT scale(double value);

    @Override
    public final GLMatD asGLMatD() {
        return this;
    }

    public abstract double determinant();

    @Override
    public final int hashCode() {
        return Arrays.hashCode(
                Arrays.copyOfRange(this.data(), this.offset(), this.size() * this.size()));
    }

    @Override
    public final boolean equals(final Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof GLMatD) {
            final GLMatD mat = (GLMatD) other;

            if (mat.size() != this.size()) {
                return false;
            } else {
                for (int i = 0; i < this.size(); i++) {
                    for (int j = 0; j < this.size(); j++) {
                        final double v0 = this.get(i, j);
                        final double v1 = mat.get(i, j);

                        if (Math.abs(v0 - v1) > EPSILON) {
                            return false;
                        }
                    }
                }
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public final String toString() {
        final StringBuilder out = new StringBuilder();

        out.append("GLMat/D:[");
        for (int i = 0; i < this.size(); i++) {
            out.append("[");
            for (int j = 0; j < this.size(); j++) {
                out.append(this.get(i, j));
                out.append((j < this.size() - 1) ? ", " : "]");
            }
            out.append((i < this.size() - 1) ? ", " : "]");
        }

        return out.toString();
    }
}
