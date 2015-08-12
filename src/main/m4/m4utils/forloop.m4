m4_define(`forloop',
       `m4_pushdef(`$1', `$2')_forloop(`$1', `$2', `$3', `$4')m4_popdef(`$1')')
m4_define(`_forloop',
       `$4`'m4_ifelse($1, `$3', ,
		   `m4_define(`$1', m4_incr($1))_forloop(`$1', `$2', `$3', `$4')')')