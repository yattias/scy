/*  $Id$
 *  
 *  File	xml.pl
 *  Part of	Prolog utilities
 *  Author	Anjo Anjewierden, a.a.anjewierden@utwente.nl
 *  Purpose	XML support predicates
 *  Works with	SWI-Prolog (www.swi-prolog.org)
 *  
 *  Notice	Copyright (c) 2010       University of Twente
 *  		Copyright (c) 1999-2006  University of Amsterdam
 *  
 *  History	28/09/99  (Created)
 *  		08/03/10  (Last modified)
 */ 

/*------------------------------------------------------------
 *  Directives
 *------------------------------------------------------------*/

:- module(xml_utilities,
	  [ xml_member/2,		% Element -> XML
	    xml_memberchk/2,		% Element -> XML
	    source_to_xml/3,		% Source -> XML <- Options
	    atom_to_xml/3,		% Atom -> XML <- Options
	    xml_to_atom/3,		% XML -> Atom <- Options
	    xml_to_string/2,		% XML <-> String

	    xml_avs_to_terms/2,		% Att=Val's -> Att(Val)'s

	    xml_check/1			% XML
	  ]).

:- use_module(load).

:- use_module(library(lists), [delete/3]).
:- use_module(library(memfile), [atom_to_memory_file/2, free_memory_file/1,
				 memory_file_to_atom/2, new_memory_file/1,
				 open_memory_file/3]). 
:- use_module(library(sgml), [dtd/2, load_sgml_file/2, load_structure/3]).
:- use_module(library(sgml_write), [xml_write/3]).
:- use_module(library('http/http_client'), [http_get/3]).


/*------------------------------------------------------------
 *  Membership
 *------------------------------------------------------------*/

%%	xml_member(+Element:term, +XML:term) is nondet.
%
%	Succeeds for every Element part of XML.  Like member/2, but also
%	traverses the XML hierarchy.

xml_member(Element, XML) :-
	xml_member2(XML, Element).

xml_member2([Head|_], Element) :-
	xml_member2_h(Head, Element).
xml_member2([_|Tail], Element) :-
	xml_member2(Tail, Element).

xml_member2_h(element(E,A,C), element(E,A,C)).
xml_member2_h(element(_,_,C), Element) :-
	xml_member2(C, Element).


%%	xml_memberchk(+Element:term, +XML:term) is nondet.
%
%	Succeeds once if Element part of XML.  Like memberchk/2, but also
%	traverses the XML hierarchy.

xml_memberchk(Element, XML) :-
	xml_memberchk2(XML, Element).

xml_memberchk2([Head|_], Element) :-
	xml_memberchk2_h(Head, Element), !.
xml_memberchk2([_|Tail], Element) :-
	xml_memberchk2(Tail, Element).

xml_memberchk2_h(element(E,A,C), element(E,A,C)) :- !.
xml_memberchk2_h(element(_,_,C), Element) :-
	xml_memberchk2(C, Element).


/*------------------------------------------------------------
 *  Source to XML
 *------------------------------------------------------------*/

source_to_xml(element(E,A,C), [element(E,A,C)], _) :- !.
source_to_xml([pi(_)|Tail], Result, Options) :- !,
	source_to_xml(Tail, Result, Options).
source_to_xml([element(E,A,C)|Rest], [element(E,A,C)|Rest], _) :- !.
source_to_xml(file(File), XML, Options0) :- !,
	absolute_file_name(File, Abs),
	file_name_extension(_Base, Extension, Abs),
	open(Abs, read, Stream),
	(   memberchk(encoding(Encoding), Options0)
	->  delete(Options0, encoding(Encoding), Options),
	    set_stream(Stream, encoding(Encoding))
	;   Options0 = Options
	),
	(   ( Extension == html; Extension == htm )
	->  dtd(html, DTD),
	    load_structure(Stream, XML, [max_errors(-1), dtd(DTD), dialect(sgml), syntax_errors(quiet), shorttag(false) | Options])
	;   Extension == sgml
	->  load_sgml_file(Stream, XML)
	;   load_structure(Stream, XML, [dialect(xml), max_errors(-1), syntax_errors(quiet)|Options])
	),
	close(Stream).
