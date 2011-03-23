/*
 * BasicContentCreatorRegistryFX.fx
 *
 * Created on 24-sep-2009, 9:21:33
 */

package eu.scy.client.desktop.scydesktop.elofactory.impl;

import java.lang.IllegalArgumentException;
import java.util.HashMap;
import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;

/**
 * @author sikkenj
 */

// place your code here

public class BasicContentCreatorRegistryFX{
   def logger = Logger.getLogger(this.getClass());

   def contentCreatorsFXMap = new HashMap();

   protected function registerContentCreatorFX(contentCreator: Object, id: String):Void{
//      logger.info("registering ContentCreatorFX with id {id}, class {contentCreator.getClass()}");
      checkIfIdIsDefined(id);
      contentCreatorsFXMap.put(id.toLowerCase(), contentCreator);
   }

   function checkIfIdIsDefined(id:String){
      if (idUsed(id)){
         throw new IllegalArgumentException("id {id} is already defined");
      }
   }

   function idUsed(id:String):Boolean{
      return getContentCreatorFX(id)!=null;
   }

   protected function getContentCreatorFX(id:String):Object{
      return contentCreatorsFXMap.get(id.toLowerCase())
   }


}
