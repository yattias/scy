/*  $Id$
 *  
 *  File	drag_and_drop.pl
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

:- module(drag_and_drop_specification_ic,
	  [ drag_and_drop_evaluation/3	% GLS -> Evaluation <- Options
	  ]).

:- use_module(load).

:- use_module(library(option), [option/2]).

:- use_module(gls(model), [gls_node_label/3, gls_node_term/3,
			   gls_apply_term_set/3, gls_node/2,
			   gls_node_visualchk/3, gls_node_propchk/3,
			   gls_node_replace_by/3, gls_node_delete/2,
			   gls_edge_head/3, gls_edge_tail/3]).
:- use_module(gls(evaluation), [gls_node_perfect_match/3]).
:- use_module(specification(reference_model), [reference_model_propchk/2,
			   reference_model_node_propchk/3, reference_model_edge/3]). 


drag_and_drop_evaluation(G, Evaluation, Options) :-
	option(term_set(TS), Options),
	option(reference_model(RM), Options),
	gls_apply_term_set(G, TS, []),
	findall(N, gls_node(G,N), Nodes),
	findall(D, ( gls_node(G,D),
		     gls_node_term(G, D, Term),
		     reference_model_node_propchk(RM, Term, drop(true))), Drops),
	empty_node_areas(Nodes, G, NAs),
	find_drops(Drops, NAs, G, Dropped, NotDropped),
	correctly_dropped(Dropped, G, RM, Correct, Wrong),
	length(Dropped, Len1),
	length(NotDropped, Len2),
	Evaluation = [ dropped(Len1),
		       not_dropped(Len2),
		       correct_drop(Correct),
		       wrong_drop(Wrong)
		     ].


/*------------------------------------------------------------
 *  Node checking and area storage
 *------------------------------------------------------------*/

empty_node_areas([], _, []).
empty_node_areas([Node|Nodes], G, [na(Node,Area)|NAs]) :-
	gls_node_label(G, Node, Label),
	Label == '', !,
	node_area(G, Node, Area),
	empty_node_areas(Nodes, G, NAs).
empty_node_areas([_|Nodes], G, NAs) :-
	empty_node_areas(Nodes, G, NAs).


/*------------------------------------------------------------
 *  Finding dropped nodes overlapping an empty node
 *------------------------------------------------------------*/

find_drops([], _,_, [], []).
find_drops([Node|Nodes], NAs, G, [Node|Dropped], NotDropped) :-
	node_area(G, Node, Area),
	node_dropped(NAs, Node,Area, G, RestNAs), !,
	find_drops(Nodes, RestNAs, G, Dropped, NotDropped).
find_drops([Node|Nodes], NAs, G, Dropped, [Node|NotDropped]) :-
	find_drops(Nodes, NAs, G, Dropped, NotDropped).

node_dropped([na(Node2,Area2)|NAs], Node, Area1, G, NAs) :-
	intersection_area(Area1, Area2, _Int, Ratio),
	Ratio > 0.5, !,
	gls_node_replace_by(G, Node2, Node),
	gls_node_delete(G, Node2).
node_dropped([NA|NAs], Node,Area, G, [NA|Rest]) :-
	node_dropped(NAs, Node,Area, G, Rest).


/*------------------------------------------------------------
 *  Correctly dropped
 *------------------------------------------------------------*/

correctly_dropped([], _, _, [], []).
correctly_dropped([Node|Nodes], G, RM, [Node|Correct], Wrong) :-
	gls_node_perfect_match(G, Node, RM), !,
	correctly_dropped(Nodes, G, RM, Correct, Wrong).
correctly_dropped([Node|Nodes], G, RM, Correct, [Node|Wrong]) :-
	correctly_dropped(Nodes, G, RM, Correct, Wrong).


/*------------------------------------------------------------
 *  Visible area of a node
 *------------------------------------------------------------*/

node_area(G, Node, area(X,Y,W,H)) :-
	gls_node_visualchk(G, Node, x(X)),
	gls_node_visualchk(G, Node, y(Y)),
	gls_node_visualchk(G, Node, w(W)),
	gls_node_visualchk(G, Node, h(H)).

/*------------------------------------------------------------
 *  Intersection of two areas (trying to avoid use of XPCE)
 *------------------------------------------------------------*/

intersection_area(area(X1,Y1,W1,H1), area(X2,Y2,W2,H2), Int, Ratio) :-
	Smallest is min(W1*H1, W2*H2),
	(X1 > X2 -> X = X1; X = X2),
	(Y1 > Y2 -> Y = Y1; Y = Y2),
	XW1 is X1 + W1,
	XW2 is X2 + W2,
	(XW1 < XW2 -> W is XW1 - X; W is XW2 - X),
	YH1 is Y1 + H1,
	YH2 is Y2 + H2,
	(YH1 < YH2 -> H is YH1 - Y; H is YH2 - Y),
	(   (W < 0; H < 0)
	->  Int = 0, Ratio = 0
	;   Int is W * H,
	    Ratio is Int / Smallest
	).
