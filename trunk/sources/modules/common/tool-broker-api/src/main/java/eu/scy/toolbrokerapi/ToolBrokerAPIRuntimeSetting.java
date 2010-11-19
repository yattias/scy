package eu.scy.toolbrokerapi;

import java.net.URI;

/**
 *
 * @author sikken, bollen
 */
public interface ToolBrokerAPIRuntimeSetting {

   @Deprecated
   public void setMissionId(String missionId);

   public void setMissionRuntimeURI(URI missionRuntimeURI);
   
   public void setMissionSpecificationURI(URI missionSpecificationURI);
}
