package eu.scy.agents.search.searchresultenricher;

import java.util.Locale;

//import eu.scy.agents.impl.AgentProtocol;
//import info.collide.sqlspaces.client.TupleSpace;
//import info.collide.sqlspaces.commons.Field;
//import info.collide.sqlspaces.commons.Tuple;
//import info.collide.sqlspaces.commons.TupleSpaceException;
//import info.collide.sqlspaces.commons.User;

public class SimpleSearch {

    /*
	 * Just for testing purposes
	 */
	public static void main(String[] args) {
		
		Locale locale = new Locale("en");
		for(Locale loc : Locale.getAvailableLocales()) {
			System.out.println(loc.getCountry() + " : " + loc.getLanguage() + " : " + loc.getVariant());
//			if(locale.getLanguage().equals(loc.getLanguage())) {
//				System.out.println(loc.getLanguage());
//			}
		}
		
//		TupleSpace sessionSpace; 
//		try {
			
//			actionSpace = new TupleSpace(new User("test"), "localhost", 2525, false, false, AgentProtocol.ACTION_SPACE_NAME);
//			sessionSpace = new TupleSpace(new User("test"), "scy.collide.info", 2525, false, false, AgentProtocol.SESSION_SPACE_NAME);

		    // ("action":String, <ID>:String, <Timestamp>:long, <Type>:String, <User>:String, <Tool>:String,
			// <Mission>:String, <Session>:String, <ELOUri>:String, 
			// <Key=Value>:String*)
//			Tuple t1 = new Tuple(
//					"action", new VMID().toString(), System.currentTimeMillis(), "search-query", "krueger", "scy-simple-search", 
//					"n/a", "n/a",	"n/a", 
//					"query=experiment", "search_results=");
//			actionSpace.write(t1);
			
//			Tuple t = new Tuple(String.class, String.class, Integer.class, String.class);
//			Tuple t = new Tuple("language", String.class, String.class);
//			Tuple[] response = sessionSpace.readAll(t);
//			for (Tuple tu : response) {
//				System.out.println(tu);
//			}
//			for(Field field : response.getFields()){
//				System.out.println(field.getValue().toString());
//			}
			
			
//			for(Tuple st : sessionSpace.readAll(t, 50)) {
//				System.out.println(st);
////				for(Field f : st.getFields()) {
////					System.out.println(f.getValue().toString());
////				}
////				System.out.println(st.getFields().toString());
////				System.out.println(st.toString());
//			}
			

//			sessionSpace.disconnect();
//		} catch (TupleSpaceException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}
}
