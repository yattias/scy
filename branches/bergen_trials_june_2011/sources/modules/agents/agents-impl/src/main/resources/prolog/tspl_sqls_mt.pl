/*  $Id: tspl.pl,v 1.4 2011-03-24 08:53:46 weinbrenner Exp $
 *  
 *  File       tspl.pl
 *  Part of    SQLSpaces
 *  Author     Stefan Weinbrenner, weinbrenner@collide.info
 *             Anjo Anjewierden, a.a.anjewierden@utwente.nl
 *  Purpose    Prolog API
 *  Works with SWI-Prolog (www.swi-prolog.org)
 *  
 *  Notice     Copyright (c) 2009  University of Duisburg-Essen
 *             Copyright (c) 2009  University of Twente
 *  
 *  History    29/06/09  (Created)
 *             21/03/11  (Last modified)
 */ 


/*------------------------------------------------------------
 *  Directives
 *------------------------------------------------------------*/

:- module(tspl,
       [ tspl_connect_to_ts/3,      % Space -> TS <- Options
         tspl_disconnect/1,         % TS

         tspl_formal_field/2,       % Class -> Field
         tspl_actual_field/3,       % Class x Value -> Field
         tspl_semiformal_field/2,   % Pattern -> Field
         tspl_inverse_field/2,      % Value -> Field
         tspl_wildcard_field/1,     % -> Field
         tspl_tuple/2,              % Fields <-> Tuple
         tspl_expiring_tuple/3,     % Fields -> Tuple
         tspl_tuple_field/3,        % Tuple x Nth0 -> Value
         tspl_tuple_id/2,           % Tuple -> Id

         tspl_read/3,               % TS x Query -> Tuple
         tspl_read_first/3,         % TS x Query -> Tuple
         tspl_read_all/3,           % TS x Query -> Tuples
         tspl_take/3,               % TS x Query -> Tuple
         tspl_take_first/3,         % TS x Query -> Tuple
         tspl_take_all/3,           % TS x Query -> Tuples
         tspl_delete/2,             % TS x Query
         tspl_delete_first/2,       % TS x Query
         tspl_delete_all/3,         % TS x Query -> Count
         tspl_count/3,              % TS x Query -> Count

         tspl_wait_to_read/3,       % TS x Query -> Tuple
         tspl_wait_to_read/4,       % TS x Query x TimeOut -> Tuple
         tspl_wait_to_read_first/3, % TS x Query -> Tuple
         tspl_wait_to_read_first/4, % TS x Query x TimeOut -> Tuple
         tspl_wait_to_take/3,       % TS x Query -> Tuple
         tspl_wait_to_take/4,       % TS x Query x TimeOut -> Tuple
         tspl_wait_to_take_first/3, % TS x Query -> Tuple
         tspl_wait_to_take_first/4, % TS x Query x TimeOut -> Tuple

         tspl_write/2,              % TS x Tuple
         tspl_write/3,              % TS x Tuple -> TupleID
         tspl_update/3,             % TS x TupleID x Tuple

         tspl_get_all_spaces/2,     % TS -> Spaces
	 
    	 tspl_transaction/2,        % TS x Transaction

         tspl_event_register/5,     % TS x Command x Query x CallBack x Seq
         tspl_event_deregister/2,   % TS x Seq
         tspl_replace_tuple_field/4

       ]).

