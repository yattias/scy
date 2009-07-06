/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.test;

import eu.scy.client.desktop.scydesktop.missionmap.Anchor;
import eu.scy.client.desktop.scydesktop.missionmap.BasicAnchor;
import eu.scy.client.desktop.scydesktop.missionmap.BasicMissionModel;
import eu.scy.client.desktop.scydesktop.missionmap.MissionModel;
import eu.scy.client.desktop.scydesktop.missionmap.MissionModelCreator;
import java.awt.Color;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sikkenj
 */
public class TestMissionModelCreator implements MissionModelCreator
{

   private final String baseUri = "test://anchors/";
   private final String defaultExtention = ".test";
   private int anchorCount = 0;

   @Override
   public MissionModel createMissionModel()
   {
      try
      {
         List<Anchor> anchors = createAnchors();
         BasicMissionModel missionModel = new BasicMissionModel(anchors, anchors.get(0));
         return missionModel;
      }
      catch (Exception ex)
      {
         throw new IllegalStateException("failed to create mission model", ex);
      }
   }

   private List<Anchor> createAnchors() throws URISyntaxException
   {
      List<Anchor> anchors = new ArrayList<Anchor>();
      anchors.add(createBasicAnchor("black", Color.BLACK, 0, 0));
      anchors.add(createBasicAnchor("green",".tst", Color.GREEN, 40, 0));
      anchors.add(createBasicAnchor("blue", Color.BLUE, 0, 40));
      anchors.add(createBasicAnchor("red", Color.RED, 40, 40));
      return anchors;
   }

   private BasicAnchor createBasicAnchor(String title, String extention, Color color, float xPos, float yPos) throws URISyntaxException
   {
      return new BasicAnchor(new URI(baseUri + (anchorCount++) + "/" + title + extention), title, color, xPos, yPos, null, null);
   }

   private BasicAnchor createBasicAnchor(String title, Color color, float xPos, float yPos) throws URISyntaxException
   {
      return createBasicAnchor(title, defaultExtention, color, xPos, yPos);
   }
}
