package eu.scy.agents.processguidanceservice;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleID;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;

import java.rmi.dgc.VMID;
import java.util.Map;
import java.util.HashMap;

import eu.scy.actionlogging.Action;
import eu.scy.actionlogging.ActionTupleTransformer;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.agents.impl.AgentProtocol;

public class CurrentlyOpenedELOAgent implements Callback {

    private static final String TUPLE_INFO = "CurrenlyOpenELO";

    private TupleSpace commandSpace;

    private TupleSpace actionSpace;

    private HashMap<String, TupleID> tuples;
    

    public CurrentlyOpenedELOAgent() {
        try {
            commandSpace = new TupleSpace(User.getDefaultUser(), "scy.collide.info", 2525, false, false, "command");
            actionSpace = new TupleSpace(User.getDefaultUser(), "scy.collide.info", 2525, false, false, "actions");
            actionSpace.eventRegister(Command.WRITE, new Tuple("action", Field.createWildCardField()), this, false);
            commandSpace.takeAll(new Tuple(TUPLE_INFO,Field.createWildCardField()));
            System.out.println("CurrentlyOpenedELOAgent start work");
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
        tuples = new HashMap<String, TupleID>();
    }

    @Override
    public void call(Command cmd, int seqnum, Tuple afterTuple, Tuple beforeTuple) {
    	//System.out.println("there is a call");
        if (afterTuple == null) {
            return;
        }
        //System.out.println(afterTuple.toString());
        Action a = (Action) ActionTupleTransformer.getActionFromTuple(afterTuple);        
        if (!a.getUser().equalsIgnoreCase("studentm@scy.collide.info/Smack")) return;
        //System.out.println(afterTuple.toString());
        System.out.println(a.toString());
        processAction(a);
    }

    private void processAction(Action a) {
    	
        if (a.getType().equals("tool_opened")) {
            if (tuples.containsKey(a.getUser())) {
                try {
                    commandSpace.update(tuples.get(a.getUser()), new Tuple(TUPLE_INFO,a.getUser(), a.getContext().get(ContextConstants.eloURI)));
                } catch (TupleSpaceException e) {
                    e.printStackTrace();
                }

            } else {
                try {
                    Tuple aTuple = new Tuple(TUPLE_INFO, a.getUser(), a.getContext().get(ContextConstants.eloURI));
                    TupleID tupleID = commandSpace.write(aTuple);
                    tuples.put(a.getUser(), tupleID);
                } catch (TupleSpaceException e) {
                    e.printStackTrace();
                }
            }
        } 
        else if (a.getType().equals("tool_quit")) {
            Tuple tuple = new Tuple();
            tuple.setTupleID(tuples.get(a.getUser()));
            try {
                commandSpace.delete(tuple);
            } catch (TupleSpaceException e) {
                e.printStackTrace();
            }
            //System.out.println(a);
        } 
        else if (a.getType().equals("elo_finished")) {
        	provideActiveGuidance();	
        }
        /*
        else if (a.getType().equals("tool_got_focus")) {
            
            //System.out.println(a);
        } 
        else if (a.getType().equals("text_inserted")) {
        	System.out.println(//"id="+a.getId()+
        			//" user="+a.getUser()+
                	"action="+a.getType()+
                	", text="+a.getAttribute("text")+
        			//" tool="+a.getContext(ContextConstants.tool)+
        			" mission="+a.getContext(ContextConstants.mission)
        			//" session="+a.getContext(ContextConstants.session)+
        			//" ELO_URI="+a.getAttribute(AgentProtocol)
        			);
        	if ((a.getAttribute("text")!=null) && (a.getAttribute("text").length()>0 )) {
        	time_spent = time_spent + 3;
        	contentLength = contentLength + a.getAttribute("text").length();
        	changedAmount= changedAmount + a.getAttribute("text").length();
            System.out.println("time_spent="+time_spent+
        			", contentLength="+contentLength+
        			", changedAmount="+changedAmount);
        	}
        } 
        else if (a.getType().equals("tool_lost_focus")) {
            
            //System.out.println(a);
        } 
        else if (a.getType().equals("text_deleted")) {
        	if ((a.getAttribute("text")!=null) && (a.getAttribute("text").length()>0 )) {
        		
            	System.out.println(//"id="+a.getId()+
            			//" user="+a.getUser()+
            			//" time="+a.getTimeInMillis()+
                    	"action="+a.getType()+
                    	", text="+a.getAttribute("text")+
            			//" tool="+a.getContext(ContextConstants.tool)+
            			" mission="+a.getContext(ContextConstants.mission)
            			//" session="+a.getContext(ContextConstants.session)+
            			//" ELO_URI="+a.getAttribute(AgentProtocol.ACTIONLOG_ELO_URI)
            			//" ELO_TYPE="+a.getAttribute(AgentProtocol.ACTIONLOG_ELO_TYPE)
            			);

        	contentLength = contentLength - a.getAttribute("text").length();
        	changedAmount = changedAmount + a.getAttribute("text").length();
            System.out.println("time_spent="+time_spent+
        			", contentLength="+contentLength+
        			", changedAmount="+changedAmount);
            
            //System.out.println(a);
        	}
        } 
 
        else if (a.getType().equals(AgentProtocol.ACTION_ELO_SAVED)) {
        	System.out.println(//"id="+a.getId()+
			//" user="+a.getUser()+
			//" time="+a.getTimeInMillis()+
        	"action="+a.getType()+
			//" tool="+a.getContext(ContextConstants.tool)+
			" mission="+a.getContext(ContextConstants.mission)+
			//" session="+a.getContext(ContextConstants.session)+
			" ELO_URI="+a.getAttribute(AgentProtocol.ACTIONLOG_ELO_URI)
			//" ELO_TYPE="+a.getAttribute(AgentProtocol.ACTIONLOG_ELO_TYPE)
			);
        	System.out.println("time_spent="+time_spent+
        			", contentLength="+contentLength+
        			", changedAmount="+changedAmount);
        	
        	if (changedAmount < 100) {
        		System.out.println("no big change");
        	} else { 
        		System.out.println("make a big change");
        		if (ELOWorkingStatus == EXPECTED ){
        			ELOWorkingStatus = IN_PROGRESS;
        		} else if (ELOWorkingStatus == IN_PROGRESS ){
        			if ((contentLength/AVERAGE_LENGTH > LENGTH_THRESHOLD) && (time_spent/AVERAGE_TIME_NEEDED > LENGTH_THRESHOLD)) {
        				ELOWorkingStatus = FINISHED;
        				changedAmount = 0;        				
        			}
        		} else if (ELOWorkingStatus == FINISHED ){
        			if (changedAmount/AVERAGE_LENGTH > CHANGE_THRESHOLD) {
        				ELOWorkingStatus = OBSOLETE;
        				changedAmount = 0;        				
        			}
        		} else if (ELOWorkingStatus == OBSOLETE ){
        			if (changedAmount/AVERAGE_LENGTH > CHANGE_THRESHOLD) {
        				ELOWorkingStatus = IN_PROGRESS;
        				changedAmount = 0;        				
        			}
        		}
        	}
        	System.out.println("the state is " + ELOWorkingStatus);
        	
        	this.processELOSavedAction(
					a.getId(),
					a.getUser(),
					a.getTimeInMillis(),
					a.getContext(ContextConstants.tool),
					a.getContext(ContextConstants.mission),
					a.getContext(ContextConstants.session);
					//a.getContext(ContextConstants.eloURI),
					//a.getContext(ContextConstants.eloType),

					// getting the eloUri from the properties, not from the
					// context-constants
					// a.getAttribute(AgentProtocol.ACTIONLOG_ELO_URI),
					// a.getAttribute(AgentProtocol.ACTIONLOG_ELO_TYPE)
					);
        } else {

        }
        //int i = a.getUser().indexOf("@");
        //System.out.println(a.getUser().substring(0, i)+" performs "+a.getType());
        System.out.println(a);
    }
    
    public void processELOSavedAction(String actionId, String user,
			long timeInMillis, String tool, String mission, String session,
			String eloUri, String eloType) {
        if(!EloTypes.SCY_WEBRESOURCER.equals(eloType)) {
            return;
        }
        
    	String NAME = ELOHasBeenSavedAgent.class.getName();
    	String TYPE = "type=elo_show";


		Tuple notificationTuple = new Tuple(AgentProtocol.NOTIFICATION,
				new VMID().toString(), user, tool, NAME, mission, session,
				AgentProtocol.ACTIONLOG_ELO_URI + "=" + eloUri,
				AgentProtocol.ACTIONLOG_ELO_TYPE + "=" + eloType, TYPE);

		try {
			commandSpace.write(notificationTuple);
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
		*/
	}
	public void provideActiveGuidance() {
		
		try {
			
			String completeNotificationId;
			Tuple completeNotificationTuple;
			
			for (int i=10; i<53; i++) {
				completeNotificationId = createId();
				completeNotificationTuple = createAgendaNotification(completeNotificationId,
						"reflect ", "COMPLETED", "13000000000"+String.valueOf(i), String.valueOf(i));		
				ProcessGuidanceAgent.getCommandSpace().write(completeNotificationTuple);
			}

			
			completeNotificationId = createId();
			completeNotificationTuple = createAgendaNotification(completeNotificationId,
					"reflect on importance of criteria", "COMPLETED", "1305120772341", "1");			
			ProcessGuidanceAgent.getCommandSpace().write(completeNotificationTuple);

			completeNotificationId = createId();
			completeNotificationTuple = createAgendaNotification(completeNotificationId,
					"fill the criteria table", "COMPLETED", "1305121072342", "2");			
			ProcessGuidanceAgent.getCommandSpace().write(completeNotificationTuple);

			completeNotificationId = createId();
			completeNotificationTuple = createAgendaNotification(completeNotificationId,
					"fill the criteria weight table", "ACTIVATED", "1305121972393", "3");			
			ProcessGuidanceAgent.getCommandSpace().write(completeNotificationTuple);

			completeNotificationId = createId();
			completeNotificationTuple = createAgendaNotification(completeNotificationId,
					"fill the criteria final table", "NEED2CHECK", "1305122371394", "4");			
			ProcessGuidanceAgent.getCommandSpace().write(completeNotificationTuple);

			completeNotificationId = createId();
			completeNotificationTuple = createAgendaNotification(completeNotificationId,
					"create my optimized healthy pizza", "NEED2CHECK", "1305123572395", "5");			
			ProcessGuidanceAgent.getCommandSpace().write(completeNotificationTuple);

			completeNotificationId = createId();
			completeNotificationTuple = createAgendaNotification(completeNotificationId,
					"write a letter to school canteen", "ENABLED", "", "9");			
			ProcessGuidanceAgent.getCommandSpace().write(completeNotificationTuple);

			completeNotificationId = createId();
			completeNotificationTuple = createAgendaNotification(completeNotificationId,
					"set taste scores", "ENABLED", "", "8");			
			ProcessGuidanceAgent.getCommandSpace().write(completeNotificationTuple);

			completeNotificationId = createId();
			completeNotificationTuple = createAgendaNotification(completeNotificationId,
					"write a group report", "ENABLED", "", "7");			
			ProcessGuidanceAgent.getCommandSpace().write(completeNotificationTuple);

			completeNotificationId = createId();
			completeNotificationTuple = createAgendaNotification(completeNotificationId,
					"write an individual report", "ACTIVATED", "", "6");			
			ProcessGuidanceAgent.getCommandSpace().write(completeNotificationTuple);

			

			String nextActivityNotificationId;
			Tuple nextActivityNotificationTuple;
			
			for (int i=10; i<33; i++) {
				nextActivityNotificationId = createId();
				nextActivityNotificationTuple = createMessageNotification(nextActivityNotificationId,
						 "You completed the activity \"reflect on \".", "13000000000"+String.valueOf(i));		
				ProcessGuidanceAgent.getCommandSpace().write(nextActivityNotificationTuple);
			}
			
			nextActivityNotificationId = createId();
			nextActivityNotificationTuple = createMessageNotification(nextActivityNotificationId,
					 "You completed the activity \"reflect on importance of criteria\".", "1305120772341");		
			ProcessGuidanceAgent.getCommandSpace().write(nextActivityNotificationTuple);
			
			nextActivityNotificationId = createId();
			nextActivityNotificationTuple = createMessageNotification(nextActivityNotificationId,
					 "You completed the activity \"fill the criteria table\".", "1305121072342");		
			ProcessGuidanceAgent.getCommandSpace().write(nextActivityNotificationTuple);
			
			nextActivityNotificationId = createId();
			nextActivityNotificationTuple = createMessageNotification(nextActivityNotificationId,
					 "You completed the activity \"fill the criteria weight table\".", "1305121972393");		
			ProcessGuidanceAgent.getCommandSpace().write(nextActivityNotificationTuple);
			
			nextActivityNotificationId = createId();
			nextActivityNotificationTuple = createMessageNotification(nextActivityNotificationId,
					 "You completed the activity \"fill the criteria final table\".", "1305122371394");		
			ProcessGuidanceAgent.getCommandSpace().write(nextActivityNotificationTuple);
			
			nextActivityNotificationId = createId();
			nextActivityNotificationTuple = createMessageNotification(nextActivityNotificationId,
					 "You completed the activity \"create my optimized healthy pizza\".", "1305123572395");		
			ProcessGuidanceAgent.getCommandSpace().write(nextActivityNotificationTuple);			
			
			nextActivityNotificationId = createId();
			nextActivityNotificationTuple = createMessageNotification(nextActivityNotificationId,
					 "You confirmed that you modified the ELO \"criteria weight table\".", "1305123972396");		
			ProcessGuidanceAgent.getCommandSpace().write(nextActivityNotificationTuple);			
			
			nextActivityNotificationId = createId();
			nextActivityNotificationTuple = createMessageNotification(nextActivityNotificationId,
					 "Because two finished  ELOs \"criteria final table\" and \"my optimized healthy pizza\" are dependent on the \"criteria weight table\". If you change this ELO, please consider whether you will change those two effected ELOs as well.", "1305123972399");
			ProcessGuidanceAgent.getCommandSpace().write(nextActivityNotificationTuple);

		} catch (TupleSpaceException e) {
	    	ProcessGuidanceAgent.logger.info("Error in TupleSpace while load an object in roolo");
		}						
	}	

	private Tuple createAgendaNotification(String notificationId, 
		String message, String status, String time, String elo_uri) {
		Tuple notificationTuple = new Tuple();
		notificationTuple.add(AgentProtocol.NOTIFICATION);  //1
		notificationTuple.add(notificationId);				//2
		notificationTuple.add("studentm@scy.collide.info/Smack");						//3
		notificationTuple.add("scylab");					//4
		notificationTuple.add("process guidance agent");	//5
		notificationTuple.add("mission");					//6
		notificationTuple.add("session");					//7
		notificationTuple.add("type=agenda_notify");		//8
		notificationTuple.add("text="+message);				//9
		notificationTuple.add("timestamp="+time);			//10
		notificationTuple.add("state="+status);				//11
		notificationTuple.add("elouri="+elo_uri);			//12
		System.out.println(notificationTuple.toString());
		return notificationTuple;
	}


	private Tuple createMessageNotification(String notificationId, 
		String message, String time) {
		Tuple notificationTuple = new Tuple();
		notificationTuple.add(AgentProtocol.NOTIFICATION);  //1
		notificationTuple.add(notificationId);				//2
		notificationTuple.add("studentm@scy.collide.info/Smack");						//3
		notificationTuple.add("scylab");					//4
		notificationTuple.add("process guidance agent");	//5
		notificationTuple.add("mission");					//6
		notificationTuple.add("session");					//7
		notificationTuple.add("type=agenda_notify");		//8
		notificationTuple.add("text="+message);				//9
		notificationTuple.add("timestamp="+time);			//10 
		System.out.println(notificationTuple.toString());
		return notificationTuple;
	}

	private String createId() {
		return new VMID().toString();
	}

	public static void main(String[] args) {
        new CurrentlyOpenedELOAgent();
    }

}