/** <module> Prolog client library for SQLSpaces

This module provides predicates to access an SQLSpaces server. The SQLSpaces are an implementation of the TupleSpaces concept. TupleSpaces are an elegant and easy way of creating distributed software systems that are robust and modular since they are loosely coupled. In addition to the original set of operations (in, out, rd) and to some typical features already known by other TupleSpaces implementations, SQLSpaces furthermore introduces some unique features like multi-language support, versioning, reverse structured naming and wildcard fields. This module allows programmers to connect, to define and execute read and write queries and to register for event notifications. More information on the SQLSpaces can be found at http://sqlspaces.collide.info .

---++ Background

---+++ TupleSpaces

The idea of TupleSpaces goes back to the mid-80s, when they were first introduced by Gelernter and Carriero from Yale University together with the Linda coordination language. The idea is to have a client server architecture with a communication solely based on tuples, i.e. ordered lists of primitive data. The clients can write and take tuples from the server and do not need to know anything about other clients. They just communicate over the server without "knowing" any other client's address. The query mechanisms of a TupleSpaces system are mainly associative, i.e. the queries are defined by creating templates tuples with so-called formal fields, that are interpreted as wildcards.

---+++ SQLSpaces

As an implementation of the TupleSpaces concept, the SQLSpaces are using a relational database to store the data. The server is written in Java, but there are clients for several languages available, including Java, Prolog, PHP, Ruby and .Net.

---+++ Possibilities

By using the SQLSpaces it is quite easy to build up a distributed system, since the knowledge that all clients need have is minimal. Since all communication is sent to the server, no client needs to have ips or similar addresses of the other clients (as long as they know where the server is). Moreover the coordination of the message exchange is also done by the SQLSpaces server that supports blocking as well as non-blocking queries and also asynchronous notifications. There is also no need to explicitly create a protocol, because the protocol is defined rather implicit by choosing the exchange format of the tuples. Especially for Prolog this is step is not very difficult, since the tuple format is closely related to Prolog's own syntax.

---++ Operations

To get a platform based on the SQLSpaces running, you should visit http://sqlspaces.collide.info to get the latest server. Download the server bundle and extract it. You will find a startServer.sh (for Linux and Mac) and startServer.bat (for Windows) that will start you a server you can use.

---+++ Connection Handling

In order to access an SQLSpaces server, you first have to establish a connection using: tspl_connect_to_ts/3.  This predicate returns a reference to a connection object that will be needed by further calls, so better store it. A typical way of dealing with this is:

==
:- dynamic
	con/2.		% Space -> TS handle

ts_host(localhost).
ts_port(2525).

connection(SpaceName, TS) :-
	con(SpaceName, TS), !.

connection(SpaceName, TS) :-
	ts_host(Server),
	ts_port(Port),
	tspl_connect_to_ts(SpaceName, -TS, [host(Server), port(Port)]),
	assert(con(SpaceName, TS)).
==

By handling the connection in this way, you can easily access the connection's reference by the predicate *connection* and let it be lazily created the first time when it is accessed. Notice that this example code can also handle connections to several disjoint spaces (by using different values for SpaceName).

If the connection is no longer needed, it is advised to call tspl_disconnect/1 in order to gracefully end the connection.


---+++ Tuples and Fields

After the connection has been established, it is possible to read from and write to the space. However, before a read or write query can be sent, it is first necessary to understand the basics of the tuple creation: Tuples consist of actual fields and formal fields. Actual fields (tspl_actual_field/3) are fields that have a data type and a value, whereas formal fields (tspl_formal_field/2) only have values. The data types that characterize the fields are derived from the Java type system plus two special types. The possible types are therefore:

* string
  Arbitrary string of UTF-8 characters
* character
  Single UTF-8 character
* boolean
  Either true or false
* byte
  8-bit signed integer (-128...127)
* short
  16-bit signed integer (-32768...32767)
* integer
  32-bit signed integer (-2147483648...2147483647)
* long
  64-bit signed integer (-9223372036854775808...9223372036854775807)
* float
  32-bit floating point number
* double
  64-bit floating point number
* xml
  Arbitrary XML string that is either using the notation of the SGML package (element(node, [], value)) or an xml string ('<node>value</node>').
* binary
  Base64 encoded string representing binary data
  
Although the Prolog type system is not that fine-grained, it is necessary to
program in this system, because the whole SQLSpaces system is based on that to
be as interoperable as possible between different programming
languages. This is the reason, why these data types need to be explicitly
mentioned during the creation of the fields. The following example shows, how
a tuple that represents a user is created.

==
user_tuple(Name, Age, Tuple) :-
	tspl_actual_field(string, 'User', F0),
	tspl_actual_field(string, Name, F1),
	tspl_actual_field(integer, Age, F2),
	tspl_tuple([F0,F1,F2], Tuple).
==

---+++ Writing, Reading, Taking and Updating

After you have created a tuple, you can write such a tuple to the TupleSpace using tspl_write/2:

==
connection(userspace, TS),
user_tuple('Stefan', 27, Tuple),
tspl_write(TS, Tuple).
==

There are two operations to get tuples from a space, read and take. If something is read (tspl_read/3) from the TupleSpace, it means that the matching tuple is handed to the client, but still stays in the space, whereas a take (tspl_take/3) operation also deletes the matching tuple. In order to call a read or a take operation, a tuple template has first to be created that is used for matching. If you want to get an arbitrary tuple that represents a user, use the following code:

==
fetch_user(UserTuple) :-
	tspl_actual_field(string, 'User', F0),
	tspl_formal_field(string, F1),
	tspl_formal_field(integer, F2),
	tspl_tuple([F0,F1,F2], Template),
	connection(userspace, TS),
	tspl_read(TS, Template, UserTuple).
==

The operations tspl_read/3 and tspl_take/3 only return *one* matching tuple and evaluate *immediately*, i.e. they will not bind the response variable, if nothing was found. There are, however, the variants tspl_read_all/3 and tspl_take_all/3 that return *all* matching tuple and there are the predicates tspl_wait_to_read/3 and tspl_wait_to_take/3 that will *block* the interpretation of the program until a match is found. It is also possible to define a *timeout* for the blocking commands by using tspl_wait_to_read/4 and tspl_wait_to_take/4.

After one or more tuples have been received from the server, you can access the fields with the predicate tspl_tuple_field/3:

==
print_user(UserTuple) :-
	fetch_user(UserTuple),
	tspl_tuple_field(UserTuple, 1, Name),
	tspl_tuple_field(UserTuple, 2, Age),
	write('User: '), writln(Name),
	write('Age: '), writln(Age).
==

Finally, if you just want to change a given tuple, you do not need to execute a take and a write operation, but you can also just update a tuple with the predicate tspl_update/3. Therefore you need the tuple id: After a tuple is written into the space, it gets a unique tuple id. You can get this id either directly after the write by using tspl_write/3 instead of tspl_write/2, or by getting the id from a result of read/take with the predicate tspl_tuple_id/2. The following examples shows how it works:

==
increase_age(UserName) :-
	tspl_actual_field(string, 'User', F0),
	tspl_actual_field(string, UserName, F1),
	tspl_formal_field(integer, F2),
	tspl_tuple([F0,F1,F2], Template),
	connection(userspace, TS),
	tspl_read(TS, Template, UserTuple),
	tspl_tuple_id(UserTuple, Id),
	tspl_tuple_field(UserTuple, 2, OldAge),
	NewAge is OldAge + 1,
	tspl_actual_field(integer, NewAge, F2New),
	tspl_tuple([F0,F1,F2New], NewTuple),
	tspl_update(TS, Id, NewTuple).
==

---++ Extended Operations

Additionally to the typical TupleSpaces operations mentioned above, the SQLSpaces have some extended operations.

---+++ NullTuple

A special tuple is the tuple without any fields. It is called NullTuple and it matches *everything* (in this space). So if you need to clear a whole space, it could look like this:

==
clean_space(SpaceName) :-
	tspl_tuple([], NullTuple),
	connection(SpaceName, TS),
	tspl_take_all(SpaceName, NullTuple, _).
==

---+++ Wildcard Fields

Sometimes the exact signature of a tuple is not known. Especially when complex data structures have been flattened in order to "tupleize" e.g. a tree structure, there will be a variable number of fields. To define now a query that matches all of these different tuples, wildcard fields can be used, which match an arbitrary number of fields, which are of arbitrary types. If you want to fetch all the tuples starting with a string field "action" and a type "login" you can use the following code:

==
tspl_actual_field(string, action, F0),
tspl_actual_field(string, login, F1),
tspl_wildcard_field(F2),
tspl_tuple([F0,F1,F2], Template),
tspl_read_all(TS, Template, AllActions).
==


---+++ Semiformal Fields

In addition to a the wildcard and the formal field there is also the possibility to create a field that does not match an arbitrary value or an arbitrary number of fields, but also one field that matches one field but with an "semi-arbitrary" value. This is currently only supported for string fields that contain '*' as a wildcard character that matches all sequences of characters.

==
tspl_actual_field(string, action, F0),
tspl_semiformal_field('oldValue=*', F1),
tspl_tuple([F0,F1], Template),
tspl_read_all(TS, Template, AllActions).
==

---+++ Inverse Fields

The last special field type is the inverse field. It allows a developer to create a field that does *not* match a given value. This is also currently only supported for string fields, but maybe extended in later versions also to other data types.

==
tspl_actual_field(string, sheep, F0),
tspl_inverse_field(white, F1),
tspl_tuple([F0,F1], Template),
tspl_read_all(TS, Template, NonWhiteSheep).
==

---+++ Transactions

All operations on the SQLSpaces server are interpreted on their own and are processed in the order of their arrival. This carries the typical problems of relational databases like phantom reads, lost updates etc. to the tuple level. To cope with these problems, SQLSpaces offer transaction support to encapsulate several operations into one action. Transactions commands have either the value *begin*, *commit* or *abort*. They can be used as follows:

==
connection(userspace, TS),
tspl_transaction(TS, begin),
% do some stuff
tspl_transaction(TS, commit).
==

---+++ Expiration

Sometimes it is desirable to let tuples be deleted after a certain time. This expiration of tuples can be done by the server automatically by defining this expiration before the corresponding write or update command. For this definition it is necessary to use tspl_expiring_tuple/3 as an alternative constructor for the tuple. Expiration is always defined in milliseconds. Example:

==
start_session(UserName) :-
	connection(sessionspace, TS),
	tspl_actual_field(string, session F0),
	tspl_actual_field(string, UserName, F1),
	tspl_expiring_tuple([F0,F1], 3600000, Tuple),
	% session expires after an hour
	tspl_write(TS, Tuple).
==

---+++ Callbacks

Additionally to the operations mentioned above, which are all queries actively sent from the client to the server, there are also notifications that the server can send to the client. A client can register and deregister itself for these so-called callbacks and define for which tuples, which operations and which spaces this registration should be valid. The corresponding predicates are tspl_event_register/5 and tspl_event_deregister/2. The 4th argument of tspl_event_register/5 must be the name of a predicate that will be called whenever a notification for this callback is received. This callback predicate needs to have the signature my_callback(Command, Seq, Before, After), where =Command= will be one of *write*, *delete* (take or expiration) and *update*, =Seq= is the unique sequence number and =Before= and =After= are the tuples before, respectively after the execution of the operation that triggered the callback. For the call of the callback predicate a new thread will be created. A callback that will count the user logins, might look like that:

==
callback_register :-
	connection(sessionspace, TS),
	tspl_actual_field(string, session, F0),
	tspl_formal_field(string, F1),
	tspl_tuple([F0, F1], Template),
	tspl_event_register(TS, write, Template, count_actions, _).
==

---+++ Performance Issues

There are two major locations, where the performance might get into trouble: The first problem might occur, if the answer is very big, i.e. either a huge amount of tuples are returned or one really big tuple is being returned. This will cost cpu time and memory on the server to construct the response packet. If you are actually not interested in the content of the response and just want to delete the tuples (i.e. while using tspl_take_all/3) or count the matches (by using tspl_read_all/3), you should rather use predicates that don't have return values. So if you want to delete a tuple, rather use tspl_delete/2 (instead of tspl_take/3) or tspl_delete_all/2 (instead of tspl_take_all/3). If you just want to know the number of matching tuples to a given template tuple, you should use tspl_count/3 (instead of tspl_read_all/3).  

The second problem might occur when finding a random tuple in a large set of tuples. The calculation of a random match is normally a very quick and no problem at all. However, the more tuples match, the longer this takes, and to prevent this, you can choose to drop the randomization and just get the first result in the database in order to speed calls up. For this, you just use the predicates tspl_read_first/3, tspl_take_first/3, tspl_delete_first/3, tspl_wait_to_read_first/3, tspl_wait_to_read_first/4, tspl_wait_to_take_first/3, tspl_wait_to_take_first/4.

Both optimizations are developed for amounts of one million tuples or higher. In cases with perhaps 10.000 tuples there is no real difference between the "normal" predicates and the faster ones.


---++ Typical Patterns

tspl has been developed with the Java interface in mind, to have the interfaces as similar as possible on a conceptual level. Obviously this interface is therefore not very prolog-ish, but this can be achieved with some simple patterns.

The first pattern was already introduced before and deals with connection handling. Often you need a reference to a connection that has been created before. So in order to store the connection and to create it the first time it is needed, it is recommended use a predicate like the following, with the corresponding adaptations for the concrete system:

==
:- dynamic
	con/2.

ts_host(localhost).
ts_port(2525).

connection(SpaceName, TS) :-
	con(SpaceName, TS), !.

connection(SpaceName, TS) :-
	ts_host(Server),
	ts_port(Port),
	tspl_connect_to_ts(SpaceName, TS, [host(Server), port(Port)]),
	assert(con(SpaceName, TS)).
==

This code makes it easy to have only one connection to a specific space, so that this connection can be accessed quite conveniently. If the connection has not been established yet, it will be, and the reference will be stored for later queries.
	
Another pattern deals with the access of the tuplespace. Often a tuplespace will be used as a kind of external knowledge base. However, the read access to this base is not that transparent as it could be. To achieve more transparency a predicate layer in between needs to be introduced that takes care about defining queries and binding variables. In the user example mentioned earlier, it would be more elegant to have a functor user and to have all users stored in the form "user(Name, Age)", so that the two arguments can be either bound or not. The following predicate does exactly this:

==
user(Name, Age) :-
	user_pre(Name, Age, Tuples),
	member(Tuple, Tuples),
	user_post(Name, Age, Tuple).

% do the query
user_pre(Name, Age, Tuples) :-
	connection(userspace, TS),
	tspl_actual_field(string, user, F0),
	determine_field_type(string, Name, F1),
	determine_field_type(integer, Age, F2),
	tspl_tuple([F0,F1,F2], Template),
	tspl_read_all(TS, Template, Tuples).

% bind unbound variables
user_post(Name, Age, Tuple) :-
	tspl_field_value(Tuple, 1, Name),
	tspl_field_value(Tuple, 2, Age).

% choose the correct field type (actual or formal)
% depending on whether the Value is bound or not
determine_field_type(Type, Value, Field) :-
	var(Value),
	tspl_formal_field(Type, Field).

determine_field_type(Type, Value, Field) :-
	nonvar(Value),
	tspl_actual_field(Type, Value, Field).
==

If this code is used during the interpretation of "user('Stefan', Age)", it works like this: First the predicate user_pre is called, where a template tuple for the readall operation is built up. This template tuple is created using the auxiliary predicate determine_field_type, which either creates a formal or an actual field, depending on whether the variable Value is bound or not. After the template is created, the readall operation is executed. The tuples that are returned from the server are then chosen one after the other by the member call and are passed to the user_post predicate. This predicate will then bind the remaining unbound variables by a call of tspl_tuple_field. It is obvious that such predicates can only be implemented with the knowledge about the specific domain	and the tuple format in mind.

A last more complex pattern can be used to implement an agent that reacts on certain requests. The general tuple format for such cases that we recommended is to have a unique id in the first field, the type of the request in the second and some payload for the request in the following fields. The unique id in the first field will be used for the response, so that the client that triggered the request can easily determine the answer (he just needs to waitToTake a tuple (<myUniqueId>, *). Given this tuple format, the structure of the agent code may look like this:

==

agent_loop :-
	repeat,
	next_command(Cmd, Id, Params),
	process_command(Cmd, Id, Params),
	fail.

next_command(Cmd, Id, Params) :-
	write('Looking for command ... '),
	flush_output,
	connection(commandspace, TS),
	tspl_formal_field(string, F0),
	tspl_wildcard_field(F1),
	tspl_tuple([F0, F1], T),
	tspl_wait_to_take(TS, T, 0, Tuple),
	field_values(Tuple, [Id, _, Cmd|Params]),
	write(Cmd),
	writeln(' found!'),
	flush_output.

process_command(Cmd, Id, Params) :-
	(Cmd == 'stop'
		-> 	respond(Id, ['will exit']),
			exit
		; true),
	(Cmd == 'ping'
		-> 	writeln('PING!'),
			respond(Id, ['pong'])
		; true),
	(Cmd == 'calc'
		-> 	do_some_calculation(Params, Result),
			respond(Id, [Result])
		; true),
	!.

respond(Id, Params) :-
	connection(commandspace, TS),
	tspl_actual_field(string, Id, F0),
	tspl_tuple([F0|Params], T),
	tspl_write(TS, T),
	!.

==

For further conceptual or technical questions regarding the SQLSpaces or blackboard systems in generel, look at the homepage of the SQLSpaces ( http://sqlspaces.collide.info ), especially the manual and the FAQ.

@author Stefan Weinbrenner (code, documentation)
@author Anjo Anjewierden (documentation)
*/

