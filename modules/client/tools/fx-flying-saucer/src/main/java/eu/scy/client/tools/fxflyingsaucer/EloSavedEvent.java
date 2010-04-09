/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxflyingsaucer;

import java.util.EventObject;
import roolo.elo.api.IELO;

/**
 *
 * @author sikken
 */
public class EloSavedEvent  extends EventObject
{
	private static final long serialVersionUID = -7389076400870695128L;

	private IELO elo;
   private String title;

	public EloSavedEvent(Object source, IELO elo, String title)
	{
		super(source);
		this.elo = elo;
		this.title = title;
	}

   public IELO getElo()
   {
      return elo;
   }

   public String getTitle()
   {
      return title;
   }

}
