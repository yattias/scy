/*  $Id$
 *  
 *  File	reference_model.pl
 *  Part of	IC - behavioural data mining
 *  Author	Anjo Anjewierden, a.a.anjewierden@utwente.nl
 *  Purpose	Predicates related to reference models
 *  Works with	SWI-Prolog (www.swi-prolog.org)
 *  Status      check, clean, public
 *  
 *  Notice	Copyright (c) 2009, 2010, 2011  University of Twente
 *  
 *  History	29/09/09  (Created)
 *  		27/05/11  (Last modified)
 */

/*------------------------------------------------------------
 *  Directives
 *------------------------------------------------------------*/

:- module(reference_model_specification_ic,
	  [ reference_model_parse/2,		% XMLorFile x Options	// deprecated
	    reference_model_parse/3,		% XMLorFile -> RM <- Options

	    reference_model_to_graph/2,		% RM -> Graph
	    
	    reference_model_create/2,		% RM x Properties
	    reference_model_delete/1,		% RM 

	    reference_model/1,			  % RM
	    reference_model_properties/2,	% RM -> Properties
	    reference_model_property/2,		% RM -> Property
	    reference_model_propchk/2,		% RM x Property

	    reference_model_node/2,   		% RM -> Term
	    reference_model_node/3,		% RM -> Term x Attributes
	    reference_model_link/2,   		% RM -> Term
	    reference_model_link/3,		% RM -> Term x Attributes
	    reference_model_edge/3,		% RM -> From x To
	    reference_model_edge/4,		% RM -> From x To x Attributes
	    reference_model_t_edge/3,		% RM -> From x To
	    reference_model_t_edge/4,		% RM -> From x To x Attributes
	    reference_model_p_edge/3,		% RM -> From x To
	    reference_model_p_edge/4		% RM -> From x To x Attributes
	  ]).

:- use_module(load).

:- use_module(library(lists), [delete/3, select/3, member/2]).
:- use_module(library(broadcast), [listen/2]).
:- use_module(library(option), [option/2, option/3, select_option/3]).

:- use_module(utilities(xml), [xml_avs_to_terms/2, xml_memberchk/2, source_to_xml/3]).


/*------------------------------------------------------------
 *  Existence
 *------------------------------------------------------------*/

reference_model_create(RM, Props) :-
	reference_model_delete(RM),
	assert(reference_model_properties(RM,Props)).

reference_model_delete(RM) :-
	nonvar(RM),
	retractall(reference_model_properties(RM,_)),
	retractall(node(RM,_,_)),
	retractall(edge(RM,_,_,_)),
	retractall(t_edge(RM,_,_,_)),
	retractall(p_edge(RM,_,_,_)).
	

reference_model(RM) :-
	reference_model_properties(RM, _).

reference_model_property(RM, Prop) :-
	reference_model_properties(RM, Props),
	member(Prop, Props).

reference_model_propchk(RM, Prop) :-
	reference_model_properties(RM, Props),
	memberchk(Prop, Props).


/*------------------------------------------------------------
 *  Parts
 *------------------------------------------------------------*/

reference_model_node(RM, Node) :-
	node(RM, Node, _).

reference_model_node(RM, Node, Attributes) :-
	node(RM, Node, Attributes).

reference_model_link(RM, Link) :-
	link(RM, Link, _).

reference_model_link(RM, Link, Attributes) :-
	link(RM, Link, Attributes).

reference_model_edge(RM, From, To) :-
	edge(RM, From, To, _).

reference_model_edge(RM, From, To, Attributes) :-
	edge(RM, From, To, Attributes).

reference_model_t_edge(RM, From, To) :-
	t_edge(RM, From, To, _).

reference_model_t_edge(RM, From, To, Attributes) :-
	t_edge(RM, From, To, Attributes).

reference_model_p_edge(RM, From, To) :-
	p_edge(RM, From, To, _).

reference_model_p_edge(RM, From, To, Attributes) :-
	p_edge(RM, From, To, Attributes).


/*------------------------------------------------------------
 *  Convert to a graph
 *------------------------------------------------------------*/

%%	reference_model_to_graph(+Reference:any, -Graph:term) is semidet.
%
%	Converts reference model RM to a Graph representation.  Graph is of the
%	form graph(reference(RM), Nodes, Edges).

reference_model_to_graph(RM, Graph) :-
	findall(node(Name,Type),
		(reference_model_node(RM,Name,Atts),
		 memberchk(type(Type),Atts)), Nodes),
	findall(Edge,
		((reference_model_edge(RM,From,To,Atts),
		  memberchk(type(Type),Atts),
		  Edge = edge(From,To,Type))
		;(reference_model_t_edge(RM,From,To,Atts),
		  memberchk(type(Type),Atts),
		  memberchk(excluded(Excluded), Atts),
		  Edge = t_edge(From,To,Type,Excluded))
		;(reference_model_p_edge(RM,From,To,Atts),
		  memberchk(type(Type),Atts),
		  Edge = t_edge(From,To,Type))), Edges),
	Graph = graph(reference(RM), Nodes, Edges).	


/*------------------------------------------------------------
 *  Parser
 *------------------------------------------------------------*/

%%	reference_model_parse(+Source:term, -RM:name, +Options:list) is det.
%
%	Parses the reference model in the Source (file(File), atom(Atom)).

reference_model_parse(Source, Options) :-
	format('*** reference_model_parse/2 deprecated, use .../3~n', []),
	reference_model_parse(Source, _, Options).