:- use_module(library('socket')).
:- use_module(library('random')).
:- use_module(library('sgml')).
:- use_module(library('sgml_write')).
:- use_module(library('memfile')).

:- module_transparent
	tspl_event_register/5.

/*------------------------------------------------------------
 *  Connecting and disconnecting
 *------------------------------------------------------------*/

%%	tspl_connect_to_ts(+Space:atom, -TS:term, +Options:list) is semidet.
%
%	Succeeds if Space is the name of a TupleSpace and the connection to
%	this space is the handle TS.   Options are: 
%
%	* host(Host)
%		Address of the Host on which the TupleSpace server runs.
%		Defaults to =localhost=.
%	* port(Port)
%		Port on which the TupleSpace server runs.  Defaults to =2525=.
%	* user(User)
%		User.
%	* password(Password)
%		Password.

tspl_connect_to_ts(Space, TS, Options) :-
	(memberchk(host(Host), Options) -> true; Host = localhost),
	(memberchk(port(Port), Options) -> true; Port = 2525),
        SpaceXML = element(spaces, [], [element(space, [], [Space])]),
	(   memberchk(user(User), Options),
	    memberchk(password(Password), Options)
	->  UserXML = element(user, [], [User]),
	    PasswordXML = element(password, [], [Password]),
            connect_ts_([SpaceXML,UserXML,PasswordXML], Host, Port, TS, SpaceId)
	;   connect_ts_([SpaceXML], Host, Port, TS, SpaceId)
	),
        assert(tspl_ts_spacename_id(TS, Space, SpaceId)).


