/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.utils;

import java.lang.Class;
import java.lang.NoSuchMethodException;
import org.apache.log4j.Logger;
import javafx.reflect.FXClassType;
import javafx.reflect.FXLocal;
import javafx.reflect.FXType;
import javafx.reflect.FXVarMember;
import java.lang.Exception;

/**
 * @author SikkenJ
 */
public class InjectObjectsUtils {

}
def logger = Logger.getLogger(InjectObjectsUtils.class);

public function injectObjectIfWantedJava(object: Object, serviceClass: Class, propertyName: String, service: Object):Void {
   try {
      var setterName = "set{propertyName.substring(0, 1).toUpperCase()}{propertyName.substring(1)}";
      var setServiceMethod = object.getClass().getMethod(setterName, serviceClass);
      setServiceMethod.invoke(object, service);
      logger.info("injected {serviceClass.getName()} into object {object}");
   }
   catch (e: NoSuchMethodException) {
      // no service setter method
      logger.info("object ({object.getClass().getName()}) does not have method set{serviceClass.getSimpleName()}");
   }
}

public function injectObjectIfWantedFX(object: Object, serviceClass: Class, propertyName: String, service: Object):Void {
   var context: FXLocal.Context = FXLocal.getContext();
   var objectValue: FXLocal.ObjectValue = new FXLocal.ObjectValue(object, context);
   var cls: FXClassType = objectValue.getClassType();
   if (not cls.isJfxType() and not cls.isMixin()) {
      // object is not a javafx object
      //println("not a javafx object: {object.getClass()}");
      return;
   }
   var varRef: FXVarMember = cls.getVariable(propertyName);
   if (varRef != null) {
      var type: FXType = varRef.getType();
      //logger.info("there is a variable {propertyName} of type {type.getName()} in {object.getClass().getName()}");
      try {
         varRef.setValue(objectValue, context.mirrorOf(service));
         logger.info("the variable {propertyName} in {object.getClass().getName()} is set");
      }
      catch (e: Exception) {
         logger.error("failed to set FX property {propertyName} of type {type.getName()} in {object.getClass().getName()}, with type {service.getClass()}, error {e.getMessage()}");
      //            logger.error("failed to set FX property {propertyName} of type {type.getName()} in {object.getClass().getName()}, with type {service.getClass()}", e);
      }
   } else {
      logger.info("no variable {propertyName} in {object.getClass().getName()}");
   }
}
