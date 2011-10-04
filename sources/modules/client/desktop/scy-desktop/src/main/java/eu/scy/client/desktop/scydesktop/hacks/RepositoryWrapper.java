/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.hacks;

import eu.scy.common.scyelo.ScyRooloMetadataKeyIds;
import java.awt.EventQueue;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;
import roolo.api.IRepository;
import roolo.search.IQuery;
import roolo.search.ISearchResult;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.elo.api.metadata.IMetadataKeyIdDefinition;
import roolo.elo.metadata.keys.Contribute;

/**
 *
 * @author sikkenj
 */
public class RepositoryWrapper implements IRepository
{

   private final static Logger logger = Logger.getLogger(RepositoryWrapper.class);
//	private final static String meloType = "scy/melo";
   private URI missionRuntimeEloUri;
   private URI missionSpecificationEloUri;
   private String missionId;
   private String lasId;
   private String userId;
   private URI anchorEloUri;
   private IRepository repository;
   private IMetadataTypeManager metadataTypeManager;
   private boolean sendEloSavedEvents = false;
   private IMetadataKey uriKey;
   private IMetadataKey authorKey;
   private IMetadataKey dateCreatedKey;
   private IMetadataKey dateLastModifiedKey;
   private IMetadataKey lasKey;
   private IMetadataKey activeAnchorEloKey;
   private IMetadataKey missionRuntimeKey;
   private IMetadataKey missionRunningKey;
   private IMetadataKey templateKey;
   private IMetadataKey missionIdKey;
//	private IMetadataKey typeKey;
//	private IMetadataKey annotatesRelationKey;
   private List<EloSavedListener> eloSavedListeners = new CopyOnWriteArrayList<EloSavedListener>();

   // to enable the EDT check start SCY-Lab with -UEDTCheck=true
   private boolean edtCheck = Boolean.getBoolean("EDTCheck");

   @Override
   public String toString()
   {
      return "RepositoryWrapper{" + "missionRuntimeEloUri=" + missionRuntimeEloUri + ", missionId=" + missionId + ", lasId=" + lasId + ", userId=" + userId + ", anchorEloUri=" + anchorEloUri + "\nrepository=" + repository + '}';
   }

   public void addEloSavedListener(EloSavedListener eloSavedListener)
   {
      if (!eloSavedListeners.contains(eloSavedListener))
      {
         eloSavedListeners.add(eloSavedListener);
      }
   }

   public void removeEloSavedListener(EloSavedListener eloSavedListener)
   {
      if (eloSavedListeners.contains(eloSavedListener))
      {
         eloSavedListeners.remove(eloSavedListener);
      }
   }

   private void sendNewEloSavedEvent(URI eloURI)
   {
      if (sendEloSavedEvents)
      {
         for (EloSavedListener eloSavedListener : eloSavedListeners)
         {
            eloSavedListener.newEloSaved(eloURI);
         }
      }
   }

   private void sendForkedEloSavedEvent(URI eloURI)
   {
      if (sendEloSavedEvents)
      {
         for (EloSavedListener eloSavedListener : eloSavedListeners)
         {
            eloSavedListener.forkedEloSaved(eloURI);
         }
      }
   }

   private void sendEloUpdatedEvent(URI eloURI)
   {
      if (sendEloSavedEvents)
      {
         for (EloSavedListener eloSavedListener : eloSavedListeners)
         {
            eloSavedListener.eloUpdated(eloURI);
         }
      }
   }

   private void sendMetadataChangedEvent(URI eloURI, IMetadata metadata)
   {
      if (sendEloSavedEvents)
      {
         for (EloSavedListener eloSavedListener : eloSavedListeners)
         {
            eloSavedListener.metadataChanged(eloURI, metadata);
         }
      }
   }

    //XXX maybe we shouldnt implement this method here, because it is just used in the exporter
    public List<Map<Integer, URI>> getAllVersionLists() {
        throw new UnsupportedOperationException("Not supported yet. Just for the roolo-jpa Exporter!");
    }

