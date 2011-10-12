/*  $Id$
 *  
 *  File	formula.pl
 *  Part of	IC - behavioural data mining
 *  Author	Anjo Anjewierden, a.a.anjewierden@utwente.nl
 *  Purpose	Formula's related to models
 *  Works with	SWI-Prolog (www.swi-prolog.org)
 *  Status      clean, check, pldoc
 *  
 *  Notice	Copyright (c) 2009  University of Twente
 *  
 *  History	11/05/09  (Created)
 *  		12/08/09  (Last modified)
 */ 

/*------------------------------------------------------------
 *  Directives
 *------------------------------------------------------------*/

:- module(formula_mod_ic,
	  [ sdm_formula_parse/2,		% Atom -> Term
	    sdm_formula_rename/4,		% Formula x Old x New -> NewFormula
	    sdm_formula_normalise/2,		% Term -> Normalised
	    sdm_formula_normalise/3,		% Term -> Normalised x Assignment
	    sdm_formula_ids/3			% Formula x Ids -> NewFormula
	  ]).

:- use_module(load).


/*------------------------------------------------------------
 *  Parse formula's
 *------------------------------------------------------------*/

%%	sdm_formula_parse(+Atom:atom, -Formula:term) is det.
%
%	Parse a formula represented by Atom and return its term representation
%	in Formula.  The Prolog parser is used to do the proper parsing.  In
%	Co-Lab the =^= symbol is used for exponents, this is converted to
%	Prolog's =**=.
%
%	@tbd  Prolog cannot parse 2 ** 3 ** 4, a hack attempts to solve this.

sdm_formula_parse('', '') :- !.
sdm_formula_parse([], '') :- !.
sdm_formula_parse(Already, Already) :-
	functor(Already, _, Arity),
	Arity > 1, !.
sdm_formula_parse(Atom, Formula) :-
%	tokenise_atom(Atom, Tokens),
%	pl_style(Tokens, Tokens1),
%	detokenise_atom(Tokens1, Atom1),
	downcase_atom(Atom, Atom2),
	catch(atom_to_term(Atom2, Formula, Bindings), Error, true),
	(   var(Error)
	->  apply_bindings(Bindings)
	;   format('FORMULA ERROR is ~q~n', [Error]),
	    format('  ~q~n', [Atom]),
	    format('  ~q~n', [Atom2]),
	    Formula = Atom2
	).


/*
pl_style(In, [character('(')|Out]) :-
	pl_style2(In, Out).

pl_style2([], [character(')')]).
pl_style2([character('^'),integer(I)], [character(') **'), integer(I)]) :- !.
pl_style2([In|Ts], [Out|More]) :-
	pl_token(In, Out), !,
	pl_style2(Ts, More).
pl_style2([T|Ts], [T|More]) :-
	pl_style2(Ts, More).

pl_token(character('^'), character(' ** ')).
*/

apply_bindings([]).
apply_bindings([Name=Name|Bs]) :-
	apply_bindings(Bs).


/*------------------------------------------------------------
 *  Rename a variable in a formula
 *------------------------------------------------------------*/

%%	sdm_formula_rename(+In:term, +Old:atom, +New:atom, -Out:term) is det.
%
%	In is a formula, rename variable Old and call it New, new formula is in Out.

sdm_formula_rename(In, In, Out, Out) :- !.
sdm_formula_rename(In, _, _, In) :-
	atomic(In), !.
sdm_formula_rename(In, Old, New, Out) :-
	functor(In, Functor, Arity),
	functor(Out, Functor, Arity),
	rename_args(1, Arity, In, Out, Old,New).

rename_args(N, Arity, In, Out, Old,New) :-
	(   N > Arity
	->  true
	;   arg(N, In, Arg),
	    sdm_formula_rename(Arg, Old,New, Tmp),
	    arg(N, Out, Tmp),
	    succ(N, Next),
	    rename_args(Next, Arity, In, Out, Old,New)
	).


/*------------------------------------------------------------
 *  Normalise formula's
 *------------------------------------------------------------*/

%%	sdm_formula_normalise(+In:term, +Out:term) is det.
%%	sdm_formula_normalise(+In:term, +Out:term, +Assignment:atom) is det.
%
%	Normalise formula In by replacing the names of variables by the
%	normalised variables and return the result in Out.

sdm_formula_normalise(Var, Out) :-
	sdm_formula_normalise(Var, Out, _).

sdm_formula_normalise(Atomic, Atomic, _) :-
	atomic(Atomic), !.
sdm_formula_normalise(Term, Out, Assignment) :-
	functor(Term, Functor, Arity),
	functor(Out, Functor, Arity),
	normalise_args(1, Arity, Term, Out, Assignment).

normalise_args(N, Arity, Term, Out, Assignment) :-
	(   N > Arity
	->  true
	;   arg(N, Term, Arg),
	    sdm_formula_normalise(Arg,New, Assignment),
	    arg(N, Out, New),
	    succ(N, Next),
	    normalise_args(Next, Arity, Term, Out, Assignment)
	).


%%	sdm_formula_ids(+Formula:term, +NameIds:list, -NewFormula) is det
%
%	Replace symbolic names of variables in Formula by Ids and return as
%	NewFormula.  NameIds is of the form =/2.

sdm_formula_ids('', _, '') :- !.
sdm_formula_ids(Var, NameIds, Id) :-
	atom(Var),
	memberchk(Var=Id, NameIds), !.
sdm_formula_ids(Atomic, _, Atomic) :-
	atomic(Atomic), !.
sdm_formula_ids(Term, NameIds, Out) :-
	functor(Term, Functor, Arity),
	functor(Out, Functor, Arity),
	ids_args(1, Arity, Term, NameIds, Out).

ids_args(N, Arity, Term, NameIds, Out) :-
	(   N > Arity
	->  true
	;   arg(N, Term, Arg),
	    sdm_formula_ids(Arg,NameIds,New),
	    arg(N, Out, New),
	    succ(N, Next),
	    ids_args(Next, Arity, Term, NameIds, Out)
	).
