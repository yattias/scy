/*  $Id$
 *
 *  File	model.pl
 *  Part of	IC - educational data mining
 *  Author	Anjo Anjewierden, a.a.anjewierden@utwente.nl
 *  Purpose	Representing system dynamics models
 *  Works with	SWI-Prolog (www.swi-prolog.org)
 *  Status	clean, checked, pldoc
 *
 *  Notice	Copyright (c) 2008, 2009, 2010, 2011  University of Twente
 *
 *  History	30/06/08  (Created)
 *  		19/04/11  (Last modified)
 */

/*------------------------------------------------------------
 *  Directives
 *------------------------------------------------------------*/

:- module(model_sdm_ic,
	  [ sdm/1,				% Model
	    sdm_properties/2,			% Model -> Properties
	    sdm_property/2,			% Model x Property
	    sdm_propchk/2,			% Model x Property
	    sdm_store/4,			% XML x Atts x Elements <-> Model
	    sdm_duplicate/2,			% Model -> Model
	    sdm_element/4,			% Model x Symbol x Type x Atts
	    sdm_element_by_name/3,		% Model x Label -> Id
	    sdm_element_by_name/5,		% Model x Label -> Id x Type x Atts
	    sdm_element_label/3,		% Model x Symbol -> Label
	    sdm_element_label/4,		% Model x Symbol x Mode -> Label
	    sdm_element_assign_concept/3,	% Model x Symbol x Concept
	    sdm_element_concept/3,		% Model x Symbol -> Concept
	    sdm_element_deleted_label/4,	% Model x Symbol x Mode -> Label
	    sdm_element_expression/4,		% Model x Symbol x Expr x Unit
	    sdm_edge/4,				% Model x From x To -> Symbol
	    sdm_element_type/3,			% Model x Symbol -> Type (for flows)

	    sdm_concept_set/3,			% Model x ConceptSet <- Options

	    sdm_export/1,			% Stream
	    sdm_import_term/1,			% Term

	    sdm_from_xml/2, 			% XML -> Model
	    sdm_from_xml_atom/2, 		% Atom -> Model

	    sdm_xml/2,				% Model -> XML
	    sdm_clcm_import/2,			% File -> Model
	    sdm_clcm_format/3,			% Model x Session -> XMLinCLCM

	    sdm_bounding_box/2,			% Model -> area(X,Y,W,H)
	    sdm_session/2,			% Model -> Session
	    sdm_session_last/2,			% Model -> Session

	    sdm_to_network/3,			% Model -> Network <- Options
	    sdm_to_graph/3,			% Model -> Graph <- Options

	    sdm_metrics/2,			% Model <-> Metrics

	    sdm_internally_consistent/1,	% Model

	    sdm_rename_element/3,		% Model x OldName x NewName
	    sdm_delete_element/2,		% Model x Symbol
	    sdm_delete/1,			% Model

	    is_sdm_signature_model/2,		% Signature -> Model

	    is_sdm_node_type/1,			% Type
	    is_sdm_link_type/1,			% Type
	    is_sdm_nondisplay_type/1,		% Type

	    sdm_element_anonymous/3,		% Model x Symbol -> Anon
	    sdm_anonymous_label/1		% Label
	  ]).


:- use_module(load).

:- use_module(library(apply), [maplist/3]).
:- use_module(library(debug), [debug/3]).
:- use_module(library(gensym), [gensym/2]).
:- use_module(library(option), [option/2, option/3]).
:- use_module(library(sgml), [load_structure/3]).
:- use_module(library(lists), [append/3, delete/3, list_to_set/2, max_list/2, member/2, min_list/2, select/3]).
:- use_module(library('dialog/pretty_print'), []).

:- use_module(formula, [sdm_formula_rename/4, sdm_formula_parse/2, sdm_formula_ids/3]).
:- use_module(parse, [sdm_parse/4]).

:- use_module(kernel(item2), [item2_type/2, item2_model/2, item2_propchk/2, item2_add_property/2]).
:- use_module(sort(compound_terms), [compound_terms_sort/3]).
:- use_module(utilities(xml), [atom_to_xml/3, xml_memberchk/2]).
:- use_module(kernel(seq), [seq_members/2]).
:- use_module(kernel(signature), [item2_signature_raw/2, item2_set_signature/2]).
:- use_module(specification(concept_set), [concept_set_match_concept/3]).

:- use_module(gls(model), [gls_create/2, sdm_to_gls/2]).


/**	<module> Representation of system dynamics models

These predicates manipulate the representation of system dynamics models.
Some of the code is related to the old representation in Co-Lab and correcting
for errors in that representation.  In general, we assume that the new Co-Lab
and the equivalent SCY Dynamics representation will be used.

@author	Anjo Anjewierden
*/


%%	sdm_store(+XML:xml, +Atts:list, +Elements:list, -Model:int) is det.
%
%	Create Model from sdm_parse/4.

sdm_store(XML, ModelProps, Elements, Model) :-
	gls_create(Model, [notation(sdm)]),
	assert(sdm_properties(Model,ModelProps)),
	assert(sdm_xml(Model,XML)),
	store_elements(Elements, Model),
	sdm_internally_consistent(Model),
	sdm_to_gls(Model, []).

store_elements([], _).
store_elements([element(Id,Type0,Atts)|Elements], Model) :-
	sdm_symbol_type(Type0, Type),
	(   Type == flow,	% If a flow has no start or end, it is in the
				% XML of the model, but not shown on the
				% screen.  We delete such "empty flows".
	    \+ memberchk(start(_), Atts),
	    \+ memberchk(end(_), Atts)
	->  true
	;   Type == relation
	->  delete(Atts, type(RelType0), Atts1),
	    type_to_relation(RelType0, RelType),
	    assert_sdm_element(Model,Id,Type,[type(RelType)|Atts1])
	;   assert_sdm_element(Model,Id,Type,Atts)
	),
	store_elements(Elements, Model).

sdm_symbol_type(lflow, flow).
sdm_symbol_type(lrelation, relation).
sdm_symbol_type(naux, auxiliary).
sdm_symbol_type(nconst, constant).
sdm_symbol_type(ndataset, dataset).
sdm_symbol_type(nstock, stock).

type_to_relation('0', unspecified).
type_to_relation('1', linear).
type_to_relation('2', inverse).
type_to_relation('3', negative_linear).
type_to_relation('4', exponential).
type_to_relation('5', asymptotical).
type_to_relation('6', unspecified).


sdm_internally_consistent(Model) :-
	findall(sa(S,As), sdm_element(Model,S,_,As), SAs),
	maplist(arg(1), SAs, Symbols),
	sdm_internally_consistent(SAs, Symbols, Model).