source_to_xml(kb(KB), XML, Options) :- !,
	absolute_file_name(kb(KB), File),
	source_to_xml(file(File), XML, Options).
source_to_xml(url(URL), XML, Options) :- !,
	http_get(URL, XML, Options).
source_to_xml(atom(Atom), XML, Options) :- !,
        atom_to_memory_file(Atom, File),
        open_memory_file(File, read, In),
	(   memberchk(encoding(Encoding), Options)
	->  set_stream(In, encoding(Encoding)),
	    delete(Options, encoding(_), Options1)
	;   Options1 = Options
	),
	(   memberchk(dialect(_), Options1)
	->  load_structure(stream(In), XML, Options1)
        ;   load_structure(stream(In), XML, [dialect(xml)|Options1])
	),
        close(In),
        free_memory_file(File).
source_to_xml(Source, _, _Options) :-
	format('source_to_xml/3: failed on ~w~n', [Source]),
	fail.


%%	atom_to_xml(+Atom:atom, -XML:xml, +Options:list) is semidet.
%
%	Converts Atom to XML.
%
%	@param Options Global control of behaviour
%
%	* encoding(Encoding)
%	  Sets the encoding.  Defaults to =utf8=.

atom_to_xml(Atom, XML, Options) :-
	source_to_xml(atom(Atom), XML, Options).


%%	xml_to_atom(+XML:xml, -Atom:atom, +Options:list) is det
%
%	Converts XML to an ATOM.  Options are given to xml_write/3.

xml_to_atom(XML, Atom, Options) :-
	new_memory_file(MemFile),
	open_memory_file(MemFile, write, Stream),
	xml_write(Stream, XML, Options),
	close(Stream),
	memory_file_to_atom(MemFile, Atom),
	free_memory_file(MemFile).
	

%%	xml_to_string(+XML:xml, -String:string) is det.
%%	xml_to_string(-XML:xml, +String:string) is det.
%
%	Convert between XML and String.

xml_to_string(XML, String) :-
	var(String),
	nonvar(XML), !,
	with_output_to(string(String), xml_write(XML,[header(false),layout(false)])).
xml_to_string(XML, String) :-
        var(XML),
        nonvar(String),
        new_memory_file(MemFile),
        open_memory_file(MemFile, write, WriteMem),
        write(WriteMem, String),
        nl(WriteMem),
        close(WriteMem),
        open_memory_file(MemFile, read, ReadMem),
        load_structure(ReadMem, XML, [dialect(xml)]),
        close(ReadMem),
        free_memory_file(MemFile).


%%	xml_avs_to_terms(+AttVals:list, -Terms:list) is det.
%
%	Convert a list of attribute-value pairs to a list of terms were
%	attribute is the functor and value the only argument.

xml_avs_to_terms([], []).
xml_avs_to_terms([Att=Val|AVs], [Term|Terms]) :-
	functor(Term, Att, 1),
	arg(1, Term, Val),
	xml_avs_to_terms(AVs, Terms).


%%	xml_check(+XML:term) is det.
%
%	Checks an XML structure for correctness.

xml_check([element(E,As,C)]) :-
	xml_check_e(E),
	xml_check_as(As),
	xml_check_c(C).


xml_check_e(E) :-
	non_empty_atom(E), !.
xml_check_e(E) :-
	format('  illegal element ~q~n', [E]).

xml_check_as([]).
xml_check_as([Att=Value|AVs]) :-
	non_empty_atom(Att),
	atom(Value), !,
	xml_check_as(AVs).
xml_check_as([AV|AVs]) :-
	format('  illegal Att=Value: ~q~n', [AV]),
	xml_check_as(AVs).

xml_check_c([]).
xml_check_c([H|T]) :-
	atom(H), !,
	xml_check_c(T).
xml_check_c([element(E,As,C)|T]) :-
	xml_check_e(E),
	xml_check_as(As),
	xml_check_c(C), !,
	xml_check_c(T).
xml_check_c([H|T]) :-
	format('  illegal content: ~q~n', [H]),
	xml_check_c(T).


non_empty_atom(E) :-
	atom(E),
	atom_length(E, Len),
	Len > 0, !.

