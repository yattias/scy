/*  $Id$
 *  
 *  File	term_set.pl
 *  Part of	IC - educational data mining
 *  Author	Anjo Anjewierden, a.a.anjewierden@utwente.nl
 *  Purpose	Term set specification
 *  Works with	SWI-Prolog (www.swi-prolog.org)
 *  Status      check, clean, pldoc, public
 *  
 *  Notice	Copyright (c) 2009, 2010, 2011  University of Twente
 *  
 *  History	03/07/09  (Created)
 *  		14/01/11  (Last modified)
 */

/*------------------------------------------------------------
 *  Directives
 *------------------------------------------------------------*/

:- module(term_set_specification_ic,
	  [ term_set_create/2,			% Set x Properties
	    term_set_delete/1,			% Set

	    term_set/1,		   		% Set
	    term_set_properties/2,		% Set -> Properties
	    term_set_property/2,		% Set -> Property
	    term_set_propchk/2,			% Set x Property
	    term_set_add_property/2,		% Set x Property

	    term_set_parse/3,			% Source -> Set <- Options
	    term_set_manage/2,			% Set x Options

	    term_set_term/2,			% Set -> Term

	    term_set_normalise_string/3,	% String -> Normalised <- Options
	    term_set_string_similarity/3,	% Term1 x Term -> Similarity
	    
	    term_set_match_string/4,		% Set x String -> Term <- Options
	    
	    term_set_export_matches/3,		% Set x File <- Options
	    term_set_unused_strings/1		% Set
	  ]).

:- use_module(load).

:- use_module(library(lists), [append/3, delete/3, list_to_set/2, member/2,
			       subtract/3]).
:- use_module(library(sgml), [load_structure/3]).
:- use_module(library(option), [option/2, option/3, merge_options/3]).
:- use_module(library('dialog/pretty_print'), [pretty_print/1]).

:- use_module(normalise_string, [normalise_string/3, normalise_string_manage/1]).

:- use_module(dac(atom_to_csv), [atom_to_csv/2]).
:- use_module(sdm(model), [sdm_element_anonymous/3, sdm_element/4]).
:- use_module(sort(compound_terms), [compound_terms_sort/3]).
:- use_module(atom(edit_distance), [edit_distance/3, edit_distance_pl/3]).
:- use_module(utilities(xml), [xml_memberchk/2, xml_avs_to_terms/2, source_to_xml/3]).


/**	<module> Term sets

A term set defines a set of terms and the corresponding syntactic strings learners
might use for these term.  The basic functionality is to take a name provided
by a learner and map it to one of the terms in the set.

@author	Anjo Anjewierden
*/


%%	term_set_parse(+Source:term, -TermSet:atom, +Options:list) is det.
%
%	Source contains the term set.  Options are:
%
%	* term_set(Name)
%	  Override the identifier of the term set, normally the one in the
%	  file is used.

term_set_parse(Source, Set, Options) :-
	source_to_xml(Source, XML,
		       [ dialect(xml),
			 max_errors(-1),
			 syntax_errors(quiet),
			 space(remove)
		       ]),
	(   option(term_set(Set), Options)
	->  true
	;   xml_memberchk(element(term_set,As,_), XML),
	    memberchk(name=Set, As)
	),
	term_set_module(Set, Module),
	format('  Module is ~w~n', [Module]),
	dynamic(Module:string_term/4),
	dynamic(Module:string_term_cache/5),
	option(verbose(Bool), Options, false),
	parse(XML, Bool, Set, Module),
	(   Source = file(File)
	->  absolute_file_name(File, AbsFile),
	    time_file(AbsFile, TimeStamp),
	    term_set_add_property(Set, file(AbsFile)),
	    term_set_add_property(Set, last_modified(TimeStamp))
	;   true
	).

parse([], _, _, _).
parse([H|T], Verbose, Set, Module) :-
	(   Verbose == true
	->  pretty_print(H)
	;   true
	),
	parse_h(H, Verbose, Set, Module),
	parse(T, Verbose, Set, Module).