   public void setAnchorEloUri(URI anchorEloUri)
   {
      this.anchorEloUri = anchorEloUri;
   }

   public void setRepository(IRepository repository)
   {
      this.repository = repository;
   }

   public void setUserId(String userId)
   {
      this.userId = userId;
   }

   public void setLasId(String lasId)
   {
      this.lasId = lasId;
   }

   public void setMissionId(String missionId)
   {
      this.missionId = missionId;
   }

   public void setMissionRuntimeEloUri(URI missionSpcificationEloUri)
   {
      this.missionRuntimeEloUri = missionSpcificationEloUri;
   }

   public void setMissionSpecificationEloUri(URI missionSpecificationEloUri)
   {
      this.missionSpecificationEloUri = missionSpecificationEloUri;
   }

   public void setMetadataTypeManager(IMetadataTypeManager metadataTypeManager)
   {
      this.metadataTypeManager = metadataTypeManager;
      uriKey = getMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER);
      authorKey = getMetadataKey(CoreRooloMetadataKeyIds.AUTHOR);
      dateCreatedKey = getMetadataKey(CoreRooloMetadataKeyIds.DATE_CREATED);
      dateLastModifiedKey = getMetadataKey(CoreRooloMetadataKeyIds.DATE_LAST_MODIFIED);
      lasKey = getMetadataKey(ScyRooloMetadataKeyIds.LAS);
      activeAnchorEloKey = getMetadataKey(ScyRooloMetadataKeyIds.ACTIVE_ANCHOR_ELO);
      missionRuntimeKey = getMetadataKey(ScyRooloMetadataKeyIds.MISSION_RUNTIME);
      missionRunningKey = getMetadataKey(ScyRooloMetadataKeyIds.MISSION_RUNNING);
      templateKey = getMetadataKey(CoreRooloMetadataKeyIds.TEMPLATE);
      missionIdKey = getMetadataKey(CoreRooloMetadataKeyIds.MISSION_ID);
   }

   private IMetadataKey getMetadataKey(IMetadataKeyIdDefinition keyId)
   {
      IMetadataKey key = metadataTypeManager.getMetadataKey(keyId.getId());
      if (key == null)
      {
         logger.error("Couldn't get metadata key named: " + keyId);
      }
      return key;
   }

   public void setSendEloSavedEvents(boolean sendEloSavedEvents)
   {
      this.sendEloSavedEvents = sendEloSavedEvents;
   }

   private void addGeneralMetadata(IELO elo)
   {
      if (elo == null)
      {
         return;
      }
      if (overwriteAuthor(elo))
      {
         elo.getMetadata().getMetadataValueContainer(authorKey).setValue(new Contribute(userId, System.currentTimeMillis()));
      }
      if (!elo.getMetadata().metadataKeyExists(dateCreatedKey))
      {
         // no date created key, add it
         elo.getMetadata().getMetadataValueContainer(dateCreatedKey).setValue(System.currentTimeMillis());
      }
      elo.getMetadata().getMetadataValueContainer(dateLastModifiedKey).setValue(System.currentTimeMillis());
      if (missionId != null)
      {
         elo.getMetadata().getMetadataValueContainer(missionIdKey).setValue(missionId);
      }
      if (missionRuntimeEloUri != null)
      {
         elo.getMetadata().getMetadataValueContainer(missionRuntimeKey).setValue(missionRuntimeEloUri);
      }
      if (missionSpecificationEloUri != null)
      {
         elo.getMetadata().getMetadataValueContainer(missionRunningKey).setValue(missionSpecificationEloUri);
      }
      if (lasId != null)
      {
         elo.getMetadata().getMetadataValueContainer(lasKey).setValue(lasId);
      }
      clearTemplateIfNeeded(elo);
      // TODO store active anchor relation
//		else
//		{
//			elo.getMetadata().getMetadataValueContainer(authorKey).setValue(new Contribute(User.instance.getUsername(), System.currentTimeMillis()));
//		}
//		boolean addAnnotesRelation = true;
//		Object eloType = elo.getMetadata().getMetadataValueContainer(authorKey).getValue();
//		addAnnotesRelation = meloType.equals(eloType);
//		if (addAnnotesRelation && anchorEloUri != null && annotatesRelationKey != null)
//		{
//			elo.getMetadata().getMetadataValueContainer(annotatesRelationKey).setValue(anchorEloUri);
//		}
   }

   private boolean overwriteAuthor(IELO elo)
   {
      if (!StringUtils.hasLength(userId))
      {
         return false;
      }
      if (authorKey == null)
      {
         return false;
      }
      List<Contribute> authors = (List<Contribute>) elo.getMetadata().getMetadataValueContainer(authorKey).getValueList();
      if (authors != null)
      {
         for (Contribute author : authors)
         {
            if (userId.equalsIgnoreCase(author.getVCard()))
            {
               return false;
            }
         }
      }
      return true;
   }

   @Override
   public IMetadata addNewELO(IELO elo)
   {
      throwExceptionIfOnEDT();
      addGeneralMetadata(elo);
      IMetadata metadata = repository.addNewELO(elo);
      if (uriKey != null)
      {
         sendNewEloSavedEvent((URI) metadata.getMetadataValueContainer(uriKey).getValue());
      }
      return metadata;
   }

   @Override
   public IMetadata addForkedELO(IELO elo)
   {
      throwExceptionIfOnEDT();
      addGeneralMetadata(elo);
      IMetadata metadata = repository.addForkedELO(elo);
      if (uriKey != null)
      {
         sendForkedEloSavedEvent((URI) metadata.getMetadataValueContainer(uriKey).getValue());
      }
      return metadata;
   }

   @Override
   public IMetadata addForkedELO(IELO elo, URI parentUri)
   {
      throwExceptionIfOnEDT();
      addGeneralMetadata(elo);
      IMetadata metadata = repository.addForkedELO(elo, parentUri);
      if (uriKey != null)
      {
         sendForkedEloSavedEvent((URI) metadata.getMetadataValueContainer(uriKey).getValue());
      }
      return metadata;
   }

   @Override
   public IELO retrieveELO(URI arg0)
   {
      throwExceptionIfOnEDT();
      return repository.retrieveELO(arg0);
   }

   @Override
   public void deleteELO(URI arg0)
   {
      throwExceptionIfOnEDT();
      repository.deleteELO(arg0);
   }

   @Override
   public IMetadata updateELO(IELO elo)
   {
      throwExceptionIfOnEDT();
      addGeneralMetadata(elo);
      IMetadata metadata = repository.updateELO(elo);
      if (uriKey != null)
      {
         sendEloUpdatedEvent((URI) metadata.getMetadataValueContainer(uriKey).getValue());
      }
      return metadata;
   }

   @Override
   public IMetadata updateELO(IELO elo, URI parentUri)
   {
      throwExceptionIfOnEDT();
      addGeneralMetadata(elo);
      IMetadata metadata = repository.updateELO(elo, parentUri);
      if (uriKey != null)
      {
         sendEloUpdatedEvent((URI) metadata.getMetadataValueContainer(uriKey).getValue());
      }
      return metadata;
   }

   @Override
   public void archiveELO(URI arg0)
   {
      throwExceptionIfOnEDT();
      repository.archiveELO(arg0);
   }

   @Override
   public void unarchiveELO(URI arg0)
   {
      throwExceptionIfOnEDT();
      repository.unarchiveELO(arg0);
   }

   @Override
   public List<ISearchResult> search(IQuery arg0)
   {
       throwExceptionIfOnEDT();
       return repository.search(arg0);
   }

   @Override
   public IMetadata retrieveMetadata(URI arg0)
   {
      throwExceptionIfOnEDT();
      return repository.retrieveMetadata(arg0);
   }

   @Override
   public void addMetadata(URI arg0, IMetadata arg1)
   {
      throwExceptionIfOnEDT();
      repository.addMetadata(arg0, arg1);
      if (uriKey != null)
      {
         sendMetadataChangedEvent(arg0, arg1);
      }
   }

   @Override
   public IELO retrieveELOLastVersion(URI arg0)
   {
      throwExceptionIfOnEDT();
      return repository.retrieveELOLastVersion(arg0);
   }

   @Override
   public IELO retrieveELOFirstVersion(URI arg0)
   {
      throwExceptionIfOnEDT();
      return repository.retrieveELOFirstVersion(arg0);
   }

   @Override
   public IMetadata retrieveMetadataLastVersion(URI arg0)
   {
      throwExceptionIfOnEDT();
      return repository.retrieveMetadataLastVersion(arg0);
   }

   @Override
   public IMetadata retrieveMetadataFirstVersion(URI arg0)
   {
      throwExceptionIfOnEDT();
      return repository.retrieveMetadataFirstVersion(arg0);
   }

   @Override
   public List<IELO> retrieveELOAllVersions(URI uri)
   {
      throwExceptionIfOnEDT();
      return repository.retrieveELOAllVersions(uri);
   }

   @Override
   public List<IELO> retrieveELOs(List<URI> uris)
   {
      throwExceptionIfOnEDT();
      return repository.retrieveELOs(uris);
   }

   @Override
   public List<IMetadata> retrieveMetadataAllVersions(URI uri)
   {
      throwExceptionIfOnEDT();
      return repository.retrieveMetadataAllVersions(uri);
   }

   @Override
   public List<IMetadata> retrieveMetadatas(List<URI> uris)
   {
      throwExceptionIfOnEDT();
      return repository.retrieveMetadatas(uris);
   }

   @Override
   public List<IELO> retrieveAllELOs()
   {
       //shouldnt be used from within client
      throw new UnsupportedOperationException("Not supported yet.");
   }

    @Override
    public List<ISearchResult> listAllElos() {
        //shouldnt be used from within client
        throw new UnsupportedOperationException("Not supported yet.");
    }

	@Override
	public IMetadata updateWithMinorChange(IELO elo) {
		throw new UnsupportedOperationException("Not yet implemented...");
	}

   private void clearTemplateIfNeeded(IELO elo)
   {
      if (elo.getMetadata().metadataKeyExists(templateKey)){
         final String templateValue = (String) elo.getMetadata().getMetadataValueContainer(templateKey).getValue();
         if (Boolean.valueOf(templateValue)){
            if ("TRUE".equals(templateValue)){
               elo.getMetadata().getMetadataValueContainer(templateKey).setValue("false");
            }
            else{
               elo.getMetadata().getMetadataValueContainer(templateKey).setValue("TRUE");
            }
         }
      }
   }

    @Override
    public int getHits(IQuery query) {
        return repository.getHits(query);
    }

    private void throwExceptionIfOnEDT() {
        if (edtCheck && EventQueue.isDispatchThread()) {
            try {
                throw new RuntimeException("ROOLO ACCESS ON EDT IS FORBIDDEN!");
            } catch (RuntimeException e) {
                System.err.println("ROOLO ACCESS ON EDT IS FORBIDDEN!\n"
                                 + "RoOLO is a remote component. If you ever want to access it,\n"
                                 + "do it on an extra thread, as access on the EDT will freeze\n"
                                 + "the UI and make SCY-Lab look crashed for the user. This will\n"
                                 + "create a very bad user experience and users will not like it.\n"
                                 + "You can use FX.deferAction() for \"fire and forget\" operations\n"
                                 + "or XFX.runActionInBackgroundAndCallBack(f, f) to pass a\n"
                                 + "function into the background and get a callback on the EDT\n"
                                 + "to complete the result. You should always give feedback to\n"
                                 + "the user, that something is happening by indicating progress.\n"
                                 + "The simple solution is to use ProgressOverlay.startShowWorking();\n"
                                 + "and ProgressOverlay.stopShowWorking();. From now on, no excuses\n"
                                 + "for a laggy UI!");
                e.printStackTrace();
            }
        }
    }
}