sdm_internally_consistent([], _, _).
sdm_internally_consistent([sa(S,As)|SAs], Symbols, Model) :-
	consistent_as(As, Symbols, S, Model),
	sdm_internally_consistent(SAs, Symbols, Model).

consistent_as([], _, _, _).
consistent_as([A|As], Symbols, S, Model) :-
	consistent_a(A, Symbols, S, Model), !,
	consistent_as(As, Symbols, S, Model).
consistent_as([_|As], Symbols, S, Model) :-
	consistent_as(As, Symbols, S, Model).

consistent_a(start(Start), Symbols, S, Model) :-
	(   memberchk(Start, Symbols)
	->  true
	;   debug(sdm(model), 'Inconsistent ~w / ~w: ~w~n', [Model,S,start(Start)])
	).
consistent_a(end(End), Symbols, S, Model) :-
	(   memberchk(End, Symbols)
	->  true
	;   debug(sdm(model), 'Inconsistent ~w / ~w: ~w~n', [Model,S,end(End)])
	).


/*------------------------------------------------------------
 *  Storage
 *------------------------------------------------------------*/

%%	sdm(+Model:name) is semidet.
%
%	Succeeds if Model is the identifier of a system dynamics model.

sdm(Model) :-
	sdm_properties(Model, _).


%%	sdm_properties(+Model:atom, -Properties:list) is det.
%
%	Succeeds if Model is the identifier of a system dynamics model and
%	Properties is a list of properties related to the model.  The
%	property name(Name) is always present.

%%	sdm_property(+Model:atom, -Property:term) is nondet.
%%	sdm_propchk(+Model:atom, -Property:term) is semidet.
%
%	Succeeds if Model is the identifier of a system dynamics model and
%	Property is a property of the model.  An example property is file(File).

sdm_property(Model, Property) :-
	sdm_properties(Model, Props),
	member(Property, Props).

sdm_propchk(Model, Property) :-
	sdm_properties(Model, Props),
	memberchk(Property, Props).


%%	sdm_element(+Model:atom, -Symbol:atom, -Type:atom, -Atts:list) is nondet
%
%	Succeeds if Model is the identifier of a system dynamics model, Symbol a named
%	object of Type in the model and it has attributes Atts.

%%	sdm_xml(+Model:atom, -XML:xml) is nondet.
%
%	Succeeds if Model is the identifier of a system dynamics model and XML the
%	corresponding XML representation.

:- dynamic
	sdm_properties/2,	% Model -> Attributes
	sdm_xml/2,		% Model -> XML
	sdm_element/4.		% Model x Symbol x Type x Atts


/*------------------------------------------------------------
 *  Export / import
 *------------------------------------------------------------*/

%%	sdm_export(+Stream:stream) is det.
%
%	Export all SDM related data to Stream.

sdm_export(Stream) :-
	forall(sdm_properties(Model,Properties),
	       format(Stream, 'sdm_properties(~q,~q).~n', [Model,Properties])),
	forall(sdm_xml(Model,XML),
	       format(Stream, 'sdm_xml(~q,~q).~n', [Model,XML])),
	forall(sdm_element(Model,Symbol,Type,Atts),
	       format(Stream, 'sdm_element(~q,~q,~q,~q).~n', [Model,Symbol,Type,Atts])).


%%	sdm_import_term(+Term:term) is det.
%
%	Import the SDM related term.

sdm_import_term(sdm_properties(Model,Properties)) :-
	assertz(sdm_properties(Model,Properties)).
sdm_import_term(sdm_xml(Model,XML)) :-
	assertz(sdm_xml(Model,XML)).
sdm_import_term(sdm_element(Model,Symbol,Type,Atts)) :-
	assertz(sdm_element(Model,Symbol,Type,Atts)).



%%	sdm_duplicate(+Model:sdm_id, -Copy:sdm_id) is det.
%
%	Copy is a copy of Model.

sdm_duplicate(Model, NewModel) :-
	gls_create(Model, [notation(sdm)]),
	sdm_properties(Model, Props),
	assert(sdm_properties(NewModel,Props)),
	sdm_xml(Model, XML),
	assert(sdm_properties(NewModel,XML)),
	forall(sdm_element(Model,Symbol,Type,Atts),
	       assert(sdm_element(NewModel,Symbol,Type,Atts))).


%%	sdm_clcm_import(+File:file, -Model:int) is det.
%
%	Loads File, which is assumed to contain an SD model in CLCM
%	format and convert it to the internal SDM representation.  The
%	identifier of the resulting model is returned.

sdm_clcm_import(File, Model) :-
	absolute_file_name(File, AbsFile),
	load_structure(AbsFile, XML, [dialect(xml), space(remove)]),
	xml_memberchk(element(model,As,C), XML),
	sdm_parse([element(model,As,C)], Atts, Elements, [format(clcm)]),
	file_base_name(AbsFile, Base),
	sdm_store([element(model,As,C)], [file(Base)|Atts], Elements, Model).


%%	sdm_clcm_format(+Model:name, +Session:name, -CLCM:xml) is det.
%
%	Exports Model in Session to CLCM format.  This format can be loaded
%	in Co-Lab (and possibly SCY Dynamics).

sdm_clcm_format(Model, Session, CLCM) :-
	atomic_list_concat(Parts, ' ', Session),
	atomic_list_concat(Parts, '%20', Printed),
	atom_concat('file://dc-appl/appl/Colab/', Printed, File),
	sdm_xml(Model, XML),
	CLCM =
	[ element(ciel, [xmlns='http://collide.info/ciel/schema'],
		  [ element(cieldata, [],
                    [ element('ims:lom', ['xmlns:ims'='http://www.imsglobal.org/xsd/imsmd_v1p2'],
                              [ element('ims:general', [],
                                        [ element('ims:identifier', [], [File]),
                                          element('ims:title', [],
                                                  [ element('ims:langstring',
                                                            [ 'xml:lang' = nl
                                                            ],
                                                            [ Session ])
                                                  ]),
                                          element('ims:language',
                                                  [],
                                                  [ nl
                                                  ]),
                                          element('ims:keyword',
                                                  [],
                                                  [ element('ims:langstring',
                                                            [],
                                                            [ 'colab:room:5'
                                                            ])
                                                  ]),
                                          element('ims:keyword',
                                                  [],
                                                  [ element('ims:langstring',
                                                            [],
                                                            [ 'colab:floor:lightCapacitorCondition3'
                                                            ])
                                                  ])
                                        ]),
                                element('ims:technical',
                                        [],
                                        [ element('ims:format',
                                                  [],
                                                  [ 'ciel/colabModel'
                                                  ])
                                        ])
                              ]),
		      element(data, [],
			      [ element(anyFormat, [], XML)])
		    ])])].


clear_cache_all :-
	retractall(sdm_properties(_,_)),
	retractall(sdm_xml(_,_)),
	retractall(sdm_element(_,_,_,_)).