%%     tspl_disconnect(+TS:term) is det.
%
%       Disconnects from TS.

tspl_disconnect(TS) :-
        uid(UID),
        xml_to_string_([element(disconnect, [id=UID], [])], XMLString),
        send_receive_xml_(XMLString, UID, _, TS).


/*------------------------------------------------------------
 *  Defining fields for a query
 *------------------------------------------------------------*/

%%      tspl_formal_field(+Class:atom, -Field:xml) is det.
%
%	Creates a formal Field which matches the type given by Class (often =string=).

tspl_formal_field(Class, element(field, [type=Class, fieldtype=formal], [])).


%%     tspl_actual_field(+Class:atom, +Value:any, -Field:xml) is det
%
%     Creates an actual (specific) Field which matches the type given by
%     Class and the given Value.

tspl_actual_field(xml, element(Node, Attr, Value), element(field, [type=xml, fieldtype=actual], [StrValue])) :-
	xml_to_string_([element(Node, Attr, Value)], XMLValue),
	string_to_atom(XMLValue, StrValue).
tspl_actual_field(Class, Value, element(field, [type=Class, fieldtype=actual], [StrValue])) :-
	(   float(Value)		% for large floats string_to_atom/2 uses E notation
		->  format(atom(StrValue), '~10f', [Value])
		;   string_to_atom(Value, StrValue)
	).


