package eu.scy.actionlogging.logger;

import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.ContextConstants;

public class TestRun {
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        ActionLogger al = new ActionLogger();
        IAction action = new Action("test_action", "test_user");
        action.addAttribute("Hallo!!", "FOOBAR!!");
        action.addContext(/*"omg"*/ /*Commendted out by Oyvind because of compile error*/null, "hi2u");
        al.log("test", "tool", action);
        // System.out.println("SENT!!!!");        
    }
    
}