parse_h(element(T,As,C), Verbose, Set, Module) :- !,
	parse_t(T,As,C, Verbose, Set, Module).
parse_h(_, _, _, _).

parse_t(term_set,As,C, Verbose, Set, Module) :- !,
	xml_avs_to_terms(As, Props0),
	semi_colon_in_type_props(Props0, Props),
	term_set_create(Set, Props),
	parse(C, Verbose, Set, Module).
parse_t(term,As,C, Verbose, Set, Module) :- !,
	memberchk(name=Term, As),
	nb_setval(term_set_current, Term),
	term_set_assert_term(Set, Term),
	parse(C, Verbose, Set, Module).
parse_t(string, As, [String], _Verbose, Set, Module) :- !,
	term_set_properties(Set, SetOptions),
	xml_avs_to_terms(As, Options),
	nb_getval(term_set_current, Term),
	add_term_string(Term, String, Set,Module, SetOptions, Options).
parse_t(sub_string, As, [Sub], _Verbose, Set, Module) :- !,
	term_set_properties(Set, SetOptions),
	xml_avs_to_terms(As, Options),
	nb_getval(term_set_current, Term),
	add_term_string(Term, Sub, Set,Module, SetOptions, [method(sub_string)|Options]).
parse_t(C, _, _, Verbose, _, _) :-				% Ignore everything else
	(   Verbose == true
	->  format('*** ignoring ~w ***~n', [C])
	;   true
	).


semi_colon_in_type_props([], []).
semi_colon_in_type_props([H|T], [H2|More]) :-
	semi_colon_in_type_prop(H, H2),
	semi_colon_in_type_props(T, More).

semi_colon_in_type_prop(type(Type), RealType) :- !,
	(   sub_atom(Type, _,_,_, ';')
	->  atomic_list_concat(Types0, ';', Type),
	    findall(T, ( member(Type0,Types0),
			 normalize_space(atom(T), Type0)), RealType)
	;   RealType = Type
	).
semi_colon_in_type_prop(Prop, Prop).



add_term_string(Term, String, _Set,Module, SetOptions, StringOptions) :-
	merge_options(StringOptions, SetOptions, Options),
	option(word_order(WO), Options, any),
	(   option(method(sub_string), Options)
	->  assert(Module:string_term(String,Term,String,Options))
	;   term_set_normalise_string(String, Norm, Options),
	    (   WO == fixed
	    ->  assert(Module:string_term(String,String,Term,Options))
	    ;   term_set_normalise_string(String, Norm, Options),
		atomic_list_concat(Words, ' ', Norm),
		forall(permutation(Words,Perm),
		       ( atomic_list_concat(Perm, ' ', T),
			 assert(Module:string_term(T,Term,String,Options))))
	    )
	).



/*------------------------------------------------------------
 *  Remove unwanted codes (perhaps should be replaced by a space)
 *------------------------------------------------------------*/

unwanted_code(47).		% /

remove_unwanted_chars(Term0, Term) :-
	atom_codes(Term0, Codes0),
	remove_unwanted_codes(Codes0, Codes),
	atom_codes(Term, Codes).

remove_unwanted_codes([], []).
remove_unwanted_codes([Code|Codes], More) :-
	unwanted_code(Code), !,
	remove_unwanted_codes(Codes, More).
remove_unwanted_codes([Code|Codes], [Code|More]) :-
	remove_unwanted_codes(Codes, More).



%%	term_set_manage(+Set:atom, +Options:list) is det.
%
%	Options are:
%
%	* cache(clear)
%	  Clear the cache of string to term matches computed
%	  previously.  Mainly for maintenance reasons.
%
%	* language(Language)
%	  Language used for stop words in string normalisation (term_set_normalise_string/3).

term_set_manage(Set, Options) :-
	term_set_manage2(Options, Set).

