m4_define(`BaseT', _fdef(`GLMat',,TYPE))
m4_define(`MatT', _fdef(`GLMat',`N',TYPE))
m4_define(`VecT', _fdef(`GLVec',`N',TYPE))
m4_define(`_next', `this.getFactory()._fdef(`nextGLMat',$1,$2)($3)')
m4_define(`_cast',`$1.as$2($3)')
m4_define(`_call',`Matrices._fdef($1,`N',TYPE)')
m4_define(`_asMat', `m4_dnl
@Override
    public final _fdef(`GLMat', $1, TYPE) _fdef(`asGLMat', $1, TYPE)() {
        final int length = Math.min(this.size(), $1);
        final _fdef(`GLMat', $1, TYPE) out = _next($1, TYPE);

        out.zero();
        
        for(int i = 0; i < $1; i++) {
            for(int j = 0; j < $1; j++) {
                out.set(i, j, this.get(i, j));
            }
        }
        
        return out;
    }
')
m4_define(`OTHER', `m4_ifelse(TYPE, `float', `double', `float')')