

tspl_read_all(TS, Query, Scan) :-
        query_tuple_(Query, Scan, TS, readall, 0).

tspl_take_all(TS, Query, Scan) :-
        query_tuple_(Query, Scan, TS, takeall, 0).

tspl_read(TS, Query, Tuple) :-
        query_tuple_(Query, [Tuple|_], TS, read, 0).

tspl_take(TS, Query, Tuple) :-
        query_tuple_(Query, [Tuple|_], TS, take, 0).

tspl_wait_to_read(_, Template, _, _) :-
        Template \= element(_,_,_), !,
        writeln('You cannot use waitToRead with timeout *and* extended query!'),
        fail.
tspl_wait_to_read(TS, Query, Timeout, Tuple) :-
        query_tuple_(Query, [Tuple|_], TS, waittoread, Timeout).
tspl_wait_to_read(TS, Query, Tuple) :-
        query_tuple_(Query, [Tuple|_], TS, waittoread, 0).

tspl_wait_to_take(_, Template, _, _) :-
        Template \= element(_,_,_),
        !,
        writeln('You cannot use waitToTake with timeout *and* extended query!'),
        fail.
tspl_wait_to_take(TS, Query, Timeout, Tuple) :-
        query_tuple_(Query, [Tuple|_], TS, waittotake, Timeout).
tspl_wait_to_take(TS, Query, Tuple) :-
        query_tuple_(Query, [Tuple|_], TS, waittotake, 0).

tspl_write(TS, Tuple) :-
        tspl_write(TS, Tuple, _).

tspl_write(TS, Tuple, TupleID) :-
        uid(UID),
        string_concat('id=', UID, UIDStr),
        term_to_atom(UIDParam, UIDStr),
        tspl_ts_spacename_id(TS, _, SpaceId),
        string_concat('space=', SpaceId, SpaceIdStr),
        term_to_atom(SpaceIdParam, SpaceIdStr),
        xml_to_string_([element(write, [UIDParam, SpaceIdParam], [Tuple])], XMLString),
        send_receive_xml_(XMLString, UID, XMLResponse, TS),
        XMLResponse = [element(response, AttrList, [RespTuple|_])],
        tspl_tuple_field(RespTuple, 0, TupleID),
        member(id=UID, AttrList),
        member(type=answer, AttrList).

tspl_update(TS, TupleIDStr, Tuple) :-
        uid(UID),
        string_concat('id=', UID, UIDStr),
        term_to_atom(UIDParam, UIDStr),
        tspl_ts_spacename_id(TS, _, SpaceId),
        string_concat('space=', SpaceId, SpaceIdStr),
        term_to_atom(SpaceIdParam, SpaceIdStr),
        string_concat('id=', TupleIDStr, TupleIDAttr),
        term_to_atom(TupleIDParam, TupleIDAttr),
        TupleID = element(tupleid,[TupleIDParam],[]),
        xml_to_string_([element(update, [UIDParam, SpaceIdParam], [Tuple, TupleID])], XMLString),
        send_receive_xml_(XMLString, UID, XMLResponse, TS),
        XMLResponse = [element(response, AttrList, _)],
        member(id=UID, AttrList),
        member(type=ok, AttrList).

tspl_get_all_spaces(TS, Spaces) :-
        uid(UID),
        xml_to_string_([element('get-all-spaces', [id=UID], [])], XMLString),
        send_receive_xml_(XMLString, UID, XMLResponse, TS),
        XMLResponse = [element(response, AttrList, SpacesTuples)],
        member(id=UID, AttrList),
        member(type=answer, AttrList),
        findall(space(Sp, Id), (member(SpaceTupel, SpacesTuples), tspl_tuple_field(SpaceTupel, 0, Sp), tspl_tuple_field(SpaceTupel, 1, Id)), Spaces).

tspl_formal_field(Class, Field) :-
        string_concat('type=', Class, TStr),
        term_to_atom(T, TStr), 
        Field = element(field, [T, fieldtype=formal], []).

tspl_formal_field(Class, element(field, [T, fieldtype=formal | BoundXML], []), LowerBound, UpperBound) :-
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

tspl_actual_field(Class, Value, Field) :-
        string_concat('type=', Class, TStr),
        term_to_atom(T, TStr),
        string_to_atom(Value, StrValue),
        Field = element(field, [T, fieldtype=actual], [StrValue]).

tspl_wildcard_field(Field) :-
        Field = element(field, [fieldtype=wildcard], []).

tspl_tuple(Fields, element(tuple, [], Fields)).

tspl_expiring_tuple(Fields, Exp, Tuple) :-
        string_concat('expiration=', Exp, ExpStr),
        term_to_atom(ExpParam, ExpStr),
        Tuple = element(tuple, [ExpParam], Fields).

tspl_field_count(element(tuple, _, Fields), Count) :-
		length(Fields, Count).
		
		
tspl_tuple_field(element(tuple, _, Fields), Num, FieldVal) :-
        nth0(Num, Fields, Field),
        field_value_(Field, FieldVal).