assert_sdm_element(Model, Symbol, Type, Atts) :-
	assert(sdm_element(Model,Symbol,Type,Atts)).

retract_sdm_element(Model, Symbol, Type, Atts) :-
	retract(sdm_element(Model,Symbol,Type,Atts)).


:- initialization
	listen(clear_cache_all, clear_cache_all),
	clear_cache_all.



sdm_element_expression(Model, Symbol, Expression, Unit) :-
	sdm_element(Model, Symbol, _, Atts),
	(memberchk(expression(Expression), Atts); memberchk(initial(Expression), Atts)),
	memberchk(unit(Unit), Atts).

sdm_element_set_expression(Model, Symbol, Formula) :-
	retract_sdm_element(Model, Symbol, Type, Atts),
	(   memberchk(expression(_), Atts)
	->  delete(Atts, expression(_), Atts1),
	    assert_sdm_element(Model, Symbol, Type, [expression(Formula)|Atts1])
	;   memberchk(initial(_), Atts)
	->  delete(Atts, initial(_), Atts1),
	    assert_sdm_element(Model, Symbol, Type, [initial(Formula)|Atts1])
	).


sdm_element_type(Model, Symbol, Type) :-
	sdm_element(Model, Symbol, Type0, Atts),
	(   Type0 == flow
	->  (   memberchk(start(_), Atts)
	    ->  Type = outflow
	    ;   memberchk(end(_), Atts)
	    ->  Type = inflow
	    ;   Type = flow
	    )
	;   Type0 = Type
	).


sdm_edge(Model, From, To, Symbol) :-
	sdm_element(Model, Symbol, _, Atts),
	memberchk(start(From), Atts),
	memberchk(end(To), Atts).


%%	sdm_from_xml_atom(+Atom:atom, -Model:int) is det
%
%	Extract a system dynamics model as XML from an Atom (as found in the
%	log file) and generate an internal model structure as Model.  The XML
%	is available through sdm_xml/2.

sdm_from_xml(XML, Model) :-
	convert(XML, ModelSpec),
	store(ModelSpec, XML, Model),
	normalise_model(Model).

sdm_from_xml_atom(Atom, Model) :-
	atom_to_xml(Atom, XML, [space(remove)]),
	sdm_from_xml(XML, Model).


%%	sdm_bounding_box(+Model:atom, -Area:term) is det
%
%	Returns the bounding box of Model in Area (area/4 as in PCE).
%
%	@tbd Take into account width of labels.

sdm_bounding_box(Model, area(X,Y,W,H)) :-
	findall(X,
		( sdm_element(Model, _, _, Atts),
		  (memberchk(x1(X), Atts);
		   memberchk(x2(X), Atts);
		   memberchk(cx1(X), Atts);
		   memberchk(cx2(X), Atts))), Xs),
	findall(Y,
		( sdm_element(Model, _, _, Atts),
		  (memberchk(y1(Y), Atts);
		   memberchk(y2(Y), Atts);
		   memberchk(cy1(Y), Atts);
		   memberchk(cy2(Y), Atts))), Ys),
	max_list(Xs, MaxX),
	min_list(Xs, X),
	max_list(Ys, MaxY),
	min_list(Ys, Y),
	W is MaxX-X,
	H is MaxY-Y.


%%	sdm_element_by_label(+Model:atom, +Name:atom, -Id:atom) is det
%
%	Name is a model element with identifier Id in Model with the given Type and Atts.

sdm_element_by_name(Model, Name, Id) :-
	sdm_element(Model, Id, _, Atts),
	memberchk(name(Name), Atts), !.
sdm_element_by_name(Model, Name, Id, Type, Atts) :-
	sdm_element(Model, Id, Type, Atts),
	memberchk(name(Name), Atts), !.


sdm_element_label(Model, Symbol, Label) :-
	sdm_element(Model, Symbol, _, Atts),
	memberchk(name(Label), Atts).


sdm_element_label(Model, Symbol, Mode, Label) :-
	(   sdm_element(Model, Symbol, _, Atts)
	->  (   Mode == concept
	    ->  (   memberchk(concept(Label), Atts)
		->  true
		;   sdm_element_label(Model, Symbol, name, Label)
		)
	    ;   Mode == name
	    ->  (   memberchk(name(Label), Atts)
		->  true
		;   sdm_element_label(Model, Symbol, id, Label)
		)
	    ;   Label = Symbol
	    )
	;   debug(sanity, 'sdm_element_label/4: Symbol ~w not found in model ~w~n', [Symbol,Model]),
	    Label = Symbol
	).

sdm_element_concept(Model, Symbol, Concept) :-
	sdm_element(Model, Symbol, _, Atts),
	memberchk(concept(Concept), Atts).

sdm_element_assign_concept(Model, Symbol, Concept) :-
	retract_sdm_element(Model, Symbol, Type, Atts), !,
	delete(Atts, concept(_), Atts1),
	assert_sdm_element(Model, Symbol, Type, [concept(Concept)|Atts1]).


sdm_element_deleted_label(Model, Symbol, Mode, Label) :-
	sdm_element_label(Model, Symbol, Mode, Label), !.
sdm_element_deleted_label(Model, Symbol, Mode, Label) :-
	sdm_element(Prev, Symbol, _, Atts), !,
	Prev \== Model,
	(   Mode == concept
	->  (   memberchk(concept(Label), Atts)
	    ->  true
	    ;   sdm_element_deleted_label(Model, Symbol, name, Label)
	    )
	;   Mode == name
	->  (   memberchk(name(Label), Atts)
	    ->  true
	    ;   sdm_element_deleted_label(Model, Symbol, id, Label)
	    )
	;   Label = Symbol
	).
sdm_element_deleted_label(_, Label, _, Label).


/*------------------------------------------------------------
 *  Normalise all names in the model
 *------------------------------------------------------------*/

%%	sdm_concept_set(+Model:atom, +ConceptSet:atom, +Options:list) is det.
%
%	Normalises the name of the elements of Model according to ConceptSet.
%	The normalised name is stored as concept(Name) for each element.
%	If a model has been normalised with the same concept set previously,
%	this predicate is a no-op.  Use sdm_denormalise/1 to remove the
%	normalisation if the concept set or model has been modified.
%
%	* matches(Mode)
%	  If Mode is =single= (default) then a concept can only match on one
%	  model element, otherwise a concept can match =multiple+ elements.
%
%	* cache(clear)
%	  When this option is provided sdm_denormalise/1 removes the
%	  previous matches between names and concepts.

