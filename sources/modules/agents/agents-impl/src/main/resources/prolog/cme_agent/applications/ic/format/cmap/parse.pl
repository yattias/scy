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

:- use_module(utilities(xml), [xml_member/2, xml_memberchk/2, source_to_xml/3]).


/*------------------------------------------------------------
 *  Parsing
 *------------------------------------------------------------*/
	
node_entry_tag('eu.scy.scymapper.impl.model.NodeModel').

node_shape_class('eu.scy.scymapper.impl.shapes.nodes.RoundRectangle', rectangle).
node_shape_class('eu.scy.scymapper.impl.shapes.nodes.Diamond', diamond).
node_shape_class('eu.scy.scymapper.impl.shapes.nodes.Ellipse', ellipse).
node_shape_class('eu.scy.scymapper.impl.shapes.nodes.Hexagon', hexagon).

link_entry_tag('eu.scy.scymapper.impl.model.NodeLinkModel').

link_shape_class('eu.scy.scymapper.impl.shapes.links.CubicCurvedLine', curved, none).
link_shape_class('eu.scy.scymapper.impl.shapes.links.Line', straight).
link_shape_class('eu.scy.scymapper.impl.shapes.links.Arrow', arrow).


cmap_parse(Source, Nodes, Links, Options) :-
	nb_setval(cmap_parse_options, Options),
	source_to_xml(Source, XML, [dialect(xml), space(remove)]),
	node_entry_tag(NodeModel),
	findall(Node,
		( xml_member(element(NodeModel,_,C), XML),
		  parse_node(C, Node)), Nodes),
	link_entry_tag(LinkModel),
	findall(Link,
		( xml_member(element(LinkModel,_,C), XML),
		  parse_link(C, Nodes, Link)), Links).



/*------------------------------------------------------------
 *  Parsing nodes
 *------------------------------------------------------------*/
	
parse_node(Es, Props) :-
	parse_node2(Es, Props-[]).		% Difference list

parse_node2([], L-L).
parse_node2([H|T], L1-L3) :-
	parse_node_h(H, L1-L2),
	parse_node2(T, L2-L3).

parse_node_h(element(Tag,_,_), L-L) :-
	ignore_node_tag(Tag), !.
parse_node_h(element(Tag,As,C), Props) :-
	parse_node_tag(Tag, As, C, Props), !.
parse_node_h(element(Tag,_,_), L-L) :-
	format('ignoring ~w~n', [Tag]).

parse_node_tag(label, _, C, [name(Name)|L]-L) :-
	(   C = [Name]
	;   Name = ''
	), !.
parse_node_tag(x, _, [Atom], [x(X)|L]-L) :-
	atom_number(Atom, X).
parse_node_tag(y, _, [Atom], [y(Y)|L]-L) :-
	atom_number(Atom, Y).
parse_node_tag(height, _, [Atom], [h(H)|L]-L) :-
	atom_number(Atom, H).
parse_node_tag(width, _, [Atom], [w(W)|L]-L) :-
	atom_number(Atom, W).
parse_node_tag(id, _, [Id], [id(Id)|L]-L).
parse_node_tag(shape, As, C, [shape(Shape)|L1]-L2) :-
	(   memberchk(class=Class, As)
	->  node_shape_class(Class, Shape)
	;   Shape = rectangle
	),
	parse_node2(C, L1-L2).
parse_node_tag(style, _, C, L1-L2) :-
	parse_node2(C, L1-L2).
parse_node_tag(foregroundColor, As, C, [text_colour(Colour)|L]-L) :-
	(   memberchk(reference=_, As)
	->  Colour = rgb(0,0,0)
	;   c_colour(C, Colour)
	).
parse_node_tag(backgroundColor, As, C, [shape_colour(Colour)|L]-L) :-
	(   memberchk(reference=_, As)
	->  Colour = rgb(0,0,0)
	;   c_colour(C, Colour)
	).

