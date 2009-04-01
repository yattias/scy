/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.elobrowser.tbi_hack;

import eu.scy.elobrowser.main.user.User;
import java.net.URI;
import java.util.List;
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
public class AddGeneralMetadataRepositoryWrapper<K extends IMetadataKey> implements IRepository<IELO<K>, K>{
	 private String userId;
	 private URI anchorEloUri;
	 private IRepository<IELO<K>, K> repository;
	 private IMetadataTypeManager<K> metadataTypeManager;
	 private K authorKey;
	 private K annotatesRelationKey;

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
		  authorKey = metadataTypeManager.getMetadataKey("author");
		  annotatesRelationKey = metadataTypeManager.getMetadataKey("annotates");
	 }

	 private void addGeneralMetadata(IELO<K> elo)
	 {
		  if (StringUtils.hasLength(userId))
				elo.getMetadata().getMetadataValueContainer(authorKey).setValue(new Contribute(userId,System.currentTimeMillis()));
		  else
				elo.getMetadata().getMetadataValueContainer(authorKey).setValue(new Contribute(User.instance.getUsername(),System.currentTimeMillis()));
		  if (anchorEloUri!=null)
				elo.getMetadata().getMetadataValueContainer(annotatesRelationKey).setValue(anchorEloUri);
	 }

	 @Override
	 public IMetadata<K> addELO(IELO<K> elo)
	 {
		  addGeneralMetadata(elo);
		  return repository.addELO(elo);
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