%%     tspl_inverse_field(+Value:atom, -Field:xml) is det.
%
%     Creates an inverse field, which is a string field that contains a string
%	that should *not* be matched.

tspl_inverse_field(Value, element(field, [type=string, fieldtype=inverse], [Value])).


%%     tspl_semiformal_field(+Pattern:atom, -Field:xml) is det.
%
%     Creates a semiformal field, which is a string field that may contain the '*' 
%     character that will be expanded to match any character sequence.

tspl_semiformal_field(Pattern, element(field, [type=string, fieldtype=semiformal], [Pattern])).


%%     tspl_wildcard_field(-Field:xml) is det.
%
%     Field is a wildcard (matches anything).

tspl_wildcard_field(element(field, [fieldtype=wildcard], [])).


%%     tspl_tuple(+Fields:list, -Tuple:xml) is det.
%%     tspl_tuple(-Fields:list, +Tuple:xml) is det.
%
%     Tuple is constructed out of Fields.  See tspl_formal_field/2,
%     tspl_actual_field/3 and tspl_wildcard_field/1 for creating the fields.

tspl_tuple(Fields, element(tuple, [], Fields)).


replace_member(LS,Index,Element,Res):-
	replace_member(LS,Index,Element,0,Res).
replace_member([],_,_,[]).
replace_member([_|Rest],Index,Element,Count,[Element|Rest]):-
	Index=:=Count,!.
replace_member([First|Rest],Index,Element,Count,[First|Rest2]):-
	Count2 is Count +1,
	replace_member(Rest,Index,Element,Count2,Rest2).

tspl_replace_tuple_field(element(tuple,_,Fields),Index,FieldVal,element(tuple,[],Fields2)):-
    nonvar(FieldVal),
    nonvar(Index),
    replace_member(Fields,Index,FieldVal,Fields2).

%%     tspl_tuple_field(+Tuple:xml, ?Nth0:int, -Value:any) is det.
%
%     Value is the value of the Nth0 field in Tuple.

tspl_tuple_field(element(tuple, _, Fields), Num, FieldVal) :-
        nth0(Num, Fields, Field),
        field_value_(Field, FieldVal).


%%     tspl_tuple_id(+Tuple:xml, -Id:atom) is det.
%
%     Id is the identifier of Tuple.

tspl_tuple_id(element(tuple, Attr, _), Id) :-
	memberchk(id=Id, Attr).


/*------------------------------------------------------------
 *  Reading from the TupleSpace
 *------------------------------------------------------------*/

%%     tspl_read(+TS:term, +Query:term, -Tuple:term) is semidet.
%
%     Tuple matches Query in TS.  tspl_read/3 randomly returns one of the
%     tuples that matches Query.  The same tuple may be returned many times
%     over.  The predicate fails if TS does not contain any tuples matching
%     the query.
%
%     @bug It appears that if a query with X fields is created and TS does
%     not contain any tuples with X fields, tspl_read/3 hangs (or waits for
%     a tuple to appear).

tspl_read(TS, Query, Tuple) :-
        query_tuple_(Query, [Tuple|_], TS, read, 0, true, true).


%%     tspl_read_first(+TS:term, +Query:term, -Tuple:term) is semidet.
%
%     Tuple matches Query in TS.  tspl_read_first/3 returns the first tuple
%     that matches Query (according to the database). In spaces with many tuples,
%     this will be faster than the randomized version tspl_read/3. The predicate 
%     fails if TS does not contain any tuples matching the query.

tspl_read_first(TS, Query, Tuple) :-
        query_tuple_(Query, [Tuple|_], TS, read, 0, true, false).


%%     tspl_read_all(+TS:term, +Query:term, -Tuples:list) is det.
%
%     Tuples is a list of all tuples matching Query in TS.  See also tspl_read/3.

tspl_read_all(TS, Query, Scan) :-
        query_tuple_(Query, Scan, TS, readall, 0, true, true).


%%     tspl_take(+TS:term, +Query:term, -Tuple:term) is semidet.
%
%     Tuple matches Query in TS.  tspl_take/3 randomly returns one of the
%     tuples that matches Query and removes it from the TS.

tspl_take(TS, Query, Tuple) :-
        query_tuple_(Query, [Tuple|_], TS, take, 0, true, true).


%%     tspl_take_first(+TS:term, +Query:term, -Tuple:term) is semidet.
%
%     Tuple matches Query in TS.  tspl_take_first/3 returns the first tuple
%     that matches Query (according to the database) and removes it. In spaces
%     with many tuples, this will be faster than the randomized version 
%     tspl_take/3. The predicate fails if TS does not contain any tuples 
%     matching the query.

tspl_take_first(TS, Query, Tuple) :-
        query_tuple_(Query, [Tuple|_], TS, take, 0, true, false).

%%     tspl_take_all(+TS:term, +Query:term, -Tuples:list) is det.
%
%     Tuples is a list of all tuples matching Query in TS.  These tuples are
%     removed from the TS.  See also tspl_take/3.

tspl_take_all(TS, Query, [Tuples]) :-
        query_tuple_(Query, Tuples, TS, takeall, 0, true, true).


%%     tspl_delete(+TS:term, +Query:term) is det.
%
%     A tuple matching Query in TS is deleted.

tspl_delete(TS, Query) :-
        query_tuple_(Query, [Tuple|_], TS, take, 0, false, true),
	  tspl_tuple_field(Tuple, 0, Count),
        Count == 1.

%%     tspl_delete_first(+TS:term, +Query:term) is semidet.
%
%     A tuple matching Query in TS is deleted. In spaces with many tuples, this
%     will be faster than the randomized version tspl_delete/3. The predicate 
%     fails if TS does not contain any tuples matching the query.