term_set_manage2([], _).
term_set_manage2([Option|Options], Set) :-
	term_set_manage_option(Option, Set),
	term_set_manage2(Options, Set).

term_set_manage_option(cache(clear), Set) :-
	term_set_module(Set, Module),
	retractall(Module:string_term_cache(_,_,_,_,_)).
term_set_manage_option(language(Language), _Set) :-
	normalise_string_manage([language(Language)]).


/*------------------------------------------------------------
 *  Normalise strings
 *------------------------------------------------------------*/

%%	term_set_normalise_string(+String:atom, -Normalised:atom, +Options:list) is det.
%
%	Normalised is the normalised String after applying Options.  Options
%	are:
%
%	* downcase(Bool)
%	  If Bool is =true= (default), then all upper case characters in String
%	  are replaced by lower case characters (using downcase/2).
%
%	* stop_word(Bool)
%	  If Bool is =true= (default), then all stop words are removed from
%	  String.  Currently stop words are only supported for the Dutch
%	  language.
%
%	* punctuation(Bool)
%	  If Bool is =true= (default), all punctuation characters are removed
%	  from String.  Punctuation characters are those for which char_type/2
%	  succeeds with =punct=.

term_set_normalise_string(String, Normalised, Options) :-
	normalise_string(String, Normalised, Options).





/*------------------------------------------------------------
 *  Storage
 *------------------------------------------------------------*/

%%	term_set_term(-Set:atom, -Term:atom) is nondet.
%
%	Succeeds if Term is part of Set.

:- dynamic
	term_set_term/2.	% Set x Term
%	string_term/4		% Set: String -> Term x BaseString x Options
%	string_term_cache/5	% Set: String -> Term x BaseString x Score x Options


term_set_assert_term(Set, Concept) :-
	(   term_set_term(Set, Concept)
	;   assert(term_set_term(Set,Concept))
	), !.

/*------------------------------------------------------------
 *  Unused strings
 *------------------------------------------------------------*/

%%	term_set_unused_strings(+Set:atom) is det.
%
%	Compares the terms given for the concept set and those actually used
%	in the current set of models.  Unused terms are reported on the terminal.

term_set_unused_strings(Set) :-
	term_set_module(Set, Module),
	findall(tc(T,C), ( Module:string_term(_, C, T, Opts),
			   \+ memberchk(accept(false), Opts)), TCs),
	list_to_set(TCs, Unique),
	term_set_module(Set, Module),
	term_set_unused_strings(Unique, Module).

term_set_unused_strings([], _).
term_set_unused_strings([tc(T,C)|TCs], Module) :-
	Module:string_term_cache(_, C, T, _, _), !,
	term_set_unused_strings(TCs, Module).
term_set_unused_strings([tc(T,C)|TCs], Module) :-
	format('~w~40|~t~w~n', [T,C]),
	term_set_unused_strings(TCs, Module).


/*------------------------------------------------------------
 *  Existence
 *------------------------------------------------------------*/

term_set_create(Set, Properties) :-
	term_set_delete(Set),
	assert(term_set_properties(Set,Properties)),
	(   memberchk(language(Language), Properties)
	->  term_set_manage(Set, [language(Language)])
	;   true
	).


term_set_delete(Set) :-
	term_set(Set), !,
	term_set_module(Set, Module),
	retractall(Module:string_term(_,_,_,_)),
	retractall(Module:string_term_cache(_,_,_,_,_)),
	retractall(term_set_properties(Set,_)),
	retractall(term_set_term(Set,_)).
term_set_delete(_).


%%	term_set(+Set:atom) is semidet.
%%	term_set(-Set:atom) is nondet.
%
%	Succeeds if Set is the name of a concept set.

term_set(Set) :-
	term_set_properties(Set, _).


%%	term_set_properties(-Set:atom, -Properties:list) is nondet.
%
%	Succeeds if Set has Properties

:- dynamic
	term_set_properties/2.		% Set x Properties


%%	term_set_property(-Set:atom, -Property:term) is nondet.
%
%	Succeeds if Set has Property.

