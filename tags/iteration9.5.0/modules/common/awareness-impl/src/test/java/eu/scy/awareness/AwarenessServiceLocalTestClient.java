package eu.scy.awareness;

import eu.scy.awareness.event.IAwarePresenceEvent;
import eu.scy.awareness.event.IAwarenessEvent;
import eu.scy.awareness.event.IAwarenessListEvent;
import eu.scy.awareness.event.IAwarenessListListener;
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
            
            awarenessService.addAwarenessListListener(new IAwarenessListListener(){

                @Override
                public void handleAwarenessListEvent(IAwarenessListEvent e) {
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
            
            awarenessService.setStatus("palin", "loser");
            awarenessService.setPresence("obama", IAwarePresenceEvent.ONLINE);
            awarenessService.removeBuddy("mccain");
            
            awarenessService.sendMessage("obama", "howdy!");
            
            
        } catch (AwarenessServiceException e) {
            e.printStackTrace();
        }
    
    }
    
}
