package eu.scy.agents.search.searchresultenricher;

import java.rmi.dgc.VMID;

import eu.scy.agents.impl.AgentProtocol;
import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;

public class SimpleSearch {

	public static void main(String[] args) {
		
		TupleSpace actionSpace; 
		try {
			
//			actionSpace = new TupleSpace(new User("test"), "localhost", 2525, false, false, AgentProtocol.ACTION_SPACE_NAME);
			actionSpace = new TupleSpace(new User("test"), "scy.collide.info", 2525, false, false, AgentProtocol.ACTION_SPACE_NAME);

		    // ("action":String, <ID>:String, <Timestamp>:long, <Type>:String, <User>:String, <Tool>:String,
			// <Mission>:String, <Session>:String, <ELOUri>:String, 
			// <Key=Value>:String*)
			Tuple t1 = new Tuple(
					"action", new VMID().toString(), System.currentTimeMillis(), "search-query", "krueger", "scy-simple-search", 
					"n/a", "n/a",	"n/a", 
					"query=experiment", "search_results=");
			actionSpace.write(t1);

			actionSpace.disconnect();
		} catch (TupleSpaceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
