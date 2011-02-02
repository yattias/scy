/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.corners.elomanagement;

import eu.scy.common.scyelo.ScyElo;
import java.util.List;
import roolo.search.ISearchResult;

/**
 *
 * @author SikkenJ
 */
public interface EloBasedSearcher
{

   public String getDisplayId();

   public List<ISearchResult> findElos(ScyElo scyElo);
}
