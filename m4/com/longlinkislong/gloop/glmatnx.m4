m4_divert(-1)
m4_include(`m4/m4utils/fdef.m4')
m4_include(`m4/m4utils/types.m4')
m4_include(`m4/com/longlinkislong/gloop/glmatnx_def.m4')
m4_divert(0)m4_dnl 
package com.longlinkislong.gloop;

public abstract class MatT extends BaseT<MatT> {
    @Override
    public final _fdef(`GLMat',,OTHER) _fdef(`asGLMat',,OTHER)() {
        final _fdef(`GLMat',,OTHER) out = _next(`N', OTHER, this.size());

        for(int i = 0; i < this.size(); i++) {
            for(int j = 0; j < this.size(); j++) {
                out.set(i, j, (OTHER) this.get(i, j));
            }
        }

        return out;
    }

    _asMat(2)
    _asMat(3)
    _asMat(4)

    @Override
    public final _fdef(`GLMat', `N', TYPE) _fdef(`asGLMat', `N', TYPE)(final int size) {
        final MatT out = _next(`N', TYPE, size);
        final int length = Math.min(this.size(), size);

        out.zero();
        out.set(0, 0, this.data(), this.offset(), length * length, length);
        return out;
    }

    @Override
    public final MatT set(final int i, final int j, final TYPE value) {
        if(i < 0 || j < 0 || i >= this.size() || j >= this.size()) {
            throw new IndexOutOfBoundsException();
        }
    
        final int index = this.offset() + i + this.size() * j;

        this.data()[index] = value;
        return this;
    }

    @Override
    public final TYPE get(final int i, final int j) {
        if(i < 0 || j < 0 || i >= this.size() || j >= this.size()) {
            throw new IndexOutOfBoundsException();
        }

        final int index = this.offset() + i + this.size() * j;

        return this.data()[index];
    }
    
    @Override
    public final MatT zero() {
        for(int i = 0; i < (this.size() * this.size()); i++) {
            this.data()[this.offset() + i] = _real(TYPE, 0.0);
        }

        return this;
    }

    @Override
    public MatT asStaticMat() {
        throw new UnsupportedOperationException("Not supported yet");
    }
}