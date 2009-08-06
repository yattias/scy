/*
 * Reader.fx
 *
 * Created on 17-feb-2009, 19:55:34
 */

package chatter;

import javafx.async.JavaTaskBase;

import javafx.async.RunnableFuture;
import eu.scy.awareness.event.IAwarenessEvent;

public class ChatReader extends JavaTaskBase {
    public-init var chatHistoryBox: ChatHistoryBox;
    public-init var messageEvent: IAwarenessEvent;
    var peer: AsyncReader;
    
    override protected function create() : RunnableFuture {
        println ("[JVDBG] start called");
        var chatBox: ChatInput = chatHistoryBox as ChatInput;
        peer = new AsyncReader ( chatBox, messageEvent );
        return peer;
    }

}