tspl_delete_first(TS, Query) :-
        query_tuple_(Query, [Tuple|_], TS, take, 0, false, false),
	  tspl_tuple_field(Tuple, 0, Count),
        Count == 1.

%%     tspl_delete_all(+TS:term, +Query:term, -TupleCount:int) is det.
%
%     TupleCount is the amount of all tuples matching Query in TS. These tuples are
%     removed from the TS.  See also tspl_delete/2.

tspl_delete_all(TS, Query, TupleCount) :-
        query_tuple_(Query, [Tuple|_], TS, takeall, 0, false, true),
	  tspl_tuple_field(Tuple, 0, TupleCount).


%%     tspl_count(+TS:term, +Query:term, -TupleCount:int) is det.
%
%     TupleCount is the amount of all tuples matching Query in TS. See also tspl_delete_all/3.

tspl_count(TS, Query, TupleCount) :-
        query_tuple_(Query, [Tuple|_], TS, readall, 0, false, true),
	  tspl_tuple_field(Tuple, 0, TupleCount).


%%     tspl_wait_to_read(+TS:term, +Query:term, -Tuple:term) is semidet.
%%     tspl_wait_to_read(+TS:term, +Query:term, +TimeOut:int, -Tuple:term) is semidet.
%
%     Succeeds when a Tuple matching Query appears in TS.  The maximum
%     number of seconds to wait is given by TimeOut.

tspl_wait_to_read(TS, Query, Tuple) :-
        query_tuple_(Query, [Tuple|_], TS, waittoread, 0, true, true).

tspl_wait_to_read(TS, Query, Timeout, Tuple) :-
        query_tuple_(Query, [Tuple|_], TS, waittoread, Timeout, true, true).


%%     tspl_wait_to_read_first(+TS:term, +Query:term, -Tuple:term) is semidet.
%%     tspl_wait_to_read_first(+TS:term, +Query:term, +TimeOut:int, -Tuple:term) is semidet.
%
%     Succeeds when a Tuple matching Query appears in TS.  The maximum
%     number of seconds to wait is given by TimeOut. In spaces with many tuples, this will 
%     be faster than the randomized version tspl_wait_to_read/3 or tspl_wait_to_read/4.

tspl_wait_to_read_first(TS, Query, Tuple) :-
        query_tuple_(Query, [Tuple|_], TS, waittoread, 0, true, false).

tspl_wait_to_read_first(TS, Query, Timeout, Tuple) :-
        query_tuple_(Query, [Tuple|_], TS, waittoread, Timeout, true, false).


%%     tspl_wait_to_take(+TS:term, +Query:term, -Tuple:term) is semidet.
%%     tspl_wait_to_take(+TS:term, +Query:term, +TimeOut:int, -Tuple:term) is semidet.
%
%     Succeeds when a Tuple matching Query appears in TS and removes it.  The maximum
%     number of seconds to wait is given by TimeOut.

tspl_wait_to_take(TS, Query, Timeout, Tuple) :-
        query_tuple_(Query, [Tuple|_], TS, waittotake, Timeout, true, true).
tspl_wait_to_take(TS, Query, Tuple) :-
        query_tuple_(Query, [Tuple|_], TS, waittotake, 0, true, true).


%%     tspl_wait_to_take_first(+TS:term, +Query:term, -Tuple:term) is semidet.
%%     tspl_wait_to_take_first(+TS:term, +Query:term, +TimeOut:int, -Tuple:term) is semidet.
%
%     Succeeds when a Tuple matching Query appears in TS and removes it.  The maximum
%     number of seconds to wait is given by TimeOut. In spaces with many tuples, this will 
%     be faster than the randomized version tspl_wait_to_take/3 or tspl_wait_to_take/4.

tspl_wait_to_take_first(TS, Query, Timeout, Tuple) :-
        query_tuple_(Query, [Tuple|_], TS, waittotake, Timeout, true, false).
tspl_wait_to_take_first(TS, Query, Tuple) :-
        query_tuple_(Query, [Tuple|_], TS, waittotake, 0, true, false).


%%     tspl_write(+TS:term, +Tuple:term) is det.
%%     tspl_write(+TS:term, +Tuple:term, -TupleID:atom) is det.
%
%     Writes Tuple to TS.  TupleID is the identifier for the tuple, see tspl_update/3.

tspl_write(TS, Tuple) :-
        tspl_write(TS, Tuple, _).

tspl_write(TS, Tuple, TupleID) :-
        uid(UID),
        tspl_ts_spacename_id(TS, _, SpaceId),
	  xml_to_string_([element(write, [id=UID, space=SpaceId], [Tuple])], XMLString),
        send_receive_xml_(XMLString, UID, XMLResponse, TS),
        XMLResponse = [element(response, AttrList, [RespTuple|_])],
        tspl_tuple_field(RespTuple, 0, TupleID),
        memberchk(id=UID, AttrList),
        memberchk(type=answer, AttrList).


%%     tspl_update(+TS:term, +TupleID:atom, +Tuple:term) is det.
%
%     Updates Tuple in TS.  TupleID is the identifier of the tuple, see tspl_write/3.

tspl_update(TS, TupleIDStr, Tuple) :-
        uid(UID),
        tspl_ts_spacename_id(TS, _, SpaceId),
        TupleID = element(tupleid,[id=TupleIDStr],[]),
        xml_to_string_([element(update, [id=UID, space=SpaceId], [Tuple, TupleID])], XMLString),
        send_receive_xml_(XMLString, UID, XMLResponse, TS),
        XMLResponse = [element(response, AttrList, _)],
        memberchk(id=UID, AttrList),
        memberchk(type=ok, AttrList).


%%     tspl_get_all_spaces(+TS:term, -Spaces:list) is det.
%
%     Spaces is a list of all tuplespaces for TS.  The list consists of
%     elements of the form space(Name, ID).

