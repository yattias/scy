/*  $Id$
 *  
 *  File	model.pl
 *  Part of	IC - educational data mining
 *  Author	Anjo Anjewierden, a.a.anjewierden@utwente.nl
 *  Purpose	API for Graph-like Structures
 *  Works with	SWI-Prolog (www.swi-prolog.org)
 *  Status	clean, checked
 *  
 *  Notice	Copyright (c) 2011  University of Twente
 *  
 *  History	13/05/11  (Created)
 *  		23/08/11  (Last modified)
 */

/*------------------------------------------------------------
 *  Directives
 *------------------------------------------------------------*/

:- module(model_gls_ic,
	  [ gls/1,			% <-> GLS
	    gls_dump/1,			% GLS

	    gls_apply_term_set/3,	% GLS x TermSet <- Options
	    gls_clear_terms/1,		% GLS

	    gls_create/2,		% GLS <- Properties
	    gls_delete/1,		% GLS
	    gls_clear/1,		% GLS		// Clear all content

	    gls_properties/2,		% GLS -> Properties
	    gls_property/2,		% GLS -> Property
	    gls_propchk/2,		% GLS -> Property
	    gls_add_property/2,		% GLS x Property
	    gls_set_property/2,		% GLS x Property
	    gls_clear_property/2,	% GLS x Property

	    gls_export/1,		% Stream
	    gls_import_term/1,		% Term

	    glss_visible/1,		% GLSs
	    glss_last_of_session/2,	% GLSs <- Options

	    cmap_to_gls/2,		% Map <- Options
	    sdm_to_gls/2,		% Model <- Options

	    gls_node/2,			% GLS -> Node
	    gls_node_label/3,		% GLS x Node -> Label
	    gls_node_term/3,		% GLS x Node -> Term
	    gls_node_term_status/3,	% GLS x Node -> Status
	    gls_node_type/3,		% GLS x Node -> Type

	    gls_node_set_term_status/3,	% GLS x Node x Status
	    gls_node_set_term/3,	% GLS x Node x Term
	    gls_node_clear_term/2,	% GLS x Node

	    gls_edge/2,			% GLS -> Edge
	    gls_edge_label/3,		% GLS x Edge -> Label
	    gls_edge_term/3,		% GLS x Edge -> Term
	    gls_edge_term_status/3,	% GLS x Edge -> Status
	    gls_edge_type/3,		% GLS x Edge -> Type
	    gls_edge_tail/3,		% GLS x Edge -> Tail
	    gls_edge_head/3,		% GLS x Edge -> Head
	    gls_edge_direction/3,	% GLS x Edge -> Direction

	    gls_edge_set_term_status/3,	% GLS x Edge x Status
	    gls_edge_set_term/3,	% GLS x Edge x Term
	    gls_edge_clear_term/2,	% GLS x Edge

	    gls_node_properties/3,	% GLS x Node -> Properties
	    gls_node_property/3,	% GLS x Node -> Property
	    gls_node_propchk/3,		% GLS x Node -> Property
	    gls_node_visuals/3,		% GLS x Node -> Visuals
	    gls_node_visual/3,		% GLS x Node -> Visual
	    gls_node_visualchk/3,	% GLS x Node -> Visual

	    gls_edge_properties/3,	% GLS x Edge -> Properties
	    gls_edge_property/3,	% GLS x Edge -> Property
	    gls_edge_propchk/3,		% GLS x Edge -> Property
	    gls_edge_visuals/3,		% GLS x Edge -> Visuals
	    gls_edge_visual/3,		% GLS x Edge -> Visual
	    gls_edge_visualchk/3	% GLS x Edge -> Visual
	  ]).

:- use_module(load).

:- use_module(library(lists), [append/3, delete/3, member/2]).
:- use_module(library(option), [option/2, option/3, select_option/3]).
:- use_module(library(gensym), [gensym/2]).

:- use_module(library('dialog/pretty_print'), [pretty_print/1]).

