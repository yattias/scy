package eu.scy.awareness;

import eu.scy.awareness.event.IAwarePresenceEvent;
import eu.scy.awareness.event.IAwarenessEvent;
import eu.scy.awareness.event.IAwarenessRosterEvent;
import eu.scy.awareness.event.IAwarenessRosterListener;
import eu.scy.awareness.event.IAwarenessMessageListener;
import eu.scy.awareness.event.IAwarenessPresenceListener;


public class AwarenessServiceLocalTestClient {
    
    /**
     * @param args
     */
    public static void main(String[] args) {
   
        try {
            IAwarenessService awarenessService = AwarenessServiceFactory.getAwarenessService(AwarenessServiceFactory.MOCK_STYLE);
            
            
            awarenessService.addAwarenessMessageListener(new IAwarenessMessageListener(){

                @Override
                public void handleAwarenessMessageEvent(IAwarenessEvent e) {
                    System.out.println(".handleAwarenessMessageEvent()");
                    System.out.println( "user: " +e.getUser() + " " + e.getMessage());
                }});
            
            awarenessService.addAwarenessRosterListener(new IAwarenessRosterListener(){

                @Override
                public void handleAwarenessRosterEvent(IAwarenessRosterEvent e) {
                    System.out.println(".handleAwarenessListEvent()");
                    System.out.println( "user: " +e.getUser() + " " + e.getMessage());
                    
                }});
            awarenessService.addAwarenessPresenceListener(new IAwarenessPresenceListener(){
            
                @Override
                public void handleAwarenessPresenceEvent(IAwarePresenceEvent e) {
                    System.out.println(".handleAwarenessPresenceEvent()");
                    System.out.println( "user: " +e.getUser() + " " + e.getMessage() + "presence: " + e.getPresence() + " status: " + e.getStatus());
                }
            });

            awarenessService.addBuddy("obama");
            awarenessService.addBuddy("biden");
            awarenessService.addBuddy("palin");
            awarenessService.addBuddy("mccain");
            
            awarenessService.setStatus("loser");
            awarenessService.setPresence(IAwarePresenceEvent.ONLINE);
            awarenessService.removeBuddy("mccain");
            
            
            
            
        } catch (AwarenessServiceException e) {
            e.printStackTrace();
        }
    
    }
    
}
