package chatter;

import javafx.async.JavaTaskBase;

import javafx.async.RunnableFuture;
import eu.scy.chat.controller.ChatController;


/**
 * ChatWriter
 */
public class ChatWriter extends JavaTaskBase {

var peer: AsyncWriter;
public-init var txt: String;
public-init var ccontroller: ChatController;
public-init var recpt: String;

  

    override protected function create() : RunnableFuture {
        println ("[ChatWriter] start called");
        peer = new AsyncWriter (txt, ccontroller, recpt);
        peer;
    }



}
