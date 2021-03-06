 Copyright (c) 2015, Zachary Michaels
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.

m4_define(`BaseT', _fdef(`GLMat',,TYPE))
m4_define(`GenT', `GLMat'MAT_SIZE)
m4_define(`MatT', _fdef(`GLMat',MAT_SIZE,TYPE))
m4_define(`VecT', _fdef(`GLVec',MAT_SIZE,TYPE))
m4_define(`_next', `this.getFactory()._fdef(`nextGLMat',$1,$2)($3)')
m4_define(`_cast', `$1.as$2()')
m4_define(`_expand', `$1.ex$2()')
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
m4_define(`OMatT', _fdef(`GLMat',MAT_SIZE,OTHER))
