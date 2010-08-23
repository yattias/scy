/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.common.mission;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author SikkenJ
 */
public enum EloLogicalRole {
   CONCET_MAP, DATASET, STRUCTURED_TEXT, DRAWING, PLAIN_TEXT, RICH_TEXT, RICH_TEXT_WITH_IMAGES,PRESENTATION, TABLE, INFORMATION;

   public List<EloLogicalRole> convertToEloLogicalRoles(List<String> values){
      List<EloLogicalRole> eloLogicalRoles = new ArrayList<EloLogicalRole>();
      for (String value : values){
         eloLogicalRoles.add(EloLogicalRole.valueOf(value));
      }
      return eloLogicalRoles;
   }
}
