/*  $Id$
 *  
 *  File	evaluate.pl
 *  Part of	IC - educational data mining
 *  Author	Anjo Anjewierden, a.a.anjewierden@utwente.nl
 *  Purpose	Evaluating concept maps
 *  Works with	SWI-Prolog (www.swi-prolog.org)
 *  Status	
 *  
 *  Notice	Copyright (c) 2011  University of Twente
 *  
 *  History	08/06/11  (Created)
 *  		11/06/11  (Last modified)
 */ 

/*------------------------------------------------------------
 *  Directives
 *------------------------------------------------------------*/

:- module(evaluate_cme_agent_ic,
	  [ cmap_evaluate/4	% Map x Method -> Result <- Options
	  ]).

:- use_module(load).

:- use_module(library(option), [option/2]).

:- use_module(gls(model), [gls_node_term/3, gls_edge_term/3, gls_edge_tail/3, gls_edge_head/3, gls_apply_term_set/3]).
:- use_module(specification(reference_model), [reference_model_node/2, reference_model_edge/4]).


cmap_evaluate(Map, Method, Result, Options) :-
	option(term_set(Set), Options),
	option(reference_model(Ref), Options),
	gls_apply_term_set(Map, Set, [cache(clear)]),
	evaluate(Method, Map, Ref, Result).


evaluate(simple, Map, Ref, Result) :-
	simple(Map, Ref, Result).


simple(Map, Ref, Result) :-
	findall(Node, simple_node_match(Map,Ref,Node), Nodes),
	findall(Edge, simple_edge_match(Map,Ref,Edge), Edges),
	findall(Link, simple_link_match(Map,Ref,Link), Links),
	findall(Dir, simple_dir_match(Map,Ref,Dir), Dirs),
	length(Nodes, N),
	length(Edges, E),
	length(Links, L),
	length(Dirs, D),
	Result is N + (E+L+D)/3.
	


simple_node_match(Map, Ref, Node) :-
	gls_node_term(Map, Node, Term),
	reference_model_node(Ref, Term).


simple_edge_match(Map, Ref, Edge) :-
	simple_edge_match(Map, Ref, Edge, _).

simple_edge_match(Map, Ref, Edge, l(Term1,Term2,Link)) :-
	gls_node_term(Map, Node1, Term1),
	reference_model_node(Ref, Term1),
	gls_node_term(Map, Node2, Term2),
	reference_model_node(Ref, Term2),
	Node1 \== Node2,
	(   (gls_edge_tail(Map, Edge, Node1),
	     gls_edge_head(Map, Edge, Node2))
	;   (gls_edge_tail(Map, Edge, Node2),
	     gls_edge_head(Map, Edge, Node1))
	),
	ref_mod_edge(Ref, Term1, Term2, Link).

ref_mod_edge(Ref, Term1, Term2, Link) :-
	reference_model_edge(Ref, Term1, Term2, Props),
	memberchk(link(Link), Props), !.
ref_mod_edge(Ref, Term1, Term2, Link) :-
	reference_model_edge(Ref, Term2, Term1, Props),
	memberchk(link(Link), Props).
	    
	    
simple_link_match(Map, Ref, Edge) :-
	simple_edge_match(Map, Ref, Edge, l(Term1,Term2,Link)),
	gls_edge_term(Map, Edge, Link),
	(reference_model_edge(Ref, Term1, Term2, Props)
	;reference_model_edge(Ref, Term2, Term1, Props)
	),
	memberchk(link(Link), Props).
	
	    
	    
simple_dir_match(Map, Ref, Edge) :-
	simple_edge_match(Map, Ref, Edge, l(Term1,Term2,Link)),
	gls_edge_term(Map, Edge, Link),
	reference_model_edge(Ref, Term1, Term2, Props),
	memberchk(link(Link), Props).
	