tspl_tuple_id(element(tuple, Attr, _), Id) :-
        xml_attribute_value_(element(tuple, Attr, _), id, Id).

tspl_connect_to_ts(Space, TS) :-
        SpaceXML = element(spaces, [], [element(space, [], [Space])]),
        connect_ts_([SpaceXML], localhost, 2525, TS, SpaceId),
        assert(tspl_ts_spacename_id(TS, Space, SpaceId)).

tspl_connect_to_ts(Space, Host, Port, TS) :-
        SpaceXML = element(spaces, [], [element(space, [], [Space])]),
        connect_ts_([SpaceXML], Host, Port, TS, SpaceId),
        assert(tspl_ts_spacename_id(TS, Space, SpaceId)).

tspl_connect_to_ts(Space, Host, Port, User, Password, TS) :-
        SpaceXML = element(spaces, [], [element(space, [], [Space])]),
        UserXML = element(user, [], [User]),
        PasswordXML = element(password, [], [Password]),
        connect_ts_([SpaceXML, UserXML, PasswordXML], Host, Port, TS, SpaceId),
        assert(tspl_ts_spacename_id(TS, Space, SpaceId)).

tspl_disconnect(TS) :-
        tcp_close_socket(TS).


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
        message_queue_create(Q),
        assert(mqueue(Q, TS)),
        thread_create(responsethread(TS), ThreadId, []),
        assert(responsethread(TS, ThreadId)),
        xml_to_string_([element(connect, [id=UID], LoginData)], XMLConnect),
        send_receive_xml_(XMLConnect, UID, XMLResponse, TS),
        XMLResponse = [element(response, AttrList, [Tuple])],
        member(type=answer, AttrList),
        tspl_tuple_field(Tuple, 1, SpaceId),
        writeln('Connection established!').

tspl_transaction(TS, Action) :-
        string_concat('type=', Action, T1),
        term_to_atom(Type, T1),
        uid(UID),
        string_concat('id=', UID, UIDStr),
        term_to_atom(UIDParam, UIDStr),
        xml_to_string_([element(transaction, [UIDParam,Type], [])], XMLTrans),
        send_receive_xml_(XMLTrans, UID, XMLResponse, TS),
        XMLResponse = [element(response, AttrList, [])],
        member(id=UID, AttrList),
        member(type=ok, AttrList).


tspl_event_register(TS, Command, Template, Callback, Seq) :-
        uid(UID),
        xml_to_string_([element('event-register', [id=UID, command=Command], [element(after, [], [Template])])], XMLTrans),
        send_receive_xml_(XMLTrans, UID, XMLResponse, TS),
        XMLResponse = [element(response, AttrList, [Tuple])],
        member(id=UID, AttrList),
        member(type=answer, AttrList),
        tspl_tuple_field(Tuple, 0, Seq),
        assert(callback(Seq, Callback)).

field_value_(element(field, Attr, _), _) :-
        xml_attribute_value_(element(field, Attr, _), fieldtype, formal),
        !.

field_value_(element(field, Attr, [StrValue]), Value) :-
        xml_attribute_value_(element(field, Attr, [StrValue]), fieldtype, actual),
        xml_attribute_value_(element(field, Attr, [StrValue]), type, Type),
        adapt_type_(StrValue, Value, Type),
        !.

field_value_(element(field, Attr, []), '') :-
        xml_attribute_value_(element(field, Attr, []), fieldtype, actual),
        xml_attribute_value_(element(field, Attr, []), type, Type),
        Type = 'string',
        !.

adapt_type_(Value1, Value1, string) :- 
        !.
adapt_type_(Value1, Value1, xml) :- 
        !.
adapt_type_(StrValue, Value, _) :-
        term_to_atom(Value, StrValue),
        number(Value).

query_tuple_(Query, Tuple, TS, Mode, TimeOut) :-
        parse_query_(Query, QueryXML),
        string_concat('type=', Mode, T1Str),
        term_to_atom(TypeParam, T1Str),
        term_to_atom(TimeOut, TimeAtom),
        string_concat('timeout=', TimeAtom, T2Str),
        term_to_atom(TimeParam, T2Str),
        uid(UID),
        string_concat('id=', UID, UIDStr),
        term_to_atom(UIDParam, UIDStr),
        tspl_ts_spacename_id(TS, _, SpaceId),
        string_concat('space=', SpaceId, SpacesStr),
        term_to_atom(SpacesParam, SpacesStr),
        XMLRequest = [element(query, [UIDParam,TypeParam,TimeParam,SpacesParam], [QueryXML])],
        xml_to_string_(XMLRequest, XMLString),
	(
		(
			TimeOut > 0,
			!,
			thread_create((produce_timed_response(UID, TimeOut, TS)), _, [])
		);(
			true
		)
	),
        send_receive_xml_(XMLString, UID, XMLResponse, TS),
        XMLResponse = [element(response, AttrList, Tuple)],
        member(id=UID, AttrList),
        member(type=answer, AttrList),
        !.

