package eu.scy.agents.search.searchresultenricher;

import eu.scy.agents.impl.AgentProtocol;
import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;

public class SimpleSearch {

    /*
	 * Just for testing purposes
	 */
	public static void main(String[] args) {
		
		TupleSpace sessionSpace; 
//		TupleSpace actionSpace;

		try {
//			actionSpace = new TupleSpace(new User("test"), "localhost", 2525, false, false, AgentProtocol.ACTION_SPACE_NAME);
			
		    // ("action":String, <ID>:String, <Timestamp>:long, <Type>:String, <User>:String, <Tool>:String,
			// <Mission>:String, <Session>:String, <ELOUri>:String, 
			// <Key=Value>:String*)
//			Tuple t1 = new Tuple(
//					"action", new VMID().toString(), System.currentTimeMillis(), "search-query", "krueger", "scy-simple-search", 
//					"n/a", "n/a",	"n/a", 
//					"query=experiment", "search_results=");
//			actionSpace.write(t1);

			
			sessionSpace = new TupleSpace(new User("test"), "scy.collide.info", 2525, false, false, AgentProtocol.SESSION_SPACE_NAME);
			Tuple t = new Tuple("language", String.class, String.class);
			for (Tuple tu : sessionSpace.readAll(t)) {
				System.out.println(tu);
			}
			
			for(Tuple st : sessionSpace.readAll(t, 50)) {
				System.out.println(st);
				for(Field f : st.getFields()) {
					System.out.println(f.getValue().toString());
				}
				System.out.println(st.getFields().toString());
				System.out.println(st.toString());
			}

			sessionSpace.disconnect();
//			actionSpace.disconnect();
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
	}
}
