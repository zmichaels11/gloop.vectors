m4_define(`BaseT', _fdef(`GLMat',,TYPE))
m4_define(`MatT', _fdef(`GLMat',MAT_SIZE,TYPE))
m4_define(`VecT', _fdef(`GLVec',MAT_SIZE,TYPE))
m4_define(`_next', `this.getFactory()._fdef(`nextGLMat',$1,$2)($3)')
m4_define(`_cast', `$1.as$2()')
m4_define(`_call', `Matrices._fdef($1,MAT_SIZE,TYPE)')
m4_define(`_asMat', `m4_dnl
@Override
    public final _fdef(`GLMat', $1, TYPE) _fdef(`asGLMat', $1, TYPE)() {
    m4_ifelse(MAT_SIZE, $1, `m4_dnl
    return this;', `m4_dnl
        final int length = Math.min(this.size(), $1);
        final _fdef(`GLMat', $1, TYPE) out = _next($1, TYPE);

        out.zero();
        out.set(0, 0, this.data(), this.offset(), length * length, length);
        return out;')
    }
')
m4_define(`OTHER', `m4_ifelse(TYPE, `float', `double', `float')')