:- use_module(kernel(item2), [item2_propchk/2]).
:- use_module(kernel(seq), [seq_members/2]).
:- use_module(kernel(session), [session/1]).
:- use_module(specification(term_set), [term_set_match_string/4]).
:- use_module(cmap(model), [cmap_nodes/2, cmap_edges/2]).
:- use_module(sdm(model), [sdm_element/4, is_sdm_link_type/1,
			   is_sdm_node_type/1, sdm_anonymous_label/1]).


/*------------------------------------------------------------
 *  Create / delete
 *------------------------------------------------------------*/

%%	gls(+Gls:gls_handle) is semidet.
%%	gls(-Gls:gls_handle) is nonddet.
%
%	Succeeds if Gls is the handle of a graph-like structure.

gls(Gls) :-
	gls_properties(Gls, _).


%%	gls_properties(+Gls:gls_handle, -Properties:list) is semidet.
%%	gls_properties(-Gls:gls_handle, -Properties:list) is nondet.
%
%	Succeeds if Gls is the handle of a graph-like structure and Properties
%	it's list of properties.


%%	gls_create(-Gls:gls_handle, +Properties:list) is det.
%
%	Creates a Gls with the given Properties.

gls_create(Gls, Properties) :-
	gls_handle(Gls),
	assert(gls_properties(Gls,Properties)).


%%	gls_delete(+GLS:gls_handle) is det.
%
%	Deletes the given GLS.

gls_delete(G) :-
	gls(G), !,
	gls_clear(G),
	clear_cache(G).
gls_delete(_).


%%	gls_clear(+GLS:gls_handle) is semidet.
%
%	Clear the GLS by removing all nodes and edges.

gls_clear(G) :-
	gls(G),
	gls_module(G, Module),
	
	retractall(Module:node(_)),
	retractall(Module:node_label(_,_)),
	retractall(Module:node_term(_,_)),
	retractall(Module:node_term_status(_,_)),
	retractall(Module:node_type(_,_)),
	retractall(Module:node_visuals(_,_)),
	retractall(Module:node_properties(_,_)),
	retractall(Module:edge(_)),
	retractall(Module:edge_label(_,_)),
	retractall(Module:edge_term(_,_)),
	retractall(Module:edge_term_status(_,_)),
	retractall(Module:edge_type(_,_)),
	retractall(Module:edge_tail(_,_)),
	retractall(Module:edge_head(_,_)),
	retractall(Module:edge_direction(_,_)),
	retractall(Module:edge_visuals(_,_)),
	retractall(Module:edge_properties(_,_)).
	


gls_property(Gls, Prop) :-
	gls_properties(Gls, Props),
	member(Prop, Props).

gls_propchk(Gls, Prop) :-
	gls_properties(Gls, Props),
	memberchk(Prop, Props).


gls_add_property(Gls, Prop) :-
	clear_properties(Gls, Props),
	assert(gls_properties(Gls,[Prop|Props])).

gls_set_property(Gls, Prop) :-
	clear_properties(Gls, Props0),
	functor(Prop, Functor, Arity),
	functor(Temp, Functor, Arity),
	delete(Props0, Temp, Props),
	asserta(gls_properties(Gls,[Prop|Props])).

gls_clear_property(Gls, Prop) :-
	clear_properties(Gls, Props0),
	delete(Props0, Prop, Props),
	assert(gls_properties(Gls,Props)).


clear_properties(Gls, Props) :-
	gls_properties(Gls, Props),
	retractall(gls_properties(Gls,_)).
	


/*------------------------------------------------------------
 *  Apply term set to a Gls
 *------------------------------------------------------------*/

%%	gls_apply_term_set(+GLS:gls_handle, +TermSet:name, +Options:list) is semidet.
%
%	Applies TermSet to GLS.  For all nodes or edges that match on a term
%	in TermSet, gls_node_term/3 or gls_edge_term/3 will return the
%	association.  A =|term_set(Set)|= is added to the GLS.  Options are:
%
%	* cache(clear)
%	  gls_apply_term_set/3 succeeds silently if previously applied.  This
%	  option forces re-execution.