produce_timed_response(UID, TimeOut, TS) :-
	flush_output,
	Secs is TimeOut / 1000,
	sleep(Secs),
	mqueue(Q, TS),
	thread_send_message(Q, event(UID, [])),
	sleep(5),
	(
		(thread_peek_message(Q, event(UID, [])), thread_get_message(Q, event(UID, [])))
		; true
	).

parse_query_([], []).
parse_query_(id_query(Id), element(tuple, [tid=Id], [])).
parse_query_(and_query(Query), element(and-query, [], Tuples)) :-
        parse_query_(Query, Tuples).
parse_query_(or_query(Query), element(or-query, [], Tuples)) :-
        parse_query_(Query, Tuples).
parse_query_(index_query(Index, Type, Value), element(index-query, [TypeParam], [IndexXML, ValueXML])):-
        string_concat('type=', Type, TStr),
        term_to_atom(TypeParam, TStr),
        IndexXML = element(index, [], [Index]),
        string_to_atom(Value, StrValue),
        ValueXML = element(value, [], [StrValue]).
parse_query_(index_query(Index, Type, LowBd, UpBd), element(index-query, [TypeParam], [IndexXML, RangeXML])):-
        string_concat('type=', Type, TStr),
        term_to_atom(TypeParam, TStr),
        IndexXML = element(index, [], [Index]),
        RangeXML = element(range, [], [LowBdXML, UpBdXML]),
        (
                (var(LowBd), LowBdXML = element(lower-bound, [], []))
                ;
                (nonvar(LowBd), string_to_atom(LowBd, LBV), LowBdXML = element(lower-bound, [], [LBV]))
        ),
        (
                (var(UpBd), UpBdXML = element(upper-bound, [], []))
                ;
                (nonvar(UpBd), string_to_atom(UpBd, UBV), UpBdXML = element(upper-bound, [], [UBV]))
        ).
parse_query_(element(tuple, Attr, Fields), element(tuple, Attr, Fields)).
parse_query_([H|T], [Hq|Tq]) :-
        parse_query_(H, Hq), parse_query_(T, Tq).

send_receive_xml_(XMLString, UID, XMLResponse, TS) :-
        mqueue(Q, TS),
        ts_writer_(WriteFd, TS),
        write(WriteFd, XMLString),
        nl(WriteFd),
        flush_output(WriteFd), 
% TIMEOUT HINZUFÜGEN UND VON EINEM ANDEREN THREAD AUS NACH X SEKUNDEN EINE FAIL-NACHRICHT IN DIE QUEUE SCHREIBEN
        thread_get_message(Q, event(UID, XMLResponse)).

xml_to_string_(XML, String) :-
        var(String),
        nonvar(XML),
        new_memory_file(MemFile),
        open_memory_file(MemFile, write, WriteMem),
        xml_write(WriteMem, XML, [layout(false)]),
        close(WriteMem),
        open_memory_file(MemFile, read, ReadMem),
        read_line_(_, ReadMem),
        read_line_(_, ReadMem),
        read_line_(String, ReadMem),
        close(ReadMem),
        free_memory_file(MemFile).

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

responsethread(TS) :-
        mqueue(Q, TS),
        ts_reader_(ReadFd, TS),
        repeat,
        read_line_to_codes(ReadFd, Codes),
        string_to_list(Response, Codes),
	(
		(
			Codes = 'end_of_file',
			!,
			writeln('Server closed the socket, exiting!'),
			Finish = true
		);(
			xml_to_string_(XMLResponse, Response),
			XMLResponse = [El|_],
			(
				(
					El = element(response, _, _), 
					xml_attribute_value_(El, id, UID), 
					thread_send_message(Q, event(UID, XMLResponse))
				);(
					El = element(callback, _, Children), 
					xml_attribute_value_(El, seq, SEQAtom),
					xml_attribute_value_(El, command, Cmd),
					term_to_atom(SEQ, SEQAtom),
					member(element(after, _, AfterCommand), Children),
					member(element(before, _, BeforeCommand), Children), 
					callback(SEQ, Call),
					CbCall =.. [Call, Cmd, SEQ, BeforeCommand, AfterCommand],
					thread_create(call(CbCall), _, [])
				)
			),
			Finish = false
		)
	),
        Finish == true.

read_line_(Line, Reader) :-
        read_line_to_codes(Reader, Codes),
        string_to_list(Line, Codes).

xml_attribute_value_(element(_, AttrList, _), Attr, Value) :-
        search_attr_(AttrList, Attr, ValueStr),
        term_to_atom(Value, ValueStr).

search_attr_([], _, _) :-
        fail.
search_attr_([Head|_], Attr, Val) :-
        string_concat(Attr, '=', S),
        term_to_atom(Head, StrHead),
        sub_string(StrHead, 0, L, _, S),
        sub_string(StrHead, L, _, 0, StrVal),
        string_to_atom(StrVal, Val), !.
search_attr_([_|Rest], Attr, Val) :-
        search_attr_(Rest, Attr, Val).
