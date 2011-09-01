/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.localtoolbroker;

import java.io.Closeable;
import java.io.IOException;

import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.actionlogging.api.IContextService;
import eu.scy.awareness.IAwarenessService;
import eu.scy.client.common.datasync.IDataSyncService;
import eu.scy.notification.api.INotifiable;
import eu.scy.server.pedagogicalplan.PedagogicalPlanService;
import eu.scy.server.pedagogicalplan.StudentPedagogicalPlanService;
import eu.scy.sessionmanager.SessionManager;
import eu.scy.toolbrokerapi.ConnectionListener;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.toolbrokerapi.ToolBrokerAPIRuntimeSetting;
import java.net.URI;
import org.apache.log4j.Logger;
import roolo.api.IExtensionManager;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;

/**
 *
 * @author sikken
 */
public class LocalToolBrokerAPI implements ToolBrokerAPI,ToolBrokerAPIRuntimeSetting, Closeable
{
   private final static Logger logger = Logger.getLogger(LocalToolBrokerAPI.class);

   private IRepository repository;
   private IMetadataTypeManager metadataTypeManager;
   private IExtensionManager extensionManager;
   private IELOFactory eloFactory;
   private IActionLogger actionLogger;
   private IAwarenessService awarenessService;
   private IDataSyncService dataSyncService;
   private PedagogicalPlanService pedagogicalPlanService;
   private StudentPedagogicalPlanService studentPedagogicalPlanService;
   private IContextService contextService;

   private String userName = "not set";
   private URI missionSpecificationURI;
   private URI missionRuntimeURI;


   @Override
   public void close()
   {
      closeIfPossible(metadataTypeManager,"metadataTypeManager");
      closeIfPossible(extensionManager,"extensionManager");
      closeIfPossible(repository,"repository");
      closeIfPossible(eloFactory,"eloFactory");
      closeIfPossible(actionLogger,"actionLogger");
      closeIfPossible(awarenessService,"awarenessService");
      closeIfPossible(dataSyncService,"dataSyncService");
      closeIfPossible(pedagogicalPlanService,"pedagogicalPlanService");      
      closeIfPossible(studentPedagogicalPlanService,"studentPedagogicalPlanService");      
      closeIfPossible(contextService,"contextService");      
   }
   
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
   
   public void setContextService(IContextService contextService) {
       this.contextService = contextService;
   }
   
   @Override
    public IContextService getContextService() {
        return contextService;
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

   public void setStudentPedagogicalPlanService(StudentPedagogicalPlanService studentPedagogicalPlanService) {
       this.studentPedagogicalPlanService = studentPedagogicalPlanService;
   }

   @Override
   public String getLoginUserName(){
      return userName;
   }

   public void setUserName(String userName)
   {
      this.userName = userName;
   }

   @Override
   public URI getMissionRuntimeURI()
   {
      return missionRuntimeURI;
   }

   @Override
   public void setMissionRuntimeURI(URI missionRuntimeURI)
   {
      logger.info("setMissionRuntimeURI: "+missionRuntimeURI);
      this.missionRuntimeURI = missionRuntimeURI;
   }

   @Override
   public URI getMissionSpecificationURI() {
       return missionSpecificationURI;
   }

   @Override
   public void setMissionSpecificationURI(URI missionSpecificationURI) {
       this.missionSpecificationURI = missionSpecificationURI;
   }

   @Deprecated
   @Override
   public String getMission() {
       return "ToolBrokerAPI.getMission() is deprecated";
   }

   @Deprecated
   @Override
   public void setMissionId(String missionId) {
       // do nothing, deprecated
   }

   @Override
   public void proposeCollaborationWith(String proposedUser, String elouri, String mucid)
   {

   }

   @Override
   public String answerCollaborationProposal(boolean accept, String proposingUser, String elouri)
   {
      return "false";
   }

   private void closeIfPossible(Object object, String label)
   {
      if (object instanceof Closeable){
         try
         {
            ((Closeable)object).close();
         }
         catch (IOException e)
         {
            logger.error("an exception occured during the close of " + label,e);
         }
      }
   }

   @Override
   public void addConnectionListener(ConnectionListener connectionListener)
   {
   }

   @Override
   public void removeConnectionListener(ConnectionListener connectionListener)
   {
   }

    @Override
    public void setUserPresence(boolean available) {
    }

}
