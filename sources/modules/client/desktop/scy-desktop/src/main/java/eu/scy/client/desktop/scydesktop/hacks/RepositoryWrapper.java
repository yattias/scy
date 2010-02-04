/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.hacks;

import eu.scy.client.desktop.scydesktop.ScyRooloMetadataKeyIds;
import java.net.URI;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;
import roolo.api.IRepository;
import roolo.api.search.IQuery;
import roolo.api.search.ISearchResult;
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
   private IMetadataKey missionKey;
   private IMetadataKey lasKey;
   private IMetadataKey activeAnchorEloKey;
//	private IMetadataKey typeKey;
//	private IMetadataKey annotatesRelationKey;
   private List<EloSavedListener> eloSavedListeners = new CopyOnWriteArrayList<EloSavedListener>();

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

   public void setMetadataTypeManager(IMetadataTypeManager metadataTypeManager)
   {
      this.metadataTypeManager = metadataTypeManager;
      uriKey = getMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER);
      authorKey = getMetadataKey(CoreRooloMetadataKeyIds.AUTHOR);
      dateCreatedKey = getMetadataKey(CoreRooloMetadataKeyIds.DATE_CREATED);
      dateLastModifiedKey = getMetadataKey(CoreRooloMetadataKeyIds.DATE_LAST_MODIFIED);
      missionKey = getMetadataKey(ScyRooloMetadataKeyIds.MISSION);
      lasKey = getMetadataKey(ScyRooloMetadataKeyIds.LAS);
      activeAnchorEloKey = getMetadataKey(ScyRooloMetadataKeyIds.ACTIVE_ANCHOR_ELO);
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
      if (StringUtils.hasLength(userId) && authorKey != null)
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
         elo.getMetadata().getMetadataValueContainer(missionKey).setValue(missionId);
      }
      if (lasId != null)
      {
         elo.getMetadata().getMetadataValueContainer(missionKey).setValue(lasId);
      }
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

   @Override
   public IMetadata addNewELO(IELO elo)
   {
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
      return repository.retrieveELO(arg0);
   }

   @Override
   public void deleteELO(URI arg0)
   {
      repository.deleteELO(arg0);
   }

   @Override
   public IMetadata updateELO(IELO elo)
   {
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
      repository.archiveELO(arg0);
   }

   @Override
   public void unarchiveELO(URI arg0)
   {
      repository.unarchiveELO(arg0);
   }

   @Override
   public List<ISearchResult> search(IQuery arg0)
   {
      return repository.search(arg0);
   }

   @Override
   public IMetadata retrieveMetadata(URI arg0)
   {
      return repository.retrieveMetadata(arg0);
   }

   @Override
   public void addMetadata(URI arg0, IMetadata arg1)
   {
      repository.addMetadata(arg0, arg1);
   }

   @Override
   public IELO retrieveELOLastVersion(URI arg0)
   {
      return repository.retrieveELOLastVersion(arg0);
   }

   @Override
   public IELO retrieveELOFirstVersion(URI arg0)
   {
      return repository.retrieveELOFirstVersion(arg0);
   }

   @Override
   public IMetadata retrieveMetadataLastVersion(URI arg0)
   {
      return repository.retrieveMetadataLastVersion(arg0);
   }

   @Override
   public IMetadata retrieveMetadataFirstVersion(URI arg0)
   {
      return repository.retrieveMetadataFirstVersion(arg0);
   }

   @Override
   public List<IELO> retrieveELOAllVersions(URI uri)
   {
      return repository.retrieveELOAllVersions(uri);
   }

   @Override
   public List<IELO> retrieveELOs(List<URI> uris)
   {
      return repository.retrieveELOs(uris);
   }

   @Override
   public List<IMetadata> retrieveMetadataAllVersions(URI uri)
   {
      return repository.retrieveMetadataAllVersions(uri);
   }

   @Override
   public List<IMetadata> retrieveMetadatas(List<URI> uris)
   {
      return repository.retrieveMetadatas(uris);
   }
}