sdm_concept_set(Model, ConceptSet, Options) :-
	option(matches(Mode), Options, single),
	(   option(cache(clear), Options)
	->  sdm_denormalise(Model)
	;   true
	),
	sdm_properties(Model, Props),
	(   select(concept_set(CS), Props, OldProps)
	->  (   CS == ConceptSet
	    ->  true
	    ;   sdm_denormalise(Model),
		(   Mode == multiple
		->  sdm_concept_set_multiple(Model, ConceptSet)
		;   sdm_concept_set_single(Model, ConceptSet)
		),
		retract(sdm_properties(Model,_)),
		assert(sdm_properties(Model,[concept_set(ConceptSet)|OldProps]))
	    )
	;   (   Mode == multiple
	    ->  sdm_concept_set_multiple(Model, ConceptSet)
	    ;   sdm_concept_set_single(Model, ConceptSet)
	    ),
	    retract(sdm_properties(Model,Props)),
	    assert(sdm_properties(Model,[concept_set(ConceptSet)|Props]))
	), !.

sdm_concept_set_multiple(Model, ConceptSet) :-
	findall(snt(Symbol,Name,Type),
		(sdm_element(Model,Symbol,Type,As),
		 memberchk(name(Name), As),
		 \+ memberchk(concept(_), As),
		 \+ sdm_anonymous_label(Name)), SNTs),
	concept_set_normalise(SNTs, Model, ConceptSet).

concept_set_normalise([], _,_).
concept_set_normalise([snt(Symbol,Name,Type)|SNTs], Model, Set) :-
	concept_set_match_concept(Name, Concept, [concept_set(Set), type(Type)]), !,
	retract_sdm_element(Model,Symbol,_,Atts),
	assert_sdm_element(Model,Symbol,Type,[concept(Concept)|Atts]),
	concept_set_normalise(SNTs, Model, Set).
concept_set_normalise([_|NTs], Model, Set) :-
	concept_set_normalise(NTs, Model, Set).


sdm_denormalise(Model) :-
	forall(retract_sdm_element(Model,Symbol,Type,Atts),
	       ( delete(Atts, concept(_), NewAtts),
		 assert_sdm_element(Model,Symbol,Type,NewAtts))),
	retract(sdm_properties(Model,ModelProps)),
	delete(ModelProps, concept_set(_), NewProps),
	assert(sdm_properties(Model,NewProps)).


sdm_concept_set_single(Model, ConceptSet) :-
	findall(cssn(Concept,Score,Symbol,Name),
		( sdm_element(Model,Symbol,Type,As),
		  memberchk(name(Name), As),
		  \+ sdm_anonymous_label(Name),
		  concept_set_match_concept(Name, Concept,
						[ score(Score),
						  type(Type),
						  concept_set(ConceptSet)
						])), Matches),
	assign_best_matches(Matches, Model, ConceptSet).

assign_best_matches(Matches, Model, _ConceptSet) :-
	maplist(arg(1), Matches, Concepts),
	list_to_set(Concepts, Unique),
	length(Concepts, Len),
	length(Unique, Len), !,
	forall(member(cssn(Concept,_,Symbol,_), Matches),
	       sdm_element_assign_concept(Model, Symbol, Concept)).
assign_best_matches(Matches, Model, ConceptSet) :-
	compound_terms_sort(Matches, Sorted, [arg(2), order(descending)]),
	assign_best_matches(Sorted, [], Model, ConceptSet).

assign_best_matches([], _, _, _).
assign_best_matches([cssn(_,_,Symbol,Name)|Matches], Exclude, Model, ConceptSet) :-
	sdm_element(Model, Symbol, Type, _), !,
	(   concept_set_match_concept(Name, Concept,
				      [ type(Type),
					concept_set(ConceptSet),
					exclude(Exclude)
				      ])
	->  sdm_element_assign_concept(Model, Symbol, Concept),
	    assign_best_matches(Matches, [Concept|Exclude], Model, ConceptSet)
	;   assign_best_matches(Matches, Exclude, Model, ConceptSet),
	    sdm_element_assign_concept(Model, Symbol, '** already elsewhere in model **')
	).


%%	sdm_session(-Model:sdm_id, +Session:name) is det
%
%	Model is a model in Session.

sdm_session(Model, Session) :-
	seq_members(session(Session), Items), !,
	member(Item, Items),
	item2_model(Item, Model).


%%	sdm_session_last(-Model:sdm_id, +Session:name) is det
%
%	Model is the last in a Session.

sdm_session_last(Model, Session) :-
	seq_members(session(Session), Items), !,
	last_model(Items, Model).

last_model([Item|Items], Model) :-
	item2_model(Item, Model0), !,
	last_model(Items, Model0, Model).
last_model([_|Items], Model) :-
	last_model(Items, Model).

last_model([], Model0, Model) :-
	Model0 = Model.
last_model([Item|Items], Model0, Model) :-
	(   item2_model(Item, Model1)
	->  last_model(Items, Model1, Model)
	;   last_model(Items, Model0, Model)
	).


convert([], []).
convert([H|T], [H2|T2]) :-
	convert_h(H, H2),
	convert(T, T2).

convert_h(element(T,As,C), Result) :- !,
	convert_h(T, As, C, Result).
convert_h(Atom, Atom).

convert_h(descriptor, _As, C, C2) :-
	convert(C, [C2]), !.
convert_h(specification, _As, C, specification(C2)) :-
	convert(C, [C2]), !.
convert_h(T, [], [C], Result) :-
	atom(C), !,
	functor(Result, T, 1),
	arg(1, Result, C).
convert_h(T, As, [], Result) :-
	As \== [], !,
	functor(Result, T, 1),
	convert_as(As, Cs),
	arg(1, Result, Cs).
convert_h(T, As, C, Result) :-
	(   As == []
	->  functor(Result, T, 1),
	    arg(1, Result, C2)
	;   functor(Result, T, 2),
	    convert(As, Cs),
	    arg(1, Result, Cs),
	    arg(2, Result, C2)
	),
	convert(C, C2).


convert_as([], []).
convert_as([A|As], [C|Cs]) :-
	convert_a(A, C),
	convert_as(As, Cs).

convert_a(Var=Value, Term) :-
	functor(Term, Var, 1),
	(   convert_value(Var, Value, Convd)
	->  arg(1, Term, Convd)
	;   arg(1, Term, Value)
	).

convert_value(x1, Atom, Num) :- atom_number(Atom, Num).
convert_value(y1, Atom, Num) :- atom_number(Atom, Num).
convert_value(x2, Atom, Num) :- atom_number(Atom, Num).
convert_value(y2, Atom, Num) :- atom_number(Atom, Num).
convert_value(cx1, Atom, Num) :- atom_number(Atom, Num).
convert_value(cy1, Atom, Num) :- atom_number(Atom, Num).
convert_value(cx2, Atom, Num) :- atom_number(Atom, Num).
convert_value(cy2, Atom, Num) :- atom_number(Atom, Num).


