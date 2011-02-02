/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.toolbrokerapi;

/**
 *
 * @author sikken
 */
public class ServerNotRespondingException extends RuntimeException {

   private String server;
   private int port;

   public ServerNotRespondingException(String server, int port){
      super();
      this.server = server;
      this.port = port;
   }

   public String getServer()
   {
      return server;
   }

   public int getPort()
   {
      return port;
   }

}
