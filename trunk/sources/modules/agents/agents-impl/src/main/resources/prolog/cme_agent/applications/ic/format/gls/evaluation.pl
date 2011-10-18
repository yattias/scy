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

:- use_module(model, [gls_edge_tail/3, gls_edge_head/3, gls_node_term/3, gls_node_label/3]). 
:- use_module(specification(reference_model), [reference_model_edge/3]).

%%	gls_node_perfect_match(+GLS:gls_hdl, +Node:atom, +RM:atom) is semidet.
%
%	Succeeds if Node in GLS is perfectly placed according to the reference
%	model RM.  Perfectly placed means that is connected to all edges in
%	the reference model.

gls_node_perfect_match(G, Node, RM) :-
	findall(e(Tail,Node),
		(gls_edge_tail(G, Edge, Tail),
		 gls_edge_head(G, Edge, Node),
		 gls_node_term(G, Tail, Term)), SEs1),
	findall(e(Node,Head),
		(gls_edge_tail(G, Edge, Node),
		 gls_edge_head(G, Edge, Head),
		 gls_node_term(G, Head, Term)), SEs2),
	gls_node_term(G, Node, Term),
	findall(e(Tail,Node),
		(reference_model_edge(RM, TailTerm, Term),
		 gls_node_term(G, Tail, TailTerm)), REs1),
	findall(e(Node,Head),
		(reference_model_edge(RM, Term, HeadTerm),
		 gls_node_term(G, Head, HeadTerm)), REs2),
	subtract(SEs1, REs1, TailDiff),
	subtract(SEs2, REs2, HeadDiff),
/*	filter_wrong_nodes(TailDiff0, G, Node, TailDiff),
	filter_wrong_nodes(HeadDiff0, G, Node, HeadDiff),
  format('gls_node_perfect_match for ~w~n', [Node]),
  format('tail diff = ~w~n', [TailDiff]),
  format('head diff = ~w~n', [HeadDiff]),
	(   TailDiff == []
	->  true
	;   forall(member(e(T,H), TailDiff),
		   ( gls_node_label(G, H, L1),
		     gls_node_label(G, T, L2),
		     format('tail diff ~w -> ~w~n', [L2,L1])))
	),
	(   HeadDiff == []
	->  true
	;   forall(member(e(T,H), HeadDiff),
		   ( gls_node_label(G, H, L1),
		     gls_node_label(G, T, L2),
		     format('head diff ~w -> ~w~n', [L2,L1])))
	),
*/	TailDiff == [],
	HeadDiff == [].


filter_wrong_nodes([], _, []).
filter_wrong_nodes([e(T,H)|Es], G, Self, Rest) :-
	T \== Self,
	H \== Self,
	\+ (gls_node_term(G, T, _);
	    gls_node_term(G, H, _)), !,
	filter_wrong_nodes(Es, G, Self, Rest).
filter_wrong_nodes([e(T,H)|Es], G, Self, [e(T,H)|Rest]) :-
	filter_wrong_nodes(Es, G, Self, Rest).
