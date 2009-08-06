package chatter;

import eu.scy.awareness.IAwarenessUser;
import eu.scy.chat.controller.ChatController;
import javafx.async.RunnableFuture;

/**
 *
 * @author johan
 */
public class AsyncWriter implements RunnableFuture {

  String txt;
  ChatController ccontroller;
  String recipient;

  public AsyncWriter(String txt, ChatController ccontroller, String recipient) {
    this.txt = txt;
    this.ccontroller = ccontroller;
    this.recipient = recipient;
  }

  @Override
  public void run() throws Exception {
    System.out.println ("[JVDBG] executing async message sending");
    IAwarenessUser au;
    for(int i = 0; i < ccontroller.getBuddyListArray().size(); i++) {
        au = (IAwarenessUser) ccontroller.getBuddyListArray().elementAt(i);
        if(au.getUsername().equals(recipient)) {
            ccontroller.sendMessage(au, txt);
        }
    }
    System.out.println ("[JVDBG] done executing async message sending");

  }
}