term_set_property(Set, Prop) :-
	term_set_properties(Set, Props),
	member(Prop, Props).


%%	term_set_propchk(-Set:atom, -Property:term) is semidet.
%
%	Succeeds once if Set has Property.

term_set_propchk(Set, Prop) :-
	term_set_properties(Set, Props),
	memberchk(Prop, Props).


term_set_add_property(Set, Prop) :-
	retract(term_set_properties(Set,Props)),
	assert(term_set_properties(Set,[Prop|Props])).


term_set_module(Set, Module) :-
	atom_concat(Set, '_term_set', Module).


/*------------------------------------------------------------
 *  Export matches
 *------------------------------------------------------------*/

%%	term_set_export_matches(+Set:name, +File:name, +Options:list) is det.
%
%	Export the matches of Set on all applicable Gls's to File.
%	The output is in .csv format and contains rows for: gls, type, name and
%	term.
%
%	* prefix(Label)
%	  If provided, Label is printed as the first column for all rows.
%	  This makes it possible to label the outputs of various versions of
%	  the term set and compare them (by concatenating the outputs).
%
%	@tbd Only works for SDM at this moment.

term_set_export_matches(CS, File, Options) :-
	open(File, write, Stream),
	(   option(prefix(Prefix), Options)
	->  format(Stream, 'prefix,', []),
	    nb_setval(cs_export_prefix, true(Prefix))
	;   nb_setval(cs_export_prefix, false)
	),
	format(Stream,'gls,type,name,term~n', []),
	findall(M, gls_propchk(M, term_set(CS)), Ms),
	export_matches(Ms, Stream),
	close(Stream).

export_matches([], _).
export_matches([M|Ms], Stream) :-
	findall(S, sdm_element(M,S,_,_), Ss),
	export_matches(Ss, M, Stream),
	export_matches(Ms, Stream).

export_matches([], _, _).
export_matches([S|Ss], Model, Stream) :-
	sdm_element_anonymous(Model, S, _), !,
	export_matches(Ss, Model, Stream).
export_matches([S|Ss], Model, Stream) :-
	sdm_element(Model, S, Type, As),
	option(name(Name), As, '*'),
	option(concept(Concept), As, '** no match **'),
	atom_to_csv(Name, Name1),
	atom_to_csv(Concept, Concept1),
	(   nb_getval(cs_export_prefix, true(Prefix))
	->  format(Stream, '~w,', [Prefix])
	;   true
	),
	format(Stream, '~w,~w,~w,~w~n', [Model,Type,Name1,Concept1]),
	export_matches(Ss, Model, Stream).


/*------------------------------------------------------------
 *  String similarity
 *------------------------------------------------------------*/

term_set_string_similarity(String1, String2, Similarity) :-
	atom_length(String1, Len1),
	atom_length(String2, Len2),
	edit_distance(String1, String2, Distance),
	(   edit_distance_pl(String1, String2, Distance)
	->  true
	;   format('dist differ ~w ~w ~w~n', [String1,String2,Distance])
	),
	(   (Len1 < 5; Len2 < 5)
	->  (  Distance == 0
	    -> Similarity = 1.0
	    ;  Similarity = 0.0
	    )
%	;   Similarity is (Len1+Len2 - Distance) / (Len1+Len2)
	;   Similarity is (max(Len1,Len2) - Distance) / max(Len1,Len2)
	).


term_set_string_term(Set, String, Term, Base, Options) :-
	term_set_module(Set, Module),
	Module:string_term(String, Term, Base, Options).


/*------------------------------------------------------------
 *  Matching a given string against the term set
 *------------------------------------------------------------*/

term_set_match_string(Set, String0, Term, Options) :-
	option(threhold(Threshold), Options, 0.5),
	pre_string_terms(Set, PreTerms),
	term_set_normalise_string(String0, String, []),
	match_sts(PreTerms, String, STSs),
	compound_terms_sort(STSs, Sorted, [arg(3), functor(sts)]),
	memberchk(sts(_,Term,Similarity), Sorted),
	Similarity >= Threshold.


