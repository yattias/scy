/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.common.mission;

import java.net.URI;

/**
 *
 * @author SikkenJ
 */
public interface MissionRuntimeEloContent {
   public URI getMissionSpecificationEloUri();
   public URI getMissionMapModelEloUri();
   public URI getEloToolConfigsEloUri();
}
