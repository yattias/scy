package eu.scy.common.mission.impl;

import java.net.URI;
import java.util.List;

import eu.scy.common.mission.Las;
import eu.scy.common.mission.MissionModelEloContent;

public class BasicMissionModelEloContent implements MissionModelEloContent
{
   private List<URI> loEloUris;
   private List<Las> lasses;
   private Las selectedLas;
   private String name;

   @Override
   public List<URI> getLoEloUris()
   {
      return loEloUris;
   }
   
   @Override
   public List<Las> getLasses()
   {
      return lasses;
   }

   @Override
   public Las getSelectedLas()
   {
      return selectedLas;
   }

   @Override
   public void setSelectedLas(Las selectedLas)
   {
      this.selectedLas = selectedLas;
   }

   public void setLoEloUris(List<URI> loEloUris)
   {
      this.loEloUris = loEloUris;
   }

   public void setLasses(List<Las> lasses)
   {
      this.lasses = lasses;
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   
}
