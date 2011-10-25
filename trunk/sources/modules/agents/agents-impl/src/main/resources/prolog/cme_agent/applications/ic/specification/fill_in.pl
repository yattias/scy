/*  $Id$
 *  
 *  File	fill_in.pl
 *  Part of	IC - behavioural data mining
 *  Author	Anjo Anjewierden, a.a.anjewierden@utwente.nl
 *  Purpose	Drag-and-drop style concept maps
 *  Works with	SWI-Prolog (www.swi-prolog.org)
 *  Status      
 *  
 *  Notice	Copyright (c) 2011  University of Twente
 *  
 *  History	05/10/11  (Created)
 *  		05/10/11  (Last modified)
 */

/*------------------------------------------------------------
 *  Directives
 *------------------------------------------------------------*/

:- module(fill_in_specification_ic,
	  [ fill_in_evaluation/3	% GLS -> Evaluation <- Options
	  ]).

:- use_module(load).

:- use_module(library(lists), [subtract/3]).
:- use_module(library(option), [option/2]).

:- use_module(gls(model), [gls_node_label/3, gls_node_term/3,
			   gls_apply_term_set/3, gls_node/2,
			   gls_node_visualchk/3, gls_node_propchk/3,
			   gls_node_replace_by/3, gls_node_delete/2,
			   gls_edge_head/3, gls_edge_tail/3]).   
:- use_module(gls(evaluation), [gls_node_perfect_match/3]).
:- use_module(specification(reference_model), [reference_model_propchk/2,
			   reference_model_node_propchk/3, reference_model_edge/3]). 


fill_in_evaluation(G, Evaluation, Options) :-
	option(term_set(TS), Options),
	option(reference_model(RM), Options),
	gls_apply_term_set(G, TS, []),
	findall(nt(N,T), gls_node_term(G, N, T), NTs),
	duplicate_terms(NTs, Dups),
	findall(E, gls_node_label(G,E,''), Es),		% Empty -- not filled in
	findall(C, ( gls_node(G,C),
		     gls_node_term(G, C, Term),
		     reference_model_node_propchk(RM, Term, fill_in(true))), Cs),
	findall(N, ( gls_node(G,N),
		     \+ memberchk(N, Es),
		     \+ gls_node_term(G, N, Term)), Ns),
	findall(P, ( member(P,Cs),
		     gls_node_term(G, P, Term),
		     gls_node_perfect_match(G, P, RM)), Ps),
	subtract(Cs, Ps, Ds),
	Evaluation = [ gls(G),
		       empty(Es),		% Empty nodes
		       duplicates(Dups),	% Duplicate nodes
		       not_understood(Ns),	% Not recognised by agent
		       correct(Cs),		% Correct
		       perfect(Ps),		% In the correct location
		       dislocated(Ds)		% At the wrong location
		     ].


duplicate_terms([], []).
duplicate_terms([nt(N1,T)|NTs], [[N1|Ns]|Dups]) :-
	findall(N2, member(nt(N2,T), NTs), Ns),
	Ns \== [], !,
	delete(NTs, nt(_,T), Rest),
	duplicate_terms(Rest, Dups).
duplicate_terms([_|NTs], Dups) :-
	duplicate_terms(NTs, Dups).


	