tspl_get_all_spaces(TS, Spaces) :-
        uid(UID),
        xml_to_string_([element('get-all-spaces', [id=UID], [])], XMLString),
        send_receive_xml_(XMLString, UID, XMLResponse, TS),
        XMLResponse = [element(response, AttrList, SpacesTuples)],
        memberchk(id=UID, AttrList),
        memberchk(type=answer, AttrList),
        findall(space(Sp, Id),
		(member(SpaceTupel, SpacesTuples),
		 tspl_tuple_field(SpaceTupel, 0, Sp),
		 tspl_tuple_field(SpaceTupel, 1, Id)), Spaces).


/*------------------------------------------------------------
 *  Utilities
 *------------------------------------------------------------*/

tspl_formal_field(Class, element(field, [T, fieldtype=formal | BoundXML], []),
		  LowerBound, UpperBound) :-
        string_concat('type=', Class, TStr),
        term_to_atom(T, TStr),
        bound_xml_(LowerBound, UpperBound, BoundXML).

bound_xml_(LowerBound, UpperBound, BoundXML) :-
        var(LowerBound), var(UpperBound),
        BoundXML = [].

bound_xml_(LowerBound, UpperBound, BoundXML) :-
        nonvar(LowerBound), nonvar(UpperBound),
        BoundXML = ['lower-bound'=LowerBound, 'upper-bound'=UpperBound].

bound_xml_(LowerBound, UpperBound, BoundXML) :-
        var(LowerBound), nonvar(UpperBound),
        BoundXML = ['upper-bound'=UpperBound].

bound_xml_(LowerBound, UpperBound, BoundXML) :-
        nonvar(LowerBound), var(UpperBound),
        BoundXML = ['lower-bound'=LowerBound].


%%	tspl_expiring_tuple(+Fields:list, +Exp:int, -Tuple:term) is det.
%
%	This is an alternative constructor predicate for tuples, which
%       additionally sets an expiration timeout. After this timeout, the
%       tuple will be deleted from the space automatically.

tspl_expiring_tuple(Fields, Exp, element(tuple, [expiration=Exp], Fields)).



:- dynamic
     ts_writer_/2,
     ts_reader_/2,
     callback/3,
     mqueue/2,
     tspl_ts_spacename_id/3,
     mutex_name/2.

uid(X) :-
        get_time(T1),
        random(T2),
        T3 is round(T1 * T2),
        T is T3**2,
        format(atom(X), 'p~16r', [T]).

connect_ts_(LoginData, Host, Port, TS, SpaceId) :-
        tcp_socket(TS),
        catch(
                tcp_connect(TS, Host:Port),
                _,
                (
                        print_message(error, 'Could not establish connection. Server not found!'),
                        fail
                )
        ),
        tcp_open_socket(TS, ReadFd, WriteFd),
        asserta(ts_reader_(ReadFd, TS)),
        asserta(ts_writer_(WriteFd, TS)),
        uid(UID),
	uid(Mutex),
	mutex_create(Mutex),
	assert(mutex_name(TS, Mutex)),
        message_queue_create(Q),
        assert(mqueue(Q, TS)),
        thread_create(responsethread(TS), _, []),
        xml_to_string_([element(connect, [id=UID], LoginData)], XMLConnect),
        send_receive_xml_(XMLConnect, UID, XMLResponse, TS),
        XMLResponse = [element(response, AttrList, [Tuple])],
        member(type=answer, AttrList),
        tspl_tuple_field(Tuple, 1, SpaceId),
        writeln('Connection established!').

	
%%	tspl_transaction(+TS:any, +Action:atom) is det.
%
%	Handles transactions on the server side. Action is either *begin*, *commit* or *abort*.

tspl_transaction(TS, Action) :-
        uid(UID),
        xml_to_string_([element(transaction, [id=UID,type=Action], [])], XMLTrans),
        send_receive_xml_(XMLTrans, UID, XMLResponse, TS),
        XMLResponse = [element(response, AttrList, [])],
        member(id=UID, AttrList),
        member(type=ok, AttrList).


%%     tspl_event_register(+TS:any, +Command:atom, +Query:term, +CallBack:atom, -Seq:int) is det.
%
%     Registers a CallBack predicate to be called when Command happens in
%     the TS and matches Query.  Command is one of =write= (new tuple
%     written), =delete= (tuple is deleted) or =update= (tuple has changed). Seq is the 
%     sequence number of the callback that is used for deregistration.
%
%     ==
%     ?- tspl_formal_field(string, F0),
%        tspl_tuple([F0], Query),
%     ?- tspl_event_register(TS, write, Query, my_callback, Seq).
%
%     my_callback(Command, Seq, Before, After) :-
%          ... handle callback here ...
%     ==

tspl_event_register(TS, Command, Template, Callback, Seq) :-
        context_module(Context),
        uid(UID),
        xml_to_string_([element('event-register', [id=UID, command=Command], [element(after, [], [Template])])], XMLTrans),
        send_receive_xml_(XMLTrans, UID, XMLResponse, TS),
        XMLResponse = [element(response, AttrList, [Tuple])],
        memberchk(id=UID, AttrList),
        memberchk(type=answer, AttrList),
        tspl_tuple_field(Tuple, 0, Seq),
        assert(tspl:callback(Seq, TS, Context:Callback)).


%%     tspl_event_deregister(+TS:any, +Seq:int) is det.
%
%     Deregisters a CallBack predicate. Seq is the sequence number of 
%     the callback that was returned after registration.

tspl_event_deregister(TS, Seq) :-
        uid(UID),
        xml_to_string_([element('event-deregister', [id=UID, seq=Seq], [])], XMLTrans),
        send_receive_xml_(XMLTrans, UID, XMLResponse, TS),
        XMLResponse = [element(response, AttrList, [])],
        memberchk(id=UID, AttrList),
        memberchk(type=ok, AttrList),
        retract(tspl:callback(Seq, TS, _)).

field_value_(element(field, Attr, _), _) :-
	memberchk(fieldtype=formal, Attr), !.
