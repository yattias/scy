/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.elobrowser.tbi_hack;

import eu.scy.elobrowser.main.user.User;
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
import roolo.elo.metadata.keys.Contribute;

/**
 *
 * @author sikkenj
 */
public class AddGeneralMetadataRepositoryWrapper<K extends IMetadataKey> implements IRepository<IELO<K>, K>
{

	private final static Logger logger = Logger.getLogger(AddGeneralMetadataRepositoryWrapper.class);
	private final static String meloType = "scy/melo";
	private String userId;
	private URI anchorEloUri;
	private IRepository<IELO<K>, K> repository;
	private IMetadataTypeManager<K> metadataTypeManager;
	private boolean sendEloSavedEvents = false;
	private K uriKey;
	private K authorKey;
	private K typeKey;
	private K annotatesRelationKey;
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

	private void sendEloSavedEvent(URI eloURI)
	{
		if (sendEloSavedEvents)
		{
			for (EloSavedListener eloSavedListener : eloSavedListeners)
			{
				eloSavedListener.eloSaved(eloURI);
			}
		}
	}

	public void setAnchorEloUri(URI anchorEloUri)
	{
		this.anchorEloUri = anchorEloUri;
	}

	public void setRepository(IRepository<IELO<K>, K> repository)
	{
		this.repository = repository;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public void setMetadataTypeManager(IMetadataTypeManager<K> metadataTypeManager)
	{
		this.metadataTypeManager = metadataTypeManager;
		uriKey = getMetadataKey("uri");
		authorKey = getMetadataKey("author");
		typeKey = getMetadataKey("type");
		annotatesRelationKey = getMetadataKey("annotates");
//		  System.out.println("AddGeneralMetadataRepositoryWrapper found:\n- authorKey: " + authorKey + "\n-annotatesRelationKey: " + annotatesRelationKey);
	}

	private K getMetadataKey(String keyName)
	{
		K key = metadataTypeManager.getMetadataKey(keyName);
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

	private void addGeneralMetadata(IELO<K> elo)
	{
		if (StringUtils.hasLength(userId) && authorKey != null)
		{
			elo.getMetadata().getMetadataValueContainer(authorKey).setValue(new Contribute(userId, System.currentTimeMillis()));
		}
		else
		{
			elo.getMetadata().getMetadataValueContainer(authorKey).setValue(new Contribute(User.instance.getUsername(), System.currentTimeMillis()));
		}
		boolean addAnnotesRelation = true;
		Object eloType = elo.getMetadata().getMetadataValueContainer(authorKey).getValue();
		addAnnotesRelation = meloType.equals(eloType);
		if (addAnnotesRelation && anchorEloUri != null && annotatesRelationKey != null)
		{
			elo.getMetadata().getMetadataValueContainer(annotatesRelationKey).setValue(anchorEloUri);
		}
	}

	@Override
	public IMetadata<K> addELO(IELO<K> elo)
	{
		addGeneralMetadata(elo);
		IMetadata<K> metadata = repository.addELO(elo);
		if (uriKey != null)
		{
			sendEloSavedEvent((URI) metadata.getMetadataValueContainer(uriKey).getValue());
		}
		return metadata;
	}

	@Override
	public IELO<K> retrieveELO(URI arg0)
	{
		return repository.retrieveELO(arg0);
	}

	@Override
	public void deleteELO(URI arg0)
	{
		repository.deleteELO(arg0);
	}

	@Override
	public IMetadata<K> updateELO(IELO<K> elo)
	{
		addGeneralMetadata(elo);
		return repository.updateELO(elo);
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
	public IMetadata<K> retrieveMetadata(URI arg0)
	{
		return repository.retrieveMetadata(arg0);
	}

	@Override
	public void addMetadata(URI arg0, IMetadata<K> arg1)
	{
		repository.addMetadata(arg0, arg1);
	}
}
