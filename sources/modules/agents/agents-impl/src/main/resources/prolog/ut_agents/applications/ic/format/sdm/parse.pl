/*  $Id$
 *  
 *  File	parse.pl
 *  Part of	IC - behavioural data mining
 *  Author	Anjo Anjewierden, a.a.anjewierden@utwente.nl
 *  Purpose	Parsing system dynamics models
 *  Works with	SWI-Prolog (www.swi-prolog.org)
 *  Status	clean, checked, pldoc
 *  
 *  Notice	Copyright (c) 2008, 2009  University of Twente
 *  
 *  History	30/06/08  (Created)
 *  		27/07/09  (Last modified)
 */

/*------------------------------------------------------------
 *  Directives
 *------------------------------------------------------------*/

:- module(parse_sdm_ic,
	  [ sdm_parse/4		% XML -> Model x Elements <- Options
	  ]).

:- use_module(load).

:- use_module(library(lists), [delete/3, select/3]).
:- use_module(library(debug), [debug/3]).
:- use_module(library(option), [option/2, option/3]).
:- use_module(library('dialog/pretty_print'), [pretty_print/1]).

:- use_module(formula, [sdm_formula_parse/2, sdm_formula_ids/3]).


/*------------------------------------------------------------
 *  Parser for XML representation of models
 *------------------------------------------------------------*/

%%	sdm_parse(+XML:xml, -Attributes:list, -Elements:list, +Options:list) is semidet.
%
%	Parses a system dynamics model in XML.  The attributes of
%	the model are stored in Attributes and the elements in Elements.  Use
%	sdm_store/4 to create an internal representation of the model.  Options
%
%	* format(Format)
%
%	  The format of the model.  Currently this can only be =clcm=, the
%	  CLCM format used by Co-Lab and SCY Dynamics.
%
%	* parse_formulas(Bool)
%
%	  If Bool is =true= attempts to parse the formulas using
%	  sdm_formula_parse/2.  The Prolog parser is used for parsing
%	  the formula and the notations are not always compatible.

sdm_parse(_XML, _Model, _Elements, Options) :-
	\+ option(format(clcm), Options), !,
	throw(error(existence_error(format,clcm),
		    context(sdm_parse/4, Options))).
sdm_parse(XML, Model, Elements, Options) :-
	option(parse_formulas(Formulas), Options, false),
	nb_setval(sdm_parse_formulas, Formulas),
	clear,
	parse(XML),				% Fills model_name/1 and element/2 structure
	extract_model_attributes(Model),	% Extract name and time information
	symbol_conversion(Elements).		% Convert symbolic names and collect elements


parse([]).
parse([element(Tag,_,C)|T]) :-
	parse_t(Tag, C), !,
	parse(T).
parse([H|T]) :-
	format('sdm_parse failed on~n', []),
	pretty_print(H),
	parse(T).


parse_t(model, C) :-
	memberchk(element(descriptor,_,Desc), C),
	memberchk(element(variables,_,Vars), C),
	(   memberchk(element(layout,_,Layout), C)
	->  true
	;   Layout = []
	),
	descriptor(Desc, Name),
	assert(model_name(Name)),
	variables(Vars),
	layout(Layout).

descriptor(C, Name) :-
	memberchk(element(symbol,_,[Name]), C), !.
descriptor(C, Name) :-
	memberchk(element(symbol,_,[]), C), !,
	debug(sdm(parse), 'sdm_parse/2: empty element name ~w', [C]),
	Name = ''.


variables([]).
variables([element(varspec,_,C)|T]) :-
	memberchk(element(variable,_,Var), C),
	memberchk(element(specification,_,Spec), C),
	variable(Var, Name),
	specification(Spec, Name),
	variables(T).


variable(C, Name) :-
	delete(C, element(descriptor,_,Desc), Rest),
	descriptor(Desc, Name),
	convert_c(Rest, Atts),
	delete(Atts, type(Type), Atts1),
	add_element(Name, [representation(Type)|Atts1]).