field_value_(element(field, Attr, [StrValue]), Value) :-
	memberchk(fieldtype=actual, Attr),
	memberchk(type=Type, Attr),
        adapt_type_(Type, StrValue, Value), !.
field_value_(element(field, Attr, []), '') :-
	memberchk(fieldtype=actual, Attr),
	memberchk(type=string, Attr).

adapt_type_(string, Value, Value) :- !.
adapt_type_(xml, Value, Value) :- !.
adapt_type_(_, StrValue, Value) :-
	atom_number(StrValue, Value).


query_tuple_(Query, Tuple, TS, Mode, TimeOut, Returning, Randomize) :-
        parse_query_(Query, QueryXML),
        uid(UID),
        tspl_ts_spacename_id(TS, _, SpaceId),
        XMLRequest = [element(query, [id=UID,type=Mode,timeout=TimeOut,space=SpaceId,returning=Returning,randomize=Randomize], [QueryXML])],
        xml_to_string_(XMLRequest, XMLString),
        (   TimeOut > 0
	->  thread_create((produce_timed_response(UID, TimeOut, TS)), _, [])
        ;   true
        ),
        send_receive_xml_(XMLString, UID, XMLResponse, TS),
        XMLResponse = [element(response, AttrList, Tuple)],
        memberchk(id=UID, AttrList),
        memberchk(type=answer, AttrList), !.

produce_timed_response(UID, TimeOut, TS) :-
	flush_output,
	Secs is TimeOut / 1000,
	sleep(Secs),
	mqueue(Q, TS),
	thread_send_message(Q, event(UID, [])),
	sleep(5),
	(   thread_peek_message(Q, event(UID, []))
	->  thread_get_message(Q, event(UID, []))
	;   true
	).

/*	Slightly akward because parse_query_/2 can be called with
	a list and a single query.
 */	
parse_query_([], []).
parse_query_([H|T], [Hq|Tq]) :-
        parse_query_(H, Hq),
	parse_query_(T, Tq).
parse_query_(id_query(Id), element(tuple, [tid=Id], [])).
parse_query_(and_query(Query), element(and-query, [], Tuples)) :-
        parse_query_(Query, Tuples).
parse_query_(or_query(Query), element(or-query, [], Tuples)) :-
        parse_query_(Query, Tuples).
parse_query_(index_query(Index, Type, Value),
	      element(index-query, [type=Type], [IndexXML, ValueXML])):-
        IndexXML = element(index, [], [Index]),
        string_to_atom(Value, StrValue),
        ValueXML = element(value, [], [StrValue]).
parse_query_(index_query(Index, Type, LowBd, UpBd),
	      element(index-query, [type=Type], [IndexXML, RangeXML])):-
        IndexXML = element(index, [], [Index]),
        RangeXML = element(range, [], [LowBdXML, UpBdXML]),
        (   var(LowBd)
	->  LowBdXML = element(lower-bound, [], [])
	;   string_to_atom(LowBd, LBV),
	    LowBdXML = element(lower-bound, [], [LBV])
        ),
        (   var(UpBd)
	->  UpBdXML = element(upper-bound, [], [])
	;   string_to_atom(UpBd, UBV),
	    UpBdXML = element(upper-bound, [], [UBV])
        ).
parse_query_(element(tuple, Attr, Fields), element(tuple, Attr, Fields)).


send_receive_xml_(XMLString, UID, XMLResponse, TS) :-
        mqueue(Q, TS),
        ts_writer_(WriteFd, TS),
        mutex_name(TS, Mutex),
        with_mutex(Mutex, (
	      write(WriteFd, XMLString),
	      nl(WriteFd),
	      flush_output(WriteFd))
	    ),
        % TODO: ADD TIMEOUT SO THAT A NON RESPONSIVE SERVER WILL NOT LEAD TO FREEZE, BUT TO A CLEAN ERROR MESSAGE
        thread_get_message(Q, event(UID, XMLResponse)).

xml_to_string_(XML, String) :-
	var(String),
	nonvar(XML), !,
	with_output_to(string(String), xml_write(XML,[header(false),layout(false)])).
xml_to_string_(XML, String) :-
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


callback_handler(Q) :-
	repeat,
	thread_get_message(Q, CbCall),
	(
	  CbCall == [] ;
	  thread_create(call(CbCall), _, [detached(true)]),
	  fail
	),
	message_queue_destroy(Q).

responsethread(TS) :-
        mqueue(Q, TS),
        message_queue_create(CallbackQ, [max_size(1024)]),
        thread_create(callback_handler(CallbackQ), _, [detached(true)]),
        ts_reader_(ReadFd, TS),
        repeat,
        read_line_to_codes(ReadFd, Codes),
	(   Codes == end_of_file, !
	;   string_to_list(Response, Codes),
	    xml_to_string_(XMLResponse, Response),
	    XMLResponse = [El|_],
	    (   El = element(response, _, _), 
		xml_attribute_value_(El, id, UID), 
		thread_send_message(Q, event(UID, XMLResponse))
	    ;   El = element(callback, _, Children),
		xml_attribute_value_(El, seq, SEQAtom),
		xml_attribute_value_(El, command, Cmd),
		term_to_atom(SEQ, SEQAtom),
		memberchk(element(after, _, AfterCommand), Children),
		memberchk(element(before, _, BeforeCommand), Children), 
		callback(SEQ, TS, _:Call),
		CbCall =.. [Call, Cmd, SEQ, BeforeCommand, AfterCommand],
		thread_send_message(CallbackQ, CbCall)
	    ),
	    fail
	),
	retract(mqueue(Q, TS)),
	retract(mutex_name(TS, Mutex)),
        retract(ts_writer_(_, TS)),
	retract(ts_reader_(_, TS)),
        retractall(callback(_, TS, _)),
	message_queue_destroy(Q),
	mutex_destroy(Mutex),
	thread_send_message(CallbackQ, []),
	tcp_close_socket(TS).

xml_attribute_value_(element(_,AttrList,_), Attr, Value) :-
	memberchk(Attr=Value, AttrList).
