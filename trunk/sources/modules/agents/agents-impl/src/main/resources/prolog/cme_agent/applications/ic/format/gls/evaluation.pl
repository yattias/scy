/*  $Id$
 *  
 *  File	evaluation.pl
 *  Part of	IC - educational data mining
 *  Author	Anjo Anjewierden, a.a.anjewierden@utwente.nl
 *  Purpose	Evaluation of graph-like structures
 *  Works with	SWI-Prolog (www.swi-prolog.org)
 *  Status	clean, checked
 *  
 *  Notice	Copyright (c) 2011  University of Twente
 *  
 *  History	10/10/11  (Created)
 *  		13/10/11  (Last modified)
 */

/*------------------------------------------------------------
 *  Directives
 *------------------------------------------------------------*/

:- module(evaluation_gls_ic,
	  [ gls_node_perfect_match/3	% GLS x Node x RefMod
	  ]).

:- use_module(library(lists), [subtract/3]).
:- use_module(library(debug), [debug/3]).

:- use_module(model, [gls_edge_tail/3, gls_edge_head/3, gls_node_term/3,
		      gls_node_label/3, gls_node/2, gls_set_property/2,
		      gls_propchk/2]).
:- use_module(specification(reference_model), [reference_model_edge/3, reference_model_node_propchk/3]).


%%	gls_node_perfect_match(+GLS:gls_hdl, +Node:atom, +RM:atom) is semidet.
%
%	Succeeds if Node in GLS is perfectly placed according to the reference
%	model RM.  Perfectly placed means that is connected to all edges in
%	the reference model.

gls_node_perfect_match(G, Node, RM) :-
	gls_compute_dislocated(G, RM),
	gls_propchk(G, dislocated(RM,Dislocated)),
	\+ member(Node, Dislocated).


gls_compute_dislocated(G, RM) :-
	gls_propchk(G, dislocated(RM,_)), !.
gls_compute_dislocated(G, RM) :-
	debug(cme(dev), '~ngls_compute_dislocated/2~n', []),
	findall(N, ( gls_node_term(G,N,Term),
		     reference_model_node_propchk(RM,Term,fill_in(true))), Ns),
	debug(cme(dev), 'PASS 1~n', []),
	dislocated1(Ns, G,RM, Dislocated1),
	debug(cme(dev), 'PASS 2~n', []),
	dislocated2(Dislocated1, Dislocated1, G,RM, Dislocated),
	findall(T, ( member(N,Dislocated),
		     gls_node_term(G, N, T)), Ts),
	debug(cme(dev), 'dislocated ~w~n', [Ts]),
	gls_set_property(G, dislocated(RM,Dislocated)).


dislocated1([], _G,_RM, []).
dislocated1([Node|Nodes], G,RM, [Node|Dislocated]) :-
	is_dislocated1(Node, G, RM), !,
	dislocated1(Nodes, G, RM, Dislocated).
dislocated1([_|Nodes], G,RM, Dislocated) :-
	dislocated1(Nodes, G, RM, Dislocated).

is_dislocated1(Node, G, RM) :-
	findall(e(Tail,Node),
		(gls_edge_tail(G, Edge, Tail),
		 gls_edge_head(G, Edge, Node),
		 gls_node_term(G, Tail, _)), SEs1),
	findall(e(Node,Head),
		(gls_edge_tail(G, Edge, Node),
		 gls_edge_head(G, Edge, Head),
		 gls_node_term(G, Head, _)), SEs2),
	gls_node_term(G, Node, Term),
	findall(e(Tail,Node),
		(reference_model_edge(RM, TailTerm, Term),
		 gls_node_term(G, Tail, TailTerm)), REs1),
	findall(e(Node,Head),
		(reference_model_edge(RM, Term, HeadTerm),
		 gls_node_term(G, Head, HeadTerm)), REs2),
	append(SEs1, SEs2, SEs),
	append(REs1, REs2, REs),
	length(SEs, LenGiven),
	length(REs, LenExpected),
	intersection(SEs, REs, Correct),
	length(Correct, LenCorrect),
	LenWrong is LenGiven - LenCorrect,
	(   LenExpected \== LenGiven
	->  Status = dislocated
	;   LenCorrect == 0
	->  Status = dislocated
	;   LenWrong == 0
	->  Status = perfect
	;   Status = consider
	),
	eval(LenGiven, LenExpected, LenCorrect, LenWrong, Quick),
	debug(cme(dev), 'term ~w~30|~t  ~w ~w ~w ~w ~w ~w', [Term,LenGiven,LenExpected,LenCorrect,LenWrong,Status,Quick]),
	Status == dislocated.


/*------------------------------------------------------------
 *  Pass 2
 *------------------------------------------------------------*/

dislocated2([], _, _G,_RM, []).
dislocated2([Node|Nodes], Previous, G,RM, [Node|Dislocated]) :-
	is_dislocated2(Node, Previous, G, RM), !,
	dislocated2(Nodes, Previous, G,RM, Dislocated).
dislocated2([_|Nodes], Previous, G,RM, Dislocated) :-
	dislocated2(Nodes, Previous, G,RM, Dislocated).

is_dislocated2(Node, Previous, G, RM) :-
	findall(e(Tail,Node),
		(gls_edge_tail(G, Edge, Tail),
		 gls_edge_head(G, Edge, Node),
		 gls_node_term(G, Tail, _),
		 \+ memberchk(Tail, Previous)), SEs1),
	findall(e(Node,Head),
		(gls_edge_tail(G, Edge, Node),
		 gls_edge_head(G, Edge, Head),
		 gls_node_term(G, Head, _),
		 \+ memberchk(Head, Previous)), SEs2),
	gls_node_term(G, Node, Term),
	findall(e(Tail,Node),
		(reference_model_edge(RM, TailTerm, Term),
		 gls_node_term(G, Tail, TailTerm)), REs1),
	findall(e(Node,Head),
		(reference_model_edge(RM, Term, HeadTerm),
		 gls_node_term(G, Head, HeadTerm)), REs2),
	append(SEs1, SEs2, SEs),
	append(REs1, REs2, REs),
	length(SEs, LenGiven),
	length(REs, LenExpected),
	intersection(SEs, REs, Correct),
	length(Correct, LenCorrect),
	LenWrong is LenGiven - LenCorrect,
	eval(LenGiven, LenExpected, LenCorrect, LenWrong, Status),
	debug(cme(dev), 'term ~w~30|~t  ~w ~w ~w ~w ~w~n', [Term,LenGiven,LenExpected,LenCorrect,LenWrong,Status]),
	Status \== perfect.


/*------------------------------------------------------------
 *  Evaluation rule
 *------------------------------------------------------------*/

eval(G, E, C, _, Eval) :-	% If C == 0 it could still be at the
	(   E == C
	->  (   G > E
	    ->  Eval = consider
	    ;   Eval = perfect
	    )
	;   Eval = dislocated
	).
