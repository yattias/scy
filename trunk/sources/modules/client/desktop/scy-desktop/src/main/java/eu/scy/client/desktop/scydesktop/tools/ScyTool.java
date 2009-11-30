/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools;

import java.net.URI;

/**
 *
 * @author sikken
 */
public interface ScyTool
{
   public void initialize();

   public void newElo();
   public void loadElo(URI eloUri);

   public void onGotFocus();
   public void onLostFocus();

   public void onMinimized();
   public void onUnMinimized();

   public void onClosed();

   public void setEloSaver(EloSaver efzloSaver);

   public void setMyEloChanged(MyEloChanged myEloChanged);
}