/*
gls_apply_term_set(GLS, Set, Options) :-
	gls_propchk(GLS, term_set(Set)),
	\+ option(cache(clear), Options), !.
  */
gls_apply_term_set(G, Set, Options) :-
	gls_clear_terms(G), 
	findall(N, gls_node(G,N), Nodes),
	findall(E, gls_edge(G,E), Edges),
	delete(Options, cache(clear), MatchOptions),
	nodes_term_set(Nodes, G, Set, MatchOptions),
	edges_term_set(Edges, G, Set, MatchOptions),
	gls_set_property(G, term_set(Set)).

nodes_term_set([], _, _, _).
nodes_term_set([Node|Nodes], G, Set, Options) :-
	node_term_set(Node, G, Set, Options),
	nodes_term_set(Nodes, G, Set, Options).

node_term_set(Node, G, _Set, _Options) :-
	gls_node_term_status(G, Node, anonymous), !.
node_term_set(Node, G, Set, Options) :-
	gls_node_label(G, Node, Label),
	(   term_set_match_string(Set, Label, Term, Options)
	->  gls_node_set_term(G, Node, Term),
	    gls_node_set_term_status(G, Node, term)
	;   gls_node_set_term_status(G, Node, unmatched)
	).


edges_term_set([], _, _, _).
edges_term_set([Edge|Edges], G, Set, Options) :-
	edge_term_set(Edge, G, Set, Options),
	edges_term_set(Edges, G, Set, Options).

edge_term_set(Edge, G, _, _) :-
	gls_edge_term_status(G, Edge, anonymous), !.
edge_term_set(Edge, G, Set, Options) :-
	gls_edge_label(G, Edge, Label),
	(   term_set_match_string(Set, Label, Term, Options)
	->  gls_edge_set_term(G, Edge, Term),
	    gls_edge_set_term_status(G, Edge, term)
	;   gls_edge_set_term_status(G, Edge, unmatched)
	).


gls_clear_terms(G) :-
	gls_propchk(G, term_set(_)), !,
	gls_clear_property(G, term_set(_)),
	forall(gls_node(G,Node), gls_node_clear_term(G,Node)),
	forall(gls_edge(G,Edge), gls_edge_clear_term(G,Edge)).
gls_clear_terms(_).


/*------------------------------------------------------------
 *  Export / import
 *------------------------------------------------------------*/

%%	gls_export(+Stream:stream) is det.
%
%	Export all GLS related data to Stream.

gls_export(Stream) :-
	forall(gls_properties(GLS,Properties),
	       format(Stream, 'gls_properties(~q,~q).~n', [GLS,Properties])).


%%	gls_import_term(+Term:term) is det.
%
%	Import the GLS related term.

gls_import_term(gls_properties(GLS,Properties)) :-
	assertz(gls_properties(GLS,Properties)).


/*------------------------------------------------------------
 *  Find all "visible" GLSs
 *------------------------------------------------------------*/

glss_visible(Gs) :-
	findall(G, (gls(G), \+ gls_propchk(G, invisible(true))), Gs).


glss_last_of_session(Gs, Options) :-
	memberchk(notation(Notation), Options),
	forall(gls(G), gls_set_property(G, invisible(true))),
	findall(S, session(S), Ss),
	last_only(Ss, Notation, Gs).
	

last_only([], _, []).
last_only([S|Ss], Notation, [G|Gs]) :-
	seq_members(session(S), Items0),
	reverse(Items0, Items),
	(   Notation == cmap
	->  member(Item, Items),
	    item2_propchk(Item, concept_map(G))
	;   Notation == sdm
	->  member(Item, Items),
	    item2_propchk(Item, model(G))
	), !,
	gls_set_property(G, invisible(false)),
	last_only(Ss, Notation, Gs).