reference_model_parse(Source, RM, Options) :-
	source_to_xml(Source, XML, [dialect(xml), space(remove)]),
	retractall(alias(_,_)),
	(   option(reference_model(RM), Options)
	;   xml_memberchk(element(reference_model,As,_), XML),
	    memberchk(name=RM, As)
	), !,
	reference_model_delete(RM),
	parse(XML, RM, unknown).

parse([], _, _).
parse([H|T], RM, Rep) :-
	parse_h(H, RM, Rep),
	parse(T, RM, Rep).


parse_h(element(T,As,C), RM, Rep) :-
	parse_t(T,As,C, RM, Rep), !.
parse_h(Unk, _, _) :-			% Ignore everything else
	format('reference_model_parse/3: not recognised~n  ~w~n', [Unk]).

parse_t(reference_model, As, C, RM, _) :-
	xml_avs_to_terms(As, Props),
	assert(reference_model_properties(RM,Props)),
	option(notation(Rep), Props, sdm),
	parse(C, RM, Rep).
parse_t(nodes, _, C, RM, Rep) :- !,
	parse(C, RM, Rep).
parse_t(edges, _, C, RM, Rep) :- !,
	parse(C, RM, Rep).
parse_t(links, _, C, RM, Rep) :- !,
	parse(C, RM, Rep).
parse_t(node, As, _, RM, Rep) :-
	(   Rep == sdm
	->  select(type=Type0, As, As1),
	    delete(As1, id=Id, As2),
	    delete(As2, concept=Concept, As3), !,
	    (   (var(Id); var(Concept))
	    ->  Id = Concept
	    ;   true
	    ),
	    convert_type(Type0, Type),
	    xml_avs_to_terms(As3, Atts),
	    assert(node(RM,Concept,[type(Type)|Atts])),
	    assert_alias(Concept, Concept),
	    assert_alias(Id, Concept)
	;   Rep == cmap
	->  select_option(term(Term), As, As1),
	    xml_avs_to_terms(As1, Atts),
	    assert(node(RM,Term,Atts))
	).
parse_t(link, As, _, RM, Rep) :-
	(   Rep == cmap
	->  select_option(term(Term), As, As1),
	    xml_avs_to_terms(As1, Atts),
	    assert(link(RM,Term,Atts))
	;   format('rm_parse: no link for ~w~n', [Rep])
	).
parse_t(edge, As, _, RM, Rep) :-
	(   Rep == sdm
	->  select(tail=From0, As, As1),
	    select(head=To0, As1, As2),
	    (   select(type=Type, As2, Atts0)
	    ->  true
	    ;   Atts0 = As2,
		Type = link
	    ), !,
	    xml_avs_to_terms(Atts0, Atts),
	    alias(From0, From),
	    alias(To0, To),
	    assert(edge(RM,From,To,[type(Type)|Atts]))
	;   Rep == cmap
	->  select_option(tail(Tail), As, As1),
	    select_option(head(Head), As1, As2),
	    xml_avs_to_terms(As2, Atts),
	    assert(edge(RM,Tail,Head,Atts))
	).
parse_t(t_edge, As, _, RM, Rep) :-
	(   Rep == sdm
	->  select(head=From0, As, As1),
	    select(tail=To0, As1, As2),
	    select(excluded=Excluded0, As2, As3), !,
	    convert_excluded(Excluded0, Excluded),
	    delete(As3, type=Type, Atts0),
	    xml_avs_to_terms(Atts0, Atts),
	    (   var(Type)
	    ->  Type = link
	    ;   true
	    ),
	    alias(From0, From),
	    alias(To0, To),
	    assert(t_edge(RM,From,To,[type(Type),excluded(Excluded)|Atts]))
	;   format('rm_parse; no t_edge for ~w~n', [Rep])
	).
parse_t(p_edge, As, _, RM, Rep) :-
	(   Rep == sdm
	->  select(head=From0, As, As1),
	    select(tail=To0, As1, As2),
	    delete(As2, type=Type, Atts0),
	    xml_avs_to_terms(Atts0, Atts),
	    (   var(Type)
	    ->  Type = link
	    ;   true
	    ),
	    alias(From0, From),
	    alias(To0, To),
	    assert(p_edge(RM,From,To,[type(Type)|Atts]))
	;   format('rm_parse; no t_edge for ~w~n', [Rep])
	).
	

convert_type(In, Out) :-
	atomic_list_concat(Parts, ';', In),
	findall(T, ( member(P,Parts),
		     normalize_space(atom(T),P)), Ts),
	(   Ts = [Out]
	->  true
	;   Out =.. [or | Ts]
	).


convert_excluded(In, Out) :-
	atomic_list_concat(Parts, ', ', In),
	excluded_parts(Parts, Out).

excluded_parts([], []).
excluded_parts([P|Parts], [A|As]) :-
	normalize_space(atom(T), P),
	alias(T, A),
	excluded_parts(Parts, As).


/*------------------------------------------------------------
 *  Storage
 *------------------------------------------------------------*/

:- dynamic
	alias/2,			% Alias x Concept
	reference_model_properties/2,	% RM x Props
	node/3,				% RM x Term x Props
	link/3,				% RM x Term x Props
	edge/4,				% RM x From x To x Props
	t_edge/4,			% RM x From x To x Props
	p_edge/4.			% RM x From x To x Props

assert_alias(Alias, Concept) :-
	alias(Alias, Concept), !.
assert_alias(Alias, Concept) :-
	assert(alias(Alias,Concept)).


clear_cache :-
	reference_model(RM),
	reference_model_delete(RM),
	fail.
clear_cache.


:- initialization
	listen(clear_cache_all, clear_cache).
