m4_define(`BaseT', _fdef(`GLVec',,TYPE))
m4_define(`GenT', `GLVec'VEC_SIZE)
m4_define(`VecT', _fdef(`GLVec',VEC_SIZE,TYPE))
m4_define(`_next', `this.getFactory()._fdef(`nextGLVec',$1,$2)($3)')
m4_define(`_cast', `$1.as$2()')
m4_define(`_call', `Vectors._fdef($1,VEC_SIZE,TYPE)')
m4_define(`_asVec', `m4_dnl
@Override
    public final _fdef(`GLVec', $1, TYPE) _fdef(`asGLVec', $1, TYPE)() {
    m4_ifelse(VEC_SIZE, $1, `m4_dnl
    return this;', `m4_dnl
        final int length = Math.min(this.size(), $1);
        final _fdef(`GLVec', $1, TYPE) out = _next($1, TYPE);

        out.zero();
        out.set(this.data(), this.offset(), length);
        return out;')
    }
')
m4_define(`OTHER', `m4_ifelse(TYPE, `float', `double', `float')')
m4_define(`OVecT', _fdef(`GLVec',VEC_SIZE,OTHER))
