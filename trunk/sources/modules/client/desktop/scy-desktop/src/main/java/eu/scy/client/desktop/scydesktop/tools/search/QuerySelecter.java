/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.search;

import eu.scy.common.scyelo.ScyElo;
import java.util.List;
import org.jdom.Element;
import roolo.search.IQuery;

/**
 *
 * @author SikkenJ
 */
public interface QuerySelecter
{

   public String getId();

   public void setBasedOnElo(ScyElo elo);

   public String getEloIconName();

   public List<String> getDisplayOptions();

   public String getSelectedOption();

   public void setSelectedOption(String option);

   public void addState(Element xml);

   public void setState(Element xml);

   public void setFilterOptions(IQuery query);

   public void setAuthorMode(boolean authorMode);

   public void setDebugMode(boolean debugMode);
}
