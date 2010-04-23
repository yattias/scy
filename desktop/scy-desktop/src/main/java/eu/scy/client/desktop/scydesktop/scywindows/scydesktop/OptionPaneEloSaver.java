/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.scywindows.scydesktop;

import eu.scy.client.desktop.scydesktop.tools.EloSaver;
import eu.scy.client.desktop.scydesktop.tools.EloSaverCallBack;
import eu.scy.client.desktop.scydesktop.tools.MyEloChanged;
import java.net.URI;
import javax.swing.JOptionPane;
import org.springframework.util.StringUtils;
import roolo.api.IRepository;
import roolo.elo.api.IELO;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;

/**
 *
 * @author sikken
 */
public class OptionPaneEloSaver implements EloSaver
{
   private MyEloChanged myEloChanged;
   private IRepository repository;
   private IELOFactory eloFactory;
   private IMetadataKey titleKey;

   public void setMyEloChanged(MyEloChanged myEloChanged)
   {
      this.myEloChanged = myEloChanged;
   }

   public void setEloFactory(IELOFactory eloFactory)
   {
      this.eloFactory = eloFactory;
   }

   public void setRepository(IRepository repository)
   {
      this.repository = repository;
   }

   public void setTitleKey(IMetadataKey titleKey)
   {
      this.titleKey = titleKey;
   }

   @Override
   public void eloSaveAs(IELO elo, EloSaverCallBack eloSaverCallBack)
   {
      String currentEloTitle = (String) elo.getMetadata().getMetadataValueContainer(titleKey).getValue();
      String newEloTitle = JOptionPane.showInputDialog("Enter title:", currentEloTitle);
      if (StringUtils.hasText(newEloTitle))
      {
         elo.getMetadata().getMetadataValueContainer(titleKey).setValue(newEloTitle);
         if (elo.getUri() != null)
         {
            eloFactory.detachELO(elo);
         }
         IMetadata newMetadata = repository.addNewELO(elo);
         eloFactory.updateELOWithResult(elo, newMetadata);
         myEloChanged.myEloChanged(elo);
         if (eloSaverCallBack!=null){
            eloSaverCallBack.eloSaved(elo);
         }
         return;
      }
      eloSaverCallBack.eloSaveCancelled(elo);
   }

   @Override
   public void eloUpdate(IELO elo, EloSaverCallBack eloSaverCallBack)
   {
      if (elo.getUri() != null)
      {
         URI oldUri = elo.getUri();
         IMetadata newMetadata = repository.updateELO(elo);
         eloFactory.updateELOWithResult(elo, newMetadata);
            myEloChanged.myEloChanged(elo);
         if (eloSaverCallBack!=null){
            eloSaverCallBack.eloSaved(elo);
         }
      }
      else
      {
         eloSaveAs(elo,eloSaverCallBack);
      }
   }

   @Override
   public void otherEloSaveAs(IELO elo, EloSaverCallBack eloSaverCallBack)
   {
      String currentEloTitle = (String) elo.getMetadata().getMetadataValueContainer(titleKey).getValue();
      String newEloTitle = JOptionPane.showInputDialog("Enter title:", currentEloTitle);
      if (StringUtils.hasText(newEloTitle))
      {
         elo.getMetadata().getMetadataValueContainer(titleKey).setValue(newEloTitle);
         if (elo.getUri() != null)
         {
            eloFactory.detachELO(elo);
         }
         IMetadata newMetadata = repository.addNewELO(elo);
         eloFactory.updateELOWithResult(elo, newMetadata);
         if (eloSaverCallBack!=null){
            eloSaverCallBack.eloSaved(elo);
         }
         return;
      }
      eloSaverCallBack.eloSaveCancelled(elo);
   }

}
