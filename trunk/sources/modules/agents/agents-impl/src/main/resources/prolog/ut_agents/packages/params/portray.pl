:- multifile
	user:portray/1.

user:portray(element(E,As,C)) :- !,
	(   C = []
	->  format('element(~w, ~q, [])~n', [E,As])
	;   format('element(~w, ~q, [...,...])~n', [E,As])
	).

