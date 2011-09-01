/*  $Id$
 *  
 *  File	atom_to_csv.pl
 *  Part of	Document analysis
 *  Part of	Convert an atom to .csv representation
 *  Author	Anjo Anjewierden, a.a.anjewierden@utwente.nl
 *  Purpose	Convert an atom to .csv representation
 *  Works with	SWI-Prolog (www.swi-prolog.org)
 *  Status	check, pldoc, public
 *  
 *  Notice	Copyright (c) 2011        University of Twente
 *  
 *  History	25/07/02  (Created)
 *  		12/04/11  (Last modified)
 */ 

/*------------------------------------------------------------
 *  Directives
 *------------------------------------------------------------*/

:- module(atom_to_csv,
	  [ atom_to_csv/2,	% Atom -> AtomCSV
	    atom_to_csv/5	% Atom x Sep x Esc x Meta -> AtomCSV
	  ]).


/*------------------------------------------------------------
 *  Converting atoms to the .csv printable representation
 *------------------------------------------------------------*/

%%	atom_to_csv(+Atom:atom, -CSV:atom) is det.
%
%	Convert Atom such that it appears identical in .csv representation.
%	If necessary quoting characters are inserted.

atom_to_csv(Atom, CSV) :-
	atom_to_csv(Atom, ',', '"', '""', CSV).


%%	atom_to_csv(+Atom:atom, +Sep:atom, Esc:atom, Meta:atom, -CSV:atom) is det.
%
%	Convert Atom such that it appears identical in .csv representation.
%	If necessary quoting characters are inserted.

atom_to_csv(Atom, _, Esc, Meta, CSV) :-
	sub_atom(Atom, _, _, _, Esc), !,
	replace_atom(Atom, Esc, Meta, CSV0),
	atomic_list_concat([Esc,CSV0,Esc], CSV).
atom_to_csv(Atom, Sep, Esc, _, CSV) :-
	sub_atom(Atom, _, _, _, Sep), !,
	atomic_list_concat([Esc,Atom,Esc], CSV).
atom_to_csv(Atom, _, _, _, Atom).


replace_atom(Atom, In, Out, Result) :-
	sub_atom(Atom, B,_,A, In), !,
	sub_atom(Atom, 0, B, _, Before),
	sub_atom(Atom, _, A, 0, After),
	replace_atom(After, In, Out, After1),
	atomic_list_concat([Before,Out,After1], Result).
replace_atom(Atom, _, _, Atom).
