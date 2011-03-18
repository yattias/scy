:- consult('tspl_sqls_mt.pl').
:- dynamic(ont/3).
:- dynamic(current_ont/1).

ont_connect(TS, OntoName, NS) :-
	ont(TS, OntoName, NS),
	retractall(current_ont(_)),
	assert(current_ont(OntoName)),
	!.

%ont_connect(TS, OntoName, NS) :-
%	ont(TSOld, OntoNameOld, NSOld),
%	tspl_disconnect(TSOld),
%	retract(ont(TSOld, OntoNameOld, NSOld)),
%	ont(OntoName, TS, NS).

ont_connect(TS, OntName, NS) :-
	ts_host(Server),
	ts_port(Port),
	tspl_connect_to_ts(OntName, TS, [host(Server), port(Port)]),
	id(type, Type),
	id(ontology, Ontology),
	rdf(ont(TS, _, _), NS1, Type, Ontology),
	string_concat(NS1, '#', NS),
%	retractall(ont(_,_,_)),
	retractall(current_ont(_)),
	assert(ont(TS, OntName, NS)),
	assert(current_ont(OntName)).

rdf(S, P, O) :-
	current_ont(Ont),
	ont(TS, Ont, _),
	rdf(ont(TS, _, _), S, P, O).

rdf(ont(TS, _, _), S, P, O) :-
	var_to_field(S, SF),
	var_to_field(P, PF),
	var_to_field(O, OF),
	tspl_formal_field(string, F),
	tspl_tuple([F,SF,PF,OF], T),
	tspl_read_all(TS, T, Tuples),
	!,
	member(Tuple,Tuples),
	field_to_var(Tuple, 1, S),
	field_to_var(Tuple, 2, P),
	field_to_var(Tuple, 3, O).

prdf(S, P, O) :-
	current_ont(ON),
	ont(TS, ON, NS),
	prdf(ont(TS, ON, NS), S, P, O).
	
prdf(Ont, S, P, O) :-
	(plain(S) -> plain_entity(Ont, SP, S); true),
	(plain(P) -> plain_entity(Ont, PP, P); true),
	(plain(O) -> plain_entity(Ont, OP, O); true),
	rdf(Ont, SP, PP, OP),
	plain_entity(Ont, SP, S),
	plain_entity(Ont, PP, P),
	plain_entity(Ont, OP, O).

var_to_field(V, F) :-
	 var(V),
	 tspl_formal_field(string, F).
var_to_field(V, F) :-
	 nonvar(V),
	 tspl_actual_field(string, V, F).

field_to_var(_, _, V) :-
	nonvar(V).
field_to_var(Tuple, Num, V) :-
	var(V),
	tspl_tuple_field(Tuple, Num, V).

plain(Ent) :-
	current_ont(ON),
	ont(TS, ON, NS),
	plain(ont(TS, ON, NS), Ent).
	
plain(_, Ent) :-
	var(Ent),
	!,
	fail.
plain(_, Ent) :-
	id(_, Ent),
	!.
plain(ont(_, _, NS), Ent) :-
	sub_atom(Ent, 0, _, _, NS),
	!,
	fail.
plain(_, _).

plain_entity(Ent, SEnt) :-
	current_ont(ON),
	ont(TS, ON, NS),
	plain_entity(ont(TS, ON, NS), Ent, SEnt).
plain_entity(_, Id, Type) :-
	id(Type, Id),
	!.
plain_entity(ont(_, _, _), Ent, literal(Lit, Type)) :-
	concat_atom([Lit, Rest], '^^', Ent),
	xsd(Type, Rest),
	!.
plain_entity(ont(_, _, _), Ent, Ent) :-
	sub_string(Ent, 0, 1, _, '"'),
	sub_string(Ent, _, 2, 2, '"@'),
	!.
plain_entity(ont(_, _, NS), Ent, SEnt) :-
	atom_concat(NS, SEnt, Ent),
	!.