last_only([_|Ss], Notation, Gs) :-
	last_only(Ss, Notation, Gs).


/*------------------------------------------------------------
 *  Storage
 *------------------------------------------------------------*/

:- dynamic
	gls_properties/2.

clear_cache(GLS) :-
	retractall(gls_properties(GLS,_)).

clear_cache_all :-
	retractall(gls_properties(_,_)).


/*------------------------------------------------------------
 *  Convert after importing
 *------------------------------------------------------------*/

post_import_hook :-
%	forall(gls_propchk(G, notation(cmap)), cmap_to_gls(G, [])),
	forall(gls_propchk(G, notation(sdm)), sdm_to_gls(G, [])).


gls_handle(GLS) :-
	var(GLS), !,
	gensym(gls_, GLS).
gls_handle(_).


/*------------------------------------------------------------
 *  Converting a concept map to a GLS
 *------------------------------------------------------------*/

%%	cmap_to_gls(+Map:gls_id, +Options:list) is det.
%
%	Convert concept map Map to the internal representation of a GLS.

cmap_to_gls(Map, _Options) :-
	gls_module(Map, Module),
	cmap_nodes(Map, Nodes),
	cmap_nodes_to_gls(Nodes, Module),
	cmap_edges(Map, Edges),
	cmap_edges_to_gls(Edges, Module).


/*------------------------------------------------------------
 *  Converting concept map nodes
 *------------------------------------------------------------*/

cmap_nodes_to_gls([], _).
cmap_nodes_to_gls([Node|Nodes], Module) :-
	select_option(id(Id), Node, Props),
	cmap_node_to_gls(Props, Id, Module),
	cmap_nodes_to_gls(Nodes, Module).

cmap_node_to_gls([], Node, Module) :-
	assert(Module:node(Node)).
cmap_node_to_gls([Prop|Props], Node, Module) :-
	(   cmap_node_prop_to_gls(Prop, Node, Module)
	->  true
	;   format('  ** cmap node property ~q ****~n', [Prop])
	),
	cmap_node_to_gls(Props, Node, Module).

cmap_node_prop_to_gls(name(Name), Node, Module) :-
	assert(Module:node_term_status(Node,unknown)),
	assert(Module:node_label(Node,Name)).
cmap_node_prop_to_gls(term(Term), Node, Module) :-
	assert(Module:node_term(Node,Term)).
cmap_node_prop_to_gls(x(X), Node, Module) :-
	add_node_visual(Node, x(X), Module).
cmap_node_prop_to_gls(y(Y), Node, Module) :-
	add_node_visual(Node, y(Y), Module).
cmap_node_prop_to_gls(w(W), Node, Module) :-
	add_node_visual(Node, w(W), Module).
cmap_node_prop_to_gls(h(H), Node, Module) :-
	add_node_visual(Node, h(H), Module).


/*------------------------------------------------------------
z *  Converting concept map edges
 *------------------------------------------------------------*/

cmap_edges_to_gls([], _).
cmap_edges_to_gls([Edge|Edges], Module) :-
	select_option(id(Id), Edge, Props),
	cmap_edge_to_gls(Props, Id, Module),
	cmap_edges_to_gls(Edges, Module).

cmap_edge_to_gls([], Edge, Module) :-
	assert(Module:edge(Edge)).
cmap_edge_to_gls([Prop|Props], Edge, Module) :-
	(   cmap_edge_prop_to_gls(Prop, Edge, Module)
	->  true
	;   format('  ** cmap edge property ~q ****~n', [Prop])
	),
	cmap_edge_to_gls(Props, Edge, Module).

cmap_edge_prop_to_gls(name(Name), Edge, Module) :-
	assert(Module:edge_term_status(Edge,unknown)),
	assert(Module:edge_label(Edge,Name)).
cmap_edge_prop_to_gls(term(Term), Edge, Module) :-
	assert(Module:edge_term(Edge,Term)).