specification(Spec, Name) :-
	memberchk(element(Type,_,C), Spec),
	convert_c(C, Atts),
	add_element(Name, [specification(Type)|Atts]).


layout([]).
layout([element(Tag,_,C)|T]) :-
	memberchk(Tag, [nodes,links]),
	nodes_links(C),
	layout(T).


nodes_links([]).
nodes_links([element(Type,AVs,_)|T]) :-
	convert_atts(AVs, Atts),
	delete(Atts, symbol(Symbol), Atts1),
	(   \+ memberchk(id(_), Atts1)
%	->  debug(sdm(parse), 'BOGUS ELEMENT ~w: ~w~n', [Symbol,Atts1])
	->  add_element(Symbol, [element(Type),id(Symbol)|Atts1])
        ;   add_element(Symbol, [element(Type)|Atts1])
	),
	nodes_links(T).


convert_atts([], []).
convert_atts([A=V|AVs], [Term|Terms]) :-
	functor(Term, A, 1),
	(   convert_value(A, V, Value)
	->  arg(1, Term, Value)
	;   arg(1, Term, V)
	),
	convert_atts(AVs, Terms).


convert_c([], []).
convert_c([element(Type,As,_)|Es], [Term|Terms]) :-
	Type == time, !,
	functor(Term, Type, 1),
	memberchk(start=Start, As),
	memberchk(stop=Stop, As),
	arg(1, Term, [start(Start), stop(Stop)]),
	convert_c(Es, Terms).
convert_c([element(Type,As,_)|Es], [Term|Terms]) :-
	Type == integration, !,
	functor(Term, Type, 1),
	memberchk(method=Method, As),
	memberchk(step=Step, As),
	arg(1, Term, [method(Method), step(Step)]),
	convert_c(Es, Terms).
convert_c([element(Type,_,[C])|Es], [Term|Terms]) :- !,
	functor(Term, Type, 1),
	arg(1, Term, C),
	convert_c(Es, Terms).
convert_c([element(Type,[],[])|Es], [Term|Terms]) :- !,
	functor(Term, Type, 1),
	arg(1, Term, ''),
	convert_c(Es, Terms).
convert_c([H|T], Terms) :-
	format('cannot convert ~q~n', [H]),
	convert_c(T, Terms).

	
convert_value(x1, Atom, Num) :- atom_number(Atom, Num).
convert_value(y1, Atom, Num) :- atom_number(Atom, Num).
convert_value(x2, Atom, Num) :- atom_number(Atom, Num).
convert_value(y2, Atom, Num) :- atom_number(Atom, Num).
convert_value(cx1, Atom, Num) :- atom_number(Atom, Num).
convert_value(cy1, Atom, Num) :- atom_number(Atom, Num).
convert_value(cx2, Atom, Num) :- atom_number(Atom, Num).
convert_value(cy2, Atom, Num) :- atom_number(Atom, Num).


/*------------------------------------------------------------
 *  Storage
 *------------------------------------------------------------*/

:- dynamic
	model_name/1,
	element/2.	% Symbol x Atts

clear :-
	retractall(model_name(_)),
	retractall(element(_,_)).


add_element(Symbol, Atts0) :-
	(   retract(element(Symbol, Atts1))
	->  merge_atts(Atts1, Atts0, Atts),
	    assert(element(Symbol,Atts))
	;   assert(element(Symbol,Atts0))
	).


merge_atts(Given, New, All) :-
	merge_atts2(New, Given, All).

merge_atts2([], Given, All) :- !,
	Given = All.
merge_atts2([Att|Atts], Given, All) :-
	functor(Att, Func, Arity),
	functor(Tmp, Func, Arity),
	memberchk(Tmp, Given), !,
	merge_atts2(Atts, Given, All).
merge_atts2([Att|Atts], Given, All) :-
	merge_atts2(Atts, [Att|Given], All).