store(Model, XML, Id) :-
	gls_create(Id, [notation(sdm)]),
	assert(sdm_xml(Id,XML)),
	memberchk(model(_,Atts), Model),
	memberchk(symbol(Symbol), Atts),
	assert(sdm_properties(Id,[name(Symbol)])),
	memberchk(variables(Vars), Atts),
	store_variables(Vars, Id),
	(   memberchk(layout(Layout), Atts)
	->  store_layout(Layout, Id)
	;   true
	).

store_variables([], _).
store_variables([varspec([variable(Var),specification(Spec)])|Vars], Model) :-
	extract_element_id(Var, Symbol, Var1),		% New version with uuid
	functor(Spec, Type, _),
	arg(1, Spec, Atts),
	append(Var1, Atts, AllAtts),
	assert_sdm_element(Model, Symbol, Type, AllAtts),
	store_variables(Vars, Model).

store_layout([], _).
store_layout([nodes(Nodes)|Layout], Id) :- !,
	store_nodes(Nodes, Id),
	store_layout(Layout, Id).
store_layout([links(Links)|Layout], Id) :- !,
	store_links(Links, Id),
	store_layout(Layout, Id).


store_nodes([], _).
store_nodes([Node|Nodes], Id) :-
	functor(Node, Type, 1),
	arg(1, Node, Atts),
	extract_element_id(Atts, Symbol, Atts1),
	assert_sdm_element(Id,Symbol,Type,Atts1),
	store_nodes(Nodes, Id).

store_links([], _).
store_links([Link|Links], Id) :-
	functor(Link, Type, 1),
	arg(1, Link, Atts),
	extract_element_id(Atts, Symbol, Atts1),
	assert_sdm_element(Id,Symbol,Type,Atts1),
	store_links(Links, Id).


extract_element_id(Atts, Id, Atts1) :-
	(   select(id(Id), Atts, Atts1)		% New version of Co-Lab as unique id
	->  true
	;   select(symbol(Id), Atts, Atts1),	% Old version of Co-Lab only has symbolic name
	    format('ATTS ~w~n', [Atts]),
	    format('ATTS1~w~n', [Atts1])
	),
	format('  element id ~w~n', [Id]).


normalise_model(Model) :-
	findall(sta(S,T,A), sdm_element(Model,S,T,A), STAs),
	sort(STAs, Sorted),
	normalise_model(Sorted, Model).


normalise_model([], _).
normalise_model([sta(S,T1,A1),sta(S,T2,A2)|STAs], Model) :- !,
	merge_types(T1, T2, A1, A2, T, As),
	retractall(sdm_element(Model,S,T1,A1)),
	retractall(sdm_element(Model,S,T2,A2)),
	assert_sdm_element(Model,S,T,As),
	normalise_model(STAs, Model).
normalise_model([_|STAs], Model) :-
	normalise_model(STAs, Model).


merge_types(stockSpec, stock, A1, A2, stock, As) :- !,
	merge_atts(A1, A2, As).
merge_types(auxSpec, auxiliary, A1, A2, auxiliary, As) :- !,
	merge_atts(A2, A1, As).
merge_types(constSpec, constant, A1, A2, constant, As) :- !,
	merge_atts(A2, A1, As).
merge_types(datasetSpec, dataset, A1, A2, dataset, As) :- !,
	merge_atts(A2, A1, As).
merge_types(T1, T2, A1, A2, T1, As) :-
	format('sdm/model:merge_types/6: DO NOT KNOW HOW TO MERGE ~w and ~w~n', [T1,T2]),
	append(A1, A2, As).


merge_atts([], A2, A2) :- !.
merge_atts([A1|A1s], A2s, [A1|As]) :-		% Terms in A1 are preferred
                                     		% over A2
	functor(A1, Functor, Arity),
	functor(Term, Functor, Arity),
	delete(A2s, Term, Valid),		% Delete same term from A2s
	merge_atts(A1s, Valid, As).


%%	is_sdm_signature_model(+Signature:term, -Model:atom) is semidet.
%
%	Succeeds if Signature is a signature that refers to Model (see
%	sdm/2).

is_sdm_signature_model(add_stock(_,Model), Model).
is_sdm_signature_model(add_flow(_,Model,_,_), Model).
is_sdm_signature_model(add_auxiliary(_,Model), Model).
is_sdm_signature_model(add_constant(_,Model), Model).
is_sdm_signature_model(add_relation(_,Model,_,_), Model).
is_sdm_signature_model(delete_flow(_,Model), Model).
is_sdm_signature_model(delete_auxiliary(_,Model), Model).
is_sdm_signature_model(delete_stock(_,Model), Model).
is_sdm_signature_model(delete_constant(_,Model), Model).
is_sdm_signature_model(delete_relation(_,Model,_,_), Model).
is_sdm_signature_model(rename_element(_,Model,_,_), Model).


%%	is_sdm_delete_item2_type(+Type:atom) is semidet.
%
%	Succeeds if Type refers to a delete action.

is_sdm_delete_item2_type(delete_relation).
is_sdm_delete_item2_type(delete_flow).
is_sdm_delete_item2_type(delete_stock).
is_sdm_delete_item2_type(delete_constant).
is_sdm_delete_item2_type(delete_auxiliary).
is_sdm_delete_item2_type(delete_dataset).


/*------------------------------------------------------------
 *  Convert a system dynamics model to a network of nodes and edges
 *------------------------------------------------------------*/

%%	sdm_to_network(+Model:atom, -Network:term, +Options:list) is det.
%
%	Converts a Model to a Network, a list of nodes and edges.
%	Options can contain normalise(true or false).

sdm_to_network(Model, Network, Options) :-
	option(normalise(Normalise), Options, true),
	findall(ta(T,As), sdm_element(Model,_,T,As), TAs),
	nb_setval(sdm_to_network, Model),
	model_to_network(TAs, Nodes0, Edges0, Normalise),
	sort(Nodes0, Nodes),
	sort(Edges0, Edges),
	Network = network(Nodes,Edges).

model_to_network([], [], [], _).
model_to_network([ta(T,As)|OTs], [Node|Nodes], Edges, Normalise) :-
	is_network_node(T, As, Normalise, Node), !,
	model_to_network(OTs, Nodes, Edges, Normalise).
model_to_network([ta(T,As)|OTs], Nodes, [Edge|Edges], Normalise) :-
	is_network_edge(T, As, Normalise, Edge), !,
	model_to_network(OTs, Nodes, Edges, Normalise).
model_to_network([_|OTs], Nodes, Edges, Normalise) :-
	model_to_network(OTs, Nodes, Edges, Normalise).


