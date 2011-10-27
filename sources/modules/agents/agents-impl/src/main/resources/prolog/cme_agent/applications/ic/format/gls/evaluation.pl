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
		      gls_node_label/3, gls_node/2]). 
:- use_module(specification(reference_model), [reference_model_edge/3]).


%%	gls_node_perfect_match(+GLS:gls_hdl, +Node:atom, +RM:atom) is semidet.
%
%	Succeeds if Node in GLS is perfectly placed according to the reference
%	model RM.  Perfectly placed means that is connected to all edges in
%	the reference model.

gls_node_perfect_match(G, Node, RM) :-
	format('NODE = ~w~n', [Node]),
	findall(N, ( gls_node(G,N),
		     gls_node_term(G,N,_),
		     node_perfect_match_first_pass(G,N,RM)), Perfect),
	node_perfect_second_pass(G, Node, RM, Perfect).

node_perfect_match_first_pass(G, Node, RM) :-
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
	subset(SEs1, REs1),
	subset(REs1, SEs1),
	subset(SEs2, REs2),
	subset(REs2, SEs2).


node_perfect_second_pass(G, Node, RM, Perfect) :-
	findall(e(Tail,Node),
		(gls_edge_tail(G, Edge, Tail),
		 gls_edge_head(G, Edge, Node),
		 member(Tail, Perfect)), SEs1),
	findall(e(Node,Head),
		(gls_edge_tail(G, Edge, Node),
		 gls_edge_head(G, Edge, Head),
		 member(Head, Perfect)), SEs2),
	gls_node_term(G, Node, Term),
	findall(e(Tail,Node),
		(reference_model_edge(RM, TailTerm, Term),
		 gls_node_term(G, Tail, TailTerm)), REs1),
	findall(e(Node,Head),
		(reference_model_edge(RM, Term, HeadTerm),
		 gls_node_term(G, Head, HeadTerm)), REs2),
	subset(SEs1, REs1),
	subset(REs1, SEs1),
	subset(SEs2, REs2),
	subset(REs2, SEs2).