/*------------------------------------------------------------
 *  Extract model attributes
 *------------------------------------------------------------*/

extract_model_attributes(ModelAtts) :-
	retract(model_name(Name)),
	retract(element(time,Time)),
	memberchk(integration(Integration), Time),
	memberchk(time(TimeAtts), Time),
	memberchk(method(Method), Integration),
	memberchk(step(Step0), Integration),
	memberchk(start(Start0), TimeAtts),
	memberchk(stop(Stop0), TimeAtts),
	atom_number(Step0, Step),
	atom_number(Start0, Start),
	atom_number(Stop0, Stop),
	ModelAtts = [name(Name), method(Method), step(Step), start(Start), stop(Stop)].


/*------------------------------------------------------------
 *  Element basics
 *------------------------------------------------------------*/

:- dynamic
	symbol_id/2.

symbol_conversion(Elements) :-
	retractall(symbol_id(_,_)),
	findall(e(S,A), element(S,A), SAs),
	symbol_conversion(SAs, Tmp),
	apply_mappings(Tmp, Elements).

symbol_conversion([], []).
symbol_conversion([e(S,Atts)|SAs], [element(Id,Type,[name(S)|Atts2])|Elements]) :-
	select(id(Id), Atts, Atts1), !,		% Model contains unique id
	assert(symbol_id(S,Id)),
	extract_type(Atts1, Type, Atts2),
	symbol_conversion(SAs, Elements).
symbol_conversion([e(S,Atts)|SAs], [element(S,Type,[name(S)|Atts1])|Elements]) :-
	assert(symbol_id(S,S)),
	extract_type(Atts, Type, Atts1),
	symbol_conversion(SAs, Elements).


extract_type([], naux, []) :-
	format('parse_sdm:extract_type/3: Failed to extract type~n', []).
extract_type([element(Type)|Atts], Type, Atts) :- !.
extract_type([specification(Spec)|Atts], Type, Atts) :- !,
	(   map_spec(Spec, Type)
	->  true
	;   format('parse_sdm:extract_type/3: Unknown specification ~w~n', [Spec]),
	    Type = naux
	).
extract_type([Att|Atts], Type, [Att|More]) :-
	extract_type(Atts, Type, More).

map_spec(auxSpec, naux).
map_spec(stockSpec, nstock).
map_spec(constSpec, nconst).
map_spec(datasetSpec, ndataset).


apply_mappings([], []).
apply_mappings([element(Id,Type,Atts0)|Es], [element(Id,Type,Atts)|More]) :-
	apply_mappings2(Atts0, Atts),
	apply_mappings(Es, More).


apply_mappings2([], []).
apply_mappings2([Att0|Atts], [Att|More]) :-
	(   apply_mapping(Att0, Att)
	;   Att0 = Att
	), !,
	apply_mappings2(Atts, More).


apply_mapping(expression(Form), expression(Norm)) :-
	parse_formulas(Form, Parsed),
	findall(S=Id, symbol_id(S,Id), SIds),
	sdm_formula_ids(Parsed, SIds, Norm).
apply_mapping(initial(Form), expression(Norm)) :-
	parse_formulas(Form, Parsed),
	findall(S=Id, symbol_id(S,Id), SIds),
	sdm_formula_ids(Parsed, SIds, Norm).
apply_mapping(start(Symbol), start(Id)) :-
	symbol_id(Symbol, Id).
apply_mapping(end(Symbol), end(Id)) :-
	symbol_id(Symbol, Id).
apply_mapping(start(Symbol), start(Id)) :-
	symbol_id(Symbol, Id).
apply_mapping(inflow(Symbol), inflow(Id)) :-
	symbol_id(Symbol, Id).
apply_mapping(outflow(Symbol), outflow(Id)) :-
	symbol_id(Symbol, Id).


parse_formulas(In, In) :-
	nb_getval(sdm_parse_formulas, false), !.
parse_formulas(In, Out) :-
	sdm_formula_parse(In, Out).
