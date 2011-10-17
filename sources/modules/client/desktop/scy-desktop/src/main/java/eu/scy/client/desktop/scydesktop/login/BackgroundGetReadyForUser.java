/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.login;

import eu.scy.client.desktop.scydesktop.hacks.RepositoryTimer;
import eu.scy.client.desktop.scydesktop.hacks.RepositoryWrapper;
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
   private final RepositoryTimer repositoryTimer;

   public BackgroundGetReadyForUser(ToolBrokerLogin toolBrokerLogin, Object loginResult, TbiReady tbiReady, RepositoryTimer repositoryTimer)
   {
      this.toolBrokerLogin = toolBrokerLogin;
      this.loginResult = loginResult;
      this.tbiReady = tbiReady;
      this.repositoryTimer = repositoryTimer;
   }

   public void start()
   {
      Thread thread = new Thread(this);
      thread.setName(BackgroundGetReadyForUser.class.getName());
      thread.start();
   }

   @Override
   public void run()
   {
      try
      {
//         throw new IllegalStateException("testing");
         final ToolBrokerAPI tbi = toolBrokerLogin.getReadyForUser(loginResult);
         if (repositoryTimer != null)
         {
            setUpRepositoryTiming(tbi);
         }
         final Missions missions = MissionLocatorUtils.findMissions(tbi, tbi.getLoginUserName());
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

   private void setUpRepositoryTiming(ToolBrokerAPI tbi)
   {
      if (tbi.getRepository() instanceof RepositoryWrapper)
      {
         RepositoryWrapper repositoryWrapper = (RepositoryWrapper) tbi.getRepository();
         repositoryWrapper.setRepositoryTimer(repositoryTimer);
      }
   }
}