ignore_node_tag(labelHidden).
ignore_node_tag(deleted).
ignore_node_tag(constraints).
ignore_node_tag(mode).
ignore_node_tag(star).
ignore_node_tag(stroke).
ignore_node_tag(opaque).
ignore_node_tag(minWidth).
ignore_node_tag(minHeight).
ignore_node_tag(shadow).
ignore_node_tag(border).
ignore_node_tag(xPoints).
ignore_node_tag(yPoints).


c_colour(C, Colour) :-
	xml_memberchk(element(red,_,[Red]), C),
	xml_memberchk(element(green,_,[Green]), C),
	xml_memberchk(element(blue,_,[Blue]), C),
	atom_number(Red, R),
	atom_number(Green, G),
	atom_number(Blue, B),
	Colour = rgb(R,G,B).


/*------------------------------------------------------------
 *  Parsing links
 *------------------------------------------------------------*/

parse_link(Es, Nodes, Props) :-
	parse_link2(Es, Nodes, Props-[]).

parse_link2([], _, L-L).
parse_link2([H|T], Nodes, L1-L3) :-
	parse_link_h(H, Nodes, L1-L2),
	parse_link2(T, Nodes, L2-L3).

parse_link_h(element(Tag,_,_), _, L-L) :-
	ignore_link_tag(Tag), !.
parse_link_h(element(Tag,As,C), Nodes, Props) :-
	parse_link_tag(Tag, As, C, Nodes, Props), !.
parse_link_h(element(Tag,_,_), _, L-L) :-
	format('ignoring ~w~n', [Tag]).


parse_link_tag(myLabel, _As, C, _Nodes, [name(Name)|L]-L) :-
	(   C = [Name]
	->  true
	;   nb_getval(cmap_parse_options, Options),
	    (   memberchk(empty_link_label(Name), Options)
	    ->  true
	    ;   Name = ''
	    )
	).
parse_link_tag(fromNode, As, _, Nodes, [from(From)|L]-L) :-
	memberchk(reference=Ref, As),
	ref_to_index(Ref, Index),
	nth1(Index, Nodes, Node),
	memberchk(id(From), Node).
parse_link_tag(toNode, As, _, Nodes, [to(To)|L]-L) :-
	memberchk(reference=Ref, As),
	ref_to_index(Ref, Index),
	nth1(Index, Nodes, Node),
	memberchk(id(To), Node).
parse_link_tag(shape, As, C, _Nodes, Props) :-
	link_shape_atts(As, C, Props).
parse_link_tag(foregroundColor, As, C, _Nodes, [text_colour(Colour)|L]-L) :-
	(   memberchk(reference=_, As)
	->  Colour = rgb(0,0,0)
	;   c_colour(C, Colour)
	).
parse_link_tag(id, _, [Id], _, [id(Id)|L]-L).


link_shape_atts(As, [], [shape(Shape), direction(uni)|L]-L) :-
	memberchk(class=Ref, As),
	link_shape_class(Ref, Shape), !.
link_shape_atts(As, _, [shape(Shape), direction(none)|L]-L) :-
	memberchk(class=Ref, As),
	link_shape_class(Ref, Shape), !.
link_shape_atts(_As, C, [shape(Shape), direction(Dir)|L]-L) :-
	xml_memberchk(element(lineShape,As2,_), C),
	memberchk(class=Ref, As2),
	link_shape_class(Ref, Shape),
	(   xml_memberchk(element(tail,_,_), C)
	->  Dir = bi
	;   Dir = uni
	).


ignore_link_tag(label).
ignore_link_tag(style).
ignore_link_tag(labelHidden).
ignore_link_tag(deleted).
ignore_link_tag(constraints).
	    

	

/*
parse_link([], [], _).
parse_link([Att|Atts], [Prop|Props], Nodes) :-
	Att = element(Tag,As,C),
	link_att_prop(Tag,As,C, Prop, Nodes), !,
	parse_link(Atts, Props, Nodes).
parse_link([Att|Atts], Props, Nodes) :-
	Att = element(Tag,_As,_C),
	link_att_noprop(Tag), !,
	parse_link(Atts, Props, Nodes).
parse_link([Att|Atts], Props, Nodes) :-
%	format('LINK att~n', [Att]),
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
*/


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