pre_string_terms(Set, PreTerms) :-
	term_set_module(Set, Module),
	findall(st(String,Term),
		( Module:string_term(String,Term,_,Options),
		  \+ memberchk(when(post), Options),
		  \+ memberchk(method(sub_string), Options)), PreTerms).

match_sts([], _, []).
match_sts([st(Match,Term)|STs], String, [sts(Match,Term,Similarity)|STSs]) :-
	term_set_string_similarity(Match, String, Similarity),
	match_sts(STs, String, STSs).

end_of_file.
	

%%	term_set_match_string(+Set:atom, +String:atom, -Term:atom, +Options) is semidet.
%
%	Options are:
%
%	* exclude(Terms)
%	  Exclude the terms in the list of Terms from a possible match.
%	  This can be used to prevent that a single string maps on multiple
%	  terms in a given model.
%
%	* type(Type)
%	  Type of the element corresponding to Term.  Type can be an atom or a list of types.
%
term_set_match_string(Set, String, Term, Options) :-
	option(exclude(Exclude), Options, []),
	match_relevant_options(Options, MatchOptions),
	term_set_module(Set, Module),
	(   Exclude == [],
	    Module:string_term_cache(String,Term,_,Value,MatchOptions)
	->  ignore(memberchk(score(Value), Options))
	;   find_tcdos(Set, Exclude, String, TCDOs),
	    findall(tcdo(T,C,pre(X),Opts), member(tcdo(T,C,pre(X),Opts), TCDOs), Pres0),
	    findall(tcdo(T,C,post(X),Opts), member(tcdo(T,C,post(X),Opts), TCDOs), Posts0),
	    compound_terms_sort(Pres0, Pres, [arg(3), order(descending)]),
	    compound_terms_sort(Posts0, Posts, [arg(3), order(descending)]),
	    append(Pres, Posts, Sorted),
	    select_best(Sorted, Matched,Value, Term,Opts, MatchOptions),
	    ignore(option(score(Value), Options)),
	    term_set_module(Set, Module),
	    assert(Module:string_term_cache(String,Term,Matched,Value,MatchOptions))
	).


match_relevant_options(All, Relevant) :-
	match_relevant_options2(All, Tmp),
	sort(Tmp, Relevant).

match_relevant_options2([], []).
match_relevant_options2([Opt|Opts], [Opt|More]) :-
	match_relevant_option(Opt), !,
	match_relevant_options2(Opts, More).
match_relevant_options2([_|Opts], More) :-
	match_relevant_options2(Opts, More).

match_relevant_option(type(_)).


find_tcdos(Set, Exclude, String, TCDOs) :-
	term_set_module(Set, Module),
	findall(C, term_set_term(Set,C), Terms0),
	subtract(Terms0, Exclude, Terms1),
	remove_accept_false(Terms1, Set,Module,String, Terms),
	findall(tcdo(S,T,Score,Opts),
		( member(T, Terms),
		  Module:string_term(S, T, _, Opts),
		  term_set_normalise_string(String, Norm, Opts),
		  term_options_score(Norm, S, Opts, Score)), TCDOs).


remove_accept_false([], _,_,_, []).
remove_accept_false([T|Terms], Set,Module,String, More) :-
	Module:string_term(S, T, _, Options),
	term_set_normalise_string(String, Norm, Options),
	memberchk(method(sub_string), Options),
	memberchk(accept(false), Options),
	sub_atom(Norm, _,_,_, S), !,
	remove_accept_false(Terms, Set,Module,String, More).
remove_accept_false([T|Terms], Set,Module,String, [T|More]) :-
	remove_accept_false(Terms, Set,Module,String, More).


