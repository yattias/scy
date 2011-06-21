package eu.scy.agents.processguidanceservice;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.rmi.dgc.VMID;

public class RetrieveELOs {
	
	public static void main(String[] args) {

	    try {
	    	while (true) {
	    		System.out.println("please input an id: "); 
	    		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
	    		String userInput = stdin.readLine ();
	    		System.out.println ("You input is: " + userInput);
	    		String elo_xml = loadELO("roolo://scy.collide.info/scy-collide-server/"+userInput);
	        	if (elo_xml==null) {
	        		System.out.println("the elo has not been found");
	        	}
	        	else {
	        		System.out.println(elo_xml);
	        	}
	    	}
	    } catch (IOException e) {
	    } catch (TupleSpaceException e) {	    	
	    }
	}

	private static String loadELO(String eloUri) throws TupleSpaceException {
		String eloAsXML = null;	    
	    try {
            String id = new VMID().toString();
            ProcessGuidanceAgent.getCommandSpace().write(new Tuple(id, "roolo-agent", "elo", eloUri));
            Tuple responseTuple = ProcessGuidanceAgent.getCommandSpace().waitToTakeFirst(
            		new Tuple(id, "roolo-response", String.class));
            eloAsXML = responseTuple.getField(2).getValue().toString();
	    
	    } catch (TupleSpaceException e) {
	    	ProcessGuidanceAgent.logger.info("Error in TupleSpace while load an object in roolo");
	    }
	    return eloAsXML;
	}

}
