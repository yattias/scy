/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.common.mission.impl;

import java.net.URI;
import java.util.List;

import eu.scy.common.mission.TemplateElosEloContent;

/**
 *
 * @author SikkenJ
 */
public class BasicTemplateElosEloContent implements TemplateElosEloContent {
   private List<URI> templateEloUris;

   @Override
   public String toString()
   {
      return "BasicTemplateElosEloContent{" + "templateEloUris=" + templateEloUris + '}';
   }

   @Override
   public List<URI> getTemplateEloUris()
   {
      return templateEloUris;
   }

   @Override
   public void setTemplateEloUris(List<URI> templateEloUris)
   {
      this.templateEloUris = templateEloUris;
   }

}
