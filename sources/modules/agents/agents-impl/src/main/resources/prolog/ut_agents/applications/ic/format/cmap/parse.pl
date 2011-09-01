/*  $Id$
 *  
 *  File	parse.pl
 *  Part of	IC - educational data mining
 *  Author	Anjo Anjewierden, a.a.anjewierden@utwente.nl
 *  Purpose	Parse concept maps in SCY log files
 *  Works with	SWI-Prolog (www.swi-prolog.org)
 *  
 *  Notice	Copyright (c) 2010  University of Twente
 *  
 *  History	17/11/10  (Created)
 *  		17/06/11  (Last modified)
 */ 

/*------------------------------------------------------------
 *  Directives
 *------------------------------------------------------------*/

:- module(parse_cmap_ic,
	  [ cmap_parse/4		% XML -> Nodes x Links <- Options
	  ]).

:- use_module(load).

:- use_module(library(lists), [nth1/3]).
:- use_module(library('dialog/pretty_print'), [pretty_print/1]).

:- use_module(utilities(xml), [xml_member/2]).


/*------------------------------------------------------------
 *  Parsing
 *------------------------------------------------------------*/
	
link_entry_tag('eu.scy.scymapper.impl.model.NodeLinkModel').
node_entry_tag('eu.scy.scymapper.impl.model.NodeModel').

cmap_parse(XML, Nodes, Links, _Options) :-
	node_entry_tag(NodeModel),
	findall(Node,
		( xml_member(element(NodeModel,_,C), XML),
		  parse_node(C, Node)), Nodes),
	link_entry_tag(LinkModel),
	findall(Link,
		( xml_member(element(LinkModel,_,C), XML),
		  parse_link(C, Link, Nodes)), Links).



/*------------------------------------------------------------
 *  Parsing nodes
 *------------------------------------------------------------*/
	
parse_node([], []).
parse_node([Att|Atts], [Prop|Props]) :-
	Att = element(Tag,As,C),
	node_att_prop(Tag,As,C, Prop), !,
	parse_node(Atts, Props).
parse_node([Att|Atts], Props) :-
	Att = element(Tag,_As,_C),
	node_att_noprop(Tag), !,
	parse_node(Atts, Props).
parse_node([_Att|Atts], Props) :-
%	format('NODE att~n', []),
%	pretty_print(Att),
	parse_node(Atts, Props).

node_att_prop(label, _, C, name(Name)) :-
	(   C = [Name]
	->  true
	;   Name = ''
	).
node_att_prop(x, _, [Atom], x(X)) :-
	atom_number(Atom, X).
node_att_prop(y, _, [Atom], y(Y)) :-
	atom_number(Atom, Y).
node_att_prop(height, _, [Atom], h(H)) :-
	atom_number(Atom, H).
node_att_prop(width, _, [Atom], w(W)) :-
	atom_number(Atom, W).
node_att_prop(id, _, [Id], id(Id)).

node_att_noprop(shape).
node_att_noprop(style).
node_att_noprop(labelHidden).
node_att_noprop(deleted).
node_att_noprop(constraints).


/*------------------------------------------------------------
 *  Parsing links
 *------------------------------------------------------------*/

parse_link([], [], _).
parse_link([Att|Atts], [Prop|Props], Nodes) :-
	Att = element(Tag,As,C),
	link_att_prop(Tag,As,C, Prop, Nodes), !,
	parse_link(Atts, Props, Nodes).
parse_link([Att|Atts], Props, Nodes) :-
	Att = element(Tag,_As,_C),
	link_att_noprop(Tag), !,
	parse_link(Atts, Props, Nodes).
parse_link([_Att|Atts], Props, Nodes) :-
%	format('LINK att~n', []),
%	pretty_print(Att),
	parse_link(Atts, Props, Nodes).

link_att_prop(myLabel, _, Label, name(Name), _) :-
	(   Label = [Name]
	->  true
	;   Name = ''
	).
link_att_prop(fromNode, As, _, from(From), Nodes) :-
	memberchk(reference=Ref, As),
	ref_to_index(Ref, Index),
	nth1(Index, Nodes, Node),
	memberchk(id(From), Node).
link_att_prop(toNode, As, _, to(To), Nodes) :-
	memberchk(reference=Ref, As),
	ref_to_index(Ref, Index),
	nth1(Index, Nodes, Node),
	memberchk(id(To), Node).
link_att_prop(id, _, [Id], id(Id), _).

link_att_noprop(label).
link_att_noprop(shape).
link_att_noprop(style).
link_att_noprop(labelHidden).
link_att_noprop(deleted).
link_att_noprop(constraints).


% References to the tail and head of edges are of the form
% <a very long string>[10].  The head or tail is then the 10th node
% in the map.

ref_to_index(Ref, Index) :-
	sub_atom(Ref, _,_,A, '['),
	sub_atom(Ref, _,A,0, Tail),
	sub_atom(Tail, B,_,_, ']'),
	sub_atom(Tail, 0,B,_, Atom), !,
	atom_number(Atom, Index).
ref_to_index(_, 1).

