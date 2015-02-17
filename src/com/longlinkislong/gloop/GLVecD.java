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
public abstract class GLVecD<GLVecT extends GLVecD> implements GLVec<GLVecT> {

    public static final double EPSILON = 1.19e-07;

    protected abstract double[] data();

    protected abstract int offset();

    @Override
    public final GLVecT normalize() {
        final double scale = 1.0 / this.length();

        return this.scale(scale);
    }

    public abstract GLVecT scale(double value);

    @Override
    public final GLVecD asGLVecD() {
        return this;
    }

    public abstract GLVec2D asGLVec2D();

    public abstract GLVec3D asGLVec3D();

    public abstract GLVec4D asGLVec4D();

    public abstract GLVecND asGLVecND(int size);

    public abstract double get(int index);

    public abstract GLVecT set(int index, double value);

    public abstract GLVecT set(double[] values, int offset, int length);

    public final GLVecT set(final double... values) {
        return this.set(values, 0, values.length);
    }

    @Override
    public final GLVecT set(final GLVec other) {
        final int length = Math.min(other.size(), this.size());
        final GLVecD vec = other.asGLVecD();

        this.zero();

        return this.set(vec.data(), vec.offset(), length);
    }

    public abstract double dot(final GLVec other);

    @Override
    public final int hashCode() {
        return Arrays.hashCode(
                Arrays.copyOfRange(this.data(), this.offset(), this.size()));
    }

    @Override
    public final boolean equals(final Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof GLVecD) {
            final GLVecD v = (GLVecD) other;

            if (v.size() != this.size()) {
                return false;
            } else {
                for (int i = 0; i < this.size(); i++) {
                    final double v0 = this.data()[this.offset() + i];
                    final double v1 = v.data()[v.offset() + i];

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

        out.append("GLVecD: <");

        for (int i = 0; i < this.size() - 1; i++) {
            out.append(this.get(i));
            out.append(", ");
        }

        out.append(this.get(this.size() - 1));
        out.append(">");

        return out.toString();
    }
}
