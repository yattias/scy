package eu.scy.agents.search.searchresultenricher;

import eu.scy.agents.impl.AgentProtocol;
import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;

public class SimpleSearch {

    /*
	 * Just for testing purposes
	 */
	public static void main(String[] args) {
		
//		TupleSpace sessionSpace; 
		TupleSpace actionSpace;
//		TupleSpace commandSpace;

		try {
//			sessionSpace = new TupleSpace(new User("test"), "scy.collide.info", 2525, false, false, AgentProtocol.SESSION_SPACE_NAME);
			actionSpace = new TupleSpace(new User("test"), "scy.collide.info", 2525, false, false, AgentProtocol.ACTION_SPACE_NAME);
//			commandSpace = new TupleSpace(new User("test"), "scy.collide.info", 2525, false, false, AgentProtocol.COMMAND_SPACE_NAME);
			
			// perform a simple search
		    // ("action":String, <ID>:String, <Timestamp>:long, <Type>:String, <User>:String, <Tool>:String,
			// <Mission>:String, <Session>:String, <ELOUri>:String, 
			// <Key=Value>:String*)
//			Tuple t1 = new Tuple(
//					"action", new VMID().toString(), System.currentTimeMillis(), "search-query", "krueger", "scy-simple-search", 
//					"n/a", "n/a",	"n/a", 
//					"query=contents:\"experimant\"", "search_results=" + INSERT XML DATA HERE);
//			actionSpace.write(t1);

			// get a simple serach response
//			String uniqueID = new VMID().toString();
//			Tuple requestTuple = new Tuple(uniqueID, "roolo-agent", "search", "contents:\"experiment\"");
//			commandSpace.write(requestTuple);
//
//			Tuple responseTuple = commandSpace.waitToTake(new Tuple(uniqueID, "roolo-response", 
//					Field.createWildCardField()), 3000);
//			if (responseTuple != null) {
//				String xmlResults = responseTuple.getField(2).getValue().toString();
//				System.out.println(xmlResults);
//			}
			
//			sessionSpace.disconnect();
			actionSpace.disconnect();
//			commandSpace.disconnect();
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
	}
	
}
