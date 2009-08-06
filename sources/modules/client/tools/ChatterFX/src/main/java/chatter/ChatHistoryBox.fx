package chatter;

import javafx.ext.swing.SwingComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;



public class ChatHistoryBox extends SwingComponent, ChatInput {

    var textarea: JTextArea;
    var js: JScrollPane;
    var ready: Boolean;
    public override function createJComponent(){
        textarea = new JTextArea(10,40);
        js = new JScrollPane(textarea);
        ready = true;
        return js;
    }

    public function addText (entry : String) {
        textarea.append(entry);
        textarea.append ("\n");
    }

    override public function gotText (text: String) {
        if (ready) {
            addText(text);
        }
    }


}
