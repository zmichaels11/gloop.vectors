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
