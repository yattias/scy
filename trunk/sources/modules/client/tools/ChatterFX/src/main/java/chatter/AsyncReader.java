package chatter;

import com.sun.javafx.functions.Function0;
import eu.scy.awareness.event.IAwarenessEvent;
import javafx.async.RunnableFuture;

/**
 *
 * @author johan
 */
public class AsyncReader implements RunnableFuture {

  public ChatInput callback;
  IAwarenessEvent event;

  public AsyncReader(ChatInput callback, IAwarenessEvent event) {
    this.callback = callback;
    this.event = event;
  }

  @Override
  public void run() throws Exception {

    try {
      javafx.lang.FX.deferAction(new Function0<Void>() {
          public Void invoke() {
            callback.gotText(event.getUser()+": "+event.getMessage());
            return null;
          }
        });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}