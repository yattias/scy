/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.login;

import eu.scy.client.desktop.scydesktop.mission.MissionLocatorUtils;
import eu.scy.client.desktop.scydesktop.mission.Missions;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.toolbrokerapi.ToolBrokerLogin;
import javax.swing.SwingUtilities;

/**
 *
 * @author SikkenJ
 */
public class BackgroundGetReadyForUser implements Runnable
{

   private final ToolBrokerLogin toolBrokerLogin;
   private final Object loginResult;
   private final TbiReady tbiReady;

   public BackgroundGetReadyForUser(ToolBrokerLogin toolBrokerLogin, Object loginResult, TbiReady tbiReady)
   {
      this.toolBrokerLogin = toolBrokerLogin;
      this.loginResult = loginResult;
      this.tbiReady = tbiReady;
   }

   public void start()
   {
      new Thread(this).start();
   }

   @Override
   public void run()
   {
      try
      {
//         throw new IllegalStateException("testing");
         final ToolBrokerAPI tbi = toolBrokerLogin.getReadyForUser(loginResult);
         final Missions missions = MissionLocatorUtils.findMissions(tbi);
         SwingUtilities.invokeLater(new Runnable()
         {

            @Override
            public void run()
            {
               tbiReady.tbiReady(tbi, missions);
            }
         });
      }
      catch (Exception e)
      {
         tbiReady.tbiFailed(e);
      }
   }
}