is_network_node(timeSpec, _, _, _) :- !, fail.
is_network_node(relation, _, _, _) :- !, fail.
is_network_node(flow, As, Normalise, outflow(flow(Symbol),Other)) :-
	memberchk(start(Other0), As), !,
	normalise_symbol(Normalise, As, Symbol),
	nb_getval(sdm_to_network, Model),
	normalise_model_symbol(Normalise, Model, Other0, Other).
is_network_node(flow, As, Normalise, inflow(flow(Symbol),Other)) :-
	memberchk(end(Other0), As), !,
	normalise_symbol(Normalise, As, Symbol),
	nb_getval(sdm_to_network, Model),
	normalise_model_symbol(Normalise, Model, Other0, Other).
is_network_node(flow, As, Normalise, flow(Symbol)) :- !,
	normalise_symbol(Normalise, As, Symbol).
is_network_node(auxiliary, As, Normalise, auxiliary(Symbol)) :- !,
	normalise_symbol(Normalise, As, Symbol).
is_network_node(stock, As, Normalise, stock(Symbol)) :- !,
	normalise_symbol(Normalise, As, Symbol).
is_network_node(constant, As, Normalise, constant(Symbol)) :- !,
	normalise_symbol(Normalise, As, Symbol).
is_network_node(dataset, As, Normalise, dataset(Symbol)) :- !,
	normalise_symbol(Normalise, As, Symbol).
is_network_node(Type, As, _Normalise, _Symbol) :-
	format('is_network_node/4: NOT A NETWORK NODE ~w ~w~n', [Type,As]),
	fail.

is_network_edge(timeSpec, _, _, _) :- !, fail.
is_network_edge(relation, As, Normalise, edge(From,To)) :- !,
	memberchk(start(From0), As),
	memberchk(end(To0), As),
	nb_getval(sdm_to_network, Model),
	normalise_model_symbol(Normalise, Model, From0, From),
	normalise_model_symbol(Normalise, Model, To0, To).
is_network_edge(Type, As, _Normalise, Symbol) :-
	format('is_network_edge/2: NOT A NETWORK EDGE ~w ~w ~w~n', [Symbol,Type,As]),
	fail.


normalise_symbol(_, As, Symbol) :-
	memberchk(concept(Symbol), As), !.
normalise_symbol(_, As, anon(Symbol)) :-
	memberchk(name(Symbol), As),
	sdm_anonymous_label(Symbol), !.
normalise_symbol(true, As, Symbol) :-
	memberchk(concept(Symbol), As), !.
normalise_symbol(Normalise, As, Symbol) :-
	memberchk(name(Symbol), As),
	(   Normalise \== true
	->  format('NO NORMALISATION FOR ~w~n', [Symbol])
	;   true
	).


normalise_model_symbol(Normalise, Model, Symbol, Term) :-
	sdm_element(Model, Symbol, Type, As),
	functor(Term, Type, 1),
	normalise_symbol(Normalise, As, Result),
	arg(1, Term, Result).


/*------------------------------------------------------------
 *  Convert a system dynamics model to a graph of nodes and edges
 *------------------------------------------------------------*/

%%	sdm_to_graph(+Model:atom, -Graph:term, +Options:list) is det.
%
%	Converts a Model to a Graph of nodes and edges.  The lists of nodes
%	and edges are sorted, so graphs can be compared for identity by
%	comparing the list of nodes and edges.

sdm_to_graph(Model, Graph, Options) :-
	option(label(Mode), Options, concept),
	option(representation(Representation), Options, labelled),
	model_to_graph(Representation, Model, Mode, Graph).


model_to_graph(labelled, Model, Mode, Graph) :-
	findall(st(S,T), sdm_element(Model,S,T,_), STs),
	model_to_labelled_graph(STs, Model, Mode, Nodes0, Edges0),
	msort(Nodes0, Nodes),
	msort(Edges0, Edges),
	Graph = graph(model(Model,[]),Nodes,Edges).
model_to_graph(symbolic, Model, Mode, Graph) :-
	findall(st(S,T), sdm_element(Model,S,T,_), STs),
	model_to_symbolic_graph(STs, Model, Mode, Nodes0, Edges0),
	msort(Nodes0, Nodes),
	msort(Edges0, Edges),
	Graph = graph(model(Model,[]),Nodes,Edges).
model_to_graph(variable, Model, Mode, Graph) :-
	findall(st(S,T), sdm_element(Model,S,T,_), STs),
	model_to_labelled_graph(STs, Model, Mode, Nodes0, Edges0),
	replace_anonymous_labels(Nodes0, Edges0, Nodes1, Edges1),
	msort(Nodes1, Nodes),
	msort(Edges1, Edges),
	Graph = graph(model(Model,[]),Nodes,Edges).


replace_anonymous_labels([], Edges, [], Edges).
replace_anonymous_labels([node(Label,Type)|Nodes], Edges0, [node(Anon,Type)|More], Edges) :-
	sdm_anonymous_label(Label), !,
	replace_anonymous_edges(Edges0, Label,Anon, Edges1),
	replace_anonymous_labels(Nodes, Edges1, More, Edges).
replace_anonymous_labels([Node|Nodes], Edges0, [Node|More], Edges) :-
	replace_anonymous_labels(Nodes, Edges0, More, Edges).


replace_anonymous_edges([], _, _, []).
replace_anonymous_edges([edge(Label1,To,Type)|Edges], Label, Anon, [edge(Anon,To,Type)|More]) :-
	Label1 == Label, !,		% Unification always succeeds if Label1
					% is a variable from a previous substitution
	replace_anonymous_edges(Edges, Label, Anon, More).
replace_anonymous_edges([edge(From,Label1,Type)|Edges], Label, Anon, [edge(From,Anon,Type)|More]) :-
	Label1 == Label, !,
	replace_anonymous_edges(Edges, Label, Anon, More).
replace_anonymous_edges([Edge|Edges], Label, Anon, [Edge|More]) :-
	replace_anonymous_edges(Edges, Label, Anon, More).


/*------------------------------------------------------------
 *  Model to a symbolic graph
 *------------------------------------------------------------*/

model_to_symbolic_graph([], _, _, [], []).
model_to_symbolic_graph([st(S,T)|STs], Model, Mode, [Node|Nodes], [Edge|Edges]) :-
	is_graph_node_and_edge(T, S, Model, Mode, Node,Edge), !,
	model_to_symbolic_graph(STs, Model, Mode, Nodes, Edges).
model_to_symbolic_graph([st(S,T)|STs], Model, Mode, [Node|Nodes], Edges) :-
	is_graph_node(T, S, Model, Mode, Node), !,
	model_to_symbolic_graph(STs, Model, Mode, Nodes, Edges).
model_to_symbolic_graph([st(S,T)|STs], Model, Mode, Nodes, [Edge|Edges]) :-
	is_graph_edge(T, S, Model, Mode, Edge), !,
	model_to_symbolic_graph(STs, Model, Mode, Nodes, Edges).
