:- consult('swat_rdf.pl').

agent_name('onto').
command_space('command').
ts_host('localhost').

connect :-
	retractall(ts(_,_)),
	command_space(CommandSpace),
	ts_host(TsHost),
	ts_port(TsPort),
	agent_id(ID),
	tspl_connect_to_ts(CommandSpace, TsHost, TsPort, ID, '', CommandTS),
	assert(ts(command, CommandTS)).

next_command(Cmd, Id, Params) :-
	write('Looking for command ... '),
	agent_name(AgentName),
	flush_output,
	ts(command, TS),
	tspl_formal_field(string, F0),
	tspl_actual_field(string, AgentName, F1),
	tspl_formal_field(string, F2),
	tspl_wildcard_field(F3),
	tspl_tuple([F0, F1, F2, F3], T),
	tspl_wait_to_take(TS, T, 0, Tuple),
	field_values(Tuple, [Id, _, Cmd|Params]),
	write(Cmd),
	writeln(' found!'),
	flush_output.

field_values(element(tuple, _, Fields), FieldValues) :-
	findall(FieldValue, (member(Field, Fields), field_value_(Field, FieldValue)), FieldValues).


respond(Id, Params) :-
	ts(command, TS),
	tspl_actual_field(string, Id, F0),
	tspl_tuple([F0|Params], T),
	tspl_write(TS, T),
	!.

agent_loop :-
	repeat,
	next_command(Cmd, Id, Params),
	process_command(Cmd, Id, Params),
	fail.

process_command(Cmd, Id, Params) :-
	(Cmd == 'stop'
		-> 	exit
		; true),
	(Cmd == 'lookup'
		-> 	Params = [OntName, OntTerm], 
			lookup(OntName, OntTerm, Category),
			tspl_actual_field(string, Category, CategoryField),
			respond(Id, [CategoryField])
		; true),
	(Cmd == 'class info'
		-> 	Params = [OntName, OntTerm], 
			class_info(OntName, OntTerm, Instances, Superclasses, Subclasses),
			list_to_csv(Instances, InstancesCSV),
			list_to_csv(Superclasses, SuperclassesCSV),
			list_to_csv(Subclasses, SubclassesCSV),
			tspl_actual_field(string, InstancesCSV, InstancesField),
			tspl_actual_field(string, SuperclassesCSV, SuperclassesField),
			tspl_actual_field(string, SubclassesCSV, SubclassesField),
			respond(Id, [InstancesField, SuperclassesField, SubclassesField])
		; true),
	(Cmd == 'instance info'
		-> 	Params = [OntName, OntTerm], 
			instance_info(OntName, OntTerm, Classes, PropValues),
			list_to_csv(Classes, ClassesCSV),
			list_to_csv(PropValues, PropValuesCSV),
			tspl_actual_field(string, ClassesCSV, ClassesField),
			tspl_actual_field(string, PropValuesCSV, PropValuesField),
			respond(Id, [ClassesField, PropValuesField])
		; true),
	(Cmd == 'siblings'
		-> 	Params = [OntName, OntTerm], 
			siblings(OntName, OntTerm, Siblings),
			list_to_csv(Siblings, SiblingsCSV),
			tspl_actual_field(string, SiblingsCSV, SiblingsField),
			respond(Id, [SiblingsField])
		; true),
	(Cmd == 'entities'
		-> 	Params = [OntName], 
			entities(OntName, Entities),
			list_to_csv(Entities, EntitiesCSV),
			tspl_actual_field(string, EntitiesCSV, EntitiesField),
			respond(Id, [EntitiesField])
		; true),
	!.

list_to_csv([], '').
list_to_csv([E], E) :- !.
list_to_csv([H|T], CSV) :-
	list_to_csv(T, TCSV),
	atom_concat(H, ',', H1),
	atom_concat(H1, TCSV, CSV).

start_alive_notify_thread :-
	ts(command, TS),
	agent_id(ID),
	agent_name(AgentName),
	tspl_actual_field(string, ID, F0),
	tspl_actual_field(string, 'alive', F1),
	tspl_actual_field(string, AgentName, F2),
	tspl_expiring_tuple([F0, F1, F2], 30000, T),
	tspl_write(TS, T, TupleID),
	repeat,
	sleep(20),
	tspl_update(TS, TupleID, T),
% EINE METHODE ZUM BEENDEN HINZUFÜGEN
	fail.

start_agent(ID, Port) :-
	assert(agent_id(ID)),
	assert(ts_port(Port)),
	connect,
	thread_create(start_alive_notify_thread,_,[]),
	agent_loop.

% (<ID>:String, "onto":String, "lookup":String, <OntName>:String, <OntTerm>:String) -> (<ID>:String, <OntCategory>:String)
lookup(OntName, OntTerm, Category) :-
	ont_connect(TS, OntName, NS),
	(typecheck(ont(TS, OntName, NS), OntTerm, Category); Category = notfound),
	!.

