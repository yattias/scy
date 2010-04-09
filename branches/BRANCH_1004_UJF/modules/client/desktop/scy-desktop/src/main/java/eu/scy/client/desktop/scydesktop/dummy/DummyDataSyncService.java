/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.dummy;

import eu.scy.client.common.datasync.IDataSyncService;
import eu.scy.client.common.datasync.ISyncListener;
import eu.scy.client.common.datasync.ISyncSession;
import eu.scy.common.datasync.ISyncObject;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sikken
 */
public class DummyDataSyncService implements IDataSyncService {

   private class DummySyncSession implements ISyncSession{
      private final String id = "" + System.currentTimeMillis();

      @Override
      public void addSyncListener(ISyncListener il)
      {
      }

      @Override
      public void addSyncObject(ISyncObject iso)
      {
      }

      @Override
      public void changeSyncObject(ISyncObject iso)
      {
      }

      @Override
      public List<ISyncObject> getAllSyncObjects()
      {
         return new ArrayList<ISyncObject>();
      }

      @Override
      public String getId()
      {
         return id;
      }

      @Override
      public ISyncObject getSyncObject(String string)
      {
         return null;
      }

      @Override
      public void removeSyncListener(ISyncListener il)
      {
      }

      @Override
      public void removeSyncObject(ISyncObject iso)
      {
      }

   }

   @Override
   public ISyncSession createSession(ISyncListener il) throws Exception
   {
      return new DummySyncSession();
   }

   @Override
   public ISyncSession joinSession(String string, ISyncListener il)
   {
      return new DummySyncSession();
   }

   @Override
   public void leaveSession(ISyncSession iss, ISyncListener il)
   {
   }

}