model_to_symbolic_graph([st(_,T)|STs], Model, Mode, Nodes, Edges) :-
	debug(sanity, 'sdm_to_graph/3: cannot convert ~w to a node or edge~n', [T]),
	model_to_symbolic_graph(STs, Model, Mode, Nodes, Edges).

is_graph_node_and_edge(flow, Symbol, Model, Mode, node(Symbol:Label,Type), edge(From,To,Type)) :-
	sdm_element_label(Model, Symbol, Mode, Label),
	sdm_element(Model, Symbol, _, As),
	(   memberchk(start(Other), As)
	->  sdm_element_label(Model, Other, Mode, OtherLabel),
	    Type = outflow,
	    From = Other:OtherLabel,
	    To = Symbol:Label
	;   memberchk(end(Other), As)
	->  sdm_element_label(Model, Other, Mode, OtherLabel),
	    Type = inflow,
	    From = Symbol:Label,
	    To = Other:OtherLabel
	), !.

is_graph_node(Type, Symbol, Model, Mode, node(Symbol:Label,Type)) :-
	is_sdm_node_type(Type),
	sdm_element_label(Model, Symbol, Mode, Label).

is_graph_edge(relation, Symbol, Model, Mode, edge(From:FromLabel,To:ToLabel,link)) :-
	sdm_element(Model, Symbol, _, As),
	memberchk(start(From), As),
	sdm_element_label(Model, From, Mode, FromLabel),
	memberchk(end(To), As),
	sdm_element_label(Model, To, Mode, ToLabel).


/*------------------------------------------------------------
 *  Model to a labelled graph
 *------------------------------------------------------------*/

model_to_labelled_graph([], _,_, [], []).
model_to_labelled_graph([st(S,T)|STs], Model,Mode, [Node|Nodes], [Edge|Edges]) :-
	is_lgraph_node_and_edge(T, S, Model,Mode, Node,Edge), !,
	model_to_labelled_graph(STs, Model,Mode, Nodes, Edges).
model_to_labelled_graph([st(S,T)|STs], Model,Mode, [Node|Nodes], Edges) :-
	is_lgraph_node(T, S, Model,Mode, Node), !,
	model_to_labelled_graph(STs, Model,Mode, Nodes, Edges).
model_to_labelled_graph([st(S,T)|STs], Model,Mode, Nodes, [Edge|Edges]) :-
	is_lgraph_edge(T, S, Model,Mode, Edge), !,
	model_to_labelled_graph(STs, Model,Mode, Nodes, Edges).
model_to_labelled_graph([st(_,T)|STs], Model,Node, Nodes, Edges) :-
	debug(sanity, 'sdm_to_graph/3: cannot convert ~w to a node or edge~n', [T]),
	model_to_labelled_graph(STs, Model,Node, Nodes, Edges).

is_lgraph_node_and_edge(flow, Symbol, Model,Mode, node(Label,Type), edge(From,To,Type)) :-
	sdm_element(Model, Symbol, _, As),
	sdm_element_label(Model, Symbol, Mode, Label),
	(   memberchk(start(Other0), As)
	->  sdm_element_label(Model, Other0, Mode, From),
	    sdm_element_label(Model, Symbol, Mode, To),
	    Type = outflow
	;   memberchk(end(Other0), As)
	->  sdm_element_label(Model, Other0, Mode, To),
	    sdm_element_label(Model, Symbol, Mode, From),
	    Type = inflow
	), !.

is_lgraph_node(Type, Symbol, Model,Mode, node(Label,Type)) :-
	is_sdm_node_type(Type),
	sdm_element_label(Model, Symbol, Mode, Label).

is_lgraph_edge(flow, Symbol, Model,Mode, edge(From,To,flow)) :-
	sdm_element(Model, Symbol, _, As),
	(   memberchk(start(Other0), As)
	->  sdm_element_label(Model, Other0, Mode, From),
	    sdm_element_label(Model, Symbol, Mode, To)
	;   memberchk(end(Other0), As)
	->  sdm_element_label(Model, Other0, Mode, To),
	    sdm_element_label(Model, Symbol, Mode, From)
	), !.
is_lgraph_edge(relation, Symbol, Model,Mode, edge(From,To,link)) :-
	sdm_element(Model, Symbol, _, As),
	memberchk(start(From0), As),
	memberchk(end(To0), As),
	sdm_element_label(Model, From0, Mode, From),
	sdm_element_label(Model, To0, Mode, To).


/*------------------------------------------------------------
 *  Metrics
 *------------------------------------------------------------*/

sdm_metrics(Model, Metrics) :-
	model_metrics(Metrics, Model).

model_metrics([], _).
model_metrics([Metric|Metrics], Model) :-
	model_metric(Metric, Model),
	model_metrics(Metrics, Model).

model_metric(node(Count), Model) :-
	findall(S, (sdm_element(Model,S,T,_), is_sdm_node_type(T)), Ss),
	length(Ss, Count).
model_metric(edge(Count), Model) :-
	findall(S, (sdm_element(Model,S,T,_), is_sdm_edge_type(T)), Ss),
	length(Ss, Count).
model_metric(flow(Count), Model) :-
	model_metric_type(flow, Count, Model).
model_metric(constant(Count), Model) :-
	model_metric_type(constant, Count, Model).
model_metric(auxiliary(Count), Model) :-
	model_metric_type(auxiliary, Count, Model).
model_metric(dataset(Count), Model) :-
	model_metric_type(dataset, Count, Model).
model_metric(stock(Count), Model) :-
	model_metric_type(stock, Count, Model).
model_metric(anonymous(Count), Model) :-
	findall(S, (sdm_element(Model,S,T,_),
		    is_sdm_node_type(T),
		    sdm_element_anonymous(Model,S,_)), Ss),
	length(Ss, Count).
model_metric(relation(Count), Model) :-
	model_metric_type(relation, Count, Model).

model_metric_type(Type, Count, Model) :-
	findall(S, sdm_element(Model,S,Type,_), Ss),
	length(Ss, Count).


/*------------------------------------------------------------
 *  Edit models
 *------------------------------------------------------------*/

%%	sdm_delete(+Model:atom) is det.
%
%	Delete the Model.

sdm_delete(Model) :-
	retractall(sdm_properties(Model,_)),
	retractall(sdm_xml(Model,_)),
	retractall(sdm_element(Model,_,_,_)).


%%	sdm_rename_element(+Model:atom, +OldName:atom, +NewName:atom) is det.
%
%	Rename the symbol with OldName to NewName in the given Model.

sdm_rename_element(Model, Old, New) :-
	findall(S, sdm_element(Model,S,_,_), Symbols),
	rename_element_atts(Symbols, Model, Old, New),
	rename_element_formulas(Symbols, Model, Old, New).