id(alldifferent, 'http://www.w3.org/2002/07/owl#AllDifferent').                                                                                    
id(allvaluesfrom, 'http://www.w3.org/2002/07/owl#allValuesFrom').                                                                                  
id(annotationproperty, 'http://www.w3.org/2002/07/owl#AnnotationProperty').                                                                                    
id(cardinality, 'http://www.w3.org/2002/07/owl#cardinality').                                                                                      
id(class, 'http://www.w3.org/2002/07/owl#Class').                                                                                                  
id(comment, 'http://www.w3.org/2000/01/rdf-schema#comment').                                                                                       
id(complementof, 'http://www.w3.org/2002/07/owl#complementOf').                                                                                    
id(datarange, 'http://www.w3.org/2002/07/owl#DataRange').                                                                                          
id(datatypeproperty, 'http://www.w3.org/2002/07/owl#DatatypeProperty').                                                                            
id(differentfrom, 'http://www.w3.org/2002/07/owl#differentFrom').                                                                                  
id(disjointwith, 'http://www.w3.org/2002/07/owl#disjointWith').                                                                                    
id(distinctmembers, 'http://www.w3.org/2002/07/owl#distinctMembers').                                                                              
id(domain, 'http://www.w3.org/2000/01/rdf-schema#domain').                                                                                                       
id(equivalentclass, 'http://www.w3.org/2002/07/owl#equivalentClass').                                                                              
id(equivalentproperty, 'http://www.w3.org/2002/07/owl#equivalentProperty').                                                                        
id(first, 'http://www.w3.org/1999/02/22-rdf-syntax-ns#first').                                                                                     
id(functionalproperty, 'http://www.w3.org/2002/07/owl#FunctionalProperty').                                                                        
id(hasvalue, 'http://www.w3.org/2002/07/owl#hasValue').                                                                                            
id(intersectionof, 'http://www.w3.org/2002/07/owl#intersectionOf').                                                                                
id(inversefunctionalproperty, 'http://www.w3.org/2002/07/owl#InverseFunctionalProperty').                                                          
id(inverseof, 'http://www.w3.org/2002/07/owl#inverseOf').                                                                                          
id(isdefinedby, 'http://www.w3.org/2000/01/rdf-schema#isDefinedBy').                                                                               
id(label, 'http://www.w3.org/2000/01/rdf-schema#label').                                                                                           
id(maxcardinality, 'http://www.w3.org/2002/07/owl#maxCardinality').                                                                                
id(mincardinality, 'http://www.w3.org/2002/07/owl#minCardinality').                                                                                
id(nil, 'http://www.w3.org/1999/02/22-rdf-syntax-ns#nil').                                                                                         
id(objectproperty, 'http://www.w3.org/2002/07/owl#ObjectProperty').                                                                                
id(oneof, 'http://www.w3.org/2002/07/owl#oneOf').                                                                                                  
id(onproperty, 'http://www.w3.org/2002/07/owl#onProperty').                                                                                        
id(ontology, 'http://www.w3.org/2002/07/owl#Ontology').                                                                                             
id(range, 'http://www.w3.org/2000/01/rdf-schema#range').                                                                                           
id(rest, 'http://www.w3.org/1999/02/22-rdf-syntax-ns#rest').                                                                                       
id(restriction, 'http://www.w3.org/2002/07/owl#Restriction').                                                                                      
id(sameas, 'http://www.w3.org/2002/07/owl#sameAs').                                                                                                
id(seealso, 'http://www.w3.org/2000/01/rdf-schema#seeAlso').                                                                                       
id(somevaluesfrom, 'http://www.w3.org/2002/07/owl#someValuesFrom').                                                                                
id(subclassof, 'http://www.w3.org/2000/01/rdf-schema#subClassOf').                                                                                 
id(subpropertyof, 'http://www.w3.org/2000/01/rdf-schema#subPropertyOf').
id(symmetricproperty, 'http://www.w3.org/2002/07/owl#SymmetricProperty').
id(thing, 'http://www.w3.org/2002/07/owl#Thing').
id(transitiveproperty, 'http://www.w3.org/2002/07/owl#TransitiveProperty').
id(type, 'http://www.w3.org/1999/02/22-rdf-syntax-ns#type').
id(unionof, 'http://www.w3.org/2002/07/owl#unionOf').
id(valuesfrom, 'http://www.w3.org/2002/07/owl#valuesFrom').
id(versioninfo, 'http://www.w3.org/2002/07/owl#versionInfo').
xsd(string, 'http://www.w3.org/2001/XMLSchema#string').
xsd(normalizedstring, 'http://www.w3.org/2001/XMLSchema#normalizedString').
xsd(boolean, 'http://www.w3.org/2001/XMLSchema#boolean').
xsd(decimal, 'http://www.w3.org/2001/XMLSchema#decimal').
xsd(float, 'http://www.w3.org/2001/XMLSchema#float').
xsd(double, 'http://www.w3.org/2001/XMLSchema#double').
xsd(integer, 'http://www.w3.org/2001/XMLSchema#integer').
xsd(nonnegativeinteger, 'http://www.w3.org/2001/XMLSchema#nonNegativeInteger').
xsd(positiveinteger, 'http://www.w3.org/2001/XMLSchema#positiveInteger').
xsd(nonpositiveinteger, 'http://www.w3.org/2001/XMLSchema#nonPositiveInteger').
xsd(negativeinteger, 'http://www.w3.org/2001/XMLSchema#negativeInteger').
xsd(long, 'http://www.w3.org/2001/XMLSchema#long').
xsd(int, 'http://www.w3.org/2001/XMLSchema#int').
xsd(short, 'http://www.w3.org/2001/XMLSchema#short').
xsd(byte, 'http://www.w3.org/2001/XMLSchema#byte').
xsd(unsignedlong, 'http://www.w3.org/2001/XMLSchema#unsignedLong').
xsd(unsignedint, 'http://www.w3.org/2001/XMLSchema#unsignedInt').
xsd(unsignedshort, 'http://www.w3.org/2001/XMLSchema#unsignedShort').
xsd(unsignedbyte, 'http://www.w3.org/2001/XMLSchema#unsignedByte').
xsd(hexbinary, 'http://www.w3.org/2001/XMLSchema#hexBinary').
xsd(base64binary, 'http://www.w3.org/2001/XMLSchema#base64Binary').
xsd(datetime, 'http://www.w3.org/2001/XMLSchema#dateTime').
xsd(time, 'http://www.w3.org/2001/XMLSchema#time').
xsd(date, 'http://www.w3.org/2001/XMLSchema#date').
xsd(gyearmonth, 'http://www.w3.org/2001/XMLSchema#gYearMonth').
xsd(gyear, 'http://www.w3.org/2001/XMLSchema#gYear').
xsd(gmonthday, 'http://www.w3.org/2001/XMLSchema#gMonthDay').
xsd(gday, 'http://www.w3.org/2001/XMLSchema#gDay').
xsd(gmonth, 'http://www.w3.org/2001/XMLSchema#gMonth').
xsd(anyuri, 'http://www.w3.org/2001/XMLSchema#anyURI').
xsd(token, 'http://www.w3.org/2001/XMLSchema#token').
xsd(language, 'http://www.w3.org/2001/XMLSchema#language').
xsd(nmtoken, 'http://www.w3.org/2001/XMLSchema#NMTOKEN').
xsd(name, 'http://www.w3.org/2001/XMLSchema#Name').
xsd(ncname, 'http://www.w3.org/2001/XMLSchema#NCName').

