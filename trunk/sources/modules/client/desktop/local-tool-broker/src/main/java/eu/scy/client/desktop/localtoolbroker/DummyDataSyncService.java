package eu.scy.client.desktop.localtoolbroker;

import eu.scy.client.common.datasync.CollaboratorStatusListener;
import eu.scy.client.common.datasync.IDataSyncService;
import eu.scy.client.common.datasync.ISyncListener;
import eu.scy.client.common.datasync.ISyncSession;
import eu.scy.common.datasync.ISyncObject;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author sikken
 */
public class DummyDataSyncService implements IDataSyncService {

   private class DummySyncSession implements ISyncSession {
      private final String id = "" + System.currentTimeMillis();

      @Override
      public void addSyncListener(ISyncListener il) {
      }

      @Override
      public void addSyncObject(ISyncObject iso) {
      }

      @Override
      public void changeSyncObject(ISyncObject iso) {
      }

      @Override
      public List<ISyncObject> getAllSyncObjects() {
         return new ArrayList<ISyncObject>();
      }

      @Override
      public String getId() {
         return id;
      }

      @Override
      public ISyncObject getSyncObject(String string) {
         return null;
      }

      @Override
      public void removeSyncListener(ISyncListener il) {
      }

      @Override
      public void removeSyncObject(ISyncObject iso) {
      }

      @Override
      public List<ISyncObject> getAllSyncObjects(int time, TimeUnit unit) {
         return new LinkedList<ISyncObject>();
      }

      @Override
      public void leaveSession() {
      }

      @Override
      public String getUsername()
      {
         return userName;
      }

		@Override
		public void addCollaboratorStatusListener(CollaboratorStatusListener listener)
		{
		}

		@Override
		public void removeCollaboratorStatusListener(CollaboratorStatusListener listener)
		{
		}

   }
   
   private String userName;
   
   public void setUserName(String userName)
   {
      this.userName = userName;
   }

   @Override
   public ISyncSession createSession(ISyncListener il) {
      return new DummySyncSession();
   }

   @Override
   public ISyncSession joinSession(String string, ISyncListener il) {
      return new DummySyncSession();
   }

   @Override
   public ISyncSession createSession(ISyncListener listener, String toolid) {
      return new DummySyncSession();
   }

   @Override
   public ISyncSession joinSession(String mucID, ISyncListener iSyncListener, String toolid) {
      return new DummySyncSession();
   }

   @Override
   public ISyncSession joinSession(String mucID, ISyncListener iSyncListener, String toolid, boolean fetchState) {
      return new DummySyncSession();
   }

}
