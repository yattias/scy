package eu.scy.actionlogging.logger;

import eu.scy.actionlogging.api.IAction;

public class TestRun {
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        ActionLogger al = new ActionLogger();
        IAction action = new Action();
        action.addProperty("Hallo!!", "FOOBAR!!");
        action.addProperty("omg", "hi2u");
        action.addProperty("foo", "bar");
        al.log("test", "tool", action);
        System.out.println("SENT!!!!");
        
    }
    
}