cmap_edge_prop_to_gls(from(From), Edge, Module) :-
	assert(Module:edge_tail(Edge,From)).
cmap_edge_prop_to_gls(to(To), Edge, Module) :-
	assert(Module:edge_head(Edge,To)).


/*------------------------------------------------------------
 *  Convert SDM to a GLS
 *------------------------------------------------------------*/

%%	sdm_to_gls(Model:sdm_handle, +Options:list) is det.
%
%	Convert a System Dynamics Model to the GLS representation.
%	Options are currently unused.

sdm_to_gls(Model, _Options) :-
	gls_clear(Model),
	gls_module(Model, Module),
	findall(E, sdm_element(Model,E,_,_), Es),
	sdm_elements_to_gls(Es, Model, Module).

sdm_elements_to_gls([], _, _).
sdm_elements_to_gls([E|Es], Model, Module) :-
	sdm_element(Model, E, Type, As), !,
	sdm_element_to_gls(Type, E, As, Module),
	sdm_elements_to_gls(Es, Model, Module).

sdm_element_to_gls(Type, Node, Props, Module) :-
	is_sdm_node_type(Type), !,
	assert(Module:node(Node)),
	assert(Module:node_type(Node,Type)),
	sdm_node_props_to_gls(Props, Node, Module).
sdm_element_to_gls(Type, Edge, Props, Module) :-
	is_sdm_link_type(Type),
	assert(Module:edge(Edge)),
	assert(Module:edge_type(Edge,Type)),
	sdm_edge_props_to_gls(Props, Edge, Module).

sdm_node_props_to_gls([], _, _).
sdm_node_props_to_gls([Prop|Props], Node, Module) :-
	(   sdm_node_prop_to_gls(Prop, Node, Module)
	->  true
	;   format('  sdm_node unknown property ~w~n', [Prop])
	),
	sdm_node_props_to_gls(Props, Node, Module).

sdm_node_prop_to_gls(specification(_), _, _).		% Ignore
sdm_node_prop_to_gls(representation(_), _, _).		% Ignore
sdm_node_prop_to_gls(visible(_), _, _).			% Ignore
sdm_node_prop_to_gls(exprType(_), _, _).		% Ignore
sdm_node_prop_to_gls(extrapolation(_), _, _).		% Ignore
sdm_node_prop_to_gls(interpolation(_), _, _).		% Ignore
sdm_node_prop_to_gls(input(_), _, _).			% Ignore
sdm_node_prop_to_gls(output(_), _, _).			% Ignore
sdm_node_prop_to_gls(dataset(_), _, _).			% Ignore
sdm_node_prop_to_gls(name(Label), Node, Module) :-
	(   sdm_anonymous_label(Label)
	->  assert(Module:node_term_status(Node,anonymous))
	;   assert(Module:node_term_status(Node,unknown))
	),
	assert(Module:node_label(Node,Label)).
sdm_node_prop_to_gls(labelColor(Color), Node, Module) :-
	add_node_visual(Node, label_color(Color), Module).
sdm_node_prop_to_gls(color(Color), Node, Module) :-
	add_node_visual(Node, color(Color), Module).
sdm_node_prop_to_gls(label(Orientation), Node, Module) :-
	add_node_visual(Node, label_orientation(Orientation), Module).
sdm_node_prop_to_gls(unit(Unit), Node, Module) :-
	add_node_property(Node, unit(Unit), Module).
sdm_node_prop_to_gls(expression(Expr), Node, Module) :-
	add_node_property(Node, expression(Expr), Module).
sdm_node_prop_to_gls(inflow(Flow), Node, Module) :-
	add_node_property(Node, inflow(Flow), Module).
sdm_node_prop_to_gls(outflow(Flow), Node, Module) :-
	add_node_property(Node, outflow(Flow), Module).
sdm_node_prop_to_gls(x1(Y1), Node, Module) :-
	add_node_visual(Node, x1(Y1), Module).
