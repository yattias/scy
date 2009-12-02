/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.hacks;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.log4j.Logger;
import roolo.api.IRepository;
import roolo.api.search.IQuery;
import roolo.api.search.ISearchResult;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;

/**
 *
 * @author sikkenj
 */
public class RepositoryWrapper implements IRepository
{

	private final static Logger logger = Logger.getLogger(RepositoryWrapper.class);
//	private final static String meloType = "scy/melo";
	private String userId;
	private URI anchorEloUri;
	private IRepository repository;
	private IMetadataTypeManager metadataTypeManager;
	private boolean sendEloSavedEvents = false;
	private IMetadataKey uriKey;
//	private IMetadataKey authorKey;
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

	public void setMetadataTypeManager(IMetadataTypeManager metadataTypeManager)
	{
		this.metadataTypeManager = metadataTypeManager;
		uriKey = getMetadataKey("identifier");
//		authorKey = getMetadataKey("author");
//		typeKey = getMetadataKey("type");
//		annotatesRelationKey = getMetadataKey("annotates");
//		  System.out.println("AddGeneralMetadataRepositoryWrapper found:\n- authorKey: " + authorKey + "\n-annotatesRelationKey: " + annotatesRelationKey);
	}

	private IMetadataKey getMetadataKey(String keyName)
	{
		IMetadataKey key = metadataTypeManager.getMetadataKey(keyName);
		if (key == null)
		{
			logger.error("Couldn't get metadata key named: " + keyName);
		}
		return key;
	}

	public void setSendEloSavedEvents(boolean sendEloSavedEvents)
	{
		this.sendEloSavedEvents = sendEloSavedEvents;
	}

	private void addGeneralMetadata(IELO elo)
	{
//		if (StringUtils.hasLength(userId) && authorKey != null)
//		{
//			elo.getMetadata().getMetadataValueContainer(authorKey).setValue(new Contribute(userId, System.currentTimeMillis()));
//		}
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
		IMetadata metadata = repository.addForkedELO(elo,parentUri);
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
