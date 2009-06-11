package eu.scy.actionlogging.logger;

import eu.scy.actionlogging.api.IAction;

public class TestRun {
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        ActionLogger al = new ActionLogger();
        IAction action = new Action("test_action", "test_user");
        action.addAttribute("Hallo!!", "FOOBAR!!");
        action.addContext("omg", "hi2u");
        al.log("test", "tool", action);
        System.out.println("SENT!!!!");        
    }
    
}