sdm_node_prop_to_gls(x2(X2), Node, Module) :-
	add_node_visual(Node, x2(X2), Module).
sdm_node_prop_to_gls(y1(Y1), Node, Module) :-
	add_node_visual(Node, y1(Y1), Module).
sdm_node_prop_to_gls(y2(Y2), Node, Module) :-
	add_node_visual(Node, y2(Y2), Module).

sdm_edge_props_to_gls([], _, _).
sdm_edge_props_to_gls([Prop|Props], Edge, Module) :-
	(   sdm_edge_prop_to_gls(Prop, Edge, Module)
	->  true
	;   format('  sdm_edge unknown property ~w~n', [Prop])
	),
	sdm_edge_props_to_gls(Props, Edge, Module).


sdm_edge_prop_to_gls(start(Node), Edge, Module) :-
	assert(Module:edge_tail(Edge,Node)).
sdm_edge_prop_to_gls(end(Node), Edge, Module) :-
	assert(Module:edge_head(Edge,Node)).
sdm_edge_prop_to_gls(name(Label), Edge, Module) :-
	(   sdm_anonymous_label(Label)
	->  assert(Module:edge_term_status(Edge,anonymous))
	;   assert(Module:edge_term_status(Edge,unknown))
	),
	assert(Module:edge_label(Edge,Label)).
sdm_edge_prop_to_gls(color(Color), Edge, Module) :-
	add_edge_visual(Edge, color(Color), Module).
sdm_edge_prop_to_gls(type(Qual), Edge, Module) :-
	add_edge_property(Edge, qualitative(Qual), Module).
sdm_edge_prop_to_gls(cx1(Y1), Edge, Module) :-
	add_edge_visual(Edge, cx1(Y1), Module).
sdm_edge_prop_to_gls(cx2(X2), Edge, Module) :-
	add_edge_visual(Edge, cx2(X2), Module).
sdm_edge_prop_to_gls(cy1(Y1), Edge, Module) :-
	add_edge_visual(Edge, cy1(Y1), Module).
sdm_edge_prop_to_gls(cy2(Y2), Edge, Module) :-
	add_edge_visual(Edge, cy2(Y2), Module).
sdm_edge_prop_to_gls(x1(Y1), Edge, Module) :-
	add_edge_visual(Edge, x1(Y1), Module).
sdm_edge_prop_to_gls(x2(X2), Edge, Module) :-
	add_edge_visual(Edge, x2(X2), Module).
sdm_edge_prop_to_gls(y1(Y1), Edge, Module) :-
	add_edge_visual(Edge, y1(Y1), Module).
sdm_edge_prop_to_gls(y2(Y2), Edge, Module) :-
	add_edge_visual(Edge, y2(Y2), Module).



/*------------------------------------------------------------
 *  Visuals
 *------------------------------------------------------------*/

add_node_visual(Node, Visual, Module) :-
	(   retract(Module:node_visuals(Node,Visuals))
	->  assert(Module:node_visuals(Node,[Visual|Visuals]))
	;   assert(Module:node_visuals(Node,[Visual]))
	).

add_edge_visual(Edge, Visual, Module) :-
	(   retract(Module:edge_visuals(Edge,Visuals))
	->  assert(Module:edge_visuals(Edge,[Visual|Visuals]))
	;   assert(Module:edge_visuals(Edge,[Visual]))
	).


/*------------------------------------------------------------
 *  Properties (specific for the underlying representation)
 *------------------------------------------------------------*/

add_node_property(Node, Prop, Module) :-
	(   retract(Module:node_properties(Node,Props))
	->  assert(Module:node_properties(Node,[Prop|Props]))
	;   assert(Module:node_properties(Node,[Prop]))
	).

add_edge_property(Edge, Prop, Module) :-
	(   retract(Module:edge_properties(Edge,Props))
	->  assert(Module:edge_properties(Edge,[Prop|Props]))
	;   assert(Module:edge_properties(Edge,[Prop]))
	).


