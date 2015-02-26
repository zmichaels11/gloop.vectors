/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longlinkislong.gloop;

import java.util.Arrays;

/**
 *
 * @author zmichaels
 * @param <GLMatT>
 */
public abstract class GLMatF<GLMatT extends GLMatF, GLVecT extends GLVecF> implements GLMat<GLMatT, GLVecF> {

    public static final float EPSILON = 1.19e-7f;

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

    protected abstract float[] data();

    protected abstract int offset();

    public abstract GLMat2F asGLMat2F();

    public abstract GLMat3F asGLMat3F();

    public abstract GLMat4F asGLMat4F();

    public abstract GLMatNF asGLMatNF(int size);

    public abstract float get(int i, int j);

    public abstract GLMatT set(int i, int j, float value);

    public abstract GLMatT set(int i, int j, float[] data, int offset, int length, int stride);

    public final GLMatT set(float... values) {
        return this.set(0, 0, values, 0, values.length, this.size());
    }

    @Override
    public final GLMatT set(final GLMat other) {
        final int length = Math.min(other.size(), this.size());
        final GLMatF mat = other.asGLMatF();

        this.zero();

        return this.set(0, 0, mat.data(), mat.offset(), length * length, length);
    }

    public abstract GLMatT scale(float value);

    public abstract float determinant();

    @Override
    public final GLMatF asGLMatF() {
        return this;
    }

    @Override
    public final int hashCode() {
        return Arrays.hashCode(
                Arrays.copyOfRange(this.data(), this.offset(), this.size() * this.size()));
    }

    @Override
    public final boolean equals(final Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof GLMatF) {
            final GLMatF mat = (GLMatF) other;

            if (mat.size() != this.size()) {
                return false;
            } else {
                for (int i = 0; i < this.size(); i++) {
                    for (int j = 0; j < this.size(); j++) {
                        final float v0 = this.get(i, j);
                        final float v1 = mat.get(i, j);

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
        int k = 0;

        out.append("GLMat/F:[");
        for (int i = 0; i < this.size(); i++) {
            out.append("[");
            for (int j = 0; j < this.size(); j++) {
                out.append(this.data()[k++ + this.offset()]);
                out.append((j < this.size() - 1) ? ", " : "]");
            }
            out.append((i < this.size() - 1) ? ", " : "]");
        }

        return out.toString();
    }
}