term_options_score(String, Known, Options, Score) :-
	memberchk(method(sub_string), Options), !,
	(   sub_atom(String, _,_,_, Known)
	->  atom_length(Known, Len),
	    Score0 is 0.51 + Len / 100		% 0.51 is larger than the threshold of 0.5
	;   Score0 = 0.0
	),
	when_score(Options, post, Score0, Score).
term_options_score(String, Known, Options, Score) :-
	memberchk(method(words), Options), !,
	words(String, Words1),
	words(Known, Words2),
	match_words(Words2, Words1, Score0),
	when_score(Options, pre, Score0, Score).
term_options_score(String, Known, Options, Score) :-
	term_set_string_similarity(String, Known, Score0),
	when_score(Options, pre, Score0, Score).


when_score(Options, Default, Score0, Score) :-
	(   memberchk(when(When), Options)
	->  true
	;   When = Default
	),
	(   When == pre
	->  Score = pre(Score0)
	;   Score = post(Score0)
	).


words(String, Words) :-
	atomic_list_concat(Words, ' ', String).
	    
	
match_words(KnownWords, StringWords, Score) :-
	match_words(KnownWords, StringWords, 1, Score).
match_words(_, _, 0).

match_words([], _, Score, Score).
match_words([Known|KnownWords], StringWords, Score0, Score) :-
	(   Score0 < 0				% Already negative, don't search further
	->  Score = Score0
	;   best_term_word(StringWords, Known, Best, Score1),
	    Score2 is Score0 * Score1,		% If both negative, then score becomes positive
	    delete(StringWords, Best, StringRest),
	    match_words(KnownWords, StringRest, Score2, Score)
	).

best_term_word([String|StringWords], Known, Best, Score) :-
	term_set_string_similarity(String, Known, Value),
	best_term_word(StringWords, Known, String,Value, Best,Score).

best_term_word([], _, Best,Score, Best,Score).
best_term_word([String|StringWords], Known, Best0,Score0, Best,Score) :-
	term_set_string_similarity(String, Known, Value),
	(   Value > Score0
	->  best_term_word(StringWords, Known, String,Value, Best,Score)
	;   best_term_word(StringWords, Known, Best0,Score0, Best,Score)
	).


select_best([tcdo(String,Term,Score,Opts)|_], String,Value, Term,Opts, Options) :-
	check_type(Options, Opts),
	arg(1, Score, Value),		% pre(Value) or post(Value)
	Value >= 0.5, !.
select_best([_|More], String,Score, Term,Opts, Options) :-
	select_best(More, String,Score, Term,Opts, Options).


check_type(OptionsCaller, OptionsString) :-
	memberchk(type(Type0), OptionsCaller),
	memberchk(type(Type1), OptionsString), !,
	(   atom(Type1)
	->  Type0 == Type1
	;   is_list(Type1),
	    memberchk(Type0, Type1)
	).
check_type(_, _).


/*------------------------------------------------------------
 *  Consistency
 *------------------------------------------------------------*/

%%	term_set_consistency(+Set:atom) is det.
%
%	Checks the consistency of the Set and prints results on the terminal.
%	Currently input terms that would match on several concepts are reported.

term_set_consistency(Set) :-
	term_set_module(Set, Module),
	findall(tbc(String,Base,Concept,Opts),
		Module:string_term(String, Concept, Base, Opts), TBCs),
	sort(TBCs, Sorted),
	consistent(Sorted).

consistent([]).
consistent([_]) :- !.
consistent([tbc(String,Base1,Concept1,Opts1), tbc(String,Base2,Concept2,Opts2)|TBCs]) :- !,
	option(type(Type1), Opts1, ''),
	option(type(Type2), Opts2, ''),
	format('~n', []),
	format('~w~40|~t ~w (~w) ~w~n', [String,Base1,Concept1,Type1]),
	format('  ~40|~t ~w (~w) ~w~n', [     Base2,Concept2,Type2]),
	consistent([tbc(String,Base2,Concept2,Opts2)|TBCs]).
consistent([_|TBCs]) :-
	consistent(TBCs).