% (<ID>:String, "onto":String, "class info":String, <OntName>:String, <OntTerm>:String) -> (<ID>:String, <InstanceList>:String, <SuperclassList>:String, <SubclassList>:String)
class_info(OntName, OntTerm, Instances, Superclasses, Subclasses) :-
	ont_connect(_, OntName, _),
	findall(Instance, prdf(Instance, type, OntName), Instances),
	findall(Superclass, prdf(OntTerm, subclassof, Superclass), Superclasses),
	findall(Subclass, prdf(Subclass, subclassof, OntTerm), Subclasses).

% (<ID>:String, "onto":String, "instance info":String, <OntName>:String, <OntTerm>:String) -> (<ID>:String, <ClassList>:String, <PropValuesList>:String)
instance_info(OntName, OntTerm, Classes, PropValues) :-
	ont_connect(_, OntName, _),
	findall(Class, prdf(OntTerm, type, Class), Classes),
	findall(PV, (prdf(OntTerm, Prop, Value), (prdf(Prop, type, datatypeproperty);prdf(Prop, type, objectproperty)),format(atom(PV), '~w ~w', [Prop, Value])), PropValues).
	

% (<ID>:String, "onto":String, "siblings":String, <OntName>:String, <OntTerm>:String) -> (<ID>:String, <SiblingList>:String)
siblings(OntName, OntTerm, Siblings) :-
	ont_connect(TS, OntName, NS),
	(
		(
			typecheck(ont(TS, OntName, NS), OntTerm, individual),
			findall(Instance, (prdf(OntTerm, type, Class), prdf(Instance, type, Class)), Siblings)
		);
		(
			typecheck(ont(TS, OntName, NS), OntTerm, class),
			findall(Class, (prdf(OntTerm, subclassof, SuperClass), prdf(Class, subclassof, SuperClass)), Siblings)
		)
	).

% (<ID>:String, "onto":String, "entities":String, <OntName>:String) -> (<ID>:String, <EntitiesList>:String)
entities(OntName, Entities) :-
	ont_connect(_, OntName, _),
	findall(ClassName, prdf(ClassName, type, class), Classes),
	findall(InstanceName, (member(ClassName, Classes), prdf(InstanceName, type, ClassName)), Instances),
	append(Classes, Instances, Entities).

annotationproperty(Prop) :-
	member(Prop, [comment, isdefinedby, label, seealso, versioninfo]).
annotationproperty(Prop) :-
	prdf(Prop, type, annotationproperty).
completeclassmarker(Marker) :-
	member(Marker, [intersectionof, equivalentclass, unionof, complementof]).


% individual(+Ont, +IndId, -Annotations, -Types, -Values)

individual(IndId, Annotations, Types, Values) :-
	ont(O),
	individual(O, IndId, Annotations, Types, Values).

individual(Ont, IndId, Annotations, Types, Values) :-
	forall(prdf(Ont, IndId, Id, Type), assert(rdf_triple(IndId, Id, Type))),
	% find the types
	findall(type(Type), (rdf_triple(S, type, Type), retract(rdf_triple(S, type, Type))), Types),
	% find the annotations,
	annotation(IndId, Annotations),
	% find the values
	findall(value(Prop, Value), (rdf_triple(S, Prop, Value), (typecheck(Ont, URL, datatypeproperty);typecheck(Ont, URL, objectproperty)), retract(rdf_triple(S, Prop, Value))), Values), 
	dumprest.
	
dumprest :-
	\+ rdf_triple(_, _, _).

dumprest :-
	findall(Id, rdf_triple(_, Id, _), Ids),
	write('Unknown relations: '), writeln(Ids).

annotation(Id, Annotations) :-
	findall(Ann, annotationproperty(Ann), Anns),
	findall(annotation(Ann, Value), (rdf_triple(Id, Ann, Value), member(Ann, Anns), retract(rdf_triple(Id, Ann, Value))), Annotations).
	
% class(Ont, ClassId, Modality, Annotations, Supers) 

class(ClassId, Modality, Annotations, Supers) :-
	ont(O),
	class(O, ClassId, Modality, Annotations, Supers).
	
class(Ont, ClassId, Modality, Annotations, Supers) :-
	 forall(prdf(Ont, ClassId, Id, Type), assert(rdf_triple(ClassId, Id, Type))),
	 retract(rdf_triple(ClassId, type, class)),
	 modality(ClassId, Modality),
	 annotation(ClassId, Annotations),
	 findall(super(Class), (rdf_triple(ClassId, subclassof, Class), retract(rdf_triple(ClassId, subclassof, Class))), Supers), 
	 dumprest.

modality(ClassId, complete) :-
	rdf_triple(ClassId, Pred, _),
	completeclassmarker(Pred),
	!.
modality(_, partial).

typecheck(Ont, URL, class) :-
	prdf(Ont, URL, type, class).
	
typecheck(Ont, URL, individual) :-
	prdf(Ont, URL, type, Concept),
	prdf(Ont, Concept, type, class).

typecheck(Ont, URL, datatypeproperty) :-
	prdf(Ont, URL, type, datatypeproperty).

typecheck(Ont, URL, objectproperty) :-
	prdf(Ont, URL, type, objectproperty).
