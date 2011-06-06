package eu.scy.actionlogging;

import static org.junit.Assert.assertTrue;

import org.dom4j.Element;
import org.junit.Test;

import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;

/**
 * A Simple Test for the ActionXMLTransformer
 * 
 * @author engler
 * 
 */
public class ActionXMLTransformerTest {

    @Test
    public void testXMLTransformation() {
        // create new Action
        IAction action = new Action();

        action.setUser("beatrix");
        action.setType("Test");
        // add context
        action.addContext(ContextConstants.mission, "Test Mission");
        action.addContext(ContextConstants.tool, "Test Tool");
        action.addContext(ContextConstants.session, "Test Session");
        action.addContext(ContextConstants.eloURI, "Test URI");
        // add attributes
        action.addAttribute("job", "queen");
        action.addAttribute("country", "netherland");
        action.addAttribute("look similar to", "hape");
        // add data
        //action.setDataType("text");
        //action.setData("The monarch is the head of state, at present Queen Beatrix. Constitutionally, the position is equipped with limited powers. The monarch can exert some influence during the formation of a new cabinet, where they serve as neutral arbiter between the political parties. Additionally, the king (the title queen has no constitutional significance) has the right to be informed and consulted. Depending on the personality and qualities of the king and the ministers, the king might have influence beyond the power granted by the constitution.");
        ActionXMLTransformer trans = new ActionXMLTransformer(action);
        Element actionAsElement = trans.getActionAsElement();
        trans = new ActionXMLTransformer(actionAsElement);
        Action action2 = (Action) trans.getActionAsPojo();
        assertTrue(isContentEqual(action, action2));
    }

    //TODO maybe put that into Action?
    public static boolean isContentEqual(IAction a1, IAction a2) {
        
        if (!a1.getUser().equals(a2.getUser())) {
            return false;
        } else if (!a1.getType().equals(a2.getType())) {
            return false;
        /*} else if (!a1.getDataType().equals(a2.getDataType())) {
            return false;
        } else if (!a1.getData().equals(a2.getData())) {
            return false;*/
        } else if (!a1.getAttributes().equals(a2.getAttributes())) {
            return false;
        } else if (!a1.getContext().equals(a2.getContext())) {
            return false;
        }
        return true;
    }
}
