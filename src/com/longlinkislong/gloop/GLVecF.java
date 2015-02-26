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
 * @param <GLVecT>
 */
public abstract class GLVecF<GLVecT extends GLVecF> implements GLVec<GLVecT> {

    public static final float EPSILON = 1.19e-07f;   

    protected abstract float[] data();

    protected abstract int offset();               

    @Override
    public final GLVecT normalize() {
        final double scale = 1.0 / this.length();

        return this.scale((float) scale);
    }

    public abstract GLVecT scale(float value);

    @Override
    public final GLVecF asGLVecF() {
        return this;
    }
    
    @Override
    public abstract GLVecT plus(GLVec other);
    
    @Override
    public abstract GLVecT minus(GLVec other);
    
    @Override
    public abstract GLVecT cross(GLVec other);

    @Override
    public abstract GLVecT asStaticVec();
    
    public abstract GLVec2F asGLVec2F();

    public abstract GLVec3F asGLVec3F();

    public abstract GLVec4F asGLVec4F();

    public abstract GLVecNF asGLVecNF(int size);

    public abstract float get(int index);

    public abstract GLVecT set(int index, float value);

    public abstract GLVecT set(float[] values, int offset, int length);

    public final GLVecT set(final float... values) {
        return this.set(values, 0, values.length);
    }

    @Override
    public final GLVecT set(final GLVec other) {
        final int length = Math.min(other.size(), this.size());
        final GLVecF vec = other.asGLVecF();

        this.zero();

        return this.set(vec.data(), vec.offset(), length);
    }

    public abstract float dot(final GLVec other);

    @Override
    public final int hashCode() {
        return Arrays.hashCode(
                Arrays.copyOfRange(this.data(), this.offset(), this.size()));
    }

    @Override
    public final boolean equals(final Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof GLVecF) {
            final GLVecF v = (GLVecF) other;

            if (v.size() != this.size()) {
                return false;
            } else {
                for (int i = 0; i < this.size(); i++) {
                    final float v0 = this.data()[this.offset() + i];
                    final float v1 = v.data()[v.offset() + i];

                    if (Math.abs(v0 - v1) > EPSILON) {
                        return false;
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

        out.append("GLVecF: <");

        for (int i = 0; i < this.size() - 1; i++) {
            out.append(this.get(i));
            out.append(", ");
        }

        out.append(this.get(this.size() - 1));
        out.append(">");

        return out.toString();
    }
}