/*------------------------------------------------------------
 *  Module
 *------------------------------------------------------------*/

gls_module(GLS, GLS) :-
	nonvar(GLS),
	current_module(GLS), !.
gls_module(Module, Module) :-
	nonvar(Module),
	dynamic(Module:node/1),
	dynamic(Module:node_label/2),
	dynamic(Module:node_term/2),
	dynamic(Module:node_term_status/2),
	dynamic(Module:node_type/2),
	dynamic(Module:node_visuals/2),
	dynamic(Module:node_properties/2),

	dynamic(Module:edge/1),
	dynamic(Module:edge_label/2),
	dynamic(Module:edge_term/2),
	dynamic(Module:edge_term_status/2),
	dynamic(Module:edge_type/2),
	dynamic(Module:edge_tail/2),
	dynamic(Module:edge_head/2),
	dynamic(Module:edge_direction/2),
	dynamic(Module:edge_visuals/2),
	dynamic(Module:edge_properties/2).


/*------------------------------------------------------------
 *  Assessors
 *------------------------------------------------------------*/

gls_node(GLS, Node) :-
	gls_module(GLS, Module),
	Module:node(Node).

gls_node_label(GLS, Node, Label) :-
	gls_module(GLS, Module),
	Module:node_label(Node, Label).

gls_node_term(GLS, Node, Term) :-
	gls_module(GLS, Module),
	Module:node_term(Node, Term).


%%	gls_node_term_status(GLS:gls_handle, Node:atom, Status:atom) is det.
%
%	Status is one of =anonymous= (no label specified), =unknown= (no
%	attempt has been made to analyse the label), =unmatched= (term could
%	not be associated), =term= (term has been matched).

gls_node_term_status(G, Node, Status) :-
	gls_module(G, Module),
	Module:node_term_status(Node, Status).


gls_node_set_term_status(G, Node, Status) :-
	gls_module(G, Module),
	Module:retract(node_term_status(Node,_)),
	Module:assert(node_term_status(Node,Status)).


gls_node_set_term(GLS, Node, Term) :-
	gls_module(GLS, Module),
	retractall(Module:node_term(Node, _)),
	assert(Module:node_term(Node,Term)).

gls_node_clear_term(G, Node) :-
	gls_module(G, Module),
	retractall(Module:node_term(Node,_)),
	gls_node_term_status(G, Node, Status),
	(   Status == anonymous
	;   gls_node_set_term_status(G, Node, unknown)
	), !.

gls_node_type(GLS, Node, Type) :-
	gls_module(GLS, Module),
	Module:node_type(Node, Type).

gls_node_visuals(GLS, Node, Visuals) :-
	gls_module(GLS, Module),
	Module:node_visuals(Node, Visuals).

gls_node_visual(GLS, Node, Visual) :-
	gls_module(GLS, Module),
	Module:node_visuals(Node, Visuals),
	member(Visual, Visuals).

gls_node_visualchk(GLS, Node, Visual) :-
	gls_module(GLS, Module),
	Module:node_visuals(Node, Visuals),
	memberchk(Visual, Visuals).

gls_node_properties(GLS, Node, Props) :-
	gls_module(GLS, Module),
	Module:node_properties(Node, Props).

gls_node_property(GLS, Node, Prop) :-
	gls_module(GLS, Module),
	Module:node_properties(Node, Props),
	member(Prop, Props).

gls_node_propchk(GLS, Node, Prop) :-
	gls_module(GLS, Module),
	Module:node_properties(Node, Props),
	memberchk(Prop, Props).

gls_edge(GLS, Edge) :-
	gls_module(GLS, Module),
	Module:edge(Edge).

gls_edge_label(GLS, Edge, Label) :-
	gls_module(GLS, Module),
	Module:edge_label(Edge, Label).

