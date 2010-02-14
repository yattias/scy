/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.dummy;

import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.awareness.IAwarenessService;
import eu.scy.client.common.datasync.IDataSyncService;
import eu.scy.notification.api.INotifiable;
import eu.scy.server.pedagogicalplan.PedagogicalPlanService;
import eu.scy.sessionmanager.SessionManager;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import org.apache.log4j.Logger;
import roolo.api.IExtensionManager;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;

/**
 *
 * @author sikken
 */
public class SimpleToolBrokerAPI implements ToolBrokerAPI
{
   private final static Logger logger = Logger.getLogger(SimpleToolBrokerAPI.class);

   private IRepository repository;
   private IMetadataTypeManager metadataTypeManager;
   private IExtensionManager extensionManager;
   private IELOFactory eloFactory;
   private IActionLogger actionLogger;
   private IAwarenessService awarenessService;
   private IDataSyncService dataSyncService;
   private PedagogicalPlanService pedagogicalPlanService;

   private String userName;
   private String missionId;

   public void setRepository(IRepository repository)
   {
      this.repository = repository;
   }

   @Override
   public IRepository getRepository()
   {
      return repository;
   }

   public void setMetadataTypeManager(IMetadataTypeManager metadataTypeManager)
   {
      this.metadataTypeManager = metadataTypeManager;
   }

   @Override
   public IMetadataTypeManager getMetaDataTypeManager()
   {
      return metadataTypeManager;
   }

   public void setExtensionManager(IExtensionManager extensionManager)
   {
      this.extensionManager = extensionManager;
   }

   @Override
   public IExtensionManager getExtensionManager()
   {
      return extensionManager;
   }

   public void setEloFactory(IELOFactory eloFactory)
   {
      this.eloFactory = eloFactory;
   }

   @Override
   public IELOFactory getELOFactory()
   {
      return eloFactory;
   }

   @Override
   public SessionManager getUserSession(String username, String password)
   {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public void setActionLogger(IActionLogger actionLogger)
   {
      this.actionLogger = actionLogger;
   }

   @Override
   public IActionLogger getActionLogger()
   {
      return actionLogger;
   }

   @Override
   public void registerForNotifications(INotifiable notifiable)
   {
//      throw new UnsupportedOperationException("Not supported yet.");
   }

   public void setAwarenessService(IAwarenessService awarenessService)
   {
      this.awarenessService = awarenessService;
   }

   @Override
   public IAwarenessService getAwarenessService()
   {
      return awarenessService;
   }

   public void setDataSyncService(IDataSyncService dataSyncService)
   {
      this.dataSyncService = dataSyncService;
   }

   @Override
   public IDataSyncService getDataSyncService()
   {
      return dataSyncService;
   }

   public void setPedagogicalPlanService(PedagogicalPlanService pedagogicalPlanService)
   {
      this.pedagogicalPlanService = pedagogicalPlanService;
   }

   @Override
   public PedagogicalPlanService getPedagogicalPlanService()
   {
      return pedagogicalPlanService;
   }

   public String getUserName()
   {
      return userName;
   }

   public void setUserName(String userName)
   {
      this.userName = userName;
   }

   @Override
   public String getMission()
   {
      return missionId;
   }

   public void setMissionId(String missionId)
   {
      this.missionId = missionId;
   }

   @Override
   public String proposeCollaborationWith(String proposedUser, String elouri)
   {
      return proposedUser;
   }

   @Override
   public String answerCollaborationProposal(boolean accept, String proposingUser, String elouri)
   {
      return "false";
   }
}
