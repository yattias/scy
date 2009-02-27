package eu.scy.collaborationservice;

import eu.scy.core.model.impl.ScyBaseObject;



public interface ICollaborationService {

  public void create(ScyBaseObject scyBaseObject);
  public void read(String id);
  public void update(ScyBaseObject scyBaseObject);
  public void delete(ScyBaseObject scyBaseObject);
  
}