gls_edge_term(GLS, Edge, Term) :-
	gls_module(GLS, Module),
	Module:node_term(Edge, Term).

%%	gls_edge_term_status(GLS:gls_handle, Edge:atom, Status:atom) is det.
%
%	Status is one of =anonymous= (no label specified), =unknown= (no
%	attempt has been made to analyse the label), =unmatched= (term could
%	not be associated), =term= (term has been matched).

gls_edge_term_status(G, Edge, Status) :-
	gls_module(G, Module),
	Module:edge_term_status(Edge, Status).


gls_edge_set_term_status(G, Edge, Status) :-
	gls_module(G, Module),
	Module:retract(edge_term_status(Edge,_)),
	Module:assert(edge_term_status(Edge,Status)).


gls_edge_set_term(GLS, Edge, Term) :-
	gls_module(GLS, Module),
	retractall(Module:edge_term(Edge, _)),
	assert(Module:edge_term(Edge,Term)).

gls_edge_clear_term(G, Edge) :-
	gls_module(G, Module),
	retractall(Module:edge_term(Edge, _)),
	(   gls_edge_term_status(G, Edge, anonymous)
	->  true
	;   gls_edge_set_term_status(G, Edge, unknown)
	).

gls_edge_type(GLS, Edge, Type) :-
	gls_module(GLS, Module),
	Module:edge_type(Edge, Type).

gls_edge_tail(GLS, Edge, Tail) :-
	gls_module(GLS, Module),
	Module:edge_tail(Edge, Tail).

gls_edge_head(GLS, Edge, Head) :-
	gls_module(GLS, Module),
	Module:edge_head(Edge, Head).

gls_edge_direction(GLS, Edge, Direction) :-
	gls_module(GLS, Module),
	Module:edge_direction(Edge,Direction).

gls_edge_visuals(GLS, Edge, Visuals) :-
	gls_module(GLS, Module),
	Module:edge_visuals(Edge, Visuals).

gls_edge_visual(GLS, Edge, Visual) :-
	gls_module(GLS, Module),
	Module:edge_visuals(Edge, Visuals),
	member(Visual, Visuals).

gls_edge_visualchk(GLS, Edge, Visual) :-
	gls_module(GLS, Module),
	Module:edge_visuals(Edge, Visuals),
	memberchk(Visual, Visuals).

gls_edge_properties(GLS, Edge, Props) :-
	gls_module(GLS, Module),
	Module:edge_properties(Edge, Props).

gls_edge_property(GLS, Edge, Prop) :-
	gls_module(GLS, Module),
	Module:edge_properties(Edge, Props),
	member(Prop, Props).

gls_edge_propchk(GLS, Edge, Prop) :-
	gls_module(GLS, Module),
	Module:edge_properties(Edge, Props),
	memberchk(Prop, Props).

/*------------------------------------------------------------
 *  Dump of a gls (for debugging)
 *------------------------------------------------------------*/

gls_dump(GLS) :-
	format('Dump of ~w~n', [GLS]),
	gls_properties(GLS, Props),
	pretty_print(Props),
	gls_module(GLS, Module),
	listing(Module:node/1),
	listing(Module:node_label/2),
	listing(Module:node_term/2),
	listing(Module:node_term_status/2),
	listing(Module:node_type/2),
	listing(Module:node_visuals/2),
	listing(Module:node_properties/2),

	listing(Module:edge/1),
	listing(Module:edge_label/2),
	listing(Module:edge_term/2),
	listing(Module:edge_term_status/2),
	listing(Module:edge_type/2),
	listing(Module:edge_tail/2),
	listing(Module:edge_head/2),
	listing(Module:edge_direction/2),
	listing(Module:edge_visuals/2),
	listing(Module:edge_properties/2).




/*------------------------------------------------------------
 *  Initialization
 *------------------------------------------------------------*/

:- initialization
	listen(clear_cache_all, clear_cache_all),
	listen(post_import_hook, post_import_hook).