rename_element_atts([], _, _, _).
rename_element_atts([Symbol|Symbols], Model, Old, New) :-
	sdm_element(Model, Symbol, Type, Atts),
	rename_element_atts2(Atts, Old,New, NewAtts),
	(   Atts == NewAtts
	->  true
	;   retract_sdm_element(Model,Symbol,Type,Atts),
	    assert_sdm_element(Model,Symbol,Type,NewAtts)
	),
	rename_element_atts(Symbols, Model, Old, New).

rename_element_atts2([], _,_, []).
rename_element_atts2([Att|Atts], Old, New, [NewAtt|More]) :-
	rename_att(Att, Old, New, NewAtt), !,
	rename_element_atts2(Atts, Old, New, More).
rename_element_atts2([Att|Atts], Old, New, [Att|More]) :-
	rename_element_atts2(Atts, Old, New, More).

rename_att(start(Old), Old, New, start(New)).
rename_att(end(Old), Old, New, end(New)).
rename_att(name(Old), Old, New, name(New)).


rename_element_formulas([], _, _, _).
rename_element_formulas([Symbol|Symbols], Model, Old, New) :-
	sdm_element_expression(Model, Symbol, Formula, _),
	sdm_formula_rename(Formula, Old,New, NewFormula),
	Formula \== NewFormula, !,
	sdm_element_set_expression(Model, Symbol, NewFormula),
	rename_element_formulas(Symbols, Model, Old, New).
rename_element_formulas([_|Symbols], Model, Old, New) :-
	rename_element_formulas(Symbols, Model, Old, New).


%%	sdm_delete_element(+Model:atom, +Symbol:atom) is det.
%
%	Delete Symbol from the Model.  If Symbol has relations these are also
%	deleted.  If Symbol is part of a flow then the flow attributes have to
%	be updated.

sdm_delete_element(Model, Symbol) :-
  format('               delete ~w from ~w~n', [Symbol,Model]),
	retractall(sdm_element(Model,Symbol,_,_)),
	sdm_delete_references(Model, Symbol).


sdm_delete_references(Model, Symbol) :-
	sdm_element(Model, Other, relation, Atts),
	member(Att, Atts),
	functor(Att, _, Arity),
	between(1, Arity, N),
	arg(N, Att, Symbol),
	retractall(sdm_element(Model,Other,_,_)),
	fail.
sdm_delete_references(Model, Symbol) :-
	sdm_element(Model, Other, flow, Atts),
	member(Att, Atts),
	functor(Att, _, Arity),
	between(1, Arity, N),
	arg(N, Att, Symbol),
	delete(Atts, Att, Atts1),
	retractall(sdm_element(Model,Other,_,_)),
	assert_sdm_element(Model,Other,flow,Atts1),
	fail.
sdm_delete_references(_, _).


/*------------------------------------------------------------
 *  Correct symbol ids for an entire session based on signatures
 *------------------------------------------------------------*/

%%	is_sdm_node_type(+Type:atom) is det.
%%	is_sdm_node_type(-Type:atom) is nondet.
%
%	Succeeds if Type is a system dynamics model node type.

is_sdm_node_type(auxiliary).
is_sdm_node_type(constant).
is_sdm_node_type(stock).
is_sdm_node_type(dataset).


%%	is_sdm_link_type(+Type:atom) is det.
%%	is_sdm_link_type(-Type:atom) is nondet.
%
%	Succeeds if Type is a system dynamics model link type.

is_sdm_link_type(flow).
is_sdm_link_type(relation).


%%	is_sdm_nondisplay_type(+Type:atom) is det.
%%	is_sdm_nondisplay_type(-Type:atom) is nondet.
%
%	Succeeds if Type is a system dynamics model type that does not need to be displayed.

is_sdm_nondisplay_type(timeSpec).


%%	sdm_element_anonymous(+Model:atom, +Element:atom, -Name:atom) is semidet.
%
%	Succeeds if Element in Model is anonymous.  That is, the user has not
%	given it a meaningful name.  Note that
%	this predicate also succeeds for flows, which are always anonymous.

sdm_element_anonymous(Model, Element, Name) :-
	sdm_element(Model, Element, _, As),
	memberchk(name(Name), As),
	sdm_anonymous_label(Name).


%%	sdm_anonymous_label(+Label:atom) is semidet.
%
%	Succeeds if Label has the form of an anonymous element.

sdm_anonymous_label(Label) :-
	anonymous_prefix(Prefix),
	sub_atom(Label, 0,_,A, Prefix), !,
	sub_atom(Label, _,A,0, Rest),
	sub_atom(Rest, 0,1,_, '_'),
	sub_atom(Rest, 1,_,0, Counter),
	catch(atom_number(Counter,_), _, fail).

anonymous_prefix('Flow').
anonymous_prefix('Aux').
anonymous_prefix('Stock').
anonymous_prefix('Relation').
anonymous_prefix('Const').
anonymous_prefix('Dataset').


/*------------------------------------------------------------
 *  Attributes
 *------------------------------------------------------------*/

sdm_attribute(name(symbol)).		% Added by ModIC
sdm_attribute(concept(symbol)).		% Added by ModIC

sdm_attribute(end(id)).			% Convert to ID
sdm_attribute(expression(formula)).	% Convert to ID
sdm_attribute(inflow(symbol)).		% Convert to ID
sdm_attribute(initial(formula)).		% Convert to ID
sdm_attribute(outflow(symbol)).		% Convert to ID
sdm_attribute(start(symbol)).		% Convert to ID

sdm_attribute(color(int,int,int)).
sdm_attribute(cx1(int)).
sdm_attribute(cx2(int)).
sdm_attribute(cy1(int)).
sdm_attribute(cy2(int)).
sdm_attribute(dataset([])).
sdm_attribute(dataset('_unknown_')).
sdm_attribute(exprType(0)).
sdm_attribute(exprType(1)).
sdm_attribute(extrapolation(linear)).
sdm_attribute(input([])).
sdm_attribute(input(time)).
sdm_attribute(integration([method('Euler'), step(real)])).
sdm_attribute(integration([method('Runge-Kutta 45'), step(real)])).
sdm_attribute(intrapolation(linear)).
sdm_attribute(label(south)).
sdm_attribute(output([])).
sdm_attribute(output(time)).
sdm_attribute(time([start(real), stop(real)])).
sdm_attribute(type(int)).
sdm_attribute(type(ctrl_1)).
sdm_attribute(type(double)).
sdm_attribute(unit(string)).
sdm_attribute(visible(true)).
sdm_attribute(x1(int)).
sdm_attribute(x2(int)).
sdm_attribute(y1(int)).
sdm_attribute(y2(int)).
