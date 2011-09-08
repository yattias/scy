/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.search;

import eu.scy.client.desktop.scydesktop.corners.elomanagement.ScySearchResult;
import java.util.List;

/**
 *
 * @author SikkenJ
 */
public interface QuerySearchFinished
{

   public void querySearchFinished(List<ScySearchResult> scySearchResults);
}
