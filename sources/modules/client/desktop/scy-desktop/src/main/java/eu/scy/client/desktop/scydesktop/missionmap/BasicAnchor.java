/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.missionmap;

import java.awt.Color;
import java.net.URI;
import java.util.List;

/**
 *
 * @author sikkenj
 */
public class BasicAnchor implements Anchor
{

   public String title = "?";
   public Color color = Color.LIGHT_GRAY;
   public float xPos = 0.0F;
   public float yPos = 0.0F;
   public List<Anchor> nextAnchors;
   public URI eloUr;
   public List<String> relationNames;

   public BasicAnchor(URI eloUr, String title, Color color, float xPos, float yPos, List<Anchor> nextAnchors, List<String> relationNames)
   {
      this.eloUr = eloUr;
      this.title = title;
      this.color = color;
      this.xPos = xPos;
      this.yPos = yPos;
      this.nextAnchors = nextAnchors;
      this.relationNames = relationNames;
   }

   @Override
   public Color getColor()
   {
      return color;
   }

   @Override
   public URI getEloUr()
   {
      return eloUr;
   }

   @Override
   public List<Anchor> getNextAnchors()
   {
      return nextAnchors;
   }

   @Override
   public List<String> getRelationNames()
   {
      return relationNames;
   }

   @Override
   public String getTitle()
   {
      return title;
   }

   @Override
   public float getXPos()
   {
      return xPos;
   }

   @Override
   public float getYPos()
   {
      return yPos;
   }
}